import { ref } from 'vue'
import { userRegiste } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { RegisterFormData } from '@/types/user'

export const useRegister = () => {
  //内部状态
  const isRegisterLoading = ref<boolean>(false)

  /** 注册成功返回true，否则返回false */
  const register = async (data: RegisterFormData): Promise<boolean> => {
    if (isRegisterLoading.value) return false
    isRegisterLoading.value = true
    try {
      const res = await userRegiste(data)
      if (res.code === 1) {
        ElMessage.success('注册成功')
        data.password = ''
        data.rePassword = ''
        return true
      } else {
        ElMessage.error(res.msg)
        return false
      }
    } catch {
      ElMessage.error('注册失败')
    } finally {
      isRegisterLoading.value = false
    }
    return false
  }
  return {
    register,
  }
}
