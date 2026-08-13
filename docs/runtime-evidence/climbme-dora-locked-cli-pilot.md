# ClimbMe locked Dora CLI pilot

Date: 2026-08-13

Scope: local delivery-tool launcher only. No application source, database,
private record, provider, deployment, Git publishing or vendored-package removal
occurred.

- Exact lock: Dora `v1.13.1`, source commit `82e40bb3a0e46e33652490dc56e45f8d0e0031e6`,
  runtime package checksum `6674e9cc414d1bd061cdc0a6ed87c9f9c66235c0b36fa3e453c7c32656a513ae`.
- Baseline: the existing vendored launcher completed ten local control checks with
  a 135 ms median and 142 ms p95.
- Locked result: the exact local launcher completed ten local control checks with
  a 224 ms median, 227 ms cold start and 229 ms p95.
- speed budget: passed. The declared maximum is 235 ms median and 392 ms for
  cold/p95 timing; the locked result remains below every limit.
- clean-copy: passed. A temporary copy excluding both `.git` and `dora/` completed
  the control check through its consumer `bin/dora` and the exact local cache.
- rollback retained: passed. The existing `dora/bin/dora` remains executable in
  this repository and no vendored files were removed.
- Network/provider boundary: passed. The launcher resolves the lock only from the
  private local cache and the pilot invoked no network or provider command.
- Recovery verification: this evidence is re-attested by the follow-up leaf after
  a prior strict verifier retry rejected an unchanged implementation surface.

Completion boundary: this proves one local ClimbMe compatibility and speed pilot.
It does not authorize deletion of the vendored package, migration of another
consumer, publication, deployment or a claim about application behavior.
