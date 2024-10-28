<template>

  <div class="all-background">
    <div class="login-box">
      <h2>登录</h2>
      <form>
        <div class="user-box">
          <input type="text" v-model="loginForm.username" name="" required="">
          <label>账号</label>
        </div>
        <div class="user-box">
          <input type="password" name="" v-model="loginForm.password" required="">
          <label>密码</label>
        </div>
        <div style="display: flex;justify-content: space-between;">
          <a @click="handleLogin">
            登录
          </a>
          <a @click="gotoRegister">
            注册
          </a>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import { validLoginUsername, validLoginPassword } from '@/utils/validate'
  import { mapStores } from 'pinia'
  import { useUser } from '../../stores/user'
  import request from "../../utils/request"
  // 所有的validate方法都放在utils的validate.js文件里

  export default {
    name: 'Login',
    setup() {

    },
    computed: {
      ...mapStores(useUser)
    },
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!validLoginUsername(value)) {
          callback(new Error('请输入有效的用户名'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (!validLoginPassword(value)) {
          callback(new Error('密码不能少于6位'))
        } else {
          callback()
        }
      }
      return {
        // 这里是el-form的api
        loginForm: {
          username: '',
          password: ''
        },
        loginRules: {
          username: [{ required: true, trigger: 'blur', validator: validateUsername }],
          password: [{ required: true, trigger: 'blur', validator: validatePassword }]
        },
        loginLoading: false,
        passwordType: 'password',
        redirect: undefined
      }
    },
    watch: {
      $route: {
        handler: function (route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
        // 初次绑定就要执行监听函数(类似watcheffect)
      }
    },
    mounted() {
    },
    methods: {
      test() {
        return request({
          url: '/api/Home/statistics',
          method: 'post',
          data: { id: 0 }
        })
      }
      ,
      handleLogin() {
        // //强制跳转，后面得删除掉
        // this.$router.push({ path: this.redirect || '/homepage' })
        console.log(`output->开始跳转了`, this.loginForm)
        //判断username和password是否符合规范
        if (this.loginForm.username === '' || this.loginForm.password === '') {
          this.$message({
            message: '请输入用户名和密码',
            type: 'error'
          })
          return false
        }

        if (1) {
          this.loginLoading = true
          console.log("向后端发送登录请求")
          this.userStore.login(this.loginForm).then(() => {
            console.log(this.redirect)
            this.$router.push({ path: this.redirect || '/homepage' })
            // 
            this.loginLoading = false
            this.$message({
              message: '登录成功',
              type: 'success'
            })
          }).catch(() => {
            this.loginLoading = false
          })
          // this.$store.dispatch('user/login', this.loginForm).then(() => {
          //   this.$router.push({ path: this.redirect || '/' })
          //   this.loginLoading = false
          //   this.$message({
          //     message: '登录成功',
          //     type: 'success'
          //   })
          // }).catch(() => {
          //   this.loginLoading = false
          // })
        } else {
          console.log('error submit!!')
          // //强制跳转，后面得删除掉
          // this.$router.push({ path: this.redirect || '/homepage' })

          return false
        }
      },
      gotoRegister() {
        this.$router.push('/register')
      },
      gotoResetPassword() {
        this.$router.push('/forget-password')
      },
      login() {
        console.log(`output->this.validateUsername`, this.loginForm)
      }
    }
  }
</script>

<style lang="less" scoped>
  html {
    height: 100%;
  }

  .all-background {
    background-color: #141e30;
    height: 100%;
  }

  body {
    margin: 0;
    padding: 0;
    font-family: sans-serif;
    height: 1000px;
    background-color: #1e3047;
    background: linear-gradient(#141e30, #243b55);
  }

  .login-box {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 400px;
    padding: 40px;
    transform: translate(-50%, -50%);
    background: rgba(0, 0, 0, .5);
    box-sizing: border-box;
    box-shadow: 0 15px 25px rgba(0, 0, 0, .6);
    border-radius: 10px;
  }

  .login-box h2 {
    margin: 0 0 30px;
    padding: 0;
    color: #fff;
    text-align: center;
  }

  .login-box .user-box {
    position: relative;
  }

  .login-box .user-box input {
    width: 100%;
    padding: 10px 0;
    font-size: 16px;
    color: #fff;
    margin-bottom: 30px;
    border: none;
    border-bottom: 1px solid #fff;
    outline: none;
    background: transparent;
  }

  .login-box .user-box label {
    position: absolute;
    top: 0;
    left: 0;
    padding: 10px 0;
    font-size: 16px;
    color: #fff;
    pointer-events: none;
    transition: .5s;
  }

  .login-box .user-box input:focus~label,
  .login-box .user-box input:valid~label {
    top: -20px;
    left: 0;
    color: #03e9f4;
    font-size: 12px;
  }

  .login-box form a {
    position: relative;
    display: inline-block;
    padding: 10px 20px;
    color: #03e9f4;
    font-size: 16px;
    text-decoration: none;
    text-transform: uppercase;
    overflow: hidden;
    transition: .5s;
    margin-top: 40px;
    letter-spacing: 4px
  }

  .login-box a:hover {
    background: #03e9f4;
    color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px #03e9f4,
      0 0 25px #03e9f4,
      0 0 50px #03e9f4,
      0 0 100px #03e9f4;
  }

  .login-box a span {
    position: absolute;
    display: block;
  }

  .login-box a span:nth-child(1) {
    top: 0;
    left: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(90deg, transparent, #03e9f4);
    animation: btn-anim1 1s linear infinite;
  }

  @keyframes btn-anim1 {
    0% {
      left: -100%;
    }

    50%,
    100% {
      left: 100%;
    }
  }

  .login-box a span:nth-child(2) {
    top: -100%;
    right: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(180deg, transparent, #03e9f4);
    animation: btn-anim2 1s linear infinite;
    animation-delay: .25s
  }

  @keyframes btn-anim2 {
    0% {
      top: -100%;
    }

    50%,
    100% {
      top: 100%;
    }
  }

  .login-box a span:nth-child(3) {
    bottom: 0;
    right: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(270deg, transparent, #03e9f4);
    animation: btn-anim3 1s linear infinite;
    animation-delay: .5s
  }

  @keyframes btn-anim3 {
    0% {
      right: -100%;
    }

    50%,
    100% {
      right: 100%;
    }
  }

  .login-box a span:nth-child(4) {
    bottom: -100%;
    left: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(360deg, transparent, #03e9f4);
    animation: btn-anim4 1s linear infinite;
    animation-delay: .75s
  }

  @keyframes btn-anim4 {
    0% {
      bottom: -100%;
    }

    50%,
    100% {
      bottom: 100%;
    }
  }
</style>