# ClimbMe V1 semantic atomicity review

Reviewed: 2026-08-12

The sequence is intentionally serial. A task may use evidence from an earlier
task, but it cannot begin until its immediately prior inventory item is
verified.

| Inventory item | One observable outcome | Why it is atomic |
| --- | --- | --- |
| `plan-hardening` | Review and lock the delivery sequence. | It creates neither product code nor runtime proof. |
| `auth-private-data` | Private authenticated account foundation. | It excludes climbing, route, training and dashboard behavior. |
| `climbing-attempt-api` | Owner-scoped attempt persistence API. | It excludes entry UI and every derived projection. |
| `climbing-log-ui` | Fast browser entry and log editing. | It consumes the established API; no route or dashboard behavior is introduced. |
| `route-database` | Derived per-owner route projection. | It is the only slice that adds route-status correction behavior. |
| `training-log` | Owner-scoped training persistence and UI. | It is separate from climbing-attempt semantics and analytics. |
| `dashboard-metrics` | Deterministic backend metric and achievement formulas. | It excludes dashboard presentation and runtime evidence. |
| `dashboard-achievements-ui` | Dashboard, charts and achievement presentation. | It consumes only verified backend metrics. |
| `responsive-runtime-evidence` | Browser proof for the owner journeys. | It does not change product behavior. Missing runtime prerequisites would be recorded as blocked evidence, not as a passing release claim. |
| `railway-readiness` | Local container and documented Railway configuration. | It cannot create a Railway service, set secrets or claim a live deployment. |
| `owner-closeout` | Consolidated owner readback and learning record. | It is documentation/evidence only and cannot silently mark a public launch complete. |

The plan separates protocol/planning, implementation, browser proof,
deployment-readiness proof and closeout. The only external state that remains
outside this master is an actual Railway deployment; it has a visible owner
gate in the product brief and closeout task.
