import { ref } from 'vue'
import type { ApiResult, PageResult } from '@/types/common'
import type { Project, SearchQuery, ProjectListQuery } from '@/types/project'

type FetchProjectPage = (q: ProjectListQuery) => Promise<ApiResult<PageResult<Project[]>>>
type LoadTarget = { page: number; pageSize: number; query: SearchQuery }

export const createProjectListSlice = (fetchPage: FetchProjectPage) => {
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
  let pending: LoadTarget | null = null

  /** 加载项目列表方法 */
  const load = async (page?: number) => {
    // 更新页数
    if (page !== undefined) pageState.value.page = page
    // 记录最新的请求参数
    pending = {
      page: pageState.value.page,
      pageSize: pageState.value.pageSize,
      query: { ...query.value },
    }

    if (isLoading.value) return
    isLoading.value = true

    try {
      while (pending) {
        // 读最新的请求并清空记录
        const target = pending
        pending = null

        try {
          const res = await fetchPage({
            ...target.query,
            page: target.page,
            pageSize: target.pageSize,
          })
          // 请求成功后是否还有请求被记录
          if (pending) continue
          // 赋值、渲染
          pageState.value.list = res.data.records.map((project, index) => ({
            ...project,
            displayedId: (target.page - 1) * target.pageSize + index + 1,
          }))
          pageState.value.total = res.data.total
          pageState.value.loaded = true
        } catch {
          // 请求失败后是否还有请求被记录
          if (pending) continue
          console.error('加载项目列表失败')
          pageState.value.list = []
          pageState.value.total = 0
        }
      }
    } finally {
      isLoading.value = false
    }
  }

  /** 重置查询条件方法 */
  const resetQuery = () => {
    query.value.projectName = ''
    query.value.leaderName = ''
  }

  return { pageState, query, load, resetQuery }
}
