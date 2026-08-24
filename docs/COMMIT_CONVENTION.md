# Commit Convention

All commit messages MUST be written in **English** and follow the
[Conventional Commits](https://www.conventionalcommits.org/) style described below.
CI reviewers and release tooling rely on this format — treat it as a hard rule, not a suggestion.

## Format

```
<type>(<scope>): <short imperative summary>

[optional body: explains WHY, wraps at 100 chars]

[optional footer(s): BREAKING CHANGE, Refs #123, ...]
```

## Rules

1. **Subject line**
   - Max 72 characters (aim for ≤ 50).
   - Imperative mood: `add`, not `added` / `adds` / `adding`.
   - Lowercase after the colon; **no trailing period**.
   - Describe *what the commit does*, not what you did ("add validator", ~~"added some files"~~).
2. **Scope** (optional): lowercase, one word or short hyphenated token, e.g. `(core)`, `(ci)`, `(infra)`, `(local-dev)`.
3. **Body** (optional): separate with a blank line; explain motivation and context, not mechanics; wrap at 100 characters.
4. **Breaking changes**: mark with `!` before the colon (`feat(api)!: ...`) and describe in a `BREAKING CHANGE:` footer.
5. One logical change per commit. Do not mix unrelated edits.
6. Never commit secrets, keys, or credentials.

## Types

| Type       | Use for                                                        |
|------------|----------------------------------------------------------------|
| `feat`     | New feature or capability                                      |
| `fix`      | Bug fix                                                        |
| `docs`     | Documentation only                                             |
| `refactor` | Code change that neither fixes a bug nor adds a feature        |
| `perf`     | Performance improvement                                        |
| `test`     | Adding or correcting tests                                     |
| `build`    | Build system or dependency changes (Gradle, Dockerfile, libs)  |
| `ci`       | CI/CD pipeline and workflow changes                            |
| `chore`    | Maintenance tasks, repo housekeeping                           |
| `style`    | Formatting, whitespace, naming cosmetics (no logic change)     |
| `revert`   | Reverting an earlier commit                                    |

## Examples

Good:

```
feat(core): add driver registration endpoint

Validates vehicle ownership through core-service internal API before
persisting the driver profile.

Refs BLUE-42
```

```
fix(ci): use correct compose file path in staging deploy
```

```
feat(api)!: replace X-API-TOKEN header with signed JWT

BREAKING CHANGE: clients must migrate to JWT-based authentication
before v1.0.
```

Bad:

```
update stuff              <- vague, no type
fixed the bug finally lol <- wrong tense, no information
feat: add auth and fix db and update docs <- unrelated changes mixed
```

## Branch & Merge Policy

- Long-lived branches: `develop` (staging) → `main` (production).
- Work happens on short-lived branches: `feat/*`, `fix/*`, `chore/*`.
- Open a PR into `develop`; merge with **Squash and merge** so each branch lands as exactly one conventional commit.
- Promote to production by PR from `develop` into `main`, also squash-merged.
- Direct pushes and merge commits on `develop`/`main` are blocked by branch protection — keep history linear.
