# 融资管理模块数据库执行指南

## 📋 执行概述

本目录包含融资管理模块所需的数据库表创建脚本和测试数据脚本。

**数据库**: 达梦数据库 (DM8)
**数据库名**: REDACTED
**数据库地址**: 192.0.2.200:5236

---

## 📁 文件清单

| 文件名 | 说明 | 执行顺序 |
|-------|------|---------|
| financing_management_create_tables.sql | 创建融资管理模块的11个新表 | 第一步 |
| financing_management_init_data.sql | 插入测试数据 | 第二步 |

---

## 🚀 执行方法

### 方法一: 使用达梦数据库管理工具 (推荐)

#### 1. 使用达梦数据库客户端工具

**工具**: DM管理工具 或 DM数据迁移工具

**步骤**:
1. 打开达梦数据库客户端工具
2. 连接到数据库:
   - 主机: `192.0.2.200`
   - 端口: `5236`
   - 用户名: `REDACTED`
   - 密码: `REDACTED`
   - 数据库: `REDACTED`
3. 打开SQL执行窗口
4. 执行第一步脚本: `financing_management_create_tables.sql`
5. 验证表创建成功后,执行第二步脚本: `financing_management_init_data.sql`
6. 验证数据插入成功

#### 2. 使用disql命令行工具

**安装达梦数据库客户端**:
```bash
# 下载达梦数据库客户端
# 解压后进入bin目录
cd /path/to/dm/bin
```

**执行SQL脚本**:
```bash
# 连接数据库并执行建表脚本
./disql REDACTED/REDACTED@192.0.2.200:5236/REDACTED \
  -f /path/to/financing_management_create_tables.sql

# 连接数据库并执行测试数据脚本
./disql REDACTED/REDACTED@192.0.2.200:5236/REDACTED \
  -f /path/to/financing_management_init_data.sql
```

### 方法二: 使用Spring Boot应用程序自动执行

在Spring Boot启动时自动执行SQL脚本,需要配置:

```yaml
spring:
  sql:
    init:
      mode: always
      schema-locations: classpath:db/oracle/financing_management_create_tables.sql
      data-locations: classpath:db/oracle/financing_management_init_data.sql
      encoding: UTF-8
```

**注意**: 此方法需要重启应用,建议先手动执行验证。

### 方法三: 使用Java代码执行

创建一个数据库初始化工具类:

