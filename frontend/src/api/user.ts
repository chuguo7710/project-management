import request from '@/utils/request'

import type { LoginFormData, RegisterFormData } from '@/types/user'
import type { ApiResult } from '@/types/common'

/** 登录 */
export const userLogin = async (data: LoginFormData) => {
  const res = await request.post<ApiResult<string>>('/login', data) // 传回token
  return res.data
}

/** 注册 */
export const userRegister = async (data: RegisterFormData) => {
  const res = await request.post<ApiResult>('/register', data)
  return res.data
}
