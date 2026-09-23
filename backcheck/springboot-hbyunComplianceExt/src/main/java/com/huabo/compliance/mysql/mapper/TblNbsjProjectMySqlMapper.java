package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblNbsjProjectMySql;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblNbsjProjectMySqlMapper extends BaseMapper<TblNbsjProjectMySql> {

    @Select("SELECT * from TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_NBSJ_SHEET TNS ON TNP.PROJECTID = TNS.PROJECTID WHERE TNS.SHEETID = #{spid} ")
    TblNbsjProjectMySql findBySheetid(BigDecimal spid);
}
