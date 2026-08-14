#!/usr/bin/env ruby
# frozen_string_literal: true

require "yaml"

lock = YAML.load_file("dora.lock.yaml")
project = YAML.load_file(".dora/project.yaml")
distribution = project.fetch("distribution")
expected = %w[release_version source_commit package_checksum cache_layout]

abort "locked Dora identity is incomplete" unless expected.all? { |key| lock.key?(key) }
abort "Dora distribution is not locked_local_cli" unless distribution["method"] == "locked_local_cli"
abort "Dora lock path is wrong" unless distribution["lock_path"] == "dora.lock.yaml"
abort "Dora adapter and lock disagree" unless expected.all? { |key| distribution[key] == lock[key] }
abort "stale vendored Dora directory remains" if File.directory?("dora")

puts "ClimbMe locked Dora CLI metadata test passed."
