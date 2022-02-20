
## Spring

### beans
1.DefaultListableBeanFactory

2.XmlBeanDefinitionReader


### Spring中Bean创建 生命周期有哪些
> 1.推断构造方法
> 2.实例化
> 3.属性填充
> 4.处理Aware回调
> 5.初始化前 处理@PostConstruct注解
> 6.初始化中 处理 initializingBean接口
> 7.初始化后  进行AOP
> 
### Spring中的Bean是线程安全的吗
> 本身没有针对Bean对线程安全处理  如果Bean无状态 则线程安全  若果有状态 则Bean不是线程安全
> 
### Spring中的事务是如何实现的
1. Spring事务底层基于数据库事务和AOP机制实现
2. 对于使用了@Transactional注解的Bean Spring会创建一个代理对象作为Bean
3. 当调用代理对象的方法时 会先判断该方法是否加了@Transactional注解
4. 如果加了 那么则利用事务管理器创建一个数据库连接
5. 修改数据库连接的autocommit属性为false 禁止此连接的自动提交(重要)
6. 执行当前方法 方法中会执行SQL
7. 执行完当前方法后 如果没有出现异常就直接提交事务
8. 如果出现了异常 并且这个异常是需要回滚的就回滚事务 否则仍然提交事务
9. Spring事务的隔离级别对应的就是数据库的隔离级别
10. Spring事务的传播机制是Spring事务自己实现的  基于数据库连接来做的  一个数据库连接一个事务 如果传播机制配置为需要新开一个事务 那么实际上就是先建立一个数据库连接 在此新数据库连接上执行SQL

### Spring容器启动流程
> 1.首先进行扫描  得到BeanDefinition对象 并存在一个Map中
> 2.筛选出非懒加载的单例BeanDefinition进行创建Bean 对于多例Bean不需要在启动过程中创建 在每次获取Bean的时候 利用BeanDefinition创建
> 3.利用BeanDefinition创建Bean就是Bean的创建生命周期 包括了 合并BeanDefinition 推断构造方法 实例化 属性填充  初始化前 初始化  初始化后等步骤 其中AOP就是发生在初始化后这一步
> 4.单例Bean创建完了之后 Spring会发布一个容器启动时间
> 5.Spring启动结束
> 6.还涉及一些BeanFactoryPostProcessor和BeanPostProcessor的注册  还会处理@Import等注解
> 
### BeanFactory和ApplicationContext有什么区别
> ApplicationContext继承BeanFactory   还继承了其他接口 从而ApplicationContext能够 国际化 时间发布等功能 这是BeanFactory不具备的

### Spring中什么时候@Transactional会失效
> A B两个方法在同一个类  B有@Transactional A没有 A调用B  外部方法调用A B的事务失效
> 同时如果某个方法时private的 @Transactional也会失效 因为底层cglib是基于父子类来实现的  子类不能重载父类private方法
> 
>
### Springboot常用注解及其底层实现
> @SpringBootApplication  (@SpringbootConfiguration  @EnableAutoConfiguration  @ComponentScan)
> @Bean
> @RestController 等
> 
### Springboot如何启动Tomcat
> 1.首先Springboot启动时会先创建一个Spring容器
> 2.spring容器创建过程中 利用@ConditionalOnClass技术来判断当前classpath中是否存在tomcat依赖 如果存在则会生成一个启动tomcat的bean
> 3.spring容器创建完之后 会启动tomcat的bean 并创建tomcat对象 绑定端口等 并启动tomcat
> 
### Springboot配置文件加载顺序
> 1.命令行参数
> 2.Java系统属性
> 3.操作系统环境变量
> 4.jar包外部的配置文件(带profile)
> 5.jar包内部配置文件(带profile)
> 6.jar包外部配置文件(不带profile)
> 7.jar包内部配置文件(不带profile)
> 8.@Configuration注解上的@PropertySource