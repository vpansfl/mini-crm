type StatCardProps = {
  label: string
  value: string
  hint?: string
  // 상단 노란 바로 강조한다. 대시보드 핵심 지표 1~2개에만 사용한다.
  accent?: boolean
}

function StatCard({ label, value, hint, accent = false }: StatCardProps) {
  return (
    <div className={accent ? 'card stat-card stat-card-accent' : 'card stat-card'}>
      <span className="stat-label">{label}</span>
      <strong className="stat-value">{value}</strong>
      {hint && <span className="stat-hint">{hint}</span>}
    </div>
  )
}

export default StatCard
