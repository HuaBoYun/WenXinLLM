# Oracle数据库表创建说明

## 问题描述

如果遇到以下错误:
```
ORA-00942: 表或视图不存在
```

说明数据库中缺少相应的表,需要执行建表脚本。

## 解决方案

### 方式一: 使用SQL Developer或其他Oracle客户端工具

1. 连接到Oracle数据库:
   - 主机: 192.0.2.200
   - 端口: 1521
   - SID: orcl
   - 用户名: REDACTED
   - 密码: REDACTED

2. 执行建表脚本:
   - 打开文件: `create_tc_business_system_table.sql`
   - 执行整个脚本

### 方式二: 使用sqlplus命令行工具

```bash
# 连接到数据库
sqlplus REDACTED/REDACTED@192.0.2.200:1521/orcl

# 执行脚本
@create_tc_business_system_table.sql

# 退出
exit
```

### 方式三: 执行完整的建表脚本

如果需要创建所有表,可以执行:
```bash
sqlplus REDACTED/REDACTED@192.0.2.200:1521/orcl
@treasury_common_base_config_create_tables.sql
exit
```

## 验证表是否创建成功

```sql
-- 查询表是否存在
SELECT table_name FROM user_tables WHERE table_name = 'TC_BUSINESS_SYSTEM';

-- 查看表结构
DESC TC_BUSINESS_SYSTEM;

-- 查看表数据
SELECT * FROM TC_BUSINESS_SYSTEM;
```

## 注意事项

1. 确保数据库连接正常
2. 确保用户有创建表的权限
3. 如果表已存在,脚本会自动跳过创建
4. 建议在执行前备份数据库

