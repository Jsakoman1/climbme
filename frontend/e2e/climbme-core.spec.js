import { expect, test } from "@playwright/test";

async function registerAndAddAttempt(page, suffix) {
  await page.setViewportSize({ width: 390, height: 844 });
  await page.goto("/");
  await page.getByRole("button", { name: "New here? Create an account" }).click();
  await expect(page.getByLabel("Password")).toHaveAttribute("minlength", "15");
  await page.getByLabel("Email").fill(`mobile-${suffix}-${Date.now()}@example.com`);
  await page.getByLabel("Password").fill("safe-password-123");
  await page.getByRole("button", { name: "Create account" }).click();
  await expect(page.getByRole("heading", { name: "Log a climb" })).toBeVisible();
  await page.getByLabel("Location").fill("Osp");
  await page.getByLabel("Sector").fill("Misja");
  await page.getByLabel("Route name").fill("Test route");
  await page.getByRole("button", { name: "Add attempt" }).click();
  await expect(page.getByText("Attempt added.")).toBeVisible();
}

test("mobile navigation and Log preserve the primary climbing journey", async ({ page }) => {
  await registerAndAddAttempt(page, "navigation");
  await expect(page.getByText("Attempt added.")).toBeVisible();
  const tabs = page.locator(".mobile-tabbar");
  await tabs.getByRole("button", { name: "Routes" }).click();
  await expect(page.getByRole("heading", { name: "Route database" })).toBeVisible();
  await expect(page).toHaveURL(/#routes$/);
  await tabs.getByRole("button", { name: "Training" }).click();
  await expect(page.getByRole("heading", { name: "Training sessions" })).toBeVisible();
  await tabs.getByRole("button", { name: "Insights" }).click();
  await expect(page.getByRole("heading", { name: "Insights" })).toBeVisible();
  await page.goBack();
  await expect(page.getByRole("heading", { name: "Training sessions" })).toBeVisible();
  await page.goForward();
  await expect(page.getByRole("heading", { name: "Insights" })).toBeVisible();
  await tabs.getByRole("button", { name: "Log" }).click();
  await expect(page.getByRole("heading", { name: "Recent attempts" })).toBeVisible();
});

test("mobile Routes and Training keep their existing private actions usable", async ({ page }) => {
  await registerAndAddAttempt(page, "routes-training");
  const tabs = page.locator(".mobile-tabbar");
  await tabs.getByRole("button", { name: "Routes" }).click();
  await expect(page.locator("#routes").getByText("Test route")).toBeVisible();
  page.once("dialog", dialog => dialog.accept());
  await page.getByRole("button", { name: "Abandon" }).click();
  await expect(page.locator("#routes").getByText("Abandoned")).toBeVisible();
  await tabs.getByRole("button", { name: "Training" }).click();
  await page.getByLabel("Duration (min)").fill("30");
  await page.getByRole("button", { name: "Add training" }).click();
  await expect(page.locator("#training tbody strong").getByText("Climbing", { exact: true })).toBeVisible();
});

test("mobile Insights keeps existing private performance information readable", async ({ page }) => {
  await registerAndAddAttempt(page, "insights");
  const tabs = page.locator(".mobile-tabbar");
  await tabs.getByRole("button", { name: "Insights" }).click();
  await expect(page.locator("#insights-section").getByRole("heading", { name: "Insights" })).toBeVisible();
  await expect(page.locator("#insights-section").getByText("Total attempts", { exact: true })).toBeVisible();
  await expect(page.locator("#insights-section").getByRole("heading", { name: "Grade progression" })).toBeVisible();
  await expect(page.locator("#insights-section").getByText("First 6a redpoint", { exact: true })).toBeVisible();
  await expect.poll(() => page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth)).toBe(true);
});
