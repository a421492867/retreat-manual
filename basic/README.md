## Java基础

### Java语言三大特性
> 封装 继承 多态

### Java基本数据类型
> byte short int long float double boolean char

### Java中的四种引用
> 强软弱虚
> 
> 强引用 : 最普遍 例如 String s = "abc"  只要强引用存在 则垃圾回收器就不会回收这个对象
> 
> 软引用 : 用于描述还有用但非必要的对象 如果内存足够 不回收 如果内存不足 则回收
> 
> 弱引用 : 只有弱引用的对象拥有更短的生命周期  发现只具有弱引用的对象,不管当前内存空间足够与否 都会回收
> 
> 虚引用 : 一个对象仅持有虚引用 那么它就和没有任何引用一样 在任何时候都可能被回收 主要用来跟踪对象被垃圾回收器回收的活动
> 
### HashMap源码分析
> HaspMap put流程图
![avatar](./src/HashMap_put.png)


### HashSet的实现原理
> HashSet基于HashMap实现


### 常见的RuntimeException
> NullPointerException    NumberFormatException    IndexOutOfBoundsException    IllegalArgumentException    ClaasCastException. etc


### Java反射
> 指在程序运行状态中 可以构造任意一个类的对象 可以了解任意一个对象所属的类 成员变量 方法 可以调用任意一个对象的属性和方法

### IO NIO
> 阻塞IO 非阻塞IO 多路复用IO 信号驱动IO 异步IO
> NIO的核心组件  channel  buffer  selector