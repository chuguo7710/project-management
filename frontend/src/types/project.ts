// 项目对象
export interface Project {
  id: number
  displayedId: number
  name: string
  status: number
  leader: string
  progress: number
  createTime: string
  description: string
  isLeader?: boolean // 是否能进行编辑和删除操作
  memberCount?: number
}
export const createEmptyProject = (): Project => ({
  id: 0,
  displayedId: 0,
  name: '',
  status: 1,
  leader: '',
  progress: 0,
  createTime: '',
  description: '',
})
// 新建项目
export interface ProjectCreatePayload {
  name: string
  status: number
  leader: string
  progress: number
  createTime: string
  description: string
}

// 项目列表
export interface PageState {
  page: number
  pageSize: number
  total: number
}
export interface SearchQuery {
  name?: string
  leader?: string
}
export interface ProjectListQuery {
  page: number
  pageSize: number
  name?: string
  leader?: string
}
export interface ProjectCard {
  project: Project
  role: number
  joinTime: string
}

export const PROJECT_STATUS: Record<number, { class: string; text: string }> = {
  1: { class: 'dot-active', text: '进行中' },
  2: { class: 'dot-stop', text: '已暂停' },
  3: { class: 'dot-finish', text: '已完成' },
} as const
