import request from '@/utils/request'
import router from '@/router'
import { ElMessage } from 'element-plus'
import type { ApiResult } from '@/types/common'

// 数据
export interface RegisterFormData {
  username: string
  password: string
  rePassword: string
}

export const useRegisterForm = () => {
  // api方法
  const register = async (formLabelAlign: RegisterFormData): Promise<boolean> => {
    try {
      const response = await request.post<ApiResult>('/register', formLabelAlign)
      if (response.data.code === 1) {
        ElMessage.success('注册成功')
        formLabelAlign.password = ''
        formLabelAlign.rePassword = ''
        return true
      }
    } catch {
      ElMessage.error('注册失败')
    }
    return false
  }
  return {
    register,
  }
}
