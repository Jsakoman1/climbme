# Railway deployment

ClimbMe is packaged as one Docker service plus a Railway PostgreSQL service.
The Docker image builds the static web client, packages it with the Spring API,
and exposes `GET /actuator/health` as its health endpoint.

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

No Railway password, token, database URL, public domain, or user record belongs
in this repository. A real deployment is an owner-visible external operation.
