<script lang="ts" setup>
import PageBar from '../../components/PageBar.vue'

import { ref, computed, onMounted } from 'vue'
import dayjs from 'dayjs'

import { PROJECT_STATUS } from '@/types/project'

import { useProjectStore } from '@/stores/project'

import '@/assets/styles/status.scss'
// 外部依赖
const projectStore = useProjectStore()
const { listSlice } = projectStore
const { pageState, load } = listSlice
const projectList = computed(() => pageState.list)
// 内部状态
const isDescriptionDialogVisible = ref<boolean>(false)
const currentDescription = ref<string>('')
// 配置
const statusClass = (status: number): string => PROJECT_STATUS[status]?.class ?? ''
const statusText = (status: number): string => PROJECT_STATUS[status]?.text ?? '未知'
// 派生
const hasProject = computed(() => projectList.value.length > 0)
const formatDate = (datetime?: string): string =>
  datetime ? dayjs(datetime).format('YYYY-MM-DD') : '-'
// 事件处理
const handleDescription = (rowDescription: string) => {
  currentDescription.value = rowDescription
  isDescriptionDialogVisible.value = true
}

// 初始化
const initData = () => {
  if (pageState.loaded === false) {
    load(1)
  }
}
onMounted(() => {
  initData()
})
</script>
<template>
  <!-- 项目列表 -->
  <div class="project-list">
    <el-divider />
    <el-table :data="projectList" border stripe style="width: 100%">
      <el-table-column prop="displayedId" label="ID" width="80" />
      <el-table-column prop="name" label="项目名称" min-width="150" />
      <el-table-column prop="leader" label="负责人" width="120" />
      <el-table-column label="状态" width="100" />
      <el-table-column>
        <template #default="{ row }">
          <span :class="statusClass(row.status)"> </span>
          {{ statusText(row.status) }}
        </template>
      </el-table-column>
      <el-table-column prop="progress" label="进度" />
      <el-table-column
        prop="createTime"
        label="创建时间"
        :formatter="(row: any) => formatDate(row.createTime)"
      />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="primary" @click="handleDescription(row.description)">详细</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!-- /项目列表 -->

  <!-- 分页栏 -->
  <PageBar
    v-show="hasProject"
    v-model:page="pageState.page"
    v-model:pageSize="pageState.pageSize"
    :total="pageState.total"
    :pageSizes="[5, 10, 20, 50]"
    @change="load()"
  />

  <!-- 项目介绍Dialog -->
  <el-dialog v-model="isDescriptionDialogVisible" title="项目介绍" width="500">
    <div class="description">
      {{ currentDescription || '暂无描述' }}
    </div>
  </el-dialog>
</template>
<style scoped>
.description {
  background-color: #ebebeb;
  border-radius: 3%;
  padding: 10px 0 0 10px;
  font-size: large;
  min-height: 200px;
}
</style>
