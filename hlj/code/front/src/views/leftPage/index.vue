<template>
	<div class="rightPage" :style="'width:'+props.width+'px;height:100%'">
		<div class="chart-area">
			<ss-div-area title="疫苗接种率" ref="divAreaRate" :showSelect="showSelectFlag" :selectCode="selectAge" :dataList="ageList" @changeValue="changeAge"/>
			<ss-bar-chart-ym-rate :width="widthNum" :height="chartHeight-10" ref="barChartYmRate"/>
		</div>
		<div class="chart-area">
			<ss-div-area title="扫码接种率"/>
			<ss-bar-chart-scan-rate :width="widthNum" :height="chartHeight-20" ref="barChartScanRate"/>
		</div>
		<div class="chart-area">
			<ss-div-area title="实时库存" ref="divArea" :showSelect="showSelectFlag" :selectCode="selectYm" :dataList="ymList" @changeValue="changeVaccineType"/>
			<ss-bar-chart-stock :width="widthNum" :height="chartHeight-20" ref="barChartStock"/>
		</div>
		<div class="chart-area">
			<ss-div-area title="接种单位分类"/>
			<ss-pie-chart-unit-class :width="widthNum" :height="chartHeight+50" ref="pieChartUnitClass"/>
		</div>
	</div>
</template>
<script setup>
	import {ref,onMounted,getCurrentInstance, nextTick} from "vue";
	import { mainStore } from "@/store/store";
	const store = mainStore();

	import { getAges,getVaccinationRates, getWisdomScanVaccinationRates,getVaccineType,getInventory,getClassification} from '@/utils/axios/api';
	import ssBarChartYmRate from '@/views/leftPage/barChartYmRate.vue';
	import ssBarChartScanRate from '@/views/leftPage/barChartScanRate.vue';
	import ssBarChartStock from '@/views/leftPage/barChartStock.vue';
	import ssPieChartUnitClass from '@/views/leftPage/pieChartUnitClass.vue';
		
	const scale=window.scale
	const heightNum=window.winHeight
	const widthNum=(window.winWidth  - window.midW*window.scale)/2 - 20
	
	// const chartHeight=(document.body.clientHeight-83)/2-60
	const demoDataFlag=JSON.parse(process.env.VITE_DEMODATAFLAG)

	// const chartWidth=ref('0px')
	const showSelectFlag=ref(true)
	const selectAge=ref('2')
	const ageList=ref([])
	const selectYm=ref('')
	const ymList=reactive([])
	
	const barChartYmRate=ref(null) //疫苗接种率
	const barChartScanRate=ref(null) //扫码接种率
	const barChartStock=ref(null) //实时库存
	const pieChartUnitClass=ref(null) //接种单位分类
	const divArea=ref(null) //接种单位分类
	const divAreaRate=ref(null) //疫苗接种率
	
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

	let chartHeight=0
	const changeAge=(val)=>{
		selectAge.value = val;
		GetVaccinationRates();
	}
	const changeVaccineType=(val)=>{
		selectYm.value = val;
		GetInventory();
	}
	const initQueryData=()=>{
		selectAge.value="2"
		selectYm.value=""
		GetAges();
		GetVaccinationRates();
		GetWisdomScanVaccinationRates();
		GetInventory();
		GetClassification();
		GetVaccineType();
	}
	watch(() => window.innerWidth, (val) => {
		if(val>=1600){
			chartHeight=(window.winHeight-83-48.5*4)/4
		}
		if(val>=1366 && val<1600){
			chartHeight=(window.winHeight-83-41*4)/4
		}
		if(val>=1280 && val<1366){
			chartHeight=(window.winHeight-83-42.5*4)/4
		}
		if(val<1280){
			chartHeight=(window.winHeight-83-40*4)/4
		}
	},{immediate:true})
	
	//年龄组
	const GetAges=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			getAges().then(res => {
				if(res && res.ages && res.ages.length>0){
					ageList.value = res.ages;
					divAreaRate.value.dataList=res.ages;
				}
			})
		}
	}
	//疫苗种类
	const GetVaccineType=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			selectYm.value = '';
			ymList.value = [];
			getVaccineType().then(res => {
				if(res && res.vaccineTypeList && res.vaccineTypeList.length>0){
					ymList.value = res.vaccineTypeList;
					// selectYm.value = ymList.value[0].value;
					divArea.value.dataList=res.vaccineTypeList
				}
			})
		}
	}

	//疫苗接种率
	const GetVaccinationRates=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				areaCode: store.currAreaData.areaCode,
				age: selectAge.value
			}
			getVaccinationRates(params).then(res => {
				//console.log("============右侧图1：疫苗接种率============selectAge  "+that.selectAge);
				let data = res.vaccinationRates || []
				barChartYmRate.value && barChartYmRate.value.addChartData(data);
			})        				
		}
	}

	//扫码接种率
	const GetWisdomScanVaccinationRates=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				areaCode: store.currAreaData.areaCode
			}
			getWisdomScanVaccinationRates(params).then(res => {
				// console.log("============右侧图2：扫码接种率============");
				let data = res.scanVaccinationRates || []
				barChartScanRate.value && barChartScanRate.value.addChartData(data);
			})						
		}
	}
	
	//实时库存
	const GetInventory=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				areaCode: store.currAreaData.areaCode,
				vaccineId: selectYm.value
			}
			getInventory(params).then(res => {
				// console.log(res,"============左侧图2：实时库存============");
				let data = res.inventoryList || [];
				barChartStock.value && barChartStock.value.addChartData(data);
			})						
		}
	}

	// 接种单位分类
	const GetClassification=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				areaCode: store.currAreaData.areaCode
			}
			getClassification(params).then(res => {
				let objectData= res || {};
				pieChartUnitClass.value && pieChartUnitClass.value.addChartData(objectData);
			})
		}
	}

	onMounted(()=> {
	
	})

	// 公开数据
	defineExpose({
		initQueryData,
		chartHeight,
	})
</script>