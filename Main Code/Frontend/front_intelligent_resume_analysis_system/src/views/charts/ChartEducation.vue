<template>
    <div class="chart-container" id="main"></div>
</template>

<script>
    //已解决
    import * as echarts from 'echarts';
    import axios from 'axios';

    export default {
        name: 'ChartEducation',
        mounted() {
            this.renderChart();
        },
        props: ['userId'],
        methods: {
            renderChart() {
                axios.post('http://localhost:5177/api/Resume/graph/education', { id: this.userId })//此时的userid需要外界传入
                    .then(response => {

                        var data = response.data;

                        console.log(response.data)
                        var chartDom = document.getElementById('main');
                        var myChart = echarts.init(chartDom);
                        var option;

                        var GraduacatedSchool = [
                            { value: 1548, name: '985' },
                            { value: 775, name: '211' },
                            { value: 679, name: '普通一本' },
                            { value: 679, name: '一本以下' }
                        ]

                        console.log(data.graduationSchoolsLevel)
                        //给上面的数组动态获得后端的数据
                        for (var i = 0; i < 4; i++) {
                            if (GraduacatedSchool[i].name === '985') GraduacatedSchool[i].value = data.graduationSchoolsLevel._985;
                            if (GraduacatedSchool[i].name === '211') GraduacatedSchool[i].value = data.graduationSchoolsLevel._211;
                            if (GraduacatedSchool[i].name === '普通一本') GraduacatedSchool[i].value = data.graduationSchoolsLevel.ordinaryFirstClass;
                            if (GraduacatedSchool[i].name === '一本以下') GraduacatedSchool[i].value = data.graduationSchoolsLevel.secondClassOrBelow;

                        }


                        var highestEudcation = [
                            { value: 1048, name: '博士' },
                            { value: 335, name: '硕士' },
                            { value: 310, name: '本科' },
                            { value: 251, name: '大专' },
                            { value: 234, name: '高中以及以下' }
                        ]

                        console.log(data.highestEducation)
                        //给上面数组动态赋值
                        for (var i = 0; i < 5; i++) {
                            if (highestEudcation[i].name === '博士') highestEudcation[i].value = data.highestEducation.doctor;
                            if (highestEudcation[i].name === '硕士') highestEudcation[i].value = data.highestEducation.master;
                            if (highestEudcation[i].name === '本科') highestEudcation[i].value = data.highestEducation.bachelor;
                            if (highestEudcation[i].name === '大专') highestEudcation[i].value = data.highestEducation.juniorCollege;
                            if (highestEudcation[i].name === '高中以及以下') highestEudcation[i].value = data.highestEducation.highSchoolOrLess;
                        }


                        option = {
                            title: {
                                // top: '5%',
                                text: '学历分布图'
                            },
                            tooltip: {
                                trigger: 'item',
                                formatter: '{a} <br/>{b}: {c} ({d}%)'
                            },
                            // grid: {
                            //     left: '50%',
                            //     top: '10%',
                            //     right: '0%',
                            //     bottom: '10%'
                            // },
                            legend: {
                                top: '10%',
                                data: [
                                    '985',
                                    '211',
                                    '普通一本',
                                    '一本以下',
                                    '博士',
                                    '硕士',
                                    '本科',
                                    '大专',
                                    '高中以及以下'
                                ],
                                itemWidth: 22,  // 设置图例项的宽度
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
                                    name: '毕业院校',
                                    type: 'pie',
                                    selectedMode: 'single',
                                    radius: [0, '30%'],
                                    center: ['50%', '65%'],
                                    label: {
                                        position: 'inner',
                                        fontSize: 14
                                    },
                                    labelLine: {
                                        show: false
                                    },
                                    data: GraduacatedSchool
                                },
                                {
                                    name: '最高学历',
                                    type: 'pie',
                                    radius: ['45%', '60%'],
                                    center: ['50%', '65%'],
                                    labelLine: {
                                        length: 30
                                    },
                                    label: {
                                        formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                                        backgroundColor: '#F6F8FC',
                                        borderColor: '#8C8D8E',
                                        borderWidth: 1,
                                        borderRadius: 4,
                                        rich: {
                                            a: {
                                                color: '#6E7079',
                                                lineHeight: 22,
                                                align: 'center'
                                            },
                                            hr: {
                                                borderColor: '#8C8D8E',
                                                width: '100%',
                                                borderWidth: 10,
                                                height: 0
                                            },
                                            b: {
                                                color: '#4C5058',
                                                fontSize: 14,
                                                fontWeight: 'bold',
                                                lineHeight: 33
                                            },
                                            per: {
                                                color: '#fff',
                                                backgroundColor: '#4C5058',
                                                padding: [3, 4],
                                                borderRadius: 4
                                            }
                                        }
                                    },
                                    data: highestEudcation
                                }
                            ]
                        };

                        option && myChart.setOption(option);



                    })
                    .catch(error => {
                        console.error('请求失败：', error);
                    });
            }
        }
    }
</script>

<style scoped>
    .chart-container {
        width: 100%;
        height: 400px;
    }
</style>