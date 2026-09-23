package com.huabo.contract.mapper;

import org.apache.ibatis.annotations.Delete;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblLegalArbitrationAtt;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
public interface TblLegalArbitrationAttMapper extends BaseMapper<TblLegalArbitrationAtt> {

    @Delete("DELETE FROM TBL_LEGAL_ARBITRATION_ATT WHERE ATTID = #{attid}")
    void deleteRelation(String attid);
}
