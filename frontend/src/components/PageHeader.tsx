import type { ReactNode } from 'react'
import { Link } from 'react-router'

type PageHeaderProps = {
  title: string
  description?: ReactNode
  actions?: ReactNode
  back?: { to: string; label: string }
}

function PageHeader({ title, description, actions, back }: PageHeaderProps) {
  return (
    <header className="page-header">
      <div>
        {back && (
          <Link className="page-back" to={back.to}>
            ← {back.label}
          </Link>
        )}
        <h1>{title}</h1>
        {description && <p className="page-description">{description}</p>}
      </div>
      {actions && <div className="page-actions">{actions}</div>}
    </header>
  )
}

export default PageHeader
