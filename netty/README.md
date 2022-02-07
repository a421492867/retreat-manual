#Netty

## IO
> Java BIO  -- Blocking I/O  同步阻塞I/O模式
> 
> Java NIO  -- New I/O   同步非阻塞模式
> 原来I/O以流的方式处理数据  而NIO以块的方式处理数据
> 面向流的I/O系统一次一个字节地处理数据  一个输入流产生一个字节的数据  一个输出流消费一个字节的数据
> 面向块的I/O系统以块的形式处理数据  每一个操作都在一步中产生或者消费一个数据块. 按块处理比按流处理快得多 但是缺少一些面向流I/O所具有的优雅性和简单性
> 
> Java AIO  -- Asynchronous I/O  异步非阻塞I/O模型
> 在NIO的基础上引入了新的异步通道的概念  并提供了异步文件通道和异步套接字通道实现