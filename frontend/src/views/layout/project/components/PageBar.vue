<script setup lang="ts">
// prop参数
const page = defineModel<number>('page', { default: 1 })
const pageSize = defineModel<number>('pageSize', { default: 5 })
const props = withDefaults(
  defineProps<{
    total: number
    pageSizes: number[]
  }>(),
  {
    total: 0,
    pageSizes: () => [5, 10, 20, 50],
  },
)

const emit = defineEmits<{
  'update:page': [value: number]
  'update:pageSize': [value: number]
  change: []
}>()

// 派生
const handleSizeChange = () => {
  page.value = 1
  emit('change')
}
</script>

<template>
  <div class="page-bar">
    <el-pagination
      background
      layout="sizes, prev, pager, next"
      :page-sizes="pageSizes"
      :total="total"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      @current-change="emit('change')"
      @size-change="handleSizeChange"
    />

    <div class="page-info">
      <span class="page-text">第 {{ page }} / {{ Math.ceil(total / pageSize) }} 页 </span>
      <span class="total-text">共 {{ total }} 条</span>
    </div>
  </div>
</template>
<style scoped>
.page-bar {
  color: #606266;
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  .page-info {
    font-size: 14px;
    display: flex;
    align-items: center;
    .page-text {
      margin-right: 20px;
    }
  }
}
</style>
