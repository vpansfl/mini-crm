import { useParams } from 'react-router'
import { useCustomer } from '../../api/customers.ts'
import { isNotFound } from '../../api/http.ts'
import PageHeader from '../../components/PageHeader.tsx'
import { EmptyState, ErrorState, LoadingState } from '../../components/StateViews.tsx'
import { ROUTES } from '../../constants/routes.ts'
import { formatCustomerName, formatDateTime, orDash } from '../../utils/format.ts'

function CustomerDetailPage() {
  const { customerId } = useParams()
  const id = Number(customerId)
  const isValidId = Number.isInteger(id) && id > 0
  const { data: customer, isPending, isError, error, refetch } = useCustomer(id)
  const back = { to: ROUTES.CUSTOMERS, label: '고객 목록' }

  if (!isValidId || (isError && isNotFound(error))) {
    return (
      <>
        <PageHeader title="고객 상세" back={back} />
        <EmptyState title="고객을 찾을 수 없습니다." />
      </>
    )
  }
  if (isPending) {
    return (
      <>
        <PageHeader title="고객 상세" back={back} />
        <LoadingState message="고객 정보를 불러오는 중..." />
      </>
    )
  }
  if (isError) {
    return (
      <>
        <PageHeader title="고객 상세" back={back} />
        <ErrorState error={error} title="고객 정보를 불러오지 못했습니다." onRetry={() => refetch()} />
      </>
    )
  }

  return (
    <>
      <PageHeader title={formatCustomerName(customer)} description={orDash(customer.email)} back={back} />

      <div className="grid grid-2">
        <section className="card">
          <h2 className="card-title">기본 정보</h2>
          <dl className="detail-list">
            <dt>이름</dt>
            <dd>{formatCustomerName(customer)}</dd>
            <dt>이메일</dt>
            <dd>{orDash(customer.email)}</dd>
            <dt>전화</dt>
            <dd>{orDash(customer.phone)}</dd>
            <dt>휴대전화</dt>
            <dd>{orDash(customer.mobilePhone)}</dd>
            <dt>회사명</dt>
            <dd>{orDash(customer.companyName)}</dd>
            <dt>직함</dt>
            <dd>{orDash(customer.jobTitle)}</dd>
            <dt>지역</dt>
            <dd>{orDash([customer.city, customer.state, customer.country].filter(Boolean).join(', '))}</dd>
          </dl>
        </section>

        <section className="card">
          <h2 className="card-title">CRM 정보</h2>
          <dl className="detail-list">
            <dt>라이프사이클 단계</dt>
            <dd>{orDash(customer.lifecycleStage)}</dd>
            <dt>리드 상태</dt>
            <dd>{orDash(customer.leadStatus)}</dd>
            <dt>HubSpot ID</dt>
            <dd>{orDash(customer.hubspotId)}</dd>
            <dt>HubSpot 생성일</dt>
            <dd>{formatDateTime(customer.hubspotCreatedAt)}</dd>
            <dt>HubSpot 수정일</dt>
            <dd>{formatDateTime(customer.hubspotUpdatedAt)}</dd>
            <dt>CRM 등록일</dt>
            <dd>{formatDateTime(customer.createdAt)}</dd>
            <dt>CRM 수정일</dt>
            <dd>{formatDateTime(customer.updatedAt)}</dd>
          </dl>
        </section>
      </div>
    </>
  )
}

export default CustomerDetailPage
