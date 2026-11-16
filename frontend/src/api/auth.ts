import request from '@/utils/request'

import type { LoginFormData, RegisterFormData, UserAllData } from '@/types/user'
import type { ApiResult } from '@/types/common'

/** 登录 */
export const userLogin = async (signal: AbortSignal, data: LoginFormData) => {
  const res = await request.post<ApiResult<string>>('/auth/login', data, { signal }) // 传回token
  return res.data
}

/** 注册 */
export const userRegister = async (signal: AbortSignal, data: RegisterFormData) => {
  const res = await request.post<ApiResult>('/auth/register', data, { signal })
  return res.data
}

/** 获取账户所有数据 */
export const userFetchAllData = async () => {
  const res = await request.get<ApiResult<UserAllData>>('/user/me')
  return res.data
}
