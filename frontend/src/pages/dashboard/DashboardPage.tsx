import { Link, useNavigate } from 'react-router'
import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import { useDashboardSummary } from '../../api/dashboard.ts'
import PageHeader from '../../components/PageHeader.tsx'
import StatCard from '../../components/StatCard.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { CampaignStatusBadge } from '../../components/StatusBadge.tsx'
import { CHART_COLORS } from '../../constants/chartColors.ts'
import { CAMPAIGN_CHANNEL_LABELS } from '../../constants/labels.ts'
import { ROUTES, campaignDetailPath } from '../../constants/routes.ts'
import type { RecentCampaign, SegmentSize } from '../../types/dashboard.ts'
import { formatCurrency, formatNumber, formatPercent } from '../../utils/format.ts'

const formatAxisNumber = (value: number) => formatNumber(value)

// 차트 공통 스타일 (docs/design-system/components.md "차트")
const AXIS_TICK = { fontSize: 12, fill: CHART_COLORS.axis }
// 툴팁은 HTML이므로 CSS 변수를 사용할 수 있다.
const TOOLTIP_STYLE = {
  border: `1px solid ${CHART_COLORS.tooltipBorder}`,
  borderRadius: 'var(--radius-input)',
  boxShadow: 'var(--shadow-2)',
  color: CHART_COLORS.text,
  fontSize: 'var(--text-body-sm)',
}
const CURSOR_FILL = { fill: 'var(--color-primary-subtle)' }
const BAR_RADIUS: [number, number, number, number] = [6, 6, 0, 0]

