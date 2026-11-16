<script lang="ts" setup>
/**
 * 用户管理 · 列表
 *
 * 与 script / store 的连接点，由你自己补：
 *
 * ① 数据源（建议由父级 UserView 从 store 传进来，列表本身不碰 store）
 *    defineProps<{ userList: UserVO[]; isLoading: boolean }>()
 *    —— 只有行内的「是否可操作」是后端下发（canChangeStatus / canChangeRole），
 *       前端不要自己拿 role 比大小
 *
 * ② 分页
 *    const page = defineModel<number>('page', { default: 1 })
 *    const pageSize = defineModel<number>('pageSize', { default: 5 })
 *    defineProps<{ total: number }>()
 *    const emit = defineEmits<{ change: [] }>()   // 交给 UserView 调 store 的 load()
 *    import PageBar from '../../components/PageBar.vue'
 *
 * ③ 行内动作（都往上抛，落库与刷新由 UserView 决定）
 *    const emit = defineEmits<{
 *      'status-change': [row: UserVO, status: number]
 *      'role-change': [row: UserVO, role: number]
 *    }>()
 *    —— switch 是受控的：只有 emit 之后父级把 row.status 改掉，开关才会停在原位，
 *       所以失败时不需要手工回滚
 *
 * ④ 展示用的映射（本地常量即可）
 *    const ROLE_TEXT: Record<number, { text: string; type: 'danger' | 'warning' | 'info' }> = {
 *      1: { text: '站长', type: 'danger' },
 *      2: { text: '管理员', type: 'warning' },
 *      3: { text: '普通用户', type: 'info' },
 *    }
 *    const roleText = (role: number) => ROLE_TEXT[role]?.text ?? '未知'
 *    const roleTagType = (role: number) => ROLE_TEXT[role]?.type ?? 'info'
 *
 * ⑤ 时间格式化
 *    import dayjs from 'dayjs'
 *    const formatCreateTime = (row: UserVO) => row.createTime ? dayjs(row.createTime).format('YYYY-MM-DD HH:mm') : '-'
 */
import PageBar from '../../components/PageBar.vue'
import dayjs from 'dayjs'
import { onMounted } from 'vue'

import type { User } from '@/types/user'
import { ROLE_TEXT, ROLE } from '@/types/user'
import { useAuthStore } from '@/stores/auth'

// props
const userList = defineModel<User[]>('userList', { default: () => [] })
const page = defineModel<number>('page', { default: 1 })
const pageSize = defineModel<number>('pageSize', { default: 10 })
const props = defineProps<{ total: number; isLoading: boolean }>()
// emit
const emit = defineEmits<{
  change: []
  init: []
  'status-change': [row: User, status: number]
  'role-change': [row: User, role: number]
}>()
// 外部依赖
const { userData } = useAuthStore()
// 派生
const roleText = (role: number) => ROLE_TEXT[role]?.text ?? '未知'
const roleTagType = (role: number) => ROLE_TEXT[role]?.type ?? 'info'
const formatCreateTime = (row: User) =>
  row.createTime ? dayjs(row.createTime).format('YYYY-MM-DD HH:mm') : '-'
const canChangeStatus = (row: User) => {
  return userData?.role ?? 3 < row.role
}
const canChangeRole = () => {
  return userData?.role === ROLE.ROOT
}

onMounted(() => {
  emit('init')
})
</script>

<template>
  <!-- 用户列表 -->
  <div class="user-list">
    <el-divider />

    <el-table
      v-loading="props.isLoading"
      class="user-table"
      :data="userList"
      border
      stripe
      empty-text="暂无用户"
    >
      <el-table-column prop="userId" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" min-width="140" />

      <!-- 身份 -->
      <el-table-column label="身份" width="110">
        <template #default="{ row }">
          <el-tag :type="roleTagType(row.role)" effect="plain">{{ roleText(row.role) }}</el-tag>
        </template>
      </el-table-column>

      <!-- 状态：受控开关，disabled 由后端下发的 canChangeStatus 决定 -->
      <el-table-column label="状态" width="130">
        <template #default="{ row }">
          <el-switch
            :model-value="row.status"
            :active-value="1"
            :inactive-value="0"
            :disabled="canChangeStatus(row)"
            inline-prompt
            active-text="启用"
            inactive-text="禁用"
            @change="(value: number) => emit('status-change', row, value)"
          />
        </template>
      </el-table-column>

      <!-- 注册时间 -->
      <el-table-column label="注册时间" width="180">
        <template #default="{ row }">{{ formatCreateTime(row) }}</template>
      </el-table-column>

      <!-- 操作：只有站长能改身份，同样看后端下发的 canChangeRole -->
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-dropdown
            trigger="click"
            :disabled="canChangeRole()"
            @command="(role: number) => emit('role-change', row, role)"
          >
            <el-button type="primary" plain :disabled="row.canChangeRole === false">
              更改身份
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :command="ROLE.ADMIN">管理员</el-dropdown-item>
                <el-dropdown-item :command="ROLE.USER">普通用户</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!-- /用户列表 -->

  <!-- 分页栏 -->
  <PageBar
    v-show="userList.length > 0"
    v-model:page="page"
    v-model:page-size="pageSize"
    :total="props.total"
    :page-sizes="[5, 10, 20, 50]"
    @change="emit('change')"
  />
</template>

<style scoped>
.user-table {
  width: 100%;
}
</style>
