package com.huabo.contract.mapper;

import org.apache.ibatis.annotations.Delete;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblLegalNegotiateeAtt;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-18
 */
public interface TblLegalNegotiateeAttMapper extends BaseMapper<TblLegalNegotiateeAtt> {

    @Delete("DELETE FROM TBL_LEGAL_NEGOTIATEE_ATT WHERE ATTID = #{attid}")
    void deleteRelation(String attid) throws Exception;
}
