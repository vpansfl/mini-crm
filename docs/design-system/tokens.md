# 토큰

정의 위치: `frontend/src/styles/tokens.css`. 아래 CSS 변수 이름만 사용한다.

## 색상

### 브랜드 (Honey)

| 변수 | 값 | 용도 |
|---|---|---|
| `--color-primary` | `#F59E0B` | Primary 버튼, 활성 메뉴 포인트, 핵심 강조, 포커스 링 |
| `--color-primary-hover` | `#FBBF24` | Primary 버튼 hover |
| `--color-primary-pressed` | `#D97706` | Primary 버튼 active(눌림) |
| `--color-primary-soft` | `#FEF3C7` | Secondary 버튼 배경, 활성 메뉴 배경, 안내 메시지 배경 |
| `--color-primary-subtle` | `#FFFBEB` | 테이블 행 hover 등 아주 옅은 강조 |
| `--color-on-primary` | `#1F2937` | 노란 배경(`primary`) 위 텍스트 |
| `--color-on-primary-soft` | `#855300` | 옅은 노란 배경(`primary-soft`) 위 텍스트/아이콘 |

### 중립 (Charcoal & Porcelain)

| 변수 | 값 | 용도 |
|---|---|---|
| `--color-text` | `#1F2937` | 기본 텍스트, 제목, 값 |
| `--color-text-muted` | `#6B7280` | 보조 설명, 메타 정보, 라벨, 비활성 아이콘 |
| `--color-text-inverse` | `#FFFFFF` | 어두운 배경 위 텍스트 |
| `--color-canvas` | `#FBFBFA` | 페이지 배경 (Level 0) |
| `--color-surface` | `#FFFFFF` | 카드, 사이드바, 테이블, 입력 (Level 1) |
| `--color-surface-muted` | `#F3F4F6` | 검색 입력 배경, 테이블 헤더, 비활성 배지 |
| `--color-border` | `#E5E7EB` | 1px hairline 테두리, 구분선 |
| `--color-border-strong` | `#D1D5DB` | 입력 hover 테두리 |
| `--color-dark` | `#1F2937` | 진한 배지/칩 배경 (완료 상태 등) |

### 시스템 신호

| 변수 | 값 | 용도 |
|---|---|---|
| `--color-success` | `#10B981` | 진행 중, 성공, 전환 강조 |
| `--color-success-bg` | `#ECFDF5` | 성공 배지/메시지 배경 |
| `--color-success-text` | `#047857` | 성공 배경 위 텍스트 (대비 확보) |
| `--color-danger` | `#EF4444` | 삭제, 오류 테두리/아이콘 |
| `--color-danger-bg` | `#FEF2F2` | 오류 메시지/위험 버튼 hover 배경 |
| `--color-danger-text` | `#B91C1C` | 오류 배경 위 텍스트 (대비 확보) |
| `--color-info-bg` | `#D6E0F3` | 정보/예약 배지 배경 (Stitch secondary-container) |
| `--color-info-text` | `#3D4756` | 정보 배경 위 텍스트 (Stitch on-secondary-fixed-variant) |

### 차트 팔레트

Recharts(SVG)는 CSS 변수를 받지 못하므로 `frontend/src/constants/chartColors.ts`에 같은 값을 둔다.

| 이름 | 값 | 용도 |
|---|---|---|
| `sent` | `#D1D5DB` | 발송 (기준량, 가장 옅게) |
| `open` | `#FBBF24` | 오픈 |
| `click` | `#F59E0B` | 클릭 |
| `conversion` | `#10B981` | 전환 (결과, 가장 강조) |
| `segment` | `#F59E0B` | 단일 계열 막대 (세그먼트 규모 등) |
| `grid` | `#E5E7EB` | 격자선 |
| `axis` | `#6B7280` | 축 텍스트 |
| `tooltipBorder` | `#E5E7EB` | 툴팁 테두리 |
| `text` | `#1F2937` | 툴팁 텍스트 |

