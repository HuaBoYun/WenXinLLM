# TBL_BILL_AUDIT_RULE 表结构修改指南

## 问题描述

在实现稽核规则功能时，发现数据库表 `TBL_BILL_AUDIT_RULE` 缺少以下字段：
- RULE_NAME（规则名称）
- RULE_TYPE（规则类型）
- RULE_EXPRESSION（规则表达式）
- WARNING_MESSAGE（警告消息）
- IS_ENABLED（是否启用）
- DESCRIPTION（规则描述）

导致插入数据时报错：
```
dm.jdbc.driver.DMException: 第21 行附近出现错误:
无效的列名[RULE_NAME]
```

## 解决方案

执行 SQL 脚本添加缺失的字段。

## 执行步骤

### 方式一：使用 DM 管理工具（推荐）

1. **打开 DM Manager 或 DM Console**
   - 连接到数据库：`jdbc:dm://192.0.2.200:5236/REDACTED`
   - 用户名：`REDACTED`
   - 密码：`REDACTED`

2. **执行 SQL 脚本**
   - 打开文件：`src/main/resources/sql/alter_tbl_bill_audit_rule.sql`
   - 选中所有 ALTER TABLE 语句
   - 点击"执行"按钮

3. **验证结果**
   ```sql
   SELECT 
       COLUMN_NAME,
       TYPE_NAME AS DATA_TYPE,
       LENGTH AS DATA_LENGTH,
       NULLABLE
   FROM SYSCOLUMNS
   WHERE TABLE_NAME = 'TBL_BILL_AUDIT_RULE'
   ORDER BY COLID;
   ```

### 方式二：使用命令行工具

1. **连接到达梦数据库**
   ```bash
   disql REDACTED/REDACTED@192.0.2.200:5236
   ```

2. **执行 SQL 脚本**
   ```sql
   -- 添加规则名称字段
   ALTER TABLE TBL_BILL_AUDIT_RULE ADD RULE_NAME VARCHAR(200);
   COMMENT ON COLUMN TBL_BILL_AUDIT_RULE.RULE_NAME IS '规则名称';

   -- 添加规则类型字段
   ALTER TABLE TBL_BILL_AUDIT_RULE ADD RULE_TYPE VARCHAR(50);
   COMMENT ON COLUMN TBL_BILL_AUDIT_RULE.RULE_TYPE IS '规则类型';

   -- 添加规则表达式字段
   ALTER TABLE TBL_BILL_AUDIT_RULE ADD RULE_EXPRESSION VARCHAR(1000);
   COMMENT ON COLUMN TBL_BILL_AUDIT_RULE.RULE_EXPRESSION IS '规则表达式';

   -- 添加警告消息字段
   ALTER TABLE TBL_BILL_AUDIT_RULE ADD WARNING_MESSAGE VARCHAR(500);
   COMMENT ON COLUMN TBL_BILL_AUDIT_RULE.WARNING_MESSAGE IS '警告消息';

   -- 添加是否启用字段
   ALTER TABLE TBL_BILL_AUDIT_RULE ADD IS_ENABLED INTEGER DEFAULT 1;
   COMMENT ON COLUMN TBL_BILL_AUDIT_RULE.IS_ENABLED IS '是否启用';

   -- 添加规则描述字段
   ALTER TABLE TBL_BILL_AUDIT_RULE ADD DESCRIPTION VARCHAR(1000);
   COMMENT ON COLUMN TBL_BILL_AUDIT_RULE.DESCRIPTION IS '规则描述';

   COMMIT;
   ```

3. **验证结果**
   ```sql
   SELECT COUNT(*) FROM SYSCOLUMNS WHERE TABLE_NAME = 'TBL_BILL_AUDIT_RULE';
   -- 应该返回 13（原有7个字段 + 新增6个字段）
   ```

## 修改后的表结构

| 字段名 | 数据类型 | 长度 | 是否必填 | 说明 |
|--------|---------|------|---------|------|
| ID | VARCHAR | 32 | 是 | 主键ID |
| CONFIG_ID | VARCHAR | 32 | 是 | 配置ID |
| RULE_ID | VARCHAR | 32 | 是 | 规则ID |
| **RULE_NAME** | **VARCHAR** | **200** | **否** | **规则名称（新增）** |
| **RULE_TYPE** | **VARCHAR** | **50** | **否** | **规则类型（新增）** |
| PRIORITY | INTEGER | - | 否 | 优先级 |
| **RULE_EXPRESSION** | **VARCHAR** | **1000** | **否** | **规则表达式（新增）** |
| **WARNING_MESSAGE** | **VARCHAR** | **500** | **否** | **警告消息（新增）** |
| **IS_ENABLED** | **INTEGER** | - | **否** | **是否启用（新增）** |
| **DESCRIPTION** | **VARCHAR** | **1000** | **否** | **规则描述（新增）** |
| CREATE_TIME | TIMESTAMP | - | 否 | 创建时间 |
| CREATE_USER | VARCHAR | 32 | 否 | 创建人 |
| REMARK | VARCHAR | 500 | 否 | 备注 |

## 测试验证

执行 SQL 脚本后，重新测试稽核规则功能：

1. **刷新前端页面**
2. **点击"稽核规则"按钮**
3. **点击"新增规则"**
4. **填写表单并保存**
5. **查看后端日志**，应该显示：
   ```
   保存稽核规则成功，configId: xxx, 规则数量: 1
   ```

## 注意事项

1. **备份数据**：执行 ALTER TABLE 前建议备份表数据
2. **权限检查**：确保当前用户有 ALTER TABLE 权限
3. **影响范围**：此修改只影响 `TBL_BILL_AUDIT_RULE` 表，不影响其他表
4. **兼容性**：新增字段都允许为空，不影响现有数据

## 回滚方案

如果需要回滚，执行以下 SQL：

```sql
ALTER TABLE TBL_BILL_AUDIT_RULE DROP COLUMN RULE_NAME;
ALTER TABLE TBL_BILL_AUDIT_RULE DROP COLUMN RULE_TYPE;
ALTER TABLE TBL_BILL_AUDIT_RULE DROP COLUMN RULE_EXPRESSION;
ALTER TABLE TBL_BILL_AUDIT_RULE DROP COLUMN WARNING_MESSAGE;
ALTER TABLE TBL_BILL_AUDIT_RULE DROP COLUMN IS_ENABLED;
ALTER TABLE TBL_BILL_AUDIT_RULE DROP COLUMN DESCRIPTION;
COMMIT;
```

## 相关文件

- SQL 脚本：`src/main/resources/sql/alter_tbl_bill_audit_rule.sql`
- 实体类：`com.financial.sharing.entity.TblBillAuditRule`
- Service 实现：`com.financial.sharing.service.impl.TblBillConfigServiceImpl`
- Controller：`com.financial.sharing.controller.BillConfigController`

