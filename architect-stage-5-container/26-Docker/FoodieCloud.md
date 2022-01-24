(Git对md的格式支持比较差，大家可以下载到本地或者在git上选择原始文件方式进行阅读)

# foodie-cloud  

## 启动顺序  

- 先确保RabbitMQ，Redis和Mariadb/MySQL处于启动状态  
```
docker run -d --name myrabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management  
docker run -d -p 6379:6379 --name myredis redis redis-server  
docker run -d -p 3306:3306 --name mymysql -e MYSQL_ROOT_PASSWORD=imooc mysql  
```
- 启动Eureka - 所有微服务和SC平台组件都依赖Eureka做服务注册    
```
[root@training1 foodie-cloud]# cat Dockerfile  
FROM java:8  
ADD registry-center-1.0-SNAPSHOT.jar registry-center-1.0-SNAPSHOT.jar  
ENTRYPOINT ["java","-jar","registry-center-1.0-SNAPSHOT.jar"]  
[root@training1 foodie-cloud]# docker build -t myregistry .  
[root@training1 foodie-cloud]# docker run -d -p 20000:20000 --name myregistry myregistry  
```
- 启动Config-Server - 部分微服务依赖配置中心拉取配置项  
```
修改application.yml: rabbitmq:host:172.19.46.183,
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD config-server-1.0-SNAPSHOT.jar config-server-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","config-server-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myconfig .
[root@training1 foodie-cloud]# docker run -d -p 20003:20003 --name myconfig myconfig
```
- 启动Hystrix监控模块 - Turbine和Hystrix-Dashboard，等到后续微服务注册到注册中心后，Turbine下次做服务发现之后就可以正常收集数据了 
``` 
修改Turbine的application.yml:  
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD hystrix-turbine-1.0-SNAPSHOT.jar hystrix-turbine-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","hystrix-turbine-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myturbine .
[root@training1 foodie-cloud]# docker run -d -p 20001:20001 --name myturbine myturbine
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD hystrix-dashboard-1.0-SNAPSHOT.jar hystrix-dashboard-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","hystrix-dashboard-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t mydashboard .
[root@training1 foodie-cloud]# docker run -d -p 20002:20002 --name mydashboard mydashboard
```
- 启动链路追踪组件 - Zipkin和ELK容器  
```
修改application.yml:
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD zipkin-server-1.0-SNAPSHOT.jar zipkin-server-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","zipkin-server-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myzipkin .
[root@training1 foodie-cloud]# docker run -d -p 20005:20005 --name myzipkin myzipkin
```
- 依次启动Auth微服务 -> User微服务 -> Item微服务 -> Cart微服务 -> Order微服务，以及留给同学们完成的主搜服务，支付中心  
- Auth微服务:  
```
修改application.yml: redis:host:172.19.46.183,注释password, zipkin:base-url:http://172.19.46.183:20005/, 
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-auth-service-1.0-SNAPSHOT.jar foodie-auth-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-auth-service-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myauth .
[root@training1 foodie-cloud]# docker run -d -p 10006:10006 --name myauth myauth
```
- User微服务:  
```
修改application-dev.yml: datasource:url: jdbc:mysql://172.19.46.183:3306/foodie_shop_dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true,password:imooc, redis:host:172.19.46.183,注释password, zipkin:base-url:http://172.19.46.183:20005/, rabbitmq:host:172.19.46.183
修改bootstrap.yml: 
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-user-web-1.0-SNAPSHOT.jar foodie-user-web-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-user-web-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myuser .
[root@training1 foodie-cloud]# docker run -d -p 10002:10002 --name myuser myuser
```
- Item微服务:  
```
修改application-dev.yml: datasource:url: jdbc:mysql://172.19.46.183:3306/foodie_shop_dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true,password:imooc, redis:host:172.19.46.183,注释password, zipkin:base-url:http://172.19.46.183:20005/
修改bootstrap.yml:
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-item-web-1.0-SNAPSHOT.jar foodie-item-web-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-item-web-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myitem .
[root@training1 foodie-cloud]# docker run -d -p 10001:10001 --name myitem myitem
```
- Cart微服务:  
```
修改application-dev.yml: datasource:url: jdbc:mysql://172.19.46.183:3306/foodie_shop_dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true,password:imooc, redis:host:172.19.46.183,注释password, zipkin:base-url:http://172.19.46.183:20005/
修改bootstrap.yml: 
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-cart-web-1.0-SNAPSHOT.jar foodie-cart-web-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-cart-web-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t mycart .
[root@training1 foodie-cloud]# docker run -d -p 10004:10004 --name mycart mycart
```
- Order微服务:  
```
修改application-dev.yml: datasource:url: jdbc:mysql://172.19.46.183:3306/foodie_shop_dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true,password:imooc, redis:host:172.19.46.183,注释password, zipkin:base-url:http://172.19.46.183:20005/
修改bootstrap.yml:
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-order-web-1.0-SNAPSHOT.jar foodie-order-web-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-order-web-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myorder .
[root@training1 foodie-cloud]# docker run -d -p 10003:10003 --name myorder myorder
```
- 最后启动Gateway网关 - 在微服务都启动好之后再启动网关，可以保证网关启动后立即生效。反过来先启动网关再注册微服务也行，但是Gateway会处于短暂的不可用状态，因为Gateway启动的时候微服务还没注册，需要等Gateway做服务发现后才能生效  
```
修改application.yml: redis:host:172.19.46.183,注释password, zipkin:base-url:http://172.19.46.183:20005/, 
eureka:
  client:
    serviceUrl:
      defaultZone: http://172.19.46.183:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 172.19.46.183
    prefer-ip-address: true
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD gateway-1.0-SNAPSHOT.jar gateway-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","gateway-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t mygateway .
[root@training1 foodie-cloud]# docker run -d -p 20004:20004 --name mygateway mygateway
```
- 准备数据  
```
cd /usr/local/mysql/bin
./mysql -h 101.133.136.40 -u root -p
imooc
CREATE DATABASE IF NOT EXISTS foodie_shop_dev DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```
- 启动前端服务  
下载foodie-shop前端代码，上传至/root/foodie-cloud/foodie-shop目录  
```
docker run --name myfront -p 8080:80 -v /root/foodie-cloud/foodie-shop:/usr/share/nginx/html -d nginx
```
修改前端foodie-cloud/foodie-shop/js/app.js,使它指向gateway的外网地址：  
```
serverUrl: "http://101.133.136.40:20004",
```
- 解决cookie跨域问题  
在com.imooc.utils.CookieUtils类的doSetCookie方法中添加：  
```
domainName = "101.133.136.40"; //保证cookie的域和前端一致
```
mvn install后，重新build docker image，并重建docker容器。  
为了减少等待时间，可以收工重启eureka和gateway服务，并重新尝试登陆.  