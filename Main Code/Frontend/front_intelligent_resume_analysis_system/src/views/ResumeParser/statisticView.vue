<template>
    <div class="home" style="background-color: #F0F2F5; height: 1080px;">
        <div style="height: 10px;" class="zhanwei"></div>

        <el-row :gutter="10" style="height: 180px;">
            <el-col :span="12">
                <ElCard style="height: 95%; margin-left: 10px;">
                    <el-row :gutter="50" style="height: 50px;">
                        <el-col :span="4" :offset="4">
                            <svg-icon icon-class="documentation"
                                style="height: 50%; width: 220%; margin-top: 20px;"></svg-icon>
                        </el-col>
                        <el-col :span="10" :offset="4">
                            <div class="card-panel-text"> 简历数量</div>
                            <div class="card-panel-num">{{ resumeNum }}</div>
                        </el-col>
                    </el-row>
                </ElCard>
            </el-col>

            <el-col :span="12">
                <ElCard style="height: 95%;">
                    <el-row :gutter="50" style="height: 50px;">
                        <el-col :span="4" :offset="4">
                            <svg-icon icon-class="people"
                                style="height: 50%; width: 220%; margin-top: 20px;"></svg-icon>
                        </el-col>
                        <el-col :span="10" :offset="4">
                            <div class="card-panel-text"> 岗位数量</div>
                            <div class="card-panel-num">{{ totalJobs }}</div>
                        </el-col>
                    </el-row>
                </ElCard>
            </el-col>
        </el-row>

        <el-row :gutter="10" style="height: 400px;">
            <el-col :span="8">
                <ElCard style="height: 90%; margin-left: 5px;">
                    <ChartAgeGroups :userId="userStore.userId" />
                </ElCard>
            </el-col>
            <el-col :span="8">
                <ElCard style="height: 90%;">
                    <Chart :userId="userStore.userId" />
                </ElCard>
            </el-col>
            <el-col :span="8">
                <ElCard style="height: 90%;">
                    <ChartWorkStability :userId="userStore.userId" />
                </ElCard>
            </el-col>
        </el-row>

        <el-row :gutter="10" style="height: 500px;">
            <el-col :span="12">
                <ElCard style="height: 100%; margin-left: 5px;">
                    <ChartMatch :userId="userStore.userId" />
                </ElCard>
            </el-col>
            <el-col :span="12">
                <ElCard style="height: 100%; margin-left: 5px;">
                    <ChartEducation :userId="userStore.userId" />
                </ElCard>
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
    import { ref, onMounted } from 'vue';
    import { useUser } from '../../stores/user';
    import { useFileStore } from '../../stores/file';
    import { useResumeDetail } from '../../stores/resume';
    import axios from 'axios';

    // 图表组件
    import ChartAgeGroups from '../charts/ChartAgeGroups.vue';
    import ChartEducation from '../charts/ChartEducation.vue';
    import ChartWorkStability from '../charts/ChartWorkStability.vue';
    import ChartMatch from '../charts/ChartMatchScores.vue';
    import Chart from '../charts/Chart.vue';

    const userStore = useUser();
    const fileStore = useFileStore();
    const resumeDetailsStore = useResumeDetail();

    const resumeNum = ref(0);
    const totalJobs = ref(0);

    onMounted(async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/home/statistics', { userId: userStore.userId });
            resumeNum.value = response.data.totalResumes;
            totalJobs.value = response.data.totalJobs;
            console.log(resumeNum.value);
        } catch (error) {
            console.error(error);
        }
    });
</script>

<style>
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