package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctContractRectification;

import java.util.List;

/**
 * 合同整改记录服务接口
 */
public interface IGzctContractRectificationService extends IService<GzctContractRectification> {

    /**
     * 下发整改通知
     *
     * @param rectification 整改记录
     * @return 是否成功
     */
    boolean submitRectification(GzctContractRectification rectification);

    /**
     * 根据合同编号查询整改记录
     *
     * @param contractNo 合同编号
     * @return 整改记录列表
     */
    List<GzctContractRectification> getByContractNo(String contractNo);

    /**
     * 根据合同ID查询最新整改状态
     *
     * @param contractId 合同ID
     * @return 最新整改状态，无记录返回null
     */
    String getLatestRectifyStatus(String contractId);
}