## 타이포그래피

```
--font-sans:    'Pretendard Variable', Pretendard, -apple-system, BlinkMacSystemFont, system-ui, 'Segoe UI', 'Noto Sans KR', sans-serif
--font-display: 'Plus Jakarta Sans', 'Pretendard Variable', Pretendard, -apple-system, system-ui, sans-serif
```

폰트는 `frontend/index.html`에서 CDN으로 불러온다 (Pretendard: jsDelivr, Plus Jakarta Sans: Google Fonts).

- 본문은 `--font-sans`(Pretendard). 제목과 숫자(KPI, 금액)는 `--font-display`(영문/숫자는 Plus Jakarta Sans, 한글은 Pretendard로 표시).
- 한글 텍스트 자간은 `-0.02em` 이내로 유지한다.
- 숫자 열은 `font-variant-numeric: tabular-nums`로 자릿수를 맞춘다.

| 스타일 | 변수 (size / line-height) | weight | 용도 |
|---|---|---|---|
| headline-xl | `--text-headline-xl` 32px / 40px | 800 | 페이지 제목 |
| headline-lg | `--text-headline-lg` 24px / 32px | 700 | 큰 숫자, 모바일 페이지 제목 |
| metric | `--text-metric` 28px / 36px | 800 | KPI 카드 값 (display 폰트, `tabular-nums`) |
| headline-md | `--text-headline-md` 18px / 26px | 700 | 카드/섹션 제목 |
| body-lg | `--text-body-lg` 16px / 24px | 500 | 강조 본문 |
| body-md | `--text-body-md` 14px / 20px | 400 | 기본 본문, 테이블, 입력 |
| body-sm | `--text-body-sm` 13px / 18px | 400 | 보조 설명, 힌트 |
| label-lg | `--text-label-lg` 14px / 18px | 700 | 버튼, 메뉴 |
| label-md | `--text-label-md` 12px / 16px | 700 | 테이블 헤더, 폼 라벨, 배지 |
| label-sm | `--text-label-sm` 11px / 14px | 600 | 아주 작은 메타 라벨 |

## Radius

| 변수 | 값 | 용도 |
|---|---|---|
| `--radius-sm` | 8px | 작은 요소 (메뉴 아이템, 툴팁) |
| `--radius-input` | 12px | 입력, select, textarea |
| `--radius-card` | 16px | 카드, 테이블 컨테이너, 상태 표시 박스 |
| `--radius-pill` | 9999px | 버튼, 배지, 칩, 검색 입력 |

## Spacing (8pt 그리드, 4pt 보조)

| 변수 | 값 |
|---|---|
| `--space-xs` | 4px |
| `--space-sm` | 8px |
| `--space-md` | 16px |
| `--space-lg` | 24px |
| `--space-xl` | 32px |
| `--space-2xl` | 48px |

## Elevation

| 변수 | 값 | 용도 |
|---|---|---|
| `--shadow-1` | `0 2px 8px -2px rgba(31, 41, 55, 0.05)` | 카드, 테이블 (1px border와 함께) |
| `--shadow-2` | `0 8px 20px -4px rgba(31, 41, 55, 0.08)` | hover된 카드, 고정 헤더 |
| `--shadow-3` | `0 12px 32px -6px rgba(245, 158, 11, 0.28), 0 4px 12px -2px rgba(31, 41, 55, 0.08)` | Primary 버튼 hover, 떠 있는 요소 (노란빛 그림자) |
| `--focus-ring` | `0 0 0 3px rgba(245, 158, 11, 0.35)` | 키보드 포커스 표시 |

## Motion

| 변수 | 값 | 용도 |
|---|---|---|
| `--transition-fast` | `120ms ease-out` | 색상/배경 변화 |
| `--transition-bounce` | `180ms cubic-bezier(0.34, 1.56, 0.64, 1)` | 버튼 눌림, 카드 hover 이동 |
