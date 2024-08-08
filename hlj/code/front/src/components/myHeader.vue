<template>
	<div class="header">
		<div class="left-timer">
			<ss-time-refresh/>
		</div>
		<!-- jcpt登录 -->
		<div class="right-nav" @click="jcptPage()" v-if="props.jcptFlag">
			<div><ss-icon iconType="navi" :style="iconStyle"/></div>
			<div class="nav-text" :style="navTextStyle">导航<br/>中心</div>
		</div>
	</div>
</template>
<script setup>
	import { ref,onMounted, getCurrentInstance, onUnmounted,watch } from 'vue';
	
	import ssTimeRefresh from '@/components/common/timeRefresh.vue';
	import ssIcon from '@/components/common/icon.vue';
	const emits = defineEmits(["changeCityCode"])

	const oldScale= window.oldScale
	const iconStyle=ref('')
	const navTextStyle=ref('')
	const interTime=ref(null)

	const props=defineProps({
		jcptFlag: {
			type: Boolean,
			required: true
		}
	})

	const jcptPage=()=>{
		sessionStorage.removeItem('loginType');
		sessionStorage.removeItem('loginAreaData');
		sessionStorage.removeItem('currAreaData');
		window.location = window.JCPT_URL;
	}
	const refreshData=()=>{
		emits("changeCityCode");
		// console.log("========定时刷新============"+this.$util.getCurrDateTime('yyyy-MM-dd hh:mm:ss'));
	}

	onMounted(()=> {
		interTime.value = setInterval(refreshData(), window.refreshTime*1000);
		if(oldScale<0.9){
			iconStyle.value = 'width:'+27*oldScale+'px !important;height:'+27*oldScale+'px !important;';
			navTextStyle.value = 'font-size:'+14*oldScale+'px;';
		}else{
			iconStyle.value = "";
			navTextStyle.value = '';
		}
	})

	onUnmounted(()=>{
		clearInterval(interTime.value);
		interTime.value = null;
	})
</script>
<style scoped>
	.nav-text{
		color: #06f7ff;
		font-size: 14px;
		margin-top:10px;
	}
</style>
<style>
</style>