import { ref } from 'vue'
import { fetchProjectMembers, insertProjectMember, deleteProjectMember } from '@/api/project'

import { ElMessage } from 'element-plus'

import type { ProjectMember } from '@/types/project'

export const useProjectMembers = () => {
  // 内部数据
  const list = ref<ProjectMember[]>([])
  // 内部状态
  const isSearchMemberLoading = ref<boolean>(false)
  const isAddMemberLoading = ref<boolean>(false)
  const isRemoveMemberLoading = ref<boolean>(false)
  let searchSeq = 0 // 用于标记搜索请求的顺序，避免过时请求覆盖新请求
  /** 查询项目成员方法 */
  const searchProjectMembers = async (projectId: number): Promise<boolean> => {
    const seq = ++searchSeq // 每次调用时增加seq
    isSearchMemberLoading.value = true

    try {
      const res = await fetchProjectMembers(projectId)
      // 如果当前请求(seq)不是最新请求(searchSeq)，则不更新数据
      if (seq !== searchSeq) return false
      list.value = res.data
      return true
    } catch {
      return false
    } finally {
      // 当前请求(seq)是最新请求(searchSeq)时加载才能结束
      if (seq === searchSeq) isSearchMemberLoading.value = false
    }
  }

  /** 添加项目成员方法 */
  const addProjectMember = async (projectId: number, userId: number): Promise<boolean> => {
    if (isAddMemberLoading.value) return false
    isAddMemberLoading.value = true

    try {
      await insertProjectMember(projectId, userId)
      await searchProjectMembers(projectId)
      ElMessage.success('添加成员成功')
      return true
    } catch {
      return false
    } finally {
      isAddMemberLoading.value = false
    }
  }

  /** 移除项目成员方法 */
  const removeProjectMember = async (projectId: number, userId: number): Promise<boolean> => {
    if (isRemoveMemberLoading.value) return false
    isRemoveMemberLoading.value = true

    try {
      await deleteProjectMember(projectId, userId)
      await searchProjectMembers(projectId)
      ElMessage.success('移除成员成功')
      return true
    } catch {
      return false
    } finally {
      isRemoveMemberLoading.value = false
    }
  }

  return {
    list,
    searchProjectMembers,
    addProjectMember,
    removeProjectMember,
    isSearchMemberLoading,
  }
}
