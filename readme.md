# 参考命令

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

# deploy java

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./deploy
EOF
```

# deploy java & page

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./deploy all
EOF
```

# deploy page

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./page
EOF
```
