<template>
    <div class="chart-container" ref="chart"></div>
</template>

<script>
    //待解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'ChartWorkStability',
        mounted() {
            this.renderChart();
        },
        props: ['userId'],//从父组件活得userId
        methods: {
            renderChart() {
                const chartDom = this.$refs.chart;
                const myChart = echarts.init(chartDom);
                var option;


                axios.get('http://localhost:8080/api/visualization/workStability', { params: { userId: this.userId } })
                    .then(response => {
                        console.log(response);
                        var data = response.data;

                        var needData = [
                            { value: 1048, name: '非常不稳定' },
                            { value: 735, name: '较不稳定' },
                            { value: 580, name: '正常稳定' },
                            { value: 484, name: '比较稳定' },
                            { value: 300, name: '很稳定' },
                            {
                                // make an record to fill the bottom 50%
                                value: 1048 + 735 + 580 + 484 + 300,
                                itemStyle: {
                                    // stop the chart from rendering this piece
                                    color: 'none',
                                    decal: {
                                        symbol: 'none'
                                    }
                                },
                                label: {
                                    show: false
                                }
                            }
                        ]
                        var sum = 0;
                        //用来动态连接前端页面和后端的数组
                        for (var i = 0; i < 5; i++) {

                            if (needData[i].name === '非常不稳定') needData[i].value = data.low
                            if (needData[i].name === '较不稳定') needData[i].value = data.mediumLow
                            if (needData[i].name === '正常稳定') needData[i].value = data.medium
                            if (needData[i].name === '比较稳定') needData[i].value = data.mediumHigh
                            if (needData[i].name === '很稳定') needData[i].value = data.high
                            sum = sum + needData[i].value
                        }

                        needData[5].value = sum;





                        option = {
                            title: {
                                text: '工作稳定程度分布图',
                            },
                            tooltip: {
                                trigger: 'item'
                            },
                            legend: {
                                top: '13%',
                                left: 'center',
                                // doesn't perfectly work with our tricks, disable it
                                selectedMode: false
                            },
                            series: [
                                {
                                    name: 'Access From',
                                    type: 'pie',
                                    radius: ['40%', '60%'],
                                    center: ['50%', '60%'],
                                    // adjust the start angle
                                    startAngle: 180,
                                    label: {
                                        show: true,
                                        formatter(param) {
                                            // correct the percentage
                                            return param.name + ' (' + param.percent * 2 + '%)';
                                        }
                                    },
                                    data: needData
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
        height: 400px;
    }
</style>