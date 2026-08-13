import { gradeLabelAtRank } from "./grade-catalog.js";

const number = value => Number.isInteger(value) ? String(value) : value.toFixed(1);
const percent = value => `${Math.round(value)}%`;
export const insightMetricKeys = ["totalAttempts", "totalSends", "sendRate", "hardestGradeSent", "hardestOnsight", "hardestRedpoint", "activeProjects", "climbingDays"];

export function chartMarkup(name, rows, formatValue = number) {
  if (!rows.length) return `<section class="chart-card"><h3>${name}</h3><p>No data yet.</p></section>`;
  const maximum = Math.max(...rows.map(row => row.value), 1);
  return `<section class="chart-card"><h3>${name}</h3><ul class="chart-list" aria-label="${name}">${rows.map(row => `<li class="chart-row"><span class="chart-label">${row.label}</span><span class="chart-track" aria-hidden="true"><span class="chart-bar" style="--chart-width:${Math.max(4, row.value / maximum * 100)}%"></span></span><strong class="chart-value">${formatValue(row.value, row)}</strong></li>`).join("")}</ul></section>`;
}

function dashboardMarkup(data) {
  const k = data.kpis;
  const card = (name, value) => `<article class="metric-card"><span>${name}</span><strong>${value ?? "—"}</strong></article>`;
  const gradeProgression = data.gradeProgression.map(point => ({ ...point, displayValue: gradeLabelAtRank(point.value) }));
  const gradeChart = chartMarkup("Grade progression", gradeProgression, (_, point) => point.displayValue);
  return `<section id="dashboard" class="log-section insights-surface"><header class="insights-hero"><p class="eyebrow">PRIVATE PERFORMANCE</p><h2>Insights</h2><p>Your existing log, made easier to read. Every value stays private and comes from your saved attempts.</p></header><div class="metric-grid">${card("Total attempts", k.totalAttempts)}${card("Total sends", k.totalSends)}${card("Send rate", percent(k.sendRate))}${card("Hardest sent", k.hardestGradeSent)}${card("Hardest onsight", k.hardestOnsight)}${card("Hardest redpoint", k.hardestRedpoint)}${card("Active projects", k.activeProjects)}${card("Climbing days", k.climbingDays)}</div><div class="chart-grid">${gradeChart}${chartMarkup("Sends by grade", data.sendsByGrade)}${chartMarkup("Style distribution", data.styleDistribution)}${chartMarkup("Attempts to send", data.attemptsToSend)}${chartMarkup("Send rate by location", data.locationSendRate, percent)}${chartMarkup("Volume through the year", data.monthlyVolume)}</div>${chartMarkup("Achievements", data.achievements.map(item => ({ label: item.title, value: 1 })), () => "Milestone")}</section>`;
}

const dashboard = async () => {
  const page = document.querySelector(".page");
  if (!page || document.querySelector("#dashboard")) return;
  const response = await fetch("/api/dashboard", { credentials: "same-origin" });
  if (!response.ok) return;
  page.insertAdjacentHTML("beforeend", dashboardMarkup(await response.json()));
};

if (typeof document !== "undefined") {
  new MutationObserver(() => dashboard().catch(() => {})).observe(document.querySelector("#app"), { childList: true });
}
