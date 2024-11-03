<template>
    <div class="chart-container" ref="chart"></div>
</template>

<script>
    //已解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'ChartAgeGroups',
        mounted() {
            this.renderChart();
        },
        props: ['userId'],//从父组件活得userId
        methods: {
            renderChart() {
                const chartDom = this.$refs.chart;
                const myChart = echarts.init(chartDom);
                var option;
                axios.post('http://localhost:5177/api/Resume/graph/ageInfo', { id: this.userId })
                    .then(response => {
                        console.log(response);
                        var data = response.data.ageGroups;

                        var ageData = [];
                        //将ageData与那个连接起来
                        ageData.push(data.age18_22)
                        ageData.push(data.age22_25)
                        ageData.push(data.age25_30)
                        ageData.push(data.age30_35)
                        ageData.push(data.age35AndAbove)



                        option = {
                            title: {
                                text: '年龄分布图',
                                subtext: '所有投递简历者的年龄分布柱状图'
                            },
                            xAxis: {
                                type: 'category',
                                data: ['18-22', '22-25', '25-30', '30-35', '35以上']
                            },
                            yAxis: {
                                type: 'value'
                            },
                            tooltip: {
                                trigger: 'axis',
                                axisPointer: {
                                    type: 'shadow'
                                },

                            },
                            toolbox: {
                                show: true,
                                feature: {
                                    mark: { show: true },
                                    saveAsImage: { show: true }
                                }
                            },
                            series: [
                                {
                                    data: ageData,
                                    type: 'bar',
                                    showBackground: true,
                                    backgroundStyle: {
                                        color: 'rgba(180, 180, 180, 0.2)'
                                    }
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