import { ref } from 'vue'
import { userLogin } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { LoginFormData } from '@/types/user'
import { useUserStore } from '@/stores/user'

export const useLogin = () => {
  // 引入文件
  const useStore = useUserStore()

  // 内部状态
  const isLoginLoading = ref<boolean>(false)

  /** 登录成功返回true，否则返回false */
  const login = async (data: LoginFormData): Promise<boolean> => {
    if (isLoginLoading.value) return false
    isLoginLoading.value = true

    try {
      const res = await userLogin(data)
      if (res.code === 1) {
        useStore.setToken(res.data)
        ElMessage.success('登录成功')
        return true
      } else {
        ElMessage.error(res.msg)
      }
    } catch {
      ElMessage.error('登录失败')
    } finally {
      isLoginLoading.value = false
    }
    return false
  }

  return {
    login,
  }
}
