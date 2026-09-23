package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.InternalLoanApplication;

import java.util.List;
import java.util.Map;

/**
 * 内部借款申请Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface InternalLoanApplicationService extends IService<InternalLoanApplication> {

    /**
     * 分页查询内部借款申请列表
     * @param params 查询参数
     * @return 内部借款申请列表
     */
    List<InternalLoanApplication> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询内部借款申请详情
     * @param id 主键ID
     * @return 内部借款申请详情
     */
    InternalLoanApplication selectDetailById(Long id);

    /**
     * 保存内部借款申请
     * @param entity 内部借款申请实体
     * @return 是否成功
     */
    boolean saveApplication(InternalLoanApplication entity);

    /**
     * 更新内部借款申请
     * @param entity 内部借款申请实体
     * @return 是否成功
     */
    boolean updateApplication(InternalLoanApplication entity);

    /**
     * 删除内部借款申请
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteApplication(Long id);
}

