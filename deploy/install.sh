#!/bin/bash
# ============================================================
# JNPF 一键部署脚本(Linux 麒麟V10 / macOS)
# 前置:已运行 docker/nacos-config-tools/setup.sh 完成数据库配置
# 流程:导入全部镜像 -> 启动 Nacos -> 导入业务配置 -> 启动全部微服务
# ============================================================
set -e
cd "$(dirname "$0")"

echo "========================================="
echo " [步骤1/4] 导入全部服务镜像(约 5.6GB,首次需几分钟)"
echo "========================================="
find . -name "*.tar" ! -name "eclipse-temurin*" -exec docker load -i {} \;

echo ""
echo "========================================="
echo " [步骤2/4] 启动 Nacos ..."
echo "========================================="
cd docker
docker compose up -d nacos-server
echo "等待 Nacos 就绪(最长 5 分钟)..."
READY=0
for i in $(seq 1 60); do
    if curl -s -o /dev/null --connect-timeout 2 http://127.0.0.1:30099/nacos/; then
        READY=1
        echo "[成功] Nacos 已就绪: http://127.0.0.1:30099/nacos"
        break
    fi
    sleep 5
done
if [ "$READY" != "1" ]; then
    echo "[警告] Nacos 未就绪,请排查: docker logs nacos-server"
    echo "        常见原因:nacos-conf/application.properties 中数据库连接不正确"
fi

echo ""
echo "========================================="
echo " [步骤3/4] 导入业务配置到 Nacos ..."
echo "========================================="
if [ "$READY" = "1" ]; then
    ./nacos-config-tools/config-import.sh || \
        echo "[警告] 存在导入失败项,请重新执行: ./nacos-config-tools/config-import.sh"
else
    echo "[跳过] Nacos 未就绪;就绪后手动执行: ./nacos-config-tools/config-import.sh"
fi

echo ""
echo "========================================="
echo " [步骤4/4] 启动全部微服务 ..."
echo "========================================="
docker compose up -d
sleep 5
docker compose ps

echo ""
echo "========================================="
echo " 部署命令执行完毕!"
echo "   Nacos 控制台: http://127.0.0.1:30099/nacos (账号 nacos/nacos)"
echo "   网关地址:     http://127.0.0.1:30000"
echo "   服务完全就绪需 1~3 分钟,检查状态: docker compose ps"
echo "   (可选)工作流管理端: docker compose --profile workflow up -d workflow"
echo "=============================================="
