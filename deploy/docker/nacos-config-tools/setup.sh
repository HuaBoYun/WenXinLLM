#!/bin/bash
# ============================================================
# JNPF 一键初始化脚本(Linux 麒麟V10 / macOS)
# 按提示依次配置(均可回车采用默认值):
#   1) Nacos 自身数据库       -> 写入 ../nacos-conf/application.properties
#   2) 工作流管理端数据库     -> 写入 ../../application-dev.yml
#   3) 业务库/Redis(可选)    -> 写入 nacos-config/.../datasource.yaml
#   4) (可选)立即导入配置到 Nacos
# 完成后会提示启动镜像的命令
# 用法: ./setup.sh
# ============================================================
set -u
cd "$(dirname "$0")"
TOOLSDIR="$(pwd)"
NACOS_PROPS="$TOOLSDIR/../nacos-conf/application.properties"
WF_DEV_YML="$TOOLSDIR/../../application-dev.yml"
DS_YAML="$(ls -d "$TOOLSDIR"/nacos-config/*/DEFAULT_GROUP/datasource.yaml 2>/dev/null | head -1)"

[ -f "$NACOS_PROPS" ] || { echo "[错误] 未找到 $NACOS_PROPS"; exit 1; }
[ -f "$WF_DEV_YML" ]  || { echo "[错误] 未找到 $WF_DEV_YML"; exit 1; }

# ---------- 读取 config.properties 中的 Nacos 地址(用于最后导入配置) ----------
NACOS_SERVER="http://127.0.0.1:30099"
while IFS='=' read -r k v; do
  k=$(echo "$k" | tr -d ' \r\t'); v=$(echo "$v" | sed 's/[[:space:]]*$//; s/\r$//')
  [ "$k" = "NACOS_SERVER" ] && NACOS_SERVER="$v"
done < "$TOOLSDIR/config.properties"

# ============================================================
# 通用提问函数
# ============================================================
ask() { # $1提示语 $2默认值 -> 回显 ANSWER
  local tip="$1" def="${2:-}" in=""
  if [ -n "$def" ]; then printf "%s [默认: %s]: " "$tip" "$def"; else printf "%s: " "$tip"; fi
  read -r in
  ANSWER="${in:-$def}"
}
ask_pw() { # $1提示语 $2默认值
  local tip="$1" def="${2:-}" in=""
  if [ -n "$def" ]; then printf "%s [直接回车=保留默认]: " "$tip"; read -rs in; echo; ANSWER="${in:-$def}"
  else printf "%s(输入不回显): " "$tip"; read -rs in; echo; ANSWER="$in"; fi
}
yaml_escape() { printf '%s' "$1" | sed 's/\\/\\\\/g; s/"/\\"/g'; }

# 数据库类型选择: 1达梦 2金仓 3MySQL 4Oracle 5SQLServer 6PostgreSQL
choose_db() {
  echo "  1) 达梦 DM        2) 人大金仓 KingbaseES   3) MySQL"
  echo "  4) Oracle         5) SQLServer             6) PostgreSQL"
  ask "  请选择数据库类型" "${1:-1}"
  case "$ANSWER" in
    1|2|3|4|5|6) DB_CHOICE="$ANSWER" ;;
    *) echo "  输入无效,请重新选择"; choose_db "$1" ;;
  esac
}
DB_PORT_DEFAULT() { case "$1" in 1) echo 5236;; 2) echo 54321;; 3) echo 3306;; 4) echo 1521;; 5) echo 1433;; 6) echo 5432;; esac; }

echo "=============================================="
echo " JNPF 微服务一键初始化"
echo "=============================================="

# ============================================================
# 第 1 步:Nacos 自身数据库
# ============================================================
echo ""
echo "-------- [1/4] 配置 Nacos 自身数据库(存配置的库 jnpf_nacos) --------"
choose_db 3
NC_DB="$DB_CHOICE"
ask "  数据库地址" "127.0.0.1"
NC_HOST="$ANSWER"
ask "  数据库端口" "$(DB_PORT_DEFAULT "$NC_DB")"
NC_PORT="$ANSWER"
ask "  库名/模式名" "jnpf_nacos"
NC_NAME="$ANSWER"
ask "  用户名" "jnpf_nacos"
NC_USER="$ANSWER"
ask_pw "  密码" ""
# properties 格式中反斜杠是转义符,需双写
NC_PASS=$(printf '%s' "$ANSWER" | sed 's/\\/\\\\/g')

