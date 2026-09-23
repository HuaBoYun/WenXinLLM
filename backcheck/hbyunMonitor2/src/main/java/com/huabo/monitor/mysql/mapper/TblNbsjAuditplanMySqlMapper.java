package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblNbsjAuditplanMySql;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblNbsjAuditplanMySqlMapper extends BaseMapper<TblNbsjAuditplanMySql> {

    @Select("SELECT * FROM TBL_NBSJ_AUDITPLAN TNA LEFT JOIN TBL_NBSJ_AUDITPLANATT TNAT ON TNA.PLANID = TNAT.PLANID LEFT JOIN TBL_ATTACHMENT TA ON TNAT.ATTID = TA.ATTID  LEFT JOIN TBL_STAFF TS ON TNA.PRINCIPALID = TS.STAFFID \n" +
            "WHERE TNA.PLANID = #{planid}")
    TblNbsjAuditplanMySql find(String planid);

    @Select("SELECT * FROM TBL_NBSJ_AUDITPLAN WHERE PLANID = #{planid}")
    List<TblNbsjAuditplanMySql> findCheckJH(String planid);
}
