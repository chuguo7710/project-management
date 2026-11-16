<script lang="ts" setup>
/**
 * 用户管理 · 查询栏（只有用户名一个条件）
 *
 * 与 script / store 的连接点，由你自己补：
 * ① 查询词
 *    const username = defineModel<string>('username', { default: '' })
 *    —— 由 UserView 写成 <ActionBar v-model:username="query.username" ... />
 * ② 事件
 *    const emit = defineEmits<{ search: []; reset: [] }>()
 *    —— search 交给 UserView 调 store 的 load(1)；reset 先清 query 再 load(1)
 * ③ 清空按钮的可见性（可选）
 *    const hasKeyword = computed(() => username.value.trim() !== '')
 *    —— 模板里 v-if="hasKeyword" 就能让空输入时不显示「清空」
 */

// props
const username = defineModel<string>('username  ', { default: '' })
// emit
const emit = defineEmits<{
  search: []
  reset: []
}>()
</script>

<template>
  <div class="action-bar">
    <!-- 查询栏 -->
    <div class="search-bar">
      <!-- 查询参数：用户名 -->
      <el-input
        class="responsive-input"
        v-model="username"
        clearable
        placeholder="请输入账户名"
        @keyup.enter="emit('search')"
      >
        <template #prefix>
          <el-icon class="el-input-icon"><Search /></el-icon>
        </template>
      </el-input>

      <!-- 按钮：查询 -->
      <el-tooltip
        class="search-tooltip"
        effect="dark"
        placement="top-start"
        content="查询"
        :show-after="500"
      >
        <el-button class="search-btn" type="primary" @click="emit('search')">
          <el-icon><Search /></el-icon>
        </el-button>
      </el-tooltip>

      <!-- 预留位置：以后加角色 / 状态下拉时放这里 -->
    </div>
    <!-- /查询栏 -->

    <!-- 工具栏 -->
    <div class="tools-bar">
      <el-button type="primary" @click="emit('reset')">清空</el-button>
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
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;
}
.responsive-input {
  width: 320px;
}
</style>
