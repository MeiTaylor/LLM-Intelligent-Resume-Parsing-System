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
                            :expand-on-click-node="true" :indent="10" :highlight-current="true">
                            <template #default="{ node, data }">

                                <span @click="handleNodeClick(data.jobName)" class="custom-tree-node">
                                    <!-- 一级目录 -->
                                    <label v-if="data.jobNumber" style="font-size: 17px;">{{data.name}}</label>
                                    <label v-else style="font-size: 15px;">{{data.jobName}}</label>
                                    <label v-if="data.jobNumber" class="job-numbers">{{data.jobNumber}}</label>
                                    <label v-else
                                        class="job-numbers">{{data.weekJobResumes}}/{{data.allJobResume}}</label>
                                </span>
                            </template>
                        </el-tree>

                    </div>
                </el-card>


            </el-col>
            <el-col :span="18">
                <el-card class="right-part" style="height: 685px;overflow-y: auto;">
                    <template #header>
                        <div class="card-header">
                            <h2 style="margin: 0;">岗位：{{clickJobName}}</h2>
                        </div>
                    </template>
                    <div v-if="resumeInfoList.length==0?true:false">
                        <h1 class="no-resume">暂无匹配的简历</h1>
                    </div>
                    <!--TODO: 单个卡片的样式，后面需要改成v-for -->
                    <el-card v-for="resumeInfo in resumeInfoList" class="match-card" shadow="always">
                        <el-row :space="10">
                            <el-col :span="3">
                                <img :src="resumeInfo.gender==='女'?womanpicter:manpicter" class="picter">
                            </el-col>
                            <el-col :span="21">
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
    import { ref, computed, onMounted } from 'vue'
    import axios from 'axios'
    import { useUser } from '../../stores/user';

    //创建store变量
    const userStore = useUser()

    const data = ref([])

    const defaultProps = {
        children: 'children',
        label: 'label',
    }
    // TODO: 这是在后端发送完请求后再赋值的，还需加上为空时的异常处理
    const resumeInfoList = ref([

    ])

    var allResumeInfo = []


    const manpicter = ref(new URL('@/assets/images/man.jpg', import.meta.url))
    const womanpicter = ref(new URL('@/assets/images/women.jpg', import.meta.url))
    const clickJobName = ref()

    //函数  点击左侧树形结构的岗位名字
    const handleNodeClick = (newclickJobName) => {
        if (newclickJobName) {
            console.log('点击左侧名字', newclickJobName)
            allResumeInfo.forEach((element => {
                if (element.jobName == newclickJobName) {
                    resumeInfoList.value = element.resumeWithJobInfos
                }
            }))
        }
    }

    //挂载函数
    onMounted(() => {
        //向后端发送请求
        axios.post('http://localhost:8080/api/jobposition/departments/jobs', {
            userId: userStore.userId
        }).then((res) => {
            console.log(`output->res`, res)
            //修改左边的树形结构数据
            data.value = res.data
            //使用第一个默认的数据
            clickJobName.value = data.value[0].children[0].jobName
            allResumeInfo.forEach(element => {
                console.log(`output->elment--job`, element)
                if (element.jobName == clickJobName.value) {
                    console.log(`output->这是我选择出来的`, element.resumeWithJobInfos)
                    resumeInfoList.value = element.resumeWithJobInfos
                }
            });
        })

        //获取所有含有岗位信息的简历
        axios.get('http://localhost:8080/api/jobposition/allResumeWithJobInfo', {
            params: {
                userId: userStore.userId
            }
        }).then((res) => {
            console.log(`output->res`, res)
            //确保简历特性最多为3个

            //修改右边的简历信息
            allResumeInfo = res.data
            allResumeInfo.forEach(element => {
                element.resumeWithJobInfos.forEach((element) => {
                    if (element.characteristics.length > 3) {
                        element.characteristics = element.characteristics.slice(0, 3)
                    }
                })
            })
            //使用第一个默认的数据
            allResumeInfo.forEach(element => {
                console.log(`output->elment--getresume`, element)

                if (element.jobName == clickJobName.value) {
                    console.log(`output->这是我选择出来的`, element)
                    resumeInfoList.value = element.resumeWithJobInfos
                }
            });
        })
    })



    // 文件格式
    // [
    //         {
    //             name: '平面设计部',
    //             jobNumber: 2,
    //             children: [
    //                 { name: '平面设计师', weekJobResumes: 8, allJobResume: 40 },
    //                 { name: 'UI设计师', weekJobResumes: 2, allJobResume: 10 },
    //             ],
    //         }
    //     ]
</script>

<style scoped>
    .no-resume {
        color: #ff6f61;
        /* 设置文本颜色 */
        background: linear-gradient(135deg, #fff3f3, #ffe6e6);
        /* 设置渐变背景颜色 */
        padding: 15px 20px;
        /* 设置内边距 */
        border-radius: 10px;
        /* 设置圆角 */
        text-align: center;
        /* 设置文本居中 */
        font-size: 1.5em;
        /* 设置字体大小 */
        font-weight: bold;
        /* 设置字体加粗 */
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        /* 添加阴影效果 */
        transition: transform 0.3s ease;
        /* 添加过渡效果 */
    }

    .no-resume:hover {
        transform: scale(1.05);
        /* 鼠标悬停时放大 */
    }

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
        margin-bottom: 10px;
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