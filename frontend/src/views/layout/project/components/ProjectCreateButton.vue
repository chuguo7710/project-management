<script lang="ts" setup>
import { ref } from 'vue'

import { useProjectStore } from '@/stores/project'
import { storeToRefs } from 'pinia'

// props
const open = () => {
  isCreateProjectDialogVisible.value = true
}
defineExpose({ open })
// 外部依赖
const projectStore = useProjectStore()
const { projectForm, isCreateProjectLoading } = storeToRefs(projectStore)
// 事件处理
const submitDialog = async () => {
  const isSuccess = await projectStore.createNewProject()
  if (isSuccess) {
    closeDialog()
  }
}
const closeDialog = () => {
  isCreateProjectDialogVisible.value = false
  setTimeout(() => {
    projectStore.resetForm()
  }, 500)
}
// 内部状态
const isCreateProjectDialogVisible = ref<boolean>(false)
</script>
<template>
  <!-- 项目创建按钮-->
  <el-tooltip
    class="create-project-tooltip"
    effect="dark"
    placement="top-start"
    content="新增项目"
    show-after="500"
  >
    <el-button
      class="create-project-btn"
      type="primary"
      @click="isCreateProjectDialogVisible = true"
      ><el-icon><plus /></el-icon
    ></el-button>
  </el-tooltip>
  <!-- 项目创建Dialog -->
  <el-dialog v-model="isCreateProjectDialogVisible" title="项目创建信息" width="500">
    <el-form label-position="right" label-width="auto" :model="projectForm">
      <el-form-item label="项目名称">
        <el-input v-model="projectForm.name" autocomplete="off" placeholder="请输入项目名称" />
      </el-form-item>
      <el-form-item label="项目状态">
        <el-radio-group v-model="projectForm.status">
          <el-radio :value="1">进行中</el-radio>
          <el-radio :value="2">已暂停</el-radio>
          <el-radio :value="3">已完成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="项目介绍">
        <el-input
          v-model="projectForm.description"
          autocomplete="off"
          placeholder="请输入项目介绍"
          style="width: 400px"
          maxlength="255"
          show-word-limit="true"
          word-limit-position="outside"
          :autosize="{ minRows: 2, maxRows: 5 }"
          resize="none"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" :loading="isCreateProjectLoading" @click="submitDialog">
          创建
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
