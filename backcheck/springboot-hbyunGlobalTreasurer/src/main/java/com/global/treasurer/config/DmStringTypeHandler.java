package com.global.treasurer.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 达梦数据库 String/VARCHAR 类型处理器
 * 解决达梦 JDBC 驱动对 setString() 调用报错的兼容性问题：
 * 使用 ps.setObject(i, value, Types.VARCHAR) 代替 ps.setString(i, value)
 *
 * @author 华博云开发团队
 * @since 2026-03-17
 */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class DmStringTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        // 达梦驱动对 setString 有兼容性问题，改用 setObject 指定 Types.VARCHAR
        ps.setObject(i, parameter, Types.VARCHAR);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}

