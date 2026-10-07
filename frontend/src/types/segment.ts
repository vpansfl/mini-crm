import type { DateTimeString } from './common.ts'

export type SegmentField =
  | 'EMAIL'
  | 'COMPANY_NAME'
  | 'CITY'
  | 'COUNTRY'
  | 'LIFECYCLE_STAGE'
  | 'LEAD_STATUS'
  | 'CREATED_AT'

export type SegmentOperator = 'EQ' | 'CONTAINS' | 'NOT_CONTAINS' | 'EXISTS' | 'GT' | 'GTE' | 'LT' | 'LTE'

// 필드 값의 종류. 종류별로 사용할 수 있는 연산자가 다르다. (백엔드 SegmentField.ValueType과 동일)
export type SegmentValueType = 'TEXT' | 'DATE'

export type SegmentRule = {
  ruleField: SegmentField
  ruleOperator: SegmentOperator
  // EXISTS는 값이 없다. DATE 필드는 yyyy-MM-dd
  ruleValue: string | null
}

// GET /api/segments, GET /api/segments/{id}
export type Segment = SegmentRule & {
  id: number
  name: string
  description: string | null
  customerCount: number
  refreshedAt: DateTimeString | null
  createdAt: DateTimeString
  updatedAt: DateTimeString
}

// POST /api/segments, PUT /api/segments/{id}
export type SegmentRequest = SegmentRule & {
  name: string
  description: string | null
}

// POST /api/segments/preview
export type SegmentPreview = {
  customerCount: number
}
