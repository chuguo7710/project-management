import { ref } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import { userFetchAllData } from '@/api/user'

import type { UserData, UserProfileData } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  // 共享状态
  const isDataFetched = ref<boolean>(false)
  const token = ref<string>(localStorage.getItem('app_token') || '')
  const userData = ref<UserData | null>(null)
  const userProfileData = ref<UserProfileData | null>(null)

  // 基础方法
  const resetStore = () => {
    token.value = ''
    userData.value = null
    userProfileData.value = null
    isDataFetched.value = false
  }
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('app_token', newToken)
  }
  const setUserData = (newUserData: UserData | null) => {
    userData.value = newUserData
  }
  const setUserProfileData = (newUserProfileData: UserProfileData | null) => {
    userProfileData.value = newUserProfileData
  }
  const logout = () => {
    const prefix = 'app'
    Object.keys(localStorage)
      .filter((key) => key.startsWith(prefix))
      .forEach((key) => localStorage.removeItem(key))
    resetStore()
    router.push({ name: 'login' })
  }

  // api方法
  /**
   * 获取用户所有数据
   *
   * 通过get请求utils/fetchUserAllData：成功data=userVO+userProfileVO，失败401
   */
  const fetchUserAllData = async (): Promise<void> => {
    if (isDataFetched.value) return
    const res = await userFetchAllData()
    if (res.code !== 1) {
      throw new Error('获取用户数据失败')
    }
    userData.value = res.data.userVO
    userProfileData.value = res.data.userProfileVO
    isDataFetched.value = true
  }

  return {
    isDataFetched,
    token,
    userData,
    userProfileData,
    setToken,
    setUserData,
    setUserProfileData,
    logout,
    fetchUserAllData,
  }
})
