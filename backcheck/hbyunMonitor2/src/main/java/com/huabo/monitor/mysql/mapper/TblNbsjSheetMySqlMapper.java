package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblNbsjSheetMySql;
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
public interface TblNbsjSheetMySqlMapper extends BaseMapper<TblNbsjSheetMySql> {

    @Select("SELECT * FROM TBL_NBSJ_SHEET WHERE SHEETID = #{spid}")
    TblNbsjSheetMySql getSpid(BigDecimal spid);


}
