import { createRouter, createWebHashHistory } from 'vue-router'
//通常web应用的首页在这里直接引入
import HelloView from '../views/HelloView.vue'

const routes = [
  {
    path: '/',
    name: 'hello',
    component: HelloView
  },
  {
    path: '/home',
    name: 'home',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../layout'),
  },
  {
    path:'/login',
    name:'login',
    //其他子页面推荐使用异步加载方式，及未显示就不加载
    component: () => import('../views/LoginView.vue'),
    children:[
      
    ]
  },
  {
    path:'/group',
    name:'group',
    //其他子页面推荐使用异步加载方式，及未显示就不加载
    component: () => import('../views/Group.vue'),
    children:[
      
    ]
  },
  {
    path:'/userinfo',
    name:'userinfo',
    //其他子页面推荐使用异步加载方式，及未显示就不加载
    component: () => import('../views/UserInfo.vue'),
    children:[
      
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
