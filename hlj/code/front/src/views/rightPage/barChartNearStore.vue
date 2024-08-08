<template>
	<!-- 柱图-近效期库存-->
	<div id="barChartNearStoreArea" class="chartBg" :style="'width:'+props.width+'px;height:'+(props.height)+'px;'">
		<div id="barChartNearStore"></div>
	</div>
</template>

<script setup>
	import { ref,onMounted,onUnmounted,nextTick,getCurrentInstance,markRaw }  from 'vue'
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();

	const oldWidth=window.oldWidth
	const oldScale=window.oldScale
	const bl1 =globalProxy.$util.checkIsWideFlag(window.bl)

	const myChart=ref(null)
	const chartOption=ref({})
	const showDataZoom=ref(false)
	const xData=ref([])
	const yData=ref([])
	// const showNum=ref(7)
	const fontSize=ref(12)
	const gridY2=ref(36)
	// const splitNumber=ref(5)
	const fixNum=ref(3)
	const legend=ref(['省本级', '地市', '区县', '接种门诊'])
	const immunoVal=ref(0)
	const colors=ref(['#e42290','#ffb501','#32e99e', '#3199e9'])
			
	const props=defineProps({
		width: {
			type: Number,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		}
	})

	
	const initChart=()=> {
		let chart = document.getElementById('barChartNearStore');
		let chartArea = document.getElementById('barChartNearStoreArea');
		var resizeWorldMapContainer = function() {
			chart.style.width = chartArea.clientWidth + 'px';
			chart.style.height = (chartArea.clientHeight) + 'px';
		};
		resizeWorldMapContainer();
		myChart.value = markRaw(globalProxy.$echarts.init(chart));
		let level = store.currAreaData.areaLevel;
		let legendVal=JSON.parse(JSON.stringify(legend.value));
		let yDataVal=JSON.parse(JSON.stringify(yData.value));
		let color=JSON.parse(JSON.stringify(colors.value));
		let legendtemp=immunoVal.value==0 && level=='0'?legendVal:immunoVal.value==0 && level=='1'?legendVal.slice(1,4):legendVal.slice(2,4);
		let yDatatemp= immunoVal.value==0 && level=='0'?yDataVal:immunoVal.value==0 && level=='1'?yDataVal.slice(1,4):yDataVal.slice(2,4);
		let colortemp= immunoVal.value==0 && level=='0'?color:immunoVal.value==0 && level=='1'?color.slice(1,4):color.slice(2,4);
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			color: colortemp,
			legend: {
				top: 6,
				padding: [0,20],
				itemWidth: 10,
				itemHeight: 10,
				x: 'right',
				orient: 'horizontal',
				textStyle: {
					fontSize: fontSize.value,
					color: store.textColor,
					padding: [3, 0, 0, 0]
				},
				data: legendtemp
			},
			grid: {
				x: 50,
				y: 30,
				x2: 20,
				y2: 50,
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
				formatter: function(param) {
					let params=param.reverse();
					let name = params[0].name;
					let content = "<div class='comm-chart-tip-area'>";
						content += "<div class='oneRow'><div class='text'>"+name+'</div></div>';
						params.forEach(item=>{
							let val = item.value>0?item.value:'0';
							content += "<div class='oneRow'>"+item.seriesName+"：<div class='text'>"+globalProxy.$util.numFormat(val)+'万</div></div>';
						});
					content += "</div>";
					return content;
				}
			},
			yAxis: [{
				name: '数量（万）',
				type: 'value',
				nameTextStyle:{
					color:'#B0DFFF',
					fontSize:fontSize.value,
					padding:[0,0,0,-20]
				},
				nameGap:10,
				axisLabel: {
					fontSize: fontSize.value,
					color: '#7cc5f7',
					formatter: function(value,index){
						return value
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
				splitNumber: 5,
			}],
			xAxis: [{
				type: 'category',
				axisLabel: {
					fontSize: fontSize.value,
					color: store.textStyleColor,
					interval:0,
					formatter(val) {
						return '{a|' + `${val.slice(0, 6)}\n${val.slice(6, 12)}\n${val.slice(12, 18)}\n${val.slice(18, 24)}` +'}'
					},
					rich: {
						a: {
							/* 增加行高 避免太拥挤*/
							height: 16,
							fontSize: fontSize.value,
							color: store.textStyleColor,
							align: 'center',
						}
					}
				},
				axisLine: {
					show: xData.value.length==0?false:true,
					onZero: false,
					lineStyle: {
						color: '#5378A7',
						type: 'dotted',
						width: 1
					}
				},
				axisTick: {
					show: false,
				},
				data: xData.value,
			}],
			dataZoom: {
				show: showDataZoom.value,
				realtime: true, //是否实时更新
				type: 'slider',
				filterMode: 'filter',
				maxValueSpan: 6, //窗口的大小，显示数据的条数的
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
			series: [],
		};
		for (var i = legendtemp.length-1; i >=0; i--) {
			let item={
				name: legendtemp[i],
				type: 'bar',
				stack: '总量',
				barWidth: 10,
				// label: {
				// 	show: false,
				// 	position: 'insideRight'
				// },
				data: yDatatemp[i],
				itemStyle: {
					normal: {
						"color": colortemp[i],
						"borderRadius": 0,
					}
				}
			}
			chartOption.value.series.push(item)
		}
		nextTick(()=>{
			myChart.value.clear();
			myChart.value.setOption(chartOption.value);
		})
	}

	const addChartData=(NIPData,NoNIPData,code)=>{
		let objectData=[]
		if(immunoVal.value==0) objectData=NIPData;
		else objectData=NoNIPData;
		let xDatas=objectData.map(item=>{return item.GOODSSORTNAME});
		let arr1=[],arr2=[],arr3=[],arr4=[];
		if(objectData.length>7){
			showDataZoom.value = true;
		}
		objectData.map(item=>{
			if(!code){
			// 3个月  0  1个月：1   已超期：2
				arr1.push(item.provinceSTORENUM/10000);
				arr2.push(item.citySTORENUM/10000);
				arr3.push(item.couSTORENUM/10000);
				arr4.push(item.unitSTORENUM/10000);
			}else if(code=='0'){
				arr1.push(item.provinceLessThreeMonthNum/10000);
				arr2.push(item.cityLessThreeMonthNum/10000);
				arr3.push(item.couLessThreeMonthNum/10000);
				arr4.push(item.unitLessThreeMonthNum/10000);
			}else if(code=='1'){
				arr1.push(item.provinceLessOneMonthNum/10000);
				arr2.push(item.cityLessOneMonthNum/10000);
				arr3.push(item.couLessOneMonthNum/10000);
				arr4.push(item.unitLessOneMonthNum/10000);
			}else if(code=='2'){
				arr1.push(item.provinceMoreThreeMonthNum/10000);
				arr2.push(item.cityMoreThreeMonthNum/10000);
				arr3.push(item.couMoreThreeMonthNum/10000);
				arr4.push(item.unitMoreThreeMonthNum/10000);
			}
		});
		let yDatas=[arr1,arr2,arr3,arr4];
		xData.value=xDatas;
		yData.value=yDatas;
		nextTick(()=>{
			initChart()
		})
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
			gridY2.value = 78*oldScale
		}else{
			gridY2.value = 35
		}
		if(oldWidth == 1366 || oldWidth == 1360){
			gridY2.value = 35
		}
	})

	onUnmounted(()=>{
		myChart.value = null;
	})

	// 公开数据
	defineExpose({
		addChartData,
		immunoVal
	})
</script>