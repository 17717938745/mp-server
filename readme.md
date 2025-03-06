# 本地开发

### 打包

```shell
mvn clean package -DskipTests
```

### 启动java

需要配置Environment variables

```text
--spring.config.location=./application-prd.yml
```

### 外网启动

```text
java -Xms256m -Xmx2048m -XX:NewSize=64m -XX:MaxNewSize=64m -Dfile.encoding=utf-8 -Ddubbo.application.logger=slf4j -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=pch -jar ${app_jar_name} --spring.config.location=./application-pch.yml
```

# 发布帮助

### 参考命令

```shell
# ssh
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc
# ssh(natapp)
./ssh
# https(natapp)
./ssl
# 蒲公英
./sun
```

### deploy java & page

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./deploy all
EOF
```

### deploy java

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./deploy
EOF
```

### deploy page

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./page
EOF
```

### 查看定时任务

```shell
crontab -l
```

# mysql

### 设置时区

```shell
SET GLOBAL time_zone = '+08:00';
SET time_zone = '+08:00';
```

### 设置字符集

```shell
ALTER DATABASE dmmp CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
ALTER DATABASE douson CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
```

### my.cnf配置

```text
[mysqld]
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock
symbolic-links=0
log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
bind-address=0.0.0.0
lower_case_table_names=1
max_allowed_packet=104857600
init_connect='SET NAMES utf8mb4'
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci

[client]
default-character-set = utf8mb4

[mysql]
default-character-set = utf8mb4
```