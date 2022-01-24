(Git对md的格式支持比较差，大家可以下载到本地或者在git上选择原始文件方式进行阅读)  
# Env preparation
Reference: 
https://github.com/mesosphere/playa-mesos

Install VirtualBox

Install Vagrant

Clone this repository
```
git clone https://github.com/mesosphere/playa-mesos.git
cd playa-mesos
```
Make sure tests pass
```
bin/test
```
Start the VM
```
vagrant up
```
Connect to the Mesos Web UI on 10.141.141.10:5050 and the Marathon Web UI on 10.141.141.10:8080  
SSH to the VM
```
vagrant ssh
ps -ef | grep mesos
```
Halt the VM
```
vagrant halt
```
Destroy the VM
```
vagrant destroy
```
# Application deployment
Prepare docker image
```
sudo docker pull mesosphere/marathon-lb
sudo docker pull nginx:1.9
```
marathonlb.json
```
{
"id": "marathon-lb",
"instance": 1,
"constraints": [["hostname", "UNIQUE"]],
"container": {
"type": "DOCKER",
"docker": {
"image": "mesosphere/marathon-lb",
"priviledged": true,
"network": "HOST"
}
},
"args": ["sse", "-m", "http://10.141.141.10:8080","--group","external"]
}
```
nginx.json
```
{
"id": "nginx",
"labels": {
"HAPROXY_GROUP": "external"
},
"cpus": 0.2,
"mem": 20.0,
"instances": 2,
"healthChecks": [{ "path": "/"}],
"container": {
"type": "DOCKER",
"docker": {
"image": "nginx:1.9",
"network": "BRIDGE",
"portMappings": [{"containerPort":80,"hostPort":0,"servicePort":80,"protocol":"tcp"}]
}
}
}
```
Deploy application
```
curl -i -H 'Content-Type: application/json' 10.141.141.10:8080/v2/apps -d @marathonlb.json

curl -i -H 'Content-Type: application/json' 10.141.141.10:8080/v2/apps -d @nginx.json
```
Verify applicaton
```
docker cp index1.html 84aa18da936e:/usr/share/nginx/html/index.html
```
nginx_redis.json
```
{
    "id": "nginx-redis",
    "apps": [
    {
    "id": "nginx",
    "cpus": 0.2,
    "mem": 20.0,
    "instances": 1,
    "healthChecks": [{ "path": "/"}],
    "env": {
        "REDIS_HOST": "localhost",
        "REDIS_PORT": "9000"
    },
    "dependencies": ["nginx-redis/redis"],
    "container": {
    "type": "DOCKER",
    "docker": {
    "image": "nginx:1.9",
    "network": "BRIDGE",
    "portMappings": [{"containerPort":80,"hostPort":0,"servicePort":80,"protocol":"tcp"}]
    }
    }
    },
    {
    "id": "redis",
    "cpus": 0.2,
    "mem": 20.0,
    "instances": 1,
    "container": {
    "type": "DOCKER",
    "docker": {
    "image": "redis:3.2",
    "network": "BRIDGE",
    "portMappings": [{"containerPort":6379,"hostPort":0,"servicePort":9000,"protocol":"tcp"}]
    }
    }
    }
    ]
}
```
Deploy application group
```
curl -i -H 'Content-Type: application/json' 10.141.141.10:8080/v2/groups -d @nginx_redis.json
```

# Microservice Adoption
修改Foodie-cloud-master的代码: Foodie-auth-service application.yml  
1. 修改redis server IP  
```
  redis:
    database: 0
    host: 10.141.141.10
    port: 6379
```
2. 注释掉zipkin部分  
```
#  zipkin:
#    discoveryClientEnabled: true
#    base-url: http://172.19.46.183:20005/
#    locator:
#      discovery:
#        enabled: true
#  sleuth:
#    sampler:
#      probability: 1
```
3. 修改eureka和在eureka注册的本服务IP地址  
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://10.141.141.10:20000/eureka/
  instance:
    instance-id: ${eureka.instance.ip-address}:${server.port}
    ip-address: 10.141.141.10
    prefer-ip-address: true
```
4. 编译 mvn install  
5. 复制到服务器  
```  
cd /Users/xin/Downloads/foodie-cloud-master

scp MyPackages/registry-center-1.0-SNAPSHOT.jar MyPackages/foodie-auth-service-1.0-SNAPSHOT.jar root@10.141.141.10:/root/microservice/

cd ../../mesos/playa-mesos/

Vagrant status

Vagrant ssh

sudo su -

docker pull v6atfsm9.mirror.aliyuncs.com/library/java
docker tag v6atfsm9.mirror.aliyuncs.com/library/java:latest java:8

root@mesos:~/microservice# cat Dockerfile
FROM java:8
ADD registry-center-1.0-SNAPSHOT.jar registry-center-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","registry-center-1.0-SNAPSHOT.jar"]

docker build -t myregistry .

root@mesos:~/microservice# cat Dockerfile
FROM java:8
ADD foodie-auth-service-1.0-SNAPSHOT.jar foodie-auth-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-auth-service-1.0-SNAPSHOT.jar"]

docker build -t myauth .
```  
6. 安装应用  
Open mesos: http://10.141.141.10:5050/#/  
Marathon: http://10.141.141.10:8080/ui/#/apps  
In marathon GUI, create application  

# Autoscale
如果有代理服务器，可以直接从官方镜像下载autoscale模块：  
```
root@mesos:~# docker pull mesosphere/marathon-lb-autoscale
```
如果服务器无法访问外网，也可以通过国内镜像下载： 
```
root@mesos:~# docker pull v6atfsm9.mirror.aliyuncs.com/mesosphere/marathon-lb-autoscale

docker tag v6atfsm9.mirror.aliyuncs.com/mesosphere/marathon-lb-autoscale marathon-lb-autoscale
```
配置安装autoscale模块  
```
sudo vi autoscale.json
{
  "id": "marathon-lb-autoscale",
  "args":[
    "--marathon", "http://10.141.141.10:8080",
    "--haproxy", "http://10.141.141.10:9090",
    "--apps", "myauth_10006",
    "--target-rps", "100",
    "--max-instances", "3",
    "--interval", "1"
  ],
  "cpus": 0.1,
  "mem": 16.0,
  "instances": 1,
  "container": {
    "type": "DOCKER",
    "docker": {
      "image": "marathon-lb-autoscale",
      "network": "HOST",
      "forcePullImage": false
    }
  }
}

curl -i -H 'Content-Type: application/json' 10.141.141.10:8080/v2/apps -d @autoscale.json
```