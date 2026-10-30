<script lang="ts" setup>
import dayjs from 'dayjs'
import { computed } from 'vue'
import { TASK_PRIORITY } from '@/types/task'
import type { TaskVO } from '@/types/task'

const props = defineProps<{ task: TaskVO }>()

// 优先级：左侧竖条的颜色，不做成标签——卡片上一行字已经够挤了
const priority = computed(() => TASK_PRIORITY[props.task.priority] ?? TASK_PRIORITY[2])

const deadlineText = computed(() =>
  props.task.deadline ? dayjs(props.task.deadline).format('MM-DD') : '',
)

// 已完成的不再标红：完成之后再提醒「逾期」没有意义
const deadlineState = computed(() => {
  if (!props.task.deadline || props.task.status === 3) return ''
  const diff = dayjs(props.task.deadline).startOf('day').diff(dayjs().startOf('day'), 'day')
  if (diff < 0) return 'is-overdue'
  if (diff <= 3) return 'is-soon'
  return ''
})

const initial = computed(() => (props.task.assigneeName ?? '?').slice(0, 1))
</script>

<template>
  <div class="task-card" :class="{ 'is-done': task.status === 3 }">
    <div class="task-card__stripe" :style="{ backgroundColor: priority.color }"></div>
    <div class="task-card__main">
      <div class="task-card__title">{{ task.title }}</div>
      <div class="task-card__meta">
        <span class="task-card__assignee">
          <span class="task-card__avatar">{{ initial }}</span>
          {{ task.assigneeName ?? '未指派' }}
        </span>
        <span v-if="deadlineText" class="task-card__deadline" :class="deadlineState">
          {{ deadlineText }}
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.task-card {
  display: flex;
  flex: 0 0 auto; // 列内不拉伸，卡片高度由内容决定
  overflow: hidden;
  border: 1px solid $border-color;
  border-radius: 6px;
  background-color: #fff;
  box-shadow: 0 1px 2px $shadow-color;
  cursor: pointer;
  transition:
    box-shadow 0.2s,
    transform 0.2s;
  &:hover {
    box-shadow: 0 2px 8px $shadow-color;
  }
  &.is-done {
    opacity: 0.6;
  }
  &__stripe {
    flex: 0 0 3px;
  }
  &__main {
    flex: 1;
    min-width: 0;
    padding: 8px 10px;
  }
  &__title {
    font-size: 14px;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2; // 标题最多两行，再多就截断——卡片高度必须可预测
    overflow: hidden;
    overflow-wrap: anywhere;
  }
  &__meta {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    margin-top: 6px;
    font-size: 12px;
    color: $text-secondary-color;
  }
  &__assignee {
    display: flex;
    align-items: center;
    gap: 4px;
    min-width: 0;
  }
  &__avatar {
    flex: 0 0 auto;
    width: 18px;
    height: 18px;
    line-height: 18px;
    border-radius: 50%;
    background-color: $primary-color;
    color: #fff;
    font-size: 11px;
    text-align: center;
  }
  &__deadline {
    flex: 0 0 auto;
    &.is-soon {
      color: #e6a23c;
    }
    &.is-overdue {
      color: $status-stop-color;
    }
  }
}
</style>
