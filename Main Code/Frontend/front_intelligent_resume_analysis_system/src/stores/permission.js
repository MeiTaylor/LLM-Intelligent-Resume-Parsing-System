import { asyncRoutes, constantRoutes } from '../router/index'
import { defineStore } from 'pinia'


function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}


export function filterAsyncRoutes(routes, roles) {
  const res = []

  // routes.forEach(route => {
  //   const tmp = { ...route }
  //   if (hasPermission(roles, tmp)) {
  //     // 如果有权限, 将可访问route放进res
  //     if (tmp.children) {
  //       // 如果有路径子数组, 递归
  //       tmp.children = filterAsyncRoutes(tmp.children, roles)
  //     }
  //     res.push(tmp)
  //   }
  // })
  return res
}

export const usePermissions = defineStore('permission', {
  state: () => ({
    routes: [],
    addRoutes: []
  }),
  getters: {},
  actions: {
    SET_ROUTES(routes) {
      this.routes = routes
    },
    generateRoutes(roles) {
      return new Promise(resolve => {
        let accessedRoutes
        if (roles[0] === 'admin') {
          accessedRoutes = constantRoutes
        } else {
          accessedRoutes = asyncRoutes
        }
        // accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
        this.SET_ROUTES(accessedRoutes)
        resolve(accessedRoutes)
      })
    }
  },
  persist: true
})

