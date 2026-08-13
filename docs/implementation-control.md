# ClimbMe implementation control

ClimbMe uses Dora as its only delivery-control layer. Owner decisions belong in
`docs/decision-log.yaml`; work state and evidence belong in `docs/work/`; this
document never replaces either.

- A broad change begins with an owner-readable baseline and a strict serial Master Plan.
- One inventory item maps to exactly one atomic work task with one observable
  outcome, exact changed paths, a leaf validation and an evidence boundary.
- A task may record a missing external production dependency, but it must not
  call that evidence a passing deployment or public-launch proof.
- Product source, tests, API behavior, UI behavior, runtime evidence and living
  docs change together when the product meaning changes.
- Every implementation review records process-only learning in
  `docs/ai-delivery-learning.md`; it must not copy user performance data into
  Dora, IDC or any future private-context system.
- An owner-authorized Context OS/CC-01 measurement case may retain only the
  aggregate metrics explicitly declared in its local research record. It must
  never retain raw conversations, prompts, source excerpts, tool output,
  secrets, user records or personal-context material; it cannot itself prove a
  Context OS, PC or CPPE capability.
- Railway/GitHub/deployment operations require explicit owner authorization even
  when the repository is otherwise on an autonomous delivery path.
