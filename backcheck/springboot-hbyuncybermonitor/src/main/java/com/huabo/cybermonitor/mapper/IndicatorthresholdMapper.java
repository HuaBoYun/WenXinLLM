package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Indicatorthreshold;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface IndicatorthresholdMapper extends BaseMapper<Indicatorthreshold> {

    @Select("select * from TBL_INDICATORTHRESHOLD where INDICATORID = #{indicatorid} ORDER BY SEQUENCENUMBER ")
    List<Indicatorthreshold> QueryByIndicatorId(@Param("indicatorid") int indicatorid);
}