function CampaignPerformanceChart({ campaigns }: { campaigns: RecentCampaign[] }) {
  const data = campaigns
    .filter((campaign) => campaign.sentCount > 0)
    .map((campaign) => ({
      name: campaign.name,
      발송: campaign.sentCount,
      오픈: campaign.openCount,
      클릭: campaign.clickCount,
      전환: campaign.conversionCount,
    }))

  if (data.length === 0) {
    return <EmptyState title="성과 데이터가 없습니다." description="캠페인 상세에서 성과 시뮬레이션을 실행하세요." />
  }
  return (
    <div className="chart-box">
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={data} margin={{ top: 8, right: 8, bottom: 8, left: 0 }}>
          <CartesianGrid stroke={CHART_COLORS.grid} vertical={false} />
          <XAxis dataKey="name" tick={AXIS_TICK} tickLine={false} axisLine={{ stroke: CHART_COLORS.grid }} interval={0} />
          <YAxis
            tick={AXIS_TICK}
            tickLine={false}
            axisLine={false}
            tickFormatter={formatAxisNumber}
            allowDecimals={false}
          />
          <Tooltip
            formatter={(value) => formatNumber(Number(value))}
            contentStyle={TOOLTIP_STYLE}
            cursor={CURSOR_FILL}
          />
          <Legend iconType="circle" wrapperStyle={{ fontSize: 12 }} />
          <Bar dataKey="발송" fill={CHART_COLORS.sent} radius={BAR_RADIUS} />
          <Bar dataKey="오픈" fill={CHART_COLORS.open} radius={BAR_RADIUS} />
          <Bar dataKey="클릭" fill={CHART_COLORS.click} radius={BAR_RADIUS} />
          <Bar dataKey="전환" fill={CHART_COLORS.conversion} radius={BAR_RADIUS} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}

function SegmentSizeChart({ segments }: { segments: SegmentSize[] }) {
  if (segments.length === 0) {
    return <EmptyState title="세그먼트가 없습니다." description="세그먼트를 만들면 규모가 표시됩니다." />
  }
  const data = segments.map((segment) => ({ name: segment.name, 고객수: segment.customerCount }))
  return (
    <div className="chart-box">
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={data} layout="vertical" margin={{ top: 8, right: 16, bottom: 8, left: 8 }}>
          <CartesianGrid stroke={CHART_COLORS.grid} horizontal={false} />
          <XAxis
            type="number"
            tick={AXIS_TICK}
            tickLine={false}
            axisLine={false}
            tickFormatter={formatAxisNumber}
            allowDecimals={false}
          />
          <YAxis type="category" dataKey="name" tick={AXIS_TICK} tickLine={false} axisLine={false} width={120} />
          <Tooltip
            formatter={(value) => `${formatNumber(Number(value))}명`}
            contentStyle={TOOLTIP_STYLE}
            cursor={CURSOR_FILL}
          />
          <Bar dataKey="고객수" fill={CHART_COLORS.segment} radius={[0, 6, 6, 0]} barSize={20} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}

function DashboardPage() {
  const navigate = useNavigate()
  const { data: summary, isPending, isError, error, refetch } = useDashboardSummary()

  let content
  if (isPending) {
    content = <LoadingState message="대시보드를 불러오는 중..." />
  } else if (isError) {
    content = <ErrorState error={error} title="대시보드를 불러오지 못했습니다." onRetry={() => refetch()} />
  } else {
    content = (
      <>
        <div className="grid grid-4 section">
          <StatCard label="고객" value={formatNumber(summary.totalCustomers)} accent />
          <StatCard label="세그먼트" value={formatNumber(summary.totalSegments)} />
          <StatCard
            label="캠페인"
            value={formatNumber(summary.totalCampaigns)}
            hint={`진행 중 ${formatNumber(summary.activeCampaigns)}개`}
          />
          <StatCard label="매출" value={formatCurrency(summary.totalRevenue)} accent />
        </div>

        <div className="grid grid-4 section">
          <StatCard label="발송" value={formatNumber(summary.totalSent)} />
          <StatCard label="오픈" value={formatNumber(summary.totalOpen)} hint={`오픈율 ${formatPercent(summary.openRate)}`} />
          <StatCard
            label="클릭"
            value={formatNumber(summary.totalClicks)}
            hint={`클릭률 ${formatPercent(summary.clickRate)}`}
          />
          <StatCard
            label="전환"
            value={formatNumber(summary.totalConversions)}
            hint={`전환율 ${formatPercent(summary.conversionRate)}`}
          />
        </div>

        <div className="grid grid-2 section">
          <section className="card">
            <h2 className="card-title">캠페인 성과 (최근 10개)</h2>
            <CampaignPerformanceChart campaigns={summary.recentCampaigns} />
          </section>
          <section className="card">
            <h2 className="card-title">세그먼트 규모</h2>
            <SegmentSizeChart segments={summary.segmentSizes} />
          </section>
        </div>

        <section className="section">
          <h2 className="card-title">최근 캠페인</h2>
          {summary.recentCampaigns.length === 0 ? (
            <EmptyState
              title="캠페인이 없습니다."
              action={
                <Link className="btn btn-primary" to={ROUTES.CAMPAIGN_NEW}>
                  새 캠페인
                </Link>
              }
            />
          ) : (
            <div className="table-wrap">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>이름</th>
                    <th>세그먼트</th>
                    <th>채널</th>
                    <th>상태</th>
                    <th className="num">대상</th>
                    <th className="num">발송</th>
                    <th className="num">오픈</th>
                    <th className="num">클릭</th>
                    <th className="num">전환</th>
                    <th className="num">매출</th>
                  </tr>
                </thead>
                <tbody>
                  {summary.recentCampaigns.map((campaign) => (
                    <tr
                      key={campaign.id}
                      className="clickable-row"
                      tabIndex={0}
                      onClick={() => navigate(campaignDetailPath(campaign.id))}
                      onKeyDown={(event) => {
                        if (event.key === 'Enter') {
                          navigate(campaignDetailPath(campaign.id))
                        }
                      }}
                    >
                      <td className="cell-strong">{campaign.name}</td>
                      <td>{campaign.segmentName}</td>
                      <td>{CAMPAIGN_CHANNEL_LABELS[campaign.channel]}</td>
                      <td>
                        <CampaignStatusBadge status={campaign.status} />
                      </td>
                      <td className="num">{formatNumber(campaign.targetCount)}</td>
                      <td className="num">{formatNumber(campaign.sentCount)}</td>
                      <td className="num">{formatNumber(campaign.openCount)}</td>
                      <td className="num">{formatNumber(campaign.clickCount)}</td>
                      <td className="num">{formatNumber(campaign.conversionCount)}</td>
                      <td className="num">{formatCurrency(campaign.revenue)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </>
    )
  }

  return (
    <>
      <PageHeader title="대시보드" description="고객, 세그먼트, 캠페인 성과 요약입니다. 비율은 전체 발송 수 대비입니다." />
      {content}
    </>
  )
}

export default DashboardPage
