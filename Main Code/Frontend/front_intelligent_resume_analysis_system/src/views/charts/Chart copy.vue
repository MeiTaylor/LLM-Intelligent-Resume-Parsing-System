<template>
    <div id="chart" style="width:100%; height: 400px;"></div>
</template>
<!-- 瀑布图  用来展示所有岗位以及其对应的简历数量 -->
<script>
    //已解决
    import * as echarts from 'echarts';
    import axios from 'axios';
    export default {
        name: 'Chart',
        mounted() {
            this.initChart();
        },
        props: ['userId'],
        // data:{
        //     xAxisData:['1', '10'],
        //     EmptyData:
        // },
        methods: {
            initChart() {

                // let xAxisData = ['1', '10']//数组名称
                // let EmptyData = [2, 2]//空柱的高度
                // let RealData = [3, 3]//真实柱的高度

                // xAxisData.push('3'),
                //     EmptyData.push(4)
                // RealData.push(5)

                // xAxisData.push('totalResumes')
                // EmptyData.push(0)
                // RealData.push(responseData.totalResumes)


                //获取后端对应的数据
                axios.post('http://localhost:5177/api/Resume/graph/total', { id: this.userId })//此时的userid需要外界传入
                    .then(response => {
                        console.log(response.data)
                        let xAxisData = []//数组名称
                        let EmptyData = []//空柱的高度
                        let RealData = []//真实柱的高度
                        const responseData = response.data;
                        //添加第一行的
                        xAxisData.push('totalResumes')
                        EmptyData.push(0)
                        RealData.push(responseData.totalResumes)

                        //全部简历的数量
                        var totalResumes = responseData.totalResumes

                        var jobResumeCounts = responseData.jobResumeCounts;//获取具体的岗位简历数组
                        //遍历数组
                        jobResumeCounts.forEach((element) => {
                            xAxisData.push(element.jobName)
                            RealData.push(element.resumeCount)
                            let myEmptyData = 2 * totalResumes
                            RealData.forEach((e) => { myEmptyData = myEmptyData - e })
                            //myEmptyData = myEmptyData - element.resumeCount
                            EmptyData.push(myEmptyData)
                            console.log(myEmptyData)
                        })

                        const chartDom = document.getElementById('chart');
                        const myChart = echarts.init(chartDom);
                        const option = {
                            title: {
                                text: '岗位简历分布图',//图表名字
                                subtext: '展示每个岗位下有多少简历'//图表名字描述
                            },
                            tooltip: {
                                trigger: 'axis',
                                axisPointer: {
                                    type: 'shadow'
                                },
                                formatter: function (params) {
                                    var tar = params[1];
                                    return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
                                }
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '13%',
                                containLabel: true
                            },
                            xAxis: {
                                type: 'category',
                                splitLine: { show: false },
                                data: xAxisData,//数组名称
                                axisLabel: {
                                    fontSize: 11 // 设置字体大小
                                }
                            },
                            toolbox: {
                                show: true,
                                feature: {
                                    mark: { show: true },
                                    saveAsImage: { show: true }
                                }
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [
                                {
                                    name: 'Placeholder',
                                    type: 'bar',
                                    stack: 'Total',
                                    itemStyle: {
                                        borderColor: 'transparent',
                                        color: 'transparent'
                                    },
                                    emphasis: {
                                        itemStyle: {
                                            borderColor: 'transparent',
                                            color: 'transparent'
                                        }
                                    },
                                    data: EmptyData           //
                                },
                                {
                                    name: '简历数',
                                    type: 'bar',
                                    stack: 'Total',
                                    label: {
                                        show: true,
                                        position: 'inside'
                                    },
                                    data: RealData
                                }
                            ]
                        };

                        option && myChart.setOption(option);
                    })
                    .catch(error => {
                        console.error('请求失败：', error);
                    });

                //     console.log(xAxisData)
                //     console.log(EmptyData)
                //     console.log(RealData)

                //     const chartDom = document.getElementById('chart');
                //     const myChart = echarts.init(chartDom);
                //     const option = {
                //         title: {
                //             text: 'Waterfall Chart',//图表名字
                //             subtext: 'Living Expenses in Shenzhen'//图表名字描述
                //         },
                //         tooltip: {
                //             trigger: 'axis',
                //             axisPointer: {
                //                 type: 'shadow'
                //             },
                //             formatter: function (params) {
                //                 var tar = params[1];
                //                 return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
                //             }
                //         },
                //         grid: {
                //             left: '3%',
                //             right: '4%',
                //             bottom: '3%',
                //             containLabel: true
                //         },
                //         xAxis: {
                //             type: 'category',
                //             splitLine: { show: false },
                //             data: xAxisData,//数组名称
                //             axisLabel: {
                //                 fontSize: 11 // 设置字体大小
                //             }
                //         },
                //         yAxis: {
                //             type: 'value'
                //         },
                //         series: [
                //             {
                //                 name: 'Placeholder',
                //                 type: 'bar',
                //                 stack: 'Total',
                //                 itemStyle: {
                //                     borderColor: 'transparent',
                //                     color: 'transparent'
                //                 },
                //                 emphasis: {
                //                     itemStyle: {
                //                         borderColor: 'transparent',
                //                         color: 'transparent'
                //                     }
                //                 },
                //                 data: EmptyData           //
                //             },
                //             {
                //                 name: 'Life Cost',
                //                 type: 'bar',
                //                 stack: 'Total',
                //                 label: {
                //                     show: true,
                //                     position: 'inside'
                //                 },
                //                 data: RealData
                //             }
                //         ]
                //     };

                //     option && myChart.setOption(option);
                // }
            }
        }
    }

</script>

<style scoped>
    /* 根据需要添加样式 */
</style>