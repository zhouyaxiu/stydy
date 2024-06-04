<template>
  <div>编辑页面哦</div>
  <input @change="onChange" />
</template>
<script setup>
// 创建worker，函数里的值为webWorker文件名
const myWorker = new Worker("/src/views/stat/staticRpt/worker.js")
// const myWorker = new Worker("/worker.js")

// 向worker.js线程发送消息，对应worker.js线程中的e.data
myWorker.postMessage("start");

// 开启定时
const onTimeStart = () => {
  myWorker.postMessage("start");
}

// 停止定时
const onTimeEnd = () => {
  myWorker.postMessage("end");
}

// 重置定时
const onTime = () => {
  onTimeEnd();
  onTimeStart();
}
let timeCount = 0; // 全局中定义变量，用以控制切换标签页后的提示次数。
myWorker.addEventListener("message", e => {
  // 从worker.js那接收消息，每隔一秒都会接收到
  console.log(e.data);
  // campaignListLock();//发起续租请求
  if (e.data.sum >= 20) {
    onTimeEnd();
    console.log(document.visibilityState, 'document.visibilityState')
    if (document.visibilityState === 'visible') {
      console.error("页面已失效");
      // pageGoBack();
      this.history.go(-1)
      timeCount = 1;//触发了提示就禁止它后续再触发
    }
    document.addEventListener("visibilitychange", function () {
      if (document.visibilityState == 'visible' && timeCount == 0) {
        console.error("页面已失效");
        // pageGoBack();
        this.history.go(-1)
        timeCount = 1;
      }
    })
  }
})

const onChange = () => {
  onTime();
}
</script>