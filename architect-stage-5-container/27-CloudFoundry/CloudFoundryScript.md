# 环境准备
Reference:   
https://github.com/pivotal-cf/pcfdev  
https://github.com/cloudfoundry-incubator/cfdev  
https://github.com/cloudfoundry/cli  

```
#For Ubuntu:
#apt-get update
#apt-get install qemu-kvm qemu-system libvirt-bin virt-manager bridge-utils vlan
#sudo apt-get install vnc4server gnome-panel gnome-settings-daemon metacity nautilus gnome-terminal firefox
```

修改xstartup文件内容如下:
```
#!/bin/sh
export XKL_XMODMAP_DISABLE=1
/etc/X11/Xsession

#unset SESSION_MANAGER
#exec /etc/X11/xinit/xinitrc

[ -x /etc/vnc/xstartup ] && exec /etc/vnc/xstartup
[ -r $HOME/.Xresources ] && xrdb $HOME/.Xresources
xsetroot -solid grey
vncconfig -iconic &
x-terminal-emulator -geometry 80x24+10+10 -ls -title "$VNCDESKTOP Desktop" &
#x-window-manager &

gnome-panel &
gnome-settings-daemon &
gnome-session-fallback &

metacity &
nautilus &
gnome-terminal &
```
原文链接：https://blog.csdn.net/u013187057/article/details/89097483  

启动vncserver，方便通过云短服务的浏览器访问Cloud Foundry Apps Manager：   
```
#vncserver :1  
```
# Cloud Foundry 软件安装
从官网下载cf-cli命令工具和pcfdev软件包，并按照文档进行安装：  
https://network.pivotal.io/products/pcfdev  

为方便大家下载，Mac、Linux、Windows系统对应的安装包都已经放在百度网盘:    
链接:https://pan.baidu.com/s/17TXA6fsGYI0oQ0LSMMcoTA  
密码:j4hm  
下面以Mac为例介绍安装过程(其他操作系统类似):  
先分别下载 cf-cli_6.46.1_osx.tgz 和 pcfdev-v1.2.0-darwin.tgz，然后进行安装  
```
tar zxvf cf-cli_6.46.1_osx.tgz
cp cf /usr/local/bin
cf install-plugin -r CF-Community cfdev
cf dev start -f pcfdev-v1.2.0-darwin.tgz

此处略过1-2小时安装部署时间

 Done (3m8s)

 	 ██████╗  ██████╗███████╗██████╗ ███████╗██╗   ██╗
 	 ██╔══██╗██╔════╝██╔════╝██╔══██╗██╔════╝██║   ██║
 	 ██████╔╝██║     █████╗  ██║  ██║█████╗  ██║   ██║
 	 ██╔═══╝ ██║     ██╔══╝  ██║  ██║██╔══╝  ╚██╗ ██╔╝
 	 ██║     ╚██████╗██║     ██████╔╝███████╗ ╚████╔╝
 	 ╚═╝      ╚═════╝╚═╝     ╚═════╝ ╚══════╝  ╚═══╝
 	             is now running!

 	To begin using PCF Dev, please run:
 	    cf login -a https://api.dev.cfdev.sh --skip-ssl-validation

 	Admin user => Email: admin / Password: admin
 	Regular user => Email: user / Password: pass

 	To access Apps Manager, navigate here: https://apps.dev.cfdev.sh

 	To deploy a particular service, please run:
 	    cf dev deploy-service <service-name> [Available services: mysql,redis,rabbitmq,scs]
```

# 登陆 Cloud Foundry
```
LM-SHC-16503108:CF xin$ cf login -a https://api.dev.cfdev.sh --skip-ssl-validation
API 端點: https://api.dev.cfdev.sh

Email> admin

Password>
正在鑑別...
確定

選取組織（或按 Enter 鍵以跳過）:
1. cfdev-org
2. system

Org> 1
已設定組織 cfdev-org 的目標

已設定空間 cfdev-space 的目標


API 端點:   https://api.dev.cfdev.sh（API 版本: 2.125.0）
使用者:     admin
組織:       cfdev-org
空間:       cfdev-space
```

