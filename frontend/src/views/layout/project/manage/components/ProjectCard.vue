<script setup lang="ts">
import dayjs from 'dayjs'
import { computed } from 'vue'

import { PROJECT_STATUS } from '@/types/project'
import type { Project } from '@/types/project'
import { useUserStore } from '@/stores/user'

import '@/assets/styles/status.scss'

// 定义 props，父组件必须传 project
const props = defineProps<{
  currentProject: Project
}>()

const emit = defineEmits<{
  delete: [deletedProject: Project]
}>()
// 外部依赖
const userStore = useUserStore()
// 配置
const statusClass = (status: number): string => PROJECT_STATUS[status]?.class ?? ''
const statusText = (status: number): string => PROJECT_STATUS[status]?.text ?? '未知'
// 内部数据
const canEdit = computed(() => props.currentProject.canEdit)
const canManage = canEdit
const canDelete = canEdit
// 派生
const formatDate = (datetime?: string): string =>
  datetime ? dayjs(datetime).format('YYYY-MM-DD') : '-'

const handleEdit = () => {}
const handleMembers = () => {}
const handleBoard = () => {}
</script>
<template>
  <div class="project-card">
    <div class="main">
      <!-- 标题行：图标 + 名称 + 状态 -->
      <div class="title">
        <span class="project-name">{{ currentProject.projectName }}</span>
        <span>
          <span :class="statusClass(currentProject.status)"></span>
          {{ statusText(currentProject.status) }}
        </span>
      </div>
      <!-- 分割线 -->
      <el-divider style="margin: 4px 0" />
      <!-- 信息行：负责人、成员数、进度 -->
      <div class="info-row">
        <!-- 负责人 -->
        <div class="info-item">
          <span class="label">负责人：</span>
          <span class="leader">{{ currentProject.leaderName }}</span>
        </div>
        <!-- 成员数、进度 -->
        <div class="info-item">
          <span class="label">成员：</span>
          <span>{{ currentProject.memberCount ?? 0 }}</span>
        </div>
        <div class="info-item">
          <span class="label">进度：</span>
          <el-progress
            :percentage="currentProject.progress"
            :stroke-width="8"
            :show-text="false"
            style="width: 80px; display: inline-block; vertical-align: middle"
          />
          <span class="progress-text">{{ currentProject.progress }}%</span>
        </div>
        <!-- 创建时间 -->
        <div class="create-time">创建时间：{{ formatDate(currentProject.createTime) }}</div>
      </div>

      <!-- 操作按钮 -->
      <div class="actions">
        <el-tooltip content="只有项目负责人可以编辑" :disabled="canEdit" placement="top">
          <el-button type="primary" link :disabled="!canEdit" @click="handleEdit">编辑</el-button>
        </el-tooltip>

        <el-tooltip content="只有项目负责人可以管理成员" :disabled="canManage" placement="top">
          <el-button type="primary" link :disabled="!canManage" @click="handleMembers"
            >成员管理</el-button
          >
        </el-tooltip>

        <el-button type="primary" link @click="handleBoard">进入看板</el-button>

        <el-tooltip content="只有项目负责人可以删除" :disabled="canDelete">
          <el-button
            type="danger"
            link
            :disabled="!canDelete"
            @click="emit('delete', currentProject)"
          >
            删除</el-button
          >
        </el-tooltip>
      </div>
    </div>
    <!-- 描述 -->
    <div class="description">
      <div class="description-title">项目描述：</div>
      <el-scrollbar class="description-text">{{
        currentProject.description || '暂无描述'
      }}</el-scrollbar>
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;
.project-card {
  width: 700px;
  height: 220px;
  margin: 8px;
  padding: 8px 14px;
  border: 1px solid $border-color;
  border-radius: 8px;
  box-shadow: 0 2px 4px $shadow-color;
  display: flex;
  .main {
    flex: 0 0 350px;
    display: flex;
    flex-direction: column;
    .title {
      padding: 8px 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 18px;
      font-weight: bold;
      .project-name {
        flex: 1;
        min-width: 0;
        margin-right: 10px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        overflow-wrap: anywhere;
      }
    }
    .info-row {
      margin-top: auto;
      gap: 8px;
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      align-items: flex-start;
    }
    .actions {
      height: 32px;
      display: flex;
      gap: 8px;
      align-items: flex-end;
    }
  }
  .description {
    flex: 1;
    display: flex;
    flex-direction: column;
    margin: 8px 0 8px 10px;
    border-left: 1px solid $border-color;
    padding-left: 10px;
    color: $text-secondary-color;
    .description-title {
      flex: 0 0 auto;
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 8px;
    }
    .description-text {
      flex: 1;
      min-height: 0;
      font-size: 14px;
      padding: 10px 0 0 10px;
      border-radius: 6px;
      background-color: $background-color;
      overflow-wrap: anywhere;
    }
  }
}
</style>
