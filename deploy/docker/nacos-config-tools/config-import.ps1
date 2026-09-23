# ============================================================
# Nacos 配置导入脚本(Windows PowerShell 5.1 及以上)
# 将 nacos-config\ 目录下的所有配置文件发布到 Nacos
# 用法: 右键"使用 PowerShell 运行",或:
#       powershell -ExecutionPolicy Bypass -File .\config-import.ps1
# ============================================================
$ErrorActionPreference = "Stop"
Set-Location -Path $PSScriptRoot

$ConfFile   = Join-Path $PSScriptRoot "config.properties"
$ConfigDir  = Join-Path $PSScriptRoot "nacos-config"

# ---------- 读取配置文件 ----------
if (-not (Test-Path $ConfFile)) { Write-Host "[错误] 未找到 config.properties"; exit 1 }
foreach ($line in Get-Content $ConfFile) {
    $line = $line.TrimEnd()
    if ($line -match '^\s*#' -or $line -notmatch '=') { continue }
    $k = $line.Split('=')[0].Trim()
    $v = ($line.Substring($line.IndexOf('=') + 1)).Trim()
    switch ($k) {
        "NACOS_SERVER"         { $script:NacosServer   = $v }
        "NACOS_USERNAME"       { $script:Username      = $v }
        "NACOS_PASSWORD"       { $script:Password      = $v }
        "NACOS_NAMESPACE_ID"   { $script:NamespaceId   = $v }
        "NACOS_NAMESPACE_NAME" { $script:NamespaceName = $v }
        "NACOS_IDENTITY_KEY"   { $script:IdentityKey   = $v }
        "NACOS_IDENTITY_VALUE" { $script:IdentityValue = $v }
    }
}
if (-not $NacosServer) { $NacosServer = "http://127.0.0.1:30099" }

Write-Host "==============================================" 
Write-Host " Nacos 配置导入"
Write-Host " 目标: $NacosServer"
Write-Host " 命名空间: $NamespaceId"
Write-Host "==============================================" 

Add-Type -AssemblyName System.Net.Http
$client = New-Object System.Net.Http.HttpClient
$client.Timeout = [TimeSpan]::FromSeconds(30)

function Post-Form([string]$Url, [hashtable]$Form) {
    $pairs = [System.Collections.Generic.List[System.Collections.Generic.KeyValuePair[string,string]]]::new()
    foreach ($key in $Form.Keys) {
        $pairs.Add([System.Collections.Generic.KeyValuePair[string,string]]::new([string]$key, [string]$Form[$key]))
    }
    $resp = $client.PostAsync($Url, (New-Object System.Net.Http.FormUrlEncodedContent($pairs))).Result
    return $resp.Content.ReadAsStringAsync().Result
}
function Get-Url([string]$Url) {
    try { return $client.GetAsync($Url).Result.Content.ReadAsStringAsync().Result } catch { return $null }
}

# ---------- 检查可达性 ----------
Write-Host "[1/4] 检查 Nacos 服务..."
try { $null = $client.GetAsync("$NacosServer/nacos/").Result } catch {
    Write-Host "[错误] 无法连接 $NacosServer,请确认 nacos 容器已启动(docker ps)"
    exit 1
}

# ---------- 登录 ----------
Write-Host "[2/4] 登录 Nacos..."
$loginResp = Post-Form "$NacosServer/nacos/v1/auth/users/login" @{ username = $Username; password = $Password }
$token = $null
try { $token = ($loginResp | ConvertFrom-Json).accessToken } catch {}
$useIdentity = $false
if ($token) {
    Write-Host "       登录成功(账号 $Username)"
} else {
    $useIdentity = $true
    Write-Host "       账号登录失败,改用服务端鉴权头(identity)方式"
    $client.DefaultRequestHeaders.Add($IdentityKey, $IdentityValue)
}
# AccessTokenQuery: 拼 GET 查询串用
$atq = if ($token) { "accessToken=$([System.Uri]::EscapeDataString($token))" } else { "" }

# ---------- 命名空间 ----------
Write-Host "[3/4] 检查命名空间..."
$nsResp = Get-Url "$NacosServer/nacos/v1/console/namespaces$(if($atq){"?$atq"})"
if ($nsResp -and $nsResp.Contains($NamespaceId)) {
    Write-Host "       命名空间已存在"
} else {
    Write-Host "       命名空间不存在,自动创建..."
    $nsForm = @{ customNamespaceId = $NamespaceId; namespaceName = $NamespaceName; namespaceDesc = "JNPF platform namespace" }
    if ($token) { $nsForm["accessToken"] = $token }
    $r = Post-Form "$NacosServer/nacos/v1/console/namespaces" $nsForm
    Write-Host "       创建结果: $r"
}

# ---------- 导入 ----------
Write-Host "[4/4] 开始导入配置..."
$fail = 0; $ok = 0
Get-ChildItem -Path $ConfigDir -Directory | ForEach-Object {
    $nsId = $_.Name
    $tenant = $(if ($nsId -eq "public") { "" } else { $nsId })
    Get-ChildItem -Path $_.FullName -Recurse -File | ForEach-Object {
        $group   = $_.Directory.Name
        $dataId  = $_.Name
        switch -Regex ($dataId) {
            '\.(yaml|yml)$' { $ctype = "yaml" }
            '\.properties$' { $ctype = "properties" }
            '\.json$'       { $ctype = "json" }
            default         { $ctype = "text" }
        }
        $content = [System.IO.File]::ReadAllText($_.FullName, [System.Text.Encoding]::UTF8)
        $form = @{ dataId = $dataId; group = $group; tenant = $tenant; type = $ctype; content = $content }
        if ($token) { $form["accessToken"] = $token }
        $resp = Post-Form "$NacosServer/nacos/v1/cs/configs" $form
        if ($resp -eq "true") {
            $ok++
            Write-Host "  [成功] $nsId / $group / $dataId"
        } else {
            $fail++
            Write-Host "  [失败] $nsId / $group / $dataId  (返回: $resp)"
        }
    }
}

Write-Host ""
Write-Host "==============================================" 
if ($fail -eq 0) { Write-Host " 导入完成:成功 $ok 项,全部成功"; Write-Host " 建议重启服务使配置生效: docker compose restart" }
else { Write-Host " 导入完成,存在 $fail 个失败项,请检查上方 [失败] 日志" }
Write-Host "==============================================" 
