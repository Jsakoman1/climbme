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

### 2026-08-15 — GitHub Actions runtime maintenance pilot (verified static contract)

- Intended outcome: move ClimbMe's manual runtime workflow to the reviewed v5
  checkout, Node, and Java setup actions after the successful release workflow
  reported deprecated action-runtime annotations.
- Process signal: a green external workflow can still expose a future
  compatibility risk. Preserve the runtime-only Railway boundary while a static
  verifier prevents a silent return to the deprecated action versions.
- Evidence coverage: workflow and documentation contract verification only; no
  token, provider value, GitHub dispatch, Railway action, database access, or
  public-health claim is retained.



### 2026-08-14 — shared password-policy consumer adoption (verified local evidence)

- Intended outcome: apply Auth Foundation v0.3's explicit single-factor policy
  to ClimbMe's new credentials without changing its session, CSRF, BCrypt,
  rate-limit, account data or private climbing records.
- Process signal: browser `minlength` is a usability hint, not Unicode-aware
  security enforcement. The consumer must validate the same policy in its
  backend while leaving login compatible with existing stored hashes.
- Evidence coverage: synthetic 14- and 15-code-point credentials exercise the
  backend boundary; existing credential and private-data tests remain required.
- Residual risk: this is local behavior evidence only and makes no provider or
  deployment claim.

### 2026-08-14 — versioned shared auth package adoption (verified local evidence)

- Intended outcome: replace local email normalization with Auth Foundation v0.1.0
  without changing ClimbMe session, CSRF, BCrypt, rate-limit, database or public
  auth behavior.
- Reusable boundary: an authenticated private Maven package must resolve through
  environment-provided build credentials; a developer-local Maven cache is not a
  reproducible deployment dependency.
- Evidence coverage: Auth Foundation v0.1.0 resolved from the private registry and
  ClimbMe's complete backend suite passed. Session, CSRF, BCrypt, failure limits,
  database and public auth responses were unchanged.
- Residual risk: Docker and Railway package-secret injection are deliberately not
  configured; this is not deployment evidence and must not be pushed into an
  automatic deployment path until a secret-safe build solution is approved.

### 2026-08-14 — private package delivery boundary (verified local design)

- Intended outcome: keep private Maven package credentials out of Docker build
  arguments, image layers and Railway source-build variables.
- Delivery design: GitHub Actions resolves and verifies the package with its
  ephemeral workflow token, then publishes an immutable private runtime image
  assembled from a prebuilt JAR.
- External gate: package access for the consumer workflow and Railway's read-only
  GHCR pull credential remain owner-visible provider configuration; neither is
  created by this repository change.

### 2026-08-14 — consumer-owned Maven settings in CI (verified local remediation)

- Process signal: a CI setup action can create its own Maven server settings, but
  a consumer that explicitly selects a project-owned settings file must receive
  the environment variable that file declares.
- Reusable boundary: map the ephemeral workflow token only into the Maven step;
  verify that no equivalent credential configuration is present in the runtime
Dockerfile or Docker build arguments.

### 2026-08-14 — Railway Free runtime-only deployment (verified)

- Process signal: Railway Free cannot pull a private registry image, but its
  authenticated CLI can deploy a prebuilt runtime-only context without exposing
  a Maven package credential to Railway.
- Reusable boundary: disconnect source autodeploy before adopting this path, and
  keep future automation gated on an owner-created, project-scoped Railway token
  stored as a GitHub Actions secret.

### 2026-08-14 — Railway Free deployment automation (verified static contract)

- Process signal: a source-disconnected production service needs a repeatable
  upload route, but its token must be scoped to deployment actions and remain
  outside both Maven and Docker configuration.
- Reusable boundary: a manually triggered workflow can build the consumer with
  the ephemeral package token, then upload only the prebuilt JAR, runtime
  Dockerfile and Railway manifest using a pinned Railway CLI.
