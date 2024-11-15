<template>
    <div>
        <el-row :gutter="5"
            style="height: 340px; margin-left: 5px;margin-top: 5px; margin-right: 5px;margin-bottom: 5px;">
            <el-col :span="8">
                <!-- 新增邮箱界面 -->
                <ElCard style="height: 340px;">

                    <!-- <el-text class="mx-1 large-text">邮箱管理</el-text> -->
                    <h3 style="margin-top: 3px;margin-bottom: 3px;">邮箱管理</h3>
                    <!-- <div class="large-text">邮箱管理</div> -->
                    <div class="buttonParent">

                        <el-button type="success" icon="plus" @click="addEmail">
                            添加
                        </el-button>
                    </div>
                    <el-table :data=" EmailData" style="width: 100%;" max-height="250">
                        <el-table-column prop="emailAddress" label="Email" width="250" />
                        <el-table-column fixed="right" label="Operations" width="120">
                            <template #default="scope">
                                <div v-if="scope.row.syncEnabled">
                                    <!-- 监听 -->
                                    <el-popconfirm @confirm="changeListen(scope.row)" @cancel="cancelDelete(scope.row)"
                                        title="确定取消监听该邮箱吗？">
                                        <template #reference>
                                            <el-button link type="primary" size="small">取消监听</el-button> </template>
                                    </el-popconfirm>

                                    <el-popconfirm @confirm="deleteEmail(scope.row)" @cancel="cancelDelete(scope.row)"
                                        title="确定解除绑定该邮箱吗？">
                                        <template #reference>
                                            <el-button link type="primary" size="small">删除</el-button>
                                        </template>
                                    </el-popconfirm>
                                    <!-- <el-button link type="primary" size="small"
                                        @click="deleteEmail(scope.row)">删除</el-button> -->
                                </div>
                                <div v-else>
                                    <!-- 未监听 -->
                                    <el-popconfirm @confirm="changeListen(scope.row)" @cancel="cancelDelete(scope.row)"
                                        title="确定开启监听该邮箱吗？">
                                        <template #reference>
                                            <el-button link type="primary" size="small">开启监听</el-button> </template>
                                    </el-popconfirm>
                                    <!-- <el-button link type="primary" size="small">删除</el-button> -->
                                    <el-popconfirm @confirm="deleteEmail(scope.row)" title="确定解除绑定该邮箱吗？">
                                        <template #reference>
                                            <el-button link type="primary" size="small">删除</el-button>
                                        </template>
                                    </el-popconfirm>
                                </div>

                            </template>
                        </el-table-column>
                    </el-table>
                </ElCard>
            </el-col>
            <el-col :span="16">
                <ElCard>
                    <emailNewAdd :userId="userStore.userId"></emailNewAdd>

                </ElCard>
            </el-col>
        </el-row>
        <el-row style=" margin-left: 5px;margin-top: 5px; margin-right: 5px;">
            <el-col :span="24">
                <ElCard style="height: 350px;">
                    <!-- <el-text class="mx-1 large-text">邮箱接收到的简历信息</el-text> -->
                    <h3 style="margin-top: 3px;margin-bottom: 3px;">邮箱接收到的简历信息</h3>

                    <!-- 表格 存放邮箱监听的数据 -->
                    <el-table :data="emailResumeData" style="width: 100%; height: 350px;margin-top: 20px; ">
                        <el-table-column prop="receiveEmail" label="接收邮箱" width="200" />
                        <el-table-column prop="sendEmail" label="发件邮箱" width="200" />
                        <el-table-column prop="emailTitle" label="邮箱标题" width="200" />
                        <el-table-column prop="applicantName" label="求职者名称" width="200" />
                        <el-table-column prop="intentionJob" label="意向岗位" width="200" />
                        <el-table-column prop="receiveDate" label="时间" />
                    </el-table>

                </ElCard>
            </el-col>

        </el-row>

        <!-- 弹框 -->
        <el-dialog v-model="dialogEmailAddVisible" title="新建监听邮箱" align-center width="500">
            <el-form :model="emailAddInfo" label-width="auto" style="max-width: 500px">
                <el-form-item label="邮箱地址：">
                    <el-input v-model="emailAddInfo.email" />
                </el-form-item>
                <el-form-item label="邮箱授权码：">
                    <el-input v-model="emailAddInfo.password" />
                </el-form-item>
            </el-form>


            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogEmailAddVisible = false">取消</el-button>
                    <el-button type="primary" @click="sendAddEmailInfo">
                        添加
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>


