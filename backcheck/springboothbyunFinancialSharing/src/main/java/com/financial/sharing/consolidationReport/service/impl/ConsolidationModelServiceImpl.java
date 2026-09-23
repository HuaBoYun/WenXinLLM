package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ConsolidationModelQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationModel;
import com.financial.sharing.consolidationReport.mapper.ConsolidationModelMapper;
import com.financial.sharing.consolidationReport.service.ConsolidationModelService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 合并模型Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ConsolidationModelServiceImpl implements ConsolidationModelService {

    @Autowired
    private ConsolidationModelMapper consolidationModelMapper;

    @Override
    public List<TblConsolidationModel> getModelList(ConsolidationModelQueryParam param) {
        return consolidationModelMapper.selectModelList(param);
    }

    @Override
    public TblConsolidationModel getModelById(String modelId) {
        return consolidationModelMapper.selectById(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveModel(TblConsolidationModel model) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查模型编码是否已存在
        int count = consolidationModelMapper.checkModelCodeExists(model.getModelCode(), tenantId, null);
        if (count > 0) {
            throw new RuntimeException("模型编码已存在");
        }

        // 设置默认值
        model.setModelId(UUID.randomUUID().toString().replace("-", ""));
        model.setTenantId(tenantId);
        model.setCreateUser(userId);
        model.setCreateTime(now);
        model.setUpdateUser(userId);
        model.setUpdateTime(now);

        if (model.getStatus() == null || model.getStatus().isEmpty()) {
            model.setStatus("ACTIVE");
        }
        if (model.getIsAutoElimination() == null || model.getIsAutoElimination().isEmpty()) {
            model.setIsAutoElimination("Y");
        }

        consolidationModelMapper.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(TblConsolidationModel model) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查模型编码是否已存在(排除自己)
        int count = consolidationModelMapper.checkModelCodeExists(
            model.getModelCode(), tenantId, model.getModelId());
        if (count > 0) {
            throw new RuntimeException("模型编码已存在");
        }

        model.setUpdateUser(userId);
        model.setUpdateTime(now);

        consolidationModelMapper.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(String modelId) {
        consolidationModelMapper.deleteById(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModelStatus(String modelId, String status) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        TblConsolidationModel model = new TblConsolidationModel();
        model.setModelId(modelId);
        model.setStatus(status);
        model.setUpdateUser(userId);
        model.setUpdateTime(now);

        consolidationModelMapper.updateById(model);
    }
}

