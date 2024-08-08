<template>
    <div @click="exportExcel"  >导出EXCEL</div>
</template>

<script>
import { createWsSheet } from '@/plugins/createExcel.js'
export default {
    data() {
        return {
            itemsExcel: [
                {name:"飞机沙发经济",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"就飞沙回复就哈哈健身房",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"卡开机咖啡碱撒疯狂萨芬",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"按付款加萨夫开发的时刻",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"和经济哈回复坚决反对是数据恢复大会",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"卡卡反馈咖啡机地方按时付款的",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"iu网球额外i去问问秋娥其欸我",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"黄金海岸觉得啥叫骄傲的撒娇和萨芬计划",isonline:"是",host:"192.168.8.80",db:"datatss"},
                {name:"卢卡库联发科奥利弗开发萨福克",isonline:"是",host:"192.168.8.80",db:"datatss"},
            ],
            watermark:"我是excel文档的水印呀！！！",
            exportNum:5
        }
    },
 methods: {
      async getDbType() {
      this.applyDb = await getDict({ dicttype: "dbType"});
    },
      //导出报告
      exportExcel(){
        
        const header = ["平台系统名称", "是否并网", "主机关联数据", "数据库关联数据"]
            const columns = header.map((item) => {
                return {
                  header: item,
                  key: item,
                }
            })
            let itemsExcel = this.watermark == ''  ? this.itemsExcel : this.itemsExcel.slice(0,this.exportNum)  //测试版加水印，导出前exportNum条数据
            const datas =  itemsExcel.map((item) => {
                let res = {}
                res.平台系统名称 = item.name
                res.是否并网 = item.isonline
                res.主机关联数据 = item.host
                res.数据库关联数据 = item.db
                return res
            })
            console.log(datas,itemsExcel,"我被点击了")
            createWsSheet(header, columns, datas, this.watermark ,'平台系统清单');
      },
    }
}
</script>

