# JNPF 微服务 Docker 离线部署指南

本文件夹是**自包含部署包**:包含全部服务镜像、全部配置文件和全部脚本,不需要联网、不依赖其他任何文件。
请从本文档第 0 步开始按顺序操作,直到第五章确认服务完全启动。

> 系统支持:麒麟 V10 等 x86 Linux、Windows、macOS(镜像为 linux/amd64 架构)。

**部署流程总览:**

```
获取本文件夹 → 第1步 环境检查 → 第2步 准备数据库和Redis → 第3步 运行初始化脚本(改配置)
   → 第4步 一键部署(install.sh)→ 第5步 确认服务完全启动
```

---

## 第 0 步:获取部署包

将整个 `deploy` 文件夹完整获取到部署机上(解压、克隆或直接拷贝均可),**保持内部结构不变**。拿到后的结构:

```
deploy/
├── README.md                  # 本文档
├── install.sh                 # 一键部署脚本(Linux/macOS)
├── *.tar                      # 14 个服务镜像(nacos-server、gateway、oauth、system、
│                              #   visualdev、flowable、file、message、scheduletask、
│                              #   permission、visualdata、app、extend、workflow)
├── application.yml            # 工作流管理端 workflow 的配置
├── application-dev.yml        # ★ workflow 的数据库连接(setup 脚本自动生成/可手改)
└── docker/
    ├── docker-compose.yml     # 服务编排
    ├── nacos-conf/application.properties   # ★ Nacos 自身的数据库连接
    └── nacos-config-tools/    # 初始化与配置脚本
        ├── setup.sh / setup.ps1          # ★ 交互式配置(推荐)
        ├── config-import.sh / .ps1       # 业务配置导入 Nacos
        ├── config-export.sh / .ps1       # 从 Nacos 反向导出配置
        ├── config.properties             # 脚本连接参数
        └── nacos-config/                 # 全部业务配置文件(datasource.yaml 等)
```

> 下面命令的执行位置:标注了 `deploy 根目录` 的在 `deploy/` 下执行;标注了 `docker/` 的在 `deploy/docker/` 下执行。Windows 用户请使用 PowerShell。

---

## 第 1 步:检查部署环境

在部署机上逐项确认:

| 检查项 | 要求 | 检查命令 |
|---|---|---|
| Docker | 20.10 及以上,且已启动 | `docker version` |
| 磁盘 | 剩余 ≥ 20GB | `df -h` |
| 内存 | ≥ 8GB | `free -h`(macOS/Win 看系统信息) |
| 端口 | 30000、30001~30012、30019、30099 未被占用;如需工作流管理端再加 31000 | `ss -tlnp` 或 `netstat -an` |

麒麟 V10 若没有 `docker compose` 命令,请安装 compose 插件或使用 `docker-compose`(把文档中的 `docker compose` 替换为 `docker-compose`)。

---

## 第 2 步:准备数据库和 Redis

部署需要提前准备好(数据库类型支持 **达梦DM / 人大金仓KingbaseES / MySQL / Oracle / SQLServer / PostgreSQL**,国产化环境推荐前两种):

1. **Nacos 专用库:存放全部微服务配置。用安装包中**对应数据库类型**的 Nacos 初始化脚本建表;
   - 该库必须包含登录账号 `nacos/nacos`。若你是用 Nacos 原生脚本新建的库,需补两条 SQL 并重启 nacos:

     ```sql
     INSERT INTO users(username,password,enabled)
       VALUES('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',TRUE);
     INSERT INTO roles(username,role) VALUES('nacos','ADMIN');
     ```

2. **业务库**:执行业务初始化 SQL;
3. **Redis** 一个(业务缓存必须)。

准备好各库的 **地址、端口、库名、账号、密码**,下一步要用。

---

## 第 3 步:运行初始化脚本(完成全部配置)

### Linux(麒麟 V10)/ macOS

```bash
cd deploy/docker/nacos-config-tools
chmod +x setup.sh
./setup.sh
```

### Windows(PowerShell)

```powershell
cd deploy\docker\nacos-config-tools
powershell -ExecutionPolicy Bypass -File .\setup.ps1
```

脚本会**依次提问**,按提示回答即可(每项有默认值,直接回车采用):

| 顺序 | 脚本询问 | 填什么 | 自动写入 |
|---|---|---|---|
| 1 | **Nacos 自身数据库**:类型/地址/端口/库名/账号/密码 | 第 2 步准备的 `nacos` 库信息 | `docker/nacos-conf/application.properties` |
| 2 | **workflow 数据库**:类型/地址/端口/库名/账号/密码/模式名 | 工作流管理端专用库;达梦/Oracle 必填模式名,金仓/PG 回车默认 public,MySQL/SQLServer 无需 | deploy 根目录 `application-dev.yml` |
| 3 | (可选)**业务库**:y 确认后填连接信息 | 业务库| `nacos-config/**/datasource.yaml` |
| 4 | (可选)**Redis**:地址/端口/密码 | 无密码直接回车 | 同上 |
| 5 | (可选)**立即导入配置**:Nacos 已启动时才出现 | 回车=导入 | 写入 Nacos |

