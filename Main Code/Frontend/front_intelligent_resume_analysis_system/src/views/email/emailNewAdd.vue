<template>
    <div class="chart-container" ref="chart"></div>
</template>

<script>
    //待解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'emailNewAdd',
        mounted() {
            this.renderChart();
        },
        props: ['userId'],//从父组件活得userId
        methods: {
            // renderChart() {
            //     const chartDom = this.$refs.chart;
            //     const myChart = echarts.init(chartDom);
            //     var option;

            //     // 模拟测试

            //     var testData = {
            //         "resumeCounts": [
            //             { "date": "2021-07-01", "count": 0 },
            //             { "date": "2021-07-02", "count": 0 },
            //             { "date": "2021-07-03", "count": 0 },
            //             { "date": "2021-07-04", "count": 0 },
            //             { "date": "2021-07-05", "count": 0 },
            //             { "date": "2021-07-06", "count": 0 },
            //             { "date": "2021-07-07", "count": 0 }
            //         ],
            //     }
            //     var data = testData
            //     console.log(data)
            //     var xData = []//x坐标
            //     var ResumeAddData = []//的图表

            //     //先获取对应的x坐标数据
            //     for (var i = 0; i < data.resumeCounts.length; i++) {
            //         xData.push(data.resumeCounts[i].date)
            //         ResumeAddData.push(data.resumeCounts[i].count)

            //     }

            //     //全部逆序
            //     xData.reverse()
            //     ResumeAddData.reverse();

            //     //然后找到每一个x坐标对应的y数据


            //     option = {
            //         title: {
            //             text: '一周内邮箱接收的简历数'
            //         },
            //         tooltip: {
            //             trigger: 'axis'
            //         },
            //         legend: {},
            //         toolbox: {
            //             show: true,
            //             feature: {
            //                 dataZoom: {
            //                     yAxisIndex: 'none'
            //                 },
            //                 dataView: { readOnly: false },
            //                 magicType: { type: ['line', 'bar'] },
            //                 restore: {},
            //                 saveAsImage: {}
            //             }
            //         },
            //         xAxis: {
            //             type: 'category',
            //             boundaryGap: false,
            //             data: xData
            //         },
            //         yAxis: {
            //             type: 'value',
            //         },
            //         series: [
            //             {
            //                 name: '新增简历数',
            //                 type: 'line',
            //                 data: ResumeAddData
            //             },

            //         ]
            //     };

            renderChart() {
                const chartDom = this.$refs.chart;
                const myChart = echarts.init(chartDom);
                var option;
                axios.get('http://localhost:5177/api/Email/CountLastWeek?userId=1')
                    .then((res) => {
                        // console.log(response);

                        console.log(`output->res`, res)
                        // var testData =
                        // {
                        //     "resumeCounts": [
                        //         { "date": "2021-07-01", "count": 0 },
                        //         { "date": "2021-07-02", "count": 0 },
                        //         { "date": "2021-07-03", "count": 0 },
                        //         { "date": "2021-07-04", "count": 0 },
                        //         { "date": "2021-07-05", "count": 0 },
                        //         { "date": "2021-07-06", "count": 0 },
                        //         { "date": "2021-07-07", "count": 0 }
                        //     ],
                        // }
                        var data = res.data
                        console.log(`output->aaaadata`, data)
                        var displayData = []
                        for (let key in data) {
                            if (data.hasOwnProperty(key)) {  // 确保 key 是 jsonObject 自身的属性，而不是原型链上的属性
                                // 封装data
                                var sonData = { date: key.slice(0, 10), count: data[key] }
                                displayData.push(sonData)

                            }
                        }
                        displayData = {
                            "resumeCounts": displayData
                        }

                        console.log(`output->displayData`, displayData)

                        data = displayData
                        console.log(data)
                        var xData = []//x坐标
                        var ResumeAddData = []//的图表

                        //先获取对应的x坐标数据
                        for (var i = 0; i < data.resumeCounts.length; i++) {
                            xData.push(data.resumeCounts[i].date)
                            ResumeAddData.push(data.resumeCounts[i].count)

                        }

                        //全部逆序
                        // xData.reverse()
                        // ResumeAddData.reverse();

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

                            ]
                        };


                        option && myChart.setOption(option);


                    })
                    .catch(error => {
                        console.error('请求失败：', error);
                    });



                option && myChart.setOption(option);
            }




            //     option && myChart.setOption(option);
            // }
            // 真实接口


            //     axios.post('https://www.fastmock.site/mock/e444a789312a567a964a0e08bd956a55/api/api/emailaddresume', { id: this.userId })
            //         .then(response => {
            //             console.log(response);
            //             testData =
            //             {
            //                 "resumeCounts": [
            //                     { "date": "2021-07-01", "count": 0 },
            //                     { "date": "2021-07-02", "count": 0 },
            //                     { "date": "2021-07-03", "count": 0 },
            //                     { "date": "2021-07-04", "count": 0 },
            //                     { "date": "2021-07-05", "count": 0 },
            //                     { "date": "2021-07-06", "count": 0 },
            //                     { "date": "2021-07-07", "count": 0 }
            //                 ],
            //             }
            //             var data = response.data
            //             console.log(data)
            //             var xData = []//x坐标
            //             var ResumeAddData = []//的图表

            //             //先获取对应的x坐标数据
            //             for (var i = 0; i < data.jobCounts.length; i++) {
            //                 xData.push(data.jobCounts[i].date)
            //                 ResumeAddData.push(data.resumeCounts[i].count)

            //             }

            //             //全部逆序
            //             xData.reverse()
            //             ResumeAddData.reverse();

            //             //然后找到每一个x坐标对应的y数据


            //             option = {
            //                 title: {
            //                     text: '一周内新增的岗位数和简历数'
            //                 },
            //                 tooltip: {
            //                     trigger: 'axis'
            //                 },
            //                 legend: {},
            //                 toolbox: {
            //                     show: true,
            //                     feature: {
            //                         dataZoom: {
            //                             yAxisIndex: 'none'
            //                         },
            //                         dataView: { readOnly: false },
            //                         magicType: { type: ['line', 'bar'] },
            //                         restore: {},
            //                         saveAsImage: {}
            //                     }
            //                 },
            //                 xAxis: {
            //                     type: 'category',
            //                     boundaryGap: false,
            //                     data: xData
            //                 },
            //                 yAxis: {
            //                     type: 'value',
            //                 },
            //                 series: [
            //                     {
            //                         name: '新增简历数',
            //                         type: 'line',
            //                         data: ResumeAddData
            //                     },

            //                 ]
            //             };


            //             option && myChart.setOption(option);


            //         })
            //         .catch(error => {
            //             console.error('请求失败：', error);
            //         });



            //     option && myChart.setOption(option);
            // }
        }
    };
</script>

<style scoped>
    .chart-container {
        width: 100%;
        height: 300px;
    }
</style>