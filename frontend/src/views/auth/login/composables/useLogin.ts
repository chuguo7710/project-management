import { ref } from 'vue'
import { userLogin } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { LoginFormData } from '@/types/user'
import { useUserStore } from '@/stores/user'

export const useLogin = () => {
  // 外部依赖
  const useStore = useUserStore()

  // 内部状态
  const isLoginLoading = ref<boolean>(false)

  /** 登录方法 */
  const login = async (data: LoginFormData): Promise<boolean> => {
    if (isLoginLoading.value) return false
    isLoginLoading.value = true

    try {
      const res = await userLogin(data)
      useStore.setToken(res.data)
      ElMessage.success('登录成功')
      return true
    } catch {
      return false
    } finally {
      isLoginLoading.value = false
    }
  }

  return {
    login,
  }
}
