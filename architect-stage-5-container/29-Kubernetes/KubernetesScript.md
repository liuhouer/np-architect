(Git对md的格式支持比较差，大家可以下载到本地或者在git上选择原始文件方式进行阅读)
# 安装Kubernetes
https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/  
```
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF
```
关闭Selinux  
```
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
```
安装Kubernetes安装包  
```
yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes

systemctl enable --now kubelet
```

# 创建Kubernetes集群  
```
kubeadm init --apiserver-advertise-address=172.20.230.77 --pod-network-cidr=10.244.0.0/16

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 172.20.230.77:6443 --token q5yw61.yix5or5y4z0dvbpn \
    --discovery-token-ca-cert-hash sha256:6d199317942ef966eb76ddbe8a546e8ffd76d1644941fbb846ffe18c0dee0e5c


kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```

# 开始部署应用
注：部分小伙伴的Kubernetes版本较新的可以采用apiVersion: apps/v1或者apiVersion: apps/v1beta1来部署Deployment。  
```
[root@training3 ~]# cat mynginx-deployment.yml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 5
  template:
    metadata:
      labels:
        app: web_server
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
```
停止POD小技巧：  
```
kubectl scale --replicas=0 deployment/nginx-deployment
```
指定node节点小技巧：  
```
kubectl get node --show-labels
Kubectl label node training4 disktype=ssd
```
修改mynginx-pod.yaml文件，添加：  
```
      nodeSelector:
        disktype: ssd
```
再执行命令：  
```
Kubectl label node training 4 disktype-
```

# 常用Kubernetes的各种控制器命令
```
kubectl get deployment
kubectl describe deployment xxx
kubectl get replicaset
kubectl describe replicates
kubectl get pod
kubectl describe pod
kubectl get pod -o wide
kubectl get daemonset --namespace=kube-system
kubectl edit daemonset xxx --namespace=kube-system
kubectl rollout history deployment
kubectl apply -f nginx-deployment.yaml --record
kubectl rollout undo deployment --to-revision=xx
```
# Kubernetes作业发布
```
[root@training3 ~]# cat hello.yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: hello
spec:
  template:
    metadata:
      name: hello
    spec:
      containers:
      - name: hello
        image: busybox
        command: ["echo", "hello from feiyang"]
      restartPolicy: Never

[root@training3 ~]# cat myjob.yaml
apiVersion: batch/v2alpha1
kind: CronJob
metadata:
  name: hello
spec:
  schedule: "*/1 * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox
            command: ["echo", "hello feiyang"]
          restartPolicy: Never
```

# 切换网络协议栈  
```
kubeadm reset
kubeadm init --apiserver-advertise-address=172.20.230.77 --pod-network-cidr=10.244.0.0/16
  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
参考: https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/
```
kubectl apply -f https://docs.projectcalico.org/v3.8/manifests/canal.yaml
```

# Kubernetes网络配置和测试
```
[root@training3 ~]# cat service.yml
apiVersion: v1
kind: Service
metadata:
  name: nginx-svc
spec:
  type: NodePort
  selector:
    app: web_server
  ports:
  - protocol: TCP
    nodePort: 30000
    port: 8080
    targetPort: 80

[root@training3 ~]# cat policy.yml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: access-nginx
spec:
  podSelector:
    matchLabels:
      app: web_server
  ingress:
  - from:
    - podSelector:
        matchLabels:
          access: "true"
    - ipBlock:
        cidr: 10.244.0.0/16
    ports:
    - protocol: TCP
      port: 80

kubectl run busybox --rm -it --image=busybox -/bin/sh
/# wget nginx-svc:default:8080

kubectl run busybox --rm -it --labels="access=true" --image=busybox -/bin/sh
/# wget nginx-svc:default:8080
```

# Helm安装部署
```
curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get | bash
helm version
helm init

[root@training3 ~]# helm search mysql
NAME                            	CHART VERSION	APP VERSION	DESCRIPTION
stable/mysql                    	1.3.1        	5.7.14     	Fast, reliable, scalable, and easy to use open-source rel...
stable/mysqldump                	2.6.0        	2.4.1      	A Helm chart to help backup MySQL databases using mysqldump
stable/prometheus-mysql-exporter	0.5.1        	v0.11.0    	A Helm chart for prometheus mysql exporter with cloudsqlp...
stable/percona                  	1.1.0        	5.7.17     	free, fully compatible, enhanced, open source drop-in rep...
stable/percona-xtradb-cluster   	1.0.2        	5.7.19     	free, fully compatible, enhanced, open source drop-in rep...
stable/phpmyadmin               	3.0.6        	4.9.0-1    	phpMyAdmin is an mysql administration frontend
stable/gcloud-sqlproxy          	0.6.1        	1.11       	DEPRECATED Google Cloud SQL Proxy
stable/mariadb                  	6.9.1        	10.3.18    	Fast, reliable, scalable, and easy to use open-source rel...

kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
kubectl patch deploy --namespace kube-system tiller-deploy -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
```

# Kubernetes Dashoboard 图形化界面
```
https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/
https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md

[root@training3 ~]# kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta4/aio/deploy/recommended.yaml

namespace/kubernetes-dashboard created
serviceaccount/kubernetes-dashboard created
service/kubernetes-dashboard created
secret/kubernetes-dashboard-certs created
secret/kubernetes-dashboard-csrf created
secret/kubernetes-dashboard-key-holder created
configmap/kubernetes-dashboard-settings created
role.rbac.authorization.k8s.io/kubernetes-dashboard created
clusterrole.rbac.authorization.k8s.io/kubernetes-dashboard unchanged
rolebinding.rbac.authorization.k8s.io/kubernetes-dashboard created
clusterrolebinding.rbac.authorization.k8s.io/kubernetes-dashboard unchanged
deployment.apps/kubernetes-dashboard created
service/dashboard-metrics-scraper created
deployment.apps/dashboard-metrics-scraper created

[root@training3 ~]# kubectl --namespace=kubernetes-dashboard edit service kubernetes-dashboard
ClusterIP -> NodePort

[root@training3 ~]# kubectl -n kubernetes-dashboard get service
NAME                        TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)         AGE
dashboard-metrics-scraper   ClusterIP   10.100.103.3    <none>        8000/TCP        5m24s
kubernetes-dashboard        NodePort    10.100.15.196   <none>        443:32572/TCP   5m24s
Open with Firefox: https://public_ip:32572

[root@training3 ~]# kubectl apply -f dashboard.yml
serviceaccount/admin-user created
clusterrolebinding.rbac.authorization.k8s.io/admin-user created

[root@training3 ~]# kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
Name:         admin-user-token-dtqm2
Namespace:    kubernetes-dashboard
Labels:       <none>
Annotations:  kubernetes.io/service-account.name: admin-user
              kubernetes.io/service-account.uid: 2e360fda-a16d-44ef-8281-2b3e03618151

Type:  kubernetes.io/service-account-token

Data
====
token:      eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLWR0cW0yIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiIyZTM2MGZkYS1hMTZkLTQ0ZWYtODI4MS0yYjNlMDM2MTgxNTEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.Coa17yCI7FkRDEhYbrRhPFGbdN_5Buwu3cxvNzLv0CTXiRpz1WjMEC11GKG1xue16nq8KXDZy0BYoRhucFF0tYWUfHKC9Nt6cDh9yXuqGiHsL05h-1s0aA04UiQ850NHrGbo8GqTzN22iZfhMac4tdadU-QiPvQa5GzGVPsT9BsO2iv58pONjeprt3b0g6pMM6rqad8PeODq9UAhPXIZXiUiRan7Nh24AGKv5SwJ6FA7ueBP5OoCjKTMvxiBuiy3gnk39f1kWB2XH9vCKMONdS1GgC15wuXC0k7eaAJZQp-y0nOuq7Jv43hFWTMXhOoJf5kuu4KB9MJq0uGgIeZaEg
ca.crt:     1025 bytes
namespace:  20 bytes
```

# 部署Kubernetes heapster监控
```
kubectl apply --namespace kube-system -f "https://cloud.weave.works/k8s/scope.yaml?k8s-version=$(kubectl version | base64 | tr -d '\n')"

git clone https://github.com/kubernetes-retired/heapster.git
kubectl apply -f heapster/deploy/kube-config/influxdb/
kubectl apply -f heapster/deploy/kube-config/rbac/heapster-rbac.yaml
```

# 部署Prometheus监控
```
[root@training3 ~]# git clone https://github.com/coreos/prometheus-operator
[root@training3 ~]# cd prometheus-operator
kubectl create namespace monitoring
helm install --name prometheus-operator --set rbacEnable=true --namespace=monitoring stable/prometheus-operator
kubectl edit service prometheus-operator-grafana --namespace=monitoring
kubectl edit service prometheus-operator-prometheus --namespace=monitoring
kubectl edit service prometheus-operator-alertmanager --namespace=monitoring
ClusterIP -> NodePort

kubectl edit secret --namespace=monitoring prometheus-operator-grafana
  admin-password: cHJvbS1vcGVyYXRvcg==
  admin-user: YWRtaW4=

