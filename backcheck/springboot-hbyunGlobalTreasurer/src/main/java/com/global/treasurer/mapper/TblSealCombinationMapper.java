package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSealCombination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 印鉴组合配置Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
@Mapper
public interface TblSealCombinationMapper extends BaseMapper<TblSealCombination> {

    /**
     * 根据条件查询列表
     * 注意:此方法使用 ${sql} 直接拼接SQL,需要确保sql参数经过安全过滤
     *
     * @param sql WHERE条件的SQL片段(不包含WHERE关键字)
     * @return 结果列表
     */
    @Select("<script>" +
            "SELECT ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, " +
            "STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK " +
            "FROM TBL_SEAL_COMBINATION WHERE 1=1 " +
            "<if test='sql != null and sql != \"\"'>${sql}</if>" +
            "ORDER BY CREATE_TIME DESC" +
            "</script>")
    List<TblSealCombination> selectListWithCondition(@Param("sql") String sql);
}
