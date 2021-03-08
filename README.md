## 🧿 log-agent

log-agent项目是日志收集项目的client 端， 需要配合log-transfer server端进行使用。两者之间通过tcp 建立通信；进行数据发送。

## 🗒简介

+ log-agent 采用Java语言，log-transfer 采用golang语言。

+ 使用swagger `ApiOperation` 作为基础切入点，日志上报到异步事件监听。

+ log-agent事件监听通过netty channel 进行日志发送log-transfer  event-loop

+ log-agent 根据写入操作判断是否进行自动断连，默认情况60秒自动断开连接。

  

![image-20210308181233661](image/image-20210308181233661.png)

## 🧸使用

