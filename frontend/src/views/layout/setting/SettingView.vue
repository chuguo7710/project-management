<script lang="ts" setup>
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
const store = useUserStore()
const { userData } = storeToRefs(store)
</script>
<template>
  <div class="setting">
    <span
      >账户名：{{ userData?.username ?? '未命名' }}<el-icon><Edit /></el-icon
    ></span>
  </div>
</template>
