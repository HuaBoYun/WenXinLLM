package com.huabo.contract.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 达梦数据库Integer类型处理器
 * 解决达梦数据库NULL值转换Integer时的类型转换异常
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@MappedTypes({Integer.class})
@MappedJdbcTypes({JdbcType.INTEGER, JdbcType.NUMERIC, JdbcType.DECIMAL})
public class DamengIntegerTypeHandler extends BaseTypeHandler<Integer> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter);
    }

    @Override
    public Integer getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            // 先检查是否为NULL
            Object value = rs.getObject(columnName);
            if (value == null || rs.wasNull()) {
                return null;
            }
            
            // 如果是数字类型，转换为Integer
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            
            // 如果是字符串，尝试解析
            if (value instanceof String) {
                String strValue = ((String) value).trim();
                if (strValue.isEmpty()) {
                    return null;
                }
                try {
                    return Integer.valueOf(strValue);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            
            // 其他情况返回null
            return null;
        } catch (Exception e) {
            // 如果出现任何异常，返回null而不是抛出异常
            return null;
        }
    }

    @Override
    public Integer getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            // 先检查是否为NULL
            Object value = rs.getObject(columnIndex);
            if (value == null || rs.wasNull()) {
                return null;
            }
            
            // 如果是数字类型，转换为Integer
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            
            // 如果是字符串，尝试解析
            if (value instanceof String) {
                String strValue = ((String) value).trim();
                if (strValue.isEmpty()) {
                    return null;
                }
                try {
                    return Integer.valueOf(strValue);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            
            // 其他情况返回null
            return null;
        } catch (Exception e) {
            // 如果出现任何异常，返回null而不是抛出异常
            return null;
        }
    }

    @Override
    public Integer getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            // 先检查是否为NULL
            Object value = cs.getObject(columnIndex);
            if (value == null || cs.wasNull()) {
                return null;
            }
            
            // 如果是数字类型，转换为Integer
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            
            // 如果是字符串，尝试解析
            if (value instanceof String) {
                String strValue = ((String) value).trim();
                if (strValue.isEmpty()) {
                    return null;
                }
                try {
                    return Integer.valueOf(strValue);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            
            // 其他情况返回null
            return null;
        } catch (Exception e) {
            // 如果出现任何异常，返回null而不是抛出异常
            return null;
        }
    }
}
