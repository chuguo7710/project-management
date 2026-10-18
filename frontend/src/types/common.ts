export interface ApiResult<T = null> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  result: T
  total: number
}
