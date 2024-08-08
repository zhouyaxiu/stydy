<template>
  <div class="check-container">
    <div class="top-line">
      <div class="img">
        <img src="@/assets/img/print/print-top.png">
      </div>
      <svg ref="barcode"></svg>
    </div>
    <div class="title">浙江省儿童入托、入学预防接种证查验证明</div>
    <div class="center-content">
      <table class="check-table">
        <tbody>
          <tr>
            <td>儿童姓名</td>
            <td style="width: 150px;">{{ checkData.stuName }}</td>
            <td>性别</td>
            <td>{{
              checkData.stuSex }}</td>
            <td>出生日期</td>
            <td>
              {{ checkData.stuBirth }}
            </td>
          </tr>
          <tr>
            <td>身份证号码</td>
            <td>{{ checkData.stuCid }}</td>
            <td>父亲姓名</td>
            <td style="min-width: 100px;">{{ checkData.stuFathername }}</td>
            <td>联系方式</td>
            <td>
              {{ checkData.stuFatherPhone }}
            </td>
          </tr>
          <tr>
            <td>母亲姓名</td>
            <td>{{ checkData.stuMothername }}</td>
            <td>联系方式</td>
            <td colspan="3">
              {{ checkData.stuMotherPhone }}
            </td>
          </tr>
          <tr>
            <td>现居住地址</td>
            <td colspan="5">{{ checkData.stuAddr }}</td>
          </tr>
        </tbody>
      </table>
      <div class="center-result">
        <div class="result-title">
          <span v-if="checkData.ifFull === '0' || checkData.ifFull == '-1'">查验通过</span>
          <span v-else>查验不通过</span>
        </div>
        <div class="result-content">
          <span v-if="checkData.ifFull === '0' || checkData.ifFull == '-1'">
            经查验预防接种记录，该儿童已按照免疫程序完成当前阶段的儿童免疫规划疫苗接种。
          </span>
          <span v-else>
            经查验预防接种记录，该儿童按照免疫程序未完成当前阶段的儿童免疫规划疫苗接种，需补种疫苗如下：
          </span>
        </div>
        <div v-if="checkData.ifFull == '1'">
          <table class="result-table">
            <tr>
              <th width="34%">疫苗名称</th>
              <th width="33%">剂次</th>
              <th width="33%">状态</th>
            </tr>
            <template v-if="checkData.replantList.length > 0">
              <template v-for="item in checkData.replantList">
                <tr>
                  <td>{{ item.name }}</td>
                  <td>{{ item.index }}</td>
                  <td>{{ item.status }}</td>
                </tr>
              </template>
            </template>
            <template v-else>
              <tr>
                <td colspan="3" style="text-align: center;">
                  <span>暂无数据</span>
                </td>
              </tr>
            </template>
          </table>
        </div>
      </div>
      <div class="bottom-station">
        <span>现管理单位：{{ checkData.belongPalceStation }}</span>
        <span>查验时间：{{ this.$util.todayFormatHourMin() }}</span>
      </div>
      <div class="tips-bottom">
        <div class="tip-title">
          注意事项：
        </div>
        <div class="bold">1：请打印，并按要求提交托育机构、幼儿园或学校；</div>
        <div>2：如与接种记录不符，可前往现管理接种门诊咨询接种医生；</div>
        <div>3：疫苗接种，请谨遵医嘱。</div>
      </div>
    </div>
    <div class="divider">
      <div class="line"></div>
      <div class="text">权威机构 联合认证</div>
      <div class="line"></div>
    </div>
    <div class="bottom-img">
      <img src="@/assets/img/print/print-bottom.png">
    </div>

  </div>
</template>
<script>
import JsBarcode from 'jsbarcode'

export default {
  props: {
    checkData: {
      type: Object,
      required: true
    },
  },
  mounted() {
    JsBarcode(this.$refs.barcode, this.checkData.stuNo, { format: 'CODE128', width: 2, height: 50, fontSize: 14, font: "Noto Sans SC" });
  }
}
</script>
<style scoped lang="less">
.check-container {
  margin-bottom: 40px;
  min-height: 800px;
  width: 700px;
  // padding: 0 20px;
  color: #000000;
  margin-top: 20px;


  .top-line {
    display: flex;
    vertical-align: middle;
    justify-content: space-between;

    .img {
      img {
        width: 63px;
        height: 63px;
      }
    }
  }

  .title {
    margin-top: 30px;
    font-size: 36px;
    text-align: center;
    // letter-spacing: 2px;
    color: #000000;
    font-weight: bold;
  }

  .center-content {
    .check-table {
      margin-top: 30px;
      border-collapse: collapse;
      border-spacing: 0;
      border-collapse: collapse;
      border-spacing: 0;
      empty-cells: show;
      border: 1px solid #00021C;
      width: 100%;
      color: #000;

      td,
      th {
        padding: 0;
        border-left: 1px solid #00021C;
        border-width: 0 0 0 1px;
        // font-size: inherit;
        font-size: 14px;
        margin: 0;
        overflow: visible;
        padding: 1em 1em;
        background-color: transparent;
        border-bottom: 1px solid #00021C;
      }
    }

    .center-result {
      border: 1px solid #00021C;
      min-height: 180px;
      margin-top: 25px;
      padding: 30px 15px 20px;

      .result-title {
        font-size: 18px;
        color: #000000;
        font-weight: bold;
      }

      .result-content {
        font-size: 14px;
        margin-top: 5px;
      }


    }
  }

  .result-table {
    margin-top: 30px;
    border-collapse: collapse;
    border-spacing: 0;
    border-collapse: collapse;
    border-spacing: 0;
    empty-cells: show;
    border: 1px solid #DCDCDC;
    width: 100%;
    color: #000;

    td,
    th {
      padding: 0;
      border-left: 1px solid #DCDCDC;
      border-width: 0 0 0 1px;
      // font-size: inherit;
      font-size: 14px;
      margin: 0;
      overflow: visible;
      padding: 1em 1em;
      background-color: transparent;
      border-bottom: 1px solid #DCDCDC;
    }

    th {
      background-color: #EEEEEE;
      font-weight: normal;
      text-align: left;
      font-size: 12px;
    }
  }

  .bottom-station {
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
  }

  .tips-bottom {
    margin-top: 15px;
    font-size: 14px;

    .tip-title {
      font-size: 16px;
      font-weight: bold;
    }

    .bold {
      font-weight: bold;
    }
  }

  .divider {
    margin-top: 50px;
    display: flex;

    .text {
      color: #00021C;
      font-size: 12px;
      padding-left: 10px;
      padding-right: 10px;
    }

    .line {
      height: 2px;
      color: #00021C;
      width: 290px;
      border-top: 1px solid #00021C;
      margin-top: 5px
    }
  }

  .bottom-img {
    margin-top: 10px;
    text-align: center;


    img {
      width: 150px;
      height: auto;
    }
  }
}
</style>
<style media="print">
@page {
  size: A4 portrait;
  margin: 3mm;
}

@media print {
  html {
    background-color: #ffffff;
    height: auto;
    margin: 0px;
  }

  .result-table {
    table-layout: auto !important;
    width: 100% !important;
  }


}
</style>