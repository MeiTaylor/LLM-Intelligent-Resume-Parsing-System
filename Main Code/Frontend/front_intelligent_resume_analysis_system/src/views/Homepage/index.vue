<template>
    <div class="home" style="background-color: #F0F2F5; height: 1080px;">
        <div style="height: 10px;">
        </div>

        <el-row :gutter="10" style="height: 180px;">

            <el-col :span="12">
                <ElCard style="height: 95%;margin-left: 10px;">
                    <el-row :gutter="50" style="height: 50px;">
                        <el-col :span="4" :offset="4">
                            <svg-icon icon-class="documentation"
                                style="height: 50%;width: 220%; margin-top: 20px;"></svg-icon>
                        </el-col>
                        <el-col :span="10" :offset="4">
                            <div class="card-panel-text"> 简历数量</div>
                            <div class="card-panel-num">
                                {{ resumeNum }}
                            </div>
                        </el-col>
                        <!-- 简历数量 -->

                    </el-row>
                </ElCard>
            </el-col>

            <el-col :span="12">
                <ElCard style="height: 95%;">
                    <el-row :gutter="50" style="height: 50px;">
                        <el-col :span="4" :offset="4">
                            <svg-icon icon-class="people" style="height: 50%;width: 220%; margin-top: 20px;"></svg-icon>
                        </el-col>
                        <el-col :span="10" :offset="4">
                            <div class="card-panel-text"> 岗位数量</div>
                            <div class="card-panel-num">
                                {{ totalJobs }}
                            </div>
                        </el-col>
                        <!-- 简历数量 -->

                    </el-row>
                </ElCard>
            </el-col>

        </el-row>

        <el-row :gutter="10" style="height: 430px;">
            <el-col :span="24">
                <ElCard style="height: 95%;margin-left: 10px;">
                    <!-- 时序折线图 -->
                    <chart-new-add :userId="userStore.userId"></chart-new-add>
                </ElCard>
            </el-col>
        </el-row>

        <el-row :gutter="10" style="height: 400px;">
            <el-col :span="12">
                <ElCard style="height: 95%; margin-left: 10px;">
                    <template #header>
                        <span style="font-weight: bold; font-size: 18px;"> 简历查看历史记录 </span>
                    </template>
                    <!-- 岗位添加历史记录 -->
                    <el-row>
                        <el-col :span="24">
                            <ElTable :data="resumeList" :border="false" style="width: 100%;" max-height="300px"
                                :show-header="true">
                                <el-table-column prop="name" label="姓名" />
                                <el-table-column prop="age" label="年龄" />
                                <el-table-column prop="highestEducation" label="最高学历" />
                                <el-table-column prop="jobIntention" label="求职意向" />
                            </ElTable>

                        </el-col>

                    </el-row>
                </ElCard>
            </el-col>
            <el-col :span="12">
                <ElCard style="height: 95%;">
                    <Chart :userId="userStore.userId"></Chart>
                </ElCard>
            </el-col>
        </el-row>

    </div>
</template>

<script setup>
    import { ref, computed, onMounted } from 'vue';
    import { ElRow, ElCol, ElUpload, ElTable, ElPagination, ElInput, ElSelect, ElForm, ElButton, ElDrawer, ElOption } from 'element-plus';
    import axios from 'axios';
    import Cookies from 'js-cookie';
    import { getResumeParser, getAllResumeParser } from '../../api/resume';
    import { useUser } from '../../stores/user';
    import { useFileStore } from '../../stores/file';
    import { useResumeDetail } from '../../stores/resume';
    import ChartNewAdd from '../charts/ChartNewAdd.vue';
    import Chart from '../charts//Chart.vue';

    const userStore = useUser();
    const fileStore = useFileStore();
    const resumeDetailsStore = useResumeDetail();

    const resumeList = ref([]);
    const drawerResume = ref(false);
    const drawerJobInfo = ref(false);
    const detail = ref({});
    const totalJobs = ref(0);
    const resumeNum = ref(0);
    const isLoading = ref(false);

    const eduSelect = ref('');
    const ageSelect = ref('');
    const genderSelect = ref('');
    const matchingScoreSelect = ref('');
    const nameSelect = ref('');
    const jobIntensionSelect = ref('');

    const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
    `;

    const getAllResumeList = async () => {
        try {
            const response = await getAllResumeParser({ id: userStore.userId });
            resumeList.value = response.simpleResumes;
        } catch (error) {
            console.error(error);
        }
    };

    const search = () => {
        let filteredList = [...resumeDetailsStore.ResumeList];

        if (eduSelect.value) {
            filteredList = filteredList.filter(item => item.highestEducation === eduSelect.value);
        }
        if (ageSelect.value) {
            filteredList = filteredList.filter(item => item.age < ageSelect.value);
        }

        resumeList.value = filteredList;
    };

    const showDetail = async (rid) => {
        try {
            const response = await getResumeParser({ rId: rid });
            resumeDetailsStore.Resumedetail = response.detailedResume;
            detail.value = resumeDetailsStore.Resumedetail;
            Cookies.set('Last-detailed-resume', JSON.stringify(detail.value));
        } catch (err) {
            console.error(err);
        }
    };

    const onClose = () => {
        drawerResume.value = false;
    };

    onMounted(async () => {
        console.log(userStore.roles);
        await getAllResumeList();

        try {
            const res = await axios.post('http://localhost:5177/api/Home/statistics', { id: userStore.userId });
            resumeNum.value = res.data.totalResumes;
            totalJobs.value = res.data.totalJobs;
            console.log(resumeNum.value);
        } catch (error) {
            console.error(error);
        }
    });
</script>


<style scoped lang="scss">
    .docWrap {
        width: 100%;

        .Doc {
            width: 100%;
        }
    }

    :deep(.docx-wrapper) {
        width: 50%;
    }

    :deep(.docx-wrapper > section-docx) {
        width: 100%;
    }

    .card-panel-text {
        padding-top: 25px;
        font-size: 24px;
        color: gray;
    }

    .card-panel-num {
        padding-top: 25px;
        font-size: 32px;
        font-weight: bold;
    }
</style>