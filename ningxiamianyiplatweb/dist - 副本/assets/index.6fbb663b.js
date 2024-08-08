import{p as e}from"./regex.fcd5bf03.js";import{_ as l,m as a}from"./index.143fdacc.js";import{r as t,Y as s,j as u,S as o,b as i,c as n,e as d,k as r,w as m,A as c,u as p,U as v,I as b,J as h,d as f,D as g,F as V}from"./vendor.cc783c3b.js";const y=e=>(g("data-v-551c8aa2"),e=e(),V(),e),x={class:"condition-row jzjlfull"},k={class:"item"},_=y((()=>c("span",{class:"required label"},"地区选择：",-1))),U={class:"item"},T={class:"flex"},j=y((()=>c("span",{class:"label",style:{"line-height":"32px",height:"32px","margin-left":"4px"}},"出生日期：",-1))),w={class:"item"},C=y((()=>c("span",{class:"label",style:{"line-height":"32px",height:"32px","margin-left":"4px"}},"年龄范围（岁）：",-1))),$=y((()=>c("span",{class:"util"},"至",-1))),q={class:"item"},D=y((()=>c("span",{class:"required label"},"接种日期：",-1))),L={class:"item"},W=y((()=>c("span",{class:"required label"},"户籍类型：",-1))),I={class:"item"},M=y((()=>c("span",{class:"required label"},"是否合格：",-1))),S={class:"item"},z=y((()=>c("span",{class:"required label"},"受种者标识：",-1)));var N=l({__name:"index",setup(l){const g=t([]),{appContext:V}=s(),y=V.config.globalProperties,N=a();t(0);const O=t(1);t(20);const R=t(0),A=t(!0),F=u([{label:"地区",prop:"areaName"},{label:"接种剂次数",prop:"jzjlCount"},{label:"总体完整",hasChildrenTitle:!0,list:[{label:"完整数",prop:"all",minWidth:"60"},{label:"完整率(%)",prop:"all_rate",minWidth:"60"}]},{label:"单项完整数",minWidth:"120",hasChildrenTitle:!0,list:[{label:"生产企业",prop:"fenterno",minWidth:"60"},{label:"疫苗批号",prop:"ymph",minWidth:"60"},{label:"疫苗有效期",prop:"ymyxq",minWidth:"60"},{label:"接种部位",prop:"fplacecode",minWidth:"60"},{label:"接种人员",prop:"jzys",minWidth:"60"},{label:"接种单位名称",prop:"basecode",minWidth:"60"}]}]),P=y.$commonOptions.heraStateList,J=y.$commonOptions.qualifiedList,Y=t(null),B=t(null),E=t(!1),G=t(!1),H=t("1"),K=t("0"),Q=t(""),X=t(""),Z=t(null),ee=t([]),le=t([y.$dateUtil.lastMonth(),y.$dateUtil.currentMonth()]);let ae=u({areaId:N.userInfo.areaId,areaCode:N.userInfo.areaCode,selectAreaLevel:sessionStorage.getItem("currentLevel"),skipValue:String(Number(sessionStorage.getItem("currentLevel"))+1)});const te=e=>{ae=e};o((()=>E.value),(e=>{ee.value=e?[y.$dateUtil.lastMonth(),y.$dateUtil.currentMonth()]:[]}),{immediate:!0,deep:!0}),o((()=>G.value),(e=>{e||(Q.value="",X.value="")}),{immediate:!0,deep:!0});const se=l=>("number"==typeof l&&(O.value=l),E.value||G.value?!G.value||Q.value&&X.value&&(!Q.value&&!X.value||e.test(Q.value))&&e.test(X.value)?G.value&&Q.value>X.value?y.$util.showMsg("开始年龄必须小于等于截止年龄!","error",5e3,!0):0==Z.value.needResiType.length?y.$util.showMsg("户籍类型必选！","error",5e3,!0):void("0"==H.value?(A.value=!1,F[2].list[0].label="不完整数",F[2].list[1].label="不完整率(%)",F[3].label="单项不完整数"):(A.value=!0,F[2].list[0].label="完整数",F[2].list[1].label="完整率(%)",F[3].label="单项完整数")):y.$util.showMsg("年龄范围必须是大于等于0的整数!","error",5e3,!0):y.$util.showMsg("出生日期或年龄范围(岁)必须选中一个!","error",5e3,!0)),ue=e=>{ae.selectAreaLevel=e-1},oe=()=>{};return(e,l)=>{const a=i("ss-area-areaTreeStation"),t=i("el-checkbox"),s=i("ss-month-range-new"),u=i("el-input"),o=i("ss-check-resiType"),V=i("el-option"),y=i("el-select"),N=i("ss-custom-button"),O=i("ss-query-bottom"),A=i("ss-condition-area"),ie=i("ss-custom-table");return n(),d("div",null,[r(A,null,{default:m((()=>[c("div",x,[c("div",k,[_,r(a,{modelValue:p(ae),"onUpdate:modelValue":l[0]||(l[0]=e=>v(ae)?ae.value=e:ae=e),getToLevelNum:5,onNodeClick:te,showSelect:!0,onSelectClick:ue},null,8,["modelValue"])]),c("div",U,[c("div",T,[r(t,{modelValue:E.value,"onUpdate:modelValue":l[1]||(l[1]=e=>E.value=e),size:"large",class:"ml20"},{default:m((()=>[j])),_:1},8,["modelValue"])]),r(s,{ref_key:"birthRef",ref:Y,defaultValue:ee.value,"onUpdate:defaultValue":l[2]||(l[2]=e=>ee.value=e),disabledFlag:!E.value},null,8,["defaultValue","disabledFlag"])]),c("div",w,[r(t,{modelValue:G.value,"onUpdate:modelValue":l[3]||(l[3]=e=>G.value=e),size:"large",class:"ml20"},{default:m((()=>[C])),_:1},8,["modelValue"]),r(u,{modelValue:Q.value,"onUpdate:modelValue":l[4]||(l[4]=e=>Q.value=e),maxlength:"3",min:"1",max:"999",readonly:!G.value,style:{width:"80px"}},null,8,["modelValue","readonly"]),$,r(u,{modelValue:X.value,"onUpdate:modelValue":l[5]||(l[5]=e=>X.value=e),maxlength:"3",min:"1",max:"999",readonly:!G.value,style:{width:"80px"}},null,8,["modelValue","readonly"])]),c("div",q,[D,r(s,{ref_key:"jzRef",ref:B,defaultValue:le.value,"onUpdate:defaultValue":l[6]||(l[6]=e=>le.value=e)},null,8,["defaultValue"])]),c("div",L,[W,r(o,{ref_key:"checkResiTypePage",ref:Z},null,512)]),c("div",I,[M,r(y,{modelValue:H.value,"onUpdate:modelValue":l[7]||(l[7]=e=>H.value=e)},{default:m((()=>[(n(!0),d(b,null,h(p(J),(e=>(n(),f(V,{label:e.name,key:e.code,value:e.code},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])]),c("div",S,[z,r(y,{modelValue:K.value,"onUpdate:modelValue":l[8]||(l[8]=e=>K.value=e)},{default:m((()=>[(n(!0),d(b,null,h(p(P),(e=>(n(),f(V,{key:e.code,label:e.name,value:e.code},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])]),r(O,{statisTics:!0,secTime:R.value},{default:m((()=>[r(N,{type:"query",onClick:se}),r(N,{type:"output",onClick:oe})])),_:1},8,["secTime"])])),_:1}),r(ie,{tableData:g.value,columnList:F,onOpenDetailDialog:e.openDetailDialog,total:0,remarkText:"① 单项完整为档案中输入此项信息即可；<br>② 总体完整为各单项完整均输入。"},null,8,["tableData","columnList","onOpenDetailDialog"])])}}},[["__scopeId","data-v-551c8aa2"]]);export{N as default};
