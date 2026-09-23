package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.BudgetModelQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetModel;
import com.financial.sharing.budgetPlanning.mapper.BudgetModelMapper;
import com.financial.sharing.budgetPlanning.service.BudgetModelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 预算模型Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class BudgetModelServiceImpl implements BudgetModelService {

    @Autowired
    private BudgetModelMapper modelMapper;

    @Override
    public PageInfo<TblBudgetModel> getModelList(BudgetModelQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetModel> list = modelMapper.selectModelList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblBudgetModel> getModelListNoPage(BudgetModelQueryParam param) {
        if (param == null) {
            param = new BudgetModelQueryParam();
        }
        List<TblBudgetModel> list = modelMapper.selectModelList(param);
        return list != null ? list : Collections.emptyList();
    }

    @Override
    public TblBudgetModel getModelById(String modelId) {
        return modelMapper.selectById(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addModel(TblBudgetModel model) {
        // 检查模型编码是否存在
        int count = modelMapper.checkModelCodeExists(model.getModelCode(), null);
        if (count > 0) {
            throw new RuntimeException("模型编码已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        model.setModelId(UUID.randomUUID().toString().replace("-", ""));
        model.setStatus("DRAFT");
        model.setOrgId(orgId);
        model.setCreateUser(userId);
        model.setCreateTime(now);
        model.setUpdateUser(userId);
        model.setUpdateTime(now);

        modelMapper.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(TblBudgetModel model) {
        // 检查模型编码是否存在
        int count = modelMapper.checkModelCodeExists(model.getModelCode(), model.getModelId());
        if (count > 0) {
            throw new RuntimeException("模型编码已存在");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        model.setUpdateUser(userId);
        model.setUpdateTime(now);

        modelMapper.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(String modelId) {
        // TODO: 检查是否有关联数据
        modelMapper.deleteById(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteModel(List<String> modelIds) {
        for (String modelId : modelIds) {
            deleteModel(modelId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyModel(String modelId, String newModelCode, String newModelName) {
        // 查询源模型
        TblBudgetModel sourceModel = modelMapper.selectById(modelId);
        if (sourceModel == null) {
            throw new RuntimeException("源模型不存在");
        }

        // 检查新模型编码是否存在
        int count = modelMapper.checkModelCodeExists(newModelCode, null);
        if (count > 0) {
            throw new RuntimeException("新模型编码已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 创建新模型
        TblBudgetModel newModel = new TblBudgetModel();
        newModel.setModelId(UUID.randomUUID().toString().replace("-", ""));
        newModel.setModelCode(newModelCode);
        newModel.setModelName(newModelName);
        newModel.setBudgetYear(sourceModel.getBudgetYear());
        newModel.setBudgetType(sourceModel.getBudgetType());
        newModel.setBudgetCycle(sourceModel.getBudgetCycle());
        newModel.setDimensionConfig(sourceModel.getDimensionConfig());
        newModel.setStatus("DRAFT");
        newModel.setDescription("复制自: " + sourceModel.getModelName());
        newModel.setSortOrder(sourceModel.getSortOrder());
        newModel.setOrgId(orgId);
        newModel.setCreateUser(userId);
        newModel.setCreateTime(now);
        newModel.setUpdateUser(userId);
        newModel.setUpdateTime(now);

        modelMapper.insert(newModel);

        return newModel.getModelId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableModel(String modelId) {
        TblBudgetModel model = modelMapper.selectById(modelId);
        if (model == null) {
            throw new RuntimeException("模型不存在");
        }

        model.setStatus("ACTIVE");
        model.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        model.setUpdateTime(new Date());

        modelMapper.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableModel(String modelId) {
        TblBudgetModel model = modelMapper.selectById(modelId);
        if (model == null) {
            throw new RuntimeException("模型不存在");
        }

        model.setStatus("INACTIVE");
        model.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        model.setUpdateTime(new Date());

        modelMapper.updateById(model);
    }
}

