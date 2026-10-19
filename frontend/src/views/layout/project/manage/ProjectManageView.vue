<script setup lang="ts">
import ActionBar from '../components/ActionBar.vue'
import ProjectManage from './components/ProjectManage.vue'

import { useProjectStore } from '@/stores/project'

const projectStore = useProjectStore()
const { manageSlice } = projectStore
const { query, load, resetQuery } = manageSlice
// 事件处理
const handleReset = () => {
  resetQuery()
  load(1)
}
</script>
<template>
  <ActionBar v-model:name="query.name" v-model:leader="query.leader" @search="load(1)" @reset="handleReset" />
  <ProjectManage />
</template>
