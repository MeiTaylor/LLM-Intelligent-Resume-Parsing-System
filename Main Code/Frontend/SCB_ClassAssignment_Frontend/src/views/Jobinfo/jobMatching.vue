<template>
    <div>
        人岗匹配详情界面
    </div>
</template>

<script>
    import { ElRow, ElTable } from 'element-plus';
    import axios from 'axios'

    import { useUser } from '../../stores/user';
    import { useFileStore } from '../../stores/file'
    import { useResumeDetail } from '../../stores/resume'

    import pinia from '../../stores';
    import { mapState, mapStores } from 'pinia';


    export default {
        data() {
            return {
                allJob: [],
                matchResume: [],
                showJobDetais: false,
                jobinfo: null,
                activeItem: '',

                exampleScore: 5
            }
        },
        computed: {
            ...mapStores(useUser, useFileStore, useResumeDetail),
            scoreFixed(score) {
                return 0
            }
        },
        mounted() {
            axios.post('http://localhost:5177/api/Job/allJobNameOrdered', { id: this.userStore.userId }).then((res) => {
                this.allJob = res.data.allJobNames
                this.activeItem = this.$route.params.jid
                console.log(this.$route.params.jid)

                this.$nextTick(() => {
                    console.log(document.getElementById('k' + this.$route.params.jid).childNodes[0].childNodes[0])
                    document.getElementById('k' + this.$route.params.jid).childNodes[0].childNodes[0].click()
                    // this.clickItem(parseInt(this.activeItem))
                })
            }).catch((err) => {
                console.log(err)
            })

        },
        methods: {
            clickItem(jid) {
                // 岗位详细信息
                axios.post('http://localhost:5177/api/Job/Jobinfo', { id: jid }).then((res) => {
                    console.log(res)
                    this.jobinfo = res.data
                })
                axios.post('http://localhost:5177/api/Job/Match', { userId: this.userStore.userId, jobId: jid }).then((res) => {
                    console.log(res.data)
                    this.matchResume = res.data.matches
                    this.showJobDetais = true
                })
            },
            clickResume(row, column, event) {
                console.log(row, column, event)
                this.$router.push({ path: `/talent-profile/${row.resumeId}` })
            },
            getBack() {
                this.$router.push({ path: '/job-info' })
            }
        },
        components: { ElTable }
    }
</script>

<style scoped>
    :deep(.el-collapse-item__header) {
        font-size: 16px;
        font-weight: bold;
    }
</style>