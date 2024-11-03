<template>
    <div class="chart-container" ref="chart"></div>
</template>

<script>
    //待解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'ChartNewAdd',
        mounted() {
            this.renderChart();
        },
        props: ['userId'],//从父组件活得userId
        methods: {
            renderChart() {


                const chartDom = this.$refs.chart;
                const myChart = echarts.init(chartDom);
                var option;
                axios.post('http://localhost:5177/api/Home/statistics', { id: this.userId })
                    .then(response => {
                        console.log(response);

                        var data = response.data.weeklyStates
                        console.log(data)
                        var xData = []//x坐标
                        var ResumeAddData = []//简历的图表
                        var JobAddData = []//岗位的图表

                        //先获取对应的x坐标数据
                        for (var i = 0; i < data.jobCounts.length; i++) {
                            xData.push(data.jobCounts[i].date)
                            JobAddData.push(data.jobCounts[i].count);//改变的是job对应的
                            ResumeAddData.push(data.resumeCounts[i].count)

                        }

                        //全部逆序
                        xData.reverse()
                        JobAddData.reverse()
                        ResumeAddData.reverse();

                        //然后找到每一个x坐标对应的y数据


                        option = {
                            title: {
                                text: '一周内新增的岗位数和简历数'
                            },
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {},
                            toolbox: {
                                show: true,
                                feature: {
                                    dataZoom: {
                                        yAxisIndex: 'none'
                                    },
                                    dataView: { readOnly: false },
                                    magicType: { type: ['line', 'bar'] },
                                    restore: {},
                                    saveAsImage: {}
                                }
                            },
                            xAxis: {
                                type: 'category',
                                boundaryGap: false,
                                data: xData
                            },
                            yAxis: {
                                type: 'value',
                            },
                            series: [
                                {
                                    name: '新增简历数',
                                    type: 'line',
                                    data: ResumeAddData
                                },
                                {
                                    name: '新增岗位数',
                                    type: 'line',
                                    data: JobAddData
                                }
                            ]
                        };


                        option && myChart.setOption(option);


                    })
                    .catch(error => {
                        console.error('请求失败：', error);
                    });



                option && myChart.setOption(option);
            }
        }
    };
</script>

<style scoped>
    .chart-container {
        width: 100%;
        height: 400px;
    }
</style>