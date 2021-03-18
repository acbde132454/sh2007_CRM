<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/crm/jquery/echarts.min.js"></script>
    <script type="text/javascript" src="/crm/jquery/jquery-1.11.1-min.js"></script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="bar" style="width: 1000px;height:400px;margin: 20px auto"></div>
<div id="pie" style="width: 600px;height:400px;margin: 20px auto"></div>
<script type="text/javascript">
    $.get("/crm/workbench/transaction/chart",function (data) {
        // 基于准备好的dom，初始化echarts实例
        var pieChart = echarts.init(document.getElementById('pie'));
        var barChart = echarts.init(document.getElementById('bar'));
        //柱状图
        var option1 = {
            title: {
                text: '交易统计图表'
            },
            tooltip: {},
            legend: {
                data:['交易']
            },
            xAxis: {
                data: data.titles
            },
            yAxis: {},
            series: [{
                name: '交易',
                type: 'bar',
                data: data.counts
            }]
        };

        barChart.setOption(option1);
        // 饼状图:指定图表的配置项和数据
        var option2 = {
            title: {
                text: 'CRM交易阶段统计',
                subtext: '真实数据',
                left: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: data.titles
            },
            series: [
                {
                    name: '交易比例',
                    type: 'pie',
                    radius: '60%',
                    center: ['50%', '60%'],
                    data: data.charts,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        pieChart.setOption(option2);
    },'json');

</script>
</body>
</body>
</html>
