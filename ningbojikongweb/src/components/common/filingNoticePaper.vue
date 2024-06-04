<!-- 
    * 打印建档通知单模板 打印预览
    * isAfter   是否是信息添加后
    * formData  学生信息
    * isEdit    是否可以编辑
 -->
<template>
  <div style="page-break-after: always">
    <div class="notice" style="margin-top:30px;">
      <el-descriptions direction="vertical" :column="1" border>
        <el-descriptions-item labelClassName="labelStyle">
          <template slot="label">
            <span class="title" style="text-align: center;">{{ title }}</span>
            <span class="info" v-if="isEdit"><span class="bluetext">蓝色内容</span>展示的是获取的信息的示例，不可编辑</span>
          </template>
          <div style="color:#333;">
            <div><span class="bluetext">{{ formData.stuName }}</span>家长：</div>
            <div class="mt_10">
              <span v-if="!isAfter">
                <span class="bluetext">{{ formData.schoolName }}</span> 学校<span class="bluetext">{{ formData.gradeName
                }}{{
  formData.className }}</span>
              </span>
              学生（姓名）<span class="bluetext">{{ formData.stuName }}</span> 出生日期<span class="bluetext">{{ formData.stuBirth
              }}</span>
              家长（姓名）<span class="bluetext">{{ formData.stuParentname }}</span>，
              <span v-if="!isEdit">
                {{ formData.noticeContent }}
              </span>
              <span v-else>
                <div class="mt_20"><span class="redtext">红色内容</span>可编辑（建议不超300个字）</div>
                <span class="redtext mt_10 edit">
                  <el-input type="textarea" :autosize="{ minRows: 10, maxRows: 10 }" placeholder=""
                    v-model="formData.noticeContent" resize="none">
                  </el-input>
                </span>
              </span>
            </div>
            <div style="width:100%;border-bottom:1px solid #DCDFE6;height:1px;margin-bottom: 30px;margin-top: 120px;">
            </div>
            <div class="flex"><span>{{ formData.schStationName }} 接种单位盖章</span>{{ this.$util.todayFormat() }}</div>
            <!-- <div class="btn" v-if="isEdit"><el-button>保存编辑内容</el-button></div> -->
            <slot name="edit"></slot>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>
<script>
export default {
  name: 'filingNoticePaper',
  props: {
    isEdit: {
      type: Boolean,
      required: false,
    },
    isAfter: {
      type: Boolean,
      required: false,
    },
    title: {
      type: String,
      default: '建档通知单'
    },
    formData: {
      type: Object,
      default: function () { return {} }
    }
  }
}
</script>
   

   