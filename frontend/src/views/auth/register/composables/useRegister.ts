import { ref } from 'vue'
import { userRegister } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { RegisterFormData } from '@/types/user'

export const useRegister = () => {
  //内部状态
  const isRegisterLoading = ref<boolean>(false)

  /** 注册方法 */
  const register = async (data: RegisterFormData): Promise<boolean> => {
    if (isRegisterLoading.value) return false
    isRegisterLoading.value = true

    try {
      await userRegister(data)
      ElMessage.success('注册成功')
      data.password = ''
      data.rePassword = ''
      return true
    } catch {
      return false
    } finally {
      isRegisterLoading.value = false
    }
  }

  return {
    register,
  }
}
