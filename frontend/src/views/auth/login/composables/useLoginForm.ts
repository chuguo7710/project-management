import request from '@/utils/request'
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import router from '@/router'
import { ElMessage } from 'element-plus'
import type { ApiResult } from '@/types/common'

// 数据
export interface LoginFormData {
  username: string
  password: string
}

export const useLoginForm = () => {
  // 引入文件
  const useStore = useUserStore()

  // 数据
  const loading = ref<boolean>(false)

  // 基础方法

  // api方法
  const login = async (formLabelAlign: LoginFormData) => {
    if (loading.value) {
      return
    }
    loading.value = true
    try {
      const response = await request.post<ApiResult<string>>('/login', formLabelAlign)
      if (response.data.code === 1) {
        const token = response.data.data
        useStore.setToken(token)
        await useStore.fetchUserAllData()
        await router.push({ name: 'layout' })
        ElMessage.success('登录成功')
        formLabelAlign.password = ''
      }
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    login,
  }
}
