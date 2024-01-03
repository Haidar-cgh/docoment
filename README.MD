## maven 热部署
```bash
mvn spring-boot:run
```
## docker安装mysql
```bash
docker pull mysql:8.0.11
docker run --restart=always -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0.11 
```
## 容器自启动
```bash
docker ps -a
docker start mysql-test
docker update --restart=always mysql-test
```
## redis master slave1 slave2
### redis_master.config
```text
# 绑定
bind 0.0.0.0
# 连接保护模式关闭
protected-mode no
# 端口
port 6379
# 数据库数量
databases 16
# rdb快照规则
# 在900秒（15分钟）后，如果至少更改了1个键
save 900 1
save 300 10
save 60 10000
# rdb快照文件名称
dbfilename dump.rdb
# 客户端连接数量设置
maxclients 10000
# AOF模式开启，默认关闭
appendonly yes
# 文件名
appendfilename "appendonly.aof"
# AOF同步机制 
# 每秒同步
appendfsync everysec
# 文件大小切割
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
# 淘汰机制设置
maxmemory-policy noeviction

#密码设置
requirepass 123456
```
### redis_slave.conf
```text
replicaof 172.17.0.4 6379
port 6379
masterauth 123456
requirepass 123456
```

```bash
docker run --restart=always --name redis-master -v /Users/cgh/Tool/redis/config/redis_master.conf:/etc/redis/redis.conf -v /Users/cgh/Tool/redis/data:/data -p 63790:6379 -d redis:latest redis-server /etc/redis/redis.conf

docker ps 
docker inspect 1513d8e488a2
172.17.0.4


docker run --restart=always --name redis-slave-1 -v /Users/cgh/Tool/redis/config/redis_slave.conf:/etc/redis/redis.conf -v /Users/cgh/Tool/redis/data:/data -p 63791:6379 -d redis:latest redis-server /etc/redis/redis.conf
docker run --restart=always --name redis-slave-2 -v /Users/cgh/Tool/redis/config/redis_slave.conf:/etc/redis/redis.conf -v /Users/cgh/Tool/redis/data:/data -p 63792:6379 -d redis:latest redis-server /etc/redis/redis.conf
// slaveof 172.17.0.4 6379

docker exec -it redis-master bash -c "redis-cli -h 127.0.0.1 -p 6379"
docker exec -it redis-slave-1 bash -c "redis-cli -h 127.0.0.1 -p 6379"
docker exec -it redis-slave-2 bash -c "redis-cli -h 127.0.0.1 -p 6379"

```

## docker 安装nacos

```bash
docker pull nacos/nacos-server
docker run -p 8848:8848 -p 9848:9848 -p 9849:9849 -p 7848:7848 -e MODE=standalone -e NACOS_AUTH_ENABLE=true -e PREFER_HOST_MODE=hostname -e MYSQL_SERVICE_HOST=172.17.0.3 -v /Users/cgh/Tool/nacos/logs:/home/nacos/logs --restart always --name nacos -d nacos/nacos-server
```
## nacos application.properties 
```properties
# spring 
server.servlet.contextPath=${SERVER_SERVLET_CONTEXTPATH:/nacos}
server.contextPath=/nacos
server.port=${NACOS_APPLICATION_PORT:8848}
server.tomcat.accesslog.max-days=30
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i %{Request-Source}i
server.tomcat.accesslog.enabled=${TOMCAT_ACCESSLOG_ENABLED:false}
server.error.include-message=ALWAYS
# default current work dir
server.tomcat.basedir=file:.
#*************** Config Module Related Configurations ***************#
### Deprecated configuration property, it is recommended to use `spring.sql.init.platform` replaced.
#spring.datasource.platform=${SPRING_DATASOURCE_PLATFORM:}
spring.sql.init.platform=${SPRING_DATASOURCE_PLATFORM:}
nacos.cmdb.dumpTaskInterval=3600
nacos.cmdb.eventTaskInterval=10
nacos.cmdb.labelTaskInterval=300
nacos.cmdb.loadDataAtStart=false
db.num=${MYSQL_DATABASE_NUM:1}
db.url.0=jdbc:mysql://${MYSQL_SERVICE_HOST}:${MYSQL_SERVICE_PORT:3306}/${MYSQL_SERVICE_DB_NAME:nacos}?${MYSQL_SERVICE_DB_PARAM:characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false}
db.user.0=${MYSQL_SERVICE_USER:root}
db.password.0=${MYSQL_SERVICE_PASSWORD:123456}
## DB connection pool settings
db.pool.config.connectionTimeout=${DB_POOL_CONNECTION_TIMEOUT:30000}
db.pool.config.validationTimeout=10000
db.pool.config.maximumPoolSize=20
db.pool.config.minimumIdle=2
### The auth system to use, currently only 'nacos' and 'ldap' is supported:
nacos.core.auth.system.type=${NACOS_AUTH_SYSTEM_TYPE:nacos}
### worked when nacos.core.auth.system.type=nacos
### The token expiration in seconds:
nacos.core.auth.plugin.nacos.token.expire.seconds=${NACOS_AUTH_TOKEN_EXPIRE_SECONDS:18000}
### The default token:
nacos.core.auth.plugin.nacos.token.secret.key=${NACOS_AUTH_TOKEN:}
### Turn on/off caching of auth information. By turning on this switch, the update of auth information would have a 15 seconds delay.
nacos.core.auth.caching.enabled=${NACOS_AUTH_CACHE_ENABLE:false}
nacos.core.auth.enable.userAgentAuthWhite=${NACOS_AUTH_USER_AGENT_AUTH_WHITE_ENABLE:false}
nacos.core.auth.server.identity.key=${NACOS_AUTH_IDENTITY_KEY:}
nacos.core.auth.server.identity.value=${NACOS_AUTH_IDENTITY_VALUE:}
## spring security config
### turn off security
nacos.security.ignore.urls=${NACOS_SECURITY_IGNORE_URLS:/,/error,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/v1/auth/**,/v1/console/health/**,/actuator/**,/v1/console/server/**}
# metrics for elastic search
management.metrics.export.elastic.enabled=false
management.metrics.export.influx.enabled=false
nacos.naming.distro.taskDispatchThreadCount=10
nacos.naming.distro.taskDispatchPeriod=200
nacos.naming.distro.batchSyncKeyCount=1000
nacos.naming.distro.initDataRatio=0.9
nacos.naming.distro.syncRetryDelay=5000
nacos.naming.data.warmup=true
nacos.console.ui.enabled=true
nacos.core.param.check.enabled=true
nacos.core.auth.enabled=true
nacos.core.auth.server.identity.key=example
nacos.core.auth.server.identity.value=example
nacos.core.auth.plugin.nacos.token.secret.key=VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=
```