import { expect, test } from "@playwright/test";

for (const [name, viewport] of [["desktop", { width: 1280, height: 900 }], ["mobile", { width: 390, height: 844 }]]) {
  test(`${name} core`, async ({ page }) => {
    await page.setViewportSize(viewport);
    await page.goto("/");
    await page.getByRole("button", { name: "New here? Create an account" }).click();
    await page.getByLabel("Email").fill(`${name}${Date.now()}@example.com`);
    await page.getByLabel("Password").fill("safe-password-123");
    await page.getByRole("button", { name: "Create account" }).click();
    await page.getByLabel("Location").fill("Osp");
    await page.getByLabel("Sector").fill("Misja");
    await page.getByLabel("Route name").fill("Test");
    await page.getByRole("button", { name: "Add attempt" }).click();
    await expect(page.getByRole("heading", { name: "Route database" })).toBeVisible();
    await page.getByLabel("Duration (min)").fill("30");
    await page.getByRole("button", { name: "Add training" }).click();
    await expect(page.getByRole("heading", { name: "Dashboard" })).toBeVisible();
  });
}
