import type { DateTimeString } from './common.ts'

// GET /api/customers, GET /api/customers/{id}
export type Customer = {
  id: number
  hubspotId: string | null
  firstName: string | null
  lastName: string | null
  email: string | null
  companyName: string | null
  createdAt: DateTimeString
  updatedAt: DateTimeString
  phone: string | null
  mobilePhone: string | null
  jobTitle: string | null
  city: string | null
  state: string | null
  country: string | null
  lifecycleStage: string | null
  leadStatus: string | null
  companyId: number | null
  hubspotCreatedAt: DateTimeString | null
  hubspotUpdatedAt: DateTimeString | null
}
