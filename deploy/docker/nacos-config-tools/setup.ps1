# ============================================================
# JNPF 一键初始化脚本(Windows PowerShell 5.1 及以上)
# 用法: powershell -ExecutionPolicy Bypass -File .\setup.ps1
# 按提示依次配置:
#   1) Nacos 自身数据库        -> ..\nacos-conf\application.properties
#   2) 工作流管理端数据库      -> ..\..\application-dev.yml
#   3) 业务库/Redis(可选)     -> nacos-config\...\datasource.yaml
#   4) (可选)立即导入配置到 Nacos
# 完成后会提示启动镜像的命令
# ============================================================
$ErrorActionPreference = "Stop"
Set-Location -Path $PSScriptRoot

$NacosProps = Join-Path $PSScriptRoot "..\nacos-conf\application.properties"
$WfDevYml   = Join-Path $PSScriptRoot "..\..\application-dev.yml"
$DsYaml     = Get-ChildItem -Path (Join-Path $PSScriptRoot "nacos-config") -Recurse -Filter datasource.yaml | Select-Object -First 1
if (-not (Test-Path $NacosProps)) { Write-Host "[错误] 未找到 nacos-conf\application.properties"; exit 1 }
if (-not (Test-Path $WfDevYml))   { Write-Host "[错误] 未找到 application-dev.yml(deploy 根目录)"; exit 1 }

$NacosServer = "http://127.0.0.1:30099"
foreach ($line in Get-Content (Join-Path $PSScriptRoot "config.properties")) {
    if ($line -match '^\s*NACOS_SERVER=(.+)$') { $NacosServer = $Matches[1].Trim() }
}

$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)

