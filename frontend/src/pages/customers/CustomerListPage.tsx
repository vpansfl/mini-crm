import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router'
import { useCustomers, useSyncCustomers } from '../../api/customers.ts'
import PageHeader from '../../components/PageHeader.tsx'
import SearchInput from '../../components/SearchInput.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { customerDetailPath } from '../../constants/routes.ts'
import type { Customer } from '../../types/customer.ts'
import { formatCustomerName, formatDate, formatNumber, orDash } from '../../utils/format.ts'

// 이름, 이메일, 회사명, 도시, 국가에서 검색한다. (대소문자 무시)
function matchesKeyword(customer: Customer, keyword: string): boolean {
  if (!keyword) {
    return true
  }
  const target = [
    customer.firstName,
    customer.lastName,
    customer.email,
    customer.companyName,
    customer.city,
    customer.country,
  ]
    .filter(Boolean)
    .join(' ')
    .toLowerCase()
  return target.includes(keyword)
}

function CustomerListPage() {
  const navigate = useNavigate()
  const [keyword, setKeyword] = useState('')
  const { data: customers, isPending, isError, error, refetch } = useCustomers()
  const sync = useSyncCustomers()

  const normalizedKeyword = keyword.trim().toLowerCase()
  const filtered = useMemo(
    () => (customers ?? []).filter((customer) => matchesKeyword(customer, normalizedKeyword)),
    [customers, normalizedKeyword],
  )

  const goToDetail = (id: number) => navigate(customerDetailPath(id))

  let content
  if (isPending) {
    content = <LoadingState message="고객 목록을 불러오는 중..." />
  } else if (isError) {
    content = <ErrorState error={error} title="고객 목록을 불러오지 못했습니다." onRetry={() => refetch()} />
  } else if (customers.length === 0) {
    content = (
      <EmptyState title="등록된 고객이 없습니다." description="HubSpot 동기화 버튼으로 고객을 가져오세요." />
    )
  } else {
    content = (
      <>
        <div className="toolbar">
          <SearchInput value={keyword} onChange={setKeyword} placeholder="이름, 이메일, 회사, 도시, 국가 검색" />
          <span className="toolbar-meta">
            {formatNumber(filtered.length)} / {formatNumber(customers.length)}명
          </span>
        </div>
        {filtered.length === 0 ? (
          <EmptyState title="검색 결과가 없습니다." description="다른 검색어를 입력해 보세요." />
        ) : (
          <div className="table-wrap">
            <table className="data-table">
              <thead>
                <tr>
                  <th>이름</th>
                  <th>이메일</th>
                  <th>회사명</th>
                  <th>지역</th>
                  <th>라이프사이클</th>
                  <th>리드 상태</th>
                  <th>HubSpot ID</th>
                  <th>생성일</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((customer) => (
                  <tr
                    key={customer.id}
                    className="clickable-row"
                    tabIndex={0}
                    onClick={() => goToDetail(customer.id)}
                    onKeyDown={(event) => {
                      if (event.key === 'Enter') {
                        goToDetail(customer.id)
                      }
                    }}
                  >
                    <td className="cell-strong">{formatCustomerName(customer)}</td>
                    <td>{orDash(customer.email)}</td>
                    <td>{orDash(customer.companyName)}</td>
                    <td>{orDash([customer.city, customer.country].filter(Boolean).join(', '))}</td>
                    <td>{orDash(customer.lifecycleStage)}</td>
                    <td>{orDash(customer.leadStatus)}</td>
                    <td className="cell-muted">{orDash(customer.hubspotId)}</td>
                    <td>{formatDate(customer.hubspotCreatedAt ?? customer.createdAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </>
    )
  }

  return (
    <>
      <PageHeader
        title="고객"
        description="HubSpot Contact에서 동기화된 고객 목록입니다."
        actions={
          <button type="button" className="btn btn-primary" onClick={() => sync.mutate()} disabled={sync.isPending}>
            {sync.isPending ? '동기화 중...' : 'HubSpot 동기화'}
          </button>
        }
      />

      {sync.isSuccess && (
        <div className="notice notice-success">
          동기화 완료: 조회 {formatNumber(sync.data.fetched)}건 / 신규 {formatNumber(sync.data.created)}건 / 업데이트{' '}
          {formatNumber(sync.data.updated)}건 / 변경 없음 {formatNumber(sync.data.unchanged)}건 (세그먼트 재계산 완료)
        </div>
      )}
      {sync.isError && <div className="notice notice-error">동기화 실패: {sync.error.message}</div>}

      {content}
    </>
  )
}

export default CustomerListPage
