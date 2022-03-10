# MySQL

## 一条MySQL语句执行的步骤

> 1.客户端请求 -> 连接器
>
> 2.查询缓存(存在缓存则直接返回  不存在则执行后续操作)
>
> 3.分析器(对SQL进行词法分析和语法分析操作)
>
> 4.优化器(主要对执行的SQL优化选择最优的执行方案)
>
> 5.执行器 -> 去引擎层获取数据返回

## 索引的三种常见底层数据结构以及优缺点

> 三种常见索引底层结构 : 哈希表    有序数组    搜索树
>
> 哈希表:适用于等值查询 不适合范围查找
>
> 有序数组:适用于静态存储引擎  等值和范围查找性能好 但是更新数据成本高
>
> N叉树

## 索引的常见类型以及如何发挥作用

> 根据叶子节点的内容 索引类型分为   主键索引    和   非主键索引
>
> 1.主键索引的叶子节点 存的整行数据  在InnoDB里被称为聚簇索引
>
> 2.非主键索引叶子节点  存的主键的值  在InnoDB里称为二级索引

## MyISAM和InnoDB实现B树索引方式的区别是什么

> InnoDB存储引擎 : B+树索引的叶子节点保存数据本身  其数据文件本身就是索引文件
>
> MyISAM存储引擎 : B+树索引的叶子节点保存数据的物理地址 叶节点的data域存放的是数据记录的地址 索引文件和数据是分离的

## 什么是覆盖索引和索引下推

> 覆盖索引 : 在某个查询里 索引 K 已经覆盖了我们的查询要求  
>
> 索引下推 : 可以在索引遍历过程中  对索引中包含的字段优先做判断  直接过滤掉不满足条件的记录 减少回表次数



## MySQL的 change buffer是什么

> 当需要更新一个数据页时,如果数据页在内存中就直接更新;而如果这个数据页还没有在内存中的话,在不影响数据一致性的前提下,InnoDB会将这些操作缓存在 change buffer中
>
> 这样就不需要从磁盘读入这些数据页了 在下次查询需要访问这个数据页的时候 将数据页读入内存  然后执行change buffer中与这个页有关的操作  通过这种方式能保证这个数据逻辑的正确性
>
> 唯一索引的更新不能使用change buffer 只有普通索引可以使用



## MySQL如何判断一行扫描数

> MySQL在真正开始执行语句之前 并不能准确的知道满足这个条件的记录有多少条 只能根据统计信息来估算 这个统计信息就是索引的  区分度



## MySQL redo log 和 bin log

|                | redo log                                      | binlog                                                       |
| -------------- | --------------------------------------------- | ------------------------------------------------------------ |
| 作用           | 用于崩溃恢复                                  | 主从复制和数据恢复                                           |
| 实现方式       | Innodb存储引擎实现                            | Server层实现 所有的存储引擎都可以使用binlog                  |
| 记录方式       | 循环写的方式记录 写到结尾时, 会回头循环写日志 | 通过追加的方式记录 当文件尺寸大于配置值后   ,后续日志会记录在新的文件上 |
| 文件大小       | redo log大小固定                              | 通过配置指定                                                 |
| crash-safe能力 | 具有                                          | 没有                                                         |
| 日志类型       | 逻辑日志                                      | 物理日志                                                     |

## 为什么需要redo log

> redo log主要用于MySQL异常重启后的一种数据恢复手段 确保数据一致性
>
> MySQL进行更新操作 为了能够快速响应 采用了异步写回磁盘的技术 写入内存后就返回 .但是这样 会存在crash后内存数据丢失的隐患 而 redo log具备crash-safe的能力

## 为什么 redo log具有crash-safe的能力  是binlog无法替代的

