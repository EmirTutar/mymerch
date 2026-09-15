// Vite ersetzt import.meta.env.VITE_* beim Build durch den tatsächlichen Wert.
// Lokal (leer/undefiniert) bleiben wir bei relativen Pfaden wie "/api/products" -
// die übernimmt unser Vite-Proxy aus vite.config.js. In Produktion (z.B. Vercel)
// setzen wir VITE_API_BASE_URL auf die öffentliche Backend-URL, siehe .env.example.
const API_BASE = import.meta.env.VITE_API_BASE_URL ?? ''

export function apiUrl(path) {
  return `${API_BASE}${path}`
}
