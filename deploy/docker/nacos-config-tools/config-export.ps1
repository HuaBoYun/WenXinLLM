# ============================================================
# Nacos 配置导出脚本(Windows PowerShell 5.1 及以上)
# 从运行中的 Nacos 拉取全部配置,保存到 nacos-config\ 目录
# 用法: powershell -ExecutionPolicy Bypass -File .\config-export.ps1
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
        "NACOS_SERVER"         { $script:NacosServer = $v }
        "NACOS_USERNAME"       { $script:Username    = $v }
        "NACOS_PASSWORD"       { $script:Password    = $v }
        "NACOS_NAMESPACE_ID"   { $script:NamespaceId = $v }
        "NACOS_IDENTITY_KEY"   { $script:IdentityKey   = $v }
        "NACOS_IDENTITY_VALUE" { $script:IdentityValue = $v }
    }
}
if (-not $NacosServer) { $NacosServer = "http://127.0.0.1:30099" }

Write-Host "=============================================="
Write-Host " Nacos 配置导出"
Write-Host " 来源: $NacosServer"
Write-Host " 保存目录: $ConfigDir"
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

# ---------- 检查可达性 ----------
try { $null = $client.GetAsync("$NacosServer/nacos/").Result } catch {
    Write-Host "[错误] 无法连接 $NacosServer,请确认 Nacos 已启动"
    exit 1
}

# ---------- 登录 ----------
Write-Host "[1/2] 登录 Nacos..."
$loginResp = Post-Form "$NacosServer/nacos/v1/auth/users/login" @{ username = $Username; password = $Password }
$token = $null
try { $token = ($loginResp | ConvertFrom-Json).accessToken } catch {}
if ($token) {
    Write-Host "       登录成功"
} else {
    Write-Host "       账号登录失败,改用服务端鉴权头(identity)方式"
    $client.DefaultRequestHeaders.Add($IdentityKey, $IdentityValue)
}
$atq = if ($token) { "?accessToken=$([System.Uri]::EscapeDataString($token))" } else { "" }

# ---------- 拉取配置 ----------
Write-Host "[2/2] 拉取配置列表并下载..."
if (-not (Test-Path $ConfigDir)) { New-Item -ItemType Directory -Path $ConfigDir | Out-Null }

function Export-Tenant([string]$Tenant, [string]$Store) {
    $page = 1
    while ($true) {
        $listUrl = "$NacosServer/nacos/v1/cs/configs$atq" +
                   "&search=accurate&pageNo=$page&pageSize=200" +
                   "&tenant=$([System.Uri]::EscapeDataString($Tenant))"
        $listResp = $client.GetAsync($listUrl).Result.Content.ReadAsStringAsync().Result
        if (-not $listResp) { break }
        $json = $null
        try { $json = $listResp | ConvertFrom-Json } catch {}
        if (-not $json -or -not $json.pageItems -or $json.pageItems.Count -eq 0) { break }

        foreach ($item in $json.pageItems) {
            $dir = Join-Path $Store $item.group
            if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
            $getUrl = "$NacosServer/nacos/v1/cs/configs$atq" +
                      "&dataId=$([System.Uri]::EscapeDataString($item.dataId))" +
                      "&group=$([System.Uri]::EscapeDataString($item.group))" +
                      "&tenant=$([System.Uri]::EscapeDataString($Tenant))"
            $content = $client.GetAsync($getUrl).Result.Content.ReadAsStringAsync().Result
            [System.IO.File]::WriteAllText((Join-Path $dir $item.dataId), $content, (New-Object System.Text.UTF8Encoding($false)))
            Write-Host "  [导出] $(if($Tenant){$Tenant}else{'public'}) / $($item.group) / $($item.dataId)"
        }
        if ($page -ge [int]$json.pages) { break }
        $page++
    }
}

Export-Tenant "" $ConfigDir\public
Export-Tenant $NamespaceId (Join-Path $ConfigDir $NamespaceId)

Write-Host ""
Write-Host "=============================================="
Write-Host " 导出完成,文件位于: $ConfigDir"
Write-Host " 修改后请运行 config-import.ps1 写回 Nacos"
Write-Host "=============================================="
