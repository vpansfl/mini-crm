import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { Campaign, CampaignMetrics, CampaignRequest } from '../types/campaign.ts'
import { dashboardKeys } from './dashboard.ts'
import { http } from './http.ts'

export const campaignKeys = {
  all: ['campaigns'] as const,
  list: () => [...campaignKeys.all, 'list'] as const,
  detail: (id: number) => [...campaignKeys.all, 'detail', id] as const,
}

export function fetchCampaigns(): Promise<Campaign[]> {
  return http.get<Campaign[]>('/api/campaigns')
}

export function fetchCampaign(id: number): Promise<Campaign> {
  return http.get<Campaign>(`/api/campaigns/${id}`)
}

// 성과가 없으면 404. 화면에서는 캠페인 응답의 metrics 필드를 사용한다.
export function fetchCampaignMetrics(id: number): Promise<CampaignMetrics> {
  return http.get<CampaignMetrics>(`/api/campaigns/${id}/metrics`)
}

export function createCampaign(request: CampaignRequest): Promise<Campaign> {
  return http.post<Campaign>('/api/campaigns', request)
}

export function updateCampaign(id: number, request: CampaignRequest): Promise<Campaign> {
  return http.put<Campaign>(`/api/campaigns/${id}`, request)
}

export function deleteCampaign(id: number): Promise<void> {
  return http.delete(`/api/campaigns/${id}`)
}

export function simulateCampaignResults(id: number): Promise<CampaignMetrics> {
  return http.post<CampaignMetrics>(`/api/campaigns/${id}/simulate-results`)
}

export function useCampaigns() {
  return useQuery({
    queryKey: campaignKeys.list(),
    queryFn: fetchCampaigns,
  })
}

export function useCampaign(id: number) {
  return useQuery({
    queryKey: campaignKeys.detail(id),
    queryFn: () => fetchCampaign(id),
    enabled: Number.isInteger(id) && id > 0,
  })
}

function useInvalidateCampaignRelated() {
  const queryClient = useQueryClient()
  return () =>
    Promise.all([
      queryClient.invalidateQueries({ queryKey: campaignKeys.all }),
      queryClient.invalidateQueries({ queryKey: dashboardKeys.all }),
    ])
}

export function useCreateCampaign() {
  const invalidate = useInvalidateCampaignRelated()
  return useMutation({
    mutationFn: createCampaign,
    onSuccess: invalidate,
  })
}

export function useUpdateCampaign(id: number) {
  const invalidate = useInvalidateCampaignRelated()
  return useMutation({
    mutationFn: (request: CampaignRequest) => updateCampaign(id, request),
    onSuccess: invalidate,
  })
}

export function useSimulateCampaignResults(id: number) {
  const invalidate = useInvalidateCampaignRelated()
  return useMutation({
    mutationFn: () => simulateCampaignResults(id),
    onSuccess: invalidate,
  })
}

export function useDeleteCampaign() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: deleteCampaign,
    // 삭제된 상세 조회가 다시 실행되지 않도록 목록과 대시보드만 갱신한다.
    onSuccess: () =>
      Promise.all([
        queryClient.invalidateQueries({ queryKey: campaignKeys.list() }),
        queryClient.invalidateQueries({ queryKey: dashboardKeys.all }),
      ]),
  })
}