- External gate: the owner must create the production-scoped project token and
  add it as the GitHub `RAILWAY_TOKEN` secret; the project identifier is a
  non-secret repository variable. This static contract is not GitHub Actions or
  production deployment evidence.

### 2026-08-14 — cross-repository Maven package credential correction (verified static contract)

- Process signal: Apache Maven packages are repository-scoped, so a workflow in
  a separate private consumer repository cannot rely on package-level Actions
  access controls that the Maven package UI does not provide.
- Reusable boundary: use a dedicated, short-lived classic `read:packages` token
  only for the Maven dependency-resolution step. Remove the unnecessary workflow
  package permission and prohibit the token from Docker and Railway steps.
- External gate: the owner creates and rotates `AUTH_FOUNDATION_PACKAGES_TOKEN`
  in GitHub Actions. This static correction is not package-download, workflow or
  production deployment evidence.

### 2026-08-14 — Railway runtime context-root correction (verified static contract)

- Process signal: passing an isolated context as a CLI argument can leave its
  `railway.toml` outside the provider CLI's configuration-discovery root, causing
  a default build strategy instead of the intended runtime Dockerfile.
- Reusable boundary: run the Railway CLI from the assembled runtime context and
  statically reject parent-directory path uploads. This preserves the minimal
  upload while making manifest selection explicit.
- External state: an earlier upload was accepted but did not prove the expected
  manifest; production health remained available. A fresh owner-authorized
  workflow run is still required for runtime evidence.

### 2026-08-14 — Railway Free Actions runtime deployment (verified)

- Evidence coverage: the manual Actions route completed frontend and backend
  verification, resolved the cross-repository private Maven package, uploaded the
  isolated runtime context and produced a successful Railway runtime deployment
  with a passing public health check.
- Reusable boundary: package-read and Railway deployment credentials remain
  separate, and the provider CLI must run from the context that contains the
  runtime Dockerfile and manifest.
- Residual risk: this confirms one production delivery path, not backups, custom
  domains, email recovery, physical-device behavior or a general provider SLA.

### 2026-08-13 — locked Dora CLI compatibility pilot (verified local evidence)

- Intended outcome: prove that the delivery-control CLI can be resolved from one
  exact local package identity while preserving the existing vendored package as
  rollback.
- Process signal: content-hashing a full development checkout added more startup
  cost than the declared pilot budget allowed. A separate Dora runtime-package
  follow-up retained full integrity hashing while excluding Dora's own tests,
  fixtures and documentation from the installed runtime package.
- Evidence coverage: the exact local lock passed ten bounded control checks within
  the speed budget; a clean ClimbMe copy without `dora/` also passed; rollback
  remains present. No application code, database, provider or private data was
  touched.
- Candidate reusable lesson: package boundaries and performance limits need to be
  tested together for local tools. A future Dora release should validate launcher
  execute permission and its clean-copy pilot harness must fail fast. This is a
  delivery-tooling candidate, not evidence for Context OS or product behavior.
- Recovery disposition: a fresh evidence-only leaf is used after the rejected
  strict retry; it preserves the failed record instead of pretending the existing
  implementation changed again.
- Evidence-format disposition: machine checks must use stable structured fields
  instead of case-sensitive fragments from explanatory prose.

### 2026-08-13 — mobile navigation history (verified local evidence)

- Intended outcome: make the existing four mobile sections restorable through a
  browser URL and Back/Forward without creating a stored view preference.
- Process signal: the first mobile shell verified section switching, but a
  focused review found that using history replacement alone did not support a
  direct section link or browser traversal.
- Candidate reusable lesson: navigation acceptance criteria should name initial
  URL state and Back/Forward behavior whenever a client-side shell changes a
  visible section. This remains a small UX requirement, not a new product
  feature or a context-system capability claim.
- Evidence coverage: client unit/build checks, backend regression tests and one
  isolated 390px synthetic browser journey passed. The remaining physical
  iPhone/Safari gate was subsequently owner-observed as usable and closed.

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
