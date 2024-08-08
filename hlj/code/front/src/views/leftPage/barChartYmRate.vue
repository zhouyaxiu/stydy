<template>
	<!-- 柱图-疫苗接种率-->
	<div id="barChartShotRateArea" class="chartBg" :style="'width:'+props.width+'px;height:'+props.height+'px;'">
		<div id="barChartShotRate"></div>
	</div>
</template>

<script setup>
	import {ref,onMounted,getCurrentInstance,watch,onUnmounted,nextTick,markRaw} from "vue";
	// import * as echarts from 'echarts'
  	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;

	const oldWidth=window.oldWidth
	const oldScale=window.oldScale
	const bl1=globalProxy.$util.checkIsWideFlag(window.bl)

	const xData=ref([])
	const yData=ref([])
	const showDataZoom=ref(false)
	const myChart=ref(null)
	const chartOption=ref({})
	const splitNumber=ref(9)
	const fontSize=ref(12)
	const maxValueSpan=ref(4)

	const props = defineProps({
		width: {
			type: Number,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		},
	})

	const lastHeight=ref(props.height)

	watch(props.height, (val, oldVal) => {
		if(oldWidth==1280){
			lastHeight.value = props.height + 20
		}
		if(oldWidth==1366 || oldWidth==1360){
			lastHeight.value = props.height + 32
		}
		if(oldWidth==1440 || oldWidth==1400){
			lastHeight.value = props.height + 35
		}
	},{immediate:true})

	const initChart=()=> {
		let chart = document.getElementById('barChartShotRate');
		let chartArea = document.getElementById('barChartShotRateArea');
		var resizeWorldMapContainer = function() {
			chart.style.width = chartArea.clientWidth + 'px';
			chart.style.height = chartArea.clientHeight + 'px';
		};
		resizeWorldMapContainer();
		// myChart.value = echarts.init(chart);
		myChart.value = markRaw(globalProxy.$echarts.init(chart));
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			grid: {
				left: 70,
				top: 12,
				right: 40,
				bottom: 30
			},
			tooltip: {
				show: true,
				trigger: 'axis',
				backgroundColor: 'rgba(0,0,0,0)',
				borderWidth:0,
				axisPointer: {
					type: 'none',
					lineStyle: {
						color: '#D2D4EA',
						width: 1,
						type: 'dashed'
					}
				},
				formatter: function(params) {
					let name = params[0].name;
					let val = globalProxy.$util.numFormat(params[0].value);
					let content = "<div class='comm-chart-tip-area'>";
						content += "<div class='oneRow'><div class='text'>"+name+'</div></div>';
						content += "<div class='oneRow'>接种率：<div class='text'>"+val+'%</div></div>';
					content += "</div>";
					return content;
				}
			},
			xAxis: [{
				name: '',
				type: 'value',
				axisLabel: {
					interval: 0,
					showMaxLabel: true,
					textStyle: {fontSize: fontSize.value, color: '#7cc5f7'},
					formatter: function(value,index){
						return value+'%'
					}
				},
				splitLine: {show: true, lineStyle: {color: '#295c88', type:'dotted', width:1}},
				axisLine: {
					show: false,
				},
				axisTick: {
					show: false
				},
				min: 0,
				max: 100,
				minInterval: 1,
				splitNumber: splitNumber.value,
			}],
			yAxis: [{
				type: 'category',
				axisLine:{
					lineStyle: {color: ['#295c88'], type:'solid', width:1, fontSize: fontSize.value}
				},
				axisTick:{show: false},
				axisLabel:{
					showMinLabel: true,
					textStyle: {fontSize: fontSize.value, color: '#7cc5f7'},
					formatter: function(value,index){
						return value
					}
				},
				data: xData.value,
			}],
			series: [{
					name: '接种单位数量',
					type: 'bar',
					data: yData.value,
					barWidth: 13,
					itemStyle: { 
						normal:{
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 0, 1, 1,[{offset: 0, color: '#134065'}, {offset: 1, color: '#44e6b1'}]),
						}
					},
				}
			],
			dataZoom: {
				show: showDataZoom.value,
				realtime: true, //是否实时更新
				type: 'slider',
				start: 1,
				end: 100,
				yAxisIndex: [0],
				filterMode: 'filter',
				maxValueSpan: maxValueSpan.value, //窗口的大小，显示数据的条数的
				handleSize: 0,//滑动条的 左右2个滑动条的大小
				height: '90%',//组件高度
				width: 8,
				right: 10,
				top: 5,
				bottom: 15,
				brushSelect:false,//是否开启刷性功能
				backgroundColor:"transparent",
				fillerColor: '#004E95',
				borderColor: "rgba(0,0,0,0)",
				// handleIcon: "M-292,322.2c-3.2,0-6.4-0.6-9.3-1.9c-2.9-1.2-5.4-2.9-7.6-5.1s-3.9-4.8-5.1-7.6c-1.3-3-1.9-6.1-1.9-9.3c0-3.2,0.6-6.4,1.9-9.3c1.2-2.9,2.9-5.4,5.1-7.6s4.8-3.9,7.6-5.1c3-1.3,6.1-1.9,9.3-1.9c3.2,0,6.4,0.6,9.3,1.9c2.9,1.2,5.4,2.9,7.6,5.1s3.9,4.8,5.1,7.6c1.3,3,1.9,6.1,1.9,9.3c0,3.2-0.6,6.4-1.9,9.3c-1.2,2.9-2.9,5.4-5.1,7.6s-4.8,3.9-7.6,5.1C-285.6,321.5-288.8,322.2-292,322.2z",
				// handleSize: "100%",
				// handleStyle: {color: "#004E95"},
				backgroundColor: 'rgba(0,0,0,0)',//两边未选中的滑动条区域的颜色
				showDataShadow: false,//是否显示数据阴影 默认auto
				showDetail: false,//即拖拽时候是否显示详细数值信息 默认true
				orient: 'vertical',
			}				
		};
		myChart.value.clear();
		myChart.value.hideLoading();
		myChart.value.setOption(chartOption.value);
	}

	const addChartData=(dataList)=>{
		xData.value = [];
		yData.value = [];
		showDataZoom.value = false;
		if(dataList && dataList.length>0){
			dataList.forEach(item => {
				xData.value.push(item.vaccinationName);
				yData.value.push(item.rate);
			});
		}else{
			xData.value = [];
			yData.value = [];
		}
		if(xData.value.length>5){
			showDataZoom.value = true;
		}
		chartOption.value.dataZoom.show = showDataZoom.value;
		chartOption.value.yAxis[0].data = xData.value;
		chartOption.value.series[0].data = yData.value;
		myChart.value.clear();
		myChart.value.hideLoading();
		myChart.value.setOption(chartOption.value, true);
	}

	const showChartLoading=()=>{
		myChart.value.showLoading({
			text: 'loading',
			color: '#c23531',
			textColor: '#fff',
			maskColor: 'rgba(255, 255, 255, 0.2)',
			zlevel: 0,
		});
	}	

	onMounted(()=> {
		splitNumber.value = 9
		fontSize.value = 12*oldScale
		fontSize.value = fontSize.value>13?13:fontSize.value;
		if(!bl1 || oldWidth==1680 || oldWidth==1600){
			splitNumber.value = 5
		}
		if(oldScale<0.9){
		}
		if(oldWidth == 1366){
			splitNumber.value = 5
			maxValueSpan.value = 4
		}
		nextTick(async () => {
			initChart()
		})
	})

	onUnmounted(()=>{
		myChart.value = null;
	})

	// 公开数据
	defineExpose({
		addChartData
	})
</script>
<style scoped>

</style>