package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.FundAlert;

import java.util.List;
import java.util.Map;

/**
 * 资金预警Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface FundAlertService extends IService<FundAlert> {

    /**
     * 分页查询资金预警列表
     * @param params 查询参数
     * @return 资金预警列表
     */
    List<FundAlert> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询资金预警详情
     * @param id 主键ID
     * @return 资金预警详情
     */
    FundAlert selectDetailById(Long id);

    /**
     * 保存资金预警
     * @param entity 资金预警实体
     * @return 是否成功
     */
    boolean saveAlert(FundAlert entity);

    /**
     * 更新资金预警
     * @param entity 资金预警实体
     * @return 是否成功
     */
    boolean updateAlert(FundAlert entity);

    /**
     * 删除资金预警
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteAlert(Long id);
}