NC_CONV=""; NC_TEST="select 1"; NC_URL=""
case "$NC_DB" in
  1) NC_CONV="dm";       NC_URL="jdbc:dm://${NC_HOST}:${NC_PORT}/${NC_NAME}" ;;
  2) NC_CONV="kingbase"; NC_URL="jdbc:kingbase8://${NC_HOST}:${NC_PORT}/${NC_NAME}" ;;
  3) NC_CONV="mysql";    NC_URL="jdbc:mysql://${NC_HOST}:${NC_PORT}/${NC_NAME}?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8" ;;
  4) NC_CONV="oracle";   NC_TEST="select 1 from dual"; NC_URL="jdbc:oracle:thin:@${NC_HOST}:${NC_PORT}:${NC_NAME}" ;;
  5) NC_CONV="sqlserver";NC_URL="jdbc:sqlserver://${NC_HOST}:${NC_PORT};databaseName=${NC_NAME};trustServerCertificate=true" ;;
  6) NC_CONV="postgre";  NC_URL="jdbc:postgresql://${NC_HOST}:${NC_PORT}/${NC_NAME}?currentSchema=public" ;;
esac

case "$NC_DB" in
  1) NC_DB_NAME="达梦DM";; 2) NC_DB_NAME="金仓KingbaseES";; 3) NC_DB_NAME="MySQL";; 4) NC_DB_NAME="Oracle";; 5) NC_DB_NAME="SQLServer";; 6) NC_DB_NAME="PostgreSQL";;
esac

SEC_FILE=$(mktemp); PROP_TMP=$(mktemp)
{
  echo "# ------------------------------------------------------------------------------------------"
  echo "# ★ 数据库类型切换:当前启用 [${NC_DB_NAME}]"
  echo "#   如需更换数据库,重新运行 setup.sh 或按下方模板手动切换(只保留一块未注释)"
  echo "# ------------------------------------------------------------------------------------------"
  echo ""
  echo "# ============================【国产化推荐】达梦 DM ============================"
  if [ "$NC_DB" = "1" ]; then
    echo "spring.datasource.convert=$NC_CONV"
    echo "db.pool.config.connectionTestQuery=$NC_TEST"
    echo "db.url.0=$NC_URL"
    echo "db.user=$NC_USER"
    echo "db.password=$NC_PASS"
  else
    echo "#spring.datasource.convert=dm"
    echo "#db.pool.config.connectionTestQuery=select 1"
    echo "#db.url.0=jdbc:dm://数据库IP:5236/jnpf_nacos"
    echo "#db.user=JNPF_NACOS"
    echo "#db.password=修改为实际密码"
  fi
  echo ""
  echo "# ============================【国产化推荐】人大金仓 KingbaseES ============================"
  if [ "$NC_DB" = "2" ]; then
    echo "spring.datasource.convert=$NC_CONV"
    echo "db.pool.config.connectionTestQuery=$NC_TEST"
    echo "db.url.0=$NC_URL"
    echo "db.user=$NC_USER"
    echo "db.password=$NC_PASS"
  else
    echo "#spring.datasource.convert=kingbase"
    echo "#db.pool.config.connectionTestQuery=select 1"
    echo "#db.url.0=jdbc:kingbase8://数据库IP:54321/jnpf_nacos"
    echo "#db.user=system"
    echo "#db.password=修改为实际密码"
  fi
  echo ""
  echo "# ================================== MySQL =========================================="
  if [ "$NC_DB" = "3" ]; then
    echo "spring.datasource.convert=$NC_CONV"
    echo "db.pool.config.connectionTestQuery=$NC_TEST"
    echo "db.url.0=$NC_URL"
    echo "db.user=$NC_USER"
    echo "db.password=$NC_PASS"
  else
    echo "#spring.datasource.convert=mysql"
    echo "#db.pool.config.connectionTestQuery=select 1"
    echo "#db.url.0=jdbc:mysql://数据库IP:3306/jnpf_nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8"
    echo "#db.user=jnpf_nacos"
    echo "#db.password=修改为实际密码"
  fi
  echo ""
  echo "# ================================== Oracle =========================================="
  if [ "$NC_DB" = "4" ]; then
    echo "spring.datasource.convert=$NC_CONV"
    echo "db.pool.config.connectionTestQuery=$NC_TEST"
    echo "db.url.0=$NC_URL"
    echo "db.user=$NC_USER"
    echo "db.password=$NC_PASS"
  else
    echo "#spring.datasource.convert=oracle"
    echo "#db.pool.config.connectionTestQuery=select 1 from dual"
    echo "#db.url.0=jdbc:oracle:thin:@数据库IP:1521:orcl"
    echo "#db.user=JNPF_NACOS"
    echo "#db.password=修改为实际密码"
  fi
  echo ""
  echo "# ================================== SQLServer =========================================="
  if [ "$NC_DB" = "5" ]; then
    echo "spring.datasource.convert=$NC_CONV"
    echo "db.pool.config.connectionTestQuery=$NC_TEST"
    echo "db.url.0=$NC_URL"
    echo "db.user=$NC_USER"
    echo "db.password=$NC_PASS"
  else
    echo "#spring.datasource.convert=sqlserver"
    echo "#db.pool.config.connectionTestQuery=select 1"
    echo "#db.url.0=jdbc:sqlserver://数据库IP:1433;databaseName=jnpf_nacos;trustServerCertificate=true"
    echo "#db.user=sa"
    echo "#db.password=修改为实际密码"
  fi
  echo ""
  echo "# ================================== PostgreSQL =========================================="
  if [ "$NC_DB" = "6" ]; then
    echo "spring.datasource.convert=$NC_CONV"
    echo "db.pool.config.connectionTestQuery=$NC_TEST"
    echo "db.url.0=$NC_URL"
    echo "db.user=$NC_USER"
    echo "db.password=$NC_PASS"
  else
    echo "#spring.datasource.convert=postgre"
    echo "#db.pool.config.connectionTestQuery=select 1"
    echo "#db.url.0=jdbc:postgresql://数据库IP:5432/jnpf_nacos?currentSchema=public"
    echo "#db.user=dbuser"
    echo "#db.password=修改为实际密码"
  fi
} > "$SEC_FILE"

