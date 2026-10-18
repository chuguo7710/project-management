import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { ApiResult, PageResult } from '@/types/common'
import type { Project, SearchQuery, ProjectListQuery } from '@/types/project'

type FetchProjectPage = (q: ProjectListQuery) => Promise<ApiResult<PageResult<Project[]>>>

export const createProjectListSlice = (fetchPage: FetchProjectPage) => {
  const pageState = ref<{
    page: number
    pageSize: number
    total: number
    list: Project[]
    loaded: boolean
  }>({
    page: 1,
    pageSize: 5,
    total: 0,
    list: [],
    loaded: false,
  })
  const query = ref<SearchQuery>({ name: '', leader: '' })

  const load = async (page?: number) => {
    if (page !== undefined) pageState.value.page = page
    const res = await fetchPage({
      ...query.value,
      page: pageState.value.page,
      pageSize: pageState.value.pageSize,
    })
    if (res.code === 1) {
      pageState.value.list = res.data.result
      pageState.value.total = res.data.total
      pageState.value.loaded = true
    } else {
      ElMessage.error(res.msg)
      pageState.value.list = []
      pageState.value.total = 0
    }
  }

  const resetQuery = () => {
    query.value.name = ''
    query.value.leader = ''
  }

  return { pageState, query, load, resetQuery }
}
