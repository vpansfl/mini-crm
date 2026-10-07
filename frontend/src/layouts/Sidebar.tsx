import { NavLink } from 'react-router'
import { ROUTES } from '../constants/routes.ts'

const NAV_ITEMS = [
  { to: ROUTES.DASHBOARD, label: '대시보드' },
  { to: ROUTES.CUSTOMERS, label: '고객' },
  { to: ROUTES.SEGMENTS, label: '세그먼트' },
  { to: ROUTES.CAMPAIGNS, label: '캠페인' },
]

function Sidebar() {
  return (
    <aside className="app-sidebar">
      <div className="app-brand">
        <span className="app-brand-mark" aria-hidden="true">
          M
        </span>
        Mini CRM
      </div>
      <nav>
        <ul>
          {NAV_ITEMS.map((item) => (
            <li key={item.to}>
              <NavLink to={item.to}>{item.label}</NavLink>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  )
}

export default Sidebar