[root@training4 ~]# echo -n YWRtaW4=| base64 -d
admin[root@training4 ~]# echo -n cHJvbS1vcGVyYXRvcg==|base64 -d
prom-operator
```

# 部署EFK（Fluentd, Elasticsearch, Kibana）监控
下载Yaml文件到$download_directory：  
https://github.com/kubernetes/kubernetes/tree/master/cluster/addons/fluentd-elasticsearch  
修改kibana-deployment.yaml，删除$SERVER_BASEPATH  
修改kibana-logging service，成为NodePort类型  
```
kubectl apply -f $download_directory
```

# NFS小技巧
```
yum -y install nfs-utils rpcbind
sudo systemctl enable rpcbind
sudo systemctl start rpcbind
sudo systemctl enable nfs-server
sudo systemctl start nfs-server

/mnt	172.20.230.0/24(rw,sync,no_root_squash)
```

# Kubernetes存储配置
```
[root@training3 ~]# cat mysecret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
data:
  username: YWFh
  password: Y2Nj
[root@training3 ~]# cat mypv.yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mypv
spec:
  capacity:
    storage: 1Gi
  accessModes:
  - ReadWriteOnce
  persistentVolumeReclaimPolicy: Recycle
  storageClassName: nfs
  nfs:
    path: /mnt
    server: 172.20.230.78
[root@training3 ~]# cat mypvc.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mypvc
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
  storageClassName: nfs
[root@training3 ~]# cat my/mynginx.yaml
apiVersion: v1
kind: Pod
metadata:
  name: mynginx
spec:
  containers:
  - name: nginx
    image: nginx:1.7.9
    volumeMounts:
    - mountPath: /mymount
      name: mount-volume
  volumes:
  - name: mount-volume
    emptyDir: {}
[root@training3 ~]# cat mynginx-pod.yaml
apiVersion: v1
kind: Pod
metadata:
  name: mynginx
spec:
  containers:
  - name: nginx
    image: nginx:1.7.9
    volumeMounts:
    - mountPath: /mymount
      name: mount-volume
  volumes:
  - name: mount-volume
    secret:
      secretName: mysecret
```

# 认证和授权
```
[root@training3 ~]#cat mysa.yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: mysa
--- 
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: mysa-role
rules:
- apiGroups:
  - ""
  resources:
  - pods
  verbs:
  - get
  - list
  - watch
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: mysa-rolebinding
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: mysa-role
subjects:
- kind: ServiceAccount
  name: mysa

kubectl get secret
Kubectl describe secret mysa-token-tmnt7
[root@training3 ~]# cat /etc/kubernetes/manifests/kube-apiserver.yaml |grep auth
```

# 其他Yaml参照文件
```
[root@training3 ~]# cat nginx.yml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  template:
    metadata:
      labels:
        app: web_server
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
        volumeMounts:
        - mountPath: /mnt/empdir
          name: shared-volume
        env:
        - name: SECRET_USERNAME
          valueFrom:
            secretKeyRef:
              name: mysecret
              key: username
        - name: SECRET_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysecret
              key: password
      volumes:
      - name: shared-volume
        secret:
          secretName: mysecret
          items:
          - key: username
            path: my/myusername
          - key: password
            path: my/mypassword
---
apiVersion: v1
kind: Service
metadata:
  name: nginx-svc
spec:
  type: NodePort
  selector:
    app: web_server
  ports:
  - protocol: TCP
    nodePort: 30000
    port: 8080
    targetPort: 80
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mypv1
spec:
  capacity:
    storage: 1Gi
  accessModes:
  - ReadWriteOnce
  persistentVolumeReclaimPolicy: Recycle
  storageClassName: nfs
  nfs:
    path: /mnt
    server: 172.20.230.77
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mypvc1
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
  storageClassName: nfs
---
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
data:
  password: MTIzNDU2
  username: YWRtaW4=


[root@training3 ~]# cat dashboard.yml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard


[root@training3 ~]# cat policy.yml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: access-nginx
spec:
  podSelector:
    matchLabels:
      app: web_server
  ingress:
  - from:
    - podSelector:
        matchLabels:
          access: "true"
    - ipBlock:
        cidr: 10.244.0.0/16
    ports:
    - protocol: TCP
      port: 80
```

# 微服务部署
1. 阿里云上部署Mysql和Redis  
2. 阿里云上激活kuberntes集群  
3. 部署rabbitmq:management和ClusterIP类型的服务  
4. 部署Eureka Server  
```
application.yml:  eureka.instance.hostname: eureka
```
mvn install  
将jar包上传到ECS服务器，再进行容器镜像制作    
```
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD registry-center-1.0-SNAPSHOT.jar registry-center-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","registry-center-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myregistry .

