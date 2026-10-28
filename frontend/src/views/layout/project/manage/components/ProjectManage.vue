<script lang="ts" setup>
import ProjectCard from './ProjectCard.vue'
import PageBar from '../../components/PageBar.vue'
import MemberManageDrawer from './member/MemberManageDrawer.vue'
import ProjectEditDialog from './edit/ProjectEditDialog.vue'

import { ref, onMounted, computed } from 'vue'

import { ElMessageBox } from 'element-plus'

import type { Project } from '@/types/project'
import { useProject } from '../composables/useProject.ts'
import { useProjectStore } from '@/stores/project'

// 外部依赖
const { removeProject } = useProject()

const projectStore = useProjectStore()
const { manageSlice } = projectStore
const { pageState, load } = manageSlice
const projectList = computed(() => pageState.list)
// 内部状态
const memberManageDrawerRef = ref<InstanceType<typeof MemberManageDrawer> | null>(null)
const editDialogRef = ref<InstanceType<typeof ProjectEditDialog> | null>(null)
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
const handleMembersDrawer = (project: Project) => {
  memberManageDrawerRef.value?.open(project)
}
const handleEditDialog = (project: Project) => {
  editDialogRef.value?.open(project)
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
  <div class="project-manage">
    <!-- 项目卡片列表 -->
    <ProjectCard
      v-for="project in projectList"
      :key="project.displayedId"
      :project="project"
      @delete="askDelete"
      @members="handleMembersDrawer"
      @edit="handleEditDialog"
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
    <!-- 成员管理抽屉 -->
    <MemberManageDrawer ref="memberManageDrawerRef" />
    <!-- 编辑项目弹窗 -->
    <ProjectEditDialog ref="editDialogRef" />
  </div>
</template>
<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
