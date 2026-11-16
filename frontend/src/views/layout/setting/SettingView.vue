<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
const store = useAuthStore()
const { userData } = storeToRefs(store)
</script>
<template>
  <div class="setting">
    <span
      >账户名：{{ userData?.username ?? '未命名' }}<el-icon><Edit /></el-icon
    ></span>
  </div>
</template>