grep -q "DB-SWITCH-START" "$NACOS_PROPS" || { echo "[错误] application.properties 缺少改写标记行"; rm -f "$SEC_FILE" "$PROP_TMP"; exit 1; }
awk -v sec="$SEC_FILE" '
  /^# >>> DB-SWITCH-START/ {print; while ((getline l < sec) > 0) print l; skip=1; next}
  /^# <<< DB-SWITCH-END/   {skip=0}
  !skip {print}
' "$NACOS_PROPS" > "$PROP_TMP" && mv "$PROP_TMP" "$NACOS_PROPS"
rm -f "$SEC_FILE"
echo "  [完成] 已写入 Nacos 数据库配置 -> nacos-conf/application.properties"

# ============================================================
# 第 2 步:工作流管理端(workflow)数据库
# ============================================================
echo ""
echo "-------- [2/4] 配置工作流管理端(workflow)数据库 --------"
choose_db 3
WF_DB="$DB_CHOICE"
WF_PORT_DEF="$(DB_PORT_DEFAULT "$WF_DB")"; [ "$WF_DB" = "2" ] && WF_PORT_DEF=54321
ask "  数据库地址" "127.0.0.1"
WF_HOST="$ANSWER"
ask "  数据库端口" "$WF_PORT_DEF"
WF_PORT="$ANSWER"
ask "  库名" "jnpf_flow"
WF_NAME="$ANSWER"
ask "  用户名" "root"
WF_USER="$ANSWER"
ask_pw "  密码" ""
WF_PASS=$(yaml_escape "$ANSWER")

# 模式名:达梦/Oracle 必填;PG/金仓默认 public;MySQL/SQLServer 不需要
WF_SCHEMA=""
case "$WF_DB" in
  1|4) ask "  模式名(必填)" "$WF_NAME"; WF_SCHEMA="$ANSWER" ;;
  2|6) ask "  模式名(回车=public)" "public"; WF_SCHEMA="$ANSWER" ;;
esac

