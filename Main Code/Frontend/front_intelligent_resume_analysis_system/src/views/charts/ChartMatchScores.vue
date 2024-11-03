<template>
    <div class="chart-container" ref="chart"></div>
</template>

<script>
    //已解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'ChartMatchScores',
        mounted() {
            this.renderChart();
        },
        props: ['userId'],//从父组件活得userId
        methods: {
            renderChart() {
                const chartDom = this.$refs.chart;
                const myChart = echarts.init(chartDom);
                var option;
                axios.post('http://localhost:5177/api/Resume/graph/JobMatchScore', { id: this.userId })
                    .then(response => {
                        console.log(response.data);
                        var data = response.data.jobMatchScores;
                        //连接前端的数组
                        var needData = [
                            { value: 6, name: '60分以下' },
                            { value: 28, name: '60-70' },
                            { value: 26, name: '70-80' },
                            { value: 24, name: '80-90' },
                            { value: 22, name: '90-100' }
                        ]

                        //将这个数组的与后端传输过来的数据对应起来
                        for (var i = 0; i < 5; i++) {
                            if (needData[i].name === '60分以下') needData[i].value = data.below60;
                            if (needData[i].name === '60-70') needData[i].value = data.range60_70;
                            if (needData[i].name === '70-80') needData[i].value = data.range70_80;
                            if (needData[i].name === '80-90') needData[i].value = data.range80_90;
                            if (needData[i].name === '90-100') needData[i].value = data.range90_100;

                        }

                        option = {
                            title: {
                                text: '人岗匹配得分分布',
                                subtext: '人岗匹配得分分布南丁格尔玫瑰图',
                                left: 'center'
                            },
                            tooltip: {
                                trigger: 'item',
                                formatter: '{a} <br/>{b} : {c} ({d}%)'
                            },
                            legend: {
                                left: 'center',
                                top: '14%',
                                data: [
                                    '60分以下',
                                    '60-70',
                                    '70-80',
                                    '80-90',
                                    '90-100',
                                ]
                            },
                            toolbox: {
                                show: true,
                                feature: {
                                    mark: { show: true },
                                    dataView: { show: true, readOnly: false },
                                    restore: { show: true },
                                    saveAsImage: { show: true }
                                }
                            },

                            series: [
                                {
                                    name: '人岗匹配得分',
                                    type: 'pie',
                                    radius: [20, 140],
                                    center: ['50%', '60%'],
                                    roseType: 'area',
                                    itemStyle: {
                                        borderRadius: 5
                                    },
                                    data: needData
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