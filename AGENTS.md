# AGENTS.md

Exposures shared library repo — Kotlin modules consumed by both `exposures-phone` and `exposures-watch`.

## Purpose

Centralize shared model and Wear Data Layer contract/runtime code to prevent manual mirror drift between app repos.

## Commit messages

Follow the [`commit-best-practices`](.claude/skills/commit-best-practices/SKILL.md) skill when writing commit messages.

## Architecture questions

For shared-library boundaries, contract compatibility, and extraction sequencing, use this repo's docs first:

- `README.md`
- `docs/CONTRACT_COMPATIBILITY.md`

For app-specific architecture concerns, defer to the corresponding app repo guidance (`exposures-phone` or `exposures-watch`).

## Kotlin best practices

For Kotlin coding standards (immutability, null safety, naming, idiomatic constructs, concurrency/error handling, formatting), invoke the `kotlin-best-practices` skill (lives in `~/.claude/skills/` — global, not part of this repo).
