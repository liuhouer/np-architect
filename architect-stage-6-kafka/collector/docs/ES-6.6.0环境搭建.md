### ES-6.6.0环境搭建：

```shell

## 三个节点解压elasticsearch-6.6.0.tar.gz
tar -zxvf elasticsearch-6.6.0.tar.gz -C /usr/local/

## 修改配置文件：
vim elasticsearch-6.6.0/config/elasticsearch.yml

### elasticsearch.yml 配置
cluster.name: es_log_cluster
node.name: es-node-1    ## es-node-2  es-node-3 不同节点名称不同
path.data: /usr/local/elasticsearch-6.6.0/data  ## es数据存放位置
path.logs: /usr/local/elasticsearch-6.6.0/logs  ## es日志存放位置
bootstrap.memory_lock: true     ## 锁内存,强制占用(类似oracle的锁内存)保证es启动正常
network.host: 192.168.0.238     ## network.host不同节点IP对应 （对外发布IP）
## 防止脑裂配置
## 当新节点加入的时候，配置一个初始化主机列表用于节点发现.
## 默认的主机列表是 ["127.0.0.1", "[::1]"]
discovery.zen.ping.unicast.hosts: ["192.168.0.238:9300", "192.168.0.239:9300", "192.168.0.240:9300"]
## 最小节点数，为了避免脑裂的发生，使用如下配置（数值为节点总数/2 + 1）
discovery.zen.minimum_master_nodes: 2
# 如果集群发生重启，直到N个节点启动完成，才能开始进行集群初始化恢复动作
gateway.recover_after_nodes: 2
# 集群应该预期有几个节点（master或node都算）
gateway.expected_nodes: 3
### 等待凑齐预期节点时间，例如：先等凑够了3个节点，再等5分钟看看有没有凑齐5个节点
gateway.recover_after_time: 5m
# 禁止在一个操作系统启动多个节点 
node.max_local_storage_nodes: 1
# 删除索引时，需要明确的名称
action.destructive_requires_name: true
# 防止同一个分片的主副本放在同一台机器上
cluster.routing.allocation.same_shard.host: true
####################################################
#此处为6.6.0版本安装的差异点
#通过bin/elasticsearch-certutil ca生成elastic-stack-ca.p12
#将上面生成的文件复制到所有节点对应路径/usr/local/elasticsearch-6.6.0/config/elastic-stack-ca.p12
#使用自带shell修改密码,将所有密码修改为123456
#bin/elasticsearch-setup-passwords interactive
#添加配置
http.cors.allow-headers: Authorization,X-Requested-With,Content-Length,Content-Type
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.keystore.path: /usr/local/elasticsearch-6.6.0/config/elastic-stack-ca.p12
xpack.security.transport.ssl.truststore.path: /usr/local/elasticsearch-6.6.0/config/elastic-stack-ca.p12
####################################################
## 生成认证(一台节点生成，发送到其余节点上 秘钥)
elasticsearch-certutil ca
mv elastic-stack-ca.p12 ../config/

## 赋权
chown -R baihezhuo:baihezhuo /usr/local/elasticsearch-6.6.0/
## 测试启动
/usr/local/elasticsearch-6.6.0/bin/elasticsearch

## x-pack破解：
## 覆盖elasticsearch-6.6.0\modules\x-pack\x-pack-core\x-pack-core-6.6.0.jar中的两个类
## 用LicenseVerifier.class 覆盖x-pack-core-6.6.0.jar\org\elasticsearch\license目录下的同名文件
## 用 XPackBuild.class 覆盖 x-pack-core-6.6.0.jar\org\elasticsearch\xpack\core 目录下的同名文件
## 类获取地址https://pan.baidu.com/s/1ESqoZW8eieO7Zdgs31hxsQ,密码:uqnd

## jar包替换
/usr/local/elasticsearch-6.6.0/modules/x-pack-core/x-pack-core-6.6.0.jar

#使用自带shell修改密码,将所有密码修改为123456
/usr/local/elasticsearch-6.6.0/bin/elasticsearch-setup-passwords interactive

##启动集群并测试：
curl -u elastic:123456 192.168.11.35:9200

## 解压kibana
wget https://artifacts.elastic.co/downloads/kibana/kibana-6.6.0-linux-x86_64.tar.gz
tar -zxvf kibana-6.6.0-linux-x86_64.tar.gz -C /usr/local/
mv kibana-6.6.0-linux-x86_64/ kibana-6.6.0
## 进入kibana目录，修改配置文件
vim /usr/local/kibana-6.6.0/config/kibana.yml
## 修改配置如下：
server.host: "0.0.0.0"
server.name: "192.168.11.35"
elasticsearch.hosts: ["http://192.168.11.35:9200"]
elasticsearch.username: "elastic"
elasticsearch.password: "123456"
## 启动：
/usr/local/kibana-6.6.0/bin/kibana &
## 指定配置文件启动：
nohup /usr/local/kibana-6.6.0/bin/kibana -c /usr/local/kibana-6.6.0/config/kibana.yml > /dev/null 2>&1 &
## 访问:
http://192.168.0.236:5601/app/kibana (5601为kibana默认端口)

##申请license：
https://license.elastic.co/registration

## 修改申请的license， 注意license.json文件名称不能变否则认证失败
1."type":"basic" 替换为 "type":"platinum" # 基础版变更为铂金版
2."expiry_date_in_millis":1561420799999 替换为 "expiry_date_in_millis":3107746200000 # 1年变为50年

## 启动elasticsearch服务 和 kibana服务
## 进入kibana后台，Management->License Management上传修改后的token


##ik分词器：
## 安装elasticsearch-ik分词器
https://github.com/medcl/elasticsearch-analysis-ik
## 下载 https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.6.0/elasticsearch-analysis-ik-6.6.0.zip
mkdir -p /usr/local/elasticsearch-6.6.0/plugins/ik/
## 上传到/usr/local/software下 elasticsearch-analysis-ik-6.6.0.zip
## 进行解压到刚创建的/usr/local/elasticsearch-6.6.0/plugins/ik/目录：
unzip -d /usr/local/elasticsearch-6.6.0/plugins/ik/ elasticsearch-analysis-ik-6.6.0.zip

## 查看是否ok
cd /usr/local/elasticsearch-6.6.0/plugins/ik/
## 重新复权
chown -R baihezhuo:baihezhuo /usr/local/elasticsearch-6.6.0/

## 重新启动ES节点，显示如下信息代表加载ik分词器成功
[es-node01] loaded plugin [analysis-ik]
```



```shell
## 切换用户
su baihezhuo

## 运行es服务
/usr/local/elasticsearch-6.6.0/bin/elasticsearch -d

## 健康情况
- 查看某个节点情况：http://192.168.11.35:9200/
- 查看集群节点情况：http://192.168.11.35:9200/_cluster/health?pretty

## 正常关闭命令：
kill -sigterm pid

## head启动命令：
cd /usr/local/elasticsearch-head/ && /usr/local/elasticsearch-head/node_modules/grunt/bin/grunt server &

## head关闭命令：先找到head占用端口所在的进程，然后kill即可
netstat -tunpl | grep 9100

## head访问：http://192.168.11.35:9100?auth_user=elastic&auth_password=123456

## kibana启动：
/usr/local/kibana-6.6.0/bin/kibana &
## kibana指定配置文件启动：
nohup /usr/local/kibana-6.6.0/bin/kibana -c /usr/local/kibana-5.6.2/config/kibana.yml > /dev/null 2>&1 &

## kibana访问:
http://192.168.11.35:5601/app/kibana (5601为kibana默认端口)

## kibana关闭命令：先找到head占用端口所在的进程，然后kill即可
netstat -tunpl | grep 5601
```