$ docker images
$ docker login --username=ieeezhang registry.cn-shanghai.aliyuncs.com
$ docker tag my registry registry.cn-shanghai.aliyuncs.com/feiyang/myregistry:1.0
$ docker push registry.cn-shanghai.aliyuncs.com/feiyang/myregistry:1.0
```
图形化界面，创建myregistry部署和myregistry-svc服务（load balander模式）  

5. 部署Config Server  
```
application.yml:
  rabbitmq:
    host: rabbitmq-svc
    port: 5672
#    username: guest
#    password: guest

eureka:
  client:
    serviceUrl:
      defaultZone: http://myregistry-svc:20000/eureka/
  instance:
    instance-id: ${spring.cloud.client.ip-address}.${server.port}
    prefer-ip-address: true
```
mvn install  
将jar包上传到ECS服务器，再进行容器镜像制作 
```
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD config-server-1.0-SNAPSHOT.jar config-server-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","config-server-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myconfig .
$ docker tag myconfig registry.cn-shanghai.aliyuncs.com/feiyang/myconfig:1.0
$ docker push registry.cn-shanghai.aliyuncs.com/feiyang/myconfig:1.0
```
图形化界面，创建myconfig部署  

6. 部署Auth Server  
```
application.yml
  redis:
    database: 0
    host: r-uf6jixwat2fksjiuwdpd.redis.rds.aliyuncs.com
    port: 6379
    password: Im00cIm00c
#  zipkin:
#    discoveryClientEnabled: true
#    base-url: http://ZIPKIN-SERVER/
#    locator:
#      discovery:
#        enabled: true
  sleuth:
    sampler:
      probability: 1
eureka:
  client:
    serviceUrl:
      defaultZone: http://myregistry-svc:20000/eureka/
  instance:
    instance-id: ${spring.cloud.client.ip-address}.${server.port}
    prefer-ip-address: true
```
mvn install  
将jar包上传到ECS服务器，再进行容器镜像制作  
```
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-auth-service-1.0-SNAPSHOT.jar foodie-auth-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-auth-service-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myauth .
$ docker tag myauthregistry.cn-shanghai.aliyuncs.com/feiyang/myauth:1.0
$ docker push registry.cn-shanghai.aliyuncs.com/feiyang/myauth:1.0
```
图形化界面，创建myauth部署  

7. 部署User Server  
```
Bootstrap.yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://myregistry-svc:20000/eureka/
  instance:
    instance-id: ${spring.cloud.client.ip-address}.${server.port}
    prefer-ip-address: true

application.yml
spring.datasource.username: imooc

Application-dev.yml
spring:
  datasource:                                           
  # 数据源的相关配置
  # 拆分数据源到独立database instance，或者独立schema
#    url: jdbc:mysql://localhost:3306/foodie-cloud-item?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true
    url: jdbc:mysql://rm-uf68bz4sze09157vcdo.mysql.rds.aliyuncs.com:3306/foodie_shop_dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true
    password: Im00cIm00c
#    url: ${mariadb.url}
#    password: ${mariadb.password}
  redis:
    # Redis 单机单实例
    database: 0
    host: r-uf6jixwat2fksjiuwdpd.redis.rds.aliyuncs.com
    port: 6379
    password: Im00cIm00c
  # 链路追踪
#  zipkin:
#    discoveryClientEnabled: true
#    base-url: http://ZIPKIN-SERVER/
#    locator:
#      discovery:
#        enabled: true
#    # 以HTTP上传数据到Zipkin
#    # WHY? bus依赖项导入了rabbitmq的依赖项，zipkin会默认使用mq
#    sender:
#      type: web
  sleuth:
    sampler:
      probability: 1
  ### 推送变更的时候用
  rabbitmq:
    host: rabbitmq-svc
    port: 5672
#    username: guest
#    password: guest
```
mvn install  
将jar包上传到ECS服务器，再进行容器镜像制作  
```
[root@training1 foodie-cloud]# cat Dockerfile
FROM java:8
ADD foodie-user-web-1.0-SNAPSHOT.jar foodie-user-web-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","foodie-user-web-1.0-SNAPSHOT.jar"]
[root@training1 foodie-cloud]# docker build -t myuser .
$ docker tag myuser registry.cn-shanghai.aliyuncs.com/feiyang/myuser:1.0
$ docker push registry.cn-shanghai.aliyuncs.com/feiyang/myuser:1.0
```  
8. 准备数据库内容  
```
[root@training2 ~]# mysql -h rm-uf68bz4sze09157vcdo.mysql.rds.aliyuncs.com -u imooc -p
CREATE DATABASE IF NOT EXISTS foodie_shop_dev DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE foodie_shop_dev;
source foodie_shop_dev.sql;
```

9. 准备Item Server, Cart Server, Order Server，Gateway  
和Auth Server，User Server类似，此处略过5000字。。。
