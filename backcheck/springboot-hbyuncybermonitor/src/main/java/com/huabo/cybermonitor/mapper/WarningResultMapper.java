package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.WarningResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface WarningResultMapper extends BaseMapper<WarningResult> {


    @Select("select wr.* from TBL_WARNING_RESULT wr where ROWNUM = 1 order by wr.WARNINGID desc")
    List<WarningResult> getLast();
}
