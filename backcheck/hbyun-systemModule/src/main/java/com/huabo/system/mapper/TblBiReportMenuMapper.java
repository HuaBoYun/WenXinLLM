package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.mappersql.TblBiReportMenuMapperSqlConfig;

public interface TblBiReportMenuMapper extends BaseMapper<TblBiReportMenu> {

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{id}")
    TblBiReportMenu selectId(String id);


    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{id}")
    TblBiReportMenu selectTblBiReportMenu(String idStr);

    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEID = #{idStr}")
    List<TblBiReportMenu> selectIdStr(String idStr);
    
    @Select("SELECT * FROM TBL_BI_REPORT_MENU WHERE PAGEBODY = #{idStr}")
    List<TblBiReportMenu> selectListByFatherId(String pageId);

    @SelectProvider(method="selectType",type=TblBiReportMenuMapperSqlConfig.class)
    IPage<TblBiReportMenu> selectType(IPage<TblBiReportMenu> page, String type,BigDecimal orgid);

    @SelectProvider(method="selectMenuList",type=TblBiReportMenuMapperSqlConfig.class)
    IPage<TblBiReportMenu> selectMenuList(IPage<TblBiReportMenu> page, BigDecimal orgid, String type);

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
    
    @Delete("DELETE FROM TBL_BI_REPORT_MENU WHERE PAGEBODY = #{pageid}")
    void deleteByFatherId(BigDecimal pageid);

    @InsertProvider(type=TblBiReportMenuMapperSqlConfig.class,method="addPage")
    void addPage(TblBiReportMenu page);

    @SelectProvider(method="selectFirstRpeortListByModuleType",type=TblBiReportMenuMapperSqlConfig.class)
	List<TblBiReportMenu> selectFirstRpeortListByModuleType(String moduleType, BigDecimal orgid,BigDecimal pageId);

    @SelectProvider(method="selectFirstRpeortListByPerson",type=TblBiReportMenuMapperSqlConfig.class)
	List<TblBiReportMenu> selectFirstRpeortListByPerson(BigDecimal staffid,BigDecimal pageId);

}
