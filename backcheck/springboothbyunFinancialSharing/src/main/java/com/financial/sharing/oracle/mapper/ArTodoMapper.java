package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.ArTodoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 应收待办事项Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArTodoMapper extends BaseMapper<ArTodoEntity> {

    /**
     * 查询待办事项列表
     */
    List<ArTodoEntity> selectTodoList(@Param("handlerId") String handlerId, 
                                       @Param("todoStatus") Integer todoStatus,
                                       @Param("tenantId") Long tenantId);

    /**
     * 根据业务ID查询待办
     */
    ArTodoEntity selectByBusinessId(@Param("businessType") String businessType, 
                                     @Param("businessId") String businessId);

    /**
     * 批量插入待办事项
     */
    int batchInsert(@Param("todos") List<ArTodoEntity> todos);

    /**
     * 更新待办状态
     */
    int updateTodoStatus(@Param("todoId") String todoId, @Param("status") Integer status);

    /**
     * 根据业务ID更新待办状态
     */
    int updateStatusByBusinessId(@Param("businessType") String businessType, 
                                  @Param("businessId") String businessId,
                                  @Param("status") Integer status);

    /**
     * 查询待办统计
     */
    Map<String, Object> selectTodoStatistics(@Param("handlerId") String handlerId, @Param("tenantId") Long tenantId);

    /**
     * 删除已处理的待办（清理）
     */
    int deleteProcessedTodos(@Param("tenantId") Long tenantId, @Param("beforeDays") Integer beforeDays);
}

