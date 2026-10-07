import type { Customer } from '../types/customer.ts'

const numberFormat = new Intl.NumberFormat('ko-KR')
const currencyFormat = new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW', maximumFractionDigits: 0 })

export function formatCustomerName(customer: Pick<Customer, 'firstName' | 'lastName'>): string {
  const name = [customer.firstName, customer.lastName].filter(Boolean).join(' ')
  return name || '(이름 없음)'
}

// 백엔드 LocalDateTime(타임존 없음)은 로컬 시간으로, Instant(Z 포함)는 UTC로 해석된다.
export function formatDateTime(value: string | null | undefined): string {
  if (!value) {
    return '-'
  }
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }
  return date.toLocaleString('ko-KR', { dateStyle: 'medium', timeStyle: 'short' })
}

export function formatDate(value: string | null | undefined): string {
  if (!value) {
    return '-'
  }
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }
  return date.toLocaleDateString('ko-KR', { dateStyle: 'medium' })
}

export function formatNumber(value: number): string {
  return numberFormat.format(value)
}

export function formatCurrency(value: number): string {
  return currencyFormat.format(value)
}

// 0~1 비율을 백분율로 표시한다.
export function formatPercent(ratio: number): string {
  return `${(ratio * 100).toFixed(1)}%`
}

export function orDash(value: string | null | undefined): string {
  return value ? value : '-'
}
