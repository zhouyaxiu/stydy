<template>
    <div class="rightPage" :style="'width:'+props.width+'px;height:100%;position:relative'">
        <div class="middle-top-height">
            <!-- 地图 -->
            <ss-map ref="myMap" :width="props.middleWidthNum-40" :height="mapHeight" @changeCityCode="changeCityCode"/>
        </div>
        <div ref="bottomDiv">
            <!-- 地图下面的数字框 -->
            <ss-middle-nums-content :numsData="mapBottomNumData" :height="numsHeight" ref="numsPage" style="margin-bottom:10px;"/>
            <div style="display:flex;">
                <div class="chart-area" style="margin-right:16px;margin-bottom:0px;flex:1">
                    <ss-div-area title="历年出生儿童建档数"/>
                    <ss-history-year-chart :width="(props.middleWidthNum-60)/2" :height="bottomHeight" ref="historyYearChart" />
                </div>
                <div class="chart-area" style="margin-bottom:0px;flex:1">
                    <ss-div-area title="当年出生儿童建档数"/>
                    <ss-current-year-chart :width="(props.middleWidthNum-60)/2" :height="bottomHeight" ref="currentYearChart" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
	import {ref,onMounted,getCurrentInstance, nextTick} from "vue";
    import { getTotal, getWisdomBirthYear, getWisdomBirthMonth } from '@/utils/axios/api';
	import { mainStore } from "@/store/store";
	const store = mainStore();
	import ssMap from '@/components/common/myMap.vue';
	import ssMiddleNumsContent from '@/views/middlePage/numsContent.vue';
	import ssHistoryYearChart from '@/views/middlePage/historyYearChart.vue';
	import ssCurrentYearChart from '@/views/middlePage/currentYearChart.vue';
	// const chartHeight=(document.body.clientHeight-83)/2-60
	const demoDataFlag=JSON.parse(process.env.VITE_DEMODATAFLAG)
    const mapHeight=window.winHeight/1.9 - 20*window.scale
	const numsHeight=ref()
	const bottomHeight=ref()
	const mapBottomNumData=ref({
		preCheckNum: 0,
		preRegisterNum: 0,
		preJzNum: 0,
		finishRemainNum: 0,
		todayFinish: 0,
		todayBuildingNum: 0,
		todayJzNum: 0,
		todayYdJzNum: 0,
		todayTransferNum: 0
	})
    const numsPage = ref(null);
	const historyYearChart = ref(null);
	const currentYearChart = ref(null);
	const bottomDiv = ref(null);
	const emits = defineEmits(['changeCityCode'])
	const props = defineProps({
		width: {
			type: Number,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		},
		middleWidthNum: {
			type: Number,
			required: true,
		},
		middleHeight: {
			type: Number,
			required: true,
		},
	})

	const changeCityCode=()=>{
		emits('changeCityCode')
	}

	const initQueryData=()=>{
		GetTotal();
		GetWisdomBirthYear();
		GetWisdomBirthMonth();
		
	}

	const jsBottomHeight=()=>{
		numsHeight.value=bottomDiv.value.children[0].clientHeight
		bottomHeight.value = props.middleHeight - (mapHeight + numsHeight.value+30)
		bottomDiv.value.style.position="absolute"
		bottomDiv.value.style.width="100%"
		if(window.winWidth>=1400) bottomDiv.value.style.bottom="2px"
		if(window.winWidth>=1366 && window.winWidth<1400) bottomDiv.value.style.bottom="7px"
		if(window.winWidth<1366) bottomDiv.value.style.bottom="23px"
	}

	//总人数：中间地图下面的9个数字
	//地图下面的--人数统计
	const GetTotal=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			if(demoDataFlag){
				mapBottomNumData.value = {
					todayChildBookNumber: 1360,
					todayShotJCNumber: 9536,
					todayShotPeopleNumber: 6622,
					childBookNumber: 203966,
					childBook06Number: 203966,
					childBook718Number: 26489,
					childBook18Number: 1195
				};
			}else{
				let params = {
					// level: level,
					areaCode: store.currAreaData.areaCode
				}
				getTotal(params).then(res => {
					mapBottomNumData.value = res.wisdomTodayData || {};
				})
			}
		}
	}

	//历年出生儿童建档数
	const GetWisdomBirthYear=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			if(demoDataFlag){
				let data = [{"birthYear":"2013","count":19994},{"birthYear":"2014","count":1344},{"birthYear":"2015","count":566544},{"birthYear":"2016","count":15666},{"birthYear":"2017","count":11227},{"birthYear":"2018","count":41},{"birthYear":"2019","count":39},{"birthYear":"2020","count":100935},{"birthYear":"2021","count":101277},{"birthYear":"2022","count":288},{"birthYear":"2023","count":46}];
				historyYearChart.value && historyYearChart.value.addChartData(data);
			}else{
				let params = {
					// level: level,
					areaCode: store.currAreaData.areaCode
				}
				getWisdomBirthYear(params).then(res => {
					let data = res.wisdomBirthYearList || []
					historyYearChart.value && historyYearChart.value.addChartData(data);
				})
			}
		}
	}

	//当年出生儿童建档数
	const GetWisdomBirthMonth=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			if(demoDataFlag){
				let data = [{"birthMonth":"1","count":120000},{"birthMonth":"2","count":110000},{"birthMonth":"3","count":130000},{"birthMonth":"4","count":150000},{"birthMonth":"5","count":90000},{"birthMonth":"6","count":190000},{"birthMonth":"7","count":80000},{"birthMonth":"8","count":150000},{"birthMonth":"9","count":110000},{"birthMonth":"10","count":130000},{"birthMonth":"11","count":190000},{"birthMonth":"12","count":0}];
				currentYearChart.value && currentYearChart.value.addChartData(data);
			}else{
				let params = {
					// level: level,
					areaCode: store.currAreaData.areaCode
				}
				getWisdomBirthMonth(params).then(res => {
					let data = res.wisdomBirthMonthList || []
					currentYearChart.value && currentYearChart.value.addChartData(data);
				})
			}
		}
	}

	onMounted(()=> {
		jsBottomHeight()
	})

	// 公开数据
	defineExpose({
		initQueryData
	})
</script>