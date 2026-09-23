package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.entity.TblOrganization;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


public interface OrganizationServiceMapper extends BaseMapper<TblOrganization> {


    @Select("SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    public Integer selectUniqueColumn(BigDecimal orgid);


    @Select("select * from tbl_organization where orgid =#{orgid} and status = 0 ORDER BY orderid ASC")
    TblOrganization loadTblOrganization(BigDecimal orgid);



    @Select("select * from tbl_organization where orgid =#{orgid} and status = 0 ORDER BY orderid ASC")
    List<TblOrganization> loadTblOrganizationByPid(BigDecimal orgid);




}
