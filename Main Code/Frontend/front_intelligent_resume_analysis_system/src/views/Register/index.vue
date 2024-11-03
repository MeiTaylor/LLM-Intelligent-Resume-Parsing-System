<template>

  <div class="all-background">
    <div class="login-box">
      <h2>注册</h2>
      <form>
        <div class="user-box">
          <input type="text" style="margin-top: 10px;margin-bottom: 50px;" v-model="user.username">
          <label>账号</label>
        </div>
        <div class="user-box">
          <input type="password" style="margin-top: 10px;margin-bottom: 50px;" v-model="user.password">
          <label>密码</label>
        </div>
        <div class="user-box">

          <input type="password" style="margin-top: 10px;margin-bottom: 0px;" v-model="user.confirmPassword">
          <label>重新输入密码</label>
          <div v-if="!isEqual"
            style="margin-left: 0px; margin-top: 0px; color: #f56c6c;height: 25px;margin-bottom: 25px;">
            与密码不一致
          </div>
          <div v-if="isEqual"
            style="margin-left: 0px; margin-top: 0px;margin-bottom: 25px; color: #f56c6c;height: 25px;">
          </div>

        </div>
        <div class="user-box">

          <input type="text" style="margin-top: 10px;margin-bottom: 50px;" v-model="user.companyName">
          <label>公司名称</label>
          <!-- <div v-if="!isEqual" style="margin-left: 0px; margin-top: 0px; color: #f56c6c;height: 25px;">与密码不一致
                  </div>
                  <div v-if="isEqual" style="margin-left: 0px; margin-top: 0px; color: #f56c6c;height: 25px;"></div> -->

        </div>
        <div class="user-box">

          <input type="text" style="margin-top: 10px;margin-bottom: 50px;" v-model="user.email">
          <label>邮箱</label>
          <!-- <div v-if="!isEqual" style="margin-left: 0px; margin-top: 0px; color: #f56c6c;height: 25px;">与密码不一致
                  </div>
                  <div v-if="isEqual" style="margin-left: 0px; margin-top: 0px; color: #f56c6c;height: 25px;"></div> -->

        </div>
        <div style="display: flex;justify-content: space-between;">
          <a @click="handleRegister">
            注册
          </a>
          <a @click="gotoLogin">
            返回登陆
          </a>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
  import { register } from '@/api/user'
  import { validRegisterUsername, validRegisterPassword, validEmail } from '@/utils/validate'
  import ElDragSelect from '@/components/DragSelect/index.vue'

  export default {
    components: { ElDragSelect },
    name: 'index',
    data() {
      const checkUsername = (rule, value, callback) => {
        if (!validRegisterUsername(value)) {
          callback(new Error('请输入2-10位用户名'))
        } else {
          callback()
        }
      }
      const checkPassword = (rule, value, callback) => {
        if (!validRegisterPassword(value)) {
          callback(new Error('6位以上密码,至少包括一个大写字母，一个小写字母，1个数字，1个特殊字符'))
        } else {
          callback()
        }
      }
      return {
        registerLoading: false,
        interestOptions: [],
        user: {
          username: '',
          companyName: '',
          userInfo: '',
          password: '',
          confirmPassword: ''
        },
      }
    },
    computed: {
      isEqual() {
        //什么时候返回false 当两次密码不一致时
        if ((this.user.password !== this.user.confirmPassword) && this.user.confirmPassword !== '') {
          return false
        } else {
          return true
        }

      }
    },
    methods: {
      handleRegister() {
        if (1) {
          if (this.user.newPassword1 === this.user.newPassword2) {
            this.registerLoading = true
            return new Promise((resolve, reject) => {
              register({
                username: this.user.username,
                password: this.user.password,
                user_info: this.user.companyName,
                hospital: this.user.hospital
              }).then(res => {
                this.registerLoading = false
                this.$message({
                  message: '注册完成，请等待管理员审核',
                  type: 'success'
                })
                setTimeout(function () {
                  this.gotoLogin()
                }.bind(this), 1000)
                resolve()
              }).catch(error => {
                this.registerLoading = false
                reject(error)
              })
            })
          } else {
            this.registerLoading = false
            this.user.newPassword2 = ''
            this.$message({
              message: '两次输入的密码不一致',
              type: 'error'
            })
          }
        }
      },
      gotoLogin() {
        this.$router.push('/login')
      },

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
    margin-bottom: -20px;
    margin-top: -20px;
  }

  .login-box .user-box input {
    width: 100%;
    padding: 10px 0;
    font-size: 16px;
    color: #fff;
    margin-bottom: 30px;
    margin-top: 10px;
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
    font-size: 16px;
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