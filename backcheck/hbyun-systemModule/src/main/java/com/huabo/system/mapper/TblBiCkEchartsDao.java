package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblBiCkEcharts;


public interface TblBiCkEchartsDao extends BaseMapper<TblBiCkEcharts> {
//    @Select("SELECT * FROM TBL_BI_CK_ECHARTS")
//    Object tblBiCkEchartsDao(String tblBiReportMenu, TblBiReportMenu page);

    @Select("SELECT * FROM TBL_BI_CK_ECHARTS WHERE PAGEID= #{pageid} ")
    List<TblBiCkEcharts> listBySql(String pageid);

    @Delete("DELETE FROM TBL_BI_CK_ECHARTS WHERE CHARTID = #{pageid}")
    void deletePageId(BigDecimal pageid);
}
