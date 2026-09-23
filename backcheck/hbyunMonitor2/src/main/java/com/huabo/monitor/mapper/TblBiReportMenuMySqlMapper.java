package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblBiReportMenuMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblBiReportMenuMySqlMapper extends BaseMapper<TblBiReportMenuMySql> {

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{id}")
        //@Options(useGeneratedKeys=true, keyProperty="pageid", keyColumn="PAGEID")
    TblBiReportMenuMySql selectId(String id);

//    @Select("select * from TBL_BI_REPORT_MENU where PAGEID = #{pageid}")
//    List get(BigDecimal pageid);

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{idStr}")
    TblBiReportMenuMySql selectTblBiReportMenu(String idStr);

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{idStr}")
    List<TblBiReportMenuMySql> selectIdStr(String idStr);

    @SelectProvider(method = "selectType", type = TblBiReportMenuMapperSqlMySqlConfig.class)
    List<TblBiReportMenuMySql> selectType(PageInfo<TblBiReportMenuMySql> pageInfo, String type, BigDecimal orgid);

    @SelectProvider(method = "selectOrgid", type = TblBiReportMenuMapperSqlMySqlConfig.class)
    Integer selectOrgid(PageInfo<TblBiReportMenuMySql> pageInfo, String type, BigDecimal orgid);

    @SelectProvider(method = "selectMenuList", type = TblBiReportMenuMapperSqlMySqlConfig.class)
    List<TblBiReportMenuMySql> selectMenuList(PageInfo<TblBiReportMenuMySql> pageInfo, BigDecimal orgid, String type);

    @Select("select count(*) from(select * from TBL_BI_REPORT_MENU TBP WHERE 1=1)")
    Integer selectMenuCount();

    @InsertProvider(method = "insertTbrm", type = TblBiReportMenuMapperSqlMySqlConfig.class)
    void insertTbrm(TblBiReportMenuMySql tbrm);

    @Select("select count(*) from TBL_BI_REPORT_MENU where PAGECODE = #{code}")
    Integer selectCode(String code);

    @UpdateProvider(type = TblBiReportMenuMapperSqlMySqlConfig.class, method = "updateReportMenu")
    void updateReportMenu(TblBiReportMenuMySql page);


    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{pageid}")
    TblBiReportMenuMySql geTblBiReport(BigDecimal pageid);

    @Delete("DELETE FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{pageid}")
    void deletePageId(BigDecimal pageid);

    @InsertProvider(type = TblBiReportMenuMapperSqlMySqlConfig.class, method = "addPage")
    void addPage(TblBiReportMenuMySql page);
}
