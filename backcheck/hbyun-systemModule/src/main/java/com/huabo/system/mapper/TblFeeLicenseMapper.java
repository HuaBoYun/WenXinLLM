package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeLicense;

public interface TblFeeLicenseMapper extends BaseMapper<TblFeeLicense> {

    @Select("SELECT HBYUN_FEE_LICENSE_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    @Select("SELECT * FROM TBL_FEE_LICENSE WHERE LICENSE_KEY_HASH = #{licenseKeyHash}")
    TblFeeLicense findByLicenseKeyHash(@Param("licenseKeyHash") String licenseKeyHash);

    @Select("SELECT * FROM TBL_FEE_LICENSE WHERE COMPANY_ORG_ID = #{companyOrgId} AND USE_STATUS = 1 ORDER BY USE_TIME DESC")
    List<TblFeeLicense> findUsedByCompany(@Param("companyOrgId") BigDecimal companyOrgId);

    @Update("UPDATE TBL_FEE_LICENSE SET USE_STATUS = 1, USE_TIME = CURRENT_TIMESTAMP WHERE ID = #{id} AND USE_STATUS = 0")
    int markAsUsed(@Param("id") BigDecimal id);
}
