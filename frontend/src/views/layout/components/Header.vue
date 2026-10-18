<script lang="ts" setup>
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/user'
import { useHeader } from '../composables/useHeader'
const store = useUserStore()
const { userProfileData } = storeToRefs(store)
const { handleCommand } = useHeader()
</script>
<template>
  <div class="header">
    <div class="logo"></div>
    <!-- 用户基本信息 -->
    <div class="userDisplay">
      <el-dropdown class="dropdown" placement="bottom" trigger="click" @command="handleCommand">
        <div class="link">
          <el-avatar :src="userProfileData?.avatar" icon="UserFilled" />
          <span class="nicknameSlot">{{ userProfileData?.nickname ?? '未命名' }}</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="1"
              ><el-icon><Edit /></el-icon>编辑信息</el-dropdown-item
            >
            <el-dropdown-item command="2"
              ><el-icon><SwitchButton /></el-icon>退出登录</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>
<style scoped>
.icon {
  size: default;
  color: #74aaf5;
}
.nicknameSlot {
  padding: 0 10px;
}
.link {
  display: flex;
  align-items: center;
}
.header {
  background-color: #98b7ff;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dropdown {
  margin-right: 20px;
}
</style>
