const CACHE_NAME = 'my-pwa-cache-v1-work';
const cacheUrls = [
    '/',
    '/home.html',
    '/css/home.css', '/css/signin.css', '/css/welcome.css', '/css/create-account.css',
    '/app.js',
    '/22.jpg',
    // Add other assets and resources you want to cache
];


self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            return cache.addAll(cacheUrls);
        })
    );
});

self.addEventListener('fetch', (event) => {
    event.respondWith(
        caches.match(event.request).then((response) => {
            return response || fetch(event.request);
        })
    );
});

self.addEventListener('activate', (event) => {
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames
                    .filter((name) => name !== CACHE_NAME)
                    .map((name) => caches.delete(name))
            );
        })
    );
});
