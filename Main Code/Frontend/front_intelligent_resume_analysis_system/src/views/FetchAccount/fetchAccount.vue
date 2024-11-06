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
          <el-button type="primary" icon="edit">
            修改
          </el-button>
          <el-button type="danger" icon="delete">
            删除
          </el-button>
        </el-col>
      </el-row>
    </div>


    <el-table :data="list">

      <el-table-column label="账号" prop="account">

      </el-table-column>

    </el-table>

    <el-dialog :title="formTitle" v-model="formVisible" width="70%">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
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


<script src="./user.js"></script>


<style lang="scss">
  .block {
    padding: 10px 0px;
  }

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