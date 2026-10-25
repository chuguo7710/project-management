// 项目对象
export interface Project {
  projectId: number
  projectName: string
  status: number
  leaderId: number
  progress: number
  createTime?: string
  description: string

  displayedId: number
  leaderName: string
  canEdit: boolean
  memberCount?: number
}
export const createEmptyProject = (): Project => ({
  projectId: 0,
  projectName: '',
  status: 1,
  leaderId: 0,
  progress: 0,
  description: '',

  displayedId: 0,
  leaderName: '',
  canEdit: false,
})

// 新建项目表单
export interface ProjectCreatePayload {
  projectName: string
  status: number
  progress: number
  description: string
}

// 项目列表
export interface PageState {
  page: number
  pageSize: number
  total: number
}
export interface SearchQuery {
  projectName?: string
  leaderName?: string
}
export interface ProjectListQuery {
  page: number
  pageSize: number
  projectName?: string
  leaderName?: string
}
export interface ProjectCard {
  project: Project
  myProjectRole: number
  joinTime: string
}

// 项目成员
export interface ProjectMember {
  userId: number
  username: string
  avatar: string | null
  myProjectRole: number
  joinTime: string
}

export const PROJECT_STATUS: Record<number, { class: string; text: string }> = {
  1: { class: 'dot-active', text: '进行中' },
  2: { class: 'dot-stop', text: '已暂停' },
  3: { class: 'dot-finish', text: '已完成' },
} as const
