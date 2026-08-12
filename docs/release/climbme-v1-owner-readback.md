# ClimbMe V1 owner readback

## Verified local capability

ClimbMe V1 is a private, English, responsive web application for climbers. A
visitor can create an account and use a CSRF-protected session. Each account
can record, correct and delete its own climbing attempts; each attempt holds
date, location, sector, route, French grade, style, attempt number, send state,
optional length/time/RPE/conditions/partner/notes. It can also record private
training sessions.

The Route Database is derived from those attempts rather than maintained in a
second form. The dashboard shows private KPIs, achievements and responsive,
labelled proportional charts for grade progression, sends by grade, style,
attempts to send, location send rate and annual volume. The user can download
their own JSON data or permanently delete their account and owned records only
after entering the current password.

Verified evidence:

- `mvn -q -f backend/pom.xml test` covers CSRF/session behavior, owner data
  isolation, bounded failed-auth throttling, attempts, routes, training,
  dashboard, export and account deletion.
- `npm --prefix frontend run test` and `npm --prefix frontend run build` pass.
- `npm --prefix frontend run test:runtime` passes desktop 1280×900 and mobile
  390×844 registration, attempt entry, derived route, training, dashboard and
  visual-chart scenarios against a fresh local Docker package.
- `docker build -t climbme:local .` passes. A separate local Compose smoke test
  reached healthy PostgreSQL, healthy application container and
  `/actuator/health` = `UP`.

The V1 implementation commits are on the private `Jsakoman1/climbme` main
branch. The final product commits include `bd93626` (Railway package),
`f84111a` (auth-guard proof), `817a432` (private data controls), and `52f438e`
(visual charts).

## External launch gates

ClimbMe is Railway-ready, not publicly deployed. Before describing it as live,
the owner must choose the Railway project and PostgreSQL service, reference the
database variables, set `CLIMBME_OPERATIONS_SECURE_COOKIES=true`, observe
migrations and `/actuator/health`, create a private test account, and approve a
public URL/access list. The exact checklist is in
[`docs/operations/launch-gate.md`](../operations/launch-gate.md).

V1 intentionally does not include native iPhone delivery, social/public data,
subscriptions, grade conversion, AI coaching, email verification or self-service
password recovery. Railway secrets, backups, DNS and a public service have not
been created or claimed by this delivery.
