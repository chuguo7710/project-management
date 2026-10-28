<script setup lang="ts">
import { nextTick, reactive, ref } from 'vue'

import type { FormInstance, FormRules } from 'element-plus'

import type { Project } from '@/types/project'
import { useProject } from '../../composables/useProject'

type EditForm = Pick<Project, 'projectName' | 'status' | 'description'>

// 外部依赖
const { editProject } = useProject()
// 内部状态
const visible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const currentProject = ref<Project | null>(null)
// 内部数据
const form = reactive<EditForm>({
  projectName: '',
  status: 1,
  description: '',
})
// 校验规则
const rules: FormRules<EditForm> = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 50, message: '项目名称长度为 2 ~ 50 个字', trigger: 'blur' },
  ],
  description: [{ max: 255, message: '项目介绍不能超过 255 个字', trigger: 'blur' }],
}

// 事件处理
const handleSubmit = async () => {
  if (!formRef.value || !currentProject.value) return
  const isValid = await formRef.value.validate().catch(() => false)
  if (!isValid) return
  const isEdited = await editProject(currentProject.value.projectId, form)
  if (!isEdited) return
  visible.value = false
}
const handleClosed = () => {
  currentProject.value = null
  Object.assign(form, { projectName: '', status: 1, description: '' })
  formRef.value?.clearValidate()
}
// 对外暴露
const open = async (project: Project) => {
  currentProject.value = project
  Object.assign(form, {
    projectName: project.projectName,
    status: project.status,
    description: project.description,
  })
  visible.value = true
  await nextTick() // 等表单挂载完再清校验，否则 formRef 还是 null
  formRef.value?.clearValidate()
}
defineExpose({ open })
</script>

<template>
  <el-dialog v-model="visible" title="编辑项目" width="560px" @closed="handleClosed">
    <!-- 只读概要：这些字段不该在「编辑」里改 -->
    <div class="project-meta">
      <div class="meta-item">
        <span class="meta-label">项目ID</span>
        <span class="meta-value">#{{ currentProject?.projectId ?? '-' }}</span>
      </div>
      <div class="meta-item">
        <span class="meta-label">负责人</span>
        <span class="meta-value">{{ currentProject?.leaderName || '-' }}</span>
      </div>
      <div class="meta-item">
        <span class="meta-label">创建时间</span>
        <span class="meta-value">{{ currentProject?.createTime || '-' }}</span>
      </div>
      <div class="meta-item meta-item--full">
        <span class="meta-label">项目进度</span>
        <span class="meta-value">
          <el-progress
            class="meta-progress"
            :percentage="currentProject?.progress ?? 0"
            :stroke-width="8"
            :show-text="false"
          />
          <span class="meta-progress-text">{{ currentProject?.progress ?? 0 }}%</span>
          <span class="meta-hint">由任务看板自动统计</span>
        </span>
      </div>
    </div>

    <!-- 可编辑区 -->
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" label-position="right">
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="form.projectName"
          autocomplete="off"
          placeholder="请输入项目名称"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="项目状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">进行中</el-radio>
          <el-radio :value="2">已暂停</el-radio>
          <el-radio :value="3">已完成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="项目介绍" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          autocomplete="off"
          placeholder="请输入项目介绍"
          maxlength="255"
          show-word-limit
          word-limit-position="outside"
          :autosize="{ minRows: 3, maxRows: 6 }"
          resize="none"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.project-meta {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px 24px;
  margin-bottom: 20px;
  padding: 12px 16px;
  background-color: $background-color;
  border-radius: 8px;

  .meta-item {
    display: flex;
    align-items: center;
    min-width: 0;
    font-size: 13px;
    line-height: 20px;
  }
  .meta-item--full {
    grid-column: 1 / -1;
  }
  .meta-label {
    flex: 0 0 64px;
    color: $text-secondary-color;
  }
  .meta-value {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;
    gap: 8px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .meta-progress {
    width: 120px;
  }
  .meta-progress-text {
    color: $text-secondary-color;
  }
  .meta-hint {
    font-size: 12px;
    color: $text-secondary-color;
    opacity: 0.8;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
