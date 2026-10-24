<script lang="ts" setup>
import ProjectCard from './ProjectCard.vue'
import PageBar from '../../components/PageBar.vue'

import { onMounted, computed } from 'vue'

import { ElMessageBox } from 'element-plus'

import type { Project } from '@/types/project'
import { useProjectDelete } from '../composables/useProjectDelete'
import { useProjectStore } from '@/stores/project'

// 外部依赖
const { removeProject } = useProjectDelete()

const projectStore = useProjectStore()
const { manageSlice } = projectStore
const { pageState, load } = manageSlice
const projectList = computed(() => pageState.list)
// 事件处理
const askDelete = async (deletedProject: Project) => {
  try {
    await ElMessageBox.confirm(`确认删除项目 「${deletedProject.projectName}」 吗？`, '确认删除', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    })
  } catch {
    return
  }
  await removeProject(deletedProject.projectId)
}
// 派生
const hasProject = computed(() => projectList.value.length > 0)
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
  <!-- 项目卡片列表 -->
  <ProjectCard
    v-for="project in projectList"
    :key="project.displayedId"
    :currentProject="project"
    @delete="askDelete"
  />
  <!-- 分页栏 -->
  <PageBar
    v-show="hasProject"
    v-model:page="pageState.page"
    v-model:pageSize="pageState.pageSize"
    :total="pageState.total"
    :pageSizes="[5, 10, 20, 30]"
    @change="load()"
  />
</template>
<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
