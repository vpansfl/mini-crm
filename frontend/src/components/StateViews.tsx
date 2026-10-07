import type { ReactNode } from 'react'

// 서버 데이터 조회 상태(loading / error / empty)를 일관된 모양으로 표시한다.

export function LoadingState({ message = '불러오는 중...' }: { message?: string }) {
  return (
    <div className="state-view" role="status">
      <span className="spinner" aria-hidden="true" />
      {message}
    </div>
  )
}

type ErrorStateProps = {
  error: Error
  title?: string
  onRetry?: () => void
}

export function ErrorState({ error, title = '데이터를 불러오지 못했습니다.', onRetry }: ErrorStateProps) {
  return (
    <div className="state-view state-error" role="alert">
      <strong>{title}</strong>
      <span>{error.message}</span>
      {onRetry && (
        <button type="button" className="btn btn-secondary" onClick={onRetry}>
          다시 시도
        </button>
      )}
    </div>
  )
}

type EmptyStateProps = {
  title: string
  description?: ReactNode
  action?: ReactNode
}

export function EmptyState({ title, description, action }: EmptyStateProps) {
  return (
    <div className="state-view state-empty">
      <strong>{title}</strong>
      {description && <span>{description}</span>}
      {action}
    </div>
  )
}
