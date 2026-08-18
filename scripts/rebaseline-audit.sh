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

# Remaining local core-database files that were never extracted (intentional
# divergence). Informational only — a drift here is expected, not a regression.
declare -a database_remaining_local_files=(
  "core-database/src/main/kotlin/com/exposures/database/mapper/Mappers.kt"
  "core-database/src/main/kotlin/com/exposures/database/entity/ExposureEntity.kt"
  "core-database/src/main/kotlin/com/exposures/database/entity/FilmBackEntity.kt"
  "core-database/src/main/kotlin/com/exposures/database/entity/FilmRollEntity.kt"
)

declare -a extracted_database_files=(
  "core-database/src/main/kotlin/com/exposures/database/Converters.kt"
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

# Post-Phase-4 (exp-shared-library-feasibility-plan.md): phone/watch no longer
# carry local core-model/core-datalayer at all — this repo is the sole source.
# There's nothing left to diff between two copies, so this is now a regression
# guard: fail loudly if either app repo ever reintroduces a local copy
# (accidental revert, bad merge, etc.), since that would silently reopen the
# exact drift this whole extraction existed to close.
#
# Post-Phase-6: the same for the four extracted core-database files now owned
# by core-database-common.
check_no_local_reintroduction() {
  {
    echo "## Local shared-source Reintroduction Guard"
    echo
    echo "| Repo | Path | Status |"
    echo "|---|---|---|"
  } >> "${OUTPUT_PATH}"

  local reintroduced=0
  for repo_dir in "${PHONE_DIR}" "${WATCH_DIR}"; do
    local repo_name
    repo_name="$(basename "${repo_dir}")"
    for module in "core-model" "core-datalayer"; do
      # Check for build.gradle.kts, not just the directory: a gitignored build/
      # output dir can outlive the deleted module (stale Gradle cache) without
      # meaning source was reintroduced.
      if [[ -f "${repo_dir}/${module}/build.gradle.kts" ]]; then
        echo "| \`${repo_name}\` | \`${module}\` | **reintroduced — investigate** |" >> "${OUTPUT_PATH}"
        reintroduced=1
      else
        echo "| \`${repo_name}\` | \`${module}\` | absent (expected) |" >> "${OUTPUT_PATH}"
      fi
    done
    for rel in "${extracted_database_files[@]}"; do
      local short
      short="$(basename "${rel}")"
      if [[ -f "${repo_dir}/${rel}" ]]; then
        echo "| \`${repo_name}\` | \`${short}\` | **reintroduced — investigate** |" >> "${OUTPUT_PATH}"
        reintroduced=1
      else
        echo "| \`${repo_name}\` | \`${short}\` | absent (expected) |" >> "${OUTPUT_PATH}"
      fi
    done
  done
  echo >> "${OUTPUT_PATH}"

  if [[ "${reintroduced}" -eq 1 ]]; then
    echo "WARNING: extracted shared source was reintroduced in phone or watch — see report." >&2
  fi
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

check_no_local_reintroduction
compare_file_group "Database Remaining-Local Candidates (Informational)" "${database_remaining_local_files[@]}"

{
  echo "## Manual follow-up"
  echo
  echo "- Classify each drift as intentional divergence, should-converge, or out-of-scope."
  echo "- Update \`docs/CONTRACT_COMPATIBILITY.md\` if contract/versioning implications changed."
  echo "- Do not remove/replace consumer shared code until rebaseline sign-off is complete."
  echo
} >> "${OUTPUT_PATH}"

echo "Wrote rebaseline report: ${OUTPUT_PATH}"
