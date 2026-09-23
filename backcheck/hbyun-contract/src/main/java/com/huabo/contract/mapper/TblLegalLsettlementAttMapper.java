package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblLegalLsettlementAtt;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-21
 */
public interface TblLegalLsettlementAttMapper extends BaseMapper<TblLegalLsettlementAtt> {

    @Delete("DELETE FROM TBL_LEGAL_LSETTLEMENT_ATT WHERE ATTID = #{attid}")
    void deleteRelation(String attid)  throws Exception;

    @Insert("INSERT INTO  TBL_LEGAL_LSETTLEMENT_ATT(LITIGATIONID,ATTID) VALUES (#{litigationid},#{id})")
    void insertAttRelation(BigDecimal litigationid, String id)  throws Exception;
}
