import type { PageParams } from '@/types/common'

export interface LoginFormData {
  username: string
  password: string
}
export interface RegisterFormData {
  username: string
  password: string
  rePassword: string
  email?: string
}

export interface UserData {
  username: string
  avatar: string | null
  role: number
  createTime: string
}
export interface UserProfileData {
  nickname: string
  avatar: string | null
  gender: number
  bio: string | null
  updateTime: string
}
export interface UserAllData {
  userVO: UserData
  userProfileVO: UserProfileData
}

export const ROLE = {
  ROOT: 1,
  ADMIN: 2,
  USER: 3,
} as const

export interface User {
  userId: number
  username: string
  role: number
  status: number
  createTime: string
}
export interface UserSearchQuery {
  username?: string
}
export type UserListQuery = PageParams & UserSearchQuery

export const ROLE_TEXT: Record<number, { text: string; type: 'danger' | 'warning' | 'info' }> = {
  1: { text: '站长', type: 'danger' },
  2: { text: '管理员', type: 'warning' },
  3: { text: '普通用户', type: 'info' },
}
