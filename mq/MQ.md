# MQ

## MQ基本概述

> MQ (Message Queue)  消息队列  在消息传输过程中保存消息的容器   多用于分布式系统之间进行通信



## MQ的优势

> 1.应用解耦 : 服务与服务之间不再约定协议而对接接口  而是通过 生产者/消费者 模式 让中间件的MQ来对接两边的数据通信实现解耦
>
> 2.异步提速 
>
> 3.削峰填谷 :  当一瞬间有5000个请求给到服务器  服务器最大处理1000个请求  受不了了当场去世   加入MQ后  先把请求丢到队列中等待处理  系统每秒从mq中拉取一定量的请求进行处理

## MQ的劣势

> 1.系统可用性降低
>
> 2.系统复杂度提高

## RocketMQ优点

> 支持顺序性  可以做到局部有序  在单线程内使用该生产者发送的消息按照发送的顺序到达服务器存储,并按照相同的顺序被消费 但前提是这些消息发往同一个服务器的同一个分区
>
> 采取长轮询 + pull 消费消息  配合合理参数来获得更高的响应时间  实时性不低于push的方式
>
> 提供丰富的拉取模式
>
> 支持10亿级别的消息堆积  不会因为堆积导致性能下降
>
> 高效地订阅者水平扩展机制

## RocketMQ如何保证高可用

> 1.master和slave配合。master支持读、写，slave只读，producer只能和master连接写入消息，consumer可以连接master和slave
>
> 2.当master不可用或者繁忙时，consumer会被自动切换到slave读。即使master出现故障，consumer依然可以从slave读消息，不受影响
>
> 3.创建topic，把message queue创建在多个broker组上(brokerName一样，brokerId不同)，当一个broker组的master不可用后，其他组的master仍然可用，producer可以继续发消息

## RocketMQ消费者消费模式

> 1.集群消费 ： 一条消息只会投递到一个Consumer Group下面的实例
>
> 2.广播消息 ： 消息对一个Consumer Group下的各个Consumer实例都投递一边。即使这些Consumer属于同一个Consumer Group，消息也会被Consumer Group中的每个Consumer都消费一次

## RocketMQ的消息时有序的吗

> 一个topic下有多个queue 位了保证发送有序 rocketMQ提供了MessageQueueSelector队列选择机制：
>
> 1.可以使用hash取模法，让同一业务发送到同一个queue中，再使用同步发送，只有消息A发送成功，再发送消息B
>
> 2.RocketMQ的topic内的队列机制 可以保证存储满足FIFO，剩下的只需要消费者顺序消费即可
>
> 3.RocketMQ仅保证顺序发送，顺序消费由消费者业务保证

## RocketMQ事务消息的实现机制

> 第一阶段发送Prepared消息时 会拿到消息的地址
>
> 第二阶段执行本地事务，第三阶段通过第一阶段拿到的地址去访问消息，并修改消息的状态
>
> RocketMQ会定期扫描消息集群中的事务消息，如果发现了Prepared 会向消费发送端（生产者）确认，RocketMQ会根据发送端设置的策略来决定是回滚还是继续发送确认消息  保证了消息发送与本地事务同时成功或者同时失败

## RocketMQ会有重复消费问题吗  如何解决

> 可能会出现。需要保证消费端处理消息的业务逻辑保持幂等性

## RocketMQ延迟消息如何实现

> RocketMQ支持定时消息 但是不支持任意时间精度。仅支持特定的level 
>
> setDelayTimeLevel

## RocketMQ是推模型还是拉模型

> RocketMQ不管是推模式还是拉模式 底层都是拉模式。推模式是在拉模式上做了一层封装
>
> 消息存储在broker中，通过topic和tags区分消息队列。producer在发送消息时不关系consumer对应的topic和tags，只将消息发送到对应broker的对应topic和tags中
>
> 推模式中broker需要知道哪些consumer拥有那些topic和tags 但在consumer重启或更换topic时，broker无法及时获取信息，可能将消息推送到旧的consumer中。
>
> RocketMQ消息消费本质是基于拉（pull）模式。分为MQPullConsumer和MQPushConsumer。本质都是Pull 即consumer轮询从broker拉消息
>
> MQPushConsumer：consumer把轮询过程封装了，并注册MessageListener监听器，取到消息后，唤醒MessageListener的consumeMessage来消费，对用户而言 感觉消息是被推送过来的
>
> MQPullConsumer：取消息过程需要用户自己写 首先通过打算消费的topic拿到MessageQueue的集合。遍历这个集合 针对每个Queue批量取消息，一次取完后，记录该队列下一次要取的开始offset 直到取完 再换另一个Queue

## RocketMQ的负载均衡

> 1.生产者负载均衡：
>
> 从MessageQueue列表中随机选择一个（默认策略），通过自增随机数对列表大小取余获取位置信息，但获得的MessageQueue所在的集群不能是上次失败的集群
>
> 集群超时容忍策略，先随机选择一个MessageQueue 如果因为超时等异常发送失败，会优先选择该broker集群下其他的messageQueue进行发送，如果没有找到则从之前发送失败broker集群中选择一个MessageQueue进行发送 如果还没有找到 就采用默认策略

> 2.消费者负载均衡
>
> 平均分配策略（默认）
>
> 环形分配策略
>
> 手动配置分配策略
>
> 机房分配策略
>
> 一致性哈希分配策略
>
> 靠近机房策略

## RocketMQ消息积压

> 1.提高消费并行度 同一个ConsumerGroup下 通过增加Consumer实例的数量来提高并行度 超过订阅队列数的Consumer实例无效；提高单个Consumer的消息并行线程 通过修改Consumer的consumerThreadMin和consumerThreadMax来设置线程数
>
> 2.批量方式消费  设置consumerMessageBathMaxSize 默认是1。如果设置N 则每次消费的条数小于等于N
>
> 3.丢弃非重要消息
>
> 4.优化消息消费过程

