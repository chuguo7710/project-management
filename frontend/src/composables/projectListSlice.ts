import { ref } from 'vue'
import type { ApiResult, PageResult } from '@/types/common'
import type { Project, SearchQuery, ProjectListQuery } from '@/types/project'
import { useRequest } from '@/composables/useRequest'

type FetchProjectPage = (
  signal: AbortSignal,
  query: ProjectListQuery,
) => Promise<ApiResult<PageResult<Project[]>>>

export const createProjectListSlice = (fetchPage: FetchProjectPage) => {
  // 外部依赖
  const fetchProjectLatest = useRequest(fetchPage, { mode: 'latest' })
  // 内部状态
  const isLoading = ref<boolean>(false)
  // 内部数据
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
  const query = ref<SearchQuery>({ projectName: '', leaderName: '' })

  /** 加载项目列表方法 */
  const load = async (page?: number): Promise<boolean> => {
    // 更新页数
    if (page !== undefined) pageState.value.page = page
    isLoading.value = true

    try {
      const res = await fetchProjectLatest.run({
        ...query.value,
        page: pageState.value.page,
        pageSize: pageState.value.pageSize,
      })
      if (res === undefined) return false
      pageState.value.list = res.data.records.map((project, index) => ({
        ...project,
        displayedId: (pageState.value.page - 1) * pageState.value.pageSize + index + 1,
      }))
      pageState.value.total = res.data.total
      pageState.value.loaded = true
      return true
    } catch {
      pageState.value.list = []
      pageState.value.total = 0
      return false
    } finally {
      if (!fetchProjectLatest.isLoading.value) isLoading.value = false
    }
  }

  /** 重置查询条件方法 */
  const resetQuery = () => {
    query.value.projectName = ''
    query.value.leaderName = ''
  }

  return { pageState, query, load, resetQuery }
}
