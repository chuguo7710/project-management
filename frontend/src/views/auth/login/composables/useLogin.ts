import { ref } from 'vue'
import { userLogin } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { LoginFormData } from '@/types/user'
import { useUserStore } from '@/stores/user'
import { useRequest } from '@/composables/useRequest'

export const useLogin = () => {
  // 外部依赖
  const useStore = useUserStore()
  const loginDedupe = useRequest(userLogin, { mode: 'dedupe' })
  // 内部状态
  const isLoginLoading = ref<boolean>(false)

  /** 登录方法 */
  const login = async (data: LoginFormData): Promise<boolean> => {
    isLoginLoading.value = true

    try {
      const res = await loginDedupe.run(data)
      if (res === undefined) return false
      useStore.setToken(res.data)
      ElMessage.success('登录成功')
      return true
    } catch {
      return false
    } finally {
      if (!loginDedupe.isLoading.value) isLoginLoading.value = false
    }
  }

  return {
    login,
    isLoginLoading,
  }
}
