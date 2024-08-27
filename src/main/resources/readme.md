# ssh

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc
```

# deploy java

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./deploy
EOF
```

# deploy page

```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./page
EOF
```

java

```shellexport JAVA_HOME=~/program/jdk-17.0.2.jdk/Contents/Home && mvn clean package -DskipTests && scp -P 33333 target/mp-server.jar root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/ && ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./run
EOF
```

page

```shell
yarn industry-java-build && zip -r industry.zip industry/ && scp -r -P 33333 industry.zip root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/static/ && rm -rf industry/ && rm -rf industry.zip && ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/static/
rm -rf industry
unzip industry.zip
EOF
```

windows java

```shell
[Environment]::SetEnvironmentVariable('JAVA_HOME', 'D:\program\Java\jdk17.0.6', 'Process')
mvn clean package -DskipTests
scp -P 33333 target/mp-server.jar root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc
cd /opt/douson/mp-server/
./run
```

windows page

```shell
npm run industry-java-build
scp -r -P 33333 industry/ root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/static/
rm -rf industry/
```
