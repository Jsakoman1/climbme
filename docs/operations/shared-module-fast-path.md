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
   the app, then publish an immutable private container image. The Dockerfile
   receives only the prebuilt JAR.
6. Before a Railway deploy, configure a read-only registry credential in the
   Railway service and select the immutable image tag. Verify health and the
   intended public runtime separately.

## Current ClimbMe delivery path

`build-private-image.yml` is intentionally manual. It uses the GitHub Actions
token for Maven and GHCR, builds the frontend and backend before Docker runs,
then publishes only `ghcr.io/<owner>/climbme:<commit-sha>`. The workflow needs
read access to the private Auth Foundation package; grant the `climbme`
repository the least-privileged package access before the first run.

Railway is not configured by this repository change. Its private-registry
credential, selected image tag and deployment remain visible external actions.
