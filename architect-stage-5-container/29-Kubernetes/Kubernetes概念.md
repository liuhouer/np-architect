# K8S集群架构
1. Master节点：
  - API服务端
  - 调度器
  - 控制器管理器（namespace和复制管理，deployment、replicaset、daemonset、job管理等）
  - ETCD 集群配置、仲裁管理
  - 集群网络管理（Flannel、Weave、Calico、Canal等）
  - Kubelet kubernetes核心工作单元
  - Kube-proxy Kubernetes网络代理
  - Docker daemon

2. Node节点：
 - Kubelet kubernetes核心工作单元
 - Kube-proxy Kubernetes网络代理
 - Docker daemon

---
# K8S逻辑概念
1. POD
  -  Kubernetes最小工作单元
  - 运行在一个Node上
  - Pod中的容器共享网络和存储
2. 控制器 Controller
  - Deployment 支持回滚的多副本POD应用
  - ReplicaSet 多副本POD应用
  - DaemonSet 每个node运行一份的后台应用
  - StatefulSet 有状态服务
  - Job 短时/定时作业
3. Label
  - 用于区分不同的POD或机器资源
  - Pod标签：metadata: labels: XXX: YYYY
  - 调度约束：spec: selector: XXX: YYYY
  - spec: podSelector: matchLabels: XXX: YYYY
4. Namespace
  - 虚拟K8S集群
  - 管理单元的逻辑划分

---
# K8S管理
1. Kubernetes环境搭建
  - 软件安装 – Docker、Kubelet、Kubeadm、Kubectl
  - Master上创建集群 – kubeadm init & kubeadm apply
  - Node加入集群 – kubeadm join 
2. 容器部署
  - Docker镜像准备
  - YAML + kubectl apply
3. 健康检查
  - LivenessProbe（容器级别）
  - ReadinessProbe（容器级别）
  - restartPolicy （Pod级别）
4. 滚动升级
  - 升级：更新镜像 + kubectl apply
  - 查询： kubectl rollout history deployment
  - 回滚： kubectl rollout undo deployment
5. K8S排错
  - 初始化错误
  - 网络错误
  - 证书错误
  - YAML配置错误
  - 系统selinux配置错误
  - iptables错误
  - DNS错误
  - RBAC错误
6. POD访问方式
  - ClusterIP Service （内部）
  - NodePort Service （由内而外）
  - LoadBalancer Service （外部）
  - Ingress（外部）
7. 存储管理
  - Volume卷
  > 分配方式：emtpyDir、hostPath、PV/PVC、Storage Provider等
  - ConfigMap和Secret
  > 创建方式：from-literal、from-file、from-env-file、YAML  
  传递方式：volumes、env
8. 认证
  - 普通用户 User
  - 服务账号 Service Account
  - 客户端证书
  - 静态密码文件 Basic Auth
  - Token (Token Auth File、Bearer Token、Service Account Token、Java Web Token、Webhook Token)
9. 授权
  - RBAC（Role-Based Access Control）
  - Role 在单一namespace有效
  - ClusterRole 跨集群所有namespace有效  

---
# K8S扩展专题
1. 集群监控
  - 应用性能管理 (APM) - Prometheus
  - 业务追踪 - Zipkin、Jaeger
  - 日志管理 - ELK、EFK
2. Helm软件发布管理
  - Helm客户端
  - Tiller服务器端
  - Repository仓库
  - Chart安装部署手册
  - Release安装部署实现
3. CI/CD自动化部署
  - Jenkins pipeline
  - 其他开源/第三方CI/CD工具