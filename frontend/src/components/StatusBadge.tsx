import { CAMPAIGN_STATUS_LABELS } from '../constants/labels.ts'
import type { CampaignStatus } from '../types/campaign.ts'

// 배지 tone 규칙: docs/design-system/components.md "배지"
export type BadgeTone = 'neutral' | 'info' | 'success' | 'dark' | 'warning' | 'danger'

const STATUS_TONES: Record<CampaignStatus, BadgeTone> = {
  DRAFT: 'neutral',
  SCHEDULED: 'info',
  RUNNING: 'success',
  COMPLETED: 'dark',
}

export function Badge({ tone = 'neutral', children }: { tone?: BadgeTone; children: string }) {
  return <span className={`badge badge-${tone}`}>{children}</span>
}

export function CampaignStatusBadge({ status }: { status: CampaignStatus }) {
  return <Badge tone={STATUS_TONES[status]}>{CAMPAIGN_STATUS_LABELS[status]}</Badge>
}
