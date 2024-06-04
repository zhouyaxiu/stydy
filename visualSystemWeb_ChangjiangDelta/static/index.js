// 中间（门诊与档案数量查询

// 嘉善
var centreData1 = {
	stationNum: '4566',
	recordNum:"123"
}
// 吴江
var centreData2 = {
	stationNum: '46',
	recordNum:"890"
}
// 上海
var centreData3 = {
	stationNum: '346',
	recordNum:"76"
}
// 默认嘉善
var centreData0 = {
	stationNum: Number(window.centreData1.stationNum)+Number(window.centreData2.stationNum)+Number(window.centreData3.stationNum) || '0',
	recordNum:Number(window.centreData1.recordNum)+Number(window.centreData2.recordNum)+Number(window.centreData3.recordNum) || "0"
}
// 跨域累计接种情况统计（年龄组/人次数）

// 嘉善
var crossDomainInoculationAgeData1 = {
	"single": [
		"10",
		"65",
		"3",
		"7",
		"8",
		"33"
	],
	"total": [
		"40",
		"478",
		"23",
		"30",
		"63",
		"110"
	],
	"rate": []
}

// 吴江
var crossDomainInoculationAgeData2 = {
	"single": [
		"4",
		"76",
		"6",
		"13",
		"61",
		"33"
	],
	"total": [
		"34",
		"120",
		"23",
		"30",
		"63",
		"110"
	],
	"rate": []
}
// 青浦
var crossDomainInoculationAgeData3 = {
	"single": [
		"56",
		"43",
		"13",
		"21",
		"56",
		"56"
	],
	"total": [
		"135",
		"78",
		"23",
		"30",
		"63",
		"110"
	],
	"rate": []
}
function rateHandler(single,total,num){
  let len=single.length;
  if(!len) return
  let rate=[];
  for(let i=0;i<len;i++){
    rate.push((Number(single[i])/Number(total[i])*100).toFixed(2))
  }
  window['crossDomainInoculationAgeData'+Number(num)].rate=rate
}
rateHandler(window.crossDomainInoculationAgeData1.single,window.crossDomainInoculationAgeData1.total,1)
rateHandler(window.crossDomainInoculationAgeData2.single,window.crossDomainInoculationAgeData2.total,2)
rateHandler(window.crossDomainInoculationAgeData3.single,window.crossDomainInoculationAgeData3.total,3)
// 跨域累计接种情况统计（疫苗/人次数）
var crossDomainInoculationVaccineData0 = {
	vaccines:["乙肝疫苗","脊灰疫苗","百白破疫苗","白破疫苗","麻腮风疫苗","乙脑灭活疫苗","甲肝疫苗","流脑A","流脑A+C"],
	inflows:[43,21,86,27,53,53,38,51,43],
	outflows:[32,65,88,102,33,53,67,76,34]
}
var crossDomainInoculationVaccineData1 = {
	vaccines:["水痘疫苗","轮状病毒疫苗","流感疫苗","13价肺炎","23价肺炎","破伤风疫苗","甲肝疫苗","乙肝疫苗"],
	inflows:[45,221,34,87,84,24,45,32],
	outflows:[32,24,36,146,77,56,89,38]
}
// 档案流入流出情况统计
var recordFlowData0 = [
	{
		name: '魏塘街道',
		value:"123"
	},
    {
		name: '罗星街道',
		value:"25"
	},
    {
		name: '大云镇',
		value:"88"
	},
    {
		name: '惠民街道',
		value:"89"
	},
    {
		name: '干窑镇',
		value:"123"
	},
    {
		name: '西塘镇',
		value:"67"
	},
    {
		name: '天凝镇',
		value:"8"
	}, 
  // {
	// 	name: '天凝镇1',
	// 	value:"18"
	// },
]
var recordFlowData1 = [
	{
		name: '魏塘街道',
		value:"3"
	},
    {
		name: '罗星街道',
		value:"78"
	},
    {
		name: '大云镇',
		value:"43"
	},
    {
		name: '惠民街道',
		value:"45"
	},
    {
		name: '干窑镇',
		value:"67"
	},
    {
		name: '西塘镇',
		value:"43"
	},
    {
		name: '天凝镇',
		value:"56"
	},
]
sessionStorage.setItem('docInFlowtemp0',JSON.stringify(window.recordFlowData0))
sessionStorage.setItem('docInFlowtemp1',JSON.stringify(window.recordFlowData1))
// 左上（总交换量/流入流出）
var upperLeftData = {
	recordOutflow:335,
	recordInflow:523,
	flowChange:858
}


