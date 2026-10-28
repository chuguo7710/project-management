<script lang="ts" setup>
import { useTaskTabsStore } from '@/stores/taskTab'

import { storeToRefs } from 'pinia'

const taskTabsStore = useTaskTabsStore()
const { tabs, currentTabIndex } = storeToRefs(taskTabsStore)
const { openTab, closeTab } = taskTabsStore
</script>
<template>
  <div class="task-board">
    <!-- 标签栏 -->
    <div v-if="tabs.length && currentTabIndex !== null" class="task-tabs">
      <div
        v-for="tab in tabs"
        :key="tab.projectId"
        class="task-tab"
        :class="{ 'is-active': tab.projectId === tabs[currentTabIndex]?.projectId }"
        @click="openTab(tab)"
      >
        <span class="task-tab__title">{{ tab.projectName }}</span>
        <el-icon class="task-tab__close" @click.stop="closeTab(tab.projectId)"><Close /></el-icon>
      </div>
    </div>
    <!-- 内容区 -->
    <div class="task-content">
      <TaskDetail
        v-if="currentTabIndex !== null && tabs[currentTabIndex] !== undefined"
        :task-id="tabs[currentTabIndex]?.projectId"
      />
      <el-empty v-else description="从看板点一个任务开始" />
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.task-board {
  display: flex;
  flex-direction: column;
  height: 100%;
  .task-tabs {
    display: flex;
    justify-content: flex-start;
    gap: 4px;
    padding: 8px 12px 0;
    border-bottom: 1px solid $border-color;
    overflow-x: auto;
    // 标签多了横向滚动，不换行——换行会吃掉内容区高度
    &::-webkit-scrollbar {
      height: 4px;
    }
  }
  .task-tab {
    display: flex;
    align-items: center;
    gap: 6px;
    flex: 0 0 auto;
    max-width: 180px;
    padding: 6px 10px;
    border: 1px solid $border-color;
    border-bottom: none;
    border-radius: 6px 6px 0 0;
    font-size: 13px;
    color: $text-secondary-color;
    cursor: pointer;
    transition: all 0.2s;
    &:hover {
      color: $primary-color;
    }
    // 激活的那个要盖住底边框，视觉上跟内容区连成一体
    &.is-active {
      background-color: #fff;
      color: $primary-color;
      border-color: $primary-color;
      margin-bottom: -1px;
    }
    &__title {
      flex: 1;
      min-width: 0;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    &__close {
      flex: 0 0 auto;
      font-size: 12px;
      border-radius: 50%;
      &:hover {
        background-color: $border-color;
      }
    }
  }
  .task-content {
    flex: 1;
    min-height: 0;
    overflow: auto;
  }
}
</style>
