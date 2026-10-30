<script lang="ts" setup>
import dayjs from 'dayjs'
import { computed, ref } from 'vue'

import { TASK_PRIORITY, TASK_STATUS } from '@/types/task'
import type { TaskStatus, TaskVO } from '@/types/task'

// props
const props = defineProps<{
  task: TaskVO | null
}>()
// emits
const emit = defineEmits<{
  'status-change': [status: TaskStatus]
  remove: []
}>()
// 内部状态
const visible = ref<boolean>(false)
// 常量
const NEXT_LABEL: Record<TaskStatus, string> = {
  1: '重新打开',
  2: '开始处理',
  3: '标记完成',
}
// 派生 · 展示
const statusMeta = computed(() => (props.task ? TASK_STATUS[props.task.status] : null))
const priorityMeta = computed(() => TASK_PRIORITY[props.task?.priority ?? 2])
const assigneeInitial = computed(() => (props.task?.assigneeName ?? '?').slice(0, 1))
const isDone = computed(() => props.task?.status === 3)
// 派生 · 状态推进：已完成 → 回到待办；待办 → 进行中；进行中 → 已完成
const nextStatus = computed<TaskStatus>(() => {
  const status = props.task?.status ?? 1
  if (status === 1) return 2
  if (status === 2) return 3
  return 1
})
const nextLabel = computed(() => NEXT_LABEL[nextStatus.value])
// 派生 · 日期：截止日只到「天」，时间精确到分
const deadlineText = computed(() =>
  props.task?.deadline ? dayjs(props.task.deadline).format('YYYY-MM-DD') : '—',
)
const overdueDays = computed(() => {
  if (!props.task?.deadline || isDone.value) return 0
  const diff = dayjs().startOf('day').diff(dayjs(props.task.deadline).startOf('day'), 'day')
  return diff > 0 ? diff : 0
})
const createTimeText = computed(() =>
  props.task?.createTime ? dayjs(props.task.createTime).format('YYYY-MM-DD HH:mm') : '—',
)
const completedAtText = computed(() =>
  props.task?.completedAt ? dayjs(props.task.completedAt).format('YYYY-MM-DD HH:mm') : '—',
)
// 对外暴露
const open = () => {
  visible.value = true
}
const close = () => {
  visible.value = false
}
defineExpose({
  open,
  close,
})
</script>

<template>
  <el-drawer v-model="visible" class="task-detail-drawer" direction="rtl" size="420px">
    <template #header>
      <div class="detail-head">
        <span class="detail-head__title">任务详情</span>
        <span v-if="task?.projectName" class="detail-head__project">{{ task.projectName }}</span>
      </div>
    </template>
    <template #default>
      <div v-if="task" class="detail">
        <!-- ① 可滚动区：先「是什么」，再「谁/什么时候」，最后「详细说」 -->
        <div class="detail__scroll">
          <!-- 状态 / 优先级 / 编号 -->
          <div class="detail__eyebrow">
            <span
              class="detail__badge"
              :style="{ color: statusMeta?.color, borderColor: statusMeta?.color }"
            >
              {{ statusMeta?.text }}
            </span>
            <span class="detail__priority" :style="{ color: priorityMeta.color }">
              <i class="detail__dot" :style="{ backgroundColor: priorityMeta.color }"></i>
              {{ priorityMeta.text }}
            </span>
            <span class="detail__id">#{{ task.taskId }}</span>
          </div>

          <!-- 标题 -->
          <h2 class="detail__title">{{ task.title }}</h2>

          <!-- 信息表：左列标签、右列值 -->
          <dl class="detail__meta">
            <dt>负责人</dt>
            <dd>
              <span class="detail__avatar">{{ assigneeInitial }}</span>
              {{ task.assigneeName ?? '未指派' }}
            </dd>

            <dt>创建人</dt>
            <dd>{{ task.creatorName ?? '—' }}</dd>

            <dt>截止日期</dt>
            <dd>
              <span class="detail__date" :class="{ 'is-overdue': overdueDays > 0 }">
                {{ deadlineText }}
              </span>
              <span v-if="overdueDays > 0" class="detail__flag">逾期 {{ overdueDays }} 天</span>
            </dd>

            <dt>创建时间</dt>
            <dd class="detail__plain">{{ createTimeText }}</dd>

            <template v-if="isDone">
              <dt>完成时间</dt>
              <dd class="detail__plain">{{ completedAtText }}</dd>
            </template>
          </dl>

          <!-- 描述 -->
          <h3 class="detail__subtitle">描述</h3>
          <p v-if="task.description" class="detail__desc">{{ task.description }}</p>
          <p v-else class="detail__desc is-empty">还没有填写描述</p>
        </div>

        <!-- ② 底部操作条：破坏性操作在左，主操作在右（关闭由抽屉右上角 × 负责） -->
        <footer class="detail__footer">
          <el-button type="danger" plain size="default" @click="emit('remove')">删除</el-button>
          <el-button
            type="primary"
            size="default"
            @click="emit('status-change', nextStatus)"
          >
            {{ nextLabel }}
          </el-button>
        </footer>
      </div>
      <el-empty v-else description="没有选中的任务" />
    </template>
  </el-drawer>
