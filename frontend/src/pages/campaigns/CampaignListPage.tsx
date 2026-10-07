import { Link, useNavigate } from 'react-router'
import { useCampaigns } from '../../api/campaigns.ts'
import PageHeader from '../../components/PageHeader.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { CampaignStatusBadge } from '../../components/StatusBadge.tsx'
import { CAMPAIGN_CHANNEL_LABELS } from '../../constants/labels.ts'
import { ROUTES, campaignDetailPath } from '../../constants/routes.ts'
import { formatCurrency, formatDate, formatNumber } from '../../utils/format.ts'

function CampaignListPage() {
  const navigate = useNavigate()
  const { data: campaigns, isPending, isError, error, refetch } = useCampaigns()

  let content
  if (isPending) {
    content = <LoadingState message="캠페인을 불러오는 중..." />
  } else if (isError) {
    content = <ErrorState error={error} title="캠페인을 불러오지 못했습니다." onRetry={() => refetch()} />
  } else if (campaigns.length === 0) {
    content = (
      <EmptyState
        title="캠페인이 없습니다."
        description="세그먼트를 대상으로 첫 캠페인을 만들어 보세요."
        action={
          <Link className="btn btn-primary" to={ROUTES.CAMPAIGN_NEW}>
            새 캠페인
          </Link>
        }
      />
    )
  } else {
    content = (
      <div className="table-wrap">
        <table className="data-table">
          <thead>
            <tr>
              <th>이름</th>
              <th>대상 세그먼트</th>
              <th>채널</th>
              <th>상태</th>
              <th className="num">대상 고객</th>
              <th className="num">발송</th>
              <th className="num">전환</th>
              <th className="num">매출</th>
              <th>생성일</th>
            </tr>
          </thead>
          <tbody>
            {campaigns.map((campaign) => (
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
                <td className="num">{campaign.metrics ? formatNumber(campaign.metrics.sentCount) : '-'}</td>
                <td className="num">{campaign.metrics ? formatNumber(campaign.metrics.conversionCount) : '-'}</td>
                <td className="num">{campaign.metrics ? formatCurrency(campaign.metrics.revenue) : '-'}</td>
                <td>{formatDate(campaign.createdAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )
  }

  return (
    <>
      <PageHeader
        title="캠페인"
        description="세그먼트 대상 캠페인입니다. 실제 메시지는 발송되지 않으며 성과는 시뮬레이션합니다."
        actions={
          <Link className="btn btn-primary" to={ROUTES.CAMPAIGN_NEW}>
            새 캠페인
          </Link>
        }
      />
      {content}
    </>
  )
}

export default CampaignListPage
