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

`docs/CONTRACT_COMPATIBILITY.md` is also the source of truth for the audit-first execution policy (re-audit/rebaseline before consumer replacement work).

For phone app architecture, component ownership, and where to implement phone-side changes, invoke the `describe-phone-architecture` skill (lives in `~/.claude/skills/` — global, not part of this repo, so it stays available regardless of which Exposures repo a session started in).

For watch app architecture, sync model behavior, and where to implement watch-side changes, invoke the `describe-watch-architecture` skill (lives in `~/.claude/skills/` — global, not part of this repo, so it stays available regardless of which Exposures repo a session started in).

## Kotlin best practices

For Kotlin coding standards (immutability, null safety, naming, idiomatic constructs, concurrency/error handling, formatting), invoke the `kotlin-best-practices` skill (lives in `~/.claude/skills/` — global, not part of this repo).
