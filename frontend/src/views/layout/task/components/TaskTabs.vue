<script lang="ts" setup>
import type { TaskTab } from '@/types/task'

// props
defineProps<{
  tabs: TaskTab[]
  currentTabProjectId: number | null
}>()
// emits
const emit = defineEmits<{
  'open-tab': [tab: TaskTab]
  'close-tab': [projectId: number]
}>()
</script>

<template>
  <div v-if="tabs.length" class="task-tabs">
    <div
      v-for="tab in tabs"
      :key="tab.projectId"
      class="task-tab"
      :class="{ 'is-active': tab.projectId === currentTabProjectId }"
      @click="emit('open-tab', tab)"
    >
      <span class="task-tab__title">{{ tab.projectName }}</span>
      <el-icon class="task-tab__close" @click.stop="emit('close-tab', tab.projectId)"
        ><Close
      /></el-icon>
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.task-tabs {
  display: flex;
  justify-content: flex-start;
  gap: 4px;
  padding: 8px 12px 0;
  border-bottom: 1px solid $border-color;
  overflow-x: auto;
  background-color: #fff;
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
</style>
