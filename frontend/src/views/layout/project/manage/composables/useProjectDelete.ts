import { ElMessage } from 'element-plus'

import { deleteProject } from '@/api/project'
import { useProjectStore } from '@/stores/project'

export const useProjectDelete = () => {
  const projectStore = useProjectStore()
  const { listSlice, manageSlice } = projectStore
  // 删除项目方法
  const removeProject = async (id: number): Promise<boolean> => {
    try {
      const res = await deleteProject(id)
      if (res.code === 1) {
        ElMessage.success('成功删除项目')
        listSlice.load(1)
        manageSlice.load(1)
        return true
      } else {
        ElMessage.error(res.msg)
        return false
      }
    } catch {
      ElMessage.error('删除项目失败')
      return false
    }
  }
  return {
    removeProject,
  }
}
