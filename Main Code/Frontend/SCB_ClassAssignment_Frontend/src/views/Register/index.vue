<template>
  <div id="register" style="height:100%;width:100%;display:flex;justify-content: center; align-items: center;">
    <el-card class="box-card" style="width: 60%;text-align: center; margin-bottom: 5%;">
      <div slot="header" style="text-align: center;">
        <h2 class="register-title">欢迎注册</h2>
      </div>
      <div style="width: 60%;margin-left: 20%">
        <el-form label-width="100px"
                 ref="registerForm"
                 :rules="registerRules"
                 :model="user">
          <el-form-item label="用户名：" prop="username">
            <el-input type="text" placeholder="请输入用户名" v-model="user.username"/>
          </el-form-item>
          <el-form-item label="所属公司：" prop="hospital">
            <el-input type="text" placeholder="请输入所属公司名称" v-model="user.hospital"/>
          </el-form-item>
          <!--          <el-form-item label="性别:" prop="gender">-->
          <!--            <el-select style="float: left;width: 220px" v-model="user.gender" placeholder="Please select your gender">-->
          <!--              <el-option label="Male" value="M"></el-option>-->
          <!--              <el-option label="Female" value="F"></el-option>-->
          <!--            </el-select>-->
          <!--          </el-form-item>-->
          <!--          <el-form-item label="Email:" prop="email">-->
          <!--            <el-input v-model="user.email" placeholder="Please enter your email"></el-input>-->
          <!--          </el-form-item>-->
          <el-form-item label="岗位：" prop="userInfo">
            <el-input v-model="user.userInfo" type="text" :autosize="{ minRows: 4}" placeholder="请输入岗位信息"/>
          </el-form-item>
          <el-form-item label="密码：" prop="newPassword1">
            <el-input type="password" placeholder="请输入密码" v-model="user.newPassword1" show-password/>
          </el-form-item>
          <el-form-item label="确认密码：" prop="newPassword2">
            <el-input type="password" placeholder="请确认您的密码" v-model="user.newPassword2" show-password/>
          </el-form-item>
          <el-button :loading="registerLoading" type="primary" style="width:100%;margin-bottom:20px;font-size:20px;"
                     @click.native.prevent="handleRegister">注册
          </el-button>
          <el-row style="text-align:center;cursor:pointer;">
            <span @click="gotoLogin()">已有账号，前往登录</span>
          </el-row>
        </el-form>
      </div>
    </el-card>
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
    // const checkEmail = (rule, value, callback) => {
    //   if (!validEmail(value)) {
    //     callback(new Error('请输入正确的邮箱格式'))
    //   } else {
    //     callback()
    //   }
    // }
    return {
      registerLoading: false,
      interestOptions: [],
      user: {
        username: '',
        // email: '',
        // gender: '',
        // school: '',
        // interest: [],
        hospital: '',
        userInfo: '',
        newPassword1: '',
        newPassword2: ''
      },
      registerRules: {
        username: [
          {
            required: true,
            validator: checkUsername,
            trigger: 'blur'
          }
        ],
        // gender: [
        //   {
        //     message: 'Please select your gender',
        //     trigger: 'blur'
        //   }
        // ],
        hospital: [
          {
            required: true,
            trigger: 'blur'
          }
        ],
        newPassword1: [
          {
            required: true,
            validator: checkPassword,
            trigger: 'blur'
          }
        ],
        newPassword2: [
          {
            required: true,
            validator: checkPassword,
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          if (this.user.newPassword1 === this.user.newPassword2) {
            this.registerLoading = true
            return new Promise((resolve, reject) => {
              register({
                username: this.user.username,
                password: this.user.newPassword1,
                user_info: this.user.userInfo,
                hospital: this.user.hospital
              }).then(res => {
                this.registerLoading = false
                this.$message({
                  message: '注册完成，请等待管理员审核',
                  type: 'success'
                })
                setTimeout(function() {
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
      })
    },
    gotoLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
span:hover {
  color: #41b883;
}

#register{
  background-image: url('../../assets/images/login_background.png');
}
</style>
