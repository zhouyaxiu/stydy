<template>
	<!-- 0-7岁儿童NIP疫苗全程接种率 -->
	<div id="pieChartUnitOpenArea" class="chartBg" :style="'width:'+props.width+'px;height:'+props.height+'px;'">
		<div id="pieChartUnitOpen"></div>
	</div>
</template>

<script setup>
	import { ref,onMounted,onUnmounted,nextTick,getCurrentInstance } from 'vue'
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();

	const bl1=globalProxy.$util.checkIsWideFlag(window.bl)
	const oldScale=window.oldScale
	const oldWidth=window.oldWidth

	const myChart=ref(null)
	const chartOption=ref({})
	const showDataZoom=ref(false)
	// const pieData=ref([])
	// const allCountData=ref([])
	const fontSize=ref(12)
	// const radius=ref([40, 75])
	// const labelLine=ref({length: 6, length2: 12})
	const xData=ref([])
	const yData =ref([])
	const fixNums=ref(6)
	const gridY=ref(40)
	const gridY2=ref(35)
		
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
		let chart = document.getElementById('pieChartUnitOpen');
		let chartArea = document.getElementById('pieChartUnitOpenArea');
		var resizeWorldMapContainer = function() {
			chart.style.width = chartArea.clientWidth + 'px';
			chart.style.height = (chartArea.clientHeight) + 'px';
		};
		resizeWorldMapContainer();
		myChart.value = markRaw(globalProxy.$echarts.init(chart));
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			grid: {
				left: 45,
				top: 20,
				right: 20,
				bottom: gridY2.value
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
							let val = item.value.toFixed(2)||'0';
							content += "<div class='oneRow'>接种率：<div class='text'>"+globalProxy.$util.numFormat(val)+'%</div></div>';
						});
					content += "</div>";
					return content;
				}
			},
			xAxis: [{
				name: '',
				position: 'right',
				type: 'category',
				nameTextStyle: {
					fontSize: fontSize.value,
					color: store.textColor,
				},
				data: xData.value,
				axisLabel: {
					interval: 0,
					showMaxLabel: true,
					textStyle: {
						fontSize: fontSize.value,
						color: store.textStyleColor
					},
					formatter: function (val) {
						if(val && fixNums.value){
							let fixNum = fixNums.value;
							return `${val.slice(0, fixNum)}\n${val.slice(fixNum, fixNum*2)}\n${val.slice(fixNum*2, fixNum*3)}\n${val.slice(fixNum*3, fixNum*4)}`
						}
					}
				},
				splitLine: {
					show: false
				},
				axisLine: {
					show: true,
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
				name: '',
				nameTextStyle: {
					padding: [0, 0, 0, -40]
				},
				type: 'value',
				position: 'left',
				splitLine: {show: true, lineStyle: {color: '#295c88', type:'dotted', width:1}},
				axisLine:{
					lineStyle: {color: ['#7cc5f7'], type:'dotted', width:0, fontSize: 12}
				},
				axisTick:{show: false},
				axisLabel:{
					showMinLabel: true,
					textStyle: {fontSize: fontSize.value,color: '#7cc5f7',},
					formatter: function(value,index){
						if(value=='0'){
							return value;
						}
						return value+'%'
					}
				},
				min: 0,
				max:100,
				minInterval: 1,
				splitNumber: 5,
			}],
			series: [{
				type: 'bar',
				data: yData.value,
				barWidth: 10,
				itemStyle: { 
					normal:{
						color: new globalProxy.$echarts.graphic.LinearGradient(0, 1, 0, 0, [{ offset: 0, color: '#40929b' }, { offset: 0.5, color: '#0da284' }, { offset: 1, color: '#21e28f' }]),
					}
				},
				barGap:"-2",
			}],
			dataZoom: {
				show: showDataZoom.value,
				realtime: true, //是否实时更新
				type: 'slider',
				filterMode: 'filter',
				maxValueSpan: 5, //窗口的大小，显示数据的条数的
				handleSize: 0,//滑动条的 左右2个滑动条的大小
				height: 8,//组件高度
				right: 15,
				left: 45,
				bottom: 5,
				brushSelect:false,//是否开启刷性功能
				borderColor: "rgba(0,0,0,0)",
				fillerColor: '#004E95',
				backgroundColor: 'rgba(0,0,0,0)',//两边未选中的滑动条区域的颜色
				showDataShadow: false,//是否显示数据阴影 默认auto
				showDetail: false,//即拖拽时候是否显示详细数值信息 默认true
			},		
		};
		myChart.value.clear();
		myChart.value.setOption(chartOption.value);
	}

	const addChartData=(objectData)=>{
		let xData=objectData.map(item=>{return item.areaName})
		let yData=objectData.map(item=>{return item.sZPercent})
		if(objectData.length>5){
			showDataZoom.value = true;
		}
		if(chartOption.value.dataZoom || chartOption.value.series){
			chartOption.value.dataZoom.show = showDataZoom.value;
			chartOption.value.xAxis[0].data = xData;
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
		if(oldScale<0.9){
			gridY.value = 40*oldScale
			gridY2.value = 75*oldScale
		}else{
			gridY.value = 40
			gridY2.value = 35
		}
		if(oldWidth == 1366){
			gridY.value = 40
			gridY2.value = 35
		}
		nextTick(()=>{
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