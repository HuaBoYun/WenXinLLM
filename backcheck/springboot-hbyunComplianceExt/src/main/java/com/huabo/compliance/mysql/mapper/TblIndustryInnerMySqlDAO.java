package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblIndustryInnerMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblIndustryInnerMySqlDAO extends BaseMapper<TblIndustryInnerMySql> {
    @Select("select * from TBL_INDUSTRY_INNER where ORGID = #{hyid}")
    List<TblIndustryInnerMySql> findByList(String hyid);

//    @Delete("DELECT FROM TBL_INDUSTRY_INNER where INDUSTRYID = #{industryid}")
//    void deleteOrgid(TblIndustryInner industryid);

    //    @InsertProvider(method="saveTblIndustryInner",type=TblLoginTypeMapperSqlConfig.class)
    @Insert("INSERT INTO TBL_INDUSTRY_INNER (ORGID,INDUSTRYID) VALUES (#{orgid},#{industryid})")
    void saveTblIndustryInner(TblIndustryInnerMySql industryInner);

    @Delete("DELETE FROM TBL_INDUSTRY_INNER where ORGID = #{orgid} and INDUSTRYID = #{industryid}")
    void deleteBYii(TblIndustryInnerMySql ii);

    @Delete("DELETE FROM TBL_INDUSTRY_INNER where ORGID = #{orgid}")
    void deleteByOrgid(BigDecimal orgid);
}
