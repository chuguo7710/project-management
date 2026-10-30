import { ref } from 'vue'
import { useRequest } from '@/composables/useRequest'
import { taskListSelect, taskCreate } from '@/api/task'

import { ElMessage } from 'element-plus'

import type { TaskVO, TaskCreatePayload } from '@/types/task'

export const useTask = () => {
  // 外部依赖
  const searchTaskListDedupe = useRequest(taskListSelect, { mode: 'dedupe' })
  const addTaskDedupe = useRequest(taskCreate, { mode: 'dedupe' })
  // 内部状态
  const isSearchLoading = ref<Map<number, boolean>>(new Map())
  const isAddTaskLoading = ref<boolean>(false)
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

  return {
    isSearchLoading,
    TabTasksMap,
    isAddTaskLoading,
    addTask,
    searchTaskList,
  }
}
