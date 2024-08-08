<template>
	<!-- 柱图-柱 实时库存-->
	<div id="bar3ChartStockArea" class="chartBg" :style="'width:'+props.width+'px;height:'+props.height+'px;'">
		<div id="bar3ChartStock"></div>
	</div>
</template>

<script setup>
	// import * as echarts from 'echarts'
	import { ref,onMounted, nextTick,getCurrentInstance, onUnmounted,markRaw } from 'vue';
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();

	const oldScale=window.oldScale
	const oldWidth=window.oldWidth
	const bl1=globalProxy.$util.checkIsWideFlag(window.bl)

	const myChart=ref(null)
	const chartOption=ref({})
	const showDataZoom=ref(false)
	const xData=ref([])
	const yData1=ref([])
	const yData2=ref([])
	const yData3=ref([])
	
	const fontSize=ref(12)
	const gridY2=ref(16)
	const showNum=ref(4)
	let fixNums=ref(6)
			
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

	const initChart=() =>{
		let chart = document.getElementById('bar3ChartStock');
		let chartArea = document.getElementById('bar3ChartStockArea');
		var resizeWorldMapContainer = function() {
			chart.style.width = chartArea.clientWidth + 'px';
			chart.style.height = chartArea.clientHeight + 'px';
		};
		resizeWorldMapContainer();
		myChart.value = markRaw(globalProxy.$echarts.init(chart));
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			grid: {
				left: 60,
				right: 20,
				top: 40,
				bottom: gridY2.value
			},
			legend: {
				orient: 'horizontal',
				top:10,
				right: 10,
				itemWidth: 10,
				itemHeight: 10,
				textStyle:{fontSize:fontSize.value-1, color:store.textColor},	
				icon:'rect',
				data:['小于1个月','1-3个月','3个月以上']			
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
			xAxis: [{
				name: '',
				type: 'category',
				nameTextStyle: {
					fontSize: fontSize.vue,
					color: '#75B8EB',
				},
				data: xData.value,
				axisLabel: {
					interval: 0,
					showMaxLabel: true,
					textStyle: {
						fontSize: fontSize.value,
						color: '#75B8EB'
					},
					formatter: function (val) {
						let fixNum = fixNums.value;
						return `${val.slice(0, fixNum)}\n${val.slice(fixNum, fixNum*2)}\n${val.slice(fixNum*2, fixNum*3)}\n${val.slice(fixNum*3, fixNum*4)}`
					}
				},
				splitLine: {
					show: false
				},
				axisLine: {
					show: false,
					onZero: false,
					lineStyle: {
						color: '#5378A7',
						type: 'dotted',
						width: 1
					}
				},
				axisTick: {
					show: false
				},
			}],
			yAxis: [{
				name: '数量',
				nameTextStyle: {
					padding: [0, 0, 0, -40],
					fontSize: fontSize.value,
					color:'#B0DFFF'
				},
				type: 'value',
				position: 'left',
				splitLine: {show: true, lineStyle: {color: '#295c88', type:'dotted', width:1}},
				axisLine:{
					lineStyle: {color: ['#7cc5f7'], type:'dotted', width:0, fontSize: fontSize.value}
				},
				axisTick:{show: false},
				axisLabel:{
					showMinLabel: true,
					textStyle: {fontSize: fontSize.value, color: '#7cc5f7'},
					formatter: function(value,index){
						return globalProxy.$util.numFormat(value||0)
					}
				},
				min: 0,
				minInterval: 1,
				splitNumber: 3,
			}],
			series: [{
					name: '小于1个月',
					type: 'bar',
					data: yData1.value,
					barWidth: 10,
					itemStyle: { 
						normal:{
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 0, 0, 1,[{offset: 0, color: '#ee6767'}, {offset: 0.5, color: '#ee6767'}, {offset: 1, color: '#0c2764'}]),
						}
					},
					barGap:"0",
				},
				{
					name: '1-3个月',
					type: 'bar',
					data: yData2.value,
					barWidth: 10,
					itemStyle: { 
						normal:{
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 0, 0, 1,[{offset: 0, color: '#f2b55e'}, {offset: 0.5, color: '#f2b55e'}, {offset: 1, color: '#0c2764'}]),
						}
					},
					barGap:"0",
				},
				{
					name: '3个月以上',
					type: 'bar',
					data: yData3.value,
					barWidth: 10,
					itemStyle: { 
						normal:{
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 0, 0, 1,[{offset: 0, color: '#44e3b0'}, {offset: 0.5, color: '#44e3b0'}, {offset: 1, color: '#0c2764'}]),
						}
					},
					barGap:"0",
				}
			],
			dataZoom: {
				show: showDataZoom.value,
				realtime: true, //是否实时更新
				type: 'slider',
				filterMode: 'filter',
				maxValueSpan: 3, //窗口的大小，显示数据的条数的
				handleSize: 0,//滑动条的 左右2个滑动条的大小
				height: 8,//组件高度
				right: 15,
				left: 45,
				bottom: 5,
				start:0,
				end:100,
				brushSelect:false,//是否开启刷性功能
				borderColor: "rgba(0,0,0,0)",
				fillerColor: '#004E95',
				// handleIcon: "M-292,322.2c-3.2,0-6.4-0.6-9.3-1.9c-2.9-1.2-5.4-2.9-7.6-5.1s-3.9-4.8-5.1-7.6c-1.3-3-1.9-6.1-1.9-9.3c0-3.2,0.6-6.4,1.9-9.3c1.2-2.9,2.9-5.4,5.1-7.6s4.8-3.9,7.6-5.1c3-1.3,6.1-1.9,9.3-1.9c3.2,0,6.4,0.6,9.3,1.9c2.9,1.2,5.4,2.9,7.6,5.1s3.9,4.8,5.1,7.6c1.3,3,1.9,6.1,1.9,9.3c0,3.2-0.6,6.4-1.9,9.3c-1.2,2.9-2.9,5.4-5.1,7.6s-4.8,3.9-7.6,5.1C-285.6,321.5-288.8,322.2-292,322.2z",
				// handleSize: "100%",
				// handleStyle: {color: "#004E95"},
				backgroundColor: 'rgba(0,0,0,0)',//两边未选中的滑动条区域的颜色
				showDataShadow: false,//是否显示数据阴影 默认auto
				showDetail: false,//即拖拽时候是否显示详细数值信息 默认true
			}
		};
		myChart.value.clear();
		myChart.value.hideLoading();
		myChart.value.setOption(chartOption.value);
	}

	const addChartData=(dataList)=>{
		xData.value = [];
		yData1.value = [];
		yData2.value = [];
		yData3.value = [];
		showDataZoom.value = false;
		if(dataList && dataList.length>0){
			dataList.forEach(item => {
				let name = item.GBNAME;
				xData.value.push(name);
				yData1.value.push(item.lessOneMonthNum);
				yData2.value.push(item.lessThreeMonthNum);
				yData3.value.push(item.moreThreeMonthNum);
			});
			chartOption.value.xAxis[0].axisLine.show=true
		}else{
			chartOption.value.xAxis[0].axisLine.show=false
		}
		if(xData.value.length>showNum.value){
			showDataZoom.value = true;
		}else{
			showDataZoom.value = false;
		}
		chartOption.value.dataZoom.show = showDataZoom.value;
		chartOption.value.xAxis[0].data = xData.value;
		chartOption.value.series[0].data = yData1.value;
		chartOption.value.series[1].data = yData2.value;
		chartOption.value.series[2].data = yData3.value;
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
		fontSize.value = 12*oldScale
		fontSize.value = fontSize.value>13?13:fontSize.value;
		if(oldScale<0.9){
			gridY2.value = 48*oldScale
		}else{
			gridY2.value = 36
		}
		if(oldWidth == 1366){
			gridY2.value = 36
		}
		initChart()
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