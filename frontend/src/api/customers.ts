import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { SyncResult } from '../types/common.ts'
import type { Customer } from '../types/customer.ts'
import { campaignKeys } from './campaigns.ts'
import { dashboardKeys } from './dashboard.ts'
import { http } from './http.ts'
import { segmentKeys } from './segments.ts'

export const customerKeys = {
  all: ['customers'] as const,
  list: () => [...customerKeys.all, 'list'] as const,
  detail: (id: number) => [...customerKeys.all, 'detail', id] as const,
}

export function fetchCustomers(): Promise<Customer[]> {
  return http.get<Customer[]>('/api/customers')
}

export function fetchCustomer(id: number): Promise<Customer> {
  return http.get<Customer>(`/api/customers/${id}`)
}

export function syncCustomers(): Promise<SyncResult> {
  return http.post<SyncResult>('/api/customers/sync')
}

export function useCustomers() {
  return useQuery({
    queryKey: customerKeys.list(),
    queryFn: fetchCustomers,
  })
}

export function useCustomer(id: number) {
  return useQuery({
    queryKey: customerKeys.detail(id),
    queryFn: () => fetchCustomer(id),
    enabled: Number.isInteger(id) && id > 0,
  })
}

export function useSyncCustomers() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: syncCustomers,
    // 동기화 후 백엔드가 세그먼트 멤버를 다시 계산하므로 세그먼트/캠페인 대상 수/대시보드도 다시 조회한다.
    onSuccess: () =>
      Promise.all([
        queryClient.invalidateQueries({ queryKey: customerKeys.all }),
        queryClient.invalidateQueries({ queryKey: segmentKeys.all }),
        queryClient.invalidateQueries({ queryKey: campaignKeys.all }),
        queryClient.invalidateQueries({ queryKey: dashboardKeys.all }),
      ]),
  })
}
