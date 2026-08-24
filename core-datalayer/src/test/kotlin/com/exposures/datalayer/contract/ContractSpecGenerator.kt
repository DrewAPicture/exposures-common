package com.exposures.datalayer.contract

import com.exposures.datalayer.DataLayerPaths
import com.exposures.datalayer.dto.CameraBodyDto
import com.exposures.datalayer.dto.CaptureResultCommand
import com.exposures.datalayer.dto.CompleteFilmMediumCommand
import com.exposures.datalayer.dto.CreateExposureAckCommand
import com.exposures.datalayer.dto.CreateExposureCommand
import com.exposures.datalayer.dto.ExposureDto
import com.exposures.datalayer.dto.FilmBackDto
import com.exposures.datalayer.dto.FilmMediumDto
import com.exposures.datalayer.dto.LensDto
import com.exposures.datalayer.dto.LightMeterDto
import com.exposures.datalayer.dto.PhotoStatusDto
import com.exposures.datalayer.dto.ShutterSpeedDto
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlinx.serialization.serializer
import java.lang.reflect.Modifier

/**
 * Renders the machine-readable Data Layer contract spec from [DataLayerPaths] and kotlinx
 * serialization descriptors. Checked in at `docs/contracts/data-layer.json` and compared in
 * [ContractSpecDriftTest] so path/DTO drift fails CI before merge.
 */
