<template>
    <div>
        <!-- 上传简历界面和简历库界面结合在一起 -->
        <el-row :gutter="10">
            <el-col :span="6" style="height:fit-content;">
                <el-card style="margin-left: 10px; margin-top: 10px;">
                    <el-select v-model="selectJobId" placeholder="选择岗位" style="width: 100%;margin-bottom: 7px;">
                        <el-option v-for="job in allJobList" :key="job.id" :label="job.name" :value="job.id" />
                    </el-select>
                    <el-upload class="upload-demo" drag action="#" :on-error="handleUploadError"
                        :before-upload="beforeUpload">
                        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                        <div class="el-upload__text">
                            拖动 或者 <em>点击上传简历</em>
                        </div>
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
                                placeholder="年龄小于X检索" />
                        </el-col>
                        <el-col :span="8">
                            <el-select v-model="searchByEducation" class="m-2" placeholder="按学历检索" size="large">
                                <el-option v-for="item in EducationOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </el-col>
                        <el-col :span="8">
                            <el-input-number v-model="searchByScore" style="width: 75%;height: 40px;" :min=" 0"
                                :max="100" placeholder="匹配分数大于X检索" />
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
                            <el-button type="primary" style="width: 55%;" @click="search()">搜索</el-button>
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

                <div style="height: 370px;overflow-y: scroll;">
                    <el-card v-for="resumeInfo in resumeInfoSearchList" class="match-card" shadow="always">
                        <el-row :gutter="10">
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
                            <el-col :span="4" style="align-content: center;">
                                <el-button type="warning" round
                                    @click="gotoDetailResume(resumeInfo.resumeId)">查看详细简历</el-button>
                            </el-col>
                        </el-row>

                    </el-card>
                </div>
            </el-card>
        </div>
        <el-drawer v-model="sencondemodify" title="简历修正" :direction="direction" style="margin-bottom: 0px;"
            :before-close="handleClose" size="90%">
            <el-row :gutter="5">
                <el-col :span="12" style="background-color: #5a8255; min-height: 600px;max-height: 700px;">

                </el-col>

                <el-col :span="12">
                    <el-card style="margin-left: 5px;margin-right: -10px;">
                        <template #header>
                            <div class="card-header">
                                <span>解析的简历信息</span>
                            </div>
                        </template>

                        <el-form :model="modifyResumeInfo" label-width="auto"
                            style="max-width: 600px;height: 540px;overflow-y: scroll;">
                            <el-form-item label="姓名" class="modify-input">
                                <el-input v-model="modifyResumeInfo.name" />
                            </el-form-item>
                            <el-form-item label="性别" class="modify-input">
                                <el-input v-model="modifyResumeInfo.gender" />
                            </el-form-item>
                            <el-form-item label="年龄" class="modify-input">
                                <el-input v-model="modifyResumeInfo.age" />
                            </el-form-item>
                            <el-form-item label="学历" class="modify-input">
                                <el-input v-model="modifyResumeInfo.highestEducation" />
                            </el-form-item>
                            <el-form-item label="毕业院校" class="modify-input">
                                <el-input v-model="modifyResumeInfo.graduatedFrom" />
                            </el-form-item>
                            <el-form-item label="学校层级" class="modify-input">
                                <el-input v-model="modifyResumeInfo.graduatedFromLevel" />
                            </el-form-item>
                            <el-form-item label="意向岗位" class="modify-input">
                                <el-input v-model="modifyResumeInfo.jobIntention" />
                            </el-form-item>
                            <el-form-item label="电话号码" class="modify-input">
                                <el-input v-model="modifyResumeInfo.phoneNumber" />
                            </el-form-item>
                            <el-form-item label="邮箱" class="modify-input">
                                <el-input v-model="modifyResumeInfo.email" />
                            </el-form-item>
                            <el-form-item label="个人陈述" prop="desc">
                                <el-input v-model="modifyResumeInfo.selfEvaluation" type="textarea" />
                            </el-form-item>
                            <el-form-item label="工作年限" class="modify-input">
                                <el-input v-model="modifyResumeInfo.totalWorkYears" />
                            </el-form-item>
                            <el-form-item label="工作稳定性" class="modify-input">
                                <el-input v-model="modifyResumeInfo.workStability" />
                            </el-form-item>
                            <el-form-item label="工作稳定性原因" prop="desc" class="modify-input">
                                <el-input v-model="modifyResumeInfo.workStabilityReason" type="textarea" />
                            </el-form-item>
                            <el-form-item v-for="(award, index) in modifyResumeInfo.awards" :key="award.id"
                                :label="'曾获奖项' + (index + 1)" class="modify-input">
                                <el-input v-model="award.awardName" placeholder="请输入奖项名称"
                                    @input="updateAward(index, award.awardName)" />
                            </el-form-item>
                            <div v-for="(experience, index) in modifyResumeInfo.workExperiences" :key="experience.id"
                                class="experience-item">
                                <h4 style="margin-top: 5px;margin-bottom: 5px;color: #606266;">工作经历 {{ index + 1 }}</h4>
                                <el-form-item label="工作地点" class="modify-input">
                                    <!-- 地点输入框 -->
                                    <el-input v-model="experience.地点" placeholder="请输入地点"
                                        @input="updateExperience(index, '地点', experience.地点)" />
                                </el-form-item>

                                <el-form-item label="工作职位" class="modify-input">
                                    <!-- 职位输入框 -->
                                    <el-input v-model="experience.职位" placeholder="请输入职位"
                                        @input="updateExperience(index, '职位', experience.职位)" />
                                </el-form-item>


                                <el-form-item label="工作时间" class="modify-input">
                                    <!-- 时间输入框 -->
                                    <el-input v-model="experience.时间" placeholder="请输入时间"
                                        @input="updateExperience(index, '时间', experience.时间)" />
                                </el-form-item>

                                <el-form-item label="工作任务" class="modify-input">
                                    <!-- 任务输入框 -->
                                    <el-input type="textarea" v-model="experience.任务" placeholder="请输入任务"
                                        @input="updateExperience(index, '任务', experience.任务)" />
                                </el-form-item>


                            </div>
                            <el-form-item v-for="(skillCertificate, index) in modifyResumeInfo.skillCertificates"
                                :key="skillCertificate.id" :label="'所有技能' + (index + 1)" class="modify-input">
                                <el-input v-model="skillCertificate.skillName" placeholder="请输入技能名称"
                                    @input="updateSkillCertificate(index, skillCertificate.skillName)" />
                            </el-form-item>

                        </el-form>

                        <template #footer>
                            <el-button type="warning" @click="handleClose">
                                关闭
                            </el-button>
                            <el-button type="primary" @click="submitForm()">
                                修改
                            </el-button>
                        </template>

                    </el-card>
                </el-col>
            </el-row>
        </el-drawer>
    </div>
