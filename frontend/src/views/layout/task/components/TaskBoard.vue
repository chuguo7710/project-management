<script lang="ts" setup>
import TaskCard from './TaskCard.vue'
import TaskCreateDialog from './TaskCreateDialog.vue'
import TaskDetailDrawer from './TaskDetailDrawer.vue'
import { ref, computed, watch } from 'vue'

import type { TaskVO, TaskCreatePayload, TaskStatus } from '@/types/task'
import { useTask } from '../composables/useTask'

// 外部依赖
const { searchTaskList, addTask, TabTasksMap, isAddTaskLoading } = useTask()
// props
const props = defineProps<{
  projectId: number
}>()
// watch
watch(
  () => props.projectId,
  (id) => {
    searchTaskList(id)
  },
  { immediate: true },
)
// 内部状态
const taskCreateDialogRef = ref<InstanceType<typeof TaskCreateDialog> | null>(null)
const taskDetailDrawerRef = ref<InstanceType<typeof TaskDetailDrawer> | null>(null)
// 派生
const currentTabTasks = computed(() => TabTasksMap.value.get(props.projectId) ?? [])
const currentTask = ref<TaskVO | null>(null)
// 事件处理
/** 创建任务 */
const createTask = async (taskCreatePayload: TaskCreatePayload) => {
  const isAdded = await addTask(props.projectId, taskCreatePayload)
  if (!isAdded) return
  taskCreateDialogRef.value?.close()
  await searchTaskList(props.projectId)
}
/** 打开任务详情 */
const openTaskDetail = (task: TaskVO) => {
  currentTask.value = task
  taskDetailDrawerRef.value?.open()
}

// 三列固定：对应 task.status 1 / 2 / 3
const COLUMNS = [
  { status: 1, label: '待办' },
  { status: 2, label: '进行中' },
  { status: 3, label: '已完成' },
] as const

const columns = computed(() =>
  COLUMNS.map((col) => ({
    ...col,
    tasks: currentTabTasks.value.filter((task) => task.status === col.status),
    creatable: col.status !== 3,
  })),
)
</script>

<template>
  <div class="task-board">
    <!-- 三列固定：对应 task.status 1 / 2 / 3 -->
    <section v-for="col in columns" :key="col.status" class="task-column">
      <!-- 列头：列名 + 任务数 + 新建按钮 -->
      <header class="task-column__head">
        <span class="task-column__label">{{ col.label }}</span>
        <span class="task-column__count">{{ col.tasks.length }}</span>
        <el-button
          v-if="col.creatable"
          class="task-column__add"
          type="primary"
          link
          @click="taskCreateDialogRef?.open(col.status)"
        >
          <el-icon><Plus /></el-icon>
        </el-button>
      </header>
      <!-- 列体：任务卡片列表 -->
      <div class="task-column__body">
        <TaskCard
          v-for="task in col.tasks"
          :key="task.taskId"
          :task="task"
          @click="openTaskDetail(task)"
        />
        <div v-if="!col.tasks.length" class="task-column__empty">暂无任务</div>
      </div>
    </section>
    <!-- 新建任务弹窗 -->
    <TaskCreateDialog
      ref="taskCreateDialogRef"
      :is-loading="isAddTaskLoading"
      :project-id="props.projectId"
      @create="createTask"
    />
    <!-- 任务详情抽屉 -->
    <TaskDetailDrawer ref="taskDetailDrawerRef" :task="currentTask" />
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.task-board {
  display: flex;
  gap: 12px;
  height: 100%;
  padding: 12px;
  overflow-x: auto; // 列固定三列，这个只是兜底，防止窄屏把列压扁
}

.task-column {
  display: flex;
  flex-direction: column;
  flex: 1 1 0;
  min-width: 260px;
  max-height: 100%;
  border: 1px solid $border-color;
  border-radius: 8px;
  background-color: #fafafa;
  &__head {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 0 0 auto;
    padding: 8px 12px;
    border-bottom: 1px solid $border-color;
    font-size: 14px;
    font-weight: bold;
  }
  &__count {
    min-width: 20px;
    padding: 0 6px;
    border-radius: 10px;
    background-color: $background-color;
    color: $text-secondary-color;
    font-size: 12px;
    font-weight: normal;
    text-align: center;
  }
  &__add {
    margin-left: auto;
  }
  &__body {
    flex: 1;
    min-height: 0; // 关键：不加这行，卡片会把列撑高，列内滚动条永远不出现
    display: flex;
    flex-direction: column;
    gap: 8px;
    padding: 8px;
    overflow-y: auto;
  }
  &__empty {
    padding: 24px 0;
    color: $text-secondary-color;
    font-size: 13px;
    text-align: center;
  }
}
</style>
