import { defineStore } from 'pinia'
import { ref } from 'vue'
import dayjs from 'dayjs'

import { ElMessage } from 'element-plus'

import { fetchProjectList, fetchMyProjects, createProject } from '@/api/project'
import { createProjectListSlice } from '@/composables/projectListSlice'
import { useUserStore } from '@/stores/user'

import type { Project } from '@/types/project'
import type { ProjectCreatePayload } from '@/types/project'
import { createEmptyProject } from '@/types/project'

export const useProjectStore = defineStore('project', () => {
  // 创建项目
  const isCreateProjectLoading = ref<boolean>(false)
  const projectForm = ref<Project>(createEmptyProject())

  // 项目列表对象
  const listSlice = createProjectListSlice(fetchProjectList)
  const manageSlice = createProjectListSlice(fetchMyProjects)

  // 清空创建项目表单方法
  /**
   * 重置创建项目表单
   *
   * 项目表单对象为projectForm\<Project\>
   */
  const resetForm = () => {
    projectForm.value = createEmptyProject()
  }

  // api方法
  const createNewProject = async (): Promise<boolean> => {
    if (isCreateProjectLoading.value) return false
    isCreateProjectLoading.value = true
    try {
      const userStore = useUserStore()
      const payload: ProjectCreatePayload = {
        name: projectForm.value.name,
        status: projectForm.value.status,
        leader: userStore.userData?.username ?? '',
        progress: projectForm.value.progress,
        createTime: dayjs().format('YYYY-MM-DD hh:mm:ss'),
        description: projectForm.value.description,
      }
      const res = await createProject(payload)
      if (res.code === 1) {
        ElMessage.success('创建项目成功')
        await listSlice.load(1)
        await manageSlice.load(1)
        resetForm()
        return true
      } else {
        ElMessage.error(res.msg)
        return false
      }
    } catch {
      ElMessage.error('创建项目失败')
      return false
    } finally {
      isCreateProjectLoading.value = false
    }
  }

  return {
    isCreateProjectLoading,
    projectForm,
    listSlice,
    manageSlice,
    resetForm,
    createNewProject,
  }
})
