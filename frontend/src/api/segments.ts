import { skipToken, useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { Customer } from '../types/customer.ts'
import type { Segment, SegmentPreview, SegmentRequest, SegmentRule } from '../types/segment.ts'
import { campaignKeys } from './campaigns.ts'
import { dashboardKeys } from './dashboard.ts'
import { http } from './http.ts'

export const segmentKeys = {
  all: ['segments'] as const,
  list: () => [...segmentKeys.all, 'list'] as const,
  detail: (id: number) => [...segmentKeys.all, 'detail', id] as const,
  customers: (id: number) => [...segmentKeys.detail(id), 'customers'] as const,
  preview: (rule: SegmentRule | null) => [...segmentKeys.all, 'preview', rule] as const,
}

export function fetchSegments(): Promise<Segment[]> {
  return http.get<Segment[]>('/api/segments')
}

export function fetchSegment(id: number): Promise<Segment> {
  return http.get<Segment>(`/api/segments/${id}`)
}

export function fetchSegmentCustomers(id: number): Promise<Customer[]> {
  return http.get<Customer[]>(`/api/segments/${id}/customers`)
}

export function previewSegment(rule: SegmentRule): Promise<SegmentPreview> {
  return http.post<SegmentPreview>('/api/segments/preview', rule)
}

export function createSegment(request: SegmentRequest): Promise<Segment> {
  return http.post<Segment>('/api/segments', request)
}

export function updateSegment(id: number, request: SegmentRequest): Promise<Segment> {
  return http.put<Segment>(`/api/segments/${id}`, request)
}

export function deleteSegment(id: number): Promise<void> {
  return http.delete(`/api/segments/${id}`)
}

export function refreshSegment(id: number): Promise<Segment> {
  return http.post<Segment>(`/api/segments/${id}/refresh`)
}

const isValidId = (id: number) => Number.isInteger(id) && id > 0

export function useSegments() {
  return useQuery({
    queryKey: segmentKeys.list(),
    queryFn: fetchSegments,
  })
}

export function useSegment(id: number) {
  return useQuery({
    queryKey: segmentKeys.detail(id),
    queryFn: () => fetchSegment(id),
    enabled: isValidId(id),
  })
}

export function useSegmentCustomers(id: number) {
  return useQuery({
    queryKey: segmentKeys.customers(id),
    queryFn: () => fetchSegmentCustomers(id),
    enabled: isValidId(id),
  })
}

// rule이 null이면(입력이 완성되지 않았으면) 조회하지 않는다.
export function useSegmentPreview(rule: SegmentRule | null) {
  return useQuery({
    queryKey: segmentKeys.preview(rule),
    queryFn: rule ? () => previewSegment(rule) : skipToken,
  })
}

// 세그먼트 변경은 캠페인 대상 수와 대시보드에도 영향을 준다.
function useInvalidateSegmentRelated() {
  const queryClient = useQueryClient()
  return () =>
    Promise.all([
      queryClient.invalidateQueries({ queryKey: segmentKeys.all }),
      queryClient.invalidateQueries({ queryKey: campaignKeys.all }),
      queryClient.invalidateQueries({ queryKey: dashboardKeys.all }),
    ])
}

export function useCreateSegment() {
  const invalidate = useInvalidateSegmentRelated()
  return useMutation({
    mutationFn: createSegment,
    onSuccess: invalidate,
  })
}

export function useUpdateSegment(id: number) {
  const invalidate = useInvalidateSegmentRelated()
  return useMutation({
    mutationFn: (request: SegmentRequest) => updateSegment(id, request),
    onSuccess: invalidate,
  })
}

export function useRefreshSegment(id: number) {
  const invalidate = useInvalidateSegmentRelated()
  return useMutation({
    mutationFn: () => refreshSegment(id),
    onSuccess: invalidate,
  })
}

export function useDeleteSegment() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: deleteSegment,
    // 삭제된 상세 조회가 다시 실행되지 않도록 목록과 대시보드만 갱신한다.
    onSuccess: () =>
      Promise.all([
        queryClient.invalidateQueries({ queryKey: segmentKeys.list() }),
        queryClient.invalidateQueries({ queryKey: dashboardKeys.all }),
      ]),
  })
}
