import { useQuery } from '@tanstack/react-query'
import type { DashboardSummary } from '../types/dashboard.ts'
import { http } from './http.ts'

export const dashboardKeys = {
  all: ['dashboard'] as const,
  summary: () => [...dashboardKeys.all, 'summary'] as const,
}

export function fetchDashboardSummary(): Promise<DashboardSummary> {
  return http.get<DashboardSummary>('/api/dashboard/summary')
}

export function useDashboardSummary() {
  return useQuery({
    queryKey: dashboardKeys.summary(),
    queryFn: fetchDashboardSummary,
  })
}
