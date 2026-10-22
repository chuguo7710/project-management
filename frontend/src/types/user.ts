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
  avatar: string
  role: number
  createTime: string
}

export interface UserProfileData {
  nickname: string
  avatar: string
  gender: number
  bio: string
  updateTime: string
}

export interface UserAllData {
  userVO: UserData
  userProfileVO: UserProfileData
}
