<script lang="ts" setup>
import { ref } from 'vue'

import { CloseBold } from '@element-plus/icons-vue'

import { useProjectStore } from '@/stores/project'
import { storeToRefs } from 'pinia'
// 外部依赖
const projectStore = useProjectStore()
const { manageSlice } = projectStore
const { query, load } = manageSlice
</script>
<template>
  <div class="action-bar">
    <!-- 查询参数1: 项目名称 -->
    <el-input
      class="responsive-input"
      v-model="query.name"
      clearable
      :clear-icon="CloseBold"
      placeholder="请输入项目名称"
      @keyup.enter="load(1)"
    >
      <template #prefix>
        <el-icon class="el-input-icon"><search /></el-icon>
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
      <el-button class="search-btn" type="primary" @click="load(1)"
        ><el-icon><search /></el-icon
      ></el-button>
    </el-tooltip>
  </div>
</template>
<style scoped>
.action-bar {
  display: flex;
  flex-direction: row;
  align-items: center;
}
</style>
