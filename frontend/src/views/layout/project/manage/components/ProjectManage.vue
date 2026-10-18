<script lang="ts" setup>
import { onMounted, computed } from 'vue'

import { useProjectStore } from '@/stores/project'

import ProjectCard from './ProjectCard.vue'
// 外部依赖
const projectStore = useProjectStore()
const { manageSlice } = projectStore
const { pageState, load } = manageSlice
const projectList = computed(() => pageState.list)
// 派生
const hasProject = computed(() => projectList.value.length > 0)
// 事件处理
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
  <ProjectCard
    v-for="project in projectList"
    :key="project.displayedId"
    :currentProject="project"
  />
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
</template>
<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
