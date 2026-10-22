import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: { name: 'login' },
    },
    {
      path: '/auth',
      name: 'auth',
      redirect: { name: 'login' },
      component: () => import('@/views/auth/AuthView.vue'),
      children: [
        {
          path: 'login',
          name: 'login',
          component: () => import('@/views/auth/login/LoginView.vue'),
        },
        {
          path: 'register',
          name: 'register',
          component: () => import('@/views/auth/register/RegisterView.vue'),
        },
      ],
    },
    {
      path: '/layout',
      name: 'layout',
      redirect: { name: 'dashboard' },
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
  const userStore = useUserStore()
  console.log('路由守卫触发,token:', userStore.token, 'to:', to.name, 'from:', from.name)
  // 有token时读取token并验证
  if (userStore.token) {
    try {
      // 首次获取用户数据
      if (userStore.isDataFetched === false) {
        userStore.fetchUserAllData()
        userStore.isDataFetched = true
      }
      if (to.name === 'login') {
        return { name: 'layout' } // 如果已经登录仍访问登录页，则自动跳转到首页
      }
      return true // 如果登录后访问其他页面，则放行
    } catch {
      ElMessage.error('未登录，请登录')
      return { name: 'login' } // 如果验证失败，则跳转到登录页
    }
  }
  // 如果token为空，则跳转到登录页
  if (to.name === 'login' || to.name === 'register') {
    return true // 如果是登录页，则放行
  }
  return { name: 'login' } // 如果不是登录页，则跳转到登录页
})

export default router
