var e=Object.defineProperty,a=Object.defineProperties,t=Object.getOwnPropertyDescriptors,l=Object.getOwnPropertySymbols,r=Object.prototype.hasOwnProperty,s=Object.prototype.propertyIsEnumerable,o=(a,t,l)=>t in a?e(a,t,{enumerable:!0,configurable:!0,writable:!0,value:l}):a[t]=l;import{y as u,z as n}from"./gjdc.b3af9cfa.js";import{m as i}from"./index.143fdacc.js";import{Y as c,r as d,j as p,o as m,b as v,c as f,e as b,k as g,w as N,A as y,u as C,U as h}from"./vendor.cc783c3b.js";const O={class:"finished"},w={class:"condition-row"},V={class:"item"},j=y("span",{class:"label required"},"地区：",-1),k={class:"item"},L=y("span",{class:"label required"},"接种日期：",-1),S={class:"item"},P=y("span",{class:"label required"},"制品编码：",-1),T={__name:"index",setup(e){const{appContext:T}=c(),I=T.config.globalProperties,U=i(),D=d([]),$=d(0),_=d(null),q=p([{label:"地区",prop:"areaName",minWidth:"180",formatter:function(e){return e.areaName+" "+e.areaCode.slice(0,10)}},{label:"接种人数",prop:"allCount",minWidth:"100"},{label:"已完成免疫程序人数",prop:"hasFinishzCount",minWidth:"100"},{label:"完成率（%）",prop:"completionRate",minWidth:"120"}]),A=d(null);let M=p({areaId:U.userInfo.areaId,areaCode:U.userInfo.areaCode,selectAreaLevel:sessionStorage.getItem("currentLevel"),skipValue:String(Number(sessionStorage.getItem("currentLevel"))+1),areaName:U.userInfo.areaName});const x=d([I.$dateUtil.dayAddNum(7),I.$dateUtil.currentDate()]),z=()=>{let e=x.value[0],n=x.value[1];if(!e||!n)return I.$util.showMsg("请选择接种日期范围！","error",5e3,!0);const i={areaCode:M.areaCode,areaLevel:M.selectAreaLevel,showLevel:"5"==M.skipValue?4:M.skipValue,endYzDate:n,vaccNo:_.value.vaccNo,startYzDate:e};let c=_.value.vaccNo?_.value.vaccName:"";var d,p;A.value=(d=((e,a)=>{for(var t in a||(a={}))r.call(a,t)&&o(e,t,a[t]);if(l)for(var t of l(a))s.call(a,t)&&o(e,t,a[t]);return e})({},i),p={areaName:M.areaName+M.areaCode.slice(0,10),vaccName:c},a(d,t(p))),u(i).then((e=>{200===e.data.code?(D.value=e.data.data.data,$.value=e.data.data.sec):e.data.popMsg&&($.value="",D.value=[],I.$util.showMsg(e.data.popMsg,"error"))}))},W=e=>{M=e,M.skipValue=String(Number(M.selectAreaLevel)+1)},Y=e=>{M.skipValue=e},E=()=>{if(!A.value||A.value.areaCode!=M.areaCode)return I.$util.showMsg("查询后才可以导出！","error",5e3,!0);let e=JSON.parse(JSON.stringify(A.value));e.operateType=I.$consts.OPTYPE_OUTPUT,n(e)};return m((()=>{})),(e,a)=>{const t=v("ss-area-areaTreeStation"),l=v("ss-date-range-new"),r=v("ss-product-code"),s=v("ss-custom-button"),o=v("ss-query-bottom"),u=v("ss-condition-area"),n=v("ss-custom-table");return f(),b("div",O,[g(u,null,{default:N((()=>[y("div",w,[y("div",V,[j,g(t,{modelValue:C(M),"onUpdate:modelValue":a[0]||(a[0]=e=>h(M)?M.value=e:M=e),getToLevelNum:5,showSelect:!0,onNodeClick:W,onSelectClick:Y},null,8,["modelValue"])]),y("div",k,[L,g(l,{defaultValue:x.value,"onUpdate:defaultValue":a[1]||(a[1]=e=>x.value=e)},null,8,["defaultValue"])]),y("div",S,[P,g(r,{ref_key:"selectedVaccNo",ref:_},null,512)])]),g(o,{statisTics:!1,secTime:$.value},{default:N((()=>[g(s,{type:"query",onClick:z}),g(s,{type:"output",onClick:E})])),_:1},8,["secTime"])])),_:1}),g(n,{tableData:D.value,columnList:q},null,8,["tableData","columnList"])])}}};export{T as default};
