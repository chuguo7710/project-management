<script lang="ts" setup>
import { ref } from 'vue'

import { CloseBold } from '@element-plus/icons-vue'
import { ElInput } from 'element-plus'
// props
const projectName = defineModel<string>('projectName', { default: '' })
const leaderName = defineModel<string>('leaderName', { default: '' })
const emit = defineEmits<{
  search: []
  reset: []
}>()
// 内部状态
const leaderInput = ref<InstanceType<typeof ElInput> | null>(null)
// 事件处理
function leaderInputFocus() {
  leaderInput.value?.focus()
}
</script>
<template>
  <div class="action-bar">
    <!-- 查询栏 -->
    <div class="search-bar">
      <!-- 查询参数1: 项目名称 -->
      <el-input
        class="responsive-input"
        v-model="projectName"
        clearable
        :clear-icon="CloseBold"
        placeholder="请输入项目名称"
        @keyup.enter="leaderInputFocus"
      >
        <template #prefix>
          <el-icon class="el-input-icon"><search /></el-icon>
        </template>
      </el-input>
      <!-- 查询参数2: 项目负责人 -->
      <el-input
        class="responsive-input"
        ref="leaderInput"
        v-model="leaderName"
        clearable
        :clear-icon="CloseBold"
        placeholder="请输入项目负责人"
        @keyup.enter="emit('search')"
      >
        <template #prefix>
          <el-icon class="el-input-icon"><user /></el-icon>
        </template>
      </el-input>
      <!-- 按钮1:查询项目 -->
      <el-tooltip
        class="search-tooltip"
        effect="dark"
        placement="top-start"
        content="查询"
        show-after="500"
      >
        <el-button class="search-btn" type="primary" @click="emit('search')"
          ><el-icon><search /></el-icon
        ></el-button>
      </el-tooltip>
      <!-- 额外按钮插槽-->
      <slot />
    </div>
    <!-- /查询栏 -->

    <!-- 工具栏 -->
    <div class="tools-bar">
      <el-button @click="emit('reset')" type="primary">清空</el-button>
    </div>
    <!-- /工具栏 -->
  </div>
</template>

<style scoped>
.action-bar {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin: 0 0 24px 10px;
}
.search-bar {
  width: 800px;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;
}
</style>
