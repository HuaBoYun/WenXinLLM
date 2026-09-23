package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.URuleSqlReleationEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IURuleSqlReleationService extends IService<URuleSqlReleationEntity> {

    /**
     * 保存或更新数据
     */
    public void saveOrUpdateData(URuleSqlReleationEntity entity);


    /**
     * 根据sqlDataId获取绑定的规则信息
     * @param sqlDataId
     * @return
     */
    public URuleSqlReleationEntity getReleationBySqlDataId(String sqlDataId);

    /**
     * 根据绑定ID删除规则数据
     * @param id
     * @return
     */
    public boolean removeDataById(String id);

    /**
     * 获取所有URule下所有规则集合
     * @return
     */
    public List<Map<String, String>> getRuleList();
}
