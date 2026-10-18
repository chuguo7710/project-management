<script setup lang="ts">
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'
</script>

<template>
  <div class="layout">
    <el-container>
      <el-header height="60px"><Header /></el-header>
      <el-container>
        <el-aside width="200px"><Sidebar /></el-aside>
        <el-main><router-view /></el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
.el-header {
  padding: 0px;
}
</style>
