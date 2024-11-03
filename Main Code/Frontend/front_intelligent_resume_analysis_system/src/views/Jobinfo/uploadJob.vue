<template>
    <div style="padding-left: 30px; padding-right: 30px;">
        <el-card style="width: 100%;margin-top: 20px;">
            <template #header>
                <h3 class="card-header">新增岗位</h3>
            </template>
            <div style="margin-top: 0;">
                <h4 class="input-title">
                    岗位名称
                </h4>
                <input type="text" style="width: 100%;" class="custom-input" placeholder="岗位名称"
                    v-model="jobInfoForm.jobName">
                <!-- <el-input v-model="input" style="width: 240px" placeholder="Please input" clearable /> -->
            </div>
            <el-row :gutter="20">
                <el-col :span="12">
                    <h4 class="input-title">
                        最低学历要求
                    </h4>
                    <select v-model="jobInfoForm.educationRequire" style="width: 100%;" class="custom-input">
                        <option class="select-option" value="" disabled>请选择学历</option>
                        <option class="select-option" value="高中">高中</option>
                        <option class="select-option" value="大专">大专</option>
                        <option class="select-option" value="本科">本科</option>
                        <option class="select-option" value="硕士">硕士</option>
                        <option class="select-option" value="博士">博士</option>
                    </select>
                </el-col>
                <el-col :span="12">
                    <h4 class="input-title">
                        专业要求
                    </h4>
                    <input type="text" style="width: 100%;" class="custom-input" placeholder="所需专业名称"
                        v-model="jobInfoForm.majorRequire">
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <h4 class="input-title">
                        薪资待遇
                    </h4>
                    <input type="text" style="width: 100%;" class="custom-input" placeholder="请输入薪资待遇"
                        v-model="jobInfoForm.salary">
                </el-col>
                <el-col :span="12">
                    <h4 class="input-title">
                        最低工作年限
                    </h4>
                    <input type="text" style="width: 100%;" class="custom-input" placeholder="最低工作年限"
                        v-model="jobInfoForm.workExperience">
                </el-col>
            </el-row>

            <h4 class="input-title">
                岗位描述
            </h4>
            <el-input v-model="jobInfoForm.JobDescription" :autosize="{ minRows: 8, maxRows: 12 }" type="textarea"
                placeholder="请输入上传岗位的详细描述, 通常分为岗位职责和任职要求" />

            <div style="margin-top: 20px; text-align: right;">
                <el-button plain round size="large" @click="reset">重置</el-button>
                <el-button type="primary" size="large" round @click="submit">新增</el-button>
            </div>


        </el-card>
    </div>
</template>

<script setup>
    import { reactive } from 'vue'
    import { mapStores } from 'pinia'
    import { useUser } from '../../stores/user';
    import pinia from '../../stores';
    import axios from 'axios';

    const jobInfoForm = reactive({
        jobName: '',
        educationRequire: '',
        salary: '',
        workExperience: '',
        majorRequire: '',
        JobDescription: '',
    })

    const reset = () => {
        jobInfoForm.jobName = ''
        jobInfoForm.educationRequire = ''
        jobInfoForm.salary = ''
        jobInfoForm.workExperience = ''
        jobInfoForm.majorRequire = ''
        jobInfoForm.JobDescription = ''
    }

    const submit = () => {

        console.log(`output->jobInfoFormb`, jobInfoForm)
        // axios.post('http://localhost:5177/api/Job/uploadJobs', {
        //     userId: useUser().userId,
        //     jobName: jobInfoForm.jobName,
        //     jobDetails: jobInfoForm.JobDescription,
        //     jobKeywords: jobInfoForm.majorRequire,
        //     minimumWorkYears: jobInfoForm.workExperience,
        //     minimumEducationLevel: jobInfoForm.educationRequire
        // }).then((res) => {
        //     console.log(res)
        //     $router.push({ path: '/job-info' })
        // })
    }

</script>

<style scoped>
    .card-header {
        color: #4e5155;
        margin: 0;
    }

    .input-title {
        color: #4e5155;
        margin-left: 5px;
        margin-bottom: 5px;
    }

    .select-option {
        color: #999;
        /* 设置占位符颜色 */
        font-size: 14px;
        /* 设置占位符文字大小 */
    }

    .custom-input {
        font-size: 14px;
        color: #4e5155;
        border-top: 0;
        border-right: 0;
        border-bottom: 1px solid #e7e8e8;
        border-left: 0;
        padding: 8px;
        outline: none;
        transition: border-color 0.3s;
    }

    .custom-input:focus {
        /* border-color: #716aca; */
        border-bottom: 2px solid #716aca;
        /* 你可以根据需要更改颜色 */
    }

    .custom-input::placeholder {
        color: #999;
        /* 设置占位符颜色 */
        font-size: 14px;
        /* 设置占位符文字大小 */
    }
</style>