package com.huabo.bigmodel.service;

import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SqlExecuteService 单元测试
 * 不依赖 Spring 容器，使用反射测试私有方法
 *
 * @author AI Test Generator
 * @date 2026-02-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SqlExecuteService 单元测试")
public class SqlExecuteServiceUnitTest {

    @Mock
    private JdbcTemplate dmJdbcTemplate;

    @Mock
    private JdbcTemplate mysqlJdbcTemplate;

    private SqlExecuteService sqlExecuteService;

    @BeforeEach
    void setUp() throws Exception {
        // 通过反射构造，绕过 @Qualifier
        java.lang.reflect.Constructor<SqlExecuteService> ctor =
                SqlExecuteService.class.getDeclaredConstructor(JdbcTemplate.class, JdbcTemplate.class);
        sqlExecuteService = ctor.newInstance(dmJdbcTemplate, mysqlJdbcTemplate);
    }

    // ================================================================
    // SQL 类型检测
    // ================================================================
    @Nested
    @DisplayName("SQL 类型自动检测 - detectSqlType")
    class DetectSqlType {

        private String invokeDetectSqlType(SqlExecuteRequest request) throws Exception {
            Method method = SqlExecuteService.class.getDeclaredMethod("detectSqlType", SqlExecuteRequest.class);
            method.setAccessible(true);
            return (String) method.invoke(sqlExecuteService, request);
        }

