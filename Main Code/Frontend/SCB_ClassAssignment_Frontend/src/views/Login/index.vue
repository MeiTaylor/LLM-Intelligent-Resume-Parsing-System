<template>
  <div class="login-container">
    <div id="particles-js" style="display: flex;align-items: center;justify-content: center">
      <canvas class="particles-js-canvas-el" width="737" height="950" style="width: 100%; height: 100%;"></canvas>
    </div>

    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
      label-position="left">
      <!-- vue2中,ref被视为在父组件的$ref属性上注册子组件, 但vue3中不再使用$ref -->

      <div class="title-container">
        <h3 class="title">智能简历解析系统</h3>
      </div>

      <el-form-item prop="username">
        <el-input ref="username" v-model="loginForm.username" placeholder="用户名" name="username" type="text" tabindex="1"
          auto-complete="on" />
      </el-form-item>

      <el-form-item prop="password">
        <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="密码"
          name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
      </el-form-item>

      <el-button :loading="loginLoading" type="primary" style="width:100%;margin-bottom:20px;font-size:20px;"
        @click.native.prevent="handleLogin">登录
      </el-button>
      <el-row style="text-align: center;">
        <span style="margin-left:15%;margin-right:20%;cursor:pointer;font-size:18px;" @click="gotoRegister">注册</span>
        <span style="margin-left:25%;cursor:pointer;font-size:18px;" @click="gotoResetPassword">忘记密码</span>
      </el-row>

    </el-form>
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

        this.$refs.loginForm.validate(valid => {
          if (valid) {
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
        })
      },
      gotoRegister() {
        this.$router.push('/register')
      },
      gotoResetPassword() {
        this.$router.push('/forget-password')
      },
    }
  }
</script>

<style lang="scss">
  $light_gray: #fff;
  $cursor: rgb(15, 2, 2);

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }
</style>

<style lang="scss" scoped>
  $dark_gray: #889aa4;
  $light_gray: #eee;

  .login-container {
    min-height: 100%;
    width: 100%;
    overflow: hidden;

    .login-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 160px 35px 0;
      margin: 0 auto;
      overflow: hidden;
      border: 0;
      background-color: transparent;
      COLOR: #ffffff;
      font-size: 12pt;
    }

    // .svg-container {
    //   padding: 6px 5px 6px 15px;
    //   color: $dark_gray;
    //   vertical-align: middle;
    //   width: 30px;
    //   display: inline-block;
    // }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: $light_gray;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
    }

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }
  }

  span:hover {
    color: #41b883;
  }

  canvas {
    display: block;
    vertical-align: bottom;
  }

  #particles-js {
    width: 100%;
    height: 100%;
    background-image: url('../../assets/images/login_background.png');
    background-size: cover;
    background-position: 50% 50%;
    background-repeat: no-repeat;
    position: absolute;
    top: 0;
    left: 0;
  }
</style>