import { defineStore } from "pinia";


// 登录获取token
import { getToken, setToken, removeToken } from '@/utils/auth'
import { login, logout, getInfo } from '@/api/user'

const getDefaultState = () => {
    return {
        token: getToken(),
        name: '',
        userInfo: '',
        roles: [],
        userId: '',
        companyId: '',
    }
}

export const useUser = defineStore('user', {
    state: () => {
        return getDefaultState()
    },
    getters: {

    },
    actions: {
        RESET_STATE() {
            this.$reset()
            
        },
        SET_TOKEN(token) {
            this.token = token
        },
        SET_NAME(name) {
            this.name = name
        },
        SET_USERINFO(userInfo) {
            this.userInfo = userInfo
        },
        SET_ROLES(roles) {
            this.roles = [roles]
        },
        set_userid(userId){
            this.userId = userId
        },
        set_companyId(companyId){
            this.companyId = companyId
        },
        login(userInfo) {
            const { username, password } = userInfo
            return new Promise((resolve, reject) => {
                // 去除两端空格,然后扔给API
                login({ username: username.trim(), password: password })
                .then(response => {
                    if(response.code === 20000){
                    console.log(response)
                    const token = response.data
                    const userId = response.userId
                    const companyId = response.companyId
                    const roles = token

                    this.SET_TOKEN(token)
                    this.set_userid(userId)
                    this.SET_ROLES(roles)
                    this.set_companyId(companyId)

                    // 扔进浏览器缓存
                    setToken(token)
                    resolve()
                    }else{
                        console.log(response)
                        reject()
                    }
                })
                .catch(error => {
                    console.log("这里是stores/user.js的错误处理部分")
                    console.log(error)
                    reject(error)
                })
            })
        },

        // get user info
        getInfo() {
            return new Promise((resolve, reject) => {

                getInfo().then(response => {
                    if (!response) {
                        console.log("no response")
                        return reject('验证失败，请重新登录')
                    }
                    const { user_type, username, user_info } = response.data
                    this.SET_NAME(username)
                    this.SET_USERINFO(user_info)

                    let roles = []
                    if (user_type) {
                        roles = ['admin']
                    } else {
                        roles = ['normal']
                    }

                    this.SET_ROLES(roles)
                    resolve(response)
                }).catch(error => {
                    console.log("failed getting info")
                    reject('验证失败，请重新登录')
                })
            })
        },

        // user logout
        logout() {
            removeToken() // must remove token first
            resetRouter()
            // commit('RESET_STATE')
            this.RESET_STATE()
        },

        // remove token
        resetToken() {
            return new Promise(resolve => {
                removeToken() // must remove  token  first
                // commit('RESET_STATE')
                this.RESET_STATE()
                resolve()
            })
        }
    },
    persist: true
}
)
