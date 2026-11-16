import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRequest } from '@/composables/useRequest'
import { fetchUserList } from '@/api/user'

import { ElMessage } from 'element-plus'

import type { User, UserListQuery } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  // 外部依赖
  const fetchUserListLatest = useRequest(fetchUserList, { mode: 'latest' })
  // 内部状态
  const userList = ref<User[]>([])
  const isSearchLoading = ref(false)
  const pageState = ref<{ query: UserListQuery; total: number; loaded: boolean }>({
    query: {
      page: 1,
      pageSize: 10,
      username: '',
    },
    total: 0,
    loaded: false,
  })

  // 外部接口
  /** 查询用户列表 */
  const searchUserList = async (page?: number): Promise<boolean> => {
    if (page !== undefined) pageState.value.query.page = page
    isSearchLoading.value = true

    try {
      const result = await fetchUserListLatest.run(pageState.value.query)
      if (result === undefined) return false
      userList.value = result.data.records
      pageState.value.total = result.data.total
      return true
    } catch (error) {
      return false
    } finally {
      if (!fetchUserListLatest.isLoading.value) {
        isSearchLoading.value = false
      }
    }
  }

  /** 清空查询条件 */
  const resetQuery = () => {
    pageState.value.query.username = ''
  }

  return {
    userList,
    isSearchLoading,
    pageState,
    searchUserList,
    resetQuery,
  }
})
