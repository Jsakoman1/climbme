# IDC advisory baseline: ClimbMe mobile-first UX modernization

Rendered: 2026-08-13
Profile: `research_dossier`
Disposition: advisory-only — this document is not a Dora decision, plan,
evidence record, verified status or release approval.

## Owner outcome

The owner wants ClimbMe to be modern, simple and human-friendly primarily on
iPhone, without adding a new product feature. The existing private responsive
web app, protected backend API and PostgreSQL data remain the product boundary.

## Confirmed source observations

- The V1 product is responsive web first; a native iPhone client is deferred.
- One climbing-log row remains one real attempt. Routes and dashboard values
  remain derived from those attempts.
- The current authenticated client appends Log, Routes, Training and Dashboard
  into one long page.
- The header labels only Log as active and hides its navigation below `780px`;
  it does not provide a replacement mobile navigation model.
- The climb form already hides secondary fields behind an optional-details
  disclosure, but its primary entry flow is still shaped as a collapsed desktop
  grid rather than a phone-first task flow.

## Advisory interaction model

Use four real client sections: **Log**, **Routes**, **Training** and
**Insights**. On phone widths, expose them through a persistent bottom tab bar;
on larger widths, use an equivalent visible navigation model. The Log section
owns the primary “Add attempt” action. This preserves every existing V1 screen
and API endpoint without creating new persisted data or changing analytics.

The first safe delivery slice is the shared mobile app shell, genuine section
switching and the data-preserving Log screen. Routes, Training and Insights are
separate slices so an incomplete visual redesign cannot hide a broken primary
entry flow.

## Design evidence, not product authority

Apple’s text-field guidance supports visible labels alongside hints because a
placeholder disappears while someone types; it also distinguishes short text
entry from larger text views. ClimbMe should therefore keep labels, use hints
only as help and retain Notes as a text area. [Apple Human Interface Guidelines:
Text fields](https://developer.apple.com/design/human-interface-guidelines/text-fields?changes=_5)

A selected mobile-design article supports visible primary destinations,
comfortable touch targets, one primary purpose per screen and real device
testing. It is a design lens only, not a canonical requirement. [Clay mobile
design practices](https://clay.global/blog/practices-for-mobile-web-design)

## Source provenance

| Source | Kind | Revision / observed | Supports |
| --- | --- | --- | --- |
| Owner message | owner-confirmed input | 2026-08-13 | iPhone-first direction; no new feature scope. |
| `docs/product-brief.yaml` | local canonical product source | `2768c5c`, observed 2026-08-13 | Web-first V1 and native-client non-goal. |
| `docs/domain-library.yaml` | local canonical domain source | `2768c5c`, observed 2026-08-13 | Attempt-primary record and derived views. |
| `frontend/src/main.js`, `frontend/src/styles.css` | Codex-observed local implementation | `2768c5c`, observed 2026-08-13 | Current long-page layout and hidden mobile navigation. |
| Apple HIG Text fields | external research receipt | accessed 2026-08-13 | Labels, hints and appropriate text-control guidance. |
| Clay mobile design practices | external research receipt | accessed 2026-08-13 | Navigation, touch and device-test design lens. |

## Visible uncertainty and stop conditions

- **Open question:** no specific visual-brand direction, dark-mode requirement
  or revised contrast preference was selected. The plan may use the current
  calm outdoor character, but cannot claim a rebrand.
- **Missing context:** no iPhone-sized runtime result exists for the *redesigned*
  experience. The plan therefore requires its own runtime proof before a
  mobile-ready claim.
- Do not change authentication, privacy scope, API responses, analytics
  formulas or the PostgreSQL schema.
- Do not call the design iPhone-ready until the primary navigation and logging
  journey pass at an iPhone-sized runtime viewport.

## Promotion proposal

The owner may approve a serial Dora master that modernizes the app shell,
section navigation, primary Log flow, Routes, Training and Insights
presentation, then captures mobile evidence and process-only delivery learning.
Any later product or architecture decision remains a separate Dora decision.
