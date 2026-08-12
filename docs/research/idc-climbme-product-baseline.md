# IDC advisory baseline: ClimbMe V1

This is a cited, owner-authorized IDC advisory input captured on 2026-08-12. It
is not a Dora decision, implementation plan, verification record or release
approval. The decisions it informed are recorded separately in
[`docs/decision-log.yaml`](../decision-log.yaml).

## Confirmed owner direction

- Public web application with self-registration; every account's data is private.
- English UI, PostgreSQL persistence, Railway-ready container deployment and a
  backend/API suitable for a later iPhone client.
- Mandatory V1 areas: Climbing Log, Route Database, Training Log, Dashboard and
  Achievements/annual summary.
- A climbing-log row is one attempt. Required context includes date, location,
  sector, route, grade, style, attempt number, send, notes and the owner-listed
  optional performance fields.
- French grades are accepted for V1.
- The product must be human-readable and low friction, not a heavy coach-only UI.

## Sanitized provenance

| Source | Class | Observed | Supports |
| --- | --- | --- | --- |
| Owner conversation | owner-confirmed input | 2026-08-12 | Product purpose, scope, privacy posture and deployment direction. |
| [UIAA Rock Climbing grades](https://www.theuiaa.org/grades-standards/rock-climbing/) | Codex web-observed receipt | 2026-08-12 | Grade systems differ; V1 must not infer conversion or universal comparability. |
| [Railway Dockerfiles](https://docs.railway.com/builds/dockerfiles), [PostgreSQL](https://docs.railway.com/databases/postgresql), [Variables](https://docs.railway.com/variables) | Codex web-observed receipts | 2026-08-12 | A Dockerfile plus environment-scoped PostgreSQL configuration is a supported Railway path. |

IDC's temporary input/output package was removed after rendering. The rendered
output was deterministic for its recorded inputs. IDC had no repository, shell,
Git, network or Dora-write capability.

## Important advisory conclusions

1. Attempt, send, unique route and climbing day are distinct measures. Dashboard
   formulas must name the measure rather than call everything “Total Climbs”.
2. Route Database should derive from private attempts, so it never creates a
   second manual recordkeeping burden.
3. Grade charts need an explicit ordered French catalog and preserve the user's
   selected original label.
4. A complete product can still be delivered serially: private account foundation;
   climbing log; route/training records; calculations; dashboards/achievements;
   responsive UX; deployment package and operational evidence.

## Resolved by owner or delegated ordinary choice

- Public registration with account-private records.
- French sport-grade V1 only; 3a–9c catalog; no bouldering analytics.
- Local email/password authentication, server sessions, rate limits and CSRF.
- Data retained until a user deletes it; owner-data export is part of V1.

## Still external launch gates

- Assign Railway project, production Postgres, secrets owner and public domain.
- Explicitly accept the V1 no-email-verification/no-self-service-recovery posture
  or authorize a later email-provider capability before public launch.

## Candidate first vertical slice

A visitor registers and signs in, saves one valid private French-grade climbing
attempt, refreshes the responsive web app and sees it only in their chronological
log. This proves the foundation of the full required V1; it does not reduce its
scope.
