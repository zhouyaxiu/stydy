<template>
  <!-- 自定义图片图标
    * v-iconType {Boolean}  类型，包括show、top、bottom、hide、yes、tshi、down、redtips
    * v-size {Number}  图标大小
  -->
  <div class="notice">
    <el-descriptions direction="vertical" :column="1" border>
      <el-descriptions-item labelClassName="labelStyle">
        <template slot="label">
          <span class="title">{{ title }}</span>
          <span class="info"><span class="bluetext">蓝色内容</span>展示的是获取的信息的示例，不可编辑</span>
        </template>
        <div style="color:#333;">
          <div class="maincontent">
            <div><span class="bluetext">XXX</span>家长：</div>
            <div class="mt_10">
              <span v-if="afterInfo"><span class="bluetext">XXXXXX</span>学校<span
                  class="bluetext">XXXX-XXXXXXXX</span></span>
              学生（姓名）<span class="bluetext">XX</span> 出生日期<span class="bluetext">XXXX-XX-XX</span>
              家长（姓名）<span class="bluetext">XXX XXX</span>，
              <span v-if="formData.maintype == 0">
                经仔细查验预防接种证：发现该学生有下列疫苗未按规定程序完成预防接种，请您携带孩子凭此通知单到
              </span>
              <!-- <span v-if="formData.maintype == 1">
                经仔细查验发现该学生无接种证，请您携带有效证件凭此通知单到居住地所在社区卫生服务中心办理补证手续。
              </span> -->
              <span v-if="formData.maintype == 0">
                <span class="bluetext">XXXXXXXXXXXXX</span>社区卫生服务中心办理补种手续。
              </span>
              <!-- <span v-if="formData.maintype == 1">
                居住地所在社区卫生服务中心办理补证手续。
              </span> -->
              <span v-if="formData.maintype == 2" class="redtext mt_10 edit" contenteditable>
                {{ formData.content }}
              </span>
            </div>
            <div class="mt_10" v-if="formData.maintype == 0">
              <table class="print-table-crown">
                <tr>
                  <th width="180">序号</th>
                  <th width="180">疫苗名称</th>
                  <th style="display:table-cell">剂次</th>
                </tr>
                <template v-for="(item, index) in tableData">
                  <tr>
                    <td>
                      {{ index + 1 }}
                    </td>
                    <td>{{ item.name }}</td>
                    <td>{{ item.address }}</td>
                  </tr>
                </template>
              </table>
            </div>
            <div v-if="formData.maintype !=2">
              <div class="mt_10" v-if="formData.maintype == 0">社区卫生服务中心地址：<span class="bluetext">XXXXXX</span></div>
              <div v-if="formData.maintype == 0">联系电话：<span class="bluetext">XXXX-XXXXXXXX</span></div>
              <div class="mt_20"><span class="redtext">红色内容</span>可编辑（建议不超300个字）</div>
              <div class="redtext mt_10 edit">
                <el-input type="textarea" :autosize="{ minRows: 6, maxRows: 6 }" placeholder="" v-model="formData.content"
                  resize="none" maxlength="300">
                </el-input>
              </div>
            </div>
          </div>
          <div style="width:100%;border-bottom:1px solid #DCDFE6;height:1px;margin: 30px 0;"></div>
          <div v-if="formData.maintype == 0">*为自愿自费疫苗</div>
          <div class="flex"><span><span class="bluetext">XXXXXXXXXXXX</span> 接种单位盖章</span>XXXX年XX月XX日</div>
          <slot name="edit"></slot>
          <div class="btn" v-if="formData.maintype != 3">
            <el-button @click="editHandler(formData.maintype)" style="height: 48px;line-height:1">保存编辑内容</el-button>
          </div>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
  
<script>
import { upNoticeMess } from '@/utils/axios/apis/notice/index'
export default {
  name: 'icon',
  data() {
    return {
      tableData: [{
        name: '乙肝疫苗',
        address: '第一剂次'
      }, {
        name: '*水痘疫苗',
        address: '第一剂次'
      }],
    }
  },
  props: {
    title: {
      type: String,
      required: true
    },
    afterInfo: {
      type: Boolean,
      required: false,
    },
    formData: {
      type: Object,
      default: {}
    }
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
  .el-descriptions-row .el-descriptions-item__content .maincontent{
    min-height: 400px;
  }
}
</style>