import type { DateTimeString } from './common.ts'

export type CampaignChannel = 'EMAIL' | 'SMS' | 'OTHER'

export type CampaignStatus = 'DRAFT' | 'SCHEDULED' | 'RUNNING' | 'COMPLETED'

// GET/POST /api/campaigns/{id}/metrics, simulate-results. 비율은 발송 수 대비 0~1
export type CampaignMetrics = {
  campaignId: number
  sentCount: number
  openCount: number
  clickCount: number
  conversionCount: number
  revenue: number
  openRate: number
  clickRate: number
  conversionRate: number
  createdAt: DateTimeString
  updatedAt: DateTimeString
}

// GET /api/campaigns, GET /api/campaigns/{id}
export type Campaign = {
  id: number
  name: string
  segmentId: number
  segmentName: string
  channel: CampaignChannel
  message: string
  status: CampaignStatus
  startAt: DateTimeString | null
  endAt: DateTimeString | null
  // 대상 세그먼트의 현재 고객 수
  targetCount: number
  // 성과 시뮬레이션 전에는 null
  metrics: CampaignMetrics | null
  createdAt: DateTimeString
  updatedAt: DateTimeString
}

// POST /api/campaigns, PUT /api/campaigns/{id}
export type CampaignRequest = {
  name: string
  segmentId: number
  channel: CampaignChannel
  message: string
  status: CampaignStatus | null
  startAt: DateTimeString | null
  endAt: DateTimeString | null
}
