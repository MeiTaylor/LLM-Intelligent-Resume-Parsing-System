import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login/index.vue'
import Homepage from '../views/Homepage/index.vue'
import Register from '../views/Register/index.vue'
import ForgetPassword from '../views/ForgetPassword/index.vue'
import Layout from '../layout/index.vue'

const initRoute = {
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [{
    path: '/login',
    name: 'login',
    component: Login,
    hidden: true
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
    hidden: true
  },
  {
    path: '/forget-password',
    name: 'forget-password',
    component: ForgetPassword,
    hidden: true
  },
  {
    path: '/',
    redirect: '/homepage',
    component: Layout,
    children: [
      {
        path: '/homepage',
        name: 'Homepage',
        component: Homepage,
        meta: { title: '首页', icon: 'el-icon-data-board' }
      }
    ]
  },
  ]
}

export const constantRoutes = {
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   redirect: '/login'
    // },
    {
      path: '/',
      redirect: '/homepage',
      name: 'home',
      component: Layout,
      children: [
        {
          path: '/homepage',
          name: 'Homepage',
          component: Homepage,
          meta: { title: '首页', icon: 'el-icon-data-board' }
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      hidden: true
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
      hidden: true
    },
    {
      path: '/forget-password',
      name: 'forget-password',
      component: ForgetPassword,
      hidden: true
    },
    /*
      需要经由权限验证而展现的页面
    */
    {
      path: '/all-resume',
      name: 'resume-parser',
      component: Layout,
      meta: { title: '简历分析', icon: 'document' },
      children: [{
        path: '/talent-profile/:id([a-z0-9-]+)',
        component: () => import('../views/ResumeParser/detailedResume.vue'),
        name: 'parser-details',
        hidden: true,
        meta: { title: '人才画像', icon: 'document' }
      },
      {
        path: '/resume-list',
        component: () => import('../views/ResumeParser/resumeList.vue'),
        name: 'resume-list',
        meta: { title: '简历信息', icon: 'document' }
      },
      {
        path: '/uploadResume',
        component: () => import('../views/ResumeParser/uploadResume.vue'),
        name: 'upload-file',
        meta: { title: '上传简历', icon: 'document' }
      },
      {
        path: '/previewAndAlter/:id([a-z0-9-]+)',
        component: () => import('../views/ResumeParser/previewAndAlter.vue'),
        name: 'preview-and-alter',
        hidden: true,
        meta: { title: '预览和修改', icon: 'document' }
      },
      {
        path: '/statisticResume',
        component: () => import('../views/ResumeParser/statisticView.vue'),
        name: 'statistic',
        meta: { title: '统计可视化', icon: 'document' }
      }
      ]
    },
    {
      path: '/job-info',
      name: 'job-info',
      component: Layout,
      meta: { title: '岗位分析', icon: 'document' },
      children: [{
        path: '/job-info',
        name: 'job--info-details',
        // component: () => import('../views/Jobinfo/jobDetails.vue'),
        // component: () => import('../views/Jobinfo/newJobDetails.vue'),
        component: () => import('../views/Jobinfo/jobDetails.vue'),
        meta: { title: '人岗匹配', icon: 'document' }
      },
      {
        path: '/job-upload',
        name: 'job-upload',
        component: () => import('../views/Jobinfo/uploadJob.vue'),
        meta: { title: '上传岗位', icon: 'document' }
      },
      {
        path: '/job-matching/:jid([a-z0-9-]+)',
        name: 'job-matching',
        component: () => import('../views/Jobinfo/jobMatching.vue'),
        hidden: true,
        meta: { title: '匹配结果', icon: 'document' }
      }
      ]
    },
    {
      path: '/fetch-account',
      name: 'fetch-account',
      component: Layout,
      children: [{
        path: '/fetch-account',
        name: 'fetch-account-details',
        component: () => import('../views/FetchAccount/fetchAccount.vue'),
        meta: { title: '管理账户', icon: 'document' }
      }]
    },
    {
      path: '/manage-email',
      name: 'manage-email',
      component: Layout,
      children: [{
        path: '/manage-email',
        name: 'manage-email-details',
        component: () => import('../views/email/email.vue'),
        meta: { title: '邮箱管理', icon: 'document' }
      }]
    }

    // 错误路由重定向
    // { path: '*', redirect: '/404', hidden: true }
  ]
}

export const asyncRoutes = {
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   redirect: '/login'
    // },
    {
      path: '/',
      redirect: '/homepage',
      name: 'name',
      component: Layout,
      children: [
        {
          path: '/homepage',
          name: 'Homepage',
          component: Homepage,
          meta: { title: '首页', icon: 'el-icon-data-board' }
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      hidden: true
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
      hidden: true
    },
    {
      path: '/forget-password',
      name: 'forget-password',
      component: ForgetPassword,
      hidden: true
    },
    /*
      需要经由权限验证而展现的页面
    */
    {
      path: '/all-resume',
      name: 'resume-parser',
      component: Layout,
      meta: { title: '简历分析', icon: 'document' },
      children: [{
        path: '/talent-profile/:id([a-z0-9-]+)',
        component: () => import('../views/ResumeParser/detailedResume.vue'),
        name: 'parser-details',
        hidden: true,
        meta: { title: '人才画像', icon: 'document' }
      },
      {
        path: '/resume-list',
        component: () => import('../views/ResumeParser/resumeList.vue'),
        name: 'resume-list',
        meta: { title: '简历信息', icon: 'document' }
      },
      {
        path: '/uploadResume',
        component: () => import('../views/ResumeParser/uploadResume.vue'),
        name: 'upload-file',
        meta: { title: '上传简历', icon: 'document' }
      },
      {
        path: '/previewAndAlter/:id([a-z0-9-]+)',
        component: () => import('../views/ResumeParser/previewAndAlter.vue'),
        name: 'preview-and-alter',
        hidden: true,
        meta: { title: '预览和修改', icon: 'document' }
      },
      {
        path: '/statisticResume',
        component: () => import('../views/ResumeParser/statisticView.vue'),
        name: 'statistic',
        meta: { title: '统计可视化', icon: 'document' }
      }
      ]
    },
    {
      path: '/job-info',
      name: 'job-info',
      component: Layout,
      meta: { title: '岗位分析', icon: 'document' },
      children: [{
        path: '/job-info',
        name: 'job--info-details',
        component: () => import('../views/Jobinfo/jobDetails.vue'),
        meta: { title: '人岗匹配', icon: 'document' }
      },
      {
        path: '/job-match',
        name: 'job-match',
        component: () => import('../views/Jobinfo/uploadJob.vue'),
        meta: { title: '上传岗位', icon: 'document' }
      }
      ]
    },

    // 错误路由重定向
    //  { path: '*', redirect: '/404', hidden: true }
  ]
}

const router = createRouter(constantRoutes)
// const router = createRouter(constantRoutes)

export default router
