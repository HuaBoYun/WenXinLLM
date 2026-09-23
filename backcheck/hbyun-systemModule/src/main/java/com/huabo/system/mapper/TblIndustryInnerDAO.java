package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblIndustryInner;
import com.huabo.system.entity.TblManageRight;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;


import java.math.BigDecimal;
import java.util.List;

public interface TblIndustryInnerDAO extends BaseMapper<TblIndustryInner> {
    @Select("select * from TBL_INDUSTRY_INNER where ORGID = #{hyid}")
    List<TblIndustryInner> findByList(String hyid);

//    @Delete("DELECT FROM TBL_INDUSTRY_INNER where INDUSTRYID = #{industryid}")
//    void deleteOrgid(TblIndustryInner industryid);

//    @InsertProvider(method="saveTblIndustryInner",type=TblLoginTypeMapperSqlConfig.class)
    @Insert("INSERT INTO TBL_INDUSTRY_INNER (ORGID,INDUSTRYID) VALUES (#{orgid},#{industryid})")
    void saveTblIndustryInner(TblIndustryInner industryInner);
    @Delete("DELETE FROM TBL_INDUSTRY_INNER where ORGID = #{orgid} and INDUSTRYID = #{industryid}")
    void deleteBYii(TblIndustryInner ii);
    @Delete("DELETE FROM TBL_INDUSTRY_INNER where ORGID = #{orgid}")
    void deleteByOrgid(BigDecimal orgid);
}
