<template>
    <div>
        <el-row :gutter="0">
            <el-col :span="6">
                <el-card style="max-width: 1000px;margin-left: 10px;margin-top: 10px;">
                    <template #header>
                        <div class="card-header">
                            <span>岗位列表</span>
                        </div>
                    </template>
                    <div>

                        <el-tree style="max-width: 1000px;" :data="data" node-key="id" default-expand-all
                            :expand-on-click-node="true" :indent="30" :highlight-current="true">
                            <template #default="{ node, data }">

                                <span @click="handleNodeClick(data.name)" class="custom-tree-node">
                                    <!-- 一级目录 -->
                                    <label v-if="data.jobNumber" style="font-size: 17px;">{{data.name}}</label>
                                    <label v-else style="font-size: 15px;">{{data.name}}</label>
                                    <label v-if="data.jobNumber" class="job-numbers">{{data.jobNumber}}</label>
                                    <label v-else
                                        class="job-numbers">{{data.weekJobResumes}}/{{data.allJobResume}}</label>
                                </span>
                                <!-- <el-row @click="handleNodeClick(data.name)" :span="10">
                                    <el-col style="font-size: 20px;" :span="18">{{data.name}}</el-col>
                                    <el-col :span="6">
                                        <label v-if="data.jobNumber" class="job-numbers">{{data.jobNumber}}</label>
                                        <label v-else
                                            class="job-numbers">{{data.weekJobResumes}}/{{data.allJobResume}}</label>
                                    </el-col>
                                </el-row> -->
                            </template>
                        </el-tree>

                    </div>
                </el-card>


            </el-col>
            <el-col :span="18">
                <el-card class="right-part" style="height: 685px;overflow-y: auto;">
                    <template #header>
                        <div class="card-header">
                            <h2>岗位：{{clickJobName}}</h2>
                        </div>
                    </template>
                    <!--TODO: 单个卡片的样式，后面需要改成v-for -->
                    <el-card v-for="resumeInfo in resumeInfoList" class="match-card" shadow="always">
                        <el-row :space="10">
                            <el-col :span="3">
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
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
    import { ref, computed } from 'vue'

    const data = ref(
        [
            {
                name: '平面设计部',
                jobNumber: 2,
                children: [
                    { name: '平面设计师', weekJobResumes: 8, allJobResume: 40 },
                    { name: 'UI设计师', weekJobResumes: 2, allJobResume: 10 },
                ],
            },
            {
                name: '技术部',
                jobNumber: 3,
                children: [
                    { name: '前端工程师', weekJobResumes: 10, allJobResume: 250 },
                    { name: '后端工程师', weekJobResumes: 10, allJobResume: 150 },
                    { name: '全栈工程师', weekJobResumes: 10, allJobResume: 5 },
                ],
            },
            {
                name: '运营部',
                jobNumber: 2,

                children: [
                    { name: '产品经理', weekJobResumes: 10, allJobResume: 50 },
                    { name: '运营', weekJobResumes: 10, allJobResume: 50 },
                ],
            },
            {
                name: '市场部',
                jobNumber: 2,

                children: [
                    { name: '市场', weekJobResumes: 10, allJobResume: 50 },
                    { name: '销售', weekJobResumes: 10, allJobResume: 50 },
                ],
            },
            {
                name: '人事部',
                jobNumber: 1,

                children: [
                    { name: '人事', weekJobResumes: 10, allJobResume: 50 },
                ],
            },
            {
                name: '其他',
                jobNumber: 1,

                children: [
                    { name: '其他', weekJobResumes: 10, allJobResume: 50 },
                ],
            }


        ]


    )

    const defaultProps = {
        children: 'children',
        label: 'label',
    }
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

    const resumeInfo = {
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
    }

    const manpicter = ref(new URL('@/assets/images/man.jpg', import.meta.url))
    const womanpicter = ref(new URL('@/assets/images/women.jpg', import.meta.url))
    // TODO：这个应该是在向后端发请求后再赋值
    const clickJobName = ref(data.value[0].children[0].name)

    //函数
    const handleNodeClick = (newclickJobName) => {
        console.log('点击左侧名字', newclickJobName)
        var isFirstLayer = false
        //筛选点击的岗位
        data.value.forEach(element => {
            if (element.name == newclickJobName) {
                isFirstLayer = true
            }
        });
        if (!isFirstLayer) {
            clickJobName.value = newclickJobName
            console.log(`output->是二级标题`)
        }
    }

</script>

<style scoped>
    .education-info {

        margin-left: 8px;
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

    .gender-info {
        margin-left: 15px;
        font-size: small;
        color: #668B8B;
    }

    .picter {
        height: 80px;
        width: 80px;
        border-radius: 50px;
    }

    .match-card {
        width: 100%;
        height: 120px;
        margin-right: 10px;
        margin-top: 10px;
    }

    .right-part {
        margin-top: 10px;
        height: 100px;
        margin-left: 5px;
        margin-right: 10px;
    }

    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
        margin-left: 10px;
        height: 50px;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    :deep(.el-collapse-item__header) {
        font-size: 16px;
        font-weight: bold;
    }

    .job-numbers {
        width: max-content;
        height: max-content;
        background-color: #91fb84;
        border-radius: 7px;
        padding-left: 5px;
        padding-right: 5px;
        margin-right: 20px;

    }

    .job-li {
        margin-top: 10px;
    }

    /* 覆盖element-ui样式 */
    .el-tree-node {
        height: 60px;
        /* 设置节点高度 */
        line-height: 60px;
        /* 设置行高以居中内容 */
    }

    .custom-tree-node {
        margin-bottom: 10px;
        /* 设置节点间距 */
    }
</style>