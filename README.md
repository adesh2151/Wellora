# Wellora By Adesh — Personal Health & Nutrition App

A standalone, installable, offline-capable **Progressive Web App (PWA)** for daily
health & goal tracking, tuned for Indian (Maharashtrian) food. One codebase installs
as a real app on **iPhone, Android, Windows, macOS and Linux** — no developer account,
no app store required.

- Multiple profiles · goals: weight **loss / gain / maintain / strength**
- Food diary with a large **Maharashtrian & Indian food library** (calories + macros)
- Weight, BMI (Asian-Indian cut-offs), water, steps, exercise, sleep
- Charts: weight trend, calories in-vs-out, 7-day averages
- Weekly meal plan + seed rotation, daily habit checklist
- Auto-calculated targets (Mifflin–St Jeor) · JSON backup/restore
- Works fully **offline** after first load; data stored privately on the device

> Wellness tool, not medical advice. Calorie/nutrient values are practical planning
> estimates. For a diagnosed condition or deficiency, follow your clinician.

---

## Run it locally (test on this laptop)

A PWA needs to be served over `http://localhost` or `https://` (not opened as a
`file://`). From this folder:

```bash
# any one of these:
python3 -m http.server 8080
# or:  npx serve .
```

Then open **http://localhost:8080** in Chrome/Edge. You'll see an **Install** icon
in the address bar (and an install button in the app's top bar).

---

## Install like a normal app (per device)

The app must first be hosted at an **HTTPS URL** (see *Publish for free* below).
Open that URL on each device:

| Device | Steps |
|---|---|
| **iPhone / iPad** | Open the URL in **Safari** → tap **Share** → **Add to Home Screen** → *Add*. Launches full-screen with its own icon, works offline. *(This is the only free, no-account way on iOS — Apple does not allow installing an app from a shared file.)* |
| **Android** | Open in **Chrome** → menu **⋮** → **Install app** / **Add to Home screen**. Or install the generated **APK** (see below) by sharing the file and tapping it. |
| **Windows** | Open in **Chrome/Edge** → **Install** icon in the address bar → *Install*. Gets a Start-menu entry and its own window. |
| **macOS** | **Chrome/Edge**: address-bar **Install**. **Safari**: **File → Add to Dock**. |
| **Linux** | Open in **Chrome/Edge** → **Install** icon → *Install*. Appears in your app launcher. |

---

## Publish for free (get the HTTPS URL)

### Option A — GitHub Pages (recommended, ~2 min)
This repo is already set to push to `github.com/adesh2151/Wellora`. Once pushed,
on GitHub open **Settings → Pages → Branch: main / root → Save**.
Your installable app will be live at:

```
https://adesh2151.github.io/Wellora/
```

> The `manifest.webmanifest` `id` is set to `/Wellora/` to match this Pages
> sub-path (`start_url`/`scope` are relative, so they work anywhere). If you host
> at a domain **root** instead, change `"id"` to `/`.

### Option B — Netlify Drop (no account needed to try)
Go to **app.netlify.com/drop** and drag this folder in. You get an instant HTTPS URL.

---

## Make a shareable Android APK (optional)

Once hosted, turn the PWA into an installable `.apk` you can share:

- Easiest: open **https://www.pwabuilder.com**, paste your URL, **Package → Android**,
  download the APK. Share it to your phone and tap to install
  (enable *Install unknown apps* for your file manager once).
- Or use **Bubblewrap** (`npm i -g @bwip/bubblewrap`) for a signed Play-Store bundle.

---

## Package a desktop app (optional)

- **Tauri** (tiny, native) or **Electron** can wrap `index.html` into a
  `.AppImage` / `.deb` (Linux), `.exe` (Windows) or `.dmg` (macOS).
- Simplest desktop "install" without any of that: just use Chrome/Edge **Install**.

---

## Files

```
index.html            the whole app (UI + logic, self-contained)
manifest.webmanifest  PWA metadata (name, icons, colors, install scope)
sw.js                 service worker — offline caching of the app shell
icons/                app icons (svg + png, incl. maskable & apple-touch)
```

## Updating the app
Edit `index.html`, then **bump `CACHE` in `sw.js`** (e.g. `swasthtrack-v3`) so
installed devices pick up the new version on next launch.
