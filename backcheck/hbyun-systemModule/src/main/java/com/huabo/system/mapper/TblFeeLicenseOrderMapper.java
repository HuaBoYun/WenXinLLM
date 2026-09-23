package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeLicenseOrder;

/**
 * 密钥购买订单Mapper
 */
public interface TblFeeLicenseOrderMapper extends BaseMapper<TblFeeLicenseOrder> {

    @Select("SELECT HBYUN_LICENSE_ORDER_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    @Select("<script>" +
            "SELECT * FROM TBL_FEE_LICENSE_ORDER WHERE 1=1" +
            "<if test='companyOrgId != null'> AND COMPANY_ORG_ID = #{companyOrgId}</if>" +
            "<if test='status != null'> AND STATUS = #{status}</if>" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            " ORDER BY CREATE_TIME DESC" +
            "</script>")
    List<TblFeeLicenseOrder> findOrders(@Param("companyOrgId") BigDecimal companyOrgId,
                                         @Param("status") Integer status,
                                         @Param("startTime") Date startTime,
                                         @Param("endTime") Date endTime);
}
