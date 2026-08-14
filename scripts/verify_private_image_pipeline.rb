#!/usr/bin/env ruby
# frozen_string_literal: true

ROOT = File.expand_path("..", __dir__)
WORKFLOW = File.join(ROOT, ".github/workflows/build-private-image.yml")
RUNTIME_DOCKERFILE = File.join(ROOT, "Dockerfile.runtime")

def assert_includes(source, fragment, label)
  abort("private image pipeline validation failed: missing #{label}") unless source.include?(fragment)
end

workflow = File.read(WORKFLOW)
runtime_dockerfile = File.read(RUNTIME_DOCKERFILE)

assert_includes(workflow, "workflow_dispatch:", "manual release trigger")
assert_includes(workflow, "contents: read", "read-only source permission")
assert_includes(workflow, "packages: write", "package publishing permission")
assert_includes(workflow, "server-id: github", "Maven registry server identity")
assert_includes(workflow, "GITHUB_PACKAGES_TOKEN: ${{ secrets.GITHUB_TOKEN }}", "consumer Maven token mapping")
assert_includes(workflow, "context: image-context", "prebuilt image context")
assert_includes(workflow, "file: Dockerfile.runtime", "runtime-only Dockerfile")
assert_includes(workflow, "push: true", "private image publication")
assert_includes(workflow, "ghcr.io/${{ github.repository_owner }}/climbme:${{ github.sha }}", "immutable image tag")

%w[--build-arg].each do |forbidden|
  abort("private image pipeline validation failed: forbidden #{forbidden} in workflow") if workflow.include?(forbidden)
end

assert_includes(runtime_dockerfile, "FROM eclipse-temurin:21-jre-alpine", "runtime Java base image")
assert_includes(runtime_dockerfile, "COPY app.jar app.jar", "prebuilt application artifact")
assert_includes(runtime_dockerfile, "/actuator/health", "health check")
abort("private image pipeline validation failed: Maven must not run inside the runtime Dockerfile") if runtime_dockerfile.match?(/maven|mvn/i)
abort("private image pipeline validation failed: runtime Dockerfile must not contain package credential configuration") if runtime_dockerfile.include?("GITHUB_PACKAGES_TOKEN")

puts "private image pipeline valid"
