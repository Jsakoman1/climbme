import test from "node:test";
import assert from "node:assert/strict";
import { authRequestOptions, csrfTokenFromCookie, gradeOptions, normalizedEmail, styleLabel } from "./main.js";
test("client helpers preserve CSRF, normalize email and render climbing choices", () => { assert.equal(csrfTokenFromCookie("theme=dark; XSRF-TOKEN=token%2D1"), "token-1"); assert.equal(normalizedEmail(" Climber@Example.COM "), "climber@example.com"); assert.equal(styleLabel("REDPOINT"), "Redpoint"); assert.equal(styleLabel("ABANDONED"), "Abandoned"); assert.match(gradeOptions("7a+"), /<option value="7a\+" selected>7a\+<\/option>/); assert.deepEqual(authRequestOptions("POST", { email: "climber@example.com" }, "token-1").headers, { "Content-Type": "application/json", "X-XSRF-TOKEN": "token-1" }); });
