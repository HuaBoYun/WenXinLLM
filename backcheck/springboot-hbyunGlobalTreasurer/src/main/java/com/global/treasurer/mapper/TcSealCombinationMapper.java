package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcSealCombination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 印章组合配置Mapper接口
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@Mapper
public interface TcSealCombinationMapper extends BaseMapper<TcSealCombination> {

    /**
     * 根据条件查询列表
     * 注意:此方法使用 ${sql} 直接拼接SQL,需要确保sql参数经过安全过滤
     *
     * @param sql WHERE条件的SQL片段(不包含WHERE关键字)
     * @return 结果列表
     */
    @Select("<script>" +
            "SELECT COMBINATION_ID AS id, COMBINATION_CODE, COMBINATION_NAME, COMBINATION_TYPE, BUSINESS_TYPE, " +
            "AUTHORITY_LEVEL, MAX_AMOUNT_LIMIT, USAGE_COUNT, REQUIRED_SEALS AS sealList, DESCRIPTION, " +
            "IS_ENABLED AS status, CREATE_USER AS createBy, CREATE_TIME, UPDATE_USER AS updateBy, UPDATE_TIME " +
            "FROM TBL_SEAL_COMBINATION_CONFIG WHERE 1=1 " +
            "<if test='sql != null and sql != \"\"'>${sql}</if> " +
            "ORDER BY CREATE_TIME DESC" +
            "</script>")
    List<TcSealCombination> selectListWithCondition(@Param("sql") String sql);
}