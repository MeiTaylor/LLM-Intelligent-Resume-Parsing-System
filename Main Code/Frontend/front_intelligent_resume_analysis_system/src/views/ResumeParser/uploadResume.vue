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
    </div>
</template>

<script setup>
    import { ref, computed } from 'vue'
    import axios from 'axios'
    import { onMounted } from 'vue'
    import { useUser } from '../../stores/user';
    import { useRouter } from 'vue-router'
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
        // console.log(`output->userStore.id`, userStore.userId)
        //TODO: 这里的接口地址需要改成后端的接口地址
        const res = await axios.post('https://mock.presstime.cn/mock/67275d10caf0b4e52f13f169/resume/all/resumes', {
            userId: userStore.userId
        })
        resumeInfoList.value = res.data
        resumeInfoSearchList.value = res.data
        console.log(`output->res`, res.data)
    })

    //函数
    const gotoDetailResume = (resumeId) => {
        console.log(`output->resumeId`, resumeId)
        // console.log(`output->router`, router)
        //进行页面跳转
        router.push('/talent-profile/' + resumeId)
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