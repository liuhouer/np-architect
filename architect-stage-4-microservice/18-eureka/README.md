# foodie-cloud

这个MD格式的排版咋不支持呢？算了，同学们把Readme文件拉到本地自己看吧

- 留了Search和支付中心的两个模块让同学们自己动手改造
- 特意把前面的分布式的部分章节的代码内容剔除掉了，为了让同学们亲自动手集成一遍，防止学完就忘
- 多看开源项目源码，开卷有益
- QQ群里氛围太火爆聊天刷的太快，@我不一定能看到，可以私聊我
- 最最最最重要的，同学们现在可以忘记前端了，从此注意力转向纯后端
- 没了

</br>
</br>


## 开发环境和开源项目版本

开发环境的参考版本如下，这里列出的是我本地的软件安装版本，除了Spring Cloud的版本要严格一致以外，其他的中间件版本并不需要完全保持一致。

| 组件  | 干啥的  	| 版本号 | 
|:------------- |:---------------:| :-------------:| 
| Redis     	| 缓存组件 	| 5.0.4 | 
| RabbitMQ      | 消息中间件 |  3.7.15    | 
| Kafka      | 消息中间件 | 2.2.0    
| Lua      | 限流脚本 | 5.3.5| 
| Mariadb或MySQL      | 数据库 | 10.4.6-MariaDB Homebrew版本| 
| **Spring Cloud**      | **本章主角** | **Greenwich.SR1**
| Spring Boot      | 本章配角 | 2.1.5.RELEASE
| IDEA | 开发环境 | 版本随意
| Java | 编译运行项目 | 1.8以上（推荐8u161以后的版本，否则要装JCE插件）
| Maven | 依赖管理 | 3.0.4以上

## 技术选型

Spring Cloud每个业务领域都有多个可供选择的组件，这里也列出了微服务章节中将要用到的组件+中间件的技术选型，这也是当前主流的选型。

| 内容  | 技术选型  	| 
|:------------- |:---------------:| 
| 服务治理  	| Eureka 	|
| 负载均衡     	| Ribbon 	|
| 服务间调用     	| Feign 	|
| 服务容错     	| Hystrix + Turbine + Dashboard|
| 配置管理     	| Config + Github |
| 消息总线     	| Bus + RabbitMQ	|
| 服务网关     	| Gateway |
| 调用链追踪     	| Sleuth + Zipkin + ELK |
| 消息驱动     	| Stream + RabbitMQ	|
| 流控     	| Sentinel 	|
| 基于RPC的服务治理</br>（不集成到电商项目） | Dubbo + Admin Portal |

## 默认端口

| 内容  | 端口  	| 
|:------------- |:---------------:| 
| Eureka  	| 20000 	|
| Turbine     	| 20001 	|
| Hystrix-Dashboard     	| 20002 	|
| Config-Server     	| 20003|
| Gateway     	| 20004 |
| Zipkin     	| 20005	|
| ELK镜像-ES     	| 9200 	|
| ELK镜像-Logstash     	| 5044 	|
| ELK镜像-Kibana     	| 5601 	|
| redis（单机模式）     	| 6379 	|
| rabbitmq（单机模式）     	| 5672 	|
| mariadb/mysql（单机模式）     	| 3306 	|
| 商品微服务     	| 10001 |
| 用户微服务     	| 10002 |
| 订单微服务     	| 10003 |
| 购物车微服务     	| 10004 |
| 权限微服务     	| 10006 |
| 主搜微服务     	| 同学们自己实现	|
| 支付服务     	| 没变，但回调地址要改一下 	|

## 启动方式

可以在IDEA里启动，也可以使用Maven编译后在命令行窗口启动，命令行启动方式需要在maven编译好项目之后，cd到对应项目下的target目录，然后使用命令"java -jar xxx.jar"执行编译好的jar包即可。

启动顺序：

- 先确保RabbitMQ，Redis和Mariadb/MySQL处于启动状态
- 启动Eureka - 所有微服务和SC平台组件都依赖Eureka做服务注册
- 启动Config-Server - 部分微服务依赖配置中心拉取配置项
- 启动Hystrix监控模块 - Turbine和Hystrix-Dashboard，等到后续微服务注册到注册中心后，Turbine下次做服务发现之后就可以正常收集数据了
- 启动链路追踪组件 - Zipkin和ELK容器
- 依次启动Auth微服务 -> User微服务 -> Item微服务 -> Cart微服务 -> Order微服务，以及留给同学们完成的主搜服务，支付中心
- 最后启动Gateway网关 - 在微服务都启动好之后再启动网关，可以保证网关启动后立即生效。反过来先启动网关再注册微服务也行，但是Gateway会处于短暂的不可用状态，因为Gateway启动的时候微服务还没注册，需要等Gateway做服务发现后才能生效






