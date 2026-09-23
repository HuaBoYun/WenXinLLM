package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblAttachmentMySql;
import org.apache.ibatis.annotations.Select;

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
public interface TblAttachmentMySqlMapper extends BaseMapper<TblAttachmentMySql> {

    @Select("SELECT * FROM TBL_ATTACHMENT TA LEFT JOIN TBL_NBSJ_AUDITPLANATT TNAT ON TA.ATTID = TNAT.ATTID  " +
            " LEFT JOIN TBL_NBSJ_AUDITPLAN TNA ON TNAT.PLANID = TNA.PLANID WHERE TA.ATTID = #{attid} ")
    com.hbfk.entity.TblAttachment selectAtt(BigDecimal attid);

    @Select("select * from TBL_ATTACHMENT TA LEFT JOIN TBL_NBSJ_SHEETATT TNSH ON TA.ATTID = TNSH.ATTID LEFT JOIN TBL_NBSJ_SHEET TNS ON TNSH.SHEETID = TNS.SHEETID WHERE TNSH.SHEETID = #{id} ")
    List<com.hbfk.entity.TblAttachment> findAllByTblNBSJSheet(String id);
}
