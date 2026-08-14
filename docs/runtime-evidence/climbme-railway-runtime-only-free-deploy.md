# ClimbMe Railway runtime-only Free deployment

Date: 2026-08-14

- Delivery: owner-authorized production deployment through the existing Railway
  CLI session after the service's GitHub source autodeploy was disconnected.
- Build boundary: an isolated temporary context contained only a freshly packaged
  application JAR, runtime-only Dockerfile and Railway manifest. Maven resolved
  the private package locally before that context existed.
- Result: Railway reported a successful deployment and the public health endpoint
  returned HTTP 200.
- Data boundary: no Railway variable value, credential, database content, account
  record, public URL or image registry credential is retained here.

This proves one production runtime deployment only. It does not create a private
registry, GitHub Actions deploy secret, automated future deployment or product
journey evidence.
