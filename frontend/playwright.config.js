import { defineConfig } from "@playwright/test";

export default defineConfig({
  testDir: "./e2e",
  use: { baseURL: process.env.CLIMBME_BASE_URL || "http://localhost:8081" }
});
