package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblNbsjSheetReport;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblNbsjSheetReportMapper extends BaseMapper<TblNbsjSheetReport> {

    @Select("SELECT * FROM TBL_NBSJ_SHEET_REPORT TNSR LEFT JOIN TBL_NBSJ_SHEET TNS ON TNSR.SHEETID = TNS.SHEETID  " +
            " WHERE TNSR.SHEETID = #{sheetid}")
    List<TblNbsjSheetReport> findReportListBySheet(String sheetid);
}
