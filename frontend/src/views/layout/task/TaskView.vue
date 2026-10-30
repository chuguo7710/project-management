<script lang="ts" setup>
import TaskTabs from './components/TaskTabs.vue'
import TaskBoard from './components/TaskBoard.vue'
import { computed } from 'vue'

import type { TaskTab } from '@/types/task'
import { useTaskTabsStore } from '@/stores/taskTab'
import { storeToRefs } from 'pinia'

const taskTabsStore = useTaskTabsStore()
const { tabs, currentTabProjectId } = storeToRefs(taskTabsStore)
const { openTab, closeTab } = taskTabsStore
// 派生
const currentTab = computed<TaskTab | null>(() =>
  currentTabProjectId.value === null
    ? null
    : (tabs.value[tabs.value.findIndex((tab) => tab.projectId === currentTabProjectId.value)] ??
      null),
)
</script>
<template>
  <div class="task-shell">
    <!-- 标签栏 -->
    <!--- 只负责更改currentTabProjectId -->
    <TaskTabs
      v-if="tabs.length"
      :tabs="tabs"
      :current-tab-project-id="currentTabProjectId"
      @open-tab="openTab"
      @close-tab="closeTab"
    />
    <!-- 内容区 -->
    <!--- 只负责展示当前选中标签的内容(存在时) -->
    <div class="task-content">
      <TaskBoard v-if="currentTab" :project-id="currentTab.projectId" />
      <el-empty v-else description="从「我的项目」点一个看板开始" />
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;
.task-shell {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
}
.task-content {
  flex: 1;
  min-height: 0;
  overflow: hidden;
  background-color: $background-color;
}
</style>
