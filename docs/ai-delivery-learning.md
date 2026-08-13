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

### 2026-08-13 — mobile-first UX telemetry boundary (verified)

- Intended outcome: define a privacy-safe, aggregate-only observation boundary
  before the ClimbMe mobile UX implementation begins.
- Process signal: the earlier historical ClimbMe delivery could not be measured
  retroactively without reading material that should remain private. This case
  records a protocol before the work instead of reconstructing it afterward.
- Candidate reusable lesson: a future context measurement must begin with an
  explicit source policy and a compact allowed-metric list; it must remain
  separate from product evidence and cannot by itself justify Context OS, PC or
  CPPE expansion.

### 2026-08-13 — mobile-first UX closeout (verified local evidence)

- Intended outcome: make the existing private web application easier to use on a
  phone without changing authentication, API contracts, data persistence or
  dashboard calculations.
- Process signal: the owner direction and bounded IDC baseline identified one
  real problem—one long authenticated page with no usable phone navigation—while
  retaining explicit non-goals. Separate implementation and runtime-evidence
  tasks then kept visual work from being mistaken for changed product behavior.
- Evidence coverage: frontend unit tests and build, plus three isolated 390px
  synthetic browser journeys for Log/navigation, Routes/Training and Insights.
  The Routes/Training scenario initially raced the existing asynchronous save;
  waiting for its existing completion message corrected the test without changing
  the application.
- Residual risk: this is not physical iPhone/Safari, assistive-technology or
  production-user evidence. `CLIMBME-MOB-001` keeps that gate visible.
- Candidate reusable lesson: a future Dora/IDC context experiment should retain
  only approved aggregate process measures. The protocol helped distinguish
  available delivery evidence from unavailable token/compaction evidence; it
  does not itself justify Context OS, PC or CPPE expansion.

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

### 2026-08-12 — runtime evidence isolation (verified)

- Process signal: an initial browser check reached a different local application
  because both used a conventional port.
- Correction: use an application-specific temporary Compose project, database
  volume and port for runtime proof; verify the expected health response before
  treating browser output as ClimbMe evidence.
- Candidate reusable lesson: a future Dora starter should document isolated
  local runtime names/ports as an evidence convention. This needs a separately
  approved Dora plan before changing Dora itself.

### 2026-08-12 — closeout audit (verified)

- Process signal: the pre-closeout comparison of owner intent, canonical docs
  and running product found that visual “charts” were only textual rows and
  that declared export/account-deletion rights lacked an implementation.
- Correction: split the missing proof, data lifecycle and visual rendering into
  separate atomic tasks rather than calling the existing master complete.
- Candidate reusable lesson: every greenfield closeout should compare each
  owner-visible noun in the baseline against code, UI and evidence—not only
  the original task inventory.
