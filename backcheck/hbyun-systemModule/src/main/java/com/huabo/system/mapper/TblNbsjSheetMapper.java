package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblNbsjSheet;
import com.huabo.system.entity.TblNbsjSheetReport;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblNbsjSheetMapper extends BaseMapper<TblNbsjSheet> {


    @Select("SELECT * FROM TBL_NBSJ_SHEET WHERE SHEETID = #{spid}")
    TblNbsjSheet getSpid(BigDecimal spid);


}