</template>

<script setup>
    import { UploadFilled } from '@element-plus/icons-vue'
    import { ref, computed, reactive } from 'vue'
    import axios from 'axios'
    import { onMounted } from 'vue'
    import { useUser } from '../../stores/user';
    import { useRouter } from 'vue-router'
    import { ElMessage } from 'element-plus'
    const manpicter = ref(new URL('@/assets/images/man.jpg', import.meta.url))
    const womanpicter = ref(new URL('@/assets/images/women.jpg', import.meta.url))
    const router = useRouter()
    const userStore = useUser()

    //所有ref变量
    const resumeInfoSearchList = ref([])
    const resumeInfoList = ref([])
    const searchByAge = ref()
    const searchByEducation = ref('')
    const searchByScore = ref()
    const searchByName = ref('')
    const searchByJobIntention = ref('')
    const EducationOptions = ref([
        { value: '本科', label: '本科' },
        { value: '硕士', label: '硕士' },
        { value: '博士', label: '博士' },
        { value: '大专', label: '大专' },
        { value: '高中', label: '高中' },
        { value: '初中', label: '初中' },
    ])

    const sencondemodify = ref(false)
    const selectJobId = ref()
    const modifyResumeInfo = ref()

    const allJobList = ref()

    //实现搜索功能
    const search = () => {
        var searchList = resumeInfoList.value
        //年龄
        if (searchByAge.value) {
            searchList = searchList.filter((item) => {
                return item.age < searchByAge.value
            })
        }

        //学历
        if (searchByEducation.value && searchByEducation.value !== '') {
            searchList = searchList.filter((item) => {
                return item.education === searchByEducation.value
            })
        }

        //匹配分数
        if (searchByScore.value) {
            searchList = searchList.filter((item) => {
                return item.score > searchByScore.value
            })
        }

        //姓名
        if (searchByName.value && searchByName.value !== '') {
            searchList = searchList.filter((item) => {
                console.log(`output->item.name`, item.name)
                return item.name.includes(searchByName.value)

            })
        }

        //求职意向
        if (searchByJobIntention.value && searchByJobIntention.value !== '') {
            searchList = searchList.filter((item) => {

                return item.JobIntention.includes(searchByJobIntention.value)
            })
        }


        resumeInfoSearchList.value = searchList
    }


    //开局调用接口更新界面
    onMounted(async () => {
        console.log(`output->userStore.id`, userStore.userId)
        //获取所有的岗位
        axios.get('http://localhost:8080/api/jobposition/allJobNamesAndIds', {
            params: {
                userId: userStore.userId
            }
        }).then((res) => {
            console.log(`output->res`, res)
            allJobList.value = res.data
        })
        //TODO: 这里的接口地址需要改成后端的接口地址
        // const res = await axios.post('https://mock.presstime.cn/mock/67275d10caf0b4e52f13f169/resume/all/resumes', {
        //     userId: userStore.userId
        // })
        // resumeInfoList.value = res.data
        // resumeInfoSearchList.value = res.data
        // console.log(`output->res`, res.data)
    })


    //函数
    const gotoDetailResume = (resumeId) => {
        console.log(`output->resumeId`, resumeId)
        // console.log(`output->router`, router)
        //进行页面跳转
        router.push('/talent-profile/' + resumeId)
    }

    //上传文件之前的钩子
    const beforeUpload = (file) => {
        if (selectJobId.value === undefined) {
            ElMessage.error('请先选择岗位')
            return false
        }
        console.log(`output->file`, file)
        const formData = new FormData()
        formData.append('file', file)
        formData.append('jobId', selectJobId.value)
        formData.append('userId', userStore.userId)
        formData.forEach((value, key) => console.log(key, value));  // 检查 FormData 内容
        ElMessage.success('上传成功,正在解析简历')
        // formData['file'] = file
        console.log(`output->formData`, formData)
        axios.post('http://localhost:8080/api/resume/uploadResume', formData).then((res) => {
            console.log(`output->res`, res)
            modifyResumeInfo.value = res.data
            ElMessage.success('简历解析成功')
            sencondemodify.value = true
        })
        return false;
    }

    // 更新奖项名称
    const updateAward = (index, name) => {
        modifyResumeInfo.awards[index].awardName = name;
    };

    // 更新技能名称
    const updateSkillCertificate = (index, name) => {
        modifyResumeInfo.skillCertificates[index].skillName = name;
    };

    // 更新工作经历信息
    const updateExperience = (index, field, value) => {
        modifyResumeInfo.workExperiences[index][field] = value;
    };


    // 上传文件失败
    const handleUploadError = () => {
        console.log(`output->上传文件失败`)
    }

    const handleUploadSuccess = () => {
        console.log(`output->上传文件失败`)
    }

    //TODO:后面需要将这个接口发送到后端上传二次修改信息
    const submitForm = (ruleFormRef) => {
        console.log(`output->modifyResumeInfo`, modifyResumeInfo)
        console.log(`正在修改简历`)
    }

    //关闭抽屉
    const handleClose = () => {
        sencondemodify.value = false
    }



    // format:每个简历的信息格式
    // {
    //     resumeId: 1,
    //     name: '林一',
    //     gender: '男',
    //     age: 22,
    //     workExperience: 1,
    //     education: '本科',
    //     major: '计算机科学与技术',
    //     score: '90',
    //     JobIntention: '算法工程师',
    //     matchJob: '算法工程师',
    //     school: '武汉大学',
    //     characteristics: ['能力出众', '经验丰富', '工作稳定']
    // }

    // {
    //     "id": 3,
    //     "name": "张吉惟",
    //     "email": "zhangjiwei@163.com",
    //     "phoneNumber": "13899999999",
    //     "age": 25,
    //     "gender": "女",
    //     "jobIntention": "行政管理类",
    //     "highestEducation": "本科",
    //     "major": "国际经济与贸易",
    //     "graduatedFrom": "北京师范大学",
    //     "graduatedFromLevel": null,
    //     "selfEvaluation": "本人性格开朗、稳重、有活力，待人热情、真诚。有较强的组织能力、团体协作精神，较好的社交能力，善于处理各种人际关系。能迅速的适应各种环境，并融合其中。能把企业当作家庭，企业的财富就是我的财富，在努力为企业服务的过程中实现自身价值。",
    //     "totalWorkYears": 0,
    //     "workStability": "中等",
    //     "workStabilityReason": "张吉惟在过去2.5年的工作经历中，经历了3份不同的工作，虽然每份工作的时间不算长，但职位逐级上升，显示出她的能力和对新职位的适应能力，且每份工作都有相应的职责和成就，因此可以认为她的工作稳定性在中等水平。",
    //     "applicantProfile": {
    //         "id": 3,
    //         "jobMatches": [
    //             {
    //                 "id": 13,
    //                 "applicantProfileId": 0,
    //                 "职位名称": "产品运营/电商运营",
    //                 "人岗匹配程度分数": 30,
    //                 "人岗匹配的理由": "求职者有运营助理工作经验，但没有足够的电商背景与数据分析能力的相关证明。虽然在工作中可能涉及项目管理，但不足以满足至少2年的运营经验要求。"
    //             },
    //             {
    //                 "id": 14,
    //                 "applicantProfileId": 0,
    //                 "职位名称": "设计与创意",
    //                 "人岗匹配程度分数": 0,
    //                 "人岗匹配的理由": "求职者的背景与平面设计无关，没有提及相关工作经验和设计软件熟练程度，因此不满足此职位要求。"
    //             },
    //             {
    //                 "id": 15,
    //                 "applicantProfileId": 0,
    //                 "职位名称": "财务/风控专员",
    //                 "人岗匹配程度分数": 0,
    //                 "人岗匹配的理由": "求职者本科学历为国际经济与贸易，缺乏财务、会计或税收政策知识，也没有风控经验，因此不符合此职位的要求。"
    //             },
    //             {
    //                 "id": 16,
    //                 "applicantProfileId": 0,
    //                 "职位名称": "市场营销/项目主管",
    //                 "人岗匹配程度分数": 40,
    //                 "人岗匹配的理由": "求职者有相关助理的工作经验，但缺少明确的市场策划和拓展经验。虽然具备一定的项目管理经验，但不满足3年相关经验的要求。"
    //             },
    //             {
    //                 "id": 17,
    //                 "applicantProfileId": 0,
    //                 "职位名称": "技术开发",
    //                 "人岗匹配程度分数": 0,
    //                 "人岗匹配的理由": "求职者没有提及计算机专业背景或软件开发经验，因此完全不符合此职位的要求。"
    //             },
    //             {
    //                 "id": 18,
    //                 "applicantProfileId": 0,
    //                 "职位名称": "行政支持",
    //                 "人岗匹配程度分数": 80,
    //                 "人岗匹配的理由": "求职者有相关的工作经历，如总经理助理和部长助理，涉及日常管理和档案处理，且熟练使用办公软件，符合岗位要求。"
    //             }
    //         ],
    //         "workTraits": [
    //             {
    //                 "id": 10,
    //                 "applicantProfileId": 0,
    //                 "trait": "性格开朗"
    //             },
    //             {
    //                 "id": 11,
    //                 "applicantProfileId": 0,
    //                 "trait": "团队合作"
    //             },
    //             {
    //                 "id": 12,
    //                 "applicantProfileId": 0,
    //                 "trait": "组织能力"
    //             },
    //             {
    //                 "id": 13,
    //                 "applicantProfileId": 0,
    //                 "trait": "适应能力强"
    //             }
    //         ],
    //         "characteristics": [
    //             {
    //                 "id": 19,
    //                 "name": "自我驱动性",
    //                 "catagory": "个人特性",
    //                 "分数": 8,
    //                 "原因": "求职者在简历中提到能够把企业当作家庭，并在努力为企业服务的过程中实现自身价值，显示出强烈的自我驱动性和责任感。"
    //             },
    //             {
    //                 "id": 20,
    //                 "name": "适应能力",
    //                 "catagory": "个人特性",
    //                 "分数": 9,
    //                 "原因": "个人评价中提及能迅速适应各种环境并融合其中，表明其具有很强的适应能力，能够在多变的环境中保持稳定表现。"
    //             },
    //             {
    //                 "id": 21,
    //                 "name": "社交能力",
    //                 "catagory": "个人特性",
    //                 "分数": 8,
    //                 "原因": "自我评价中提及待人热情、真诚，并有较好的社交能力，这是人才在管理领域中必备的一个重要特质。"
    //             },
    //             {
    //                 "id": 22,
    //                 "name": "问题解决能力",
    //                 "catagory": "技能和经验",
    //                 "分数": 8,
    //                 "原因": "在担任多个助理职位期间，积极参与企业的各项管理，负责质量检查及控制，显示出较强的问题解决能力。"
    //             },
    //             {
    //                 "id": 23,
    //                 "name": "团队协作能力",
    //                 "catagory": "技能和经验",
    //                 "分数": 9,
    //                 "原因": "简历中提到的组织能力和团体协作精神，显示出求职者能够在团队中有效合作，并积极促进团队目标的达成。"
    //             },
    //             {
    //                 "id": 24,
    //                 "name": "创新思维",
    //                 "catagory": "技能和经验",
    //                 "分数": 7,
    //                 "原因": "通过参与推行ISO9001及ISO14000体系，展现出一定的创新思维和主动性，尽管没有具体的创新项目细节，但参与标准推行本身就是一种创新实践。"
    //             },
    //             {
    //                 "id": 25,
    //                 "name": "领导潜力",
    //                 "catagory": "成就和亮点评价",
    //                 "分数": 8,
    //                 "原因": "作为部长助理与总经理助理的经历显示出其在管理和引领团队方向方面的潜力，这对于未来的领导角色是一个良好的基础。"
    //             },
    //             {
    //                 "id": 26,
    //                 "name": "创新成果",
    //                 "catagory": "成就和亮点评价",
    //                 "分数": 7,
    //                 "原因": "获得多个奖项，如全国大学生英语竞赛一等奖及国家级一等奖学金，显示出在学术和专业领域的优秀表现，尽管没有具体的创新项目描述。"
    //             },
    //             {
    //                 "id": 27,
    //                 "name": "行业影响力",
    //                 "catagory": "成就和亮点评价",
    //                 "分数": 6,
    //                 "原因": "简历内容中未具体提及在行业内的影响力或贡献，但在机构中的职务显示出一定的行业参与度。"
    //             }
    //         ]
    //     },
    //     "awards": [
    //         {
    //             "id": 5,
    //             "awardName": "全国大学生英语竞赛一等奖"
    //         },
    //         {
    //             "id": 6,
    //             "awardName": "国家级一等奖学金"
    //         }
    //     ],
    //     "workExperiences": [
    //         {
    //             "id": 7,
    //             "地点": "",
    //             "职位": "部长助理",
    //             "时间": "2022.3 - 至今",
    //             "任务": "负责票证车间生产质量检查,控制,兼任企业内审员,推行ISO9001:2008及ISO14000体系并年审。"
    //         },
    //         {
    //             "id": 8,
    //             "地点": "",
    //             "职位": "总经理助理",
    //             "时间": "2021.2 - 2022.3",
    //             "任务": "根据公司的经营理念和发展战略制定公司各工作岗位的工作目标;负责制定及推进公司员工的培训、绩效、薪酬的管理。"
    //         },
    //         {
    //             "id": 9,
    //             "地点": "",
    //             "职位": "运营助理",
    //             "时间": "2020.2 - 2021.2",
    //             "任务": "根据目标管理综合考评内容，深入、细致地到所跟踪的各场所了解、掌握情况，帮助和促进各场所提高行政、后勤管理水平。"
    //         }
    //     ],
    //     "skillCertificates": [
    //         {
    //             "id": 13,
    //             "skillName": "EXCEL"
    //         },
    //         {
    //             "id": 14,
    //             "skillName": "PPT"
    //         },
    //         {
    //             "id": 15,
    //             "skillName": "WORD"
    //         },
    //         {
    //             "id": 16,
    //             "skillName": "AI"
    //         },
    //         {
    //             "id": 17,
    //             "skillName": "PS"
    //         },
    //         {
    //             "id": 18,
    //             "skillName": "大学英语6级证书（CET-6）"
    //         }
    //     ]
    // }

</script>

<style scoped>
    ::v-deep .el-card__header {

        padding-top: 10px;
        padding-bottom: 10px;
    }

    ::v-deep .el-card__footer {
        display: flex;
        justify-content: flex-end;
        padding-top: 5px;
        padding-bottom: 5px;
    }

    .modify-input {
        margin-left: 0px;
        margin-bottom: 7px;
        margin-top: 0px;
    }

    .experience-item {
        margin-left: 0px;
    }


    ::v-deep .el-drawer__header {
        margin-bottom: 0px;
    }

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