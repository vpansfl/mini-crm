import { Link } from 'react-router'
import { ROUTES } from '../constants/routes.ts'

function NotFoundPage() {
  return (
    <>
      <h1>페이지를 찾을 수 없습니다</h1>
      <Link to={ROUTES.DASHBOARD}>대시보드로 이동</Link>
    </>
  )
}

export default NotFoundPage
