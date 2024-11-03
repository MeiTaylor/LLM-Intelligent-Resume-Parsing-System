import router from './router/index'
import pinia from './stores/index'
import { usePermissions } from './stores/permission'
import { useUser } from './stores/user'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress' // 进度条
import 'nprogress/nprogress.css' // 进度条样式
import { getToken } from './utils/auth'
import getPageTitle from './utils/get-page-title' // 这个文件是

NProgress.configure({ showSpinner: false }) // NProgress配置

const whiteList = ['/login', '/register', '/forget-password'] // 无需重定向白名单, 页面不需权限可直接登录

// 导航守卫, 拦截并进行权限认证
router.beforeResolve(async(to, from, next) => {
  // 进度条开始
  NProgress.start()
  
  // 获取store实例
  const user = useUser(pinia)
  const permissions = usePermissions(pinia)
  
  // 设置页面标题
  document.title = getPageTitle(to.meta.title)

  // 判断用户是否已登录
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // 若已登录则重定向至首页
      next({ path: '/' })
      NProgress.done()
    } else {
      // 判断用户是否已获取用户信息
      const hasRoles = user.roles && user.roles.length > 0 
      console.log(hasRoles)

      if (hasRoles) {
        console.log(user.roles)
        // console.log(typeof user.roles)
        const accessRoutes = permissions.generateRoutes(user.roles)
          // 动态添加可访问路由表
        router.addRoute(accessRoutes)
        console.log(permissions.routes)  // 为什么是空的啊???
        next()
      } else 
      {
        try {
          // 又忘了异步了,笑拉了
          // const { user_type } = await user.getInfo()
          // console.log("这里是" + user_type)

          // let roles = []
          // if (user_type) {
          //   roles = ['admin']
          // } else {
          //   roles = ['normal']
          // }

          // roles = [userStore.roles]
          // 基于角色生成可访问的路由表
          const accessRoutes = permissions.generateRoutes(roles)
          // 动态添加可访问路由表
          router.addRoute(accessRoutes)

          next({ ...to, replace: true })
        } 
        
        catch (error) {
          // 移除token并跳转至登录界面
          await user.resetToken()
          ElMessage.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    // 无token
    console.log("no token")
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      // 全部重定向到登录页
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // 进度条结束
  NProgress.done()
})
