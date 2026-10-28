import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface TaskTab {
  projectId: number
  projectName: string
}

const MAX_TABS = 10

export const useTaskTabsStore = defineStore('taskTabs', () => {
  // 内部状态
  const tabs = ref<TaskTab[]>([])
  const currentTabIndex = ref<number | null>(null)

  /** 打开（或激活）一个标签 */
  const openTab = (task: TaskTab) => {
    // 如果标签已经存在，激活它
    if (tabs.value.some((tab) => tab.projectId === task.projectId)) {
      currentTabIndex.value = tabs.value.findIndex((tab) => tab.projectId === task.projectId)
      return
    }
    // 满了：先删后加
    if (tabs.value.length >= MAX_TABS) {
      tabs.value.pop()
    }
    tabs.value.unshift(task)
    currentTabIndex.value = 0
  }

  /** 关闭一个标签，关的是当前则右邻优先、其次左邻 */
  const closeTab = (projectId: number) => {
    // 找到并删除要关闭的标签
    const index = tabs.value.findIndex((tab) => tab.projectId === projectId)
    if (index === -1) return
    tabs.value.splice(index, 1)
    // 如果没有标签了，currentTabIndex置空
    if (tabs.value.length === 0) {
      currentTabIndex.value = null
      return
    }
    // 根据关闭的标签位置调整currentTabIndex
    if (currentTabIndex.value === null) return
    if (index < currentTabIndex.value) {
      currentTabIndex.value--
    } else {
      currentTabIndex.value = tabs.value[index] === undefined ? index - 1 : index
    }
  }

  return { tabs, currentTabIndex, openTab, closeTab }
})