```java
@Component
public class FinancingManagementDbInitializer {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setStopOnError(true);

            // 执行建表脚本
            InputStreamReader createTableReader = new InputStreamReader(
                getClass().getResourceAsStream("/db/oracle/financing_management_create_tables.sql"),
                StandardCharsets.UTF_8
            );
            runner.runScript(createTableReader);

            // 执行测试数据脚本
            InputStreamReader initDataReader = new InputStreamReader(
                getClass().getResourceAsStream("/db/oracle/financing_management_init_data.sql"),
                StandardCharsets.UTF_8
            );
            runner.runScript(initDataReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## ✅ 验证执行结果

### 1. 验证表创建成功

执行以下SQL查询,检查11个新表是否创建成功:

```sql
-- 查询融资管理相关表
SELECT TABLE_NAME, COMMENTS FROM USER_TAB_COMMENTS
WHERE TABLE_NAME IN (
    'TBL_GUARANTEE_APPLICATION',
    'TBL_GUARANTEE_CONTRACT',
    'TBL_COLLATERAL',
    'TBL_CREDIT_APPLICATION',
    'TBL_CREDIT_CONTRACT',
    'TBL_CREDIT_LIMIT',
    'TBL_FINANCIAL_INSTITUTION',
    'TBL_BANK_LOAN',
    'TBL_BILL_DISCOUNT',
    'TBL_BILL_ACCEPTANCE',
    'TBL_FINANCIAL_LEASE'
)
ORDER BY TABLE_NAME;
```

**预期结果**: 应该返回11条记录,每条记录包含表名和注释。

### 2. 验证测试数据插入成功

执行以下SQL查询,检查测试数据是否插入成功:

```sql
-- 统计各表的记录数
SELECT '金融机构表' AS 表名, COUNT(*) AS 记录数 FROM TBL_FINANCIAL_INSTITUTION
UNION ALL
SELECT '担保申请表', COUNT(*) FROM TBL_GUARANTEE_APPLICATION
UNION ALL
SELECT '担保合同表', COUNT(*) FROM TBL_GUARANTEE_CONTRACT
UNION ALL
SELECT '担保物表', COUNT(*) FROM TBL_COLLATERAL
UNION ALL
SELECT '授信申请表', COUNT(*) FROM TBL_CREDIT_APPLICATION
UNION ALL
SELECT '授信合同表', COUNT(*) FROM TBL_CREDIT_CONTRACT
UNION ALL
SELECT '授信额度表', COUNT(*) FROM TBL_CREDIT_LIMIT
UNION ALL
SELECT '银行贷款表', COUNT(*) FROM TBL_BANK_LOAN
UNION ALL
SELECT '票据贴现表', COUNT(*) FROM TBL_BILL_DISCOUNT
UNION ALL
SELECT '银行承兑表', COUNT(*) FROM TBL_BILL_ACCEPTANCE
UNION ALL
SELECT '融资租赁表', COUNT(*) FROM TBL_FINANCIAL_LEASE;
```

**预期结果**:
- 金融机构表: 6条记录
- 担保申请表: 3条记录
- 担保合同表: 2条记录
- 担保物表: 3条记录
- 授信申请表: 3条记录
- 授信合同表: 2条记录
- 授信额度表: 2条记录
- 银行贷款表: 2条记录
- 票据贴现表: 2条记录
- 银行承兑表: 2条记录
- 融资租赁表: 2条记录

### 3. 验证索引创建成功

```sql
-- 查询索引信息
SELECT INDEX_NAME, TABLE_NAME, COLUMN_NAME
FROM USER_IND_COLUMNS
WHERE TABLE_NAME IN (
    'TBL_GUARANTEE_APPLICATION',
    'TBL_GUARANTEE_CONTRACT',
    'TBL_COLLATERAL',
    'TBL_CREDIT_APPLICATION',
    'TBL_CREDIT_CONTRACT',
    'TBL_CREDIT_LIMIT',
    'TBL_FINANCIAL_INSTITUTION',
    'TBL_BANK_LOAN',
    'TBL_BILL_DISCOUNT',
    'TBL_BILL_ACCEPTANCE',
    'TBL_FINANCIAL_LEASE'
)
ORDER BY TABLE_NAME, INDEX_NAME;
```

---

## 🔧 常见问题

### 问题1: 表已存在

**错误信息**: `table or view already exists`

**解决方案**:
```sql
-- 删除旧表(谨慎操作,会丢失数据)
DROP TABLE TBL_FINANCIAL_LEASE CASCADE CONSTRAINTS;
DROP TABLE TBL_BILL_ACCEPTANCE CASCADE CONSTRAINTS;
DROP TABLE TBL_BILL_DISCOUNT CASCADE CONSTRAINTS;
DROP TABLE TBL_BANK_LOAN CASCADE CONSTRAINTS;
DROP TABLE TBL_CREDIT_LIMIT CASCADE CONSTRAINTS;
DROP TABLE TBL_CREDIT_CONTRACT CASCADE CONSTRAINTS;
DROP TABLE TBL_CREDIT_APPLICATION CASCADE CONSTRAINTS;
DROP TABLE TBL_FINANCIAL_INSTITUTION CASCADE CONSTRAINTS;
DROP TABLE TBL_COLLATERAL CASCADE CONSTRAINTS;
DROP TABLE TBL_GUARANTEE_CONTRACT CASCADE CONSTRAINTS;
DROP TABLE TBL_GUARANTEE_APPLICATION CASCADE CONSTRAINTS;
```

### 问题2: 外键约束错误

**错误信息**: `constraint violated - parent key not found`

**解决方案**: 确保按照脚本中的顺序执行,先创建被引用的表。

### 问题3: 字符编码问题

**错误信息**: 字符显示乱码

**解决方案**:
```sql
-- 确保数据库使用UTF-8编码
-- 在连接字符串中添加: ?useUnicode=true&characterEncoding=utf-8
```

---

## 📊 创建的表说明

### 担保业务表 (3个)

1. **TBL_FINANCIAL_INSTITUTION** - 金融机构表
   - 存储银行、租赁公司等金融机构基本信息

2. **TBL_GUARANTEE_APPLICATION** - 担保申请表
   - 存储担保申请信息

3. **TBL_GUARANTEE_CONTRACT** - 担保合同表
   - 存储担保合同信息

4. **TBL_COLLATERAL** - 担保物表
   - 存储担保物权信息

### 授信业务表 (4个)

5. **TBL_CREDIT_APPLICATION** - 授信申请表
   - 存储授信申请信息

6. **TBL_CREDIT_CONTRACT** - 授信合同表
   - 存储授信合同信息

7. **TBL_CREDIT_LIMIT** - 授信额度表
   - 存储授信额度信息

### 其他业务表 (4个)

8. **TBL_BANK_LOAN** - 银行贷款表
   - 存储银行贷款信息

9. **TBL_BILL_DISCOUNT** - 票据贴现表
   - 存储票据贴现信息

10. **TBL_BILL_ACCEPTANCE** - 银行承兑表
    - 存储银行承兑汇票信息

11. **TBL_FINANCIAL_LEASE** - 融资租赁表
    - 存储融资租赁信息

---

## 📞 技术支持

如遇到问题,请联系:
- 开发团队: 华博云开发团队
- 文档位置: `/Users/Zhuanz/dev/hb-code/接口文档/`
- SQL脚本位置: `/Users/Zhuanz/dev/hb-code/hbyun-cloud/hbyun-cloud/springboot-hbyunGlobalTreasurer/src/main/resources/db/oracle/`

---

**文档版本**: v1.0
**创建日期**: 2026-01-14
**最后更新**: 2026-01-14
