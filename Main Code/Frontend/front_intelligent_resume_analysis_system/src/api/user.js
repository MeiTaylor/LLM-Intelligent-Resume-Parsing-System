import request from "../utils/request"

export function login(data) {
    return request({
      url: '/Login/Login',
      method: 'post',
      data
    })
  }

  export function register(data) {
    return request({
      url: '/register',
      method: 'post',
      data
    })
  }
  
  export function getInfo(data) {
    return request({
      url: '/Login/GetInfo',
      method: 'post',
      data
    })
  }
  
  export function resetPassword(data) {
    return request({
      url: '/reset_password',
      method: 'post',
      data
    })
  }
  
  export function logout() {
    return request({
      url: '/logout',
      method: 'post',
    })
  }
  
  export function updateInfo(data) {
    return request({
      url: '/user/update_info',
      method: 'post',
      data
    })
  }
  
  export function getUserDetail(id) {
    return request({
      url: '/user/detail',
      method: 'get',
      params: { id }
    })
  }
  