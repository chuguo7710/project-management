import { ref } from 'vue'
import { fetchProjectMembers, insertProjectMember, deleteProjectMember } from '@/api/project'

import { ElMessage } from 'element-plus'

import type { ProjectMember } from '@/types/project'
import { useRequest } from '@/composables/useRequest'

export const useProjectMembers = () => {
  // 外部依赖
  const fetchProjectMembersLatest = useRequest(fetchProjectMembers, { mode: 'latest' })
  const insertProjectMemberDedupe = useRequest(insertProjectMember, { mode: 'dedupe' })
  const deleteProjectMemberDedupe = useRequest(deleteProjectMember, { mode: 'dedupe' })
  // 内部数据
  const list = ref<ProjectMember[]>([])
  // 内部状态
  const isSearchMemberLoading = ref<boolean>(false)
  const isAddMemberLoading = ref<boolean>(false)
  const isRemoveMemberLoading = ref<boolean>(false)
  /** 查询项目成员方法 */
  const searchProjectMembers = async (projectId: number): Promise<boolean> => {
    isSearchMemberLoading.value = true

    try {
      const res = await fetchProjectMembersLatest.run(projectId)
      // 如果当前请求(seq)不是最新请求(searchSeq)，则不更新数据
      if (res === undefined) return false
      list.value = res.data
      return true
    } catch {
      return false
    } finally {
      // 当前请求(seq)是最新请求(searchSeq)时加载才能结束
      if (!fetchProjectMembersLatest.isLoading.value) isSearchMemberLoading.value = false
    }
  }

  /** 添加项目成员方法 */
  const addProjectMember = async (projectId: number, userId: number): Promise<boolean> => {
    isAddMemberLoading.value = true

    try {
      const res = await insertProjectMemberDedupe.run(projectId, userId)
      if (res === undefined) return false
      await searchProjectMembers(projectId)
      ElMessage.success('添加成员成功')
      return true
    } catch {
      return false
    } finally {
      if (!insertProjectMemberDedupe.isLoading.value) isAddMemberLoading.value = false
    }
  }

  /** 移除项目成员方法 */
  const removeProjectMember = async (projectId: number, userId: number): Promise<boolean> => {
    isRemoveMemberLoading.value = true

    try {
      const res = await deleteProjectMemberDedupe.run(projectId, userId)
      if (res === undefined) return false
      await searchProjectMembers(projectId)
      ElMessage.success('移除成员成功')
      return true
    } catch {
      return false
    } finally {
      if (!deleteProjectMemberDedupe.isLoading.value) isRemoveMemberLoading.value = false
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
