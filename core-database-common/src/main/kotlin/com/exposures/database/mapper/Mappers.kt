package com.exposures.database.mapper

import com.exposures.database.entity.CameraBodyEntity
import com.exposures.database.entity.LensEntity
import com.exposures.database.entity.LightMeterEntity
import com.exposures.model.CameraBody
import com.exposures.model.Lens
import com.exposures.model.LightMeter

fun CameraBodyEntity.toDomain() = CameraBody(
    id = id,
    name = name,
    manufacturer = manufacturer,
    availableShutterSpeeds = availableShutterSpeeds,
    hasBulbMode = hasBulbMode,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    remoteId = remoteId,
)

fun CameraBody.toEntity() = CameraBodyEntity(
    id = id,
    name = name,
    manufacturer = manufacturer,
    availableShutterSpeeds = availableShutterSpeeds,
    hasBulbMode = hasBulbMode,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    remoteId = remoteId,
)

fun LensEntity.toDomain() = Lens(
    id = id,
    name = name,
    cameraBodyId = cameraBodyId,
    minAperture = minAperture,
    maxAperture = maxAperture,
    stopIncrement = stopIncrement,
    referencePhotoZoomRatio = referencePhotoZoomRatio,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    remoteId = remoteId,
)

fun Lens.toEntity() = LensEntity(
    id = id,
    name = name,
    cameraBodyId = cameraBodyId,
    minAperture = minAperture,
    maxAperture = maxAperture,
    stopIncrement = stopIncrement,
    referencePhotoZoomRatio = referencePhotoZoomRatio,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    remoteId = remoteId,
)

fun LightMeterEntity.toDomain() = LightMeter(
    id = id,
    name = name,
    manufacturer = manufacturer,
    type = type,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    remoteId = remoteId,
)

fun LightMeter.toEntity() = LightMeterEntity(
    id = id,
    name = name,
    manufacturer = manufacturer,
    type = type,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    remoteId = remoteId,
)
