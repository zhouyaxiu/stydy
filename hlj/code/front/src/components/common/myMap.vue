<template>
	<div class="map" id="myMapBox" :style="'width:'+props.width+'px;height:'+props.height+'px;margin-top:-10px;'">
		<div id="myMap"></div>
		<!-- 上一页按钮 -->
		<div class="prov-area last-btn-active" :style="'width:'+btnWidth+'px;height:'+btnHeight+'px;margin-top:-88px'" v-if="showLastFlag" @click="clickLastBtn"></div>
	</div>
</template>
<script setup>
	// import * as echarts from 'echarts'
	import { ref,onMounted, getCurrentInstance, onUnmounted, nextTick, onBeforeMount,reactive } from 'vue';
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore()

	import { getArea } from '@/utils/axios/api';
	import allProv from '@/plugins/mapGeoData/黑龙江省.json';

	const bl1 =globalProxy.$util.checkIsWideFlag(window.bl)
	const oldScale=window.oldScale
	const scale=window.scale
	const demoDataFlag=JSON.parse(process.env.VITE_DEMODATAFLAG)

	const mapName=ref('黑龙江省')
	const cityName=ref('黑龙江省')
	const myMap=ref(null)
	// const warningList=ref([])
	// const defaultList=ref([])
	const currentMapData=ref({})
	const showLastFlag=ref(false)
	// const allAreaData=ref(null)
	const mapOption=ref(null)
	const zoom=ref(1.07)
	const defaultZoom=ref(1.07)
	const fontSize=ref(12)
	// const currentAreaData=ref({})
	const mapLevel=ref('1')
	const mapData=ref({})
	const provData=ref({})
	const bBNum=ref(480)
	
	const btnWidth=ref(134)
	const btnHeight=ref(32)
	const loginType=ref(store.loginType||'')
	const userArea=ref({})
	const pointList=ref([])
	const emits = defineEmits(['changeCityCode',"changeCityCode"])

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

	const drawMap=()=> {
		if(cityName.value==userArea.value.areaName){
			showLastFlag.value = false;
		}else{
			showLastFlag.value = true;
		}
		//先去掉原来的地图
		globalProxy.$echarts.dispose(document.getElementById('myMapBox')); 
		let myMapTemp = document.getElementById('myMap');
		let myMapBox = document.getElementById('myMapBox');
		// 动态修改图表的宽高，达到自适应的效果　
		var resizeWorldMapContainer = function() {
			myMapTemp.style.width = myMapBox.clientWidth + 'px';
			myMapTemp.style.height = myMapBox.clientHeight + 'px';
		};
		resizeWorldMapContainer();
		globalProxy.$echarts.registerMap(mapName.value, currentMapData.value.mapJsonData);
		myMap.value = globalProxy.$echarts.init(myMapTemp);
		mapOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			geo: {
				show: true,
				map: mapName.value,
				zoom: zoom.value,
				roam: false,
				itemStyle: {
					normal: {
						borderColor: "#06ddf4",
						borderWidth: 6,
						shadowColor: "#06ddf4",
						shadowOffsetX: 0,
						shadowOffsetY: 6,
						shadowBlur: 8
					},
				},
			},
			series: [{
				type: 'map',
				coordinateSystem: 'geo',
				name: mapName.value,
				map: mapName.value,
				roam: false,
				zoom: zoom.value,
				selectedMode: "single",
				geoIndex: 1,
				aspectScale: 0.75,
				showLegendSymbol: true, // 存在legend时显示
				label: {
					normal: {
						show: true,
						position: 'center',
						overflow: 'breakAll',
						textStyle: {
							fontFamily: 'Noto Sans SC',
							color: '#FFF',
							fontSize: fontSize.value,
							overflow: 'breakAll',
						},
						formatter: function(params) {
							let name = params.name;
							if(name=='向阳区' || name=='东风区' || name=='前进区' || name=='龙沙区' || name=="城子河区" || name=="鸡冠区" || name=="工农区" || name=="南山区"){
								return '';
							}
						}
					},
				},
				itemStyle: {
					normal: {
						areaColor: '#0B1069',
						borderColor: '#489ae9',
						borderWidth: 1
					},
					emphasis: {
						show: true,
						areaColor: '#21a6dd',
						borderColor: '#06f7ff',
						borderWidth: 4,
						color: '#FFFFFF',
						label: { color: '#FFFFFF'},
						itemStyle: { color: '#FFFFFF'},
					}
				},
				select:{
					disabled:false,
					itemStyle:{
						areaColor:'#21a6dd',
						borderColor: '#06f7ff',
						borderWidth: 1,
					},
					label: { color: '#FFFFFF'},
				}
			},
			{
				type: 'lines',
				symbol:'circle',
				symbolSize:[6,4],
				label:{
					show:true,
					formatter:'{b}',
					textStyle: {
						color: '#FFF',
						fontSize: fontSize.value,
						overflow: 'breakAll',
					},
				},
				lineStyle: {
					normal: {
						color:'yellow',
						width: 1, //尾迹线条宽度
						opacity: 0.9, //尾迹线条透明度
						curveness: 0.15, //尾迹线条曲直度
					},
				},
				data: pointList.value,
			}]
		};
		myMap.value.clear()
		myMap.value.setOption(mapOption.value);
		myMap.value.on('click',(params)=>{
			myMap.value.dispatchAction({
				type: 'unselect',
				name: params.name
			})
		});
		myMap.value.on('dblclick', echartsMapClick);
		myMap.value.on('mouseover', echartsMapMouseOver);
		window.addEventListener("resize", function() {
			resizeWorldMapContainer();
			myMap.value.resize();
		})
	}
	
	const echartsMapClick=(params)=>{
		//市级地图 双击直接返回
		if(mapLevel.value=='2') return false;
		cityName.value = params.name;
		//console.log(that.mapData.children);
		mapData.value.children && mapData.value.children.forEach(item => {
			if(item.name==cityName.value){
				let temp = {
					areaLevel: item.level,
					areaCode: item.code,
					areaName: item.name
				}
				rememberClickMapArea(temp);
				clickCity();
				if(cityName.value==userArea.value.areaName){
					showLastFlag.value = false;
				}else{
					showLastFlag.value = true;
				}
				// drawMap();
				myMap.value.dispatchAction({
					type: 'select',
					name: params.name
				})
			}
		})
	}

	//移入
	const echartsMapMouseOver=(params)=>{
		// myMap.value.dispatchAction({
		// 	type: 'select',
		// 	name: params.name
		// })
	}

	//根据地区加载地图json数据
	const getMapJsonData=()=>{
		currentMapData.value = {
			mapJsonData:allProv,
			cityName:cityName.value
		}
		showLastFlag.value = false;
		fontSize.value = oldScale<0.9?14*oldScale:13;
		if(cityName.value && cityName.value!='' && cityName.value.indexOf('黑龙江')>-1){
			mapLevel.value = '1';
		}
		if(!bl1){
			if(mapLevel.value == '1'){
				zoom.value = zoom.value - 0.1
			}
			if(mapLevel.value == '2'){
				zoom.value = zoom.value - 0.2
			}
		}
	}

	//地图上-单击某个地市-加载整个市的数据
	const clickCity=()=>{
		getMapJsonData();
	}

	const rememberClickMapArea=(data)=>{
		store.MAP_DATA(data);
		emits('changeCityCode');
	}

	//单击 上一页 按钮
	const clickLastBtn=()=>{
		currentMapData.value = provData.value
		cityName.value = provData.value.areaName
		zoom.value = defaultZoom.value;
		mapLevel.value = '1';
		getMapJsonData();
		drawMap();
		rememberClickMapArea(provData.value);
		showLastFlag.value = false;
	}

	//初始化进来-加载全省地图
	const initMapData=async ()=>{
		await defaultData();
		await drawMap();
	}

	const defaultData=()=>{
		cityName.value = userArea.value.areaName;
		zoom.value = defaultZoom.value;
		mapLevel.value = '1';
		getMapJsonData();
	}
	
	onMounted(() =>{
		nextTick(()=>{
			initMapData();
		})
		bBNum.value = window.winHeight/2 - 60
		if(oldScale<0.9){
			btnWidth.value = 134*oldScale
			btnHeight.value = 32*oldScale
		}else{
			btnWidth.value = 134
			btnHeight.value = 32
		}
	})

	onBeforeMount(()=> {
		if(loginType.value=='jcpt_login' && store.loginAreaData){
			let getCurrAreaData = store.loginAreaData;
			userArea.value = {
				areaLevel: getCurrAreaData.areaLevel,
				areaCode: getCurrAreaData.areaCode,
				areaName: getCurrAreaData.areaName
			};
			mapData.value = {
				children: [],
				level: getCurrAreaData.areaLevel,
				code: getCurrAreaData.areaCode,
				name: getCurrAreaData.areaName
			}
			//sessionStorage.setItem('mapData', JSON.stringify(that.mapData))
			emits('changeCityCode');
		}else{
			userArea.value = {
				areaLevel: 0,
				areaCode: '370000000000',
				areaName: '黑龙江省'
			};
		}
		if(demoDataFlag){
			mapData.value = {"children":[{"children":[{"children":[],"code":"370102000000","level":2,"name":"历下区"},{"children":[],"code":"370103000000","level":2,"name":"市中区"},{"children":[],"code":"370104000000","level":2,"name":"槐荫区"},{"children":[],"code":"370105000000","level":2,"name":"天桥区"},{"children":[],"code":"370111000000","level":2,"name":"济南1"},{"children":[],"code":"370112000000","level":2,"name":"历城区"},{"children":[],"code":"370113000000","level":2,"name":"长清区"},{"children":[],"code":"370124000000","level":2,"name":"平阴县"},{"children":[],"code":"370125000000","level":2,"name":"济阳县"},{"children":[],"code":"370126000000","level":2,"name":"商河县"},{"children":[],"code":"370181000000","level":2,"name":"章丘区"}],"code":"370100000000","level":1,"name":"鹤岗市"},{"children":[{"children":[],"code":"370202000000","level":2,"name":"市南区"},{"children":[],"code":"370203000000","level":2,"name":"市北区"},{"children":[],"code":"370211000000","level":2,"name":"黄岛区"},{"children":[],"code":"370212000000","level":2,"name":"崂山区"},{"children":[],"code":"370213000000","level":2,"name":"李沧区"},{"children":[],"code":"370214000000","level":2,"name":"城阳区"},{"children":[],"code":"370281000000","level":2,"name":"胶州市"},{"children":[],"code":"370282000000","level":2,"name":"即墨市"},{"children":[],"code":"370283000000","level":2,"name":"平度市"},{"children":[],"code":"370284000000","level":2,"name":"胶南市"},{"children":[],"code":"370285000000","level":2,"name":"莱西市"}],"code":"370200000000","level":1,"name":"七台河市"},{"children":[{"children":[],"code":"370302000000","level":2,"name":"淄川区"},{"children":[],"code":"370303000000","level":2,"name":"张店区"},{"children":[],"code":"370304000000","level":2,"name":"博山区"},{"children":[],"code":"370305000000","level":2,"name":"临淄区"},{"children":[],"code":"370306000000","level":2,"name":"周村区"},{"children":[],"code":"370312000000","level":2,"name":"高新技术产业开发区"},{"children":[],"code":"370321000000","level":2,"name":"桓台县"},{"children":[],"code":"370322000000","level":2,"name":"高青县"},{"children":[],"code":"370323000000","level":2,"name":"沂源县"}],"code":"370300000000","level":1,"name":"双鸭山市"},{"children":[{"children":[],"code":"370402000000","level":2,"name":"市中区"},{"children":[],"code":"370403000000","level":2,"name":"薛城区"},{"children":[],"code":"370404000000","level":2,"name":"峄城区"},{"children":[],"code":"370405000000","level":2,"name":"台儿庄区"},{"children":[],"code":"370406000000","level":2,"name":"山亭区"},{"children":[],"code":"370481000000","level":2,"name":"滕州市"}],"code":"370400000000","level":1,"name":"枣庄市"},{"children":[{"children":[],"code":"370502000000","level":2,"name":"东营区"},{"children":[],"code":"370503000000","level":2,"name":"河口区"},{"children":[],"code":"370521000000","level":2,"name":"垦利县"},{"children":[],"code":"370522000000","level":2,"name":"利津县"},{"children":[],"code":"370523000000","level":2,"name":"广饶县"}],"code":"370500000000","level":1,"name":"大庆市"},{"children":[{"children":[],"code":"370601000000","level":2,"name":"市辖区"},{"children":[],"code":"370602000000","level":2,"name":"芝罘区"},{"children":[],"code":"370611000000","level":2,"name":"福山区"},{"children":[],"code":"370612000000","level":2,"name":"牟平区"},{"children":[],"code":"370613000000","level":2,"name":"莱山区"},{"children":[],"code":"370614000000","level":2,"name":"开发区"},{"children":[],"code":"370634000000","level":2,"name":"长岛县"},{"children":[],"code":"370681000000","level":2,"name":"龙口市"},{"children":[],"code":"370682000000","level":2,"name":"莱阳市"},{"children":[],"code":"370683000000","level":2,"name":"莱州市"},{"children":[],"code":"370684000000","level":2,"name":"蓬莱市"},{"children":[],"code":"370685000000","level":2,"name":"招远市"},{"children":[],"code":"370686000000","level":2,"name":"栖霞市"},{"children":[],"code":"370687000000","level":2,"name":"海阳市"}],"code":"370600000000","level":1,"name":"绥化市"},{"children":[{"children":[],"code":"370702000000","level":2,"name":"潍城区"},{"children":[],"code":"370703000000","level":2,"name":"寒亭区"},{"children":[],"code":"370704000000","level":2,"name":"坊子区"},{"children":[],"code":"370705000000","level":2,"name":"奎文区"},{"children":[],"code":"370724000000","level":2,"name":"临朐县"},{"children":[],"code":"370725000000","level":2,"name":"昌乐县"},{"children":[],"code":"370781000000","level":2,"name":"青州市"},{"children":[],"code":"370782000000","level":2,"name":"诸城市"},{"children":[],"code":"370783000000","level":2,"name":"寿光市"},{"children":[],"code":"370784000000","level":2,"name":"安丘市"},{"children":[],"code":"370785000000","level":2,"name":"高密市"},{"children":[],"code":"370786000000","level":2,"name":"昌邑市"}],"code":"370700000000","level":1,"name":"潍坊市"},{"children":[{"children":[],"code":"370802000000","level":2,"name":"市中区"},{"children":[],"code":"370811000000","level":2,"name":"任城区"},{"children":[],"code":"370826000000","level":2,"name":"微山县"},{"children":[],"code":"370827000000","level":2,"name":"鱼台县"},{"children":[],"code":"370828000000","level":2,"name":"金乡县"},{"children":[],"code":"370829000000","level":2,"name":"嘉祥县"},{"children":[],"code":"370830000000","level":2,"name":"汶上县"},{"children":[],"code":"370831000000","level":2,"name":"泗水县"},{"children":[],"code":"370832000000","level":2,"name":"梁山县"},{"children":[],"code":"370881000000","level":2,"name":"曲阜市"},{"children":[],"code":"370882000000","level":2,"name":"兖州市"},{"children":[],"code":"370883000000","level":2,"name":"邹城市"}],"code":"370800000000","level":1,"name":"佳木斯市"},{"children":[{"children":[],"code":"370902000000","level":2,"name":"泰山区"},{"children":[],"code":"370911000000","level":2,"name":"岱岳区"},{"children":[],"code":"370921000000","level":2,"name":"宁阳县"},{"children":[],"code":"370923000000","level":2,"name":"东平县"},{"children":[],"code":"370982000000","level":2,"name":"新泰市"},{"children":[],"code":"370983000000","level":2,"name":"肥城市"}],"code":"370900000000","level":1,"name":"鸡西市"},{"children":[{"children":[],"code":"371002000000","level":2,"name":"环翠区"},{"children":[],"code":"371081000000","level":2,"name":"文登市"},{"children":[],"code":"371082000000","level":2,"name":"荣成县"},{"children":[],"code":"371083000000","level":2,"name":"乳山市"}],"code":"371000000000","level":1,"name":"齐齐哈尔市"},{"children":[{"children":[],"code":"371102000000","level":2,"name":"东港区"},{"children":[],"code":"371103000000","level":2,"name":"岚山区"},{"children":[],"code":"371121000000","level":2,"name":"五莲县"},{"children":[],"code":"371122000000","level":2,"name":"莒县"}],"code":"371100000000","level":1,"name":"牡丹江市"},{"children":[{"children":[],"code":"371202000000","level":2,"name":"莱城区"},{"children":[],"code":"371203000000","level":2,"name":"钢城区"}],"code":"371200000000","level":1,"name":"莱芜市"},{"children":[{"children":[],"code":"371302000000","level":2,"name":"兰山区"},{"children":[],"code":"371311000000","level":2,"name":"罗庄区"},{"children":[],"code":"371312000000","level":2,"name":"河东区"},{"children":[],"code":"371321000000","level":2,"name":"沂南县"},{"children":[],"code":"371322000000","level":2,"name":"郯城县"},{"children":[],"code":"371323000000","level":2,"name":"沂水县"},{"children":[],"code":"371324000000","level":2,"name":"苍山县"},{"children":[],"code":"371325000000","level":2,"name":"费县"},{"children":[],"code":"371326000000","level":2,"name":"平邑县"},{"children":[],"code":"371327000000","level":2,"name":"莒南县"},{"children":[],"code":"371328000000","level":2,"name":"蒙阴县"},{"children":[],"code":"371329000000","level":2,"name":"临沭县"}],"code":"371300000000","level":1,"name":"临沂市"},{"children":[{"children":[],"code":"371402000000","level":2,"name":"德城区"},{"children":[],"code":"371421000000","level":2,"name":"陵县"},{"children":[],"code":"371422000000","level":2,"name":"宁津县"},{"children":[],"code":"371423000000","level":2,"name":"庆云县"},{"children":[],"code":"371424000000","level":2,"name":"临邑县"},{"children":[],"code":"371425000000","level":2,"name":"齐河县"},{"children":[],"code":"371426000000","level":2,"name":"平原县"},{"children":[],"code":"371427000000","level":2,"name":"夏津县"},{"children":[],"code":"371428000000","level":2,"name":"武城县"},{"children":[],"code":"371481000000","level":2,"name":"乐陵市"},{"children":[],"code":"371482000000","level":2,"name":"禹城市"}],"code":"371400000000","level":1,"name":"哈尔滨市"},{"children":[{"children":[],"code":"371502000000","level":2,"name":"东昌府区"},{"children":[],"code":"371521000000","level":2,"name":"阳谷县"},{"children":[],"code":"371522000000","level":2,"name":"莘县"},{"children":[],"code":"371523000000","level":2,"name":"茌平县"},{"children":[],"code":"371524000000","level":2,"name":"东阿县"},{"children":[],"code":"371525000000","level":2,"name":"冠县"},{"children":[],"code":"371526000000","level":2,"name":"高唐县"},{"children":[],"code":"371581000000","level":2,"name":"临清市"}],"code":"371500000000","level":1,"name":"黑河市"},{"children":[{"children":[],"code":"371602000000","level":2,"name":"滨城区"},{"children":[],"code":"371621000000","level":2,"name":"惠民县"},{"children":[],"code":"371622000000","level":2,"name":"阳信县"},{"children":[],"code":"371623000000","level":2,"name":"无棣县"},{"children":[],"code":"371624000000","level":2,"name":"沾化县"},{"children":[],"code":"371625000000","level":2,"name":"博兴县"},{"children":[],"code":"371626000000","level":2,"name":"邹平县"}],"code":"371600000000","level":1,"name":"大兴安岭地区"},{"children":[{"children":[],"code":"371702000000","level":2,"name":"牡丹区"},{"children":[],"code":"371721000000","level":2,"name":"曹县"},{"children":[],"code":"371722000000","level":2,"name":"单县"},{"children":[],"code":"371723000000","level":2,"name":"成武县"},{"children":[],"code":"371724000000","level":2,"name":"巨野县"},{"children":[],"code":"371725000000","level":2,"name":"郓城县"},{"children":[],"code":"371726000000","level":2,"name":"鄄城县"},{"children":[],"code":"371727000000","level":2,"name":"定陶县"},{"children":[],"code":"371728000000","level":2,"name":"东明县"}],"code":"371700000000","level":1,"name":"伊春市"}],"code":"370000000000","level":0,"name":"黑龙江省"};;
			sessionStorage.setItem('mapData', JSON.stringify(mapData.value))
			currentMapData.value = {
				areaLevel: mapData.value.level,
				areaCode: mapData.value.code,
				areaName: mapData.value.name
			}
			provData.value = currentMapData.value;
			setTimeout(() => {
				rememberClickMapArea(provData.value);
			}, 600);
		}else{
			getArea(userArea.value).then(res => {
				//console.log(JSON.stringify(res));
				if(res && res.areaMapCode && res.areaMapCode!=''){
					mapData.value = res.areaMapCode
					sessionStorage.setItem('mapData', JSON.stringify(mapData.value))
					currentMapData.value = {
						areaLevel: mapData.value.level,
						areaCode: mapData.value.code,
						areaName: mapData.value.name
					}
					provData.value = currentMapData.value
					setTimeout(() => {
						rememberClickMapArea(provData.value);
					}, 600);
				}
			});
		}
	})

	onUnmounted(()=>{
		sessionStorage.removeItem('mapData');
	})

	// 公开数据
	defineExpose({
		initMapData
	})
</script>