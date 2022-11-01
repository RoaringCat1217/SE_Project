import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "about" */ '../views/login')
  },
  {
    path: '/',
    name: 'Home',
    component: () => import(/* webpackChunkName: "about" */ '../components/HelloWorld.vue')
  },
  {
    path: '/hotlist',
    name: 'HotList',
    component: () => import(/* webpackChunkName: "about" */ '../components/HotList.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
