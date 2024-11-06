<template>
    <div class="container">
        <el-scrollbar height="700px">
            <ElRow>
                <ElCol :span="24">
                    <div class="card-warpper">
                        <ElForm :model="resumeDetails">
                            <ElCard :header="titles[0]" style="margin-bottom: 30px;">




                                <!-- 基本信息 -->
                                <el-row>
                                    <span>姓名: </span>
                                    <el-text class="text" margin-bottom="10px">{{ resumeDetails.name }}</el-text>
                                </el-row>
                                <el-row>
                                    <span>年龄: </span>
                                    <el-text class="text" margin-bottom="10px">{{ resumeDetails.age }}</el-text>
                                </el-row>


                                <el-row>
                                    <span>性别: </span>
                                    <el-text class="text" margin-bottom="10px">{{ resumeDetails.gender }}</el-text>
                                </el-row>

                                <el-row>
                                    <span>电子邮箱: </span>
                                    <el-text class="text" margin-bottom="10px">{{ resumeDetails.email }}</el-text>
                                </el-row>

                                <el-row>

                                    <span>联系电话: </span>
                                    <el-text class="text" margin-bottom="10px">{{ resumeDetails.phoneNumber }}</el-text>
                                </el-row>


                                <el-row>
                                    <span>最高学历: </span>
                                    <el-text class="text" margin-bottom="10px">{{ resumeDetails.highestEducation }}</el-text>
                                </el-row>

                            </ElCard>

                            <ElCard :header="titles[1]" style="margin-bottom: 30px;">
                                <!-- 工作经历 -->
                                <template v-for="(item, index) in resumeDetails.workExperience" :key="index">
                                    <el-row>
                                        <span>公司: </span>
                                        <el-text class="text" margin-bottom="10px">{{ item.companyName }}</el-text>
                                    </el-row>
                                    <el-row>
                                        <span>职位: </span>
                                        <el-text class="text" margin-bottom="10px">{{ item.position }}</el-text>
                                    </el-row>
                                    <el-row>
                                        <span>时间: </span>
                                        <el-text class="text" margin-bottom="10px">{{ item.time }}</el-text>
                                    </el-row>
                                    <el-row>
                                        <span>具体经历: </span>
                                        <el-text class="text" margin-bottom="10px">{{ item.task }}</el-text>
                                    </el-row>

                                </template>

                            </ElCard>

                            <ElCard :header="titles[2]" style="margin-bottom: 30px;">
                                <!-- 教育背景 -->
                                <template v-for="(item, index) in resumeDetails.educationBackgrounds" :key="index">
                                    <el-row>
                                        <span>时间: </span>
                                        <el-text class="text">{{ item.time }}</el-text>
                                    </el-row>
                                    <el-row>
                                        <span>学校: </span>
                                        <el-text class="text">{{ item.school }}</el-text>
                                    </el-row>

                                    <template v-if="item.major !== null">
                                        <el-row>
                                            <span>专业: </span>
                                            <el-text class="text">{{ item.task }}</el-text>
                                        </el-row>

                                    </template>
                                </template>
                            </ElCard>

                            <ElCard :header="titles[3]" style="margin-bottom: 30px;">
                                <!-- 获奖情况 -->
                                <el-row v-for="(item, index) in resumeDetails.awards" :key="index">
                                    <el-text class="text">
                                        {{ item.awardName }}
                                    </el-text>
                                    <br>
                                </el-row>
                            </ElCard>

                            <ElCard :header="titles[4]" style="margin-bottom: 30px;">
                                <!-- 技能 -->
                                <el-row v-for="(item, index) in resumeDetails.skillCertificate" :key="index">
                                    <el-text class="text">
                                        {{ item.skillName }}
                                    </el-text>
                                    <br>
                                </el-row>

                            </ElCard>

                            <ElCard :header="titles[6]" style="margin-bottom: 30px;">
                                <!-- 自我评价 -->
                                <el-text class="text">{{ resumeDetails.selfEvaluation }}</el-text>
                            </ElCard>

                            <ElFormItem>
                                <ElButton type="primary" @click="onSubmit"> 保存修改 </ElButton>
                                <ElButton> 取消 </ElButton>
                            </ElFormItem>

                        </ElForm>

                    </div>
                </ElCol>

            </ElRow>
        </el-scrollbar>
    </div>
</template>

<script>
import { ElCard } from 'element-plus';
// import axios from 'axios'
import { getResumeParser } from '../../../api/resume'

export default {
    name: 'ResumeDetail',
    data() {
        return {
            titles: ['基本信息', '工作经历', '教育背景', '获奖情况', '技能', '求职信息', '自我评价', 'AI分析'],
            resumeDetails: {}
        }
    },
    props: ['RId'],
    components: { ElCard },
    mounted() {
        getResumeParser({ id: this.RId }).then((res) => {
            console.log(res)
            this.resumeDetails = res
        })
    }
}
</script>

<style scoped>
/* 
.text{
    height: 20px;
} */
</style>