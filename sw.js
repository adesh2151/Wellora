/* Wellora service worker.
   Strategy: NETWORK-FIRST for the app itself (so users always get the latest
   version when online), cache only as an offline fallback. This fixes stale/
   cached-old-version problems. Bump CACHE when cached asset files change. */
const CACHE = 'wellora-v26';
const SHELL = [
  '.', 'index.html', 'manifest.webmanifest',
  'icons/icon.svg', 'icons/icon-192.png', 'icons/icon-512.png',
  'icons/apple-touch-icon.png'
];

self.addEventListener('install', e => {
  // Activate the new version immediately so users aren't stuck on an old one.
  e.waitUntil(caches.open(CACHE).then(c => c.addAll(SHELL)).then(() => self.skipWaiting()));
});
self.addEventListener('activate', e => {
  e.waitUntil(
    caches.keys().then(keys => Promise.all(keys.filter(k => k !== CACHE).map(k => caches.delete(k))))
      .then(() => self.clients.claim())
  );
});
self.addEventListener('message', e => { if (e.data && e.data.type === 'SKIP_WAITING') self.skipWaiting(); });

function isHTML(req, url) {
  return req.mode === 'navigate' ||
    (url.origin === location.origin &&
     (url.pathname === '/' || url.pathname.endsWith('/') || url.pathname.endsWith('.html')));
}

self.addEventListener('fetch', e => {
  const req = e.request;
  if (req.method !== 'GET') return;
  const url = new URL(req.url);

  // The whole app lives in index.html → NETWORK-FIRST so it's always current.
  if (isHTML(req, url)) {
    e.respondWith(
      fetch(req).then(res => {
        const copy = res.clone();
        caches.open(CACHE).then(c => c.put('index.html', copy)).catch(() => {});
        return res;
      }).catch(() => caches.match(req).then(h => h || caches.match('index.html')))
    );
    return;
  }

  // Same-origin static assets (icons, manifest) → cache-first, refresh in background.
  if (url.origin === location.origin) {
    e.respondWith(
      caches.match(req).then(hit => hit || fetch(req).then(res => {
        const copy = res.clone();
        caches.open(CACHE).then(c => c.put(req, copy)).catch(() => {});
        return res;
      }))
    );
    return;
  }

  // Cross-origin (Google Fonts) → stale-while-revalidate.
  e.respondWith(
    caches.match(req).then(hit => {
      const net = fetch(req).then(res => {
        const copy = res.clone();
        caches.open(CACHE).then(c => c.put(req, copy)).catch(() => {});
        return res;
      }).catch(() => hit);
      return hit || net;
    })
  );
});
