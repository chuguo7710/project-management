import { ref } from 'vue'

import { ElMessage } from 'element-plus'

import { deleteProject } from '@/api/project'
import { useProjectStore } from '@/stores/project'
import { useRequest } from '@/composables/useRequest'

export const useProjectDelete = () => {
  // 外部依赖
  const projectStore = useProjectStore()
  const { listSlice, manageSlice } = projectStore
  const deleteProjectDedupe = useRequest(deleteProject, { mode: 'dedupe' }) // 创建方法在store
  // 内部状态
  const isDeleteProjectLoading = ref<boolean>(false)

  /** 删除项目方法 */
  const removeProject = async (id: number): Promise<boolean> => {
    if (isDeleteProjectLoading.value) return false
    isDeleteProjectLoading.value = true

    try {
      const res = await deleteProjectDedupe.run(id)
      if (res === undefined) return false
      ElMessage.success('成功删除项目')
      listSlice.load(1)
      manageSlice.load(1)
      return true
    } catch {
      return false
    } finally {
      if (!deleteProjectDedupe.isLoading.value) isDeleteProjectLoading.value = false
    }
  }

  return {
    removeProject,
  }
}
