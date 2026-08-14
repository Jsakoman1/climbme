# Railway deployment

ClimbMe is packaged as one Docker service plus a Railway PostgreSQL service.
The checked-in Dockerfile supports source builds. When the application consumes
the private Auth Foundation package, the safer release path is the manual
GitHub Actions workflow: it resolves the package and builds an immutable private
GHCR image before Railway pulls that already-built image. Both paths expose
`GET /actuator/health` as the health endpoint.

## Required Railway setup

1. Create or select the private `Jsakoman1/climbme` repository as the service
   source and add a PostgreSQL service in the same Railway project.
2. Configure the app service from `railway.toml`; do not use a generated Nix
   buildpack in parallel.
3. Reference PostgreSQL service variables into the app service: `PGHOST`,
   `PGPORT`, `PGDATABASE`, `PGUSER`, and `PGPASSWORD`. Alternatively provide a
   complete JDBC URL as `JDBC_DATABASE_URL`.
4. Set `CLIMBME_OPERATIONS_SECURE_COOKIES=true` for the public HTTPS service.
   Railway supplies `PORT`; it must not be hard-coded as a production variable.
5. Confirm migrations complete, `/actuator/health` is healthy, and a new account
   can register before sharing the Railway URL.

## Private image release path

1. Grant the `climbme` GitHub Actions repository read access to the private
   Auth Foundation package.
2. Run the manual `Build private ClimbMe image` workflow and retain the emitted
   immutable commit-SHA image tag. The workflow maps the ephemeral Actions token
   to `GITHUB_PACKAGES_TOKEN` only for Maven because the consumer-owned settings
   file explicitly expects that variable.
3. In Railway, configure a read-only GHCR registry credential and set that
   image as the service source. Do not place a GitHub Packages Maven token in
   Railway variables or a Docker build argument.
4. Deploy only after the owner confirms the selected service and image tag;
   then verify the health endpoint and the intended public runtime.

No Railway password, token, database URL, public domain, or user record belongs
in this repository. A real deployment is an owner-visible external operation.
