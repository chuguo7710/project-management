<template>
  <div class="sidebar">
    <h2 class="logo">御驾亲征</h2>
    <el-menu
      router
      active-text-color="#ffd04b"
      background-color="#545c64"
      default-active="1"
      class="sidebarMenu"
      text-color="#fff"
    >
      <el-menu-item index="1" :route="{ name: 'dashboard' }">
        <el-icon><House /></el-icon>
        <span>仪表盘</span>
      </el-menu-item>
      <el-sub-menu index="2">
        <template #title>
          <el-icon><EditPen /></el-icon>
          <span>项目管理</span>
        </template>
        <el-menu-item index="2-1" :route="{ name: 'projectList' }">
          <span>项目列表</span>
        </el-menu-item>
        <el-menu-item index="2-2" :route="{ name: 'projectManage' }">
          <span>我的项目</span>
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item index="3" :route="{ name: 'task' }">
        <el-icon><Document /></el-icon>
        <span>任务管理</span>
      </el-menu-item>
      <!-- <el-menu-item index="1-1">item one</el-menu-item>
            <el-menu-item index="1-2">item two</el-menu-item>
            <el-menu-item index="1-3">item three</el-menu-item>
        </el-sub-menu> -->
      <el-menu-item index="4" :route="{ name: 'user' }">
        <el-icon><User /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
      <el-menu-item index="5" :route="{ name: 'setting' }">
        <el-icon><Setting /></el-icon>
        <span>账户设置</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<style scoped>
.sidebar {
  width: 200px;
  height: 100vh;
  align-items: center;
}
</style>
