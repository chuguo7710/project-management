import type { ApiResult } from '@/types/common'
import request from '@/utils/request'

import { ElMessage } from 'element-plus'

import { deleteProject } from '@/api/project'
import { useProjectStore } from '@/stores/project'

export const useProjectDelete = () => {
  const projectStore = useProjectStore()
  const { manageSlice } = projectStore
  // 删除项目方法
  const deleteProject = async (id: number) => {
    await deleteProject(id)
    ElMessage.success('成功删除项目')
    manageSlice.load(1)
  }
  return {
    deleteProject,
  }
}
