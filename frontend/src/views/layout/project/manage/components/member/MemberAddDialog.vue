<script setup lang="ts">
import { ref } from 'vue'

import { useProjectMembers } from '../../composables/useProjectMembers'
import type { Project } from '@/types/project'
// emit
const emit = defineEmits<{
  success: [project: Project]
}>()
// 外部依赖
const manageProjectMembersSlice = useProjectMembers()
const { addProjectMember } = manageProjectMembersSlice
// 内部状态
const visible = ref<boolean>(false)
// 内部数据
const currentProject = ref<Project | null>(null)
const userId = ref<number | null>(null)
// 事件处理
const handleAdd = async () => {
  if (!currentProject.value || !userId.value) return
  const isAdded = await addProjectMember(currentProject.value?.projectId, userId.value)
  if (!isAdded) return
  await emit('success', currentProject.value)
  visible.value = false
}
// 对外暴露
const open = (project: Project) => {
  visible.value = true
  currentProject.value = project
}
defineExpose({
  open,
})
</script>
<template>
  <el-dialog v-model="visible" @closed="userId = null">
    <p>添加成员</p>
    <input v-model="userId" placeholder="请输入需要提添加的成员id" />
    <el-button type="primary" @click="handleAdd">添加</el-button>
  </el-dialog>
</template>
