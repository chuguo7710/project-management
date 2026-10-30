<script setup lang="ts">
import { computed, nextTick, reactive, ref } from 'vue'

import type { FormInstance, FormRules } from 'element-plus'
import { storeToRefs } from 'pinia'

import { fetchProjectMembers } from '@/api/project'

import { useRequest } from '@/composables/useRequest'
import { useUserStore } from '@/stores/user'
import { TASK_PRIORITY, TASK_STATUS } from '@/types/task'
import type { TaskCreatePayload, TaskStatus } from '@/types/task'
import type { ProjectMember } from '@/types/project'
import { createEmptyPayload } from '@/types/task'

// props
const props = defineProps<{
  projectId: number
  isLoading: boolean
}>()
// emits
const emit = defineEmits<{
  create: [payload: TaskCreatePayload]
}>()
// 外部依赖
const userStore = useUserStore()
const { userData } = storeToRefs(userStore)
const fetchProjectMembersLatest = useRequest(fetchProjectMembers, { mode: 'latest' })
// 内部状态
const visible = ref<boolean>(false)
const isMemberLoading = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
// 内部数据
const members = ref<ProjectMember[]>([])
const membersProjectId = ref<number | null>(null) // 成员列表是按项目缓存的，切项目要重拉
const payload = reactive<TaskCreatePayload>(createEmptyPayload())
// 派生
const statusText = computed(() => TASK_STATUS[payload.status]?.text ?? '')
const dialogTitle = computed(() => `新建任务 · 落到「${statusText.value}」`)
// 校验规则
const rules: FormRules<TaskCreatePayload> = {
  title: [
    { required: true, message: '请输入任务标题', trigger: 'blur' },
    { max: 20, message: '标题不能超过 20 个字', trigger: 'blur' },
    {
      validator: (_rule, value: string, callback) => {
        if (!value.trim()) callback(new Error('标题不能只有空格'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
  description: [{ max: 255, message: '描述不能超过 255 个字', trigger: 'blur' }],
}

// 事件处理
/** 拉项目成员：只在下拉里选人，不允许手输 id */
const loadProjectMembers = async (projectId: number): Promise<void> => {
  if (membersProjectId.value === projectId) return // 同一个项目复用上次结果
  isMemberLoading.value = true

  try {
    const res = await fetchProjectMembersLatest.run(projectId)
    if (!res) return
    members.value = res.data
    membersProjectId.value = projectId
  } catch {
    members.value = []
    membersProjectId.value = null
  } finally {
    if (!fetchProjectMembersLatest.isLoading.value) isMemberLoading.value = false
  }
}

/** 发送创建任务申请 */
const handleSubmit = async (): Promise<void> => {
  if (!formRef.value) return
  const isValid = await formRef.value.validate().catch(() => false)
  if (!isValid) return
  emit('create', payload)
}

// 对外暴露
const open = async (status: TaskStatus): Promise<void> => {
  Object.assign(payload, createEmptyPayload())
  payload.status = status === 2 ? 2 : 1
  payload.projectId = props.projectId
  console.log('payload.projectId', payload.projectId)
  visible.value = true
  await nextTick() // 等表单挂载完再清校验，否则 formRef 还是 null
  formRef.value?.clearValidate()
  await loadProjectMembers(props.projectId)
  // 默认负责人=自己
  payload.assigneeId =
    members.value.find((member) => member.username === userData.value?.username)?.userId ?? null
}
const close = (): void => {
  visible.value = false
}
defineExpose({ open, close })
</script>

<template>
  <el-dialog v-model="visible" :title="dialogTitle" width="560px">
    <el-form
      ref="formRef"
      :model="payload"
      :rules="rules"
      label-width="80px"
      label-position="right"
    >
      <!-- 任务标题、描述、负责人、优先级、截止日 -->
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="payload.title"
          autocomplete="off"
          placeholder="一句话说清要做什么"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="payload.description"
          type="textarea"
          autocomplete="off"
          placeholder="补充背景、验收标准（可空）"
          maxlength="255"
          show-word-limit
          word-limit-position="outside"
          :autosize="{ minRows: 3, maxRows: 6 }"
          resize="none"
        />
      </el-form-item>
      <el-form-item label="负责人" prop="assigneeId">
        <el-select
          v-model="payload.assigneeId"
          :loading="isMemberLoading"
          :value-on-clear="null"
          clearable
          placeholder="未指派"
          style="width: 100%"
        >
          <el-option
            v-for="member in members"
            :key="member.userId"
            :label="member.username"
            :value="member.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-radio-group v-model="payload.priority">
          <el-radio v-for="priority in TASK_PRIORITY" :key="priority.val" :value="priority.val">
            {{ priority.text }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="截止日" prop="deadline">
        <el-date-picker
          v-model="payload.deadline"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="不设截止日"
          clearable
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <!-- 底部按钮：取消、创建 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" :loading="props.isLoading" @click="handleSubmit">
          创建
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
