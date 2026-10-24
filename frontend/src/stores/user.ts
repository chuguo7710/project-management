import { ref } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import { userFetchAllData } from '@/api/user'

import type { UserData, UserProfileData } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  // 内部状态
  const isDataFetched = ref<boolean>(false)

  //内部数据
  const token = ref<string>(localStorage.getItem('app_token') || '')
  const userData = ref<UserData | null>(null)
  const userProfileData = ref<UserProfileData | null>(null)

  // 基础方法
  /** 重置store方法 */
  const resetStore = () => {
    token.value = ''
    userData.value = null
    userProfileData.value = null
    isDataFetched.value = false
  }
  /** 设置token方法 */
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('app_token', newToken)
  }
  /** 设置账户数据方法 */
  const setUserData = (newUserData: UserData | null) => {
    userData.value = newUserData
  }
  /** 设置用户资料数据方法 */
  const setUserProfileData = (newUserProfileData: UserProfileData | null) => {
    userProfileData.value = newUserProfileData
  }
  /** 退出登录方法 */
  const logout = () => {
    const prefix = 'app'
    Object.keys(localStorage)
      .filter((key) => key.startsWith(prefix))
      .forEach((key) => localStorage.removeItem(key))
    resetStore()
    router.push({ name: 'login' })
  }

  //API方法
  /** 获取用户所有数据 */
  const fetchUserAllData = async (): Promise<boolean> => {
    if (isDataFetched.value) return true

    try {
      const res = await userFetchAllData()
      userData.value = res.data.userVO
      userProfileData.value = res.data.userProfileVO
      isDataFetched.value = true
      return true
    } catch {
      return false
    }
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
