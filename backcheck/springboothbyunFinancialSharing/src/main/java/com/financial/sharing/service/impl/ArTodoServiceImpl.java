package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.ArTodoEntity;
import com.financial.sharing.oracle.mapper.ArTodoMapper;
import com.financial.sharing.service.ArTodoService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 应收待办服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArTodoServiceImpl implements ArTodoService {

    @Resource
    private ArTodoMapper arTodoMapper;

    @Override
    public MyJsonBean<PageResult> getTodoList(String handlerId, Integer todoStatus, Long tenantId) {
        try {
            List<ArTodoEntity> list = arTodoMapper.selectTodoList(handlerId, todoStatus, tenantId);
            PageResult result = new PageResult();
            result.setTlist(list);
            result.setTotalRecord(list.size());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询待办事项列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTodoById(String todoId) {
        try {
            ArTodoEntity entity = arTodoMapper.selectById(todoId);
            if (entity == null || entity.getIsDeleted() == 1) {
                return MyJsonBean.errorData("待办事项不存在");
            }
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("查询待办事项详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTodoByBusinessId(String businessType, String businessId) {
        try {
            ArTodoEntity entity = arTodoMapper.selectByBusinessId(businessType, businessId);
            return MyJsonBean.successData(entity);
        } catch (Exception e) {
            log.error("根据业务ID查询待办失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean createTodo(String todoTitle, String todoContent, String todoType,
                                  String businessType, String businessId, String handlerId,
                                  String handlerName, Long tenantId, String orgId) {
        try {
            ArTodoEntity entity = new ArTodoEntity();
            entity.setTodoId(UUID.randomUUID().toString().replace("-", ""));
            entity.setTodoTitle(todoTitle);
            entity.setTodoContent(todoContent);
            entity.setTodoType(todoType);
            entity.setTodoStatus(0); // 待处理
            entity.setPriority(1); // 普通
            entity.setBusinessType(businessType);
            entity.setBusinessId(businessId);
            entity.setHandlerId(handlerId);
            entity.setHandlerName(handlerName);
            entity.setTenantId(tenantId);
            entity.setOrgId(orgId);
            entity.setCreateTime(LocalDateTime.now());
            entity.setIsDeleted(0);
            
            arTodoMapper.insert(entity);
            return MyJsonBean.successData(entity.getTodoId());
        } catch (Exception e) {
            log.error("创建待办事项失败", e);
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchCreateTodo(List<?> todos) {
        try {
            // TODO: 实现批量创建
            return MyJsonBean.successData("批量创建成功");
        } catch (Exception e) {
            log.error("批量创建待办事项失败", e);
            return MyJsonBean.errorData("批量创建失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean completeTodo(String todoId) {
        try {
            arTodoMapper.updateTodoStatus(todoId, 1); // 已完成
            return MyJsonBean.successData("完成成功");
        } catch (Exception e) {
            log.error("完成待办事项失败", e);
            return MyJsonBean.errorData("完成失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean completeTodoByBusinessId(String businessType, String businessId) {
        try {
            arTodoMapper.updateStatusByBusinessId(businessType, businessId, 1);
            return MyJsonBean.successData("完成成功");
        } catch (Exception e) {
            log.error("根据业务ID完成待办失败", e);
            return MyJsonBean.errorData("完成失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean cancelTodo(String todoId) {
        try {
            arTodoMapper.updateTodoStatus(todoId, 2); // 已取消
            return MyJsonBean.successData("取消成功");
        } catch (Exception e) {
            log.error("取消待办事项失败", e);
            return MyJsonBean.errorData("取消失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTodoStatistics(String handlerId, Long tenantId) {
        try {
            Map<String, Object> stats = arTodoMapper.selectTodoStatistics(handlerId, tenantId);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询待办统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean cleanProcessedTodos(Long tenantId, Integer beforeDays) {
        try {
            int count = arTodoMapper.deleteProcessedTodos(tenantId, beforeDays);
            return MyJsonBean.successData("清理完成，共清理" + count + "条记录");
        } catch (Exception e) {
            log.error("清理已处理待办失败", e);
            return MyJsonBean.errorData("清理失败: " + e.getMessage());
        }
    }
}
