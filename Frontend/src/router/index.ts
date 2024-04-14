import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '../store/user'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/home',
    name: 'home',
    component: () => import('../views/HomeView.vue'),
    alias: '/',
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/upload',
    name: 'upload',
    component: () => import('../views/UploadView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/modifyPermission/:fileId',
    name: 'modifyPermission',
    component: () => import('../views/ModifyPermissionView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/modifyFile/:fileId',
    name: 'modifyFile',
    component: () => import('../views/ModifyFileView.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!userStore.loginStatus) {
      return '/login'
    }
  }
})

export default router
