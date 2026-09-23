package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.entity.TblIndicatorthreshold;
import com.huabo.compliance.mysql.entity.TblIndicatorthresholdMySql;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblIndicatorthresholdMySqlMapper extends BaseMapper<TblIndicatorthresholdMySql> {

    @Select("SELECT * FROM TBL_INDICATORTHRESHOLD WHERE THRESHOLDID = #{thresholdid} ORDER BY SEQUENCENUMBER")
    List<TblIndicatorthreshold> findByIndicatorId(String thresholdid);
}
