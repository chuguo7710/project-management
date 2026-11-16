<script setup lang="ts">
import ActionBar from './components/ActionBar.vue'
import UserList from './components/UserList.vue'
import { storeToRefs } from 'pinia'

import { useUserStore } from '@/stores/user'

// 外部依赖
const userStore = useUserStore()
const { pageState, userList, isSearchLoading } = storeToRefs(userStore)
// 派生
const reset = () => {
  userStore.resetQuery()
  userStore.searchUserList(1)
}
const init = async () => {
  if (pageState.value.loaded === true) return
  const isSearched = await userStore.searchUserList(1)
  if (!isSearched) return
  pageState.value.loaded = true
}
</script>

<template>
  <div class="user">
    <!-- 查询栏 -->
    <ActionBar
      v-model:username="pageState.query.username"
      @search="userStore.searchUserList()"
      @reset="reset()"
    />

    <!-- 列表 -->
    <UserList
      v-model:user-list="userList"
      v-model:page="pageState.query.page"
      v-model:page-size="pageState.query.pageSize"
      :total="pageState.total"
      :is-loading="isSearchLoading"
      @init="init()"
      @change="userStore.searchUserList()"
    />
  </div>
</template>
