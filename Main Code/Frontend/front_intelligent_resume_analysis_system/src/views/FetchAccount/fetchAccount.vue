<template>
  <div style="background-color: #F0F2F5;">


    <div class="block" style="margin-left: 10px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input placeholder="请输入帐号"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input placeholder="请输入姓名"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="success" icon="Search">搜索</el-button>
          <el-button type="primary" icon="refresh">重置</el-button>
        </el-col>
      </el-row>
      <br>
      <el-row>
        <el-col :span="24">
          <el-button type="success" icon="plus" @click.native="add">
            添加
          </el-button>

        </el-col>
      </el-row>
    </div>


    <el-table :data="list">

      <el-table-column label="账号" prop="account">

      </el-table-column>
      <el-table-column label="权限" prop="role">

      </el-table-column>
      <el-table-column label="操作栏">
        <template #default="scope">
          <el-button type="danger" @click="deleteUser(scope.row.id)">删除账户</el-button>
          <el-button type="success" @click="handlePause(scope.row)">修改信息</el-button>
        </template>
      </el-table-column>

    </el-table>


    <!-- 添加账户 -->
    <el-dialog :title="formTitle" v-model="formVisible" width="70%">
      <el-form :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账户" prop="account">
              <el-input v-model="form.account" minlength=1></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="isAdd">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="isAdd">
            <el-form-item label="确认密码" prop="rePassword">
              <el-input v-model="form.rePassword" type="password"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-form-item>
          <el-button type="primary" @click="saveUser">提交</el-button>
          <el-button @click.native="formVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>

</template>


<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import axios from "axios";
  import { useUser } from '../../stores/user';
  import { useFileStore } from '../../stores/file';
  import { useResumeDetail } from '../../stores/resume';
  import { useJobDetail } from '../../stores/job';
  import { useRouter } from 'vue-router';
  import { ElMessage } from 'element-plus';

  // 使用 Pinia 的 store
  const userStore = useUser();
  const fileStore = useFileStore();
  const resumeDetailStore = useResumeDetail();
  const jobDetailStore = useJobDetail();

  const router = useRouter();

  // 响应式数据
  const roleDialog = reactive({
    visible: false,
    roles: [],
    roleTree: [],
    checkedRoleKeys: [],
    defaultProps: {
      id: 'id',
      label: 'name',
      children: 'children'
    }
  });

  const formVisible = ref(false);
  const formTitle = ref('添加用户');
  const isAdd = ref(true);

  const form = reactive({
    id: '',
    account: '',
    name: '',
    email: '',
    password: '',
    rePassword: ''
  });

  const rules = {
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
  };

  const total = ref(0);
  const list = ref(null);
  const listLoading = ref(true);
  const selRow = ref({});

  // 方法
  const resetForm = () => {
    form.id = '';
    form.account = '';
    form.name = '';
    form.email = '';
    form.password = '';
    form.rePassword = '';
  };

  const add = () => {
    resetForm();
    formTitle.value = '添加用户';
    formVisible.value = true;
    isAdd.value = true;
  };

  //删除用户
  const deleteUser = (userId) => {
    console.log(`output->userId`, userId)
  }

  const saveUser = () => {
    const companyId = parseInt(userStore.companyId);
    axios.post('http://localhost:8080/api/users/addUser', {
      companyID: companyId,
      account: form.account,
      email: form.email,
      password: form.password,
      role: 'editor'
    }, {
      params: {
        currentUserId: userStore.userId
      }
    }).then((res) => {
      console.log(res);
      formVisible.value = false;
      ElMessage({
        message: '添加成功',
        type: 'success'
      });
      getUserList();
    });
  };

  const checkSel = () => {
    if (selRow.value && selRow.value.id) {
      return true;
    }
    ElMessage({
      message: '请选中操作项',
      type: 'warning'
    });
    return false;
  };

  // 获得用户全部信息
  const getUserList = () => {
    axios.get('http://localhost:8080/api/users/allUsers', { params: { userId: userStore.userId } }).then((res) => {
      console.log(res);
      list.value = res.data;
      console.log(list.value);

    });
  };

  // 生命周期
  onMounted(() => {
    getUserList();
  });
</script>



<style lang="scss">
  .el-form-item {
    margin-bottom: 2px;
  }

  .application-table-expand {
    font-size: 0;
  }

  .application-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

  .user {
    padding: 10px;
  }

  .user>strong {
    color: #777;
    font-size: 15px;
    font-weight: 400;
  }

  .user>small {
    color: #9E9E9E;
  }

  .user-content {
    box-shadow: 2px 2px 4px #edecec;
    min-height: 500px;
  }

  .user-content>.profile {
    background: #edecec;
    padding: 10px;
    height: 500px;
    width: 220px;
  }

  .user-content>.profile>img {
    width: 100%;
  }
</style>