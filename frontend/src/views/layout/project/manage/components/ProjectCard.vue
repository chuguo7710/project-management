<script setup lang="ts">
import dayjs from 'dayjs'

import { useProjectDelete } from '../composables/useProjectDelete'
import { PROJECT_STATUS } from '@/types/project'
import type { Project } from '@/types/project'

import '@/asset/styles/status.scss'

const { deleteProject } = useProjectDelete()
// 定义 props，父组件必须传 project
const props = defineProps<{
  currentProject: Project
}>()
// 配置
const statusClass = (status: number): string => PROJECT_STATUS[status]?.class ?? ''
const statusText = (status: number): string => PROJECT_STATUS[status]?.text ?? '未知'

// 派生
const formatDate = (datetime?: string): string =>
  datetime ? dayjs(datetime).format('YYYY-MM-DD') : '-'
const handleDelete = (id: number) => {
  deleteProject(id)
}
</script>
<template>
  <div class="project-card">
    <!-- 标题行：图标 + 名称 + 状态 -->
    <div class="title">
      <span>{{ currentProject.name }}</span>
      <span>
        <span :class="statusClass(currentProject.status)"></span>
        {{ statusText(currentProject.status) }}
      </span>
    </div>

    <!-- 分割线 -->
    <el-divider style="margin: 12px 0" />

    <!-- 描述 -->
    <div class="description">
      {{ currentProject.description || '暂无描述' }}
    </div>

    <!-- 信息行：负责人、成员数、进度 -->
    <div class="info-row">
      <span class="info-item">
        <span class="label">负责人：</span>
        <span>{{ currentProject.leader }}</span>
      </span>
      <span class="info-item">
        <span class="label">成员：</span>
        <span>{{ currentProject.memberCount ?? 0 }}</span>
      </span>
      <span class="info-item progress-item">
        <span class="label">进度：</span>
        <el-progress
          :percentage="currentProject.progress"
          :stroke-width="8"
          :show-text="false"
          style="width: 80px; display: inline-block; vertical-align: middle"
        />
        <span class="progress-text">{{ currentProject.progress }}%</span>
      </span>
    </div>

    <!-- 创建时间 -->
    <div class="create-time">创建时间：{{ formatDate(currentProject.createTime) }}</div>

    <!-- 操作按钮 -->
    <div class="actions">
      <el-button v-if="currentProject.isLeader" type="primary" link size="small" @click="">
        编辑
      </el-button>
      <el-button v-if="currentProject.isLeader" type="primary" link size="small" @click="">
        成员管理
      </el-button>
      <el-button
        v-if="currentPro ject.isLeader"
        type="danger"
        link
        size="small"
        @click="handleDelete(currentProject.id)"
      >
        删除
      </el-button>
      <el-button type="success" link size="small" @click=""> 进入看板 </el-button>
    </div>
  </div>
</template>
<style scoped>
.project-card {
  width: 700px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  margin: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}
.info-row {
  width: 400px;
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}
</style>
