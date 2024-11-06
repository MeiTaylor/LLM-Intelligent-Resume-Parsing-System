<!-- 人才画像界面 -->
<template>
    <div class="home" style="background-color: #F0F2F5;height: 800px;">
        <div style="height: 10px;"></div>
        <el-row :gutter="10" style="height:800px">
            <el-col :span="11" style="height: 100%;">
                <ElCard style="height: 99%;margin-left: 5px;">
                    <el-row>
                        <el-col :span="4">
                            <el-switch v-model="isGraph"
                                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" />
                        </el-col>
                        <el-col :span="20">
                            <span>显示文字/图片</span>
                        </el-col>
                    </el-row>
                    <div v-if="isGraph">
                        <getResumeFromBackend :RId="this.$route.params.id" />
                    </div>
                    <div v-else>
                        <!-- 组件, 展示文字 -->
                        <resumeDetails :RId="this.$route.params.id"></resumeDetails>
                    </div>
                </ElCard>
            </el-col>
            <el-col :span="13">

                <el-scrollbar height="800px">
                    <el-row>
                        <el-col :span="2">
                            <el-switch v-model="isChat"
                                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" />
                        </el-col>
                        <el-col span="20">
                            <span>人才画像/chat</span>
                        </el-col>
                    </el-row>
                    <div v-if="isChat">
                        <!-- 聊天界面 -->
                        <ElCard style="margin-right:20px ;">
                            <chat style="height: 640px;"></chat>

                        </ElCard>


                    </div>
                    <div v-else>
                        <!-- 人才画像界面 -->
                        <el-row :gutter="10" style="height:650px">
                            <el-col :span="12" style="height: 100%;">
                                <ElCard style="height: 98%;margin-left: 5px;">
                                    <ChartAchievementsAndHighlights :userId="this.RID"></ChartAchievementsAndHighlights>
                                </ElCard>
                            </el-col>
                            <el-col :span="12" style="height: 100%;">
                                <ElCard style="height: 98%;">
                                    <ChartCharacteristics :userId="this.RID"></ChartCharacteristics>
                                </ElCard>
                            </el-col>
                        </el-row>

                        <el-row :gutter="10" style="height:650px">
                            <el-col :span="12" style="height: 100%;">
                                <ElCard style="height: 98%;margin-left: 5px;">
                                    <ChartJobMatchResult :userId="this.RID"></ChartJobMatchResult>
                                </ElCard>
                            </el-col>
                            <el-col :span="12" style="height: 100%;">
                                <ElCard style="height: 98%;">
                                    <ChartSkillsAndExperiences :userId="this.RID"></ChartSkillsAndExperiences>
                                </ElCard>
                            </el-col>
                        </el-row>
                    </div>

                </el-scrollbar>
            </el-col>
        </el-row>

        <!-- <el-row :gutter="10" style="height:600px;margin-top: 10px;">
            <el-col :span="24">
                <ElCard style="height: 100%;margin-left: 5px;">
                    简历信息
                </ElCard>

            </el-col>
        </el-row> -->

    </div>
</template>

<script>

    import Chart from '../charts/Chart.vue';

    import { useUser } from '../../stores/user';
    import { useFileStore } from '../../stores/file'
    import { useResumeDetail } from '../../stores/resume'

    import pinia from '../../stores';
    import { mapState, mapStores } from 'pinia';


    import ChartAchievementsAndHighlights from '../charts/ChartAchievementsAndHighlights.vue'
    import ChartCharacteristics from '../charts/ChartCharacteristics.vue'
    import ChartJobMatchResult from '../charts/ChartJobMatchResult.vue'
    import ChartSkillsAndExperiences from '../charts/ChartSkillsAndExperiences.vue'

    import getResumeFromBackend from '../charts/getResumeFromBackend.vue'
    import resumeDetails from './ResumeComponent/resumeDetails.vue'
    import chat from './ResumeComponent/chat.vue'
    export default {
        data() {
            return {
                RID: this.$route.params.id,
                isGraph: true,
                isChat: false,
            };
        },
        computed: {
            // ...mapState()
            ...mapStores(useFileStore, useResumeDetail, useUser)
        },
        mounted() {
            // console.log(this.$route.params)
        },
        methods: {},
        components: { chat, Chart, ChartAchievementsAndHighlights, ChartCharacteristics, ChartJobMatchResult, ChartSkillsAndExperiences, getResumeFromBackend, resumeDetails }
    }

</script>