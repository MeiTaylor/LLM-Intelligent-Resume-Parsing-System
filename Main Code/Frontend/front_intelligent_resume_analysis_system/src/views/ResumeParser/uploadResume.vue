<template>
    <div>
        <!-- 上传简历界面和简历库界面结合在一起 -->
        <el-row :gutter="10">
            <el-col :span="6" style="height:fit-content;">
                <el-card style="margin-left: 10px; margin-top: 10px;">
                    <el-upload class="upload-demo" drag
                        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15" multiple>
                        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                        <div class="el-upload__text">
                            拖动 或者 <em>点击上传简历</em>
                        </div>
                        <template #tip>
                            <div class="el-upload__tip">
                                简历文件应小于 500kb
                            </div>
                        </template>
                    </el-upload>
                </el-card>
            </el-col>
            <el-col :span="18">
                <el-card style="margin-right: 10px; margin-top: 10px; height:96%">
                    <template #header>
                        <div class="card-header">
                            <p style="font-size: larger; font-weight: bold; margin: 5px;">简历检索</p>
                        </div>
                    </template>
                    <el-row :gutter="10">
                        <el-col :span="8">
                            <el-input-number v-model="searchByAge" style="width: 75%;height: 40px;" :min="0" :max="100"
                                placeholder="按年龄检索" />
                        </el-col>
                        <el-col :span="8">
                            <el-select v-model="searchByEducation" class="m-2" placeholder="按学历检索" size="large">
                                <el-option v-for="item in EducationOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-col>
                        <el-col :span="8">
                            <el-input-number v-model="searchByScore" style="width: 75%;height: 40px;" :min=" 0"
                                :max="100" placeholder="按匹配分数检索" />
                        </el-col>
                        <!-- 测试 -->

                    </el-row>

                    <el-row :gutter="10" style="margin-top: 10px; margin-bottom: 10px;">
                        <el-col :span="8">
                            <el-input v-model="searchByName" placeholder="按姓名检索" />
                        </el-col>
                        <el-col :span="8">
                            <el-input v-model="searchByJobIntention" placeholder="按求职意向检索" />
                        </el-col>
                        <el-col :span="8">
                            <el-button type="primary" style="width: 55%;">搜索</el-button>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>

        <div>
            <el-card style="margin-left: 10px;margin-right: 10px; margin-top: 10px; height:100%;">
                <template #header>
                    <div>
                        <p style="font-size: larger; font-weight: bold; padding: 0;margin: 0;">简历库</p>
                    </div>
                </template>

                <!--TODO: 单个卡片的样式，后面需要改成v-for -->
                <div style="height: 370px;overflow-y: scroll;">
                    <el-card v-for="resumeInfo in resumeInfoList" class="match-card" shadow="always">
                        <el-row :space="10">
                            <el-col :span="2">
                                <img :src="resumeInfo.gender==='女'?womanpicter:manpicter" class="picter">
                            </el-col>
                            <el-col :span="15">
                                <div>
                                    <label style="font-size:larger">{{resumeInfo.name}}</label>
                                    <label class="gender-info">{{resumeInfo.gender}} |
                                        {{resumeInfo.age}}岁 |
                                        {{resumeInfo.workExperience}}年工作经验</label>
                                    <label v-for="characteristic in resumeInfo.characteristics"
                                        class="characteristic">{{characteristic}}</label>
                                </div>
                                <div style="margin-top: 10px;">
                                    <label style="color: #e8bf88;">匹配岗位:{{resumeInfo.matchJob}}</label>
                                    <label
                                        style="color:#5a8255; margin-left: 30px;">求职意向:{{resumeInfo.JobIntention}}</label>
                                </div>
                                <div style="margin-top: 10px;  display: flex;align-items: center;">
                                    <el-icon color="gray">
                                        <School />
                                    </el-icon>
                                    <label class="education-info ">{{resumeInfo.school}} | {{resumeInfo.major}} |
                                        {{resumeInfo.education}}</label>
                                </div>
                            </el-col>
                        </el-row>
                    </el-card>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script setup>
    import { ref, computed } from 'vue'
    // TODO: 这是在后端发送完请求后再赋值的，还需加上为空时的异常处理
    const resumeInfoList = ref([{
        name: '林一',
        gender: '男',
        age: 22,
        workExperience: 1,
        education: '本科',
        major: '计算机科学与技术',
        score: '90',
        JobIntention: '算法工程师',
        matchJob: '算法工程师',
        school: '武汉大学',
        characteristics: ['能力出众', '经验丰富', '工作稳定']
    }, {
        name: '李思',
        gender: '女',
        age: 22,
        workExperience: 1,
        education: '本科',
        major: '计算机科学与技术',
        score: '90',
        JobIntention: '算法工程师',
        matchJob: '算法工程师',
        school: '武汉大学',
        characteristics: ['能力出众', '经验丰富', '工作稳定']
    }, {
        name: '龙天一',
        gender: '男',
        age: 22,
        workExperience: 1,
        education: '本科',
        major: '计算机科学与技术',
        score: '90',
        JobIntention: '算法工程师',
        matchJob: '算法工程师',
        school: '武汉大学',
        characteristics: ['能力出众', '经验丰富', '工作稳定']
    }, {
        name: '李思思',
        gender: '女',
        age: 22,
        workExperience: 1,
        education: '本科',
        major: '计算机科学与技术',
        score: '90',
        JobIntention: '算法工程师',
        matchJob: '算法工程师',
        school: '武汉大学',
        characteristics: ['能力出众', '经验丰富', '工作稳定']
    }, {
        name: '林二',
        gender: '男',
        age: 22,
        workExperience: 1,
        education: '本科',
        major: '计算机科学与技术',
        score: '90',
        JobIntention: '算法工程师',
        matchJob: '算法工程师',
        school: '武汉大学',
        characteristics: ['能力出众', '经验丰富', '工作稳定']
    }


    ])

    const manpicter = ref(new URL('@/assets/images/man.jpg', import.meta.url))
    const womanpicter = ref(new URL('@/assets/images/women.jpg', import.meta.url))

</script>

<style scoped>
    .upload-demo .el-upload__inner {
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0;
    }

    .upload-demo .el-upload__text {
        padding: 0;
        font-size: 14px;
    }

    .upload-demo .el-icon--upload {
        font-size: 24px;
        margin-bottom: 4px;
    }

    .match-card {
        width: 99.2%;
        height: 120px;
        margin-right: 10px;
        margin-bottom: 10px;
    }

    .picter {
        height: 80px;
        width: 80px;
        border-radius: 50px;
    }

    .gender-info {
        margin-left: 15px;
        font-size: small;
        color: #668B8B;
    }

    .characteristic {
        height: max-content;
        width: max-content;
        margin-left: 6px;
        padding-left: 5px;
        padding-right: 5px;
        padding-bottom: 3px;
        padding-top: 3px;
        font-size: small;
        color: #5d45ca;
        border-radius: 5px;
        border: 3px solid skyblue;
    }

    .education-info {

        margin-left: 8px;
        font-size: small;
        color: #668B8B;
    }
</style>