> redo log可以确保InnoDB判断哪些数据已经刷盘 哪些数据还没有
>
> redo log和binlog有一个很大的区别是  redolog循环写 binlog追加写  也就是说redo log只会记录未刷盘的日志 已经刷入磁盘的数据都会从redo log这个有限大小的日志文件里删除   binlog是追加日志 保存全量日志
>
> 当数据库crash后  想要恢复未刷盘但是已经写入redo log 和 binlog的数据到内存时, binlog是无法恢复的.因为binlog无法让InnoDB判断哪些已经刷盘 哪些还没有
>
> 如果redo log写入失败  说明此次操作失败 事务也不可能提交

## 当数据库crash后 如何恢复未刷盘的数据到内存

> 根据redo log 和binlog的两阶段提交  未持久化的数据分为几种情况
>
> 1.change buffer 写入  redo log虽然做了fsync 但未commit, binlog未fsync到磁盘 这部分数据丢失
>
> 2.change buffer写入  redo log fsync未commit, binlog 已经fsync到磁盘 先从binlog 恢复 redo log 再从redo log恢复 change buffer
>
> 3.change buffer写入 redo log 和 binlog 都fsync  直接从redolog恢复

## redo log写入方式

> redo log包括两部分内容  内存中的日志缓冲   和   磁盘上的日志文件
>
> MySQL每执行一条DML语句  会先把记录写入 redo log buffer(用户空间)  再保存到内核空间的缓冲区OS-buffer中  后续某个时间点再一次性多个操作记录到redo log file(刷盘)  这种先写日志,再写磁盘的技术 就是WAL
>
> redo log buffer写到 redo log file  是经过OS buffer中转的  
>
> 0: 延迟写; 1:实时写; 2:实时写 延迟刷

## redo log执行流程

> 1.MySQL客户端将请求语句发往MySQL Server层
>
> 2.接到SQL请求后  对其进行 分析 优化 执行等处理工作  将生成的SQL执行计划发到InnoDB存储引擎层中执行
>
> 3.InnoDB存储引擎将修改操作记录到内存中
>
> 4.记录到内存以后会修改redo log日志 添加一行记录 内容是 需要在哪个数据页上做什么修改
>
> 5.此后  将事务的状态设置为 prepare 说明已经准备好提交事务了
>
> 6.等到MySQL Server层处理完事务后 将事务状态设置为commit  也就是提交该事务
>
> 7.在收到事务提交请求以后,redo log会把刚才写入内存中的操作记录写入到磁盘 从而完成整个日志的记录过程



## binlog是什么 起到什么作用 

> binlog是归档日志 属于MySQL Server层的日志   可以实现 主从复制 和 数据恢复
>
> 当需要恢复数据时  可以取出某个时间范围内的binlog进行重放恢复
>
> binlog不可以做crash safe  因为 crash之前 binlog可能没有写入完全 mysql就挂了 需要配合redo log才可以进行crash safe



## 什么是两阶段提交

> MySQL将redo log的写入拆成两个步骤 : prepare 和 commit  中间再穿插写入binlog
>
> 执行器想要更新记录R -> InnoDB将记录R加载进Buffer Pool -> 将记录R旧值写入 undo log便于回滚 -> 执行器更新内存中的数据(此时该数据页为脏页) -> 执行器写 redo log(prepare) -> 执行器写binlog -> 执行器写 redo log(commit)



## MySQL怎么知道binlog是完整的

> 一个事务的binlog是有完整格式的:
>
> statement格式的binlog, 最后会有commit;
>
> row格式的binlog 最后会有一个XID event



## 什么是WAL技术  有什么有点

> Write-Ahead Logging  它的关键点就是日志先写内存 再写磁盘. MySQL执行更新操作后,在真正把数据写入到磁盘前 先记录日志
>
> 好处 : 不用每一次操作都实时把数据写盘 就算crash后也可以通过 redo log恢复  所以能够实现快速响应SQL语句



## binlog日志三种格式

> statement : 基于SQL语句的复制
>
> row : 基于行的复制
>
> mixed : 混合模式复制

