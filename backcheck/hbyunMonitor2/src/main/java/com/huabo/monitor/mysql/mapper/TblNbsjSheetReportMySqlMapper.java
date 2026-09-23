package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblNbsjSheetReportMySql;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblNbsjSheetReportMySqlMapper extends BaseMapper<TblNbsjSheetReportMySql> {

    @Select("SELECT * FROM TBL_NBSJ_SHEET_REPORT TNSR LEFT JOIN TBL_NBSJ_SHEET TNS ON TNSR.SHEETID = TNS.SHEETID  " +
            " WHERE TNSR.SHEETID = #{sheetid}")
    List<TblNbsjSheetReportMySql> findReportListBySheet(String sheetid);
}
