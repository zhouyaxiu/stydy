<template>
  <!-- 自定义图片图标
    * v-iconType {Boolean}  类型，包括show、top、bottom、hide、yes、tshi、down、redtips
    * v-size {Number}  图标大小
  -->
  <div class="notice">
    <el-descriptions direction="vertical" style="page-break-after:always;margin-top:10px;margin-bottom: 20px;" :column="1" border v-if="formData.stuData" v-for="stuData in formData.stuData" :key="stuData.stuName">
      <el-descriptions-item labelClassName="labelStyle">
        <template slot="label">
          <span class="title" style="text-align: center;">{{ title }}</span>
        </template>
        <div style="color:#333;">
          <div><span class="bluetext">{{ stuData.stuName }}</span>家长：</div>
          <div class="mt_10">
            <span v-if="afterInfo"><span class="bluetext">{{ stuData.schName }}</span>学校<span
                class="bluetext">{{ stuData.claName }}</span></span>
            学生（姓名）<span class="bluetext">{{ stuData.stuName }}</span> 出生日期<span class="bluetext">{{ stuData.stuBirth }}</span>
            家长（姓名）<span class="bluetext">{{ stuData.parentName }}</span>，
            <span v-if="formData.maintype == 0">
              经仔细查验预防接种证：发现该学生有下列疫苗未按规定程序完成预防接种，请您携带孩子凭此通知单到
            </span>
            <!-- <span v-if="formData.maintype == 1">
              经仔细查验发现该学生无接种证，请您携带有效证件凭此通知单到
            </span> -->
            <span v-if="formData.maintype == 2" class="redtext mt_10 edit" contenteditable>
              {{ formData.content }}
            </span>
            <span v-if="formData.maintype == 0">
              <span class="bluetext">{{ stuData.belongPalceStation }}</span>社区卫生服务中心办理{{formData.maintype==0?'补种':'补证'}}手续。
            </span>
          </div>
          <div class="mt_10" v-if="formData.maintype == 0">
            <!-- <el-table :data="stuData.tableData" border style="width: 100%">
              <el-table-column prop="date" type="index" label="序号" width="180">
              </el-table-column>
              <el-table-column prop="name" label="疫苗名称" width="180">
              </el-table-column>
              <el-table-column prop="value" label="剂次">
              </el-table-column>
            </el-table> -->
            <table class="print-table-crown">
              <tr>
                <th width="180">序号</th>
                <th width="180">疫苗名称</th>
                <th style="display:table-cell">剂次</th>
              </tr>
              <template v-for="(item, index) in stuData.tableData">
                <tr>
                  <td>
                    {{ index + 1 }}
                  </td>
                  <td>{{ item.name }}</td>
                  <td>{{ item.value }}</td>
                </tr>
              </template>
            </table>
          </div>
          <div>
            <div v-if="formData.maintype == 0" class="mt_10">社区卫生服务中心地址：<span class="bluetext">{{ stuData.belongPlaceAddress }}</span></div>
            <div v-if="formData.maintype == 0">联系电话：<span class="bluetext">{{ stuData.belongPhone }}</span></div>
            <div class="redtext mt_10 edit">{{ formData.content }}</div>
          </div>
          <div style="width:100%;border-bottom:1px solid #DCDFE6;height:1px;margin: 30px 0;"></div>
          <div v-if="formData.maintype==0">*为自愿自费疫苗</div>
          <div class="flex"><span>{{ stuData.belongSchoolStation }} 接种单位盖章</span>{{ $util.todayFormat() }}</div>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
  
<script>
export default {
  name: 'icon',
  data() {
    return {
    }
  },
  props: {
    title: {
      type: String,
      required: false
    },
    afterInfo: {
      type: Boolean,
      required: false,
    },
    formData: {
      type: Object,
      required: false,
      default: function () { return {} }
    },
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