import request from '@/utils/request'

import type { ApiResult } from '@/types/common'
import type { TaskVO, TaskCreatePayload } from '@/types/task'

export const taskListSelect = async (
  signal: AbortSignal,
  projectId: number,
): Promise<ApiResult<TaskVO[]>> => {
  const res = await request.get(`/task/${projectId}`, { signal })
  return res.data
}

export const taskCreate = async (
  signal: AbortSignal,
  projectId: number,
  payload: TaskCreatePayload,
): Promise<ApiResult> => {
  const res = await request.post(`/task/${projectId}`, payload, { signal })
  return res.data
}
