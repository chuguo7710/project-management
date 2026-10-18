import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import request from '@/utils/request'
import router from '@/router'
import type { ApiResult } from '@/types/common'

interface UserData {
  username: string
  avatar: string
  role: number
  createTime: string
}
interface UserProfileData {
  nickname: string
  avatar: string
  gender: number
  bio: string
  updateTime: string
}
interface UserAllData {
  userVO: UserData
  userProfileVO: UserProfileData
}

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
   * 验证token是否有效
   *
   * 通过get请求utils/tokenValid：成功code=1，失败code=0
   * @returns 成功返回true，失败抛出异常
   */
  const tokenValid = async (): Promise<boolean> => {
    const response = await request.get<ApiResult>('utils/tokenValid')
    if (response.data.code === 1) {
      return true
    } else {
      throw new Error()
    }
  }
  /**
   * 获取用户所有数据
   *
   * 通过get请求utils/fetchUserAllData：成功data=userVO+userProfileVO，失败401
   * @returns 成功返回true，失败抛出异常
   */
  const fetchUserAllData = async (): Promise<void> => {
    const response = await request.get<ApiResult<UserAllData>>('utils/fetchUserAllData')
    userData.value = response.data.data.userVO
    userProfileData.value = response.data.data.userProfileVO
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
    tokenValid,
    fetchUserAllData,
  }
})
