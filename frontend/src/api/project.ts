import request from '@/utils/request'

import type { ApiResult, PageResult } from '@/types/common'
import type {
  Project,
  ProjectListQuery,
  ProjectCreatePayload,
  ProjectMember,
  ProjectUpdatePayload,
} from '@/types/project'

/** 项目列表 */
export const fetchProjectList = async (
  signal: AbortSignal,
  query: ProjectListQuery,
): Promise<ApiResult<PageResult<Project[]>>> => {
  const res = await request.get<ApiResult<PageResult<Project[]>>>('/project/list', {
    params: query,
    signal,
  })
  return res.data
}

/** 我的项目 */
export const fetchMyProjects = async (
  signal: AbortSignal,
  query: ProjectListQuery,
): Promise<ApiResult<PageResult<Project[]>>> => {
  const res = await request.get<ApiResult<PageResult<Project[]>>>('/project/my', {
    params: query,
    signal,
  })
  return res.data
}

/** 创建项目 */
export const createProject = async (
  signal: AbortSignal,
  payload: ProjectCreatePayload,
): Promise<ApiResult> => {
  const res = await request.post<ApiResult>('/project', payload, { signal })
  return res.data
}

/** 删除项目 */
export const deleteProject = async (signal: AbortSignal, id: number): Promise<ApiResult> => {
  const res = await request.delete<ApiResult>(`/project/${id}`, { signal })
  return res.data
}

/** 查询项目成员 */
export const fetchProjectMembers = async (
  signal: AbortSignal,
  projectId: number,
): Promise<ApiResult<ProjectMember[]>> => {
  const res = await request.get<ApiResult<ProjectMember[]>>(`/project/${projectId}/members`, {
    signal,
  })
  return res.data
}

/** 添加项目成员 */
export const insertProjectMember = async (
  signal: AbortSignal,
  projectId: number,
  userId: number,
): Promise<ApiResult> => {
  const res = await request.post<ApiResult>(`/project/${projectId}/members`, { userId }, { signal })
  return res.data
}

/** 移除项目成员 */
export const deleteProjectMember = async (
  signal: AbortSignal,
  projectId: number,
  userId: number,
): Promise<ApiResult> => {
  const res = await request.delete<ApiResult>(`/project/${projectId}/members/${userId}`, { signal })
  return res.data
}

/** 编辑项目 */
export const updateProject = async (
  signal: AbortSignal,
  projectId: number,
  payload: ProjectUpdatePayload,
): Promise<ApiResult> => {
  const res = await request.put<ApiResult>(`/project/${projectId}`, payload, { signal })
  return res.data
}
