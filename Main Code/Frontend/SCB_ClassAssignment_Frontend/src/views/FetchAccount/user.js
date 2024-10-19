import axios from "axios"
import { useUser } from '../../stores/user'
import { useFileStore } from '../../stores/file'
import { useResumeDetail } from '../../stores/resume'

import pinia from '../../stores'
import { mapState, mapStores } from 'pinia'
import { nextTick } from "vue"


export default {
  data() {
    return {
      roleDialog: {
        visible: false,
        roles: [],
        roleTree: [],
        checkedRoleKeys: [],
        defaultProps: {
          id: 'id',
          label: 'name',
          children: 'children'
        }
      },
      formVisible: false,
      formTitle: '添加用户',
      deptTree: {
        show: false,
        data: [],
        defaultProps: {
          id: 'id',
          label: 'simplename',
          children: 'children'
        }
      },
      isAdd: true,
      form: {
        id: '',
        account: '',
        name: '',
        email: '',
        password: '',
        rePassword: '',
      },
      rules: {
        account: [
          { required: true, message: '请输入登录账号', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入email', trigger: 'blur' }
        ]
      },

      // 总数
      total: 0,
      // 有用的
      list: null,
      listLoading: true,
      selRow: {}
    }
  },
  computed: {
    ...mapStores(useUser, useFileStore, useResumeDetail)
  },
  mounted() {
    axios.post('http://localhost:5177/api/Role/allUsers', { id: this.userStore.userId }).then((res) => {
      console.log(res)
      this.list = res.data.users
      console.log(this.list)
    })

  },
  methods: {
    resetForm() {
      this.form = {
        id: '',
        account: '',
        name: '',
        email: '',
        password: '',
        rePassword: '',
      }
    },
    add() {
      this.resetForm()
      this.formTitle = '添加用户'
      this.formVisible = true
      console.log(this.formVisible)
      this.isAdd = true
    },
    saveUser() {
      let id = parseInt(this.userStore.companyId)
      console.log(id)
      axios.post('http://localhost:5177/api/Role/createUser', { companyID: id, account: this.form.account, email: this.form.email, password: this.form.password, role: 'editor' }).then((res) => {
        console.log(res)
        this.formVisible = false
        this.$router.go(0)
      })
    },
    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: '请选中操作项',
        type: 'warning'
      })
      return false
    },
  }
}
