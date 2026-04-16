import { request } from '../utils/request'

export function fetchDashboardOverview() {
  return request('/admin/dashboard/overview')
}
