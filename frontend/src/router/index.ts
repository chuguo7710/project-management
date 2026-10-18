import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/LoginView.vue'),
    },
    {
      path: '/layout',
      name: 'layout',
      redirect: 'layout/dashboard',
      component: () => import('@/views/layout/LayoutView.vue'),
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/layout/dashboard/DashboardView.vue'),
        },
        {
          path: 'project',
          name: 'project',
          children: [
            {
              path: 'list',
              name: 'projectList',
              component: () => import('@/views/layout/project/list/ProjectListView.vue'),
            },
            {
              path: 'manage',
              name: 'projectManage',
              component: () => import('@/views/layout/project/manage/ProjectManageView.vue'),
            },
          ],
        },
        {
          path: 'user',
          name: 'user',
          component: () => import('@/views/layout/user/UserView.vue'),
        },
        {
          path: 'setting',
          name: 'setting',
          component: () => import('@/views/layout/setting/SettingView.vue'),
        },
      ],
    },
  ],
})

// 守卫
router.beforeEach((to, from) => {
  const store = useUserStore()
  console.log('路由守卫触发,token:', store.token, 'to:', to.name, 'from:', from.name)
  // 读取token并验证
  if (store.token) {
    try {
      console.log('token验证中')
      store.tokenValid()
      // 首次获取用户数据
      if (store.isDataFetched === false) {
        console.log('用户数据获取中')
        store.fetchUserAllData()
        store.isDataFetched = true
        console.log('用户数据获取成功')
      }
      if (to.name === 'login') {
        return { name: 'dashboard' } // 如果已经登录仍访问登录页，则自动跳转到首页
      }
      return true // 如果登录后访问其他页面，则放行
    } catch {
      console.log('token验证失败')
      ElMessage.error('未登录，请登录')
      return { name: 'login' } // 如果验证失败，则跳转到登录页
    }
  }
  // 如果token为空，则跳转到登录页
  console.log('未登录')

  if (to.name === 'login') {
    return true // 如果是登录页，则放行
  }
  console.log('自动跳转到登录页')
  return { name: 'login' } // 如果不是登录页，则跳转到登录页
})

export default router
