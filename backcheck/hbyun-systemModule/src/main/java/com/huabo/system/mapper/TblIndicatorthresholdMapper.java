package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblIndicatorthreshold;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblIndicatorthresholdMapper extends BaseMapper<TblIndicatorthreshold> {

    @Select("SELECT * FROM TBL_INDICATORTHRESHOLD WHERE THRESHOLDID = #{thresholdid} ORDER BY SEQUENCENUMBER")
    List<TblIndicatorthreshold> findByIndicatorId(String thresholdid);
}
