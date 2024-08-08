<template>
	<el-container class="main-container">
		<el-main class="scroll-item" :style="'height:'+heightNum">
			<div class="content-bg1" style="height:100%;">
				<ss-srceen-content :jcptFlag="jcptFlag"/>
			</div>
		</el-main>
	</el-container>
</template>
<script setup>
	import ssSrceenContent from '@/views/content.vue';
	import { ref,onMounted, onUpdated } from 'vue';
	import { mainStore } from "@/store/store";
	const store = mainStore()
	
	const heightNum=ref('calc(' + (100 / window.scale) + 'vh)')
	const timestamp=ref(new Date().getTime())
	const scale=window.scale
	// const isIE=window.isIE()
	const bl=ref(window.bl)
	const jcptFlag=ref(false)
	onMounted(()=> {
		jcptFlag.value = false;
		if(store.loginType && store.loginType=='jcpt_login'){
			//集成平台登录进来
			jcptFlag.value = true;
		}else{
			// this.$store.commit(this.$types.SET_LOGINTYPE, '');
			store.SET_LOGINTYPE("")
		}
	})
	onUpdated(()=> {
	})
</script>