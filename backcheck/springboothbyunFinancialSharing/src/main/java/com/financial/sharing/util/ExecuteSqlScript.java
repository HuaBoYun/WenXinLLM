package com.financial.sharing.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL脚本执行工具
 * 用于执行收入管理模块数据库修复脚本
 */
public class ExecuteSqlScript {
    
    private static final String DB_URL = "jdbc:dm://192.0.2.200:5236/REDACTED";
    private static final String DB_USER = "REDACTED";
    private static final String DB_PASSWORD = "REDACTED";
    private static final String DRIVER_CLASS = "dm.jdbc.driver.DmDriver";
    
    public static void main(String[] args) {
        String sqlFilePath = "doc/财务共享/11.27收入管理/收入管理模块数据库修复脚本.sql";
        
        if (args.length > 0) {
            sqlFilePath = args[0];
        }
        
        System.out.println("==============================================");
        System.out.println("收入管理模块数据库修复脚本执行工具");
        System.out.println("==============================================");
        System.out.println("数据库地址: " + DB_URL);
        System.out.println("脚本文件: " + sqlFilePath);
        System.out.println("==============================================\n");
        
        Connection conn = null;
        Statement stmt = null;
        
        try {
            // 加载驱动
            System.out.println("[1/4] 加载达梦数据库驱动...");
            Class.forName(DRIVER_CLASS);
            System.out.println("✓ 驱动加载成功\n");
            
            // 连接数据库
            System.out.println("[2/4] 连接数据库...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);
            System.out.println("✓ 数据库连接成功\n");
            
            // 读取SQL文件
            System.out.println("[3/4] 读取SQL脚本...");
            List<String> sqlStatements = readSqlFile(sqlFilePath);
            System.out.println("✓ 读取到 " + sqlStatements.size() + " 条SQL语句\n");
            
            // 执行SQL
            System.out.println("[4/4] 执行SQL语句...");
            stmt = conn.createStatement();
            int successCount = 0;
            int errorCount = 0;
            
            for (int i = 0; i < sqlStatements.size(); i++) {
                String sql = sqlStatements.get(i);
                if (sql.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    System.out.println("  执行第 " + (i + 1) + " 条: " + getStatementType(sql));
                    stmt.execute(sql);
                    successCount++;
                    System.out.println("  ✓ 成功");
                } catch (Exception e) {
                    errorCount++;
                    System.out.println("  ✗ 失败: " + e.getMessage());
                    // 如果是DROP TABLE失败（表不存在），继续执行
                    if (!sql.trim().toUpperCase().startsWith("DROP")) {
                        throw e;
                    }
                }
            }
            
            // 提交事务
            conn.commit();
            
            System.out.println("\n==============================================");
            System.out.println("执行完成！");
            System.out.println("成功: " + successCount + " 条");
            System.out.println("失败: " + errorCount + " 条");
            System.out.println("==============================================\n");
            
            // 验证表是否创建成功
            System.out.println("验证表创建情况...");
            verifyTables(conn);
            
        } catch (Exception e) {
            System.err.println("\n✗ 执行失败: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("已回滚事务");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.exit(1);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                System.out.println("\n数据库连接已关闭");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static List<String> readSqlFile(String filePath) throws Exception {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 跳过注释行
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--") || trimmedLine.isEmpty()) {
                    continue;
                }
                
                currentStatement.append(line).append("\n");
                
                // 如果遇到分号，表示一条语句结束
                if (trimmedLine.endsWith(";")) {
                    String sql = currentStatement.toString().trim();
                    if (!sql.isEmpty()) {
                        // 移除末尾的分号
                        sql = sql.substring(0, sql.length() - 1);
                        statements.add(sql);
                    }
                    currentStatement = new StringBuilder();
                }
            }
            
            // 处理最后一条语句（如果没有分号结尾）
            String lastStatement = currentStatement.toString().trim();
            if (!lastStatement.isEmpty()) {
                statements.add(lastStatement);
            }
        }
        
        return statements;
    }
    
    private static String getStatementType(String sql) {
        String upperSql = sql.trim().toUpperCase();
        if (upperSql.startsWith("DROP")) return "DROP TABLE";
        if (upperSql.startsWith("CREATE TABLE")) return "CREATE TABLE";
        if (upperSql.startsWith("CREATE INDEX")) return "CREATE INDEX";
        if (upperSql.startsWith("COMMENT")) return "COMMENT";
        if (upperSql.startsWith("COMMIT")) return "COMMIT";
        return "OTHER";
    }
    
    private static void verifyTables(Connection conn) throws Exception {
        String[] tables = {
            "T_REVENUE_CONTRACT",
            "T_REVENUE_RECOGNITION", 
            "T_REVENUE_ALLOCATION",
            "T_DEFERRED_REVENUE",
            "T_REVENUE_ADJUSTMENT"
        };
        
        Statement stmt = conn.createStatement();
        for (String table : tables) {
            try {
                stmt.executeQuery("SELECT COUNT(*) FROM " + table);
                System.out.println("  ✓ " + table + " 存在");
            } catch (Exception e) {
                System.out.println("  ✗ " + table + " 不存在");
            }
        }
        stmt.close();
    }
}

