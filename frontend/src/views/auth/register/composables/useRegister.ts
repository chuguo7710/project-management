import { ref } from 'vue'
import { userRegister } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { RegisterFormData } from '@/types/user'
import { useRequest } from '@/composables/useRequest'

export const useRegister = () => {
  // 外部依赖
  const registerDedupe = useRequest(userRegister, { mode: 'dedupe' })
  // 内部状态
  const isRegisterLoading = ref<boolean>(false)

  /** 注册方法 */
  const register = async (data: RegisterFormData): Promise<boolean> => {
    isRegisterLoading.value = true

    try {
      const res = await registerDedupe.run(data)
      if (res === undefined) return false
      ElMessage.success('注册成功')
      data.password = ''
      data.rePassword = ''
      return true
    } catch {
      return false
    } finally {
      if (!registerDedupe.isLoading.value) isRegisterLoading.value = false
    }
  }

  return {
    register,
    isRegisterLoading,
  }
}
