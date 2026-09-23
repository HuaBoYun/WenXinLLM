package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblRiskAttWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-13
 */
public interface TblRiskAttWordMapper extends BaseMapper<TblRiskAttWord> {


    @Select("${sql}")
    List<TblRiskAttWord> queryList(@Param("sql") String sql);

}
