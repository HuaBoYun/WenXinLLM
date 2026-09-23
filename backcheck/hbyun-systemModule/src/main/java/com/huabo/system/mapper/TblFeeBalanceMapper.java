package com.huabo.system.mapper;

import java.math.BigDecimal;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeBalance;

public interface TblFeeBalanceMapper extends BaseMapper<TblFeeBalance> {

    @Select("SELECT HBYUN_FEE_BALANCE_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    @Select("SELECT * FROM TBL_FEE_BALANCE WHERE COMPANY_ORG_ID = #{companyOrgId}")
    TblFeeBalance findByCompanyOrgId(@Param("companyOrgId") BigDecimal companyOrgId);

    @Update("UPDATE TBL_FEE_BALANCE SET BALANCE = #{balance}, TOTAL_CONSUMED = TOTAL_CONSUMED + #{consumeAmount}, UPDATE_TIME = CURRENT_TIMESTAMP WHERE COMPANY_ORG_ID = #{companyOrgId}")
    int deductBalance(@Param("companyOrgId") BigDecimal companyOrgId, @Param("balance") String balance, @Param("consumeAmount") BigDecimal consumeAmount);

    @Update("UPDATE TBL_FEE_BALANCE SET BALANCE = #{balance}, TOTAL_RECHARGED = TOTAL_RECHARGED + #{rechargeAmount}, LAST_RECHARGE_TIME = CURRENT_TIMESTAMP, UPDATE_TIME = CURRENT_TIMESTAMP WHERE COMPANY_ORG_ID = #{companyOrgId}")
    int rechargeBalance(@Param("companyOrgId") BigDecimal companyOrgId, @Param("balance") String balance, @Param("rechargeAmount") BigDecimal rechargeAmount);
}
