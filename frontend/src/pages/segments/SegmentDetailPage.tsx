import { useNavigate, useParams } from 'react-router'
import { isNotFound } from '../../api/http.ts'
import { useDeleteSegment, useRefreshSegment, useSegment, useSegmentCustomers } from '../../api/segments.ts'
import PageHeader from '../../components/PageHeader.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { describeSegmentRule } from '../../constants/labels.ts'
import { ROUTES, customerDetailPath } from '../../constants/routes.ts'
import { formatCustomerName, formatDateTime, formatNumber, orDash } from '../../utils/format.ts'

function SegmentMembers({ segmentId }: { segmentId: number }) {
  const navigate = useNavigate()
  const { data: customers, isPending, isError, error, refetch } = useSegmentCustomers(segmentId)

  if (isPending) {
    return <LoadingState message="세그먼트 고객을 불러오는 중..." />
  }
  if (isError) {
    return <ErrorState error={error} title="세그먼트 고객을 불러오지 못했습니다." onRetry={() => refetch()} />
  }
  if (customers.length === 0) {
    return <EmptyState title="조건에 맞는 고객이 없습니다." description="고객을 동기화한 뒤 새로고침해 보세요." />
  }
  return (
    <div className="table-wrap">
      <table className="data-table">
        <thead>
          <tr>
            <th>이름</th>
            <th>이메일</th>
            <th>회사명</th>
            <th>국가</th>
            <th>라이프사이클</th>
            <th>리드 상태</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((customer) => (
            <tr
              key={customer.id}
              className="clickable-row"
              tabIndex={0}
              onClick={() => navigate(customerDetailPath(customer.id))}
              onKeyDown={(event) => {
                if (event.key === 'Enter') {
                  navigate(customerDetailPath(customer.id))
                }
              }}
            >
              <td className="cell-strong">{formatCustomerName(customer)}</td>
              <td>{orDash(customer.email)}</td>
              <td>{orDash(customer.companyName)}</td>
              <td>{orDash(customer.country)}</td>
              <td>{orDash(customer.lifecycleStage)}</td>
              <td>{orDash(customer.leadStatus)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

function SegmentDetailPage() {
  const navigate = useNavigate()
  const { segmentId } = useParams()
  const id = Number(segmentId)
  const isValidId = Number.isInteger(id) && id > 0
  const { data: segment, isPending, isError, error, refetch } = useSegment(id)
  const refreshSegment = useRefreshSegment(id)
  const deleteSegment = useDeleteSegment()
  const back = { to: ROUTES.SEGMENTS, label: '세그먼트 목록' }

  if (!isValidId || (isError && isNotFound(error))) {
    return (
      <>
        <PageHeader title="세그먼트 상세" back={back} />
        <EmptyState title="세그먼트를 찾을 수 없습니다." />
      </>
    )
  }
  if (isPending) {
    return (
      <>
        <PageHeader title="세그먼트 상세" back={back} />
        <LoadingState message="세그먼트를 불러오는 중..." />
      </>
    )
  }
  if (isError) {
    return (
      <>
        <PageHeader title="세그먼트 상세" back={back} />
        <ErrorState error={error} title="세그먼트를 불러오지 못했습니다." onRetry={() => refetch()} />
      </>
    )
  }

  const handleDelete = () => {
    if (!window.confirm(`"${segment.name}" 세그먼트를 삭제할까요?`)) {
      return
    }
    deleteSegment.mutate(id, { onSuccess: () => navigate(ROUTES.SEGMENTS) })
  }

  const busy = refreshSegment.isPending || deleteSegment.isPending

  return (
    <>
      <PageHeader
        title={segment.name}
        description={describeSegmentRule(segment)}
        back={back}
        actions={
          <>
            <button type="button" className="btn btn-secondary" onClick={() => refreshSegment.mutate()} disabled={busy}>
              {refreshSegment.isPending ? '계산 중...' : '새로고침'}
            </button>
            <button type="button" className="btn btn-danger" onClick={handleDelete} disabled={busy}>
              {deleteSegment.isPending ? '삭제 중...' : '삭제'}
            </button>
          </>
        }
      />

      {refreshSegment.isSuccess && (
        <div className="notice notice-success">새로고침 완료: {formatNumber(refreshSegment.data.customerCount)}명</div>
      )}
      {refreshSegment.isError && (
        <div className="notice notice-error">새로고침 실패: {refreshSegment.error.message}</div>
      )}
      {deleteSegment.isError && <div className="notice notice-error">삭제 실패: {deleteSegment.error.message}</div>}

      <section className="card section">
        <dl className="detail-list">
          <dt>조건</dt>
          <dd>{describeSegmentRule(segment)}</dd>
          <dt>고객 수</dt>
          <dd>{formatNumber(segment.customerCount)}명</dd>
          <dt>설명</dt>
          <dd>{orDash(segment.description)}</dd>
          <dt>마지막 계산</dt>
          <dd>{formatDateTime(segment.refreshedAt)}</dd>
          <dt>생성일</dt>
          <dd>{formatDateTime(segment.createdAt)}</dd>
        </dl>
      </section>

      <h2 className="card-title">세그먼트 고객</h2>
      <SegmentMembers segmentId={id} />
    </>
  )
}

export default SegmentDetailPage
