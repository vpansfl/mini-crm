# Mini CRM

HubSpot 고객 데이터를 동기화해 세그먼트를 만들고, 캠페인과 성과(시뮬레이션)를 관리하는 CRM MVP.

| 구성 | 기술 | 배포 |
|---|---|---|
| `frontend/` | React + TypeScript + Vite + React Router + TanStack Query | Vercel |
| `backend/` | Spring Boot 4 (Java 21) + JPA | Render (Docker, Free Web Service) |
| DB | MySQL | Aiven |
| 외부 API | HubSpot CRM API (백엔드에서만 호출) | - |

> 실제 비밀번호/토큰 값은 이 저장소에 기록하지 않는다. 환경변수로만 주입한다.

## 로컬 실행

### 1. MySQL
로컬 MySQL에 데이터베이스를 만든다. 테이블은 JPA(`ddl-auto=update`)가 자동 생성한다.

```sql
CREATE DATABASE mini_crm DEFAULT CHARACTER SET utf8mb4;
```

### 2. Backend (`backend/`, http://localhost:8080)

```powershell
$env:DB_PASSWORD = "<로컬 DB 비밀번호>"
$env:HUBSPOT_ACCESS_TOKEN = "<HubSpot Service Key>"   # 선택: 없으면 동기화만 503
.\gradlew.bat bootRun
```

### 3. Frontend (`frontend/`, http://localhost:5173)

```powershell
npm install
npm run dev
```

로컬에서는 `VITE_API_BASE_URL`을 비워 두면 Vite 프록시가 `/api` 요청을 `http://localhost:8080`으로 전달한다.

## 환경변수

### Backend

| 이름 | 필수 | 로컬 기본값 | 설명 |
|---|---|---|---|
| `DB_HOST` | 배포 시 | `localhost` | MySQL 호스트 |
| `DB_PORT` | 배포 시 | `3306` | MySQL 포트 |
| `DB_NAME` | 배포 시 | `mini_crm` | 데이터베이스 이름 |
| `DB_USERNAME` | 배포 시 | `root` | DB 사용자 |
| `DB_PASSWORD` | 예 | (없음) | DB 비밀번호 |
| `DB_SSL_MODE` | 배포 시 | `PREFERRED` | MySQL SSL 모드. Aiven은 `REQUIRED` |
| `DB_POOL_SIZE` | 아니오 | `5` (prod) | 커넥션 풀 최대 크기 |
| `HUBSPOT_ACCESS_TOKEN` | 동기화 사용 시 | (없음) | HubSpot Service Key |
| `CORS_ALLOWED_ORIGINS` | 배포 시 | `http://localhost:5173` | 허용 Origin, 쉼표 구분 (패턴 가능) |
| `SPRING_PROFILES_ACTIVE` | 아니오 | `dev` (Docker 이미지는 `prod`) | 실행 프로필 |
| `PORT` | 아니오 | `8080` | 서버 포트 (Render가 자동 주입) |

### Frontend

| 이름 | 필수 | 설명 |
|---|---|---|
| `VITE_API_BASE_URL` | 배포 시 | 백엔드 주소 (끝 `/` 없이). 로컬에서는 비워 둔다 |

`VITE_` 변수는 빌드 결과에 공개되므로 비밀값을 넣지 않는다. 예시 파일: `frontend/.env.example`

## 배포

### Backend → Render (Docker)
- Root Directory: `backend`, Runtime: Docker (`backend/Dockerfile`)
- Health Check Path: `/api/health`
- 환경변수: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_SSL_MODE`, `HUBSPOT_ACCESS_TOKEN`, `CORS_ALLOWED_ORIGINS`

### DB → Aiven MySQL
- Aiven 콘솔의 Host / Port / Database / User / Password 값을 위 백엔드 환경변수에 입력한다.
- `DB_SSL_MODE=REQUIRED`

### Frontend → Vercel
- Root Directory: `frontend` (Framework: Vite, `frontend/vercel.json`에 SPA rewrite 설정 포함)
- 환경변수: `VITE_API_BASE_URL`
- 배포 후 Vercel 주소를 백엔드 `CORS_ALLOWED_ORIGINS`에 추가한다.
