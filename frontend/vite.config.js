import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // Jede Anfrage von unserem Vue-Code an "/api/..." wird im Hintergrund
      // an das Spring-Boot-Backend auf Port 8080 weitergeleitet.
      // So denkt der Browser, alles läuft auf einem Server -> kein CORS-Problem.
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
