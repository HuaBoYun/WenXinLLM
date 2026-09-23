#!/bin/bash
# ============================================================
# Nacos 配置导入脚本(Linux 麒麟V10 / macOS 通用)
# 将 nacos-config/ 目录下的所有配置文件发布到 Nacos
# 用法: ./config-import.sh
# ============================================================
set -u
cd "$(dirname "$0")"

CONF_FILE="config.properties"
CONFIG_DIR="nacos-config"

# ---------- 读取配置文件(兼容 Windows 换行符) ----------
[ -f "$CONF_FILE" ] || { echo "[错误] 未找到 ${CONF_FILE}"; exit 1; }
while IFS='=' read -r k v; do
  k=$(echo "$k" | tr -d ' \r\t')
  v=$(echo "$v" | sed 's/[[:space:]]*$//; s/\r$//')
  case "$k" in
    ''|\#*) continue ;;
    NACOS_SERVER)          NACOS_SERVER="$v" ;;
    NACOS_USERNAME)        NACOS_USERNAME="$v" ;;
    NACOS_PASSWORD)        NACOS_PASSWORD="$v" ;;
    NACOS_NAMESPACE_ID)    NACOS_NAMESPACE_ID="$v" ;;
    NACOS_NAMESPACE_NAME)  NACOS_NAMESPACE_NAME="$v" ;;
    NACOS_IDENTITY_KEY)    NACOS_IDENTITY_KEY="$v" ;;
    NACOS_IDENTITY_VALUE)  NACOS_IDENTITY_VALUE="$v" ;;
  esac
done < "$CONF_FILE"
: "${NACOS_SERVER:=http://127.0.0.1:30099}"

echo "=============================================="
echo " Nacos 配置导入"
echo " 目标: ${NACOS_SERVER}"
echo " 命名空间: ${NACOS_NAMESPACE_ID}"
echo "=============================================="

# ---------- 检查 Nacos 是否可达 ----------
echo "[1/4] 检查 Nacos 服务..."
HTTP_CODE=$(curl -s -o /dev/null -w '%{http_code}' --connect-timeout 5 "${NACOS_SERVER}/nacos/" 2>/dev/null)
if [ "$HTTP_CODE" = "000" ]; then
  echo "[错误] 无法连接 ${NACOS_SERVER},请确认:"
  echo "       1) nacos 容器已启动(docker ps 查看)"
  echo "       2) config.properties 中 NACOS_SERVER 地址/端口正确"
  exit 1
fi

# ---------- 登录获取 accessToken(失败则改用服务端鉴权头) ----------
echo "[2/4] 登录 Nacos..."
LOGIN_RESP=$(curl -s -X POST "${NACOS_SERVER}/nacos/v1/auth/users/login" \
  --data-urlencode "username=${NACOS_USERNAME}" \
  --data-urlencode "password=${NACOS_PASSWORD}")
TOKEN=$(echo "$LOGIN_RESP" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
  echo "       登录成功(账号 ${NACOS_USERNAME})"
  AUTH=(-d "accessToken=${TOKEN}")
else
  echo "       账号登录失败,改用服务端鉴权头(identity)方式"
  AUTH=(-H "${NACOS_IDENTITY_KEY}: ${NACOS_IDENTITY_VALUE}")
fi

# ---------- 确保命名空间存在 ----------
echo "[3/4] 检查命名空间..."
NS_RESP=$(curl -s -G "${NACOS_SERVER}/nacos/v1/console/namespaces" "${AUTH[@]}")
if echo "$NS_RESP" | grep -q "$NACOS_NAMESPACE_ID"; then
  echo "       命名空间已存在"
else
  echo "       命名空间不存在,自动创建..."
  CREATE_RESP=$(curl -s -X POST "${NACOS_SERVER}/nacos/v1/console/namespaces" "${AUTH[@]}" \
    --data-urlencode "customNamespaceId=${NACOS_NAMESPACE_ID}" \
    --data-urlencode "namespaceName=${NACOS_NAMESPACE_NAME}" \
    --data-urlencode "namespaceDesc=JNPF platform namespace")
  echo "       创建结果: ${CREATE_RESP:-已完成(请核对)}"
fi

# ---------- 遍历目录,逐个发布 ----------
echo "[4/4] 开始导入配置..."
FAIL=0

for tenant_dir in "${CONFIG_DIR}"/*; do
  [ -d "$tenant_dir" ] || continue
  ns_id=$(basename "$tenant_dir")
  if [ "$ns_id" = "public" ]; then TENANT=""; else TENANT="$ns_id"; fi

  while IFS= read -r f; do
    group=$(basename "$(dirname "$f")")
    data_id=$(basename "$f")
    case "$data_id" in
      *.yaml|*.yml)  ctype="yaml" ;;
      *.properties)  ctype="properties" ;;
      *.json)        ctype="json" ;;
      *)             ctype="text" ;;
    esac

    RESP=$(curl -s -X POST "${NACOS_SERVER}/nacos/v1/cs/configs" "${AUTH[@]}" \
      --data-urlencode "dataId=${data_id}" \
      --data-urlencode "group=${group}" \
      --data-urlencode "tenant=${TENANT}" \
      --data-urlencode "type=${ctype}" \
      --data-urlencode "content@${f}")

    if [ "$RESP" = "true" ]; then
      echo "  [成功] ${ns_id} / ${group} / ${data_id}"
    else
      FAIL=$((FAIL+1))
      echo "  [失败] ${ns_id} / ${group} / ${data_id}  (返回: ${RESP})"
    fi
  done < <(find "$tenant_dir" -type f | sort)
done

echo ""
echo "=============================================="
if [ "$FAIL" = "0" ]; then
  echo " 导入完成:全部成功"
  echo " 建议重启服务使配置生效: docker compose restart"
else
  echo " 导入完成,存在 ${FAIL} 个失败项,请检查上方 [失败] 日志"
fi
echo "=============================================="
