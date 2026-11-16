import request from '@/utils/request'

import type { ApiResult } from '@/types/common'
import type { TaskVO, TaskCreatePayload } from '@/types/task'

/** 获取任务列表 */
export const taskListSelect = async (
  signal: AbortSignal,
  projectId: number,
): Promise<ApiResult<TaskVO[]>> => {
  const res = await request.get(`/task/${projectId}`, { signal })
  return res.data
}

/** 创建任务 */
export const taskCreate = async (
  signal: AbortSignal,
  projectId: number,
  payload: TaskCreatePayload,
): Promise<ApiResult> => {
  const res = await request.post(`/task/${projectId}`, payload, { signal })
  return res.data
}

/** 删除任务 */
export const taskRemove = async (signal: AbortSignal, taskId: number): Promise<ApiResult> => {
  const res = await request.delete(`/task/${taskId}`, { signal })
  return res.data
}

/** 更新任务状态 */
export const taskUpdateStatus = async (
  signal: AbortSignal,
  taskId: number,
  status: number,
): Promise<ApiResult> => {
  const res = await request.put(`/task/${taskId}/status`, { status }, { signal })
  return res.data
}
