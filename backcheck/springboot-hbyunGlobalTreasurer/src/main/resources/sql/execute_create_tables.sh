#!/bin/bash
# ============================================
# 融资管理模块建表脚本执行说明
# ============================================
#
# 数据库连接信息 (来自 application-dev.yml):
# - 数据库类型: 达梦数据库 (DM8)
# - 连接地址: jdbc:dm://192.0.2.200:5236/REDACTED
# - 用户名: REDACTED
# - 密码: REDACTED
#
# 执行方式:
#
# 方式1: 使用达梦数据库客户端工具 (disql)
# disql REDACTED/REDACTED@192.0.2.200:5236 -i create_tables.sql
#
# 方式2: 使用DBeaver或其他数据库管理工具
# 1. 连接到达梦数据库
# 2. 打开 create_tables.sql 文件
# 3. 执行脚本
#
# 方式3: 通过项目启动时自动执行 (Flyway/Liquibase)
# 将SQL文件放入 db/migration 目录
#
# 注意事项:
# 1. 执行前请确认数据库连接正常
# 2. 建议先在测试环境执行验证
# 3. 如果表已存在，需要先删除或使用 CREATE TABLE IF NOT EXISTS
# ============================================

echo "请使用以下方式之一执行建表脚本:"
echo ""
echo "1. 达梦客户端: disql REDACTED/REDACTED@192.0.2.200:5236 -i create_tables.sql"
echo ""
echo "2. 使用DBeaver等数据库管理工具连接后执行"
echo ""
echo "3. 通过项目中的数据库迁移工具执行"

