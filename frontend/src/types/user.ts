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
