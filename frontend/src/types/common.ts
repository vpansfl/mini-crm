// 백엔드 오류 응답 (RFC 9457 ProblemDetail)
export type ProblemDetail = {
  type?: string
  title?: string
  status?: number
  detail?: string
  instance?: string
  errors?: FieldError[]
}

export type FieldError = {
  field: string
  message: string
}

// 백엔드 LocalDateTime / Instant 직렬화 값 (ISO-8601 문자열)
export type DateTimeString = string

// HubSpot 동기화 결과. fetched = created + updated + unchanged
export type SyncResult = {
  fetched: number
  created: number
  updated: number
  unchanged: number
}
