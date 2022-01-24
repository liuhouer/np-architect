### RabbitMQ急速入门

------

急速入门，在这里我们使用RabbitMQ 3.6.5 版本进行操作：

- 环境搭建：

- 官网地址：http://www.rabbitmq.com/

- 环境描述：Linux（centos7 Redhat7）

  ```shell
 ## 1. 首先在Linux上进行一些软件的准备工作，yum下来一些基础的软件包
yum install build-essential openssl openssl-devel unixODBC unixODBC-devel make gcc gcc-c++ kernel-devel m4 ncurses-devel tk tc xz

## 配置好主机名称：/etc/hosts /etc/hostname

## 2. 下载RabbitMQ所需软件包（本神在这里使用的是 RabbitMQ3.6.5 稳定版本）
wget www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm
wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-1.1.el7.lux.x86_64.rpm
wget www.rabbitmq.com/releases/rabbitmq-server/v3.6.5/rabbitmq-server-3.6.5-1.noarch.rpm

## 3. 安装服务命令
rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm 
rpm -ivh socat-1.7.3.2-1.1.el7.x86_64.rpm
rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm

## 4. 修改用户登录与连接心跳检测，注意修改
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app
修改点1：loopback_users 中的 <<"guest">>,只保留guest （用于用户登录）
修改点2：heartbeat 为10（用于心跳连接）

## 5. 安装管理插件

## 5.1 首先启动服务(后面 | 包含了停止、查看状态以及重启的命令)
/etc/init.d/rabbitmq-server start | stop | status | restart

## 5.2 查看服务有没有启动： lsof -i:5672 （5672是Rabbit的默认端口）
rabbitmq-plugins enable rabbitmq_management

## 5.3 可查看管理端口有没有启动： 
lsof -i:15672 或者 netstat -tnlp | grep 15672

## 6. 一切OK 我们访问地址，输入用户名密码均为 guest ：
## http://你的ip地址:15672/

## 7. 如果一切顺利，那么到此为止，我们的环境已经安装完啦
  ```

- 1
