<template>
  <!-- 自定义图片图标
    * v-iconType {Boolean}  类型，包括show、top、bottom、hide、yes、tshi、down、redtips
    * v-size {Number}  图标大小
  -->
  <div class="notice">
    <el-descriptions direction="vertical" :column="1" border>
      <el-descriptions-item labelClassName="labelStyle">
        <template slot="label">
          <span class="title" style="text-align: center;">{{ title }}</span>
          <!-- <span class="info"><span class="bluetext">蓝色内容</span>展示的是获取的信息的示例，不可编辑</span> -->
        </template>
        <div style="color:#333;">
          <div><span class="bluetext">{{ stuData.stuName }}</span>家长：</div>
          <span class="mt_10">
            <span v-if="afterInfo"><span class="bluetext">{{ stuData.schName }}</span>学校<span
                class="bluetext">{{ getClaLevelName(stuData.claLevel) }} {{ stuData.claName }}</span></span>
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
              <span class="bluetext">{{ stuData.belongPalceStation }}</span>
              社区卫生服务中心办理{{formData.maintype==0?'补种':'补证'}}手续。</span>
          </span>
          <div class="mt_10" v-if="formData.maintype == 0">
            <table class="print-table-crown" >
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
          <div v-if="formData.maintype == 0" class="mt_10">社区卫生服务中心地址：<span class="bluetext">{{ stuData.belongPlaceAddress }}</span></div>
          <div v-if="formData.maintype == 0">联系电话：<span class="bluetext">{{ stuData.belongPhone }}</span></div>
          <!-- <div class="mt_20"><span class="redtext">红色内容</span>可编辑（建议不超300个字）</div> -->
          <div class="redtext mt_10 edit" v-if="formData.maintype == 0">{{ formData.content }}</div>
          <span class="redtext mt_10 edit" v-if="formData.maintype == 1">{{ formData.content }}</span>
          <!-- <el-divider></el-divider> -->
          <!-- <div v-if="formData.maintype==0">*为自愿自费疫苗</div> -->
          <div style="width:100%;border-bottom:1px solid #DCDFE6;height:1px;margin: 30px 0;"></div>
          <div v-if="formData.maintype==0">*为自愿自费疫苗</div>
          <div class="flex"><span>{{ stuData.belongSchoolStation }} 接种单位盖章</span>{{ this.$util.todayFormat() }}</div>
          <!-- <slot name="edit"></slot>
          <div class="btn" v-if="formData.maintype!=3" @click="editHandler(formData.maintype)"><el-button>保存编辑内容</el-button></div> -->
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
  
<script>
import { upNoticeMess } from '@/utils/axios/apis/notice'
import commonOptions from '@/mixin/commonOptions'
export default {
  name: 'icon',
  mixins: [commonOptions],
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
      default: {}
    },
    stuData: {
      type: Object,
      default: {}
    },
  },
  methods: {
    editHandler(maintype) {
      let params = {
        schId: this.$store.state.schId,
        noticeType: maintype | 0,
        noticeContent: this.formData.content,
        operateType: this.$consts.OPTYPE_UPDATE,
      }
      upNoticeMess(params).then(res => {
        this.$message({
          message: res.data || '保存成功!',
          type: 'success'
        })
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
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