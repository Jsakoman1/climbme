#!/usr/bin/env ruby
# frozen_string_literal: true

require "open3"

label = ARGV[ARGV.index("--label").to_i + 1] if ARGV.include?("--label")
runs = ARGV[ARGV.index("--runs").to_i + 1] if ARGV.include?("--runs")
command = ARGV[ARGV.index("--command").to_i + 1] if ARGV.include?("--command")
abort "usage: measure-dora-cli.rb --label <name> --runs <positive-number> --command <command>" unless label && runs&.match?(/\A[1-9][0-9]*\z/) && command
durations = Integer(runs).times.map do
  started = Process.clock_gettime(Process::CLOCK_MONOTONIC); _output, status = Open3.capture2e(command); abort "measurement command failed" unless status.success?
  ((Process.clock_gettime(Process::CLOCK_MONOTONIC) - started) * 1000).round
end
sorted = durations.sort
puts "label=#{label} count=#{durations.length} cold_ms=#{durations.first} median_ms=#{sorted[durations.length / 2]} p95_ms=#{sorted[(durations.length * 0.95).ceil - 1]}"
