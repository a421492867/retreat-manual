## Dubbo

### RPC  远程过程调用

### dubbo
![avatar](./img/architecture.png)


一次完整的RPC调用流程（同步调用）：

1.服务消费方以本地调用方式调用服务

2.Client stub 接收到调用后 负责将方法、参数等组装成能够进行网络传输的消息体

3.client stub 找到服务地址 并将消息发送到服务端

4.server stub 接收到消息后进行解码

5.server stub根据解码结果调用本地的服务

6.本地服务执行并将结果返回给server stub

7.server stub将返回结果打包成消息并发送给消费方

8.client stub成功收到消息 并进行解码

9.服务消费方得到最终结果


### dubbo 通信 Netty 

