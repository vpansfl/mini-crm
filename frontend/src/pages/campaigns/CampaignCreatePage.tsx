import { useState } from 'react'
import { Link, useNavigate } from 'react-router'
import { useCreateCampaign } from '../../api/campaigns.ts'
import { useSegments } from '../../api/segments.ts'
import PageHeader from '../../components/PageHeader.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import {
  CAMPAIGN_CHANNEL_LABELS,
  CAMPAIGN_CHANNELS,
  CAMPAIGN_STATUSES,
  CAMPAIGN_STATUS_LABELS,
} from '../../constants/labels.ts'
import { ROUTES, campaignDetailPath } from '../../constants/routes.ts'
import type { CampaignChannel, CampaignStatus } from '../../types/campaign.ts'
import { formatNumber } from '../../utils/format.ts'

function CampaignCreatePage() {
  const navigate = useNavigate()
  const { data: segments, isPending, isError, error, refetch } = useSegments()
  const createCampaign = useCreateCampaign()

  const [name, setName] = useState('')
  const [segmentId, setSegmentId] = useState('')
  const [channel, setChannel] = useState<CampaignChannel>('EMAIL')
  const [message, setMessage] = useState('')
  const [status, setStatus] = useState<CampaignStatus>('DRAFT')
  const [startAt, setStartAt] = useState('')
  const [endAt, setEndAt] = useState('')

  const back = { to: ROUTES.CAMPAIGNS, label: '캠페인 목록' }

  if (isPending) {
    return (
      <>
        <PageHeader title="새 캠페인" back={back} />
        <LoadingState message="세그먼트를 불러오는 중..." />
      </>
    )
  }
  if (isError) {
    return (
      <>
        <PageHeader title="새 캠페인" back={back} />
        <ErrorState error={error} title="세그먼트를 불러오지 못했습니다." onRetry={() => refetch()} />
      </>
    )
  }
  if (segments.length === 0) {
    return (
      <>
        <PageHeader title="새 캠페인" back={back} />
        <EmptyState
          title="대상 세그먼트가 없습니다."
          description="캠페인을 만들려면 먼저 세그먼트를 생성하세요."
          action={
            <Link className="btn btn-primary" to={ROUTES.SEGMENT_NEW}>
              새 세그먼트
            </Link>
          }
        />
      </>
    )
  }

  const selectedSegment = segments.find((segment) => String(segment.id) === segmentId)
  const scheduleInvalid = startAt !== '' && endAt !== '' && endAt < startAt
  const canSubmit =
    name.trim() !== '' && selectedSegment !== undefined && message.trim() !== '' && !scheduleInvalid && !createCampaign.isPending

  const handleSubmit = (event: { preventDefault: () => void }) => {
    event.preventDefault()
    if (!canSubmit || !selectedSegment) {
      return
    }
    createCampaign.mutate(
      {
        name: name.trim(),
        segmentId: selectedSegment.id,
        channel,
        message: message.trim(),
        status,
        startAt: startAt || null,
        endAt: endAt || null,
      },
      { onSuccess: (campaign) => navigate(campaignDetailPath(campaign.id)) },
    )
  }

  return (
    <>
      <PageHeader
        title="새 캠페인"
        description="대상 세그먼트와 채널, 메시지를 지정합니다. 실제 메시지는 발송되지 않습니다."
        back={back}
      />

      {createCampaign.isError && <div className="notice notice-error">저장 실패: {createCampaign.error.message}</div>}

      <form className="form card" onSubmit={handleSubmit}>
        <label className="field">
          <span>캠페인 이름</span>
          <input
            className="input"
            value={name}
            maxLength={100}
            required
            placeholder="예: 10월 재방문 프로모션"
            onChange={(event) => setName(event.target.value)}
          />
        </label>

        <div className="form-row form-row-2">
          <label className="field">
            <span>대상 세그먼트</span>
            <select className="select" value={segmentId} required onChange={(event) => setSegmentId(event.target.value)}>
              <option value="">세그먼트 선택</option>
              {segments.map((segment) => (
                <option key={segment.id} value={segment.id}>
                  {segment.name} ({formatNumber(segment.customerCount)}명)
                </option>
              ))}
            </select>
            <span className="field-hint">
              {selectedSegment
                ? `타겟 규모: ${formatNumber(selectedSegment.customerCount)}명`
                : '세그먼트를 선택하면 타겟 규모가 표시됩니다.'}
            </span>
          </label>

          <label className="field">
            <span>채널</span>
            <select
              className="select"
              value={channel}
              onChange={(event) => setChannel(event.target.value as CampaignChannel)}
            >
              {CAMPAIGN_CHANNELS.map((value) => (
                <option key={value} value={value}>
                  {CAMPAIGN_CHANNEL_LABELS[value]}
                </option>
              ))}
            </select>
          </label>
        </div>

        <label className="field">
          <span>메시지</span>
          <textarea
            className="textarea"
            value={message}
            maxLength={2000}
            required
            placeholder="고객에게 보낼 메시지 내용"
            onChange={(event) => setMessage(event.target.value)}
          />
          <span className="field-hint">{message.length} / 2000</span>
        </label>

        <div className="form-row">
          <label className="field">
            <span>상태</span>
            <select
              className="select"
              value={status}
              onChange={(event) => setStatus(event.target.value as CampaignStatus)}
            >
              {CAMPAIGN_STATUSES.map((value) => (
                <option key={value} value={value}>
                  {CAMPAIGN_STATUS_LABELS[value]}
                </option>
              ))}
            </select>
          </label>

          <label className="field">
            <span>시작 일시 (선택)</span>
            <input
              className="input"
              type="datetime-local"
              value={startAt}
              onChange={(event) => setStartAt(event.target.value)}
            />
          </label>

          <label className="field">
            <span>종료 일시 (선택)</span>
            <input
              className="input"
              type="datetime-local"
              value={endAt}
              onChange={(event) => setEndAt(event.target.value)}
            />
          </label>
        </div>
        {scheduleInvalid && <div className="notice notice-error">종료 일시는 시작 일시보다 빠를 수 없습니다.</div>}

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={!canSubmit}>
            {createCampaign.isPending ? '저장 중...' : '캠페인 생성'}
          </button>
          <button type="button" className="btn btn-ghost" onClick={() => navigate(ROUTES.CAMPAIGNS)}>
            취소
          </button>
        </div>
      </form>
    </>
  )
}

export default CampaignCreatePage
