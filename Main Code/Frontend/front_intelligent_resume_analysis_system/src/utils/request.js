import axios from 'axios'
// import { MessageBox, Message } from 'element-ui'
import { ElMessageBox, ElMessage } from 'element-plus'
// import store from '@/stores'
import pinia from '../stores'
import { useUser } from '../stores/user'
import { getToken } from '@/utils/auth'

// 引入使用的store

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_URL,
})

// 跨域请求
// 表示跨域请求时是否需要使用凭证
// axios.defaults.withCredentials = true
// 允许跨域
// axios.defaults.headers.post['Access-Control-Allow-Origin-Type'] = '*'

// 请求拦截器
service.interceptors.request.use(
  config => {
    console.log(config)
    const userStore = useUser(pinia)
    // 请求发送之前拦截加上token, 这里只有已注册的用户登录才有token
    if(userStore.token){
      config.headers['Authorization'] = getToken()
      console.log(config.headers['Authorization'])
    }
    return config
  },
  error => {
    console.log(error) 
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const userStore = useUser(pinia)
    // 标识
    // console.log("这是request.js响应拦截器")
    // console.log(response)

    // 获取返回数据
    const res = response.data
    if (response.status !== 200) {
      // 下载文件
      // if (response.headers['content-type'] === 'application/octet-stream') {
      //   return response
      // }
      console.log(JSON.stringify(res))
      // 二代element-ui的API也不知道还能不能用, 暂且先放着
      ElMessage({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 40001 || res.code === 40004 || res.code === 40003) {
        // to re-login
        ElMessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 改成pinia写法了
          userStore.resetToken().then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(res.msg || 'Error')
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    ElMessage({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
