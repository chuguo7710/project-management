export interface TaskTab {
  projectId: number
  projectName: string
}
export type TaskStatus = 1 | 2 | 3
export type TaskPriority = 1 | 2 | 3
// 任务卡片
export interface TaskVO {
  taskId: number
  title: string
  description: string | null
  projectId: number
  projectName?: string
  assigneeId: number | null
  assigneeName: string | null
  creatorId: number
  creatorName?: string
  status: TaskStatus // 1 待办 / 2 进行中 / 3 已完成
  priority: TaskPriority
  deadline: string | null // 日期串，可空
  completedAt?: string | null
  createTime?: string
}
// 任务栏
export interface TaskColumn {
  status: TaskStatus // 1 待办 / 2 进行中 / 3 已完成
  text: string
  tasks: TaskVO[]
}
// 任务创建表单
export interface TaskCreatePayload {
  title: string
  description: string | null
  assigneeId: number | null
  priority: TaskPriority
  deadline: string | null
  status: TaskStatus
  projectId: number
}
export const createEmptyPayload = (): TaskCreatePayload => ({
  title: '',
  description: null,
  assigneeId: null,
  priority: 2,
  deadline: null,
  status: 1,
  projectId: 0,
})

export const TASK_STATUS: Record<number, { text: string; color: string }> = {
  1: { text: '待办', color: '#909399' },
  2: { text: '进行中', color: '#409eff' },
  3: { text: '已完成', color: '#3cb4ff' },
}

// 颜色与 assets/styles/color.scss 的取值同源：红=高，橙=中，蓝=低
export const TASK_PRIORITY: Record<
  TaskPriority,
  { val: TaskPriority; text: string; color: string }
> = {
  1: { val: 1, text: '高', color: '#ff3131' },
  2: { val: 2, text: '中', color: '#e6a23c' },
  3: { val: 3, text: '低', color: '#3cb4ff' },
}
