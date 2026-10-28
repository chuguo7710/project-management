import { ref } from 'vue'

import { ElMessage } from 'element-plus'

import type { ProjectUpdatePayload } from '@/types/project'
import { deleteProject, updateProject } from '@/api/project'
import { useTaskTabsStore } from '@/stores/taskTab'
import { useProjectStore } from '@/stores/project'
import { useRequest } from '@/composables/useRequest'

export const useProject = () => {
  // 外部依赖
  const projectStore = useProjectStore()
  const taskTabsStore = useTaskTabsStore()
  const { listSlice, manageSlice } = projectStore
  const deleteProjectDedupe = useRequest(deleteProject, { mode: 'dedupe' }) // 创建方法在store
  const updateProjectDedupe = useRequest(updateProject, { mode: 'dedupe' }) // 创建方法在store
  // 内部状态
  const isDeleteProjectLoading = ref<boolean>(false)

  /** 删除项目方法 */
  const removeProject = async (projectId: number): Promise<boolean> => {
    isDeleteProjectLoading.value = true

    try {
      const res = await deleteProjectDedupe.run(projectId)
      if (res === undefined) return false
      ElMessage.success('成功删除项目')
      taskTabsStore.closeTab(projectId)
      listSlice.load(1)
      manageSlice.load(1)
      return true
    } catch {
      return false
    } finally {
      if (!deleteProjectDedupe.isLoading.value) isDeleteProjectLoading.value = false
    }
  }

  /** 编辑项目方法 */
  const editProject = async (
    projectId: number,
    payload: ProjectUpdatePayload,
  ): Promise<boolean> => {
    isDeleteProjectLoading.value = true

    try {
      const res = await updateProjectDedupe.run(projectId, payload)
      if (res === undefined) return false
      ElMessage.success('成功编辑项目')
      listSlice.load(1)
      manageSlice.load(1)
      return true
    } catch {
      return false
    } finally {
      if (!updateProjectDedupe.isLoading.value) isDeleteProjectLoading.value = false
    }
  }

  return {
    removeProject,
    editProject,
  }
}
