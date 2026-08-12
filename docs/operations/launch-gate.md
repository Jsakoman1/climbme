# ClimbMe public launch gate

The checked-in package is deployment-ready, not deployed. Before a public
launch, the owner must visibly confirm:

- the intended Railway project and PostgreSQL service;
- database variables are referenced rather than copied into source control;
- secure cookies are enabled for HTTPS;
- migration and health evidence has been observed on the deployed service;
- a private test account can register, sign in, write an attempt and sign out;
- the public URL and who may receive access are approved.

If any item is missing, the service must remain an unpublished deployment
candidate rather than being described as live.
