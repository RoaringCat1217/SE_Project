import { createRouter, createWebHashHistory } from 'vue-router'
//通常web应用的首页在这里直接引入
import HelloView from '../views/HelloView.vue'
import HomeView from '../views/HomeView.vue'
import Layout from '../layout'

const routes = [
  {
    path: '/hello',
    name: 'hello',
    component: HelloView
  },
  {
    path: '/',
    name: 'home',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: Layout,
    redirect: '/home',
    children: [
      {
        path:'home',
        name:'homepage',
        component:HomeView
      },
      {
        path:'group',
        name:'group',
        component: () => import('../views/GroupView.vue'),
      },
      {
        path:'group/:groupName',
        name: 'PostList',
        component: () => import('../views/PostList.vue')
      },
      {
        path:'hot',
        name: 'HotList',
        component: () => import('../views/HotListView.vue')
      },
      {
        path:'post/:postId',
        name: 'PostDetails',
        component: () => import('../views/PostDetails.vue')
      },
      {
        path:'star',
        name:'Star',
        component: () => import('../views/StarLists.vue')
      },
      {
        path:'user',
        name:'UserInfo',
        component: () => import('../views/UserInfo.vue')
      }
    ]
  },
  {
    path:'/login',
    name:'login',
    //其他子页面推荐使用异步加载方式，及未显示就不加载
    component: () => import('../views/LoginView.vue'),
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