        @Test
        @DisplayName("TC-SQL-001: CREATE TABLE 应识别为 DDL")
        void testCreateTableIsDdl() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("CREATE TABLE TBL_TEST (ID INT PRIMARY KEY)")
                    .autoDetect(true).build();
            assertEquals("DDL", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-002: ALTER TABLE 应识别为 DDL")
        void testAlterTableIsDdl() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("ALTER TABLE TBL_TEST ADD COLUMN NAME VARCHAR(50)")
                    .autoDetect(true).build();
            assertEquals("DDL", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-003: DROP TABLE 应识别为 DDL")
        void testDropTableIsDdl() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("DROP TABLE TBL_TEST")
                    .autoDetect(true).build();
            assertEquals("DDL", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-004: INSERT INTO 应识别为 DML")
        void testInsertIsDml() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("INSERT INTO TBL_TEST VALUES (1, 'test')")
                    .autoDetect(true).build();
            assertEquals("DML", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-005: UPDATE 应识别为 DML")
        void testUpdateIsDml() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("UPDATE TBL_TEST SET NAME = 'new' WHERE ID = 1")
                    .autoDetect(true).build();
            assertEquals("DML", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-006: DELETE 应识别为 DML")
        void testDeleteIsDml() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("DELETE FROM TBL_TEST WHERE ID = 1")
                    .autoDetect(true).build();
            assertEquals("DML", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-007: SELECT 应识别为 QUERY")
        void testSelectIsQuery() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("SELECT * FROM TBL_TEST WHERE ID > 0")
                    .autoDetect(true).build();
            assertEquals("QUERY", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-008: SHOW TABLES 应识别为 QUERY")
        void testShowIsQuery() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("SHOW TABLES")
                    .autoDetect(true).build();
            assertEquals("QUERY", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-009: 前导空格不影响检测")
        void testLeadingWhitespace() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("   SELECT 1 FROM DUAL")
                    .autoDetect(true).build();
            assertEquals("QUERY", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-010: 小写 SQL 应正确识别")
        void testLowerCaseSql() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("select * from tbl_test")
                    .autoDetect(true).build();
            assertEquals("QUERY", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-011: 未知 SQL 类型应返回 UNKNOWN")
        void testUnknownSqlType() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("GRANT ALL ON TBL_TEST TO user1")
                    .autoDetect(true).build();
            assertEquals("UNKNOWN", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-012: 指定 sqlType 且 autoDetect=false 时应直接返回指定类型")
        void testExplicitSqlType() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("SELECT * FROM TBL_TEST")
                    .sqlType("DDL")
                    .autoDetect(false).build();
            assertEquals("DDL", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-013: TRUNCATE 应识别为 DDL")
        void testTruncateIsDdl() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("TRUNCATE TABLE TBL_TEST")
                    .autoDetect(true).build();
            assertEquals("DDL", invokeDetectSqlType(req));
        }

        @Test
        @DisplayName("TC-SQL-014: EXPLAIN 应识别为 QUERY")
        void testExplainIsQuery() throws Exception {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("EXPLAIN SELECT * FROM TBL_TEST")
                    .autoDetect(true).build();
            assertEquals("QUERY", invokeDetectSqlType(req));
        }
    }

    // ================================================================
    // 危险操作检测
    // ================================================================
    @Nested
    @DisplayName("危险操作检测 - isDangerousOperation")
    class DangerousOperation {

        private boolean invokeIsDangerous(String sql) throws Exception {
            Method method = SqlExecuteService.class.getDeclaredMethod("isDangerousOperation", String.class);
            method.setAccessible(true);
            return (boolean) method.invoke(sqlExecuteService, sql);
        }

        @Test
        @DisplayName("TC-DANGER-001: DROP TABLE 应被识别为危险操作")
        void testDropIsDangerous() throws Exception {
            assertTrue(invokeIsDangerous("DROP TABLE TBL_TEST"));
        }

        @Test
        @DisplayName("TC-DANGER-002: TRUNCATE TABLE 应被识别为危险操作")
        void testTruncateIsDangerous() throws Exception {
            assertTrue(invokeIsDangerous("TRUNCATE TABLE TBL_TEST"));
        }

        @Test
        @DisplayName("TC-DANGER-003: DELETE FROM 应被识别为危险操作")
        void testDeleteIsDangerous() throws Exception {
            assertTrue(invokeIsDangerous("DELETE FROM TBL_TEST WHERE ID = 1"));
        }

        @Test
        @DisplayName("TC-DANGER-004: SELECT 不应被识别为危险操作")
        void testSelectIsNotDangerous() throws Exception {
            assertFalse(invokeIsDangerous("SELECT * FROM TBL_TEST"));
        }

        @Test
        @DisplayName("TC-DANGER-005: INSERT 不应被识别为危险操作")
        void testInsertIsNotDangerous() throws Exception {
            assertFalse(invokeIsDangerous("INSERT INTO TBL_TEST VALUES (1)"));
        }

        @Test
        @DisplayName("TC-DANGER-006: CREATE TABLE 不应被识别为危险操作")
        void testCreateIsNotDangerous() throws Exception {
            assertFalse(invokeIsDangerous("CREATE TABLE TBL_TEST (ID INT)"));
        }

        @Test
        @DisplayName("TC-DANGER-007: null 输入不应被识别为危险操作")
        void testNullIsNotDangerous() throws Exception {
            assertFalse(invokeIsDangerous(null));
        }

        @Test
        @DisplayName("TC-DANGER-008: 空字符串不应被识别为危险操作")
        void testEmptyIsNotDangerous() throws Exception {
            assertFalse(invokeIsDangerous(""));
            assertFalse(invokeIsDangerous("   "));
        }
    }

    // ================================================================
    // 危险操作拦截（集成 executeSql 入口）
    // ================================================================
    @Nested
    @DisplayName("executeSql 安全拦截")
    class ExecuteSqlSafety {

        @Test
        @DisplayName("TC-EXEC-001: DROP 语句应被拦截并返回失败")
        void testDropBlocked() {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("DROP TABLE TBL_TEST")
                    .dbType("dm").autoDetect(true).build();
            SqlExecuteResponse resp = sqlExecuteService.executeSql(req);
            assertFalse(resp.getSuccess());
            assertTrue(resp.getMessage().contains("安全限制"));
        }

        @Test
        @DisplayName("TC-EXEC-002: DELETE 语句应被拦截并返回失败")
        void testDeleteBlocked() {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("DELETE FROM TBL_TEST")
                    .dbType("dm").autoDetect(true).build();
            SqlExecuteResponse resp = sqlExecuteService.executeSql(req);
            assertFalse(resp.getSuccess());
            assertTrue(resp.getMessage().contains("安全限制"));
        }

        @Test
        @DisplayName("TC-EXEC-003: TRUNCATE 语句应被拦截并返回失败")
        void testTruncateBlocked() {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("TRUNCATE TABLE TBL_TEST")
                    .dbType("dm").autoDetect(true).build();
            SqlExecuteResponse resp = sqlExecuteService.executeSql(req);
            assertFalse(resp.getSuccess());
            assertTrue(resp.getMessage().contains("安全限制"));
        }

        @Test
        @DisplayName("TC-EXEC-004: UNKNOWN 类型 SQL 应返回不支持")
        void testUnknownTypeBlocked() {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("GRANT ALL ON TBL_TEST TO user1")
                    .dbType("dm").autoDetect(true).build();
            SqlExecuteResponse resp = sqlExecuteService.executeSql(req);
            assertFalse(resp.getSuccess());
            assertTrue(resp.getMessage().contains("不支持的SQL类型"));
        }
    }

    // ================================================================
    // 数据源选择
    // ================================================================
    @Nested
    @DisplayName("数据源选择 - selectDataSource")
    class SelectDataSource {

        private JdbcTemplate invokeSelectDataSource(String dbType) throws Exception {
            Method method = SqlExecuteService.class.getDeclaredMethod("selectDataSource", String.class);
            method.setAccessible(true);
            return (JdbcTemplate) method.invoke(sqlExecuteService, dbType);
        }

        @Test
        @DisplayName("TC-DS-001: dbType=dm 应返回达梦数据源")
        void testDmDataSource() throws Exception {
            assertSame(dmJdbcTemplate, invokeSelectDataSource("dm"));
        }

        @Test
        @DisplayName("TC-DS-002: dbType=mysql 应返回 MySQL 数据源")
        void testMysqlDataSource() throws Exception {
            assertSame(mysqlJdbcTemplate, invokeSelectDataSource("mysql"));
        }

        @Test
        @DisplayName("TC-DS-003: dbType=MYSQL（大写）应返回 MySQL 数据源")
        void testMysqlUpperCase() throws Exception {
            assertSame(mysqlJdbcTemplate, invokeSelectDataSource("MYSQL"));
        }

        @Test
        @DisplayName("TC-DS-004: dbType=null 应默认返回达梦数据源")
        void testNullDefaultToDm() throws Exception {
            assertSame(dmJdbcTemplate, invokeSelectDataSource(null));
        }

        @Test
        @DisplayName("TC-DS-005: dbType=unknown 应默认返回达梦数据源")
        void testUnknownDefaultToDm() throws Exception {
            assertSame(dmJdbcTemplate, invokeSelectDataSource("oracle"));
        }
    }

    // ================================================================
    // DDL 语句分割
    // ================================================================
    @Nested
    @DisplayName("DDL 语句分割 - splitDdlStatements")
    class SplitDdl {

        @SuppressWarnings("unchecked")
        private java.util.List<String> invokeSplit(String sql) throws Exception {
            Method method = SqlExecuteService.class.getDeclaredMethod("splitDdlStatements", String.class);
            method.setAccessible(true);
            return (java.util.List<String>) method.invoke(sqlExecuteService, sql);
        }

        @Test
        @DisplayName("TC-SPLIT-001: 单条 DDL 应返回 1 个元素")
        void testSingleStatement() throws Exception {
            java.util.List<String> result = invokeSplit("CREATE TABLE TBL_A (ID INT);");
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("TC-SPLIT-002: 分号分隔的多条 DDL 应正确分割")
        void testMultipleStatements() throws Exception {
            String sql = "CREATE TABLE TBL_A (ID INT);\nCREATE TABLE TBL_B (ID INT);";
            java.util.List<String> result = invokeSplit(sql);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("TC-SPLIT-003: 注释行应被跳过")
        void testCommentSkipped() throws Exception {
            String sql = "-- 创建表A\nCREATE TABLE TBL_A (ID INT);\n-- 创建表B\nCREATE TABLE TBL_B (ID INT);";
            java.util.List<String> result = invokeSplit(sql);
            assertEquals(2, result.size());
            assertFalse(result.get(0).contains("--"));
        }

        @Test
        @DisplayName("TC-SPLIT-004: 无分号的单条 DDL 应作为整体返回")
        void testNoSemicolon() throws Exception {
            java.util.List<String> result = invokeSplit("CREATE TABLE TBL_A (ID INT)");
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("TC-SPLIT-005: 空行应被忽略")
        void testEmptyLinesIgnored() throws Exception {
            String sql = "\n\nCREATE TABLE TBL_A (ID INT);\n\n\n";
            java.util.List<String> result = invokeSplit(sql);
            assertEquals(1, result.size());
        }
    }

    // ================================================================
    // 表名提取
    // ================================================================
    @Nested
    @DisplayName("表名提取 - extractTableName")
    class ExtractTableName {

        private String invokeExtract(String ddl) throws Exception {
            Method method = SqlExecuteService.class.getDeclaredMethod("extractTableName", String.class);
            method.setAccessible(true);
            return (String) method.invoke(sqlExecuteService, ddl);
        }

        @Test
        @DisplayName("TC-NAME-001: CREATE TABLE 应提取表名")
        void testCreateTable() throws Exception {
            assertEquals("TBL_USER_INFO", invokeExtract("CREATE TABLE TBL_USER_INFO (ID INT)"));
        }

        @Test
        @DisplayName("TC-NAME-002: 无 TABLE 关键字应返回 '未知表'")
        void testNoTableKeyword() throws Exception {
            assertEquals("未知表", invokeExtract("GRANT ALL ON schema1"));
        }
    }
}

