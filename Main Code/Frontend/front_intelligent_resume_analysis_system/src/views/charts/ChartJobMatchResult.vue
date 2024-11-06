<!-- 人岗匹配得分    -->
<template>
    <div class="chart-container" ref="chart"></div>
    <div>
        <el-table :data="tableData" style="width: 100% " height="250">
            <el-table-column prop="name" label="成就与亮点" width="100">
            </el-table-column>
            <el-table-column prop="score" label="分数" width="60px">
            </el-table-column>
            <el-table-column prop="reason" label="得分原因" width="400">
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    //待解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'ChartJobMatchResult',
        mounted() {
            this.renderChart();
        },
        data() {
            return {
                tableData: []
            }
        },
        props: ['userId'],//从父组件活得userId
        methods: {
            renderChart() {
                const chartDom = this.$refs.chart;
                const myChart = echarts.init(chartDom);
                var option;


                axios.post('http://localhost:5177/api/Resume/graph/PersonalJobMatchScore', { Id: this.userId })
                    .then(response => {
                        console.log(response.data);
                        var data = response.data;
                        console.log(data);

                        //将返回的数据赋予给data
                        for (var i = 0; i < 10; i++) {
                            this.tableData.push({ name: data[i].jobTitle, score: data[i].score, reason: data[i].reason })
                        }

                        var scoreData = []


                        //将这个动态赋值
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "产品运营") { scoreData.push(data[i].score); }
                        }
                        for (var i = 0; i < 10; i++) {

                            if (data[i].jobTitle === "平面设计师") { scoreData.push(data[i].score); continue; }
                        }
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "财务") { scoreData.push(data[i].score); continue; }

                        }
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "市场营销") { scoreData.push(data[i].score); }
                        }
                        for (var i = 0; i < 10; i++) {

                            if (data[i].jobTitle === "项目主管") { scoreData.push(data[i].score); continue; }
                        }
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "开发工程师") { scoreData.push(data[i].score); continue; }

                        }
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "文员") { scoreData.push(data[i].score); }
                        }
                        for (var i = 0; i < 10; i++) {

                            if (data[i].jobTitle === "电商运营") { scoreData.push(data[i].score); continue; }
                        }
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "人力资源管理") { scoreData.push(data[i].score); continue; }

                        }
                        for (var i = 0; i < 10; i++) {
                            if (data[i].jobTitle === "风控专员") { scoreData.push(data[i].score); continue; }

                        }
                        option = {
                            title: {
                                text: '人岗匹配得分'
                            },
                            grid: {
                                top: '0%',
                                bottom: '5%',
                                left: '10%',
                                right: '10%'
                            },
                            legend: {
                                //bottom: 'bottom',
                                top: "0%",
                                right: '5%',
                                // orient: 'horizontal',
                                // top: 'bottom',
                                // left: 'center',
                                data: ['人岗匹配得分']
                            },
                            radar: {
                                indicator: [
                                    { name: '产品运营', max: 100 },
                                    { name: '平面设计师', max: 100 },
                                    { name: '财务', max: 100 },
                                    { name: '市场营销', max: 100 },
                                    { name: '项目主管', max: 100 },
                                    { name: '开发工程师', max: 100 },
                                    { name: '文员', max: 100 },
                                    { name: '电商运营', max: 100 },
                                    { name: '人力资源管理', max: 100 },
                                    { name: '风控专员', max: 100 },

                                ],
                                center: ['50%', '50%'],

                                triggerEvent: true // 开启触发事件
                            },
                            tooltip: {
                                // 添加提示框配置
                                trigger: 'item',
                                // formatter: function(params) {
                                // var value = params.value; // 获取数据项的值
                                // // 根据需要进行处理和展示
                                // return '信息值: ' + value[1];
                                // }
                                axisPointer: {
                                    // 坐标轴指示器，坐标轴触发有效
                                    type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                                }
                            },
                            series: [
                                {
                                    //top: "20%",
                                    name: '人岗匹配',
                                    type: 'radar',
                                    data: [
                                        {
                                            value: scoreData,
                                            name: '人岗匹配得分'
                                        }
                                    ]
                                }
                            ]
                        };



                        option && myChart.setOption(option);
                    })
            }
        }

    };
</script>

<style scoped>
    .chart-container {
        width: 100%;
        height: 350px;
    }
</style>