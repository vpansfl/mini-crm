import type { CampaignChannel, CampaignStatus } from './campaign.ts'

export type RecentCampaign = {
  id: number
  name: string
  channel: CampaignChannel
  status: CampaignStatus
  segmentName: string
  targetCount: number
  sentCount: number
  openCount: number
  clickCount: number
  conversionCount: number
  revenue: number
}

export type SegmentSize = {
  id: number
  name: string
  customerCount: number
}

// GET /api/dashboard/summary. 비율은 전체 발송 수 대비 0~1
export type DashboardSummary = {
  totalCustomers: number
  totalSegments: number
  totalCampaigns: number
  activeCampaigns: number
  totalSent: number
  totalOpen: number
  totalClicks: number
  totalConversions: number
  totalRevenue: number
  openRate: number
  clickRate: number
  conversionRate: number
  recentCampaigns: RecentCampaign[]
  segmentSizes: SegmentSize[]
}
