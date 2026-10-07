import { Link, useNavigate, useParams } from 'react-router'
import {
  useCampaign,
  useDeleteCampaign,
  useSimulateCampaignResults,
  useUpdateCampaign,
} from '../../api/campaigns.ts'
import { isNotFound } from '../../api/http.ts'
import PageHeader from '../../components/PageHeader.tsx'
import StatCard from '../../components/StatCard.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { CampaignStatusBadge } from '../../components/StatusBadge.tsx'
import { CAMPAIGN_CHANNEL_LABELS, CAMPAIGN_STATUSES, CAMPAIGN_STATUS_LABELS } from '../../constants/labels.ts'
import { ROUTES, segmentDetailPath } from '../../constants/routes.ts'
import type { Campaign, CampaignMetrics, CampaignStatus } from '../../types/campaign.ts'
import { formatCurrency, formatDateTime, formatNumber, formatPercent } from '../../utils/format.ts'

function MetricsSection({ metrics }: { metrics: CampaignMetrics | null }) {
  if (!metrics) {
    return (
      <EmptyState
        title="아직 성과 데이터가 없습니다."
        description="성과 시뮬레이션 버튼을 누르면 대상 고객 수를 기준으로 성과를 생성합니다."
      />
    )
  }
  return (
    <>
      <div className="grid grid-4 section">
        <StatCard label="발송" value={formatNumber(metrics.sentCount)} />
        <StatCard label="오픈" value={formatNumber(metrics.openCount)} hint={`오픈율 ${formatPercent(metrics.openRate)}`} />
        <StatCard
          label="클릭"
          value={formatNumber(metrics.clickCount)}
          hint={`클릭률 ${formatPercent(metrics.clickRate)}`}
        />
        <StatCard
          label="전환"
          value={formatNumber(metrics.conversionCount)}
          hint={`전환율 ${formatPercent(metrics.conversionRate)}`}
        />
      </div>
      <div className="grid grid-4">
        <StatCard label="매출" value={formatCurrency(metrics.revenue)} hint={`갱신: ${formatDateTime(metrics.updatedAt)}`} />
      </div>
      <p className="muted">비율은 모두 발송 수 대비입니다.</p>
    </>
  )
}

function toRequest(campaign: Campaign, status: CampaignStatus) {
  return {
    name: campaign.name,
    segmentId: campaign.segmentId,
    channel: campaign.channel,
    message: campaign.message,
    status,
    startAt: campaign.startAt,
    endAt: campaign.endAt,
  }
}

function CampaignDetailPage() {
  const navigate = useNavigate()
  const { campaignId } = useParams()
  const id = Number(campaignId)
  const isValidId = Number.isInteger(id) && id > 0
  const { data: campaign, isPending, isError, error, refetch } = useCampaign(id)
  const updateCampaign = useUpdateCampaign(id)
  const simulate = useSimulateCampaignResults(id)
  const deleteCampaign = useDeleteCampaign()
  const back = { to: ROUTES.CAMPAIGNS, label: '캠페인 목록' }

  if (!isValidId || (isError && isNotFound(error))) {
    return (
      <>
        <PageHeader title="캠페인 상세" back={back} />
        <EmptyState title="캠페인을 찾을 수 없습니다." />
      </>
    )
  }
  if (isPending) {
    return (
      <>
        <PageHeader title="캠페인 상세" back={back} />
        <LoadingState message="캠페인을 불러오는 중..." />
      </>
    )
  }
  if (isError) {
    return (
      <>
        <PageHeader title="캠페인 상세" back={back} />
        <ErrorState error={error} title="캠페인을 불러오지 못했습니다." onRetry={() => refetch()} />
      </>
    )
  }

  const handleDelete = () => {
    if (!window.confirm(`"${campaign.name}" 캠페인을 삭제할까요? 성과 데이터도 함께 삭제됩니다.`)) {
      return
    }
    deleteCampaign.mutate(id, { onSuccess: () => navigate(ROUTES.CAMPAIGNS) })
  }

  const busy = updateCampaign.isPending || simulate.isPending || deleteCampaign.isPending
  const mutationError = updateCampaign.error ?? simulate.error ?? deleteCampaign.error

  return (
    <>
      <PageHeader
        title={campaign.name}
        description={
          <>
            <CampaignStatusBadge status={campaign.status} /> {CAMPAIGN_CHANNEL_LABELS[campaign.channel]} 캠페인
          </>
        }
        back={back}
        actions={
          <>
            <button type="button" className="btn btn-primary" onClick={() => simulate.mutate()} disabled={busy}>
              {simulate.isPending ? '시뮬레이션 중...' : campaign.metrics ? '성과 다시 시뮬레이션' : '성과 시뮬레이션'}
            </button>
            <button type="button" className="btn btn-danger" onClick={handleDelete} disabled={busy}>
              {deleteCampaign.isPending ? '삭제 중...' : '삭제'}
            </button>
          </>
        }
      />

      {mutationError && <div className="notice notice-error">요청 실패: {mutationError.message}</div>}

      <div className="grid grid-2 section">
        <section className="card">
          <h2 className="card-title">캠페인 정보</h2>
          <dl className="detail-list">
            <dt>대상 세그먼트</dt>
            <dd>
              <Link to={segmentDetailPath(campaign.segmentId)}>{campaign.segmentName}</Link>
            </dd>
            <dt>타겟 규모</dt>
            <dd>{formatNumber(campaign.targetCount)}명</dd>
            <dt>채널</dt>
            <dd>{CAMPAIGN_CHANNEL_LABELS[campaign.channel]}</dd>
            <dt>상태</dt>
            <dd>
              <select
                className="select"
                value={campaign.status}
                disabled={busy}
                aria-label="캠페인 상태 변경"
                onChange={(event) => updateCampaign.mutate(toRequest(campaign, event.target.value as CampaignStatus))}
              >
                {CAMPAIGN_STATUSES.map((value) => (
                  <option key={value} value={value}>
                    {CAMPAIGN_STATUS_LABELS[value]}
                  </option>
                ))}
              </select>
            </dd>
            <dt>시작 일시</dt>
            <dd>{formatDateTime(campaign.startAt)}</dd>
            <dt>종료 일시</dt>
            <dd>{formatDateTime(campaign.endAt)}</dd>
            <dt>생성일</dt>
            <dd>{formatDateTime(campaign.createdAt)}</dd>
          </dl>
        </section>

        <section className="card">
          <h2 className="card-title">메시지</h2>
          <pre className="message-box">{campaign.message}</pre>
        </section>
      </div>

      <h2 className="card-title">성과</h2>
      <MetricsSection metrics={campaign.metrics} />
    </>
  )
}

export default CampaignDetailPage
