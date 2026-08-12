# Capability documentation v1 closeout

## Dora-core result

The Dora-core standard is verified through the serial
`dora-machine-readable-capability-documentation-v1` inventory:

- a capability-inventory schema and deterministic validators;
- opt-in Doctor validation that leaves legacy projects compatible;
- neutral new-project scaffolding;
- Dora/IDC self-adoption, topology, evidence links and capability boundaries;
- owner-gated request routing that reuses existing IDC triage;
- an owner-approved local `v1.12.0` release contract; and
- a Dora-owned external adoption guide/template.

The v1.12.0 manifest is local release preparation only. It is not a tag, push,
publication, consumer update or consumer verification claim.

## Explicitly unstarted follow-ons

| Repository | Required future action | Authority boundary |
| --- | --- | --- |
| `ai-system` | Create its own owner-approved architecture-registry plan for Dora, IDC, planned PC and CPPE. | It may link component facts but cannot own Dora decisions, lifecycle or IDC runtime truth. |
| DoomsDayStorage | Create its own owner-approved adoption plan from the Dora template. | It retains its product/domain truth, plans, evidence and verified status. |
| TheMuffinMan | Create its own owner-approved mapping plan from its existing inventory. | Its existing inventory remains its status authority; no product status is re-evaluated here. |

PC and CPPE are still planned-only components. This closeout does not implement,
enroll or verify either one, and it does not authorize a consumer rollout.
