package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblBiReportMenu;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblBiReportMenuMapper extends BaseMapper<TblBiReportMenu> {

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{id}")
    //@Options(useGeneratedKeys=true, keyProperty="pageid", keyColumn="PAGEID")
    TblBiReportMenu selectId(String id);

//    @Select("select * from TBL_BI_REPORT_MENU where PAGEID = #{pageid}")
//    List get(BigDecimal pageid);

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{id}")
    TblBiReportMenu selectTblBiReportMenu(String idStr);

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{idStr}")
    List<TblBiReportMenu> selectIdStr(String idStr);

    @SelectProvider(method="selectType",type=TblBiReportMenuMapperSqlConfig.class)
    List<TblBiReportMenu> selectType(PageInfo<TblBiReportMenu> pageInfo, String type,BigDecimal orgid);

    @SelectProvider(method="selectOrgid",type=TblBiReportMenuMapperSqlConfig.class)
    Integer selectOrgid(PageInfo<TblBiReportMenu> pageInfo, String type, BigDecimal orgid);

    @SelectProvider(method="selectMenuList",type=TblBiReportMenuMapperSqlConfig.class)
    List<TblBiReportMenu> selectMenuList(PageInfo<TblBiReportMenu> pageInfo, BigDecimal orgid, String type);

    @Select("select count(*) from(select * from TBL_BI_REPORT_MENU TBP WHERE 1=1)")
    Integer selectMenuCount();

    @InsertProvider(method = "insertTbrm",type = TblBiReportMenuMapperSqlConfig.class)
    void insertTbrm(TblBiReportMenu tbrm);

    @Select("select count(*) from TBL_BI_REPORT_MENU where PAGECODE = #{code}")
    Integer selectCode(String code);

    @UpdateProvider(type=TblBiReportMenuMapperSqlConfig.class,method="updateReportMenu")
    void updateReportMenu(TblBiReportMenu page);


    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{pageid}")
    TblBiReportMenu geTblBiReport(BigDecimal pageid);

    @Delete("DELETE FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{pageid}")
    void deletePageId(BigDecimal pageid);

    @InsertProvider(type=TblBiReportMenuMapperSqlConfig.class,method="addPage")
    void addPage(TblBiReportMenu page);
}
