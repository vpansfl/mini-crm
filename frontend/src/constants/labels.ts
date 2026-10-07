import type { CampaignChannel, CampaignStatus } from '../types/campaign.ts'
import type { SegmentField, SegmentOperator, SegmentRule, SegmentValueType } from '../types/segment.ts'

// 백엔드 SegmentField / SegmentField.ValueType과 같은 규칙을 유지한다.
export const SEGMENT_FIELDS: { value: SegmentField; label: string; valueType: SegmentValueType }[] = [
  { value: 'EMAIL', label: '이메일', valueType: 'TEXT' },
  { value: 'COMPANY_NAME', label: '회사명', valueType: 'TEXT' },
  { value: 'CITY', label: '도시', valueType: 'TEXT' },
  { value: 'COUNTRY', label: '국가', valueType: 'TEXT' },
  { value: 'LIFECYCLE_STAGE', label: '라이프사이클 단계', valueType: 'TEXT' },
  { value: 'LEAD_STATUS', label: '리드 상태', valueType: 'TEXT' },
  { value: 'CREATED_AT', label: '생성일', valueType: 'DATE' },
]

export const OPERATORS_BY_VALUE_TYPE: Record<SegmentValueType, SegmentOperator[]> = {
  TEXT: ['EQ', 'CONTAINS', 'NOT_CONTAINS', 'EXISTS'],
  DATE: ['EQ', 'GT', 'GTE', 'LT', 'LTE', 'EXISTS'],
}

export const SEGMENT_OPERATOR_LABELS: Record<SegmentOperator, string> = {
  EQ: '같음',
  CONTAINS: '포함',
  NOT_CONTAINS: '포함하지 않음',
  EXISTS: '값이 있음',
  GT: '이후 (초과)',
  GTE: '이후 (이상)',
  LT: '이전 (미만)',
  LTE: '이전 (이하)',
}

export function segmentFieldLabel(field: SegmentField): string {
  return SEGMENT_FIELDS.find((item) => item.value === field)?.label ?? field
}

export function segmentValueType(field: SegmentField): SegmentValueType {
  return SEGMENT_FIELDS.find((item) => item.value === field)?.valueType ?? 'TEXT'
}

export function operatorsFor(field: SegmentField): SegmentOperator[] {
  return OPERATORS_BY_VALUE_TYPE[segmentValueType(field)]
}

export function operatorRequiresValue(operator: SegmentOperator): boolean {
  return operator !== 'EXISTS'
}

// 예: 국가 같음 "KR"
export function describeSegmentRule(rule: SegmentRule): string {
  const base = `${segmentFieldLabel(rule.ruleField)} ${SEGMENT_OPERATOR_LABELS[rule.ruleOperator]}`
  return rule.ruleValue ? `${base} "${rule.ruleValue}"` : base
}

export const CAMPAIGN_CHANNEL_LABELS: Record<CampaignChannel, string> = {
  EMAIL: '이메일',
  SMS: 'SMS',
  OTHER: '기타',
}

export const CAMPAIGN_STATUS_LABELS: Record<CampaignStatus, string> = {
  DRAFT: '초안',
  SCHEDULED: '예약됨',
  RUNNING: '진행 중',
  COMPLETED: '완료',
}

export const CAMPAIGN_CHANNELS = Object.keys(CAMPAIGN_CHANNEL_LABELS) as CampaignChannel[]
export const CAMPAIGN_STATUSES = Object.keys(CAMPAIGN_STATUS_LABELS) as CampaignStatus[]
