# Mini CRM 디자인 시스템 — Warm Honey

Stitch에서 받은 **Warm Honey Delivery** 디자인 시스템([source/stitch.md](source/stitch.md))을 CRM 관리 화면에 맞게 정리한 문서다.
**모든 스타일/UI 작업은 이 문서를 먼저 확인하고 진행한다.**

## 문서 구성

| 문서 | 내용 |
|---|---|
| [tokens.md](tokens.md) | 색상, 타이포그래피, radius, spacing, elevation 토큰과 CSS 변수 이름 |
| [components.md](components.md) | 버튼, 카드, 배지, 테이블, 폼, 상태 표시, 차트 규칙 |
| [layout.md](layout.md) | 앱 셸(사이드바/본문), 페이지 구조, 그리드, 반응형 기준 |
| [source/stitch.md](source/stitch.md) | Stitch 원본 (참고용, 수정하지 않음) |

## 디자인 원칙

1. **따뜻하고 선명하게** — 따뜻한 흰색 캔버스(`#FBFBFA`) 위에 흰 카드, 차콜 텍스트(`#1F2937`), 꿀색 포인트(`#F59E0B`).
2. **노란색은 아껴 쓴다** — 주요 행동(Primary 버튼), 현재 위치(활성 메뉴), 핵심 강조에만 사용한다. 넓은 면은 흰색/캔버스.
3. **둥글고 부드럽게** — 카드 16px, 입력 12px, 버튼/배지/칩은 pill(완전한 둥근 형태).
4. **숫자는 굵고 촘촘하게** — KPI, 금액, 건수는 display 폰트 + 굵은 weight + 좁은 자간으로 빠르게 읽히게 한다.
5. **상태는 한눈에** — 캠페인 상태 등은 색이 다른 pill 배지로 표시한다.

## 적용 결정 사항 (원본과 다른 점)

원본 Stitch 문서는 YAML 토큰과 본문 설명의 값이 서로 다르고, 배달 앱 전용 요소를 포함한다. 다음과 같이 결정했다.

| 항목 | 결정 |
|---|---|
| 색상 기준 | **본문(설명) 값이 기준.** primary `#F59E0B`, 텍스트 `#1F2937`, 캔버스 `#FBFBFA`. YAML 값은 보조 톤으로만 사용 (예: 노란 배경 위 텍스트 `#855300`, 정보 배지 `#D6E0F3`) |
| Primary 버튼 텍스트 | **차콜(`#1F2937`)**. 흰색은 `#F59E0B` 위 대비가 약 2.1:1로 부족하다 (차콜은 약 8:1) |
| 배달 앱 전용 요소 | Floating Cart Bar, 카테고리 원형 아이콘, 수량 Stepper, 16:9 이미지 카드는 **CRM에서 사용하지 않음**. 형태 언어(pill, 둥근 카드, 노란빛 그림자)만 가져온다 |
| 다크 모드 | **MVP는 라이트 모드만.** 다크 모드는 추후 토큰 재정의로 확장 |
| 폰트 | **Pretendard 기본**(한글/숫자), **Plus Jakarta Sans는 제목·숫자 강조용 display 폰트** (한글은 Pretendard로 대체 표시) |
| 레이아웃 | 사이드바(240px)를 제외한 본문 영역에 **최대 폭 1200px** 적용. 넓은 테이블은 가로 스크롤 |

## 사용 규칙

- 색상, 그림자, radius, 폰트는 **반드시 토큰(CSS 변수)으로만** 사용한다. CSS/TSX에 hex 값을 직접 쓰지 않는다.
  - 예외: SVG 차트(Recharts)는 CSS 변수를 속성으로 받지 못하므로 `frontend/src/constants/chartColors.ts`의 상수를 사용한다. 이 값은 토큰과 같게 유지한다.
- 토큰 정의 위치: `frontend/src/styles/tokens.css`
- 공통 컴포넌트 스타일: `frontend/src/styles/ui.css`, 앱 셸: `frontend/src/layouts/AppLayout.css`
- 새 컴포넌트가 필요하면 [components.md](components.md)의 규칙을 따르고, 새 패턴이면 이 문서에 먼저 추가한 뒤 구현한다.
- 토큰 값을 바꿀 때는 `tokens.md`, `tokens.css`, (차트 색이면) `chartColors.ts`를 함께 수정한다.
