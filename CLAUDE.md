# Mini CRM

- `frontend/`: React + TypeScript + Vite + React Router + TanStack Query (+ Recharts)
- `backend/`: Spring Boot 4 + JPA + MySQL, HubSpot API 연동 (토큰은 환경변수 `HUBSPOT_ACCESS_TOKEN`, 백엔드에서만 호출)

## 디자인 시스템 (모든 스타일/UI 작업에 적용)

- 스타일, 레이아웃, 컴포넌트 모양을 만들거나 바꾸기 전에 **반드시 `docs/design-system/`을 먼저 읽는다.**
  - `README.md`(원칙/결정 사항) → `tokens.md` → `components.md` → `layout.md`
- 색상, 그림자, radius, 폰트, 간격은 `frontend/src/styles/tokens.css`의 **CSS 변수로만** 사용한다. CSS/TSX에 hex 값을 직접 쓰지 않는다.
  - 예외: Recharts 색상은 `frontend/src/constants/chartColors.ts` 상수를 사용한다.
- 문서에 없는 새 패턴이 필요하면 문서에 먼저 추가한 뒤 구현한다. 토큰 값을 바꾸면 문서와 코드를 함께 수정한다.
- 라이트 모드만 지원한다 (다크 모드는 추후).

## 작업 규칙

- npm install/build/lint/dev, Gradle, 테스트, 서버 실행은 사용자가 직접 한다. 실행하지 않는다.
- 비밀값(DB 비밀번호, HubSpot 토큰)은 코드/파일에 쓰지 않고 환경변수로만 받는다.
- 프론트엔드: API 호출은 `src/api/`에 모으고 React Query 훅으로만 접근한다. 타입은 `src/types/`에 둔다.
- 백엔드: Controller는 얇게, 비즈니스 로직은 Service, DB 접근은 Repository. Entity와 DTO를 분리한다.
