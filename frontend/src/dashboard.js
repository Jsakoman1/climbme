const dashboard = async () => {
  const page = document.querySelector(".page");
  if (!page || document.querySelector("#dashboard")) return;
  const response = await fetch("/api/dashboard", {credentials: "same-origin"});
  if (!response.ok) return;
  const data = await response.json(); const k = data.kpis;
  const card = (name, value) => `<article class="metric-card"><span>${name}</span><strong>${value ?? "—"}</strong></article>`;
  const block = (name, rows) => `<section class="chart-card"><h3>${name}</h3>${rows.map(row => `<p>${row.label}: <strong>${row.value}</strong></p>`).join("") || "<p>No data yet.</p>"}</section>`;
  page.insertAdjacentHTML("beforeend", `<section id="dashboard" class="log-section"><p class="eyebrow">PRIVATE PERFORMANCE</p><h2>Dashboard</h2><div class="metric-grid">${card("Total attempts",k.totalAttempts)}${card("Total sends",k.totalSends)}${card("Send rate",`${k.sendRate.toFixed(0)}%`)}${card("Hardest sent",k.hardestGradeSent)}${card("Hardest onsight",k.hardestOnsight)}${card("Hardest redpoint",k.hardestRedpoint)}${card("Active projects",k.activeProjects)}${card("Climbing days",k.climbingDays)}</div><div class="chart-grid">${block("Grade progression",data.gradeProgression)}${block("Sends by grade",data.sendsByGrade)}${block("Style distribution",data.styleDistribution)}${block("Attempts to send",data.attemptsToSend)}${block("Send rate by location",data.locationSendRate)}${block("Volume through the year",data.monthlyVolume)}</div>${block("Achievements",data.achievements.map(item=>({label:item.title,value:item.location})))}</section>`);
};
new MutationObserver(() => dashboard().catch(() => {})).observe(document.querySelector("#app"), {childList:true});
