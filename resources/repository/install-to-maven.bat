@echo off
rem 将 resources/repository 下的离线 jar 安装到本地 Maven 仓库（%USERPROFILE%\.m2）
rem 用法：在 resources/repository 目录下双击或执行  install-to-maven.bat
setlocal

echo 安装 DmJdbcDriver18-1.0.jar - dm.jdbc:DmJdbcDriver18:1.0
call mvn -q install:install-file -Dfile=DmJdbcDriver18-1.0.jar -DgroupId=dm.jdbc -DartifactId=DmJdbcDriver18 -Dversion=1.0 -Dpackaging=jar
if errorlevel 1 goto :err

echo 安装 DmJdbcDriver18-1.0.jar - com.dameng:DmJdbcDriver18:1.0
call mvn -q install:install-file -Dfile=DmJdbcDriver18-1.0.jar -DgroupId=com.dameng -DartifactId=DmJdbcDriver18 -Dversion=1.0 -Dpackaging=jar
if errorlevel 1 goto :err

echo 安装 springboot-sdk-log-0.0.1-SNAPSHOT.jar
call mvn -q install:install-file -Dfile=springboot-sdk-log-0.0.1-SNAPSHOT.jar -DgroupId=com.huabo.sdk.log -DartifactId=springboot-sdk-log -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
if errorlevel 1 goto :err

echo 安装 springboot-sdk-billing-0.0.1-SNAPSHOT.jar
call mvn -q install:install-file -Dfile=springboot-sdk-billing-0.0.1-SNAPSHOT.jar -DgroupId=com.huabo.sdk.billing -DartifactId=springboot-sdk-billing -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
if errorlevel 1 goto :err

echo 安装 spire.doc.free-3.9.0.jar
call mvn -q install:install-file -Dfile=spire.doc.free-3.9.0.jar -DgroupId=e-iceblue -DartifactId=spire.doc.free -Dversion=3.9.0 -Dpackaging=jar
if errorlevel 1 goto :err

echo 安装 aspose-words-14.9.0-jdk16.jar（坐标版本 15.12.0，与各模块 pom 声明一致）
call mvn -q install:install-file -Dfile=aspose-words-14.9.0-jdk16.jar -DgroupId=com.aspose.words -DartifactId=aspose-words -Dversion=15.12.0 -Dpackaging=jar
if errorlevel 1 goto :err

echo 安装 LabelOperator2033_20220429_bytes.jar
call mvn -q install:install-file -Dfile=LabelOperator2033_20220429_bytes.jar -DgroupId=com.hbyun -DartifactId=zhlabel-operator -Dversion=2033-20220429 -Dpackaging=jar
if errorlevel 1 goto :err

echo.
echo 全部离线 jar 安装完成，可以开始构建各模块。
goto :eof

:err
echo.
echo [错误] 安装失败，请确认已安装 Maven 并在 resources/repository 目录下执行本脚本。
pause
