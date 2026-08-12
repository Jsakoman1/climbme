export function csrfTokenFromCookie(cookie = "") {
  const match = cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);
  return match ? decodeURIComponent(match[1]) : null;
}

export function normalizedEmail(email) {
  return email.trim().toLowerCase();
}

export function authRequestOptions(method, payload, csrfToken) {
  return {
    method,
    credentials: "same-origin",
    headers: { "Content-Type": "application/json", "X-XSRF-TOKEN": csrfToken || "" },
    body: payload ? JSON.stringify(payload) : undefined
  };
}

async function ensureCsrf() {
  await fetch("/api/auth/csrf", { credentials: "same-origin" });
  return csrfTokenFromCookie(document.cookie);
}

async function api(path, method = "GET", payload) {
  const csrfToken = method === "GET" ? null : await ensureCsrf();
  const options = method === "GET" ? { credentials: "same-origin" } : authRequestOptions(method, payload, csrfToken);
  const response = await fetch(path, options);
  if (!response.ok) throw new Error((await response.text()) || "Unable to complete that request.");
  return response.status === 204 ? null : response.json();
}

function authMarkup(mode, message = "") {
  const register = mode === "register";
  return `<main class="auth-shell"><section class="auth-card" aria-labelledby="app-title">
    <p class="eyebrow">CLIMBING PERFORMANCE LOG</p><h1 id="app-title">ClimbMe</h1>
    <p>${register ? "Create a private logbook for your climbs and training." : "Sign in to your private climbing log."}</p>
    <form id="auth-form"><label>Email<input name="email" type="email" autocomplete="email" required></label>
    <label>Password<input name="password" type="password" autocomplete="${register ? "new-password" : "current-password"}" minlength="12" required></label>
    <button type="submit">${register ? "Create account" : "Sign in"}</button></form>
    <p class="auth-message" role="status">${message}</p>
    <button class="text-button" id="switch-auth" type="button">${register ? "Already have an account? Sign in" : "New here? Create an account"}</button>
  </section></main>`;
}

function signedInMarkup(account) {
  return `<main class="auth-shell"><section class="auth-card"><p class="eyebrow">PRIVATE ACCOUNT</p>
    <h1>Welcome, ${account.email}</h1><p>Your climbing workspace is ready. The log, route database, training and dashboard arrive through the next verified delivery slices.</p>
    <button id="sign-out" type="button">Sign out</button></section></main>`;
}

function mountAuth(mode = "login", message = "") {
  const root = document.querySelector("#app");
  root.innerHTML = authMarkup(mode, message);
  root.querySelector("#switch-auth").addEventListener("click", () => mountAuth(mode === "login" ? "register" : "login"));
  root.querySelector("#auth-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    try {
      const account = await api(`/api/auth/${mode === "register" ? "register" : "login"}`, "POST", {
        email: normalizedEmail(form.get("email")), password: form.get("password")
      });
      mountSignedIn(account);
    } catch (error) { mountAuth(mode, error.message); }
  });
}

function mountSignedIn(account) {
  const root = document.querySelector("#app");
  root.innerHTML = signedInMarkup(account);
  root.querySelector("#sign-out").addEventListener("click", async () => { await api("/api/auth/logout", "POST"); mountAuth(); });
}

if (typeof document !== "undefined") {
  api("/api/auth/me").then(mountSignedIn).catch(() => mountAuth());
}
