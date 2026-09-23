package com.global.treasurer.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 达梦数据库DECIMAL类型转换处理器
 * 解决达梦数据库DECIMAL类型与Java Long类型之间的转换异常
 *
 * @author 华博云开发团队
 * @since 2024-12-24
 */
@MappedTypes({Long.class, long.class})
public class DmDecimalTypeHandler extends BaseTypeHandler<Long> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        // 将Long类型转换为BigDecimal设置到PreparedStatement
        ps.setBigDecimal(i, new BigDecimal(parameter));
    }

    @Override
    public Long getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从ResultSet中获取DECIMAL值并转换为Long
        BigDecimal decimal = rs.getBigDecimal(columnName);
        return decimal == null ? null : decimal.longValue();
    }

    @Override
    public Long getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从ResultSet中根据列索引获取DECIMAL值并转换为Long
        BigDecimal decimal = rs.getBigDecimal(columnIndex);
        return decimal == null ? null : decimal.longValue();
    }

    @Override
    public Long getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从CallableStatement中获取DECIMAL值并转换为Long
        BigDecimal decimal = cs.getBigDecimal(columnIndex);
        return decimal == null ? null : decimal.longValue();
    }
}
