import { defineStore } from 'pinia'
import { ref } from 'vue'

import type { TaskTab } from '@/types/task'

const MAX_TABS = 10

export const useTaskTabsStore = defineStore('taskTabs', () => {
  // 内部状态
  const tabs = ref<TaskTab[]>([])
  const currentTabProjectId = ref<number | null>(null)
  /** 打开（或激活）一个标签 */
  const openTab = (task: TaskTab) => {
    // 如果标签已经存在，激活它
    if (tabs.value.some((tab) => tab.projectId === task.projectId)) {
      currentTabProjectId.value =
        tabs.value.find((tab) => tab.projectId === task.projectId)?.projectId ?? null
      return
    }
    // 满了：先删后加
    if (tabs.value.length >= MAX_TABS) {
      tabs.value.pop()
    }
    tabs.value.unshift(task)
    currentTabProjectId.value = task.projectId
  }

  /** 关闭一个标签 */
  const closeTab = (projectId: number) => {
    // 找到要关闭的标签索引
    const closedTabIndex = tabs.value.findIndex((t) => t.projectId === projectId)
    if (closedTabIndex === -1) return
    tabs.value.splice(closedTabIndex, 1)
    // 关闭的是激活的标签则更新 currentTabProjectId
    if (!(currentTabProjectId.value === projectId)) return
    currentTabProjectId.value =
      (tabs.value[closedTabIndex] ?? tabs.value[closedTabIndex - 1])?.projectId ?? null
  }

  return { tabs, currentTabProjectId, openTab, closeTab }
})
