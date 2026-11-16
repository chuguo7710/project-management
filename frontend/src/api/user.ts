import request from '@/utils/request'

import type { ApiResult, PageResult } from '@/types/common'
import type { User, UserListQuery } from '@/types/user'

/** 获取用户列表 */
export const fetchUserList = async (
  signal: AbortSignal,
  query: UserListQuery,
): Promise<ApiResult<PageResult<User[]>>> => {
  const res = await request.get<ApiResult<PageResult<User[]>>>('/user/list', {
    params: query,
    signal,
  })
  return res.data
}

/** 更改用户状态 */
export const updateUserStatus = async (
  signal: AbortSignal,
  userId: number,
  status: number,
): Promise<ApiResult> => {
  const res = await request.put<ApiResult>(`/user/${userId}/status`, { status }, { signal })
  return res.data
}

/** 更改用户角色 */
export const updateUserRole = async (
  signal: AbortSignal,
  userId: number,
  role: number,
): Promise<ApiResult> => {
  const res = await request.put<ApiResult>(`/user/${userId}/role`, { role }, { signal })
  return res.data
}
