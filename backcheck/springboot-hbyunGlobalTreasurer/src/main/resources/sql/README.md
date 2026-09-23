# 测试数据导入说明

## 数据库连接信息

- 数据库类型: 达梦数据库 (DM8)
- 主机地址: 192.0.2.200
- 端口: 5236
- 数据库名: REDACTED
- 用户名: REDACTED
- 密码: REDACTED

## 方法1: 使用达梦数据库客户端工具 (推荐)

### Windows系统
1. 打开达梦数据库管理工具 (DM Management Tool)
2. 连接到数据库: 192.0.2.200:5236
3. 打开SQL文件: `src/main/resources/sql/test-data.sql`
4. 执行SQL脚本 (F5或点击执行按钮)

### Linux/Mac系统
```bash
# 使用disql命令行工具
disql REDACTED/REDACTED@192.0.2.200:5236

# 在disql中执行
SQL> start /path/to/test-data.sql

# 或者直接执行
disql REDACTED/REDACTED@192.0.2.200:5236 < /path/to/test-data.sql
```

## 方法2: 使用达梦JDBC驱动执行

```bash
cd /Users/Zhuanz/dev/hb-code/hbyun-cloud/hbyun-cloud/springboot-hbyunGlobalTreasurer

# 使用Java执行SQL
java -cp ".:target/classes:$HOME/.m2/repository/com/dameng/DmJdbcDriver18/192.0.2.200/DmJdbcDriver18-192.0.2.200.jar" \
  com.dmdb.jdbc.Driver \
  jdbc:dm://192.0.2.200:5236/REDACTED \
  REDACTED \
  REDACTED \
  @src/main/resources/sql/test-data.sql
```

## 方法3: 通过后端应用执行

### 3.1 创建SQL执行类

在后端项目中创建临时的SQL执行类:

```java
package com.global.treasurer.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
public class SqlImportTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    public void importTestData() throws IOException {
        String sqlFile = "src/main/resources/sql/test-data.sql";
        StringBuilder sql = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 跳过注释和空行
                if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                    continue;
                }
                sql.append(line).append("\n");
            }
        }

        // 分割SQL语句并执行
        String[] statements = sql.toString().split(";");
        for (String statement : statements) {
            if (!statement.trim().isEmpty()) {
                try {
                    jdbcTemplate.execute(statement.trim());
                    System.out.println("执行成功: " + statement.substring(0, Math.min(50, statement.length())));
                } catch (Exception e) {
                    System.err.println("执行失败: " + e.getMessage());
                }
            }
        }
    }
}
```

### 3.2 执行测试

```bash
cd /Users/Zhuanz/dev/hb-code/hbyun-cloud/hbyun-cloud/springboot-hbyunGlobalTreasurer

# 运行测试导入数据
mvn test -Dtest=SqlImportTest
```

## 验证数据导入

### 方式1: SQL查询验证

```sql
-- 查询业务系统注册表数据
SELECT COUNT(*) FROM TBL_BUSINESS_SYSTEM_REGISTRY;
-- 应该返回: 8

-- 查询数据映射配置表数据
SELECT COUNT(*) FROM TC_DATA_MAPPING;
-- 应该返回: 8

-- 查询电票账户设置表数据
SELECT COUNT(*) FROM TC_ETICKET_ACCOUNT;
-- 应该返回: 6
```

### 方式2: 查看详细数据

```sql
-- 查看业务系统列表
SELECT ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, CREATE_TIME
FROM TBL_BUSINESS_SYSTEM_REGISTRY
ORDER BY CREATE_TIME DESC;

-- 查看数据映射列表
SELECT ID, MAPPING_NAME, SOURCE_SYSTEM, TARGET_SYSTEM, MAPPING_TYPE, STATUS
FROM TC_DATA_MAPPING
ORDER BY CREATE_TIME DESC;

-- 查看电票账户列表
SELECT ID, BANK_NAME, ACCOUNT_NO, ACCOUNT_TYPE, ACCOUNT_STATUS
FROM TC_ETICKET_ACCOUNT
ORDER BY CREATE_TIME DESC;
```

### 方式3: 通过后端接口验证

启动后端服务后,访问以下接口:

1. 业务系统列表接口:
   ```
   GET http://localhost:8683/financial/basicConfig/system/list?pageNo=1&pageSize=10
   ```

2. 数据映射列表接口:
   ```
   GET http://localhost:8683/financial/basicConfig/mapping/list?pageNo=1&pageSize=10
   ```

3. 电票账户列表接口:
   ```
   GET http://localhost:8683/financial/basicConfig/eTicketAccount/list?pageNo=1&pageSize=10
   ```

## 如果数据已存在

### 删除现有测试数据

```sql
-- 清空测试数据
DELETE FROM TC_ETICKET_ACCOUNT WHERE ID LIKE 'EA%';
DELETE FROM TC_DATA_MAPPING WHERE ID LIKE 'DM%';
DELETE FROM TBL_BUSINESS_SYSTEM_REGISTRY WHERE ID BETWEEN 1001 AND 1008;
COMMIT;

-- 重新执行test-data.sql即可
```

## 故障排查

### 问题1: 连接数据库失败
- 检查网络连接: `ping 192.0.2.200`
- 检查端口开放: `telnet 192.0.2.200 5236`
- 检查防火墙设置

### 问题2: 表不存在
- 确认表名是否正确
- 检查数据库schema
- 查看建表DDL

### 问题3: 权限不足
- 确认用户权限: `GRANT ALL PRIVILEGES TO REDACTED;`
- 联系数据库管理员

## 联系方式

如有问题,请联系:
- 开发团队: dev@example.com
- 技术支持: 400-xxx-xxxx
