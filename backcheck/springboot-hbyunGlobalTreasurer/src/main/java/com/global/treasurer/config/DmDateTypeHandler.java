package com.global.treasurer.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * 达梦数据库DATE类型转换处理器
 * 解决达梦数据库DATE类型与Java LocalDate类型之间的转换异常
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@MappedTypes(LocalDate.class)
public class DmDateTypeHandler extends BaseTypeHandler<LocalDate> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        // 将LocalDateTime转换为Date设置到PreparedStatement
        ps.setDate(i, Date.valueOf(parameter));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从ResultSet中获取DATE值并转换为LocalDate
        Date date = rs.getDate(columnName);
        return date == null ? null : date.toLocalDate();
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从ResultSet中根据列索引获取DATE值并转换为LocalDate
        Date date = rs.getDate(columnIndex);
        return date == null ? null : date.toLocalDate();
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从CallableStatement中获取DATE值并转换为LocalDate
        Date date = cs.getDate(columnIndex);
        return date == null ? null : date.toLocalDate();
    }
}
