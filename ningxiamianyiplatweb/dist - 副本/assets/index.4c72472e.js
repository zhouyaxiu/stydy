var e=Object.defineProperty,a=Object.defineProperties,l=Object.getOwnPropertyDescriptors,t=Object.getOwnPropertySymbols,i=Object.prototype.hasOwnProperty,n=Object.prototype.propertyIsEnumerable,o=(a,l,t)=>l in a?e(a,l,{enumerable:!0,configurable:!0,writable:!0,value:t}):a[l]=t,r=(e,a)=>{for(var l in a||(a={}))i.call(a,l)&&o(e,l,a[l]);if(t)for(var l of t(a))n.call(a,l)&&o(e,l,a[l]);return e},d=(e,t)=>a(e,l(t));import{Q as s,R as c,S as u,T as p}from"./gjdc.b3af9cfa.js";import{r as m,S as v,b,c as g,d as f,w as h,A as y,k as C,l as w,B as k,U as N,Y as x,n as _,j,e as T,f as P,u as S}from"./vendor.cc783c3b.js";import{m as V}from"./index.143fdacc.js";const U={__name:"dialogInfo",props:{visible:{type:Boolean,default:!1},detailData:{type:Object,default:{}}},emits:["close"],setup(e,{emit:a}){const l=e,{appContext:t}=x(),i=t.config.globalProperties,n=m([]),o=m(0),u=m(1),p=m(20),j=m({}),T=m(!1),P=e=>{if(!l.detailData.areaCode||""==l.detailData.areaCode)return!1;"number"==typeof e&&(u.value=e);const a=d(r({},l.detailData),{pageNum:u.value,operateType:i.$consts.OPTYPE_QUERY});j.value=a,s(a).then((e=>{n.value=e.data.data.list,o.value=e.data.data.total}))};v((()=>l.detailData.areaCode),(e=>{e&&P()}),{deep:!0,immediate:!0});const S=()=>{a("close",!0)},V=()=>{j.value?0!=n.value.length?T.value=!0:_.error("暂无需要导出的数据！"):_.error("查询后才可以导出！")},U=()=>{T.value=!1},D=()=>{let e=d(r({},l.detailData),{areaCode:l.detailData.areaCode,areaName:l.detailData.areaName,pageNum:u.value,operateType:i.$consts.OPTYPE_OUTPUT});c(e)};return(a,l)=>{const t=b("ss-pagination"),i=b("ss-custom-button"),r=b("el-table-column"),d=b("ss-table"),s=b("ss-export-dialog"),c=b("el-dialog");return g(),f(c,{title:"详细列表",modelValue:e.visible,"onUpdate:modelValue":l[0]||(l[0]=e=>N(visible)?visible.value=e:null),width:"70%","before-close":S,"close-on-click-modal":!1,class:"userManageFormDialog","append-to-body":!0},{default:h((()=>[y("div",null,[C(t,{total:o.value,pageIndex:u.value,pageSize:p.value,onPageChange:P,style:{width:"calc(100% - 130px)",float:"left"}},null,8,["total","pageIndex","pageSize"]),C(i,{type:"output",onClick:V,style:{display:"inline-block",float:"right","margin-right":"16px"}})]),C(d,{data:n.value,maxHeight:400,class:"xgRptZL-table"},{default:h((()=>[C(r,{align:"center",type:"index","min-width":"60",label:"序号",fixed:""},{default:h((e=>[w(k((u.value-1)*p.value+e.$index+1),1)])),_:1}),C(r,{prop:"fchildno",align:"center","min-width":"200",label:"预防接种档案编号"}),C(r,{prop:"name",align:"center","min-width":"150",label:"受种者姓名"}),C(r,{prop:"fidCardTypeText",align:"center","min-width":"120",label:"身份证件类别"}),C(r,{prop:"fidCardType",align:"center","min-width":"150",label:"身份证件类别代码"}),C(r,{prop:"cid",align:"center","min-width":"160",label:"身份证件号码"}),C(r,{prop:"sexCn",align:"center","min-width":"100",label:"性别"}),C(r,{prop:"sexCodeCn",align:"center","min-width":"90",label:"性别代码"}),C(r,{prop:"birth",align:"center","min-width":"140",label:"受种者出生时间"}),C(r,{prop:"nationName",align:"center","min-width":"100",label:"国籍"}),C(r,{prop:"fnationAlityCode",align:"center","min-width":"100",label:"国籍代码"}),C(r,{prop:"crowdKindCnName",align:"center","min-width":"200",label:"人群分类","show-overflow-tooltip":""}),C(r,{prop:"fcrowdKindCode",align:"center","min-width":"120",label:"人群分类代码"}),C(r,{prop:"jzrq",align:"center","min-width":"170",label:"疫苗接种时间"}),C(r,{prop:"jc",align:"center","min-width":"90",label:"疫苗剂次"}),C(r,{prop:"compShortNameCn",align:"center","min-width":"120",label:"疫苗生产企业"}),C(r,{prop:"compGjCode",align:"center","min-width":"150",label:"疫苗生产企业代码"}),C(r,{prop:"ymph",align:"center","min-width":"100",label:"疫苗批号"}),C(r,{prop:"fmonitorCode",align:"center","min-width":"200",label:"疫苗追溯码"}),C(r,{prop:"stationNameCn",align:"center","min-width":"220",label:"接种单位"}),C(r,{prop:"stationCodeCn",align:"center","min-width":"200",label:"接种单位编码"}),C(r,{prop:"counNameCn",align:"center","min-width":"100",label:"区县名称"}),C(r,{prop:"counCodeCn",align:"center","min-width":"100",label:"区县编码"}),C(r,{prop:"cityNameCn",align:"center","min-width":"100",label:"地市名称"}),C(r,{prop:"cityCodeCn",align:"center","min-width":"100",label:"地市编码"})])),_:1},8,["data"]),C(s,{onCallback:D,onHandleClose:U,total:o.value,typeStr:"新冠病毒疫苗接种记录质量控制报表详细列表",dialogVisible:T.value},null,8,["total","dialogVisible"])])),_:1},8,["modelValue"])}}},D={class:"condition-row"},O={class:"item"},z=y("span",{class:"label required"},"选择地区：",-1),I={class:"item"},$={class:"item"},A={key:0},E=["onClick"],q={key:1},L={key:0},Y=["onClick"],M={key:1},R={key:0},H=["onClick"],Q={key:1},B={key:0},F=["onClick"],K={key:1},G={key:0},Z=["onClick"],J={key:1},W={key:0},X=["onClick"],ee={key:1},ae={key:0},le=["onClick"],te={key:1},ie={__name:"index",setup(e){const a=V(),{appContext:l}=x(),t=l.config.globalProperties,i=m([t.$dateUtil.dayAddNum(t.$dateUtil.getMonthDayNum(t.$dateUtil.lastMonthStartDay())),t.$dateUtil.currentDate()]),n=m([t.$dateUtil.dayAddNum(7),t.$dateUtil.currentDate()]),o=m([]),s=m(null),c=m(0),v=m(!1),ie=m({}),ne=m(""),oe=m(null);let re=j({areaId:a.userInfo.areaId,areaCode:a.userInfo.areaCode,areaName:a.userInfo.areaName,selectAreaLevel:sessionStorage.getItem("currentLevel"),skipValue:String(Number(sessionStorage.getItem("currentLevel"))+1)});const de=e=>{s.value.setheight()},se=e=>{re=e,re.skipValue=String(Number(re.selectAreaLevel)+1)},ce=e=>{re.skipValue=e},ue=()=>{let e=i.value[0]||"",l=i.value[1]||"";if(!e||""==e||!l||""==l)return _.error("接种日期为必填项，请填写完整再查询！"),!1;let r=n.value[0]||"",d=n.value[1]||"";if(!r||""==r||!d||""==d)return _.error("出生日期为必填项，请填写完整再查询！"),!1;let s={areaCode:re.areaCode,areaName:re.areaName,skipLevel:re.skipValue,birthStart:r,birthEnd:d,jzrqStart:e,jzrqEnd:l,operateType:t.$consts.OPTYPE_QUERY,cModId:a.moduleId};oe.value=s,ne.value=re.areaName+" "+re.areaCode,u(s).then((e=>{o.value=e.data.data,c.value=e.data.sec})).catch((()=>{}))},pe=()=>{if(!oe.value)return void _.error("查询后才可以导出！");if(1===o.value.length)return void _.error("暂无需要导出的数据！");oe.value.selectAreaName=ne.value;let e=d(r({},oe.value),{operateType:t.$consts.OPTYPE_OUTPUT});p(e)},me=(e,a)=>{let l=e,t=l.areaCode;if(!t||""==t)return _.error("所选中的行的区划代码为空！"),!1;let i=l.areaName;"合计"==i&&(i=ne.value),ie.value=d(r({},oe.value),{selectAreaCode:t,queryType:a,selectAreaName:i}),v.value=!0},ve=()=>{v.value=!1};return(e,a)=>{const l=b("ss-area-areaTreeStation"),t=b("ss-date-range"),r=b("ss-custom-button"),d=b("ss-query-bottom"),u=b("ss-condition-area"),p=b("el-table-column"),m=b("ss-table");return g(),T("div",null,[C(u,{onSetConditionFlag:de},{default:h((()=>[y("div",D,[y("div",O,[z,C(l,{modelValue:S(re),"onUpdate:modelValue":a[0]||(a[0]=e=>N(re)?re.value=e:re=e),getToLevelNum:5,onNodeClick:se,showSelect:!0,onSelectClick:ce},null,8,["modelValue"])]),y("div",I,[C(t,{title:"接种日期",defaultValue:i.value,"onUpdate:defaultValue":a[1]||(a[1]=e=>i.value=e)},null,8,["defaultValue"])]),y("div",$,[C(t,{title:"出生日期",defaultValue:n.value,"onUpdate:defaultValue":a[2]||(a[2]=e=>n.value=e)},null,8,["defaultValue"])])]),C(d,{secTime:c.value},{default:h((()=>[C(r,{type:"query",onClick:ue}),C(r,{type:"output",onClick:pe})])),_:1},8,["secTime"])])),_:1}),C(m,{data:o.value,ref_key:"tabPage",ref:s},{default:h((()=>[C(p,{prop:"areaName",align:"center","min-width":"250",label:"地区"},{default:h((({row:e})=>[w(k(e.areaName+" "+e.areaCode),1)])),_:1}),C(p,{align:"center",label:"总体数据情况（人数）"},{default:h((()=>[C(p,{prop:"childCount",align:"center","min-width":"90",label:"总数"}),C(p,{align:"center","min-width":"100",label:"不合格数"},{default:h((({row:e})=>[e.all&&e.all>0?(g(),T("div",A,[y("div",{class:"link-text",onClick:a=>me(e,"all")},k(e.all),9,E)])):(g(),T("div",q,k(e.all),1))])),_:1}),C(p,{prop:"allHgrate",align:"center","min-width":"150",label:"合格率"})])),_:1}),C(p,{align:"center",label:"单项不合格数（接种数）"},{default:h((()=>[C(p,{align:"center","min-width":"100",label:"疫苗剂次"},{default:h((({row:e})=>[e.fbactid&&e.fbactid>0?(g(),T("div",L,[y("div",{class:"link-text",onClick:a=>me(e,"fbactid")},k(e.fbactid),9,Y)])):(g(),T("div",M,k(e.fbactid),1))])),_:1}),C(p,{align:"center","min-width":"100",label:"疫苗批号"},{default:h((({row:e})=>[e.ymph&&e.ymph>0?(g(),T("div",R,[y("div",{class:"link-text",onClick:a=>me(e,"ymph")},k(e.ymph),9,H)])):(g(),T("div",Q,k(e.ymph),1))])),_:1}),C(p,{align:"center","min-width":"100",label:"接种部位"},{default:h((({row:e})=>[e.placecode&&e.placecode>0?(g(),T("div",B,[y("div",{class:"link-text",onClick:a=>me(e,"placecode")},k(e.placecode),9,F)])):(g(),T("div",K,k(e.placecode),1))])),_:1}),C(p,{align:"center","min-width":"100",label:"接种医生"},{default:h((({row:e})=>[e.jzys&&e.jzys>0?(g(),T("div",G,[y("div",{class:"link-text",onClick:a=>me(e,"jzys")},k(e.jzys),9,Z)])):(g(),T("div",J,k(e.jzys),1))])),_:1}),C(p,{align:"center","min-width":"100",label:"疫苗追溯码"},{default:h((({row:e})=>[e.zsm&&e.zsm>0?(g(),T("div",W,[y("div",{class:"link-text",onClick:a=>me(e,"zsm")},k(e.zsm),9,X)])):(g(),T("div",ee,k(e.zsm),1))])),_:1}),C(p,{align:"center","min-width":"100",label:"接种单位编码"},{default:h((({row:e})=>[e.stationcode&&e.stationcode>0?(g(),T("div",ae,[y("div",{class:"link-text",onClick:a=>me(e,"stationcode")},k(e.stationcode),9,le)])):(g(),T("div",te,k(e.stationcode),1))])),_:1})])),_:1})])),_:1},8,["data"]),v.value?(g(),f(U,{key:0,visible:v.value,onClose:ve,detailData:ie.value},null,8,["visible","detailData"])):P("",!0)])}}};export{ie as default};
