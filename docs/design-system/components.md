# 컴포넌트

구현 위치: `frontend/src/styles/ui.css` (클래스), `frontend/src/components/` (React 컴포넌트).
모든 값은 [tokens.md](tokens.md)의 변수를 사용한다.

## 버튼 — `.btn`

모든 버튼은 pill(`--radius-pill`), `label-lg`(14px/700), 높이 약 40px.

| 클래스 | 모양 | 용도 |
|---|---|---|
| `.btn-primary` | `--color-primary` 배경 + 차콜 텍스트. hover `--color-primary-hover` + `--shadow-3`, active `--color-primary-pressed` | 화면의 주요 행동 (생성, 저장, 동기화, 시뮬레이션). **한 화면 영역에 1개 권장** |
| `.btn-secondary` | `--color-primary-soft` 배경 + `--color-on-primary-soft` 텍스트 | 보조 행동 (새로고침, 다시 시도) |
| `.btn-ghost` | 흰 배경 + `--color-border` 테두리 + 차콜 텍스트 | 취소, 중립 행동 |
| `.btn-danger` | 흰 배경 + `--color-danger` 테두리/`--color-danger-text` 텍스트, hover `--color-danger-bg` | 삭제 |

- 눌림 시 `transform: scale(0.97)` (`--transition-bounce`).
- `disabled`는 opacity 0.5, 그림자/이동 없음.
- 진행 중에는 버튼 텍스트를 "저장 중..."처럼 바꾸고 비활성화한다 (중복 요청 방지).

## 카드 — `.card`

- `--color-surface` + 1px `--color-border` + `--radius-card` + `--shadow-1`, 안쪽 여백 `--space-lg`.
- 카드 제목: `.card-title` (`headline-md`).
- 클릭 가능한 카드는 hover 시 `--shadow-2` + `translateY(-2px)`.

### KPI 카드 — `StatCard` (`.stat-card`)

Stitch의 Store Feed Card를 CRM용으로 바꾼 것.

- 라벨: `label-md`, `--color-text-muted`.
- 값: `--font-display`, `metric`(28px), weight 800, 자간 `-0.02em`, `tabular-nums`.
- 보조 정보(비율 등): `body-sm` muted.
- `accent` 속성이 있는 카드는 상단에 4px 노란 바를 표시한다 (대시보드 핵심 지표 1~2개에만).

## 배지 — `.badge` / `Badge`, `CampaignStatusBadge`

pill, `label-md`, 좌우 10px, 테두리 없음, 색 배경 + 진한 텍스트.

| tone | 배경 / 텍스트 | 사용 |
|---|---|---|
| `neutral` | `--color-surface-muted` / `--color-text-muted` | DRAFT(초안) |
| `info` | `--color-info-bg` / `--color-info-text` | SCHEDULED(예약됨) |
| `success` | `--color-success-bg` / `--color-success-text` | RUNNING(진행 중) |
| `dark` | `--color-dark` / `--color-text-inverse` | COMPLETED(완료) |
| `warning` | `--color-primary-soft` / `--color-on-primary-soft` | 강조 태그 (예: "추천") |
| `danger` | `--color-danger-bg` / `--color-danger-text` | 오류/경고 태그 |

상태 → tone 매핑은 `CampaignStatusBadge`에서만 정의한다.

## 테이블 — `.table-wrap` + `.data-table`

- 컨테이너: 카드와 같은 표면(흰 배경, 테두리, `--radius-card`, `--shadow-1`), 가로 스크롤 허용.
- 헤더: `--color-surface-muted` 배경, `label-md`, `--color-text-muted`.
- 셀: `body-md`, 세로 패딩 12px, 행 구분 1px `--color-border`.
- 숫자 열: `.num` (오른쪽 정렬, `tabular-nums`).
- 클릭 가능한 행: `.clickable-row` — hover/포커스 시 `--color-primary-subtle` 배경, 키보드 Enter로 이동.

## 폼

| 요소 | 규칙 |
|---|---|
| `.field` | 라벨(`label-md`, 차콜) + 입력 + 힌트(`.field-hint`, `body-sm` muted), 세로 간격 `--space-sm` |
| `.input` / `.select` / `.textarea` | 흰 배경, 1px `--color-border`, `--radius-input`, 높이 약 42px, `body-md`. hover `--color-border-strong`, focus `--color-primary` 테두리 + `--focus-ring` |
| `.search-input` | pill, `--color-surface-muted` 배경, 테두리 없음, 왼쪽 돋보기 아이콘. focus 시 흰 배경 + 포커스 링 |
| `.form` | 카드 안에서 세로 간격 `--space-lg`, 최대 폭 760px |
| `.form-row` | 3열 그리드 (`.form-row-2`는 2열), 간격 `--space-md` |
| `.form-actions` | 버튼을 왼쪽부터 Primary → Ghost 순서로 배치 |

## 안내 메시지 — `.notice`

- 기본: `--color-primary-soft` 배경 + `--color-on-primary-soft` 텍스트, `--radius-input`.
- `.notice-success`: 성공 배경/텍스트. `.notice-error`: 오류 배경/텍스트.

## 조회 상태 — `LoadingState`, `ErrorState`, `EmptyState`

- 공통(`.state-view`): 흰 표면, 1px 점선 `--color-border`, `--radius-card`, 가운데 정렬, 세로 패딩 48px.
- 로딩: 노란 스피너 + 메시지.
- 오류: 테두리 `--color-danger`, 제목 `--color-danger-text`, "다시 시도"는 `.btn-secondary`.
- 빈 상태: 제목(차콜, `body-lg`) + 설명(muted) + 필요한 경우 Primary 버튼 1개.

## 페이지 헤더 — `PageHeader`

- 제목: `headline-xl`(32px/800, display 폰트). 설명: `body-md` muted.
- 오른쪽에 행동 버튼(`.page-actions`). 뒤로가기 링크는 제목 위 `label-lg` muted, hover 시 `--color-on-primary-soft`.

## 사이드바 내비게이션

- 흰 표면 + 오른쪽 1px 테두리.
- 로고: 노란 둥근 사각형 마크 + "Mini CRM"(display 폰트, 800).
- 메뉴: `label-lg`, pill 모양. 기본 muted, hover `--color-surface-muted`, **활성: `--color-primary-soft` 배경 + 차콜 텍스트 + 왼쪽 노란 점**.

## 차트 (Recharts)

- 카드 안에 높이 280px로 배치 (`.chart-box`).
- 색상은 `constants/chartColors.ts`만 사용 (발송 → 오픈 → 클릭 → 전환 순으로 진해짐, 결과인 전환은 민트).
- 막대 끝은 둥글게(`radius`), 격자선은 가로만 옅게, 축 텍스트 12px muted.
- 툴팁은 흰 카드 스타일(`--radius-input`, `--shadow-2`).

## 사용하지 않는 Stitch 요소

Floating Cart Bar, Category Roundels, Stepper, 16:9 이미지 Store Card, Checkbox/Radio 커스텀(필요 시 primary 색 `accent-color`만 적용).
