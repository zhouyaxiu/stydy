<template>
	<div class="leftPage" :style="'width:'+props.width+'px;height:100%'">
		<div class="chart-area">
			<ss-div-area title="当日接种"/>
			<ss-bar-chart-day-vacc :width="widthNum" :height="chartHeight-10" ref="barChartDayVacc"/>
		</div>
		<div class="chart-area">
			<ss-div-area title="0-7岁儿童NIP疫苗全程接种率"/>
			<ss-pie-chart-nip-rate :width="widthNum" :height="chartHeight-20" ref="pieChartNipRate"/>
		</div>
		<div class="chart-area">
			<ss-div-area title="当年非免疫规划疫苗接种统计"/>
			<ss-pie-chart-not-stat :width="widthNum" :height="chartHeight-20" ref="pieChartNotStat"/>
		</div>
		<div class="chart-area">
			<ss-div-area title="近效期库存" immunoName="jxq" :showSelect="showSelectFlag" :selectCode="selectYm" :dataList="ageList" :showImmuno="true"  @changeValue="changeVaccineType" @changeImmuno="changeImmunoType" />
			<ss-bar-chart-near-store :width="widthNum" :height="chartHeight+50" ref="barChartNearStore"/>
		</div>
	</div>
</template>
<script setup>
	import { ref,onMounted, getCurrentInstance, nextTick } from 'vue';
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore()

	import { getSameDayVaccination,getNIPVaccination,getVaccinationCounts,getWisdomInventory } from '@/utils/axios/api';
	import ssBarChartDayVacc from '@/views/rightPage/barChartDayVacc.vue';
	import ssPieChartNipRate from '@/views/rightPage/pieChartNipRate.vue';
	import ssPieChartNotStat from '@/views/rightPage/pieChartNotStat.vue';
	import ssBarChartNearStore from '@/views/rightPage/barChartNearStore.vue';

	const scale=window.scale
	const demoDataFlag=JSON.parse(process.env.VITE_DEMODATAFLAG)
	const widthNum=(window.winWidth  - window.midW*window.scale)/2 - 20
	
	let chartHeight=0
	// const chartHeight=(document.body.clientHeight-83)/2-60
	
	const chartWidth=ref('0px')
	const showSelectFlag=ref(true)
	const selectAge=ref('')
	const ageList=ref([{label:"3个月",value:'0'},{label:"1个月",value:'1'},{label:"已超期",value:'2'}])
	const selectYm=ref('')
	
	const barChartYmRate=ref(null)
	const barChartDayVacc=ref(null)
	const pieChartNipRate=ref(null)
	const pieChartNotStat=ref(null)
	const barChartNearStore=ref(null)
	
	const props = defineProps({
		width: {
			type: Number,
			required: true,
		},
	})

	const initQueryData=()=>{
		selectYm.value="";
		GetSameDayVaccination();
		GetNIPVaccination();
		GetVaccinationCounts();
		GetWisdomInventory();
	}
	
	watch(() => window.winWidth, (val) => {
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

	//当日接种
	const GetSameDayVaccination=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				level: level,
				areaCode: store.currAreaData.areaCode
			}
			getSameDayVaccination(params).then(res => {
				setTimeout(() => {
					let data = res.sameDayVaccs || []
					barChartDayVacc.value && barChartDayVacc.value.addChartData(data);
				}, 200)
			})
		}
	}

	//0-7岁儿童NIP疫苗全程接种率
	const GetNIPVaccination=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				// level: store.currAreaData.areaLevel,
				areaCode: store.currAreaData.areaCode
			}
			getNIPVaccination(params).then(res => {
				if(Object.keys(res).length==0) return pieChartNipRate.value.initChart();
				let objectData=res.NIPVaccs || [];
				pieChartNipRate.value && pieChartNipRate.value.addChartData(objectData);
			})
		}
	}

	// 当年非免疫规划疫苗接种统计
	const GetVaccinationCounts=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				areaCode: store.currAreaData.areaCode
			}
			getVaccinationCounts(params).then(res => {
				if(Object.keys(res).length==0) return pieChartNotStat.value.initChart();
				let objectData=res.vaccinationCounts || [];
				pieChartNotStat.value && pieChartNotStat.value.addChartData(objectData);
			})
		}
	}

	// 近效期库存
	const NIPData=ref(null)
	const NoNIPData=ref(null)
	const GetWisdomInventory=()=>{
		let level = store.currAreaData.areaLevel;
		if(level=='0' || level=='1'){
			let params = {
				areaCode: store.currAreaData.areaCode,
				vaccineId:selectYm.value
			}
			getWisdomInventory(params).then(res => {
				if(Object.keys(res).length==0){
					barChartYmRate.value && barChartYmRate.value.initChart();
					barChartNearStore.value && barChartNearStore.value.initChart();
					return
				}
				if(!res.NIPwisdomInventoryList) return;
				if(!res.wisdomInventoryList) return;
				NIPData.value=res.NIPwisdomInventoryList;
				NoNIPData.value=res.wisdomInventoryList;
				barChartYmRate.value && barChartYmRate.value.addChartData(NIPData.value,NoNIPData.value);
				barChartNearStore.value && barChartNearStore.value.addChartData(NIPData.value,NoNIPData.value);
			})
		}
	}

	const changeVaccineType=(val)=>{
		if(val && val!=''){
			selectYm.value=val
			GetWisdomInventory()
		}
	}

	const changeImmunoType=(val)=>{
		barChartNearStore.value.immunoVal=val
		barChartNearStore.value && barChartNearStore.value.addChartData(NIPData.value,NoNIPData.value)
	}

	onMounted(()=> {
	})

	// 公开数据
	defineExpose({
		initQueryData,
		chartHeight
	})
</script>