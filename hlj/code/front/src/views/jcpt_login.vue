<template>
	<div>
	</div>
</template>
<script setup>
	import { jcpt_login	} from '@/utils/axios/api';
	import { onMounted,getCurrentInstance } from 'vue';
	import { useRouter } from "vue-router";
	import { mainStore } from "@/store/store";
	const router = useRouter();
	const {$route}=getCurrentInstance()
	const store = mainStore()
	const token= ref('');
	const jcptLogin=()=> {
		token.value = $route.query.token;
		if(!token.value || token.value == '') {
			return false;
		}
		jcpt_login({token: token.value}).then(res => {
			console.log('============集成平台-登录后台返回===================');
			console.log(res);
			if(res && res.code==='0'){
				let level = res.level+'';
				if(level=='0' || level=='1'){
					let currAreaData = {areaLevel: level, areaCode: res.areaCode, areaName: res.areaName, lastCityName: ''};
					// store.commit(that.$types.MAP_DATA, currAreaData);
					// store.commit(that.$types.SET_LOGINAREADATA, currAreaData);
					store.MAP_DATA(currAreaData)
					store.SET_LOGINAREADATA(currAreaData)
					console.log('============集成平台-登录成功===================');
					console.log(currAreaData);
					router.push('/jcpt');
				}else{
					// store.commit(that.$types.SET_ERROR, '只有省市级用户可以查看，请联系管理员！');
					store.SET_ERROR('只有省市级用户可以查看，请联系管理员！')
					router.push('/jcpt_error');
				}
			}else{
				// store.commit(that.$types.SET_ERROR, res.msg);
				store.SET_ERROR(res.msg);
				router.push('/jcpt_error');
			}
		}).catch(res => {
			console.log('============集成平台-登录报错==================='+res);
		})
	}
	onMounted(() =>{
		// store.commit(that.$types.SET_LOGINTYPE, 'jcpt_login');
		store.SET_LOGINTYPE('jcpt_login');
		jcptLogin();
	})
</script>
<style scoped>
</style>