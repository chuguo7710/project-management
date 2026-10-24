import request from '@/utils/request'

import type { ApiResult, PageResult } from '@/types/common'
import type { Project, ProjectListQuery, ProjectCreatePayload } from '@/types/project'

/** 项目列表 */
export const fetchProjectList = async (query: ProjectListQuery) => {
  const res = await request.get<ApiResult<PageResult<Project[]>>>('/project/list', {
    params: query,
  })
  return res.data
}

/** 我的项目 */
export const fetchMyProjects = async (query: ProjectListQuery) => {
  const res = await request.get<ApiResult<PageResult<Project[]>>>('/project/my', {
    params: query,
  })
  return res.data
}

/** 创建项目 */
export const createProject = async (payload: ProjectCreatePayload) => {
  const res = await request.post<ApiResult>('/project', payload)
  return res.data
}

/** 删除项目 */
export const deleteProject = async (id: number) => {
  const res = await request.delete<ApiResult>(`/project/${id}`)
  return res.data
}
