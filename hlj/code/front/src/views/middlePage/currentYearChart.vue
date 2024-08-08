<template>
	<!-- 当年出生儿童建档数-->
	<div id="currentYearChartArea" class="chartBg1" :style="'width:'+props.width+'px;height:'+props.height+'px;'">
		<div id="currentYearChart"></div>
	</div>
</template>

<script setup>
	import {ref,getCurrentInstance,onMounted,onUnmounted,markRaw} from "vue"
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();

	const oldWidth=window.oldWidth
	const oldScale=window.oldScale
	const bl=window.bl
	const bl1=globalProxy.$util.checkIsWideFlag(window.bl)

	const myChart=ref(null)
	const chartOption=ref({})
	const xData=ref(['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'])
	const yData=ref([0,0,0,0,0,0,0,0,0,0,0,0])
	const fontSize=ref(12)
			
	const props=defineProps({
		width: {
			type: Number,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		},
	})

	const initChart=()=> {
		let chart = document.getElementById('currentYearChart');
		let chartArea = document.getElementById('currentYearChartArea');
		var resizeWorldMapContainer = function() {
			chart.style.width = chartArea.clientWidth + 'px';
			chart.style.height = chartArea.clientHeight + 'px';
		};
		resizeWorldMapContainer();
		myChart.value = markRaw(globalProxy.$echarts.init(chart));
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			grid: {
				left: 40*oldScale,
				top: 35,
				right: 18,
				bottom: 28
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
					let content = "<div class='comm-chart-tip-area'>";
						content += "<div class='oneRow'><div class='text'>"+name+'</div></div>';
						params.forEach(item=>{
							let val = item.value||'0';
							content += "<div class='oneRow'>"+item.seriesName+"：<div class='text'>"+globalProxy.$util.numFormat(val)+'</div></div>';
						});
					content += "</div>";
					return content;
				}
			},
			xAxis: {
				data: xData.value,
				type: 'category',
				axisLabel: {
					interval: 0,
					showMaxLabel: true,
					textStyle: {
						fontSize: fontSize.value,
						color: store.textStyleColor
					},
				},
				splitLine: {
					show: false,
					lineStyle: { color: "rgba(115, 194, 220, 0.3)", type: 'dashed', width: 1 }
				},
				axisLine: { show: false },
				axisTick: { show: false },
				boundaryGap: false,
			},
			yAxis: {
				name: '数量（万）',
				nameTextStyle: {
					padding: [0, 0, 0, -5],
					fontSize: fontSize.value,
					color:"#B0DFFF"
				},
				type: 'value',
				position: 'left',
				splitLine: {show: true, lineStyle: {color: '#295C88', type:'dotted', width:1}},
				axisLine:{
					lineStyle: {color: ['#94D2FC'], type:'dotted', width:0, fontSize: fontSize.value}
				},
				axisTick:{show: false},
				axisLabel:{
					showMinLabel: true,
					textStyle: {fontSize: fontSize.value, color: '#B0DFFF'},
					formatter: function(value,index){
						return value
					}
				},
				min: 0,
				minInterval: 1,
				splitNumber: 4,
			},
			series: [{
				name: '数量(万)',
				data: yData.value,
				type: "line",
				symbol: "circle",
				smooth: false,
				showAllSymbol: true,
				symbolSize: 4,
				itemStyle:{
					normal:{
						//折点颜色
						color:'#32E89D',
						borderColor: '#fff',
						lineStyle:{//折线颜色
							color: 'rgba(50,232,157,1)',
							shadowColor: 'rgba(0, 0, 0, .3)',
							shadowBlur: 0,
							shadowOffsetY: 1,
							shadowOffsetX: 1,
							width: 2
						}
					}
				},
				areaStyle: {
					normal: {
						color: new globalProxy.$echarts.graphic.LinearGradient(0, 1, 0, 0, [{ offset: 0, color: 'rgba(11,31,83, 0.1)' }, { offset: 1, color: 'rgba(50,232,157,1)' }], false),
						shadowColor: 'rgba(4,27,62, 0.1)',
						shadowBlur: 100,
					},
				},
			}],
		};
		myChart.value.clear();
		myChart.value.setOption(chartOption.value);
	}

	const addChartData=(dataList)=>{
		xData.value = [];
		yData.value = [];
		if(dataList && dataList.length>0){
			dataList.forEach(item => {
				xData.value.push(item.birthMonth+'月');
				yData.value.push(item.count/10000);
			});
		}else{
			xData.value = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
			yData.value = [0,0,0,0,0,0,0,0,0,0,0,0];
		}
		if(chartOption.value.xAxis || chartOption.value.series){
			chartOption.value.xAxis.data = xData.value;
			chartOption.value.series[0].data = yData;
			myChart.value.clear();
			myChart.value.hideLoading();
			myChart.value.setOption(chartOption.value, true);
		}
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
		fontSize.value = 12*oldScale
		fontSize.value = fontSize.value>13?13:fontSize.value;
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