<template>
    <div class="searchTerm">
      <table class="certificate-table">
        <tbody>
          <tr>
            <td colspan="6">受种者基本情况</td>
          </tr>
          <tr>
            <td class="bold">预防接种档案编号</td>
            <td>35</td>
            <td class="bold">受种者姓名</td>
            <td>35</td>
            <td class="bold">出生日期</td>
            <td>
              324
            </td>
          </tr>
          <tr>
            <td class="bold">身份证件号码</td>
            <td>324</td>
            <td class="bold">受种者信息介质识别码</td>
            <td>324</td>
            <td class="bold">性别</td>
            <td>
              43
            </td>
          </tr>
          <tr>
            <td colspan="6">免疫接种情况</td>
          </tr>
          <tr>
            <td class="bold">疫苗种类</td>
            <td class="bold">疫苗批号</td>
            <td class="bold">生产企业</td>
            <td class="bold">接种时间</td>
            <td class="bold">剂次</td>
            <td class="bold">接种人员</td>
          </tr>
          <!-- <template v-for="item in table_data">
            <template v-for="(citem, cidx) in item">
              <tr :key="citem.FSHOTGUID">
                <td :rowspan="cidx == 0 ? item.length : 1" v-if="cidx == 0">
                  {{ citem.FBACTSHORTNAME }}
                </td>
                <td>{{ citem.YMPH }}</td>
                <td>{{ citem.ENTERPNAME }}</td>
                <td>
                  {{
                    citem.FBACTID == "B001" || citem.FBACTID == "B063"
                      ? citem.JZRQ
                      : citem.JZRQ.split(" ")[0]
                  }}
                </td>
                <td>{{ citem.FBACTSEQNAME }}</td>
                <td>{{ citem.JZYS }}</td>
              </tr>
            </template>
          </template> -->
          <tr>
            <td>门诊电话</td>
            <td></td>
            <td>联系人</td>
            <td></td>
            <td>门诊地址</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>门诊名称</td>
            <td colspan="3"></td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {};
    },
    props: {
      recipient_info: {
        type: Object,
        default: () => {
          return {};
        },
      },
      table_data: {
        type: Array,
        default: () => {
          return [];
        },
      },
    },
    destroyed() {
      window.removeEventListener("resize", this.onScreenResize, true);
    },
    updated() {
      this.setScrollBar();
    },
    mounted() {
      this.setScrollBar();
      if (this.isSetWidth) {
        window.addEventListener("resize", this.onScreenResize, true);
      } else {
        // this.tableHeight = 600;
        // this.tableWidth =
        //   document.getElementsByClassName("searchTerm")[0].offsetWidth;
      }
      console.log("table_data", this.table_data);
    },
    methods: {
      onScreenResize() {
        return (() => {
          let height;
          if (document.compatMode === "BackCompat") {
            height = document.body.scrollHeight - 270;
          } else {
            height = document.documentElement.scrollHeight - 270;
          }
          this.$nextTick(() => {
            // this.tableHeight = height < 250 ? 250 : height;
            // this.tableWidth =
            //   document.getElementsByClassName("searchTerm")[0].offsetWidth;
          });
        })();
      },
    },
  };
  </script>
  
  <style scoped lang='less'>
  .certificate-table {
    border-collapse: collapse;
    border-spacing: 0;
    border-collapse: collapse;
    border-spacing: 0;
    empty-cells: show;
    border: 1px solid #cbcbcb;
    width: 100%;
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
    tr:nth-child(1),
    tr:nth-child(4) {
      font-weight: 600;
      font-size: 20px;
      letter-spacing: 6px;
      td {
        font-size: 20px;
      }
    }
    .bold {
      font-weight: 600;
    }
  }
  .certificate-table caption {
    color: #000;
    font: italic 85%/1 arial, sans-serif;
    padding: 1em 0;
    text-align: center;
  }
  .certificate-table tbody > tr:last-child > td {
    border-bottom-width: 0;
  }
  </style>
  