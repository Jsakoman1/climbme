import test from "node:test";
import assert from "node:assert/strict";
import { authRequestOptions, csrfTokenFromCookie, normalizedEmail } from "./main.js";

test("auth helpers preserve CSRF and normalize email before sending", () => {
  assert.equal(csrfTokenFromCookie("theme=dark; XSRF-TOKEN=token%2D1"), "token-1");
  assert.equal(normalizedEmail(" Climber@Example.COM "), "climber@example.com");
  assert.deepEqual(authRequestOptions("POST", { email: "climber@example.com" }, "token-1"), {
    method: "POST", credentials: "same-origin",
    headers: { "Content-Type": "application/json", "X-XSRF-TOKEN": "token-1" },
    body: "{\"email\":\"climber@example.com\"}"
  });
});
