# Railway deployment

ClimbMe is one Docker service plus a Railway PostgreSQL service. The production
service uses a runtime-only Dockerfile and exposes `GET /actuator/health`.

The current Railway Free route is manual GitHub Actions deployment: Actions
resolves the private Auth Foundation package, verifies the application, assembles
only a JAR plus runtime Dockerfile and uploads that isolated context to Railway.
Railway never receives a Maven package credential.
The workflow uses the maintained v5 checkout, Node, and Java setup actions so a
successful deployment does not retain the deprecated Node 20 action runtime.

## Required Railway setup

1. Keep the production app service source autodeploy disconnected. The checked-in
   `railway.toml` selects the runtime Dockerfile; do not use a generated Nix
   buildpack in parallel.
2. Reference PostgreSQL service variables into the app service: `PGHOST`,
   `PGPORT`, `PGDATABASE`, `PGUSER`, and `PGPASSWORD`. Alternatively provide a
   complete JDBC URL as `JDBC_DATABASE_URL`.
3. Set `CLIMBME_OPERATIONS_SECURE_COOKIES=true` for the public HTTPS service.
   Railway supplies `PORT`; it must not be hard-coded as a production variable.
4. Confirm migrations complete, `/actuator/health` is healthy, and a new account
   can register before sharing the Railway URL.

## Railway Free Actions route

1. Create a short-lived GitHub classic personal access token with only
   `read:packages`, then store it as the `AUTH_FOUNDATION_PACKAGES_TOKEN`
   repository secret. Apache Maven packages are repository-scoped; this dedicated
   secret is required to read Auth Foundation from a separate private repository.
2. In GitHub Actions configuration, set `RAILWAY_PROJECT_ID` as a repository
   variable for the selected Railway project. Create `RAILWAY_TOKEN` as a
   repository secret from a Railway **project token scoped only to production
   deployment actions**. Do not paste the token into source code, a Docker build,
   Railway variables or chat.
3. Run the manual `Deploy ClimbMe runtime to Railway` workflow. It maps
   `AUTH_FOUNDATION_PACKAGES_TOKEN` to `GITHUB_PACKAGES_TOKEN` only for Maven
   because the consumer-owned settings file explicitly expects that variable.
4. The workflow uploads only the prebuilt JAR, `Dockerfile.runtime` and
   `railway.toml` to the existing `climbme` production service. Its Railway CLI
   step runs from that assembled context, so the runtime Dockerfile and manifest
   are the deployment root. It cannot read provider variables or create a service.
5. Verify health and the intended public runtime after the owner-authorized run.

## Optional paid private-image route

`build-private-image.yml` remains a manual build-only route for a future plan
that supports private registry pulls. It publishes an immutable private image;
it does not deploy the current Railway Free service. If this route is adopted,
configure a read-only GHCR credential in Railway and select an immutable image
tag. The workflow maps the ephemeral Actions token
   to `GITHUB_PACKAGES_TOKEN` only for Maven because the consumer-owned settings
   file explicitly expects that variable.

No Railway password, token, database URL, public domain, or user record belongs
in this repository. A real deployment is an owner-visible external operation.
