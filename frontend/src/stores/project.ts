import { defineStore } from 'pinia'
import { ref } from 'vue'

import { ElMessage } from 'element-plus'

import { fetchProjectList, fetchMyProjects, createProject } from '@/api/project'
import { createProjectListSlice } from '@/composables/projectListSlice'

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

  // 重置表单
  const resetForm = () => {
    projectForm.value = createEmptyProject()
  }

  /** 创建新项目方法 */
  const createNewProject = async (): Promise<boolean> => {
    if (isCreateProjectLoading.value) return false
    isCreateProjectLoading.value = true

    const payload: ProjectCreatePayload = {
      projectName: projectForm.value.projectName,
      status: projectForm.value.status,
      progress: projectForm.value.progress,
      description: projectForm.value.description,
    }

    try {
      const res = await createProject(payload)
      ElMessage.success('创建项目成功')
      await listSlice.load(1)
      await manageSlice.load(1)
      resetForm()
      return true
    } catch {
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
