import { createBrowserRouter, Navigate } from 'react-router'
import { ROUTES } from './constants/routes.ts'
import AppLayout from './layouts/AppLayout.tsx'
import DashboardPage from './pages/dashboard/DashboardPage.tsx'
import CustomerListPage from './pages/customers/CustomerListPage.tsx'
import CustomerDetailPage from './pages/customers/CustomerDetailPage.tsx'
import SegmentListPage from './pages/segments/SegmentListPage.tsx'
import SegmentCreatePage from './pages/segments/SegmentCreatePage.tsx'
import SegmentDetailPage from './pages/segments/SegmentDetailPage.tsx'
import CampaignListPage from './pages/campaigns/CampaignListPage.tsx'
import CampaignCreatePage from './pages/campaigns/CampaignCreatePage.tsx'
import CampaignDetailPage from './pages/campaigns/CampaignDetailPage.tsx'
import NotFoundPage from './pages/NotFoundPage.tsx'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AppLayout />,
    children: [
      { index: true, element: <Navigate to={ROUTES.DASHBOARD} replace /> },
      { path: 'dashboard', element: <DashboardPage /> },
      {
        path: 'customers',
        children: [
          { index: true, element: <CustomerListPage /> },
          { path: ':customerId', element: <CustomerDetailPage /> },
        ],
      },
      {
        path: 'segments',
        children: [
          { index: true, element: <SegmentListPage /> },
          { path: 'new', element: <SegmentCreatePage /> },
          { path: ':segmentId', element: <SegmentDetailPage /> },
        ],
      },
      {
        path: 'campaigns',
        children: [
          { index: true, element: <CampaignListPage /> },
          { path: 'new', element: <CampaignCreatePage /> },
          { path: ':campaignId', element: <CampaignDetailPage /> },
        ],
      },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
])
