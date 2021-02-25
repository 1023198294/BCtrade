import Vue from "vue"
import VueRouter from "vue-router"

// 引入组件

import login from '../page/login.vue'
import home from '../page/home.vue'
import notFound from '../page/404.vue'
import sub11 from '../page/menu1/sub1.vue'
import sub12 from '../page/menu1/sub2.vue'
import sub21 from '../page/menu2/sub1.vue'
import news from '../page/info/news'
import register from '../page/register'
// 要告诉 vue 使用 vueRouter
Vue.use(VueRouter);

let routes = [
  {
    path: "/",
    redirect: '/login',
    hidden: true
  },
  {
    path: '/login',
    component: login,
    name: '',
    hidden: true
  },
  {
    path: '/info',
    component: home,
    name: '消息列表',
    hidden: true,
    children: [{
      path: 'news',
      component: news,
      name: '新消息'
    }]
  },
  {
    path: '/register',
    component: register,
    hidden: true
  },
  {
    path: '/404',
    component: notFound,
    name: '',
    hidden: true
  },
  {
    path: '/menu1',
    component: home,
    name: '我的钱包',
    iconCls: 'el-icon-message', //图标样式class
    children: [
      {
        path: 'sub1',
        component: sub11,
        name: '充值'
      },
      {
        path: 'sub2',
        component: sub12,
        name: '提现'
      }
    ]
  },
  {
    path: '/menu2',
    component: home,
    name: '我的数据',
    iconCls: 'el-icon-message',
    children: [
      {
        path: 'sub1',
        component: sub21,
        name: '数据管理'
      }
    ]
  },
  {
    path: '/menu3',
    component: home,
    name: '数据广场',
    iconCls: 'el-icon-message'
  }
];
var router = new VueRouter({
    routes
})
export default router;
