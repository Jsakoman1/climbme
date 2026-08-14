# ClimbMe Railway Free Actions runtime deployment

Verified: 2026-08-14

- Delivery: the owner-authorized manual Free deployment workflow completed its
  frontend verification, private-package Maven verification, runtime-context
  assembly and Railway upload successfully.
- Runtime: Railway reported a successful deployment using the context-local
  runtime Dockerfile and declared health-check configuration.
- Health: the public health endpoint returned HTTP 200 after deployment.
- Correction: the earlier parent-directory upload was removed by Railway; the
  verified workflow now invokes the Railway CLI from the assembled runtime root.
- Data boundary: this evidence retains no secret, token, provider variable,
  workflow/deployment identifier, public URL, build log, database content or
  account data.