object ContractSpecGenerator {

    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
    }

    private data class PathMeta(
        val kind: String,
        val writer: String? = null,
        val reader: String? = null,
        val payloadSchema: String? = null,
        val payloadCardinality: String? = null,
        val notes: String? = null,
    )

    private val pathMeta = mapOf(
        "CAMERA_BODIES" to PathMeta("dataItem", "phone", "watch", "CameraBodyDto", "array"),
        "LENSES" to PathMeta("dataItem", "phone", "watch", "LensDto", "array"),
        "LIGHT_METERS" to PathMeta("dataItem", "phone", "watch", "LightMeterDto", "array"),
        "FILM_BACKS" to PathMeta("dataItem", "phone", "watch", "FilmBackDto", "array"),
        "FILM_MEDIA" to PathMeta("dataItem", "phone", "watch", "FilmMediumDto", "array"),
        "EXPOSURES" to PathMeta("dataItem", "watch", "phone", "ExposureDto", "array"),
        "PHOTO_STATUSES" to PathMeta("dataItem", "phone", "watch", "PhotoStatusDto", "array"),
        "CAPTURE_RESULT_COMMAND" to PathMeta(
            kind = "command",
            writer = "phone",
            reader = "watch",
            payloadSchema = "CaptureResultCommand",
            payloadCardinality = "object",
        ),
        "COMPLETE_FILM_MEDIUM_COMMAND" to PathMeta(
            kind = "command",
            writer = "watch",
            reader = "phone",
            payloadSchema = "CompleteFilmMediumCommand",
            payloadCardinality = "object",
        ),
        "REQUEST_FILM_MEDIA_SYNC_COMMAND" to PathMeta(
            kind = "command",
            writer = "watch",
            reader = "phone",
            notes = "empty payload; phone responds by pushing current equipment/film media DataItems",
        ),
        "CONNECTIVITY_PING_COMMAND" to PathMeta(
            kind = "command",
            writer = "watch",
            reader = "phone",
            notes = "empty payload",
        ),
        "CONNECTIVITY_PING_ACK_COMMAND" to PathMeta(
            kind = "command",
            writer = "phone",
            reader = "watch",
            notes = "empty payload",
        ),
        "CREATE_EXPOSURE_COMMAND" to PathMeta(
            kind = "command",
            writer = "phone",
            reader = "watch",
            payloadSchema = "CreateExposureCommand",
            payloadCardinality = "object",
        ),
        "CREATE_EXPOSURE_ACK_COMMAND" to PathMeta(
            kind = "command",
            writer = "watch",
            reader = "phone",
            payloadSchema = "CreateExposureAckCommand",
            payloadCardinality = "object",
        ),
        "CAPABILITY_EXPOSURES_APP" to PathMeta(
            kind = "capability",
            notes = "both apps advertise this so each side can find the other's connected node",
        ),
        "KEY_PAYLOAD" to PathMeta(
            kind = "payloadKey",
            notes = "DataMap key for the JSON payload; DataMap also carries an updatedAt timestamp " +
                "that is not part of the JSON contract",
        ),
    )

    private val schemaTypes = listOf(
        serializer<ShutterSpeedDto>(),
        serializer<CameraBodyDto>(),
        serializer<LensDto>(),
        serializer<LightMeterDto>(),
        serializer<FilmBackDto>(),
        serializer<FilmMediumDto>(),
        serializer<ExposureDto>(),
        serializer<PhotoStatusDto>(),
        serializer<CaptureResultCommand>(),
        serializer<CompleteFilmMediumCommand>(),
        serializer<CreateExposureCommand>(),
        serializer<CreateExposureAckCommand>(),
    )

    fun render(): String {
        val declaredNames = DataLayerPaths::class.java.declaredFields
            .filter { Modifier.isStatic(it.modifiers) && it.type == String::class.java }
            .map { it.name }
            .toSet()
        check(declaredNames == pathMeta.keys) {
            "Path metadata must cover every DataLayerPaths constant. " +
                "Missing: ${declaredNames - pathMeta.keys}. Extra: ${pathMeta.keys - declaredNames}."
        }

        val spec = buildJsonObject {
            put("contract", "exposures-data-layer")
            put(
                "description",
                "Wear Data Layer contract shared by exposures-phone and exposures-watch. " +
                    "Regenerated from DataLayerPaths and kotlinx.serialization descriptors. " +
                    "Update with ./gradlew :core-datalayer:testDebugUnitTest -PupdateContractSpec",
            )
            putJsonObject("encoding") {
                put("format", "json")
                put("ignoreUnknownKeys", true)
                put("encodeDefaults", false)
            }
            putJsonObject("compatibility") {
                put("policy", "docs/CONTRACT_COMPATIBILITY.md")
                put("evolution", "additive-by-default")
                put(
                    "breaking",
                    "field removal/rename/type-change is a major version with coordinated phone+watch rollout",
                )
                put(
                    "enumAdditions",
                    "allowed only when every consumer handles unknown values without unchecked valueOf",
                )
            }
            putJsonObject("paths") {
                declaredNames.sorted().forEach { name ->
                    val field = DataLayerPaths::class.java.getField(name)
                    val value = field.get(null) as String
                    val meta = pathMeta.getValue(name)
                    putJsonObject(name) {
                        put("value", value)
                        put("kind", meta.kind)
                        meta.writer?.let { put("writer", it) }
                        meta.reader?.let { put("reader", it) }
                        if (meta.payloadSchema != null) {
                            putJsonObject("payload") {
                                put("\$ref", meta.payloadSchema)
                                put("cardinality", meta.payloadCardinality)
                            }
                        }
                        meta.notes?.let { put("notes", it) }
                    }
                }
            }
            putJsonObject("schemas") {
                schemaTypes.forEach { serializer ->
                    val descriptor = serializer.descriptor
                    put(simpleName(descriptor), schemaObject(descriptor))
                }
            }
        }
        return json.encodeToString(JsonElement.serializer(), spec) + "\n"
    }

    private fun schemaObject(descriptor: SerialDescriptor): JsonObject = buildJsonObject {
        put("type", "object")
        put("serialName", descriptor.serialName)
        putJsonArray("fields") {
            for (i in 0 until descriptor.elementsCount) {
                add(fieldObject(descriptor, i))
            }
        }
    }

    private fun fieldObject(parent: SerialDescriptor, index: Int): JsonObject = buildJsonObject {
        val element = parent.getElementDescriptor(index)
        put("name", parent.getElementName(index))
        put("optional", parent.isElementOptional(index))
        put("nullable", element.isNullable)
        when (element.kind) {
            StructureKind.LIST -> {
                put("type", "array")
                put("items", typeRef(element.getElementDescriptor(0)))
            }
            StructureKind.CLASS, StructureKind.OBJECT -> {
                val name = simpleName(element)
                if (schemaTypes.any { simpleName(it.descriptor) == name }) {
                    put("\$ref", name)
                } else {
                    put("type", "object")
                    put("serialName", element.serialName)
                }
            }
            else -> put("type", jsonTypeName(element))
        }
    }

    private fun typeRef(descriptor: SerialDescriptor): JsonObject = buildJsonObject {
        when (descriptor.kind) {
            StructureKind.CLASS, StructureKind.OBJECT -> {
                val name = simpleName(descriptor)
                if (schemaTypes.any { simpleName(it.descriptor) == name }) {
                    put("\$ref", name)
                } else {
                    put("type", "object")
                    put("serialName", descriptor.serialName)
                }
            }
            else -> put("type", jsonTypeName(descriptor))
        }
    }

    private fun jsonTypeName(descriptor: SerialDescriptor): String = when (descriptor.kind) {
        PrimitiveKind.STRING, PrimitiveKind.CHAR -> "string"
        PrimitiveKind.BOOLEAN -> "boolean"
        PrimitiveKind.INT, PrimitiveKind.LONG, PrimitiveKind.SHORT, PrimitiveKind.BYTE -> "integer"
        PrimitiveKind.FLOAT, PrimitiveKind.DOUBLE -> "number"
        else -> descriptor.kind.toString()
    }

    private fun simpleName(descriptor: SerialDescriptor): String =
        descriptor.serialName.substringAfterLast('.')
}
