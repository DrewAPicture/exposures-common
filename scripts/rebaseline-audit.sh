#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PHONE_DIR="${ROOT_DIR}/../phone"
WATCH_DIR="${ROOT_DIR}/../watch"
DEFAULT_OUTPUT="${ROOT_DIR}/docs/rebaseline/latest-rebaseline-report.md"
OUTPUT_PATH="${DEFAULT_OUTPUT}"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --output)
      OUTPUT_PATH="$2"
      shift 2
      ;;
    *)
      echo "Unknown argument: $1" >&2
      echo "Usage: ./scripts/rebaseline-audit.sh [--output <path>]" >&2
      exit 1
      ;;
  esac
done

if [[ ! -d "${PHONE_DIR}" || ! -d "${WATCH_DIR}" ]]; then
  echo "Expected sibling repos at ${PHONE_DIR} and ${WATCH_DIR}" >&2
  exit 1
fi

mkdir -p "$(dirname "${OUTPUT_PATH}")"

declare -a core_datalayer_files=(
  "core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerPaths.kt"
  "core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerJson.kt"
  "core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerClient.kt"
  "core-datalayer/src/main/kotlin/com/exposures/datalayer/DataLayerGateway.kt"
  "core-datalayer/src/main/kotlin/com/exposures/datalayer/dto/Dtos.kt"
  "core-datalayer/src/main/kotlin/com/exposures/datalayer/mapper/DtoMappers.kt"
)

declare -a core_model_files=(
  "core-model/src/main/kotlin/com/exposures/model/CameraBody.kt"
  "core-model/src/main/kotlin/com/exposures/model/Lens.kt"
  "core-model/src/main/kotlin/com/exposures/model/FilmRoll.kt"
  "core-model/src/main/kotlin/com/exposures/model/Exposure.kt"
  "core-model/src/main/kotlin/com/exposures/model/ShutterSpeed.kt"
  "core-model/src/main/kotlin/com/exposures/model/Zone.kt"
)

declare -a database_candidate_files=(
  "core-database/src/main/kotlin/com/exposures/database/Converters.kt"
  "core-database/src/main/kotlin/com/exposures/database/mapper/Mappers.kt"
  "core-database/src/main/kotlin/com/exposures/database/entity/CameraBodyEntity.kt"
  "core-database/src/main/kotlin/com/exposures/database/entity/LensEntity.kt"
  "core-database/src/main/kotlin/com/exposures/database/entity/LightMeterEntity.kt"
)

compare_file_group() {
  local title="$1"
  shift
  local files=("$@")

  {
    echo "## ${title}"
    echo
    echo "| File | Status |"
    echo "|---|---|"
  } >> "${OUTPUT_PATH}"

  for rel in "${files[@]}"; do
    local phone_file="${PHONE_DIR}/${rel}"
    local watch_file="${WATCH_DIR}/${rel}"
    local status=""

    if [[ ! -f "${phone_file}" || ! -f "${watch_file}" ]]; then
      status="missing in one repo"
    elif cmp -s "${phone_file}" "${watch_file}"; then
      status="identical"
    else
      status="drifted"
    fi

    echo "| \`${rel}\` | ${status} |" >> "${OUTPUT_PATH}"
  done

  echo >> "${OUTPUT_PATH}"
}

{
  echo "# Rebaseline Audit Report"
  echo
  echo "- Generated at: $(date -u +"%Y-%m-%d %H:%M:%S UTC")"
  echo "- Phone repo: \`${PHONE_DIR}\`"
  echo "- Watch repo: \`${WATCH_DIR}\`"
  echo
  echo "Use this report with \`docs/rebaseline/CHECKLIST.md\` to make include/exclude and versioning decisions."
  echo
} > "${OUTPUT_PATH}"

compare_file_group "Core Datalayer Contract Surface" "${core_datalayer_files[@]}"
compare_file_group "Core Model High-Risk Surface" "${core_model_files[@]}"
compare_file_group "Database Common Candidates (Informational)" "${database_candidate_files[@]}"

{
  echo "## Manual follow-up"
  echo
  echo "- Classify each drift as intentional divergence, should-converge, or out-of-scope."
  echo "- Update \`docs/CONTRACT_COMPATIBILITY.md\` if contract/versioning implications changed."
  echo "- Do not remove/replace consumer shared code until rebaseline sign-off is complete."
  echo
} >> "${OUTPUT_PATH}"

echo "Wrote rebaseline report: ${OUTPUT_PATH}"