</template>

<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

// 抽屉本体是 Element Plus 渲染的，scoped 够不到，只能靠自定义 class 兜一层
:global(.task-detail-drawer .el-drawer__body) {
  display: flex;
  flex-direction: column;
  padding: 0;
  overflow: hidden;
}
:global(.task-detail-drawer .el-drawer__header) {
  margin-bottom: 0;
  padding: 16px 20px;
  border-bottom: 1px solid $border-color;
}

.detail-head {
  display: flex;
  align-items: baseline;
  gap: 8px;
  &__title {
    font-size: 15px;
    font-weight: 600;
    color: #1f1f1f;
  }
  &__project {
    font-size: 12px;
    color: $text-secondary-color;
  }
}

.detail {
  display: flex;
  flex: 1;
  flex-direction: column;
  min-height: 0;
  color: #333;

  // 中间滚动，头脚固定
  &__scroll {
    flex: 1;
    min-height: 0;
    padding: 16px 20px 20px;
    overflow-y: auto;
  }
  &__eyebrow {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 12px;
  }
  &__badge {
    padding: 1px 8px;
    border: 1px solid currentColor;
    border-radius: 10px;
    line-height: 18px;
  }
  &__priority {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    color: $text-secondary-color;
  }
  &__dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
  }
  &__id {
    margin-left: auto;
    color: #bbb;
    font-variant-numeric: tabular-nums;
  }
  &__title {
    margin: 10px 0 18px;
    font-size: 18px;
    font-weight: 600;
    line-height: 1.5;
    word-break: break-word;
  }
  // dt / dd 直接用 grid 排成两列，每一对自动成行
  &__meta {
    display: grid;
    grid-template-columns: 64px 1fr;
    gap: 10px 12px;
    margin: 0 0 18px;
    padding-bottom: 18px;
    border-bottom: 1px solid $border-color;
    font-size: 13px;
    dt {
      color: #999;
    }
    dd {
      display: flex;
      align-items: center;
      gap: 6px;
      margin: 0;
      word-break: break-word;
    }
  }
  &__avatar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #f0f2f5;
    color: $text-secondary-color;
    font-size: 11px;
    text-transform: uppercase;
  }
  &__date {
    font-variant-numeric: tabular-nums;
    & + .detail__flag {
      margin-left: 6px;
    }
    &.is-overdue {
      color: $status-stop-border-color;
    }
  }
  &__flag {
    padding: 0 6px;
    border-radius: 4px;
    background-color: #fdecec;
    color: $status-stop-border-color;
    font-size: 11px;
    line-height: 18px;
  }
  &__plain {
    color: $text-secondary-color;
    font-variant-numeric: tabular-nums;
  }
  &__subtitle {
    margin: 0 0 8px;
    font-size: 13px;
    font-weight: 600;
    color: #333;
  }
  &__desc {
    margin: 0;
    font-size: 13px;
    line-height: 1.8;
    color: #333;
    white-space: pre-wrap; // 保留用户敲的换行
    word-break: break-word;
    &.is-empty {
      color: #bbb;
    }
  }
  &__footer {
    display: flex;
    flex: 0 0 auto;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    padding: 12px 20px;
    border-top: 1px solid $border-color;
    background-color: #fff;
  }
}
</style>