function Ask([string]$tip, [string]$def = "") {
    if ($def) { $v = Read-Host "$tip [默认: $def]" } else { $v = Read-Host $tip }
    if ([string]::IsNullOrEmpty($v)) { return $def } else { return $v }
}
function AskPw([string]$tip, [string]$def = "") {
    if ($def) {
        $v = Read-Host "$tip (直接回车=保留默认)" -AsSecureString
        $plain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto([System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($v))
        if ([string]::IsNullOrEmpty($plain)) { return $def } else { return $plain }
    } else {
        $v = Read-Host "$tip (输入不回显)" -AsSecureString
        return [System.Runtime.InteropServices.Marshal]::PtrToStringAuto([System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($v))
    }
}
function YamlEscape([string]$s) { return $s.Replace("\", "\\").Replace('"', '\"') }
function PropsEscape([string]$s) { return $s.Replace("\", "\\") }

function ChooseDb([string]$def = "3") {
    Write-Host "  1) 达梦 DM        2) 人大金仓 KingbaseES   3) MySQL"
    Write-Host "  4) Oracle         5) SQLServer             6) PostgreSQL"
    $v = Ask "  请选择数据库类型" $def
    if ($v -notmatch '^[1-6]$') { Write-Host "  输入无效,请重新选择"; return ChooseDb $def }
    return [int]$v
}
function DbPortDefault([int]$t) {
    switch ($t) { 1 {5236} 2 {54321} 3 {3306} 4 {1521} 5 {1433} 6 {5432} default {3306} }
}
$DbNames = @{ 1 = "达梦DM"; 2 = "金仓KingbaseES"; 3 = "MySQL"; 4 = "Oracle"; 5 = "SQLServer"; 6 = "PostgreSQL" }

Write-Host "=============================================="
Write-Host " JNPF 微服务一键初始化"
Write-Host "=============================================="

# ---------------- [1/4] Nacos 自身数据库 ----------------
Write-Host ""
Write-Host "-------- [1/4] 配置 Nacos 自身数据库(存配置的库 jnpf_nacos) --------"
$ncType   = ChooseDb 3
$ncHost   = Ask "  数据库地址" "127.0.0.1"
$ncPort   = Ask "  数据库端口" (DbPortDefault $ncType)
$ncName   = Ask "  库名/模式名" "jnpf_nacos"
$ncUser   = Ask "  用户名" "jnpf_nacos"
$ncPass   = PropsEscape (AskPw "  密码")

$ncConv = ""; $ncTest = "select 1"; $ncUrl = ""
switch ($ncType) {
    1 { $ncConv = "dm";        $ncUrl = "jdbc:dm://${ncHost}:${ncPort}/${ncName}" }
    2 { $ncConv = "kingbase";  $ncUrl = "jdbc:kingbase8://${ncHost}:${ncPort}/${ncName}" }
    3 { $ncConv = "mysql";     $ncUrl = "jdbc:mysql://${ncHost}:${ncPort}/${ncName}?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8" }
    4 { $ncConv = "oracle";    $ncTest = "select 1 from dual"; $ncUrl = "jdbc:oracle:thin:@${ncHost}:${ncPort}:${ncName}" }
    5 { $ncConv = "sqlserver"; $ncUrl = "jdbc:sqlserver://${ncHost}:${ncPort};databaseName=${ncName};trustServerCertificate=true" }
    6 { $ncConv = "postgre";   $ncUrl = "jdbc:postgresql://${ncHost}:${ncPort}/${ncName}?currentSchema=public" }
}
function Block([int]$t, [string]$title) {
    $L = New-Object System.Collections.Generic.List[string]
    $L.Add(""); $L.Add("# ================================== $title ==========================================")
    if ($ncType -eq $t) {
        $L.Add("spring.datasource.convert=$ncConv")
        $L.Add("db.pool.config.connectionTestQuery=$ncTest")
        $L.Add("db.url.0=$ncUrl")
        $L.Add("db.user=$ncUser")
        $L.Add("db.password=$ncPass")
    } else {
        switch ($t) {
            1 { $L.Add("#spring.datasource.convert=dm");            $L.Add("#db.pool.config.connectionTestQuery=select 1");            $L.Add("#db.url.0=jdbc:dm://数据库IP:5236/jnpf_nacos");          $L.Add("#db.user=JNPF_NACOS");  $L.Add("#db.password=修改为实际密码") }
            2 { $L.Add("#spring.datasource.convert=kingbase");      $L.Add("#db.pool.config.connectionTestQuery=select 1");            $L.Add("#db.url.0=jdbc:kingbase8://数据库IP:54321/jnpf_nacos"); $L.Add("#db.user=system");      $L.Add("#db.password=修改为实际密码") }
            3 { $L.Add("#spring.datasource.convert=mysql");         $L.Add("#db.pool.config.connectionTestQuery=select 1");            $L.Add("#db.url.0=jdbc:mysql://数据库IP:3306/jnpf_nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8"); $L.Add("#db.user=jnpf_nacos"); $L.Add("#db.password=修改为实际密码") }
            4 { $L.Add("#spring.datasource.convert=oracle");        $L.Add("#db.pool.config.connectionTestQuery=select 1 from dual");  $L.Add("#db.url.0=jdbc:oracle:thin:@数据库IP:1521:orcl");       $L.Add("#db.user=JNPF_NACOS");  $L.Add("#db.password=修改为实际密码") }
            5 { $L.Add("#spring.datasource.convert=sqlserver");     $L.Add("#db.pool.config.connectionTestQuery=select 1");            $L.Add("#db.url.0=jdbc:sqlserver://数据库IP:1433;databaseName=jnpf_nacos;trustServerCertificate=true"); $L.Add("#db.user=sa");          $L.Add("#db.password=修改为实际密码") }
            6 { $L.Add("#spring.datasource.convert=postgre");       $L.Add("#db.pool.config.connectionTestQuery=select 1");            $L.Add("#db.url.0=jdbc:postgresql://数据库IP:5432/jnpf_nacos?currentSchema=public"); $L.Add("#db.user=dbuser"); $L.Add("#db.password=修改为实际密码") }
        }
    }
    return $L
}

$lines = [System.IO.File]::ReadAllLines($NacosProps)
$start = -1; $end = -1
for ($i = 0; $i -lt $lines.Count; $i++) {
    if ($lines[$i] -like "# >>> DB-SWITCH-START*") { $start = $i }
    if ($lines[$i] -like "# <<< DB-SWITCH-END*")   { $end = $i }
}
if ($start -lt 0 -or $end -lt 0) { Write-Host "[错误] application.properties 缺少改写标记行"; exit 1 }

$newSec = New-Object System.Collections.Generic.List[string]
$newSec.Add("# ------------------------------------------------------------------------------------------")
$newSec.Add("# ★ 数据库类型切换:当前启用 [$($DbNames[$ncType])]")
$newSec.Add("#   如需更换数据库,重新运行 setup 脚本或按下方模板手动切换(只保留一块未注释)")
$newSec.Add("# ------------------------------------------------------------------------------------------")
$newSec.AddRange([string[]](Block 1 "【国产化推荐】达梦 DM"))
$newSec.AddRange([string[]](Block 2 "【国产化推荐】人大金仓 KingbaseES"))
$newSec.AddRange([string[]](Block 3 "MySQL"))
$newSec.AddRange([string[]](Block 4 "Oracle"))
$newSec.AddRange([string[]](Block 5 "SQLServer"))
$newSec.AddRange([string[]](Block 6 "PostgreSQL"))

$out = New-Object System.Collections.Generic.List[string]
for ($i = 0; $i -lt $lines.Count; $i++) {
    if ($i -eq $start) {
        $out.Add($lines[$i]); $out.AddRange($newSec); $i = $end - 1; continue
    }
    $out.Add($lines[$i])
}
[System.IO.File]::WriteAllLines($NacosProps, $out, $Utf8NoBom)
Write-Host "  [完成] 已写入 Nacos 数据库配置 -> nacos-conf\application.properties"

# ---------------- [2/4] workflow 数据库 ----------------
Write-Host ""
Write-Host "-------- [2/4] 配置工作流管理端(workflow)数据库 --------"
$wfType = ChooseDb 3
$wfPortDef = DbPortDefault $wfType
$wfHost = Ask "  数据库地址" "127.0.0.1"
$wfPort = Ask "  数据库端口" $wfPortDef
$wfName = Ask "  库名" "jnpf_flow"
$wfUser = Ask "  用户名" "root"
$wfPass = YamlEscape (AskPw "  密码")

$wfSchema = ""
switch ($wfType) {
    { $_ -eq 1 -or $_ -eq 4 } { $wfSchema = Ask "  模式名(必填)" $wfName }
    { $_ -eq 2 -or $_ -eq 6 } { $wfSchema = Ask "  模式名(回车=public)" "public" }
}

$wfDriver = ""; $wfDsType = ""; $wfUrl = ""; $wfFType = ""; $wfSchemaYml = ""
switch ($wfType) {
    1 { $wfDriver = "dm.jdbc.driver.DmDriver"; $wfDsType = "dm.jdbc.driver.DmdbDataSource"
        $wfUrl = "jdbc:dm://${wfHost}:${wfPort}/${wfName}?compatibleMode=oracle"
        $wfSchemaYml = $wfSchema; $wfFType = "dm" }
    2 { $wfDriver = "org.postgresql.Driver"; $wfDsType = "org.postgresql.ds.PGSimpleDataSource"
        $wfUrl = "jdbc:postgresql://${wfHost}:${wfPort}/${wfName}?currentSchema=$(if($wfSchema){$wfSchema}else{'public'})"
        $wfSchemaYml = $wfSchema; $wfFType = "postgres" }
    3 { $wfDriver = "com.mysql.cj.jdbc.Driver"; $wfDsType = "com.mysql.cj.jdbc.MysqlDataSource"
        $wfUrl = "jdbc:mysql://${wfHost}:${wfPort}/${wfName}?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true&nullCatalogMeansCurrent=true&useSSL=false&allowPublicKeyRetrieval=true"
        $wfSchemaYml = ""; $wfFType = "mysql" }
    4 { $wfDriver = "oracle.jdbc.OracleDriver"; $wfDsType = "oracle.jdbc.datasource.impl.OracleDataSource"
        $wfUrl = "jdbc:oracle:thin:@${wfHost}:${wfPort}:${wfName}"
        $wfSchemaYml = $wfSchema; $wfFType = "oracle" }
    5 { $wfDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; $wfDsType = "com.microsoft.sqlserver.jdbc.SQLServerDataSource"
        $wfUrl = "jdbc:sqlserver://${wfHost}:${wfPort};databaseName=${wfName};trustServerCertificate=true"
        $wfSchemaYml = ""; $wfFType = "mssql" }
    6 { $wfDriver = "org.postgresql.Driver"; $wfDsType = "org.postgresql.ds.PGSimpleDataSource"
        $wfUrl = "jdbc:postgresql://${wfHost}:${wfPort}/${wfName}"
        $wfSchemaYml = $wfSchema; $wfFType = "postgres" }
}

$yml = @"
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
  database-schema: "$wfSchemaYml"
  database-type: $wfFType

spring:
  datasource:
    driver-class-name: $wfDriver
    type: $wfDsType
    url: $wfUrl
    username: $wfUser
    password: "$wfPass"
"@
[System.IO.File]::WriteAllText($WfDevYml, $yml, $Utf8NoBom)
Write-Host "  [完成] 已写入 workflow 配置 -> application-dev.yml(deploy 根目录)"

# ---------------- [3/4] 业务库 / Redis(可选) ----------------
Write-Host ""
Write-Host "-------- [3/4] 配置业务库与 Redis(写入 nacos 配置文件 datasource.yaml) --------"
$bsHost = $null
if ($DsYaml) {
    $yn = Ask "  是否配置业务库?(y=配置,回车=跳过)" ""
    if ($yn -eq "y" -or $yn -eq "Y") {
        $bsType = ChooseDb 3
        $bsTypeName = @{ 1 = "DM"; 2 = "KingbaseES"; 3 = "MySQL"; 4 = "Oracle"; 5 = "SQLServer"; 6 = "PostgreSQL" }[$bsType]
        $bsHost = Ask "  业务库地址" "127.0.0.1"
        $bsPort = Ask "  业务库端口" (DbPortDefault $bsType)
        $bsName = Ask "  业务库名" "jnpf_init"
        $bsUser = Ask "  用户名" "root"
        $bsPass = YamlEscape (AskPw "  密码")

        $dl = [System.IO.File]::ReadAllLines($DsYaml.FullName)
        $sec = ""
        for ($i = 0; $i -lt $dl.Count; $i++) {
            if ($dl[$i] -match '^  ([A-Za-z-]+):') { $sec = $Matches[1] }
            if ($sec -eq "datasource") {
                if ($dl[$i] -match '^(\s+)db-type:')      { $dl[$i] = "$($Matches[1])db-type: $bsTypeName" }
                elseif ($dl[$i] -match '^(\s+)db-name:')  { $dl[$i] = "$($Matches[1])db-name: $bsName" }
                elseif ($dl[$i] -match '^(\s+)host:')     { $dl[$i] = "$($Matches[1])host: $bsHost" }
                elseif ($dl[$i] -match '^(\s+)port:')     { $dl[$i] = "$($Matches[1])port: $bsPort" }
                elseif ($dl[$i] -match '^(\s+)username:') { $dl[$i] = "$($Matches[1])username: $bsUser" }
                elseif ($dl[$i] -match '^(\s+)password:') { $dl[$i] = "$($Matches[1])password: `"$bsPass`"" }
            }
        }
        [System.IO.File]::WriteAllLines($DsYaml.FullName, $dl, $Utf8NoBom)
        Write-Host "  [完成] 业务库已写入 datasource.yaml"
    }
    $yn = Ask "  是否配置 Redis?(y=配置,回车=跳过)" ""
    if ($yn -eq "y" -or $yn -eq "Y") {
        $rdHost = Ask "  Redis 地址" "127.0.0.1"
        $rdPort = Ask "  Redis 端口" "6379"
        $rdPass = YamlEscape (AskPw "  Redis 密码(无密码直接回车)")

        $dl = [System.IO.File]::ReadAllLines($DsYaml.FullName)
        $sec = ""
        for ($i = 0; $i -lt $dl.Count; $i++) {
            if ($dl[$i] -match '^  ([A-Za-z-]+):') { $sec = $Matches[1] }
            if ($sec -eq "redis") {
                if ($dl[$i] -match '^(\s+)host:') { $dl[$i] = "$($Matches[1])host: $rdHost" }
                elseif ($dl[$i] -match '^(\s+)port:') { $dl[$i] = "$($Matches[1])port: $rdPort" }
                elseif ($dl[$i] -match '^(\s*)#\s*password:' -and $rdPass) { $dl[$i] = "$($Matches[1])password: `"$rdPass`"" }
            }
        }
        [System.IO.File]::WriteAllLines($DsYaml.FullName, $dl, $Utf8NoBom)
        Write-Host "  [完成] Redis 已写入 datasource.yaml"
    }
} else {
    Write-Host "  [跳过] 未找到 datasource.yaml"
}

# ---------------- [4/4] 导入配置 ----------------
Write-Host ""
Write-Host "-------- [4/4] 发布业务配置到 Nacos --------"
$imported = $false
$reachable = $false
try { $null = (New-Object System.Net.Http.HttpClient).GetAsync("$NacosServer/nacos/").Result; $reachable = $true } catch {}
if (-not $reachable) {
    Write-Host "  [提示] Nacos($NacosServer)暂未启动,跳过导入"
} else {
    $yn = Ask "  Nacos 已就绪,是否立即导入业务配置?(Y=导入,回车=跳过)" "Y"
    if ($yn -ne "n" -and $yn -ne "N") {
        & powershell -ExecutionPolicy Bypass -File (Join-Path $PSScriptRoot "config-import.ps1")
        $imported = $true
    }
}

# ---------------- 完成提示 ----------------
Write-Host ""
Write-Host "=============================================="
Write-Host " 初始化配置全部完成!"
Write-Host "----------------------------------------------"
Write-Host " 已配置:"
Write-Host "   1) Nacos 自身数据库   -> docker\nacos-conf\application.properties"
Write-Host "   2) workflow 数据库    -> application-dev.yml(deploy 根目录)"
if ($bsHost) { Write-Host "   3) 业务库/Redis       -> datasource.yaml(Nacos 配置文件)" }
Write-Host ""
Write-Host " 现在可以启动镜像了,请按顺序执行:"
Write-Host "   cd docker"
Write-Host "   docker compose up -d nacos-server      # 1.先启动 nacos"
if (-not $imported) {
    Write-Host "   cd nacos-config-tools; .\config-import.ps1   # 2.导入业务配置(nacos 启动后执行一次)"
}
Write-Host "   docker compose up -d                   # 3.启动全部微服务"
Write-Host "   (可选)docker compose --profile workflow up -d workflow   # 工作流管理端"
Write-Host ""
Write-Host " 验证: nacos 控制台 http://127.0.0.1:30099/nacos (nacos/nacos)"
Write-Host "=============================================="
