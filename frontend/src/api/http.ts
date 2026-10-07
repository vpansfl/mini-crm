import type { FieldError, ProblemDetail } from '../types/common.ts'

// Spring Boot API 호출 공통 함수.
// - 로컬: VITE_API_BASE_URL을 비워 두면 상대 경로(/api/...)로 요청하고 Vite 프록시가 백엔드로 전달한다.
// - 배포: VITE_API_BASE_URL에 백엔드 주소(예: https://mini-crm-api.onrender.com)를 지정한다.
// 프론트에는 비밀값(HubSpot 토큰 등)을 두지 않는다. VITE_ 변수는 번들에 그대로 포함되기 때문이다.
const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL ?? '').replace(/\/+$/, '')

export class ApiError extends Error {
  readonly status: number
  readonly fieldErrors: FieldError[]

  constructor(status: number, message: string, fieldErrors: FieldError[] = []) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.fieldErrors = fieldErrors
  }
}

function isProblemDetail(body: unknown): body is ProblemDetail {
  return typeof body === 'object' && body !== null && ('detail' in body || 'title' in body)
}

// 백엔드 오류 응답(ProblemDetail)의 detail 메시지를 우선 사용한다.
async function toApiError(response: Response): Promise<ApiError> {
  let message = `요청에 실패했습니다. (status=${response.status})`
  let fieldErrors: FieldError[] = []
  try {
    const body: unknown = await response.json()
    if (isProblemDetail(body)) {
      message = body.detail ?? body.title ?? message
      fieldErrors = body.errors ?? []
    }
  } catch {
    // JSON이 아닌 응답(프록시 오류 등)은 기본 메시지를 사용한다.
  }
  return new ApiError(response.status, message, fieldErrors)
}

export async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...init,
      headers: { Accept: 'application/json', ...init?.headers },
    })
  } catch {
    throw new ApiError(0, '서버에 연결할 수 없습니다. 백엔드 서버가 실행 중인지 확인하세요.')
  }

  if (!response.ok) {
    throw await toApiError(response)
  }
  if (response.status === 204) {
    return undefined as T
  }
  return (await response.json()) as T
}

function withJsonBody(method: string, body: unknown): RequestInit {
  if (body === undefined) {
    return { method }
  }
  return {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  }
}

export const http = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, body?: unknown) => request<T>(path, withJsonBody('POST', body)),
  put: <T>(path: string, body: unknown) => request<T>(path, withJsonBody('PUT', body)),
  delete: (path: string) => request<void>(path, { method: 'DELETE' }),
}

export function isNotFound(error: unknown): boolean {
  return error instanceof ApiError && error.status === 404
}
