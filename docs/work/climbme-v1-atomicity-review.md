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
| `auth-guard-evidence` | Automated proof of the existing bounded local auth throttling. | It adds no new runtime behavior; it only proves the already-declared public-auth protection. |
| `private-data-rights` | One owner-controlled export and irreversible account-deletion flow. | It owns only private-data lifecycle, not analytics or deployment behavior. |
| `visual-analytics` | Real accessible visual charts for already-verified dashboard values. | It consumes the existing dashboard response and cannot change metric formulas. |
| `owner-closeout` | Consolidated owner readback and learning record. | It is documentation/evidence only and cannot silently mark a public launch complete. |

The plan separates protocol/planning, implementation, browser proof,
deployment-readiness proof and closeout. The only external state that remains
outside this master is an actual Railway deployment; it has a visible owner
gate in the product brief and closeout task.

The 2026-08-12 pre-closeout audit found that the earlier dashboard cards did
not meet the owner-requested visual-chart meaning, and that already-declared
private export/account-deletion behavior had no implementation. The new tasks
are each separate because evidence of the existing auth guard, data lifecycle,
and visual rendering have independent outcomes and failure modes.
