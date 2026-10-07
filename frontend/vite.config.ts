import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // 개발 서버에서 /api 요청을 Spring Boot로 전달한다. (같은 origin이 되므로 CORS 불필요)
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