case "$WF_DB" in
  1) WF_DRIVER="dm.jdbc.driver.DmDriver";            WF_TYPE="dm.jdbc.driver.DmdbDataSource"
     WF_URL="jdbc:dm://${WF_HOST}:${WF_PORT}/${WF_NAME}?compatibleMode=oracle"
     [ -n "$WF_SCHEMA" ] && WF_SCHEMA_YML="$WF_SCHEMA" || WF_SCHEMA_YML=""
     WF_FTYPE="dm" ;;
  2) WF_DRIVER="org.postgresql.Driver";              WF_TYPE="org.postgresql.ds.PGSimpleDataSource"
     WF_URL="jdbc:postgresql://${WF_HOST}:${WF_PORT}/${WF_NAME}?currentSchema=${WF_SCHEMA:-public}"
     WF_SCHEMA_YML="${WF_SCHEMA:-}"; WF_FTYPE="postgres" ;;
  3) WF_DRIVER="com.mysql.cj.jdbc.Driver";           WF_TYPE="com.mysql.cj.jdbc.MysqlDataSource"
     WF_URL="jdbc:mysql://${WF_HOST}:${WF_PORT}/${WF_NAME}?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true&nullCatalogMeansCurrent=true&useSSL=false&allowPublicKeyRetrieval=true"
     WF_SCHEMA_YML=""; WF_FTYPE="mysql" ;;
  4) WF_DRIVER="oracle.jdbc.OracleDriver";           WF_TYPE="oracle.jdbc.datasource.impl.OracleDataSource"
     WF_URL="jdbc:oracle:thin:@${WF_HOST}:${WF_PORT}:${WF_NAME}"
     WF_SCHEMA_YML="$WF_SCHEMA"; WF_FTYPE="oracle" ;;
  5) WF_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver"; WF_TYPE="com.microsoft.sqlserver.jdbc.SQLServerDataSource"
     WF_URL="jdbc:sqlserver://${WF_HOST}:${WF_PORT};databaseName=${WF_NAME};trustServerCertificate=true"
     WF_SCHEMA_YML=""; WF_FTYPE="mssql" ;;
  6) WF_DRIVER="org.postgresql.Driver";              WF_TYPE="org.postgresql.ds.PGSimpleDataSource"
     WF_URL="jdbc:postgresql://${WF_HOST}:${WF_PORT}/${WF_NAME}"
     WF_SCHEMA_YML="${WF_SCHEMA:-}"; WF_FTYPE="postgres" ;;
esac

cat > "$WF_DEV_YML" <<EOF
# ============================================================
# JNPF 工作流管理端(inpf-workflow)配置
# 由 setup 脚本生成,可手动修改;修改后重启容器生效:
#   docker compose --profile workflow up -d workflow
# 该文件会挂载进 workflow 容器,优先级高于 jar 内配置
# ============================================================
server:
  port: 31000

logging:
  level:
    root: info
    org.flowable.engine.impl.persistence.entity.*: debug
    org.flowable.task.service.impl.persistence.entity.*: debug

# 模式名:达梦/Oracle 必填;PostgreSQL/金仓默认 public(留空即可);MySQL/SQLServer 无需配置
flowable:
  database-schema: "${WF_SCHEMA_YML}"
  database-type: ${WF_FTYPE}

spring:
  datasource:
    driver-class-name: ${WF_DRIVER}
    type: ${WF_TYPE}
    url: ${WF_URL}
    username: ${WF_USER}
    password: "${WF_PASS}"
EOF
echo "  [完成] 已写入 workflow 配置 -> application-dev.yml(deploy 根目录)"

# ============================================================
# 第 3 步(可选):业务库 / Redis -> datasource.yaml
# ============================================================
echo ""
echo "-------- [3/4] 配置业务库与 Redis(写入 nacos 配置文件 datasource.yaml) --------"
if [ -z "$DS_YAML" ]; then
  echo "  [跳过] 未找到 datasource.yaml"
