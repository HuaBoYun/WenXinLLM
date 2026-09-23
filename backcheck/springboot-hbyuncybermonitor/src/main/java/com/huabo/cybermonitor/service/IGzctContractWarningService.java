package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctContractWarning;

import java.util.List;

/**
 * 合同风险预警 Service 接口
 */
public interface IGzctContractWarningService extends IService<GzctContractWarning> {

    /**
     * 根据预警编号查询
     */
    GzctContractWarning getByWarnNo(String warnNo);

    /**
     * 根据合同ID查询所有预警
     */
    List<GzctContractWarning> getByContractId(String contractId);

    /**
     * 处置预警
     */
    boolean handleWarning(String warnNo, String measures, String owner, String deadline, String remark);

    /**
     * 关闭预警
     */
    boolean closeWarning(String warnNo);

    /**
     * 保存或更新预警记录（用于同步动态计算的预警到持久化表）
     */
    GzctContractWarning saveOrUpdateByWarnNo(GzctContractWarning warning);
}