> 此阶段 Nacos 通常尚未启动,第 5 项会提示跳过 —— 属正常,第 4 步会自动导入。

**不想用脚本?** 也可以手动编辑同样这三个位置:`docker/nacos-conf/application.properties`(Nacos 库,数据库区域"保持只有一个块未注释")、根目录 `application-dev.yml`(workflow 库,同规则)、`docker/nacos-config-tools/nacos-config/69c4eecb-.../DEFAULT_GROUP/datasource.yaml`(业务库与 Redis)。效果完全相同。

---

## 第 4 步:部署启动

### 方式一:一键部署(Linux 麒麟 V10 / macOS)

```bash
cd deploy           # deploy 根目录
./install.sh
```

脚本自动完成四步:**导入全部镜像 → 启动 Nacos → 导入业务配置 → 启动全部微服务**,并打印状态表。

### 方式二:分步执行(Windows 或想逐步控制的场景)

```powershell
# 1. 导入全部镜像(deploy 根目录,约 5.6GB,几分钟)
Get-ChildItem -Recurse -Filter *.tar | ForEach-Object { docker load -i $_.FullName }

# 2. 启动 Nacos(deploy/docker 目录)
cd docker
docker compose up -d nacos-server
docker logs -f nacos-server        # 看到 "Nacos started successfully" 即可 Ctrl+C

# 3. 导入业务配置(deploy/docker/nacos-config-tools 目录)
cd ..\nacos-config-tools
powershell -ExecutionPolicy Bypass -File .\config-import.ps1

# 4. 启动全部微服务(deploy/docker 目录)
cd ..
docker compose up -d
```

> 首次导入镜像耗时较长属正常;镜像导入是一次性的,之后重启机器只需 `docker compose start`。

---

## 第 5 步:确认服务完全启动

微服务启动依赖 Nacos,客户端会自动重试,全部就绪约需 **1~3 分钟**。依次确认:

**① 容器状态**(在 `deploy/docker` 下):

```bash
docker compose ps
```

所有服务应为 `Up`/`running` 且不再重启 —— 默认共 **13 个**(nacos-server、gateway、oauth、system、visualdev、flowable、file、message、scheduletask、permission、visualdata、app、extend);workflow 为可选,启动后为 14 个。

**② Nacos 控制台**:浏览器打开 `http://部署机IP:30099/nacos`(账号 `nacos` / `nacos`)→ 服务管理 → 服务列表(右上角切到命名空间),应看到 `gateway`、`system` 等全部服务,实例数健康。

**③ 网关连通**:

```bash
curl http://部署机IP:30000/
```

有任何 HTTP 响应(404/whitelabel 均正常)即说明网关已就绪。**前端工程把后端地址指向 `http://部署机IP:30000` 即可联调。**

**④(可选)工作流管理端**:确认根目录 `application-dev.yml` 中的数据库连接正确后:

```bash
cd deploy/docker
docker compose --profile workflow up -d workflow
```

以上 ①②③ 全部通过,即**部署完成、服务完全启动**。若前端联调时需要调整对外域名,修改 `docker/nacos-config-tools/nacos-config/69c4eecb-.../DEFAULT_GROUP/system-config.yaml` 中的 `ApiDomain/FrontDomain/AppDomain` 后,重新执行 config-import 并 `docker compose restart` 即可。

---

## 附:启动阶段常见问题速查

| 现象 | 排查与处理 |
|---|---|
| Nacos 起不来、反复重启 | `docker logs nacos-server`:数据库连接错误 → 核对 `nacos-conf/application.properties` 后 `docker compose restart nacos-server`;`Table doesn't exist` → `jnpf_nacos` 表结构未初始化;`Public Key Retrieval` → URL 追加 `&allowPublicKeyRetrieval=true` |
| 服务一直 Restarting | `docker logs <服务名>`:连不上 `nacos-server:30099` → 等 Nacos 就绪;`datasource/Redis` 连接错误 → 核对 `datasource.yaml` 后重新导入配置并 `docker compose restart` |
| 配置导入脚本报 403 / User not found | `jnpf_nacos` 库缺 `nacos` 账号或 ADMIN 角色 → 按第 2 步 SQL 补齐并重启 nacos;Windows 提示禁止运行脚本 → 加 `-ExecutionPolicy Bypass` |
| 端口被占用 | 修改 `docker-compose.yml` 中端口映射冒号左侧(宿主机侧),`docker compose up -d` 重建 |
| 想调整某服务内存 | 改 `docker-compose.yml` 对应服务 `JAVA_OPTS` 的 `-Xmx` 后 `docker compose up -d` |
| 彻底重来 | `cd deploy/docker && docker compose down` 后从第 4 步重跑(镜像和配置文件都保留) |

更多说明(配置反向导出、国产数据库切换细节、xxl-job/Seata 说明)见脚本内注释与 `docker/nacos-config-tools/config.properties`。
