package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblAuditOption;
import com.huabo.system.mappersql.TblAuditOptionMapperSqlConfig;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblAuditOptionMapper extends BaseMapper<TblAuditOption> {

    @Select("SELECT * FROM TBL_AUDIT_OPTION WHERE OPT_ID = #{planid}")
    List<TblAuditOption> findOptionByRelationId(BigDecimal planid);

    @SelectProvider(type=TblAuditOptionMapperSqlConfig.class,method="OBJlistBySql")
    List<Object[]> OBJlistBySql(String sheetid, String cyid);
}
