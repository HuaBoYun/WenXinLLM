package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblAuditOptionMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblAuditOptionMySqlMapper extends BaseMapper<TblAuditOptionMySql> {

    @Select("SELECT * FROM TBL_AUDIT_OPTION WHERE OPT_ID = #{planid}")
    List<TblAuditOptionMySql> findOptionByRelationId(BigDecimal planid);

    @SelectProvider(type = TblAuditOptionMapperSqlMySqlConfig.class, method = "OBJlistBySql")
    List<Object[]> OBJlistBySql(String sheetid, Integer cyid);
}
