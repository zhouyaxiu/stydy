<template>
	<!-- 历年出生儿童建档数-->
	<div id="historyYearChartArea" class="chartBg1" :style="'width:'+props.width+'px;height:'+props.height+'px;'">
		<div id="historyYearChart"></div>
	</div>
</template>

<script setup>
	import {ref,getCurrentInstance,onMounted,nextTick,onUnmounted,markRaw} from "vue"
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();

	const oldWidth=window.oldWidth
	const oldScale=window.oldScale
	const bl=window.bl
	const bl1 =globalProxy.$util.checkIsWideFlag(window.bl)

	const myChart=ref( null)
	const chartOption=ref( {})
	const xData=ref( [])
	const yData=ref( [0,0,0,0,0,0,0,0,0,0,0])
	const fontSize=ref(12)
	const interval=ref(0)
			
	const props=defineProps( {
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
		let chart = document.getElementById('historyYearChart');
		let chartArea = document.getElementById('historyYearChartArea');
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
						content += "<div class='oneRow'><div class='text'>"+name+'年</div></div>';
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
					interval: interval.value,
					showMaxLabel: true,
					textStyle: {
						fontSize: fontSize.value,
						color: store.textStyleColor
					},
				},
				splitLine: {
					show: false,
					lineStyle: { color: "rgba(245, 245, 245, 0.3)", type: 'dashed', width: 1 }
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
				splitLine: {show: true, lineStyle: {color: '#295c88', type:'dotted', width:1}},
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
						color:'#00FFFD',
						borderColor: '#fff',
						lineStyle:{//折线颜色
							color: 'rgba(0,255,253,1)',
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
						color: new globalProxy.$echarts.graphic.LinearGradient(0, 1, 0, 0, [{ offset: 0, color: 'rgba(11,31,83, 0.1)' }, { offset: 1, color: 'rgba(50,153,252,1)' }], false),
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
				xData.value.push(item.birthYear);
				yData.value.push(item.count/10000);
			});
		}else{
			xData.value = nearYearArry(10);
			yData.value = [0,0,0,0,0,0,0,0,0,0,0];
		}
		chartOption.value.xAxis.data = xData.value;
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

	//最近N年  返回：年度集合
	const nearYearArry=(num)=> {
		let nowYear = new Date().getFullYear();
		let arr = [];
		for(var i=num;i>=0;i--){
			arr.push(nowYear - i);
		}
		return arr;
	}

	onMounted(()=> {
		fontSize.value = 12*oldScale
		fontSize.value = fontSize.value>13?13:fontSize.value;
		interval.value = 0;
		if(!bl1 || oldWidth==1440 || oldWidth==1400 || oldWidth==1366){
			interval.value = 1;
		}
		xData.value = nearYearArry(10);
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