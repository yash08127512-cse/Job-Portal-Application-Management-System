const API = "/api";
const jobsList = document.getElementById("jobsList");
const toast = document.getElementById("toast");
let toastTimer;

function notify(message) {
  toast.textContent = message;
  toast.classList.add("show");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove("show"), 2800);
}
function money(value) {
  return new Intl.NumberFormat("en-IN", {style:"currency", currency:"INR", maximumFractionDigits:0}).format(value);
}
async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: {"Content-Type":"application/json", ...(options.headers || {})},
    ...options
  });
  if (!response.ok) {
    let message = "Something went wrong. Please try again.";
    try { const body = await response.json(); message = body.detail || body.message || body.title || message; } catch {}
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}
function escapeHtml(value = "") {
  return String(value).replace(/[&<>"']/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
}
async function loadJobs() {
  const params = new URLSearchParams({
    keyword: document.getElementById("keyword").value.trim(),
    location: document.getElementById("location").value.trim(),
    jobType: document.getElementById("jobType").value
  });
  jobsList.innerHTML = "<p class='muted'>Loading opportunities...</p>";
  try {
    const jobs = await request(`${API}/jobs?${params}`);
    document.getElementById("jobCount").textContent = `${jobs.length} opportunity${jobs.length === 1 ? "" : "ies"}`;
    if (!jobs.length) {
      jobsList.innerHTML = "<p class='muted'>No matching jobs found. Try changing your search.</p>";
      return;
    }
    jobsList.innerHTML = jobs.map(job => `
      <article class="job-card">
        <div class="job-top"><div><h3>${escapeHtml(job.title)}</h3><div class="company">${escapeHtml(job.company)}</div></div><span class="tag">${escapeHtml(job.jobType.replace("_"," "))}</span></div>
        <div class="job-meta"><span>⌖ ${escapeHtml(job.location)}</span><span>▦ Posted ${escapeHtml(job.postedDate)}</span></div>
        <p class="job-description">${escapeHtml(job.description)}</p>
        <div class="job-actions"><span class="salary">${money(job.salary)}</span><button class="apply-button" data-job-id="${job.id}" data-job-title="${escapeHtml(job.title)}">Apply now</button></div>
      </article>`).join("");
  } catch (error) {
    jobsList.innerHTML = `<p class="muted">${escapeHtml(error.message)} Check that the Spring Boot server and MySQL are running.</p>`;
  }
}
function openApply(jobId, title) {
  document.getElementById("applyJobId").value = jobId;
  document.getElementById("modalJobTitle").textContent = title;
  document.getElementById("modal").classList.remove("hidden");
}
async function loadApplications(email, targetId = "applicationList") {
  const target = document.getElementById(targetId);
  target.innerHTML = "<p class='muted'>Loading applications...</p>";
  try {
    const url = email ? `${API}/applications?email=${encodeURIComponent(email)}` : `${API}/applications`;
    const apps = await request(url);
    if (!apps.length) {
      target.innerHTML = "<p class='muted'>No applications found.</p>";
      return;
    }
    target.innerHTML = apps.map(app => `
      <div class="application-row">
        <div><strong>${escapeHtml(app.jobTitle)} · ${escapeHtml(app.company)}</strong>
          <small>${escapeHtml(app.candidateName)} · ${escapeHtml(app.email)} · Applied ${escapeHtml(app.appliedAt.replace("T"," ").slice(0,16))}</small></div>
        <span class="status">${escapeHtml(app.status.replace("_"," "))}</span>
      </div>`).join("");
  } catch (error) {
    target.innerHTML = `<p class='muted'>${escapeHtml(error.message)}</p>`;
  }
}

jobsList.addEventListener("click", event => {
  const button = event.target.closest("[data-job-id]");
  if (button) openApply(button.dataset.jobId, button.dataset.jobTitle);
});
document.getElementById("filterForm").addEventListener("submit", event => {
  event.preventDefault(); loadJobs();
});
document.getElementById("closeModal").addEventListener("click", () => document.getElementById("modal").classList.add("hidden"));
document.getElementById("modal").addEventListener("click", event => {
  if (event.target.id === "modal") event.currentTarget.classList.add("hidden");
});
document.getElementById("applyForm").addEventListener("submit", async event => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(event.currentTarget).entries());
  data.jobId = Number(data.jobId);
  try {
    await request(`${API}/applications`, {method:"POST", body:JSON.stringify(data)});
    notify("Application submitted successfully.");
    event.currentTarget.reset();
    document.getElementById("modal").classList.add("hidden");
    document.getElementById("lookupEmail").value = data.email;
    loadApplications(data.email);
  } catch (error) { notify(error.message); }
});
document.getElementById("applicationLookup").addEventListener("submit", event => {
  event.preventDefault();
  loadApplications(document.getElementById("lookupEmail").value.trim());
});
document.getElementById("adminToggle").addEventListener("click", () => {
  const panel = document.getElementById("employerPanel");
  panel.classList.toggle("hidden");
  if (!panel.classList.contains("hidden")) {
    panel.scrollIntoView({behavior:"smooth"});
    loadApplications("", "adminApplications");
  }
});
document.getElementById("jobForm").addEventListener("submit", async event => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(event.currentTarget).entries());
  data.salary = Number(data.salary);
  try {
    await request(`${API}/jobs`, {method:"POST", body:JSON.stringify(data)});
    notify("Job published successfully.");
    event.currentTarget.reset();
    loadJobs();
  } catch (error) { notify(error.message); }
});
document.getElementById("adminApplications").addEventListener("change", async event => {
  const select = event.target.closest("[data-status-id]");
  if (!select) return;
  try {
    await request(`${API}/applications/${select.dataset.statusId}/status?status=${encodeURIComponent(select.value)}`, {method:"PATCH"});
    notify("Application status updated.");
    loadApplications("", "adminApplications");
  } catch (error) { notify(error.message); }
});
loadJobs();
