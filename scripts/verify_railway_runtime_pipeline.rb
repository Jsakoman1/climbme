#!/usr/bin/env ruby
# frozen_string_literal: true

ROOT = File.expand_path("..", __dir__)
WORKFLOW = File.join(ROOT, ".github/workflows/deploy-railway-runtime.yml")
RUNTIME_DOCKERFILE = File.join(ROOT, "Dockerfile.runtime")
RAILWAY_DOCS = File.join(ROOT, "docs/operations/railway-deployment.md")
FAST_PATH_DOCS = File.join(ROOT, "docs/operations/shared-module-fast-path.md")

def assert_includes(source, fragment, label)
  abort("Railway runtime pipeline validation failed: missing #{label}") unless source.include?(fragment)
end

workflow = File.read(WORKFLOW)
runtime_dockerfile = File.read(RUNTIME_DOCKERFILE)
railway_docs = File.read(RAILWAY_DOCS)
fast_path_docs = File.read(FAST_PATH_DOCS)

assert_includes(workflow, "workflow_dispatch:", "manual deployment trigger")
assert_includes(workflow, "contents: read", "read-only source permission")
assert_includes(workflow, "uses: actions/checkout@v5", "current checkout action runtime")
assert_includes(workflow, "uses: actions/setup-node@v5", "current Node setup action runtime")
assert_includes(workflow, "uses: actions/setup-java@v5", "current Java setup action runtime")
assert_includes(workflow, "GITHUB_PACKAGES_TOKEN: ${{ secrets.AUTH_FOUNDATION_PACKAGES_TOKEN }}", "dedicated package-read secret mapping")
assert_includes(workflow, "RAILWAY_PROJECT_ID: ${{ vars.RAILWAY_PROJECT_ID }}", "non-secret project target")
assert_includes(workflow, "RAILWAY_TOKEN: ${{ secrets.RAILWAY_TOKEN }}", "project-scoped deployment token")
assert_includes(workflow, "mkdir railway-context", "isolated runtime context")
assert_includes(workflow, "cp backend/target/*.jar railway-context/app.jar", "prebuilt application artifact")
assert_includes(workflow, "cp Dockerfile.runtime railway-context/Dockerfile", "runtime Dockerfile only")
assert_includes(workflow, "cp railway.toml railway-context/railway.toml", "Railway manifest")
assert_includes(workflow, "test -n \"$RAILWAY_PROJECT_ID\"", "explicit project configuration check")
assert_includes(workflow, "test -n \"$RAILWAY_TOKEN\"", "explicit token configuration check")
assert_includes(workflow, "test -n \"$GITHUB_PACKAGES_TOKEN\"", "explicit package configuration check")
assert_includes(workflow, "- name: Upload runtime context to Railway\n        working-directory: railway-context\n        run: npx --yes @railway/cli@5.39.0 up --service climbme --environment production --project \"$RAILWAY_PROJECT_ID\" --ci", "pinned context-root Railway upload")

%w[uses:\ actions/checkout@v4 uses:\ actions/setup-node@v4 uses:\ actions/setup-java@v4 packages: docker/login-action docker/build-push-action ghcr.io --build-arg RAILWAY_API_TOKEN railway\ login secrets.GITHUB_TOKEN].each do |forbidden|
  abort("Railway runtime pipeline validation failed: forbidden #{forbidden} in workflow") if workflow.include?(forbidden)
end
abort("Railway runtime pipeline validation failed: upload must not pass a parent-directory context path") if workflow.include?("up railway-context") || workflow.include?("--path-as-root")

assert_includes(runtime_dockerfile, "FROM eclipse-temurin:21-jre-alpine", "runtime Java base image")
assert_includes(runtime_dockerfile, "COPY app.jar app.jar", "prebuilt application artifact")
assert_includes(runtime_dockerfile, "/actuator/health", "health check")
abort("Railway runtime pipeline validation failed: Maven must not run inside the runtime Dockerfile") if runtime_dockerfile.match?(/maven|mvn/i)
abort("Railway runtime pipeline validation failed: runtime Dockerfile must not contain package credential configuration") if runtime_dockerfile.include?("GITHUB_PACKAGES_TOKEN")

assert_includes(railway_docs, "RAILWAY_TOKEN", "documented deployment secret")
assert_includes(railway_docs, "RAILWAY_PROJECT_ID", "documented deployment target")
assert_includes(railway_docs, "AUTH_FOUNDATION_PACKAGES_TOKEN", "documented package-read secret")
assert_includes(railway_docs, "source autodeploy disconnected", "documented source boundary")
assert_includes(railway_docs, "Railway Free", "documented Free-plan route")
assert_includes(fast_path_docs, "assembled context as its working directory", "reusable runtime-context root Fast Path")

puts "Railway runtime pipeline valid"
