import{a as e,u as a,m as l}from"./index.143fdacc.js";import{Y as s,r as t,j as u,b as o,c as r,e as i,k as n,w as d,A as c,u as p,U as m,I as v,J as g,d as f}from"./vendor.cc783c3b.js";const h={class:"condition-row"},b={class:"item"},y=c("span",{class:"label required"},"选择地区：",-1),V={class:"item"},k={class:"flex"},C=c("span",{class:"mr10 label",style:{"line-height":"32px",height:"32px","margin-left":"4px"}},"出生日期：",-1),T={class:"item"},N=c("span",{class:"required label"},"受种者标识：",-1),L={class:"condition-row"},x={class:"item"},S=c("span",{class:"required label"},"户籍类型：",-1),w={__name:"index",setup(w){const{appContext:U}=s(),$=U.config.globalProperties,I=t([]),_=l(),P=t(0),j=u([{label:"地区名称",prop:"areaName",minWidth:"120"},{label:"受种者数量",prop:"childCount"}]),q=t(!1),M=t([]),O=t(null),A=t(""),D=$.$commonOptions.heraStateList;let R=u({areaId:_.userInfo.areaId,areaCode:_.userInfo.areaCode,areaName:_.userInfo.areaName,selectAreaLevel:sessionStorage.getItem("currentLevel"),skipValue:String(Number(sessionStorage.getItem("currentLevel"))+1)});const F=t(null),J=e=>{M.value=e?[$.$dateUtil.currentDate(),$.$dateUtil.currentDate()]:[]},B=()=>{if(0==O.value.needResiType.length)return $.$util.showMsg("户籍类型必选！","error",5e3,!0);const l={areaCode:R.areaCode,areaLevel:R.selectAreaLevel,skipValue:R.skipValue,startBirth:M.value[0],endBirth:M.value[1],regtypes:O.value.needResiType.join(","),fisadult:A.value};var s;F.value=l,(s=l,e.axiosPost(`${a.childCount}/queryChildCount`,s)).then((e=>{if(e.data.popMsg)return I.value=[],P.value="",$.$util.showMsg(e.data.popMsg,"error",5e3,!0);I.value=e.data.data.data,P.value=e.data.data.sec}))},Y=()=>{let l=JSON.parse(JSON.stringify(F.value));if(!F.value||F.value.areaCode!=R.areaCode||O.value.needResiType.join(",")!=l.regtypes)return $.$util.showMsg("查询后才可以导出！","error",5e3,!0);let s=[];var t;O.value.resiTypeList.map((e=>O.value.resiType.map((a=>{e.id==a&&s.push(e.name)})))),l.areaName=R.areaName+" "+R.areaCode.slice(0,10),l.skipName=1==R.skipValue?"以地市为单位查看":2==R.skipValue?"以区县为单位查看":3==R.skipValue?"以乡镇为单位查看":4==R.skipValue?"以接种点为单位查看":"",l.regtypesName=s.join(","),l.operateType=$.$consts.OPTYPE_OUTPUT,t=l,e.downloadFile(`${a.childCount}/outputChildCount`,t)},z=e=>{R=e,R.skipValue=String(Number(R.selectAreaLevel)+1)},E=e=>{R.skipValue=e};return(e,a)=>{const l=o("ss-area-areaTreeStation"),s=o("el-checkbox"),t=o("ss-date-range-new"),u=o("el-option"),w=o("el-select"),U=o("ss-check-resiType"),$=o("ss-custom-button"),_=o("ss-query-bottom"),F=o("ss-condition-area"),W=o("ss-custom-table");return r(),i("div",null,[n(F,{ref:"conditionPage"},{default:d((()=>[c("div",h,[c("div",b,[y,n(l,{modelValue:p(R),"onUpdate:modelValue":a[0]||(a[0]=e=>m(R)?R.value=e:R=e),getToLevelNum:5,onNodeClick:z,showSelect:!0,onSelectClick:E},null,8,["modelValue"])]),c("div",V,[c("div",k,[n(s,{modelValue:q.value,"onUpdate:modelValue":a[1]||(a[1]=e=>q.value=e),onChange:J,size:"large",class:"ml20"},{default:d((()=>[C])),_:1},8,["modelValue"])]),n(t,{disabledFlag:!q.value,defaultValue:M.value,"onUpdate:defaultValue":a[2]||(a[2]=e=>M.value=e)},null,8,["disabledFlag","defaultValue"])]),c("div",T,[N,n(w,{modelValue:A.value,"onUpdate:modelValue":a[3]||(a[3]=e=>A.value=e),clearable:"",style:{position:"relative"}},{default:d((()=>[(r(!0),i(v,null,g(p(D),(e=>(r(),f(u,{key:e.code,label:e.name,value:e.code},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])]),c("div",L,[c("div",x,[S,n(U,{ref_key:"checkResiTypePage",ref:O},null,512)])]),n(_,{statisTics:!0,secTime:P.value},{default:d((()=>[n($,{type:"query",onClick:B}),n($,{type:"output",onClick:Y})])),_:1},8,["secTime"])])),_:1},512),n(W,{tableData:I.value,columnList:j},null,8,["tableData","columnList"])])}}};export{w as default};
