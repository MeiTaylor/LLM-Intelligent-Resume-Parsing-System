<template>
    <div class="chart-container" ref="chart">
        <el-scrollbar height="750px">
            <img id="image" class="image" :src="imageSrc" alt="图片">
        </el-scrollbar>
    </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios';

export default {
    name: 'getResumeFromBackend',
    mounted() {
        console.log("mounted")
    },
    computed: {
        imageSrc() {
            // console.log(this.userId);
            const sfile = "http://localhost:5177/api/Resume/getGraph?resumeId=" + this.RId
            console.log(sfile)
            return sfile
        }
    },
    props: ['RId'],//从父组件活得userId

};

</script>

<style scoped>
.image {
    width: 100%;
    height: auto;
}
</style>