package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.util.List;

/**
 * 应收待办服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArTodoService {

    /**
     * 查询待办事项列表
     */
    MyJsonBean<PageResult> getTodoList(String handlerId, Integer todoStatus, Long tenantId);

    /**
     * 根据ID查询待办详情
     */
    MyJsonBean getTodoById(String todoId);

    /**
     * 根据业务ID查询待办
     */
    MyJsonBean getTodoByBusinessId(String businessType, String businessId);

    /**
     * 创建待办事项
     */
    MyJsonBean createTodo(String todoTitle, String todoContent, String todoType, 
                          String businessType, String businessId, String handlerId, 
                          String handlerName, Long tenantId, String orgId);

    /**
     * 批量创建待办事项
     */
    MyJsonBean batchCreateTodo(List<?> todos);

    /**
     * 完成待办事项
     */
    MyJsonBean completeTodo(String todoId);

    /**
     * 根据业务ID完成待办
     */
    MyJsonBean completeTodoByBusinessId(String businessType, String businessId);

    /**
     * 取消待办事项
     */
    MyJsonBean cancelTodo(String todoId);

    /**
     * 查询待办统计
     */
    MyJsonBean getTodoStatistics(String handlerId, Long tenantId);

    /**
     * 清理已处理的待办
     */
    MyJsonBean cleanProcessedTodos(Long tenantId, Integer beforeDays);
}

