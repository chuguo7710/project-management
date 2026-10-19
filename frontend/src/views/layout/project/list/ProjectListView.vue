<script lang="ts" setup>
import ActionBar from '../components/ActionBar.vue'
import ProjectCreatedButton from '../components/ProjectCreateButton.vue'
import ProjectList from './components/ProjectList.vue'

import { useProjectStore } from '@/stores/project'

const projectStore = useProjectStore()
const { listSlice } = projectStore
const { query, load, resetQuery } = listSlice

// 事件处理
const handleReset = () => {
  resetQuery()
  load(1)
}
</script>
<template>
  <div class="project">
    <ActionBar
      v-model:name="query.name"
      v-model:leader="query.leader"
      @search="load(1)"
      @reset="handleReset"
    >
      <ProjectCreatedButton />
    </ActionBar>
    <ProjectList />
  </div>
</template>
