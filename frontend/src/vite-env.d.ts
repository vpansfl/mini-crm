/// <reference types="vite/client" />

// 프론트에서 사용하는 환경변수 타입. VITE_ 변수는 빌드 결과에 공개되므로 비밀값을 넣지 않는다.
interface ImportMetaEnv {
  // 백엔드 API 주소. 비워 두면 같은 origin(로컬은 Vite 프록시)으로 요청한다.
  readonly VITE_API_BASE_URL?: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
