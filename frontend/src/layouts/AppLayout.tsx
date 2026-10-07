import { Outlet } from 'react-router'
import Sidebar from './Sidebar.tsx'
import './AppLayout.css'

function AppLayout() {
  return (
    <div className="app-layout">
      <Sidebar />
      <main className="app-main">
        <div className="app-content">
          <Outlet />
        </div>
      </main>
    </div>
  )
}

export default AppLayout
