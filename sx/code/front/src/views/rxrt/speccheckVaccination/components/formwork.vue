<template>
  <div class="notice">
    <el-descriptions direction="vertical" :column="1" border>
      <el-descriptions-item labelClassName="labelStyle">
        <template slot="label">
          <span class="title" style="text-align: center;">催种通知单</span>
        </template>
        <div style="color:#333;">
          <div><span class="bluetext">{{ stuData.stuName }}</span>家长：</div>
          <span class="mt_10">
            <span class="bluetext">{{ stuData.schName }}</span>学校
            <span class="bluetext">{{ getClaLevelNameNew(stuData.claLevel) }}{{ stuData.claShortname }}</span>
            学生（姓名）<span class="bluetext">{{ stuData.stuName }}</span> 出生日期<span class="bluetext">{{ stuData.stuBirth }}</span>
            家长（姓名）<span class="bluetext">{{ stuData.stuFathername }} {{ stuData.stuMothername }}</span>，
            <span>
              经仔细查验预防接种证：发现该学生有下列疫苗未按规定程序完成预防接种，请您携带孩子凭此通知单到<span class="bluetext">{{ stuData.stationName }}</span>
              社区卫生服务中心办理疫苗补种手续。</span>
          </span>
          <div class="mt_10">
            <table class="print-table-crown" >
              <tr>
                <th width="180">序号</th>
                <th width="180">疫苗名称</th>
                <th style="display:table-cell">剂次</th>
              </tr>
              <tr v-if="stuData.firstDose==1">
                <td>1</td>
                <td>{{vaccinumName!='麻腮风疫苗'?'*':''}}{{ vaccinumName }}</td>
                <td>第一剂次</td>
              </tr>
              <tr v-if="stuData.secondDose==1 && vaccinumName=='水痘疫苗'">
                <td>{{ stuData.firstDose==0?1:2 }}</td>
                <td>*{{ vaccinumName }}</td>
                <td>第二剂次</td>
              </tr>
            </table>
          </div>
          <div class="mt_10">社区卫生服务中心地址：<span class="bluetext">{{ stuData.stationAddress }}</span></div>
          <div>联系电话：<span class="bluetext">{{ stuData.linkPhone }}</span></div>
          <div class="mt_10">
            各位家长到医院补打疫苗时请携带“儿童预防接种证”！如有问题请及时联系社区卫生服务中心接种门诊
          </div>
          <div style="width:100%;border-bottom:1px solid #DCDFE6;height:1px;margin: 30px 0;"></div>
          <div class="mt_10">*为自愿自费疫苗</div>
          <div class="flex"><span><span class="bluetext">{{ stuData.stationName }}</span> 接种单位盖章</span>{{ $util.todayFormat() }}</div>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
  
<script>
import commonOptions from '@/mixin/commonOptions'
export default {
  name: 'icon',
  mixins: [commonOptions],
  data() {
    return {
    }
  },
  props: {
    stuData: {
      type: Object,
      default: {}
    },
    vaccinumName:{
      type:String,
      default:""
    }
  }
}
</script>
<style scoped lang="less">
.notice {
  .print-table-crown {
    border-collapse: collapse;
    border-spacing: 0;
    border-collapse: collapse;
    border-spacing: 0;
    empty-cells: show;
    border: 1px solid #cbcbcb;
    width: 100%;
  }

  td,
  th {
    padding: 0;
    text-align: center;
    border-left: 1px solid #cbcbcb;
    border-width: 0 0 0 1px;
    // font-size: inherit;
    font-size: 14px;
    margin: 0;
    overflow: visible;
    padding: 0.5em 1em;
    background-color: transparent;
    border-bottom: 1px solid #cbcbcb;
  }

}
</style>