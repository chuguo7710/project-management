import { ref } from 'vue'

import { ElMessage } from 'element-plus'

import { deleteProject } from '@/api/project'
import { useProjectStore } from '@/stores/project'

export const useProjectDelete = () => {
  // 外部依赖
  const projectStore = useProjectStore()
  const { listSlice, manageSlice } = projectStore

  // 内部状态
  const isDeleteProjectLoading = ref<boolean>(false)

  /** 删除项目方法 */
  const removeProject = async (id: number): Promise<boolean> => {
    if (isDeleteProjectLoading.value) return false
    isDeleteProjectLoading.value = true

    try {
      const res = await deleteProject(id)
      ElMessage.success('成功删除项目')
      listSlice.load(1)
      manageSlice.load(1)
      return true
    } catch {
      return false
    } finally {
      isDeleteProjectLoading.value = false
    }
  }

  return {
    removeProject,
  }
}
