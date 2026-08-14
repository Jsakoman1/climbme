# Shared module fast path

Use this path when a capability is genuinely common across applications and has
a stable, small boundary. Auth Foundation is the first example.

1. Extract one behavior with a small public API and its own tests. Keep product
   policy, data access and UI outside the module.
2. Version the module and publish it as a private package from its own GitHub
   repository.
3. Let the consuming application resolve the immutable version through Maven
   settings that read credentials only from its environment. Never commit a
   token or pass one into a Docker build argument.
4. First validate the consumer locally with a short-lived package credential.
   Keep the adoption limited to one primitive before migrating more behavior.
5. For a deployable consumer, use GitHub Actions to map its ephemeral token to
   the consumer Maven settings variable, resolve the package, test and package
   the app, then assemble a runtime-only context. The Dockerfile receives only
   the prebuilt JAR.
6. On Railway Free, use an owner-created environment-scoped project deployment
   token as a GitHub Actions secret and upload only that runtime context to the
   named service. Keep source autodeploy disconnected and verify health after
   the run. A private-image registry route is optional for a plan that supports
   private pulls; it is not a prerequisite for the Free route.

## Current ClimbMe delivery path

`deploy-railway-runtime.yml` is intentionally manual. It uses the GitHub Actions
token only for Maven, builds the frontend and backend before Docker runs, then
uploads only a JAR, runtime Dockerfile and Railway manifest. Its project-scoped
`RAILWAY_TOKEN` and non-secret `RAILWAY_PROJECT_ID` are configured in GitHub,
not source. The workflow needs read access to the private Auth Foundation
package; grant the `climbme` repository the least-privileged package access
before the first run.

The production Railway service remains source-disconnected. A manual workflow
run is still a visible owner-authorized external action.
