# 本地开发

### 启动java

需要配置Environment variables

```text
--spring.config.location=./application-prd.yml
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