// 地图——受种者轨迹查询接口
var mapData01 = [
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '魏塘街道',
    vaccineName: '建档',
    date: '2020年3月02日',
    value: [120.895718, 30.848702]
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '干窑镇',
    vaccineName: '4价HPV(第一针)',
    date: '2020年5月12日',
    value: [120.906044, 30.902423]
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '干窑镇',
    vaccineName: '4价HPV(第二针)',
    date: '2020年8月19日',
    value: [120.906044, 30.902423]
  },
  {
    stationName: '金泽镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '金泽',
    vaccineName: '4价HPV(第三针)',
    date: '2020年10月19日',
    value: [120.978734, 31.051765]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '朱家角',
    vaccineName: '新冠疫苗(第一针)',
    date: '2021年2月19日',
    value: [121.052906, 31.089667]
  },
  {
    stationName: '汾湖镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    stationLocation: '',
    supName: '汾湖镇',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年5月19日',
    value: [120.782626, 31.048413]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    supName: '朱家角',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年10月11日',
    fchildNo: '',
    value: [121.052906, 31.089667]

  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '流感疫苗(第一针)',
    supName: '魏塘街道',
    date: '2021年12月12日',
    fchildNo: '',
    value: [120.895718, 30.848702]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '流感疫苗(第二针)',
    supName: '魏塘街道',
    date: '2022年2月12日',
    fchildNo: '',
    value: [120.895718, 30.848702]
  },

]
var mapData02 = [
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '干窑镇',
    vaccineName: '建档',
    date: '2020年3月11日',
    value: [120.906044, 30.902423]
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '干窑镇',
    vaccineName: '4价HPV(第一针)',
    date: '2020年5月12日',
    value: [120.906044, 30.902423]
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '西塘镇',
    vaccineName: '4价HPV(第二针)',
    date: '2020年8月19日',
    value: [120.867916, 30.964299]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '朱家角',
    vaccineName: '4价HPV(第三针)',
    date: '2020年12月11日',
    value: [121.052906, 31.089667]
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '青浦镇',
    vaccineName: '新冠疫苗(第一针)',
    date: '2021年2月19日',
    value:  [121.125028, 31.115401]
  },
  {
    stationName: '汾湖镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    stationLocation: '',
    supName: '汾湖镇',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年5月19日',
    value: [120.782626, 31.048413]
  },
  {
    stationName: '盛泽镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    stationLocation: '',
    supName: '盛泽镇',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年8月19日',
    value:[120.631423, 30.889634]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    supName: '朱家角',
    vaccineName: '流感疫苗(第一针)',
    date: '2021年10月11日',
    fchildNo: '',
    value: [121.052906, 31.089667]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    supName: '魏塘街道',
    vaccineName: '流感疫苗(第二针)',
    date: '2021年12月12日',
    fchildNo: '',
    value: [120.895718, 30.848702]
  },
]
// 输入溯源码
var mapData03 = [
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '魏塘街道',
    vaccineName: '乙肝疫苗1',
    date: '2022年5月12日',
    value: [120.895718, 30.848702]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '魏塘街道',
    vaccineName: '乙肝疫苗2',
    date: '2022年7月08日',
    value: [120.895718, 30.848702]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '魏塘街道',
    vaccineName: '乙肝疫苗3',
    date: '2022年9月22日',
    value: [120.895718, 30.848702]
  },
  
]
var mapData04 = [
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '西塘镇',
    vaccineName: '建档',
    date: '2020年3月19日',
    value: [120.867916, 30.964299]
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '西塘镇',
    vaccineName: '4价HPV(第一针)',
    date: '2020年5月09日',
    value: [120.867916, 30.964299]
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '西塘镇',
    vaccineName: '4价HPV(第二针)',
    date: '2020年8月06日',
    value: [120.867916, 30.964299]
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '干窑镇',
    vaccineName: '4价HPV(第三针)',
    date: '2020年11月12日',
    value: [120.906044, 30.902423]
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '青浦镇',
    vaccineName: '新冠疫苗(第一针)',
    date: '2021年2月19日',
    value:  [121.125028, 31.115401]
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '青浦镇',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年4月10日',
    value:  [121.125028, 31.115401]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    supName: '朱家角',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年8月17日',
    fchildNo: '',
    value: [121.052906, 31.089667]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    supName: '朱家角',
    vaccineName: '流感疫苗(第一针)',
    date: '2021年10月11日',
    fchildNo: '',
    value: [121.052906, 31.089667]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    supName: '魏塘街道',
    vaccineName: '流感疫苗(第二针)',
    date: '2021年12月12日',
    fchildNo: '',
    value: [120.895718, 30.848702]
  },

]
var mapData05 = [
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '西塘镇',
    vaccineName: '建档',
    date: '2020年4月09日',
    value: [120.867916, 30.964299]
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    supName: '西塘镇',
    vaccineName: '4价HPV(第一针)',
    date: '2020年6月19日',
    value: [120.867916, 30.964299]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    supName: '魏塘街道',
    vaccineName: '4价HPV(第二针)',
    date: '2020年8月12日',
    fchildNo: '',
    value: [120.895718, 30.848702]
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    supName: '魏塘街道',
    vaccineName: '4价HPV(第三针)',
    date: '2020年10月13日',
    fchildNo: '',
    value: [120.895718, 30.848702]
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    supName: '朱家角',
    vaccineName: '新冠疫苗(第一针)',
    date: '2020年12月14日',
    fchildNo: '',
    value: [121.052906, 31.089667]
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '青浦镇',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年2月19日',
    value:  [121.125028, 31.115401]
  },
  {
    stationName: '盛泽镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    stationLocation: '',
    supName: '盛泽镇',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年4月09日',
    value:[120.631423, 30.889634]
  },

]

var mapDataTable = [
  {
    fchildno: '320584210120107100',
    mapData: mapData01,
  },
  {
    fchildno: '320584210120107200',
    mapData: mapData02
  },
  {
    cid: '330421201809036472',
    mapData: mapData04
  },
  {
    cid: '330421201810260471',
    mapData: mapData05
  },
  {
    imuno: '815449505000',
    mapData: mapData03
  }
]



// 时间轴——受种者轨迹查询接口

var trackData01= [
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '建档',
    date: '2020年3月02日',
    fchildNo: '',
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV(第一针)',
    date: '2020年5月12日',
    fchildNo: '',
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV疫苗(第二针)',
    date: '2020年8月19日',
    fchildNo: '',
  },
  {
    stationName: '金泽镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '4价HPV(第三针)',
    date: '2020年10月19日',
    fchildNo: '',
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '新冠疫苗(第一针)',
    date: '2021年2月19日',
    fchildNo: '',
  },
  {
    stationName: '汾湖镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年5月19日',
    fchildNo: '',
  },

  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年10月11日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '流感疫苗(第一针)',
    date: '2021年12月12日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '流感疫苗(第二针)',
    date: '2022年2月12日',
    fchildNo: '',
  },
]
var trackData02= [
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '建档',
    date: '2020年3月11日',
    fchildNo: '',
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV疫苗(第一针)',
    date: '2020年5月12日',
    fchildNo: '',
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV疫苗(第二针)',
    date: '2020年8月19日',
    fchildNo: '',
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '4价HPV疫苗(第三针)',
    date: '2020年12月11日',
    fchildNo: '',
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    supName: '青浦镇',
    vaccineName: '新冠疫苗(第一针)',
    date: '2021年2月19日',
  },
  {
    stationName: '汾湖镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年5月19日',
    fchildNo: '',
  },
  {
    stationName: '盛泽镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年8月19日',
    fchildNo: '',
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '流感疫苗(第一针)',
    date: '2021年10月11日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '流感疫苗(第二针)',
    date: '2021年12月12日',
    fchildNo: '',
  },
]
// 输入溯源码
var trackData03= [
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '乙肝疫苗1',
    date: '2022年5月12日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '乙肝疫苗2',
    date: '2022年7月08日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '乙肝疫苗3',
    date: '2022年9月22日',
    fchildNo: '',
  },
]

var trackData04 = [
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '建档',
    date: '2020年3月19日',
    fchildNo: '',
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV疫苗(第一针)',
    date: '2020年5月09日',
    fchildNo: '',
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV疫苗(第二针)',
    date: '2020年8月06日',
    fchildNo: '',
  },
  {
    stationName: '干窑镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV(第三针)',
    date: '2020年11月12日',
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    vaccineName: '新冠疫苗(第一针)',
    date: '2021年2月19日',
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年4月10日',
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年8月17日',
    fchildNo: '',
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '流感疫苗(第一针)',
    date: '2021年10月11日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '流感疫苗(第二针)',
    date: '2021年12月12日',
    fchildNo: '',
  },
]
var trackData05 = [
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    vaccineName: '建档',
    date: '2020年4月09日',
  },
  {
    stationName: '西塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    stationLocation: '',
    vaccineName: '4价HPV(第一针)',
    date: '2020年6月19日',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV(第二针)',
    date: '2020年8月12日',
    fchildNo: '',
  },
  {
    stationName: '魏塘镇社区卫生服务中心',
    stationCode: '',
    stationType: '0',
    vaccineName: '4价HPV(第三针)',
    date: '2020年10月13日',
    fchildNo: '',
  },
  {
    stationName: '朱家角社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    vaccineName: '新冠疫苗(第一针)',
    date: '2020年12月14日',
    fchildNo: '',
  },
  {
    stationName: '青浦镇社区卫生服务中心',
    stationCode: '',
    stationType: '2',
    stationLocation: '',
    vaccineName: '新冠疫苗(第二针)',
    date: '2021年2月19日',
  },
  {
    stationName: '盛泽镇社区卫生服务中心',
    stationCode: '',
    stationType: '1',
    stationLocation: '',
    vaccineName: '新冠疫苗(第三针)',
    date: '2021年4月09日',
  },
]
var trackDataTable = [
  {
    fchildno: '320584210120107100',
    trackData: trackData01,
  },
  {
    fchildno: '320584210120107200',
    trackData: trackData02
  },
  {
    cid: '330421201809036472',
    trackData: trackData04
  },
  {
    cid: '330421201810260471',
    trackData: trackData05
  },
  {
    imuno: '815449505000',
    trackData: trackData03
  }
]