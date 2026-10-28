import axios from 'axios'
import { ref, onScopeDispose } from 'vue'

/** 四种请求模式：
 * latest（最新优先）：请求并发，响应不合并，只return最新请求结果
 * dedupe（去重）：在途期间不重发，请求不并发
 * queue（排队）：请求排队依次发送，请求不并发，响应不合并，依次return结果
 * coalesce（尾部合并）：防抖机制，仍在操作则不发送请求，请求不并发，响应合并
 */
export type RequestMode = 'latest' | 'dedupe' | 'queue' | 'coalesce'
/** 可选配置 */
export interface UseRequestOptions {
  mode?: RequestMode
  // queue（排队）：最大队列长度，0为不限制
  maxQueue?: number
  // coalesce （尾部合并）：防抖时间，单位毫秒
  wait?: number
}

export function useRequest<A extends unknown[], R>(
  request: (signal: AbortSignal, ...args: A) => Promise<R>,
  options: UseRequestOptions = {},
) {
  // 基本数据
  const mode: RequestMode = options.mode ?? 'latest'

  /** latest（最新优先）辅助参数 */
  let requestSeq = 0 // 请求序号，非并发时始终为1
  /** dedupe（去重）辅助参数 */
  const isLoading = ref<boolean>(false) //请求时不重发，去重
  let abortCtrl: AbortController | null = null // 取消控制器
  /** queue（排队）辅助参数 */
  const maxQueue = options.maxQueue ?? 0
  let queueLength = 0 // 队列中任务数（含执行中）
  let queueTail: Promise<unknown> = Promise.resolve() // 串行链尾
  /** coalesce（尾部合并）辅助参数 */
  const wait = options.wait ?? 300 // 防抖时间
  let lastWaiter: ((value: R | undefined) => void) | null = null // 尾部合并的等待池
  let runningWaiter: ((value: R | undefined) => void) | null = null // 正在执行的等待者
  let debounceTimer: ReturnType<typeof setTimeout> | null = null // 尾部合并计时器

  /** 发送请求方法 */
  const execOnce = async (args: A): Promise<R | undefined> => {
    // latest（最新优先）：记录请求序号并注册控制器
    const mySeq = ++requestSeq
    if (mode === 'latest') {
      abortCtrl?.abort()
      abortCtrl = new AbortController()
    } else if (!abortCtrl) {
      abortCtrl = new AbortController()
    }
    // dedupe（去重）：在途期间不重发
    isLoading.value = true
    // 发送请求
    try {
      const res = await request(abortCtrl.signal, ...args)
      // latest（最新优先）：并发时非最新请求不更新data
      if (mySeq !== requestSeq) {
        return undefined
      }
      // 返回最新响应体
      return res
    } catch (error) {
      if (axios.isCancel(error)) return undefined // 主动取消或卸载
      // 只抛最新请求的错误
      if (mySeq === requestSeq) {
        throw error
      }
    } finally {
      // dedupe（去重）：最新请求结束可继续请求
      if (mySeq === requestSeq) {
        isLoading.value = false
      }
    }
  }

  /** 根据模式运行请求处理器 */
  const run = (...args: A): Promise<R | undefined> => {
    switch (mode) {
      /** latest（最新优先）：直接发请求，并发 */
      case 'latest':
        return execOnce(args)
      /** dedupe（去重）：在途期间不重发 */
      case 'dedupe':
        if (isLoading.value) {
          return Promise.resolve(undefined)
        } else {
          return execOnce(args)
        }
      /** queue（排队）：请求排队依次发送，超过上限不再发 */
      case 'queue': {
        // 队列请求超过上限不再发请求
        if (maxQueue > 0 && queueLength >= maxQueue) {
          return Promise.resolve(undefined)
        }
        // 记录当前任务并更新队尾
        queueLength++
        const task = queueTail
          .catch(() => undefined)
          .then(() => execOnce(args))
          .finally(() => {
            queueLength--
          })
        queueTail = task
        // 返回当前任务结果
        return task
      }
      /** coalesce（尾部合并）：进等待池，窗口内无新调用才用最后一次参数发 */
      case 'coalesce':
        return new Promise<R | undefined>((resolve) => {
          // 计时操作前有新调用，则是所以旧的状态失效，赋予最新的等待者和计时器
          if (lastWaiter && debounceTimer) {
            lastWaiter(undefined)
            lastWaiter = null
            clearTimeout(debounceTimer)
          }
          if (runningWaiter) {
            runningWaiter(undefined)
            runningWaiter = null
          }
          lastWaiter = resolve
          debounceTimer = setTimeout(async () => {
            // 计时操作后请求前未有新调用，则等待者递交给runningWaiter，发请求
            runningWaiter = lastWaiter
            lastWaiter = null
            try {
              const res = await execOnce(args)
              // 计时操作后请求后未有新调用，则返回结果，清理状态
              if (runningWaiter === resolve) {
                runningWaiter(res)
                debounceTimer = null
              }
            } catch {
              runningWaiter?.(undefined)
            }
          }, wait)
        })
      /** 默认：类型保护，防止模式被修改为其他值 */
      default: {
        const exhaustive: never = mode
        return exhaustive
      }
    }
  }

  /** 卸载 / effectScope 销毁时取消在途请求，避免「组件没了还往已销毁的 ref 里写」*/
  onScopeDispose(() => {
    abortCtrl?.abort()
    lastWaiter?.(undefined)
    lastWaiter = null
    runningWaiter?.(undefined)
    runningWaiter = null
    if (debounceTimer) clearTimeout(debounceTimer)
  })

  return {
    run,
    isLoading,
  }
}
