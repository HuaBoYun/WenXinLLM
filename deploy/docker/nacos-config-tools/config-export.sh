#!/bin/bash
# ============================================================
# Nacos 配置导出脚本(Linux 麒麟V10 / macOS 通用)
# 从运行中的 Nacos 拉取全部配置,保存到 nacos-config/ 目录
# 用法: ./config-export.sh
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
    NACOS_IDENTITY_KEY)    NACOS_IDENTITY_KEY="$v" ;;
    NACOS_IDENTITY_VALUE)  NACOS_IDENTITY_VALUE="$v" ;;
  esac
done < "$CONF_FILE"
: "${NACOS_SERVER:=http://127.0.0.1:30099}"

echo "=============================================="
echo " Nacos 配置导出"
echo " 来源: ${NACOS_SERVER}"
echo " 保存目录: ${CONFIG_DIR}/"
echo "=============================================="

# ---------- 检查 Nacos 是否可达 ----------
HTTP_CODE=$(curl -s -o /dev/null -w '%{http_code}' --connect-timeout 5 "${NACOS_SERVER}/nacos/" 2>/dev/null)
if [ "$HTTP_CODE" = "000" ]; then
  echo "[错误] 无法连接 ${NACOS_SERVER},请确认 Nacos 已启动"
  exit 1
fi

# ---------- 登录 ----------
echo "[1/2] 登录 Nacos..."
LOGIN_RESP=$(curl -s -X POST "${NACOS_SERVER}/nacos/v1/auth/users/login" \
  --data-urlencode "username=${NACOS_USERNAME}" \
  --data-urlencode "password=${NACOS_PASSWORD}")
TOKEN=$(echo "$LOGIN_RESP" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
  echo "       登录成功"
  AUTH=(-d "accessToken=${TOKEN}")
else
  echo "       账号登录失败,改用服务端鉴权头(identity)方式"
  AUTH=(-H "${NACOS_IDENTITY_KEY}: ${NACOS_IDENTITY_VALUE}")
fi

# ---------- 拉取 public + 业务命名空间下的全部配置 ----------
echo "[2/2] 拉取配置列表并下载..."
mkdir -p "${CONFIG_DIR}"

export_config_list () {
  tenant="$1"
  store="$2"
  page=1
  while : ; do
    LIST_RESP=$(curl -s -G "${NACOS_SERVER}/nacos/v1/cs/configs" "${AUTH[@]}" \
      --data-urlencode "search=accurate" \
      --data-urlencode "dataId=" \
      --data-urlencode "group=" \
      --data-urlencode "pageNo=${page}" \
      --data-urlencode "pageSize=200" \
      --data-urlencode "tenant=${tenant}")
    # 无数据或翻页结束
    [ -z "$LIST_RESP" ] && break
    echo "$LIST_RESP" | grep -q '"totalCount":0' && break

    # 解析 dataId / group(JSON 中按顺序成对出现),逐个下载
    echo "$LIST_RESP" | grep -oE '"(dataId|group)":"[^"]*"' \
      | sed 's/^[^:]*://; s/"//g' \
      | paste - - \
      | while read -r data_id group; do
          [ -n "$data_id" ] || continue
          dir="${store}/${group}"
          mkdir -p "$dir"
          curl -s -G "${NACOS_SERVER}/nacos/v1/cs/configs" "${AUTH[@]}" \
            --data-urlencode "dataId=${data_id}" \
            --data-urlencode "group=${group}" \
            --data-urlencode "tenant=${tenant}" \
            -o "${dir}/${data_id}"
          echo "  [导出] ${tenant:-public} / ${group} / ${data_id}"
        done

    page=$((page+1))
    [ "$page" -gt 50 ] && break
    echo "$LIST_RESP" | grep -q '"pages":1' && break
  done
}

export_config_list ""                        "${CONFIG_DIR}/public"
export_config_list "${NACOS_NAMESPACE_ID}"   "${CONFIG_DIR}/${NACOS_NAMESPACE_ID}"

echo ""
echo "=============================================="
echo " 导出完成,文件位于: ${CONFIG_DIR}/"
echo " 修改后请运行 ./config-import.sh 写回 Nacos"
echo "=============================================="
