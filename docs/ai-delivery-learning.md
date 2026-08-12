# AI delivery learning ledger

This ledger records reusable **process** lessons from ClimbMe delivery. It is not
application telemetry, a memory system, user research, or authority to alter Dora,
IDC, AI-system, PC or CPPE.

For each verified slice, record only:

- intended outcome and plan/task citation;
- clarification/rework count and the kind of missing context;
- acceptance/evidence coverage and residual risk;
- a candidate improvement for Dora/IDC/other AI-system component, if any;
- whether a separate owner-approved cross-component plan would be required.

Never record climber identities, emails, partner names, routes, training data,
session values, analytics, secrets or raw terminal output here.

## Standing delivery rule

When an owner authorizes a complete application master, continue automatically
through every eligible, safe, serial slice. Stop only at a real external owner
gate, failed validation requiring scope expansion, or an unresolved product
decision. Do not pause merely because an intermediate slice is verified.

## Entries

### 2026-08-12 — account foundation (verified)

- Intended outcome: local private accounts before any climbing-domain persistence.
- Process signal: the greenfield baseline separated owner-confirmed product direction from delegated implementation choices, so auth could remain one atomic task.
- Candidate reusable lesson: make account/privacy behavior testable before adding the data domain; record only the process lesson, never account or session values.

### 2026-08-12 — climbing log UI (verified)

- Intended outcome: one mobile-friendly primary form, then a chronological private record with corrective actions.
- Candidate reusable lesson: retain one API-owned data model and make optional fields progressively disclosed to preserve low-friction entry.

### 2026-08-12 — derived route database (verified)

- Intended outcome: reuse attempt records for route history instead of asking the climber to maintain another table.
- Candidate reusable lesson: when a derived view needs one human correction, persist only that bounded override and keep every other displayed field derivable.

### 2026-08-12 — primary-record API (verified)

- Intended outcome: one private climbing attempt is the only manual source record for later route and dashboard views.
- Process signal: the plan isolated data ownership, grade ordering and sent-state validation before any visual analytics work.
- Candidate reusable lesson: put user scoping and derived-data invariants in the API slice, then let later views consume verified records instead of reimplementing rules in the browser.

### 2026-08-12 — autonomous master continuation

- Signal: stopping after a verified intermediate slice created unnecessary owner friction despite complete master authorization.
- Correction: preserve serial verification, but treat it as the eligibility signal for the next safe task rather than a request to pause.
- Reusable lesson: a future idea-to-deploy workflow needs a visible external-deployment gate, not an artificial pause after each local commit.
