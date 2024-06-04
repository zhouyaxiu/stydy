let timer;
self.addEventListener("message", e => {
  // 接收到消息
  console.log(e.data);

  let sum = 0;
  let msg;

  if (e.data === "start") {
    timer = setInterval(() => {
      sum += 1;
      msg = {
        text: "editing",
        sum
      }
      self.postMessage(msg);//向主线程发送消息msg对象
    }, 60 * 1000)//每分钟sum加1标识积累了一分钟
  } else {
    clearInterval(timer);
  }
})