# 部署代码 Spring Music
```
Now that the PCF Dev virtual machine is running on your workstation, you are really close to deploying the sample Java app.

This sample app is built with Spring Framework and helps demonstrate the use of database services on PCF.

Download the app with git:

git clone https://github.com/cloudfoundry-samples/spring-music
If you don't have Git installed, you can download a zip file of the app at github.com/cloudfoundry-samples/spring-music/archive/master.zip

Navigate to the app directory:

cd ./spring-music
Log in to PCF Dev:

cf login -a api.local.pcfdev.io --skip-ssl-validation

API endpoint:  api.local.pcfdev.io   
Email>     user
Password>  pass
Use Gradle to assemble the app locally:

./gradlew assemble
Push the app:

cf push --hostname spring-music
Open the sample app in your browser:

requested state: started
instances: 1/1
usage: 512M x 1 instances
routes: spring-music.local.pcfdev.io
```
参考：  
https://github.com/cloudfoundry-samples/spring-music/blob/master/manifest.yml  
https://github.com/cloudfoundry/java-buildpack  

# 检查日志信息 
```
PCF provides access to an aggregated view of logs related to you application. This includes HTTP access logs, as well as output from app operations such as scaling, restarting, and restaging.

Every log line contains four fields:

Timestamp
Log type
Channel
Message
cf CLI log fields
View a snapshot of recent logs:

cf logs spring-music --recent
Or, stream live logs:

cf logs spring-music
Reload the app page to see activity. Press Control C to stop streaming.
```

# 配置路由
```
cf routes
cf domains

cf orgs
cf create-domain cfdev-org music.com

cf map-route spring-music music.com -n album 
cf unmap-route spring-music music.com -n album
cf delete-route music.com -n album

cf delete-domain music.com

manifest.yml

routes:
- route: https://xxx

---
applications:
- name: spring-music-blue
  memory: 1G
  random-route: true
  path: build/libs/spring-music-1.0.jar
  env:
    JBP_CONFIG_OPEN_JDK_JRE: '{ jre: { version: 11.+ } }'

Change to: 
---
applications:
- name: spring-music-blue
  memory: 1G
  #random-route: true
  routes:
  - route: https://spring-music-blue.dev.cfdev.sh
  path: build/libs/spring-music-1.0.jar
  env:
    JBP_CONFIG_OPEN_JDK_JRE: '{ jre: { version: 11.+ } }'


cf map-route spring-music-blue dev.cfdev.sh -n spring-music
cf push
cf apps
cf map-route spring-music-green dev.cfdev.sh -n spring-music
cf apps
cf unmap-route spring-music-blue dev.cfdev.sh -n spring-music
```

# 绑定基础架构服务
```
cf dev deploy-service <service-name> [Available services: mysql,redis,rabbitmq,scs]
cf marketplace
cf create-service p-mysql 1gb mysql
cf bind-service spring-music-green mysql
cf env spring-music-green 

cf cups pubsub -p "host, port, username, password"
host> pubsub.gcloud.com
port> 443
username> feiyang
password> password

cf bind-service spring-music-green mysql
cf unbind-service xxxx
cf delete-service xxxx
```

# 微服务改造
root@CloudFoundry:~/cf/auth# cat manifest.yml  
```
applications:
- name: myauth
  memory: 1G
  path: foodie-auth-service-1.0-SNAPSHOT.jar
  services:
  - myregistry
  - myredis
  env:
    JBP_CONFIG_SPRING_AUTO_RECONFIGURATION: '{enabled: false}'
```

Foodie-cloud-master->pom.xml  
```
            <!-- SpringCloud from Pivotal Cloud Foundry-->
            <dependency>
                <groupId>io.pivotal.spring.cloud</groupId>
                <artifactId>spring-cloud-services-dependencies</artifactId>
                <version>3.1.0.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
```  

Foodie-auth-service->pom.xml  
```
       <!--<dependency>-->
            <!--<groupId>org.springframework.cloud</groupId>-->
            <!--<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>-->
        <!--</dependency>-->

       <!-- SpringCloud from Pivotal Cloud Foundry-->
        <dependency>
            <groupId>io.pivotal.spring.cloud</groupId>
            <artifactId>spring-cloud-services-starter-service-registry</artifactId>
        </dependency>
```  

foodie-auth-service-> application.yml  
```
server:
  port: 8080

spring:
  application:
    name: foodie-auth-service
#  redis:
#    database: 0
#    host: 172.19.46.183
#    port: 6379
##    password: imooc
#  zipkin:
#    discoveryClientEnabled: true
#    base-url: http://172.19.46.183:20005/
#    locator:
#      discovery:
#        enabled: true

#eureka:
#  client:
#    serviceUrl:
#      defaultZone: http://172.19.46.183:20000/eureka/
#  instance:
#    instance-id: ${eureka.instance.ip-address}:${server.port}
#    ip-address: 172.19.46.183
#    prefer-ip-address: true 
```  

AuthApplication.java  
```
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableDiscoveryClient
```