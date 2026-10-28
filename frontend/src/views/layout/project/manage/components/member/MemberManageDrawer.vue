<script lang="ts" setup>
import MemberCard from './MemberCard.vue'
import AddMemberDialog from './MemberAddDialog.vue'
import { ref } from 'vue'

import { ElMessageBox } from 'element-plus'

import type { Project, ProjectMember } from '@/types/project'
import { useProjectMembers } from '@/views/layout/project/manage/composables/useProjectMembers'

// 外部依赖
const manageProjectMembersSlice = useProjectMembers()
const { list, searchProjectMembers, removeProjectMember } = manageProjectMembersSlice
// 内部状态
const memberDrawer = ref<boolean>(false)
const addMemberDialogRef = ref<InstanceType<typeof AddMemberDialog> | null>(null)
const currentProject = ref<Project | null>(null)
const activeCardId = ref<number | null>(null)
// 派生
const toggleCard = (member: ProjectMember) => {
  activeCardId.value = activeCardId.value === member.userId ? null : member.userId
}
// 事件处理
const handleDetail = () => {}
const openAddDialog = () => {
  if (!currentProject.value) return
  addMemberDialogRef.value?.open(currentProject.value)
}
const askRemove = async (member: ProjectMember) => {
  if (!currentProject.value) return
  try {
    await ElMessageBox.confirm(`确定要移除成员 ${member.username} 吗？`, '确认移除', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    })
  } catch {
    return
  }
  const isRemoved = await removeProjectMember(currentProject.value.projectId, member.userId)
  if (isRemoved) {
    activeCardId.value = null
    currentProject.value.memberCount = list.value.length
  }
}

const addMemberSuccess = async (project: Project) => {
  currentProject.value = project
  if (!currentProject.value) return
  await searchProjectMembers(currentProject.value.projectId)
  currentProject.value.memberCount = list.value.length
}
// 对外暴露
const open = async (project: Project) => {
  if (!project) throw new Error('project is required')
  currentProject.value = project
  activeCardId.value = null
  const isSearched = await searchProjectMembers(project.projectId)
  if (!isSearched) return
  memberDrawer.value = true
}
defineExpose({
  open,
})
</script>
<template>
  <div class="member-manage">
    <el-drawer v-model="memberDrawer" direction="rtl" size="420px">
      <template #header>
        <span>成员管理</span>
      </template>
      <template #default>
        <div class="member-cards">
          <MemberCard
            v-for="member in list"
            :key="member.userId"
            :member="member"
            :click-card="activeCardId === member.userId"
            @toggle="toggleCard"
            @detail="handleDetail"
            @remove="askRemove"
          />
        </div>
        <!-- 添加成员按钮 -->
        <div class="member-add" @click="openAddDialog">
          <el-icon><Plus /></el-icon>
        </div>
      </template>
    </el-drawer>
    <!-- 添加成员弹窗 -->
    <AddMemberDialog ref="addMemberDialogRef" @success="addMemberSuccess" />
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.member-manage {
  display: flex;
  flex-direction: column;
  .member-add {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 60px;
    border: 1px dashed $border-color; // 虚线表示「可点但还没有单位」
    border-radius: 8px;
    color: $text-secondary-color;
    cursor: pointer;
    transition: all 0.2s;
    &:hover {
      border-color: $primary-color;
      color: $primary-color;
    }
  }
}
</style>