else
  ask "  是否配置业务库?(y=配置,回车=跳过)" ""
  if [ "$ANSWER" = "y" ] || [ "$ANSWER" = "Y" ]; then
    choose_db 3
    BS_DB="$DB_CHOICE"
    case "$BS_DB" in
      1) BS_TYPE="DM";; 2) BS_TYPE="KingbaseES";; 3) BS_TYPE="MySQL";; 4) BS_TYPE="Oracle";; 5) BS_TYPE="SQLServer";; 6) BS_TYPE="PostgreSQL";;
    esac
    ask "  业务库地址" "127.0.0.1"; BS_HOST="$ANSWER"
    ask "  业务库端口" "$(DB_PORT_DEFAULT "$BS_DB")"; BS_PORT="$ANSWER"
    ask "  业务库名" "jnpf_init"; BS_NAME="$ANSWER"
    ask "  用户名" "root"; BS_USER="$ANSWER"
    ask_pw "  密码" ""; BS_PASS=$(yaml_escape "$ANSWER")
    TYPE_ENV="$BS_TYPE" HOST_ENV="$BS_HOST" PORT_ENV="$BS_PORT" NAME_ENV="$BS_NAME" USER_ENV="$BS_USER" PW_ENV="$BS_PASS" \
    awk '
      /^[ ]{2}[^ ]/ { cur=$0; sub(/[[:space:]]*:.*/, "", cur); gsub(/^[ ]+/, "", cur) }
      cur=="datasource" && /^[ ]+db-type:/   { sub(/:.*/, ": " ENVIRON["TYPE_ENV"]); print; next }
      cur=="datasource" && /^[ ]+db-name:/   { sub(/:.*/, ": " ENVIRON["NAME_ENV"]); print; next }
      cur=="datasource" && /^[ ]+host:/      { sub(/:.*/, ": " ENVIRON["HOST_ENV"]); print; next }
      cur=="datasource" && /^[ ]+port:/      { sub(/:.*/, ": " ENVIRON["PORT_ENV"]); print; next }
      cur=="datasource" && /^[ ]+username:/  { sub(/:.*/, ": " ENVIRON["USER_ENV"]); print; next }
      cur=="datasource" && /^[ ]+password:/  { sub(/:.*/, ": \"" ENVIRON["PW_ENV"] "\""); print; next }
      { print }
    ' "$DS_YAML" > "${DS_YAML}.tmp" && mv "${DS_YAML}.tmp" "$DS_YAML"
    echo "  [完成] 业务库已写入 datasource.yaml"
  fi

  ask "  是否配置 Redis?(y=配置,回车=跳过)" ""
  if [ "$ANSWER" = "y" ] || [ "$ANSWER" = "Y" ]; then
    ask "  Redis 地址" "127.0.0.1"; RD_HOST="$ANSWER"
    ask "  Redis 端口" "6379"; RD_PORT="$ANSWER"
    ask_pw "  Redis 密码(无密码直接回车)" ""; RD_PASS=$(yaml_escape "$ANSWER")
    HOST_ENV="$RD_HOST" PORT_ENV="$RD_PORT" PW_ENV="$RD_PASS" \
    awk '
      /^[ ]{2}[^ ]/ { cur=$0; sub(/[[:space:]]*:.*/, "", cur); gsub(/^[ ]+/, "", cur) }
      cur=="redis" && /^[ ]+host:/ { sub(/:.*/, ": " ENVIRON["HOST_ENV"]); print; next }
      cur=="redis" && /^[ ]+port:/ { sub(/:.*/, ": " ENVIRON["PORT_ENV"]); print; next }
      cur=="redis" && /^[ ]*#[ ]*password:/ {
        if (ENVIRON["PW_ENV"] != "") { sub(/#.*/, "password: \"" ENVIRON["PW_ENV"] "\"") }
        print; next }
      { print }
    ' "$DS_YAML" > "${DS_YAML}.tmp" && mv "${DS_YAML}.tmp" "$DS_YAML"
    echo "  [完成] Redis 已写入 datasource.yaml"
  fi
fi

# ============================================================
# 第 4 步(可选):立即导入配置到 Nacos
# ============================================================
echo ""
echo "-------- [4/4] 发布业务配置到 Nacos --------"
HTTP_CODE=$(curl -s -o /dev/null -w '%{http_code}' --connect-timeout 3 "${NACOS_SERVER}/nacos/" 2>/dev/null)
if [ "$HTTP_CODE" = "000" ]; then
  echo "  [提示] Nacos(${NACOS_SERVER})暂未启动,跳过导入"
  IMPORTED="no"
else
  ask "  Nacos 已就绪,是否立即导入业务配置?(Y=导入,回车=跳过)" "Y"
  if [ "$ANSWER" != "n" ] && [ "$ANSWER" != "N" ]; then
    chmod +x "$TOOLSDIR/config-import.sh"
    "$TOOLSDIR/config-import.sh" || true
    IMPORTED="yes"
  else
    IMPORTED="no"
  fi
fi

# ============================================================
# 完成提示
# ============================================================
echo ""
echo "=============================================="
echo " ✅ 初始化配置全部完成!"
echo "----------------------------------------------"
echo " 已配置:"
echo "   1) Nacos 自身数据库   -> docker/nacos-conf/application.properties"
echo "   2) workflow 数据库    -> application-dev.yml(deploy 根目录)"
[ -n "${BS_HOST:-}" ] && echo "   3) 业务库/Redis       -> datasource.yaml(Nacos 配置文件)"
echo ""
echo " 现在可以启动镜像了,请按顺序执行:"
echo "   cd docker"
echo "   docker compose up -d nacos-server      # 1.先启动 nacos"
if [ "${IMPORTED:-no}" != "yes" ]; then
  echo "   cd nacos-config-tools && ./config-import.sh   # 2.导入业务配置(nacos 启动后执行一次)"
fi
echo "   docker compose up -d                   # 3.启动全部微服务"
echo "   (可选)docker compose --profile workflow up -d workflow   # 工作流管理端"
echo ""
echo " 验证: nacos 控制台 http://127.0.0.1:30099/nacos (nacos/nacos)"
echo "=============================================="
