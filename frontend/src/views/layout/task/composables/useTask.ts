import { ref } from 'vue'
import { useRequest } from '@/composables/useRequest'
import { taskListSelect, taskCreate, taskRemove, taskUpdateStatus } from '@/api/task'

import { ElMessage } from 'element-plus'

import type { TaskVO, TaskCreatePayload } from '@/types/task'

export const useTask = () => {
  // 外部依赖
  const searchTaskListDedupe = useRequest(taskListSelect, { mode: 'dedupe' })
  const addTaskDedupe = useRequest(taskCreate, { mode: 'dedupe' })
  const removeTaskDedupe = useRequest(taskRemove, { mode: 'dedupe' })
  const updateTaskDedupe = useRequest(taskUpdateStatus, { mode: 'dedupe' })

  // 内部状态
  const isSearchLoading = ref<Map<number, boolean>>(new Map())
  const isAddTaskLoading = ref<boolean>(false)
  const isRemoveTaskLoading = ref<boolean>(false)
  const TabTasksMap = ref<Map<number, TaskVO[]>>(new Map())

  /** 查询任务列表方法 */
  const searchTaskList = async (projectId: number): Promise<boolean> => {
    if (isSearchLoading.value.get(projectId)) return false
    isSearchLoading.value.set(projectId, true)

    try {
      const res = await searchTaskListDedupe.run(projectId)
      if (!res) return false
      TabTasksMap.value.set(projectId, res.data ?? [])
      isSearchLoading.value.set(projectId, false)
      return true
    } catch {
      isSearchLoading.value.delete(projectId)
      return false
    }
  }

  /** 创建任务方法 */
  const addTask = async (projectId: number, payload: TaskCreatePayload): Promise<boolean> => {
    isAddTaskLoading.value = true

    try {
      const res = await addTaskDedupe.run(projectId, payload)
      if (!res) return false
      ElMessage.success('创建任务成功')
      return true
    } catch {
      return false
    } finally {
      if (addTaskDedupe.isLoading.value === false) isAddTaskLoading.value = false
    }
  }

  /** 删除任务方法 */
  const removeTask = async (taskId: number): Promise<boolean> => {
    isRemoveTaskLoading.value = true

    try {
      const res = await removeTaskDedupe.run(taskId)
      if (!res) return false
      ElMessage.success('删除任务成功')
      return true
    } catch {
      return false
    }
  }

  /** 更新任务状态方法 */
  const changeTaskStatus = async (taskId: number, status: number): Promise<boolean> => {
    try {
      const res = await updateTaskDedupe.run(taskId, status)
      if (!res) return false
      ElMessage.success('更新任务成功状态')
      return true
    } catch {
      return false
    }
  }

  return {
    isSearchLoading,
    TabTasksMap,
    removeTask,
    isAddTaskLoading,
    addTask,
    searchTaskList,
    changeTaskStatus,
  }
}