<script setup>
    import emailNewAdd from './emailNewAdd.vue'
    import { ref, reactive, onMounted } from 'vue'
    import { ElMessage } from 'element-plus'
    import { useUser } from '../../stores/user';
    import axios from 'axios'
    const emailAddInfo = reactive({
        email: '',
        password: '',
    })
    const emailResumeData = ref([
        {
            receivingEmail: '1571767468@qq.com',
            sendingEmail: '168985423@qq.com',
            subject: '求职',
            applicantName: '李中元',
            desiredPosition: '前端开发',
            receivedDate: '2024-06-11',
        },
        {
            receivingEmail: '1571767468@qq.com',
            sendingEmail: '168985423@qq.com',
            subject: '求职',
            applicantName: '章三',
            desiredPosition: '后端开发',
            receivedDate: '2024-06-11',
        }
    ])

    const userStore = useUser()
    // const EmailData = ref([
    //     {
    //         'emailAddress': '2016-05-03',
    //         'isSyncEnabled': false,
    //     },
    //     {
    //         'emailAddress': '1571767431@qq.com',
    //         'isSyncEnabled': true,
    //     },
    //     {
    //         'emailAddress': '2016-05-03',
    //         'isSyncEnabled': false,
    //     },

    // ])

    const EmailData = ref([])
    const dialogEmailAddVisible = ref(false)


    //===============================================绑定的函数=====================================
    // 打开添加邮箱的弹框
    const addEmail = () => {
        console.log('添加邮箱')
        dialogEmailAddVisible.value = true
    }
    const handleClick = () => {
        console.log('click')
    }

    const deleteEmail = (row) => {
        console.log('删除邮箱')
        console.log(`output->row`, row)
        //向后端发送post请求
        //请求参数
        const deleteEmailInfo = {
            'userEmailId': row.id,
        }
        console.log(`output->deleteEmailInfo`, deleteEmailInfo)
        axios.post('http://localhost:8080/api/email/delete/email', {
            "emailId": row.emailId
        }).then((res) => {
            console.log(`output->res`, res)
            if (res.data.code == 20000) {
                ElMessage({
                    message: '邮箱删除成功',
                    type: 'success',
                })
                EmailData.value.splice(EmailData.value.indexOf(row), 1)
            } else {
                ElMessage({
                    message: '邮箱删除失败',
                    type: 'error',
                })
            }
        }).catch((err) => {
            console.log(`output->err`, err)
        })
    }

    const cancelDelete = (row) => {
        console.log('取消删除')
        console.log(`output-row`, row)
    }

    // 改变监听状态
    const changeListen = (row) => {
        console.log('改变监听状态')
        console.log(`原先状态output->row`, row)

        //向后端发送post请求
        //请求参数
        const updateEmailInfo = {
            'emailId': row.emailId,
        }
        console.log(`output->updateEmailInfo`, updateEmailInfo)
        axios.post('http://localhost:8080/api/email/change/status', updateEmailInfo).then((res) => {
            console.log(`output->res`, res)
            ElMessage({
                message: '邮箱监听状态修改成功',
                type: 'success',
            })
            if (row.syncEnabled) {
                row.syncEnabled = false
            } else {
                row.syncEnabled = true
            }
        }).catch((err) => {
            console.log(`output->err`, err)
        })

    }

    //添加邮箱信息
    const sendAddEmailInfo = () => {
        console.log('添加邮箱信息')
        const sendData = {
            'userId': userStore.userId,
            'email': emailAddInfo.email,
            'emailPassword': emailAddInfo.password,
        }
        //向后端发送post请求
        axios.post('http://localhost:8080/api/email/add', sendData).then((res) => {
            if (res.data.code == 20000) {
                const addData = {
                    "emailAddress": emailAddInfo.email,
                    "syncEnabled": true,
                    "emailId": res.data.emailId
                }
                EmailData.value.push(addData)
                console.log(`output->res`, res)
                ElMessage({
                    message: '邮箱添加成功',
                    type: 'success',
                })
                dialogEmailAddVisible.value = false
            } else {
                ElMessage({
                    message: '该邮箱已存在,邮箱添加失败',
                    type: 'error',
                })
            }

        }).catch((err) => {
            console.log(`output->err`, err)
        })
    }

    //===============================================mounted时运行的函数=====================================
    const getAllEmailInfo = () => {
        console.log('获取所有邮箱信息')
        // console.log(this.userStore.userId)
        //使用axios向后端发送post请求
        axios.post('http://localhost:8080/api/email/get/all/email', { 'userId': userStore.userId }).then((res) => {
            console.log(`output->res`, res)
            EmailData.value = res.data

        }).catch((err) => {
            console.log(`output->err`, err)
        })

    }

    const getAllResumeInfo = () => {
        console.log('获取所有简历信息')
        //使用axios向后端发送post请求 TODO
        axios.post('http://localhost:8080/api/email/get/all/email/receive/resume/info', { 'userId': userStore.userId }).then((res) => {
            console.log(`output->res`, res)
            emailResumeData.value = res.data
            // emailResumeData.value = ref([
            //     {
            //         email: 'abcddfefzs@qq.com',
            //         sender: '123654@qq.com',
            //         title: '求职',
            //         name: '张三',
            //         jobIntention: '前端开发',
            //         time: '2016-05-03',
            //     },
            //     {
            //         email: '5656515',
            //         sender: '1515610651',
            //         title: '求职',
            //         name: '张三',
            //         jobIntention: '后端开发',
            //         time: '2016-05-03',
            //     }
            // ])

        }).catch((err) => {
            console.log(`output->err`, err)
        })
    }
    onMounted(() => {
        getAllEmailInfo()
        getAllResumeInfo()
    })
</script>

<style scoped>
    .addInput {
        margin-top: 10px;
    }

    .buttonParent {
        display: flex;
        justify-content: flex-end;
    }

    .large-text {
        font-size: 20px;
    }
</style>