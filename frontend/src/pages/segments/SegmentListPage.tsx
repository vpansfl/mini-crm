import { Link, useNavigate } from 'react-router'
import { useSegments } from '../../api/segments.ts'
import PageHeader from '../../components/PageHeader.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { describeSegmentRule } from '../../constants/labels.ts'
import { ROUTES, segmentDetailPath } from '../../constants/routes.ts'
import { formatDateTime, formatNumber, orDash } from '../../utils/format.ts'

function SegmentListPage() {
  const navigate = useNavigate()
  const { data: segments, isPending, isError, error, refetch } = useSegments()

  let content
  if (isPending) {
    content = <LoadingState message="세그먼트를 불러오는 중..." />
  } else if (isError) {
    content = <ErrorState error={error} title="세그먼트를 불러오지 못했습니다." onRetry={() => refetch()} />
  } else if (segments.length === 0) {
    content = (
      <EmptyState
        title="세그먼트가 없습니다."
        description="조건을 지정해 고객 그룹을 만들어 보세요."
        action={
          <Link className="btn btn-primary" to={ROUTES.SEGMENT_NEW}>
            새 세그먼트
          </Link>
        }
      />
    )
  } else {
    content = (
      <div className="table-wrap">
        <table className="data-table">
          <thead>
            <tr>
              <th>이름</th>
              <th>조건</th>
              <th className="num">고객 수</th>
              <th>설명</th>
              <th>마지막 계산</th>
            </tr>
          </thead>
          <tbody>
            {segments.map((segment) => (
              <tr
                key={segment.id}
                className="clickable-row"
                tabIndex={0}
                onClick={() => navigate(segmentDetailPath(segment.id))}
                onKeyDown={(event) => {
                  if (event.key === 'Enter') {
                    navigate(segmentDetailPath(segment.id))
                  }
                }}
              >
                <td className="cell-strong">{segment.name}</td>
                <td>{describeSegmentRule(segment)}</td>
                <td className="num">{formatNumber(segment.customerCount)}</td>
                <td>{orDash(segment.description)}</td>
                <td>{formatDateTime(segment.refreshedAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )
  }

  return (
    <>
      <PageHeader
        title="세그먼트"
        description="조건에 맞는 고객 그룹입니다. 고객 동기화 시 자동으로 다시 계산됩니다."
        actions={
          <Link className="btn btn-primary" to={ROUTES.SEGMENT_NEW}>
            새 세그먼트
          </Link>
        }
      />
      {content}
    </>
  )
}

export default SegmentListPage
