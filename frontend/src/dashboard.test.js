import assert from "node:assert/strict";
import test from "node:test";
import { chartMarkup } from "./dashboard.js";

test("dashboard chart markup retains labels, values and a proportional visual bar", () => {
  const markup = chartMarkup("Sends by grade", [{ label: "6c", value: 2 }, { label: "7a", value: 4 }]);
  assert.match(markup, /aria-label="Sends by grade"/);
  assert.match(markup, /6c/);
  assert.match(markup, /--chart-width:50%/);
  assert.match(markup, /--chart-width:100%/);
});
