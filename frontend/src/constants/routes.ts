export const ROUTES = {
  DASHBOARD: '/dashboard',
  CUSTOMERS: '/customers',
  SEGMENTS: '/segments',
  SEGMENT_NEW: '/segments/new',
  CAMPAIGNS: '/campaigns',
  CAMPAIGN_NEW: '/campaigns/new',
} as const

export const customerDetailPath = (customerId: number) => `${ROUTES.CUSTOMERS}/${customerId}`

export const segmentDetailPath = (segmentId: number) => `${ROUTES.SEGMENTS}/${segmentId}`

export const campaignDetailPath = (campaignId: number) => `${ROUTES.CAMPAIGNS}/${campaignId}`
