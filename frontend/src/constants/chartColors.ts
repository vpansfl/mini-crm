// Recharts(SVG)는 CSS 변수를 속성으로 받지 못하므로 토큰 값을 상수로 둔다.
// docs/design-system/tokens.md "차트 팔레트"와 같은 값을 유지한다.
export const CHART_COLORS = {
  sent: '#d1d5db',
  open: '#fbbf24',
  click: '#f59e0b',
  conversion: '#10b981',
  segment: '#f59e0b',
  grid: '#e5e7eb',
  axis: '#6b7280',
  tooltipBorder: '#e5e7eb',
  text: '#1f2937',
} as const
