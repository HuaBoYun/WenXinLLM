#!/usr/bin/env bash
# 将 resources/repository 下的离线 jar 安装到本地 Maven 仓库（~/.m2）
# 用法：在 resources/repository 目录下执行  bash install-to-maven.sh
# 前提：已安装 Maven 3.6+ 和 JDK 1.8
set -e
MVN=${MVN:-mvn}

install_jar () { # $1=文件名 $2=groupId $3=artifactId $4=version
  echo "安装 $1 -> $2:$3:$4"
  "$MVN" -q install:install-file -Dfile="$1" -DgroupId="$2" -DartifactId="$3" -Dversion="$4" -Dpackaging=jar
}

install_jar DmJdbcDriver18-1.0.jar                          dm.jdbc            DmJdbcDriver18        1.0
install_jar DmJdbcDriver18-1.0.jar                          com.dameng         DmJdbcDriver18        1.0
install_jar springboot-sdk-log-0.0.1-SNAPSHOT.jar           com.huabo.sdk.log  springboot-sdk-log    0.0.1-SNAPSHOT
install_jar springboot-sdk-billing-0.0.1-SNAPSHOT.jar       com.huabo.sdk.billing springboot-sdk-billing 0.0.1-SNAPSHOT
install_jar spire.doc.free-3.9.0.jar                        e-iceblue          spire.doc.free        3.9.0
install_jar aspose-words-14.9.0-jdk16.jar                   com.aspose.words   aspose-words          15.12.0
install_jar LabelOperator2033_20220429_bytes.jar            com.hbyun          zhlabel-operator      2033-20220429

echo ""
echo "全部离线 jar 安装完成，可以开始构建各模块。"
