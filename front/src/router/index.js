import Vue from "vue"
import VueRouter from "vue-router"

// 引入组件

import login from '../page/login.vue'
import home from '../page/home.vue'
import notFound from '../page/404.vue'
import sub11 from '../page/menu1/sub1.vue'
import sub12 from '../page/menu1/sub2.vue'
import sub01 from '../page/menu0/sub1.vue'
import sub02 from '../page/menu0/sub2.vue'
import sub21 from '../page/menu2/sub1.vue'
import sub31 from '../page/menu3/sub1.vue'
import sub41 from '../page/menu4-test/sub1'
import sub51 from '../page/menu5-test/sub1'
import sub52 from '../page/menu5-test/sub2'
import sub53 from '../page/menu5-test/sub3'
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
    name: '钱包首页',
    iconCls: 'el-icon-message', //图标样式class
    role:'any',
    children: [
      {
        path: 'sub1',
        component: sub11,
        name: '欢迎'
      },
    ]
  },
  {
    path: '/menu0',
    component: home,
    name: '我的钱包',
    iconCls: 'el-icon-message', //图标样式class
    role:'user',
    children: [
      {
        path: 'sub1',
        component: sub01,
        name: '充值'
      },
      {
        path: 'sub2',
        component: sub02,
        name: '提现'
      }
    ]
  },
  {
    path: '/menu2',
    component: home,
    name: '我的数据',
    iconCls: 'el-icon-message',
    role:'user',
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
    name: '发现数据',
    iconCls: 'el-icon-message',
    role:'user',
    children: [
      {
        path: 'sub1',
        component:sub31,
        name:'数据广场'
      }
    ]
  },
  {
    path: '/menu4-test',
    component: home,
    name: '测试页面一',
    iconCls: 'el-icon-message',
    role:'user',
    children: [
      {
        path: 'sub1',
        component:sub41,
        name:'user'
      }
    ]
  },
  {
    path: '/menu5-test',
    component: home,
    name: '管理中心',
    iconCls: 'el-icon-message',
    role:'admin',
    children: [
      {
        path: 'sub1',
        component:sub51,
        name:'举报中心'
      },
      {
        path: 'sub2',
        component:sub52,
        name:'大额预警'
      },
      {
        path: 'sub3',
        component:sub53,
        name:'交易查看'
      },
    ]
  }
];
var router = new VueRouter({
    routes
})
export default router;
