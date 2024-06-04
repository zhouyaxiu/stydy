<!-- 需补种名单表格 -->
<template>
  <div class="vaccination-table" style="">
    <table class="print-table-crown">
      <tr>
        <th width="9%">序号</th>
        <th width="9%">学校</th>
        <th width="9%">年级</th>
        <th width="9%">班级</th>
        <th width="12%">儿童姓名</th>
        <th width="12%">出生日期</th>
        <th width="16%">补种疫苗名称</th>
        <th width="12%">补种时间</th>
        <th width="12%">是否种全</th>
      </tr>
      <template v-for="(item, index) in dataList">
        <tr>
          <td>
            {{ index + 1 }}
          </td>
          <td>{{ item.schName }}</td>
          <td>{{ item.claLevel }}</td>
          <td>{{ item.claName }}</td>
          <td>{{ item.stuName }}</td>
          <td>{{ item.stuBirth }}</td>
          <td>{{ item.replantName }}</td>
          <td>{{ item.replateDate }}</td>
          <td>{{ item.ifFull == '0' ? '是' : item.ifFull == '1'?'否-需补种':item.ifFull == '-1'?'否-无需补种':'' }}</td>
        </tr>
      </template>
    </table>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  props: {
    dataList: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
}
</script>
<style scoped lang="less">
.vaccination-table {
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
<style media="print">
@page {
  size: auto;
  margin: 3mm;
}

@media print {
  html {
    background-color: #ffffff;
    height: auto;
    margin: 0px;
  }

  body {
    margin: 10mm;
    max-width: 188mm;
  }

  .print-table-crown {
    table-layout: auto !important;
    width: 100% !important;
  }


}
</style>