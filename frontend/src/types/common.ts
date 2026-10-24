export interface ApiResult<T = null> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  records: T
  total: number
}
