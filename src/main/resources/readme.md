```shell
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc
```

```shell
# export JAVA_HOME=~/program/jdk-17.0.2.jdk/Contents/Home
# export JAVA_HOME=D:\program\Java\jdk17.0.6
# export JAVA_HOME=D:/program/Java/jdk17.0.6
# mvn clean package -DskipTests -pl server/mp-server -am && rm -rf C:/Users/MLoong/Desktop/mp-server/mp-server.jar && cp server/mp-server/target/mp-server.jar C:/Users/MLoong/Desktop/mp-server/
# mvn clean package -DskipTests -pl server/mp-server -am && rm -rf ~/Desktop/mp-server/mp-server.jar && cp server/mp-server/target/mp-server.jar ~/Desktop/mp-server/
export JAVA_HOME=~/program/jdk-17.0.2.jdk/Contents/Home && mvn clean package -DskipTests && scp -P 33333 target/mp-server.jar root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/ && ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./run
EOF
```

windows

```shell
[Environment]::SetEnvironmentVariable('JAVA_HOME', 'D:\program\Java\jdk17.0.6', 'Process')
mvn clean package -DskipTests
scp -P 33333 target/mp-server.jar root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/
ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/
./run
EOF
```

```shell
# yarn industry-douson-build && rm -rf ~/Desktop/mp-server/static/industry/ && mv industry/ ~/Desktop/mp-server/static/
# yarn industry-java-build && rm -rf ~/Desktop/mp-server/static/industry/ && mv industry/ ~/Desktop/mp-server/static/
# npm run industry-java-build && rm -rf C:/Users/MLoong/Desktop/mp-server/industry/ && mv industry/ C:/Users/MLoong/Desktop/mp-server/
# yarn industry-java-build && scp -r -P 33333 industry/ root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/static/ && rm -rf industry/
yarn industry-java-build && zip -r industry.zip industry/ && scp -r -P 33333 industry.zip root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/static/ && rm -rf industry/ && rm -rf industry.zip && ssh -p 33333 root@085a8d1e51b66c57.natapp.cc << EOF
cd /opt/douson/mp-server/static/
rm -rf industry
unzip industry.zip
EOF
```

windows

```shell
npm run industry-java-build
scp -r -P 33333 industry/ root@085a8d1e51b66c57.natapp.cc:/opt/douson/mp-server/static/
rm -rf industry/
```
