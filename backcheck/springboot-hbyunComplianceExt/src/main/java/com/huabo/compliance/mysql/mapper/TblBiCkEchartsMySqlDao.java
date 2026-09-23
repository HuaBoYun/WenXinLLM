package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblBiCkEchartsMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblBiCkEchartsMySqlDao extends BaseMapper<TblBiCkEchartsMySql> {
//    @Select("SELECT * FROM TBL_BI_CK_ECHARTS")
//    Object tblBiCkEchartsDao(String tblBiReportMenu, TblBiReportMenu page);

    @Select("SELECT * FROM TBL_BI_CK_ECHARTS WHERE PAGEID= #{pageid} ")
    List<TblBiCkEchartsMySql> listBySql(String pageid);

    @Delete("DELETE FROM TBL_BI_CK_ECHARTS WHERE CHARTID = #{pageid}")
    void deletePageId(BigDecimal pageid);
}
