<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import dayjs from 'dayjs'

import { PROJECT_STATUS } from '@/types/project'

import { useProjectStore } from '@/stores/project'

import '@/asset/styles/status.scss'
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
const handlePageSizeChange = () => {
  load(1) // 每页条数改变，回到第一页
}
const handleCurrentPageChange = () => {
  load() // 当前页改变，加载当前页数据
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
  <div class="project-list">
    <action-bar />
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

  <!-- 分页栏 -->
  <div class="pagination" v-show="hasProject">
    <el-pagination
      background
      layout="sizes, prev, pager, next"
      :page-sizes="[5, 10, 20, 50]"
      :total="pageState.total"
      v-model:current-page="pageState.page"
      v-model:page-size="pageState.pageSize"
      @size-change="handlePageSizeChange"
      @current-change="handleCurrentPageChange"
    />
  </div>

  <!-- 项目介绍Dialog -->
  <el-dialog v-model="isDescriptionDialogVisible" title="项目介绍" width="500">
    <div class="description">
      {{ currentDescription || '暂无描述' }}
    </div>
  </el-dialog>
</template>
<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
.description {
  background-color: #ebebeb;
  border-radius: 3%;
  padding: 10px 0 0 10px;
  font-size: large;
  min-height: 200px;
}
</style>
