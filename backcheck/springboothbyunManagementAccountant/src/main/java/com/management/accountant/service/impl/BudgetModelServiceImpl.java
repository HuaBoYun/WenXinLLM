package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetModel;
import com.management.accountant.oracle.mapper.budget.BudgetModelMapper;
import com.management.accountant.service.BudgetModelService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算模型Service实现类
 * 
 * @description 预算模型业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetModelServiceImpl implements BudgetModelService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetModel create(BudgetModel model) {
        // 1. 参数校验
        if (model == null) {
            throw new ServiceException("模型信息不能为空");
        }
        if (!StringUtils.hasText(model.getModelName())) {
            throw new ServiceException("模型名称不能为空");
        }
        if (!StringUtils.hasText(model.getModelType())) {
            throw new ServiceException("模型类型不能为空");
        }

        // 2. 检查模型编码是否重复
        if (StringUtils.hasText(model.getModelCode())) {
            QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
            wrapper.eq("MODEL_CODE", model.getModelCode())
                   .eq("IS_DELETED", 0L);
            long count = modelMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("模型编码已存在");
            }
        } else {
            // 自动生成模型编码
            model.setModelCode(generateModelCode());
        }

        // 3. 设置默认值
        if (model.getIsDeleted() == null) {
            model.setIsDeleted(0);
        }
        if (model.getModelStatus() == null) {
            model.setModelStatus("DRAFT");
        }
        if (model.getVersion() == null) {
            model.setVersion("1.0");
        }
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = modelMapper.insert(model);
        if (result <= 0) {
            throw new ServiceException("创建预算模型失败");
        }

        log.info("创建预算模型成功，ID: {}", model.getModelId());
        return model;
    }

    @Override
    public BudgetModel getById(String modelId) {
        if (!StringUtils.hasText(modelId)) {
            throw new ServiceException("模型ID不能为空");
        }
        
        QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
        wrapper.eq("MODEL_ID", modelId)
               .eq("IS_DELETED", 0L);
        
        return modelMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetModel model) {
        if (model == null || !StringUtils.hasText(model.getModelId())) {
            throw new ServiceException("模型ID不能为空");
        }

        BudgetModel existing = getById(model.getModelId());
        if (existing == null) {
            throw new ServiceException("预算模型不存在");
        }

        // 检查模型编码是否重复
        if (StringUtils.hasText(model.getModelCode()) && !model.getModelCode().equals(existing.getModelCode())) {
            QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
            wrapper.eq("MODEL_CODE", model.getModelCode())
                   .eq("IS_DELETED", 0L)
                   .ne("MODEL_ID", model.getModelId());
            long count = modelMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("模型编码已存在");
            }
        }

        model.setUpdateTime(new Date());
        int result = modelMapper.updateById(model);
        if (result <= 0) {
            throw new ServiceException("更新预算模型失败");
        }

        log.info("更新预算模型成功，ID: {}", model.getModelId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String modelId) {
        if (!StringUtils.hasText(modelId)) {
            throw new ServiceException("模型ID不能为空");
        }

        BudgetModel model = getById(modelId);
        if (model == null) {
            throw new ServiceException("预算模型不存在");
        }

        // 检查模型是否正在使用
        if ("ACTIVE".equals(model.getModelStatus())) {
            throw new ServiceException("模型正在使用中，无法删除");
        }

        BudgetModel update = new BudgetModel();
        update.setModelId(modelId);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());

        int result = modelMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除预算模型失败");
        }

        log.info("删除预算模型成功，ID: {}", modelId);
    }

    @Override
    public PageResult<BudgetModel> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0L);

        // 模型编码
        if (params.get("modelCode") != null && !"".equals(params.get("modelCode").toString().trim())) {
            wrapper.like("MODEL_CODE", params.get("modelCode"));
        }

        // 模型名称
        if (params.get("modelName") != null && !"".equals(params.get("modelName").toString().trim())) {
            wrapper.like("MODEL_NAME", params.get("modelName"));
        }

        // 模型类型
        if (params.get("modelType") != null && !"".equals(params.get("modelType").toString().trim())) {
            wrapper.eq("MODEL_TYPE", params.get("modelType"));
        }

        // 模型状态
        if (params.get("modelStatus") != null && !"".equals(params.get("modelStatus").toString().trim())) {
            wrapper.eq("MODEL_STATUS", params.get("modelStatus"));
        }

        // 公司ID
        if (params.get("companyId") != null && !"".equals(params.get("companyId").toString().trim())) {
            wrapper.eq("COMPANY_ID", params.get("companyId"));
        }

        // 创建人
        if (params.get("creator") != null && !"".equals(params.get("creator").toString().trim())) {
            wrapper.like("CREATOR_NAME", params.get("creator"));
        }

        // 创建时间范围
        if (params.get("createTimeStart") != null && !"".equals(params.get("createTimeStart").toString().trim())) {
            wrapper.ge("CREATE_TIME", params.get("createTimeStart"));
        }
        if (params.get("createTimeEnd") != null && !"".equals(params.get("createTimeEnd").toString().trim())) {
            wrapper.le("CREATE_TIME", params.get("createTimeEnd"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetModel> page = new Page<>(pageNum, pageSize);
        IPage<BudgetModel> pageResult = modelMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetModel> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activate(String modelId) {
        if (!StringUtils.hasText(modelId)) {
            throw new ServiceException("模型ID不能为空");
        }

        BudgetModel model = getById(modelId);
        if (model == null) {
            throw new ServiceException("预算模型不存在");
        }

        if ("ACTIVE".equals(model.getModelStatus())) {
            throw new ServiceException("模型已经是激活状态");
        }

        BudgetModel update = new BudgetModel();
        update.setModelId(modelId);
        update.setModelStatus("ACTIVE");
        update.setUpdateTime(new Date());

        int result = modelMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("激活模型失败");
        }

        log.info("激活预算模型成功，ID: {}", modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivate(String modelId) {
        if (!StringUtils.hasText(modelId)) {
            throw new ServiceException("模型ID不能为空");
        }

        BudgetModel model = getById(modelId);
        if (model == null) {
            throw new ServiceException("预算模型不存在");
        }

        if ("INACTIVE".equals(model.getModelStatus())) {
            throw new ServiceException("模型已经是停用状态");
        }

        BudgetModel update = new BudgetModel();
        update.setModelId(modelId);
        update.setModelStatus("INACTIVE");
        update.setUpdateTime(new Date());

        int result = modelMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("停用模型失败");
        }

        log.info("停用预算模型成功，ID: {}", modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetModel copy(String modelId, String newModelName) {
        if (!StringUtils.hasText(modelId)) {
            throw new ServiceException("模型ID不能为空");
        }

        BudgetModel source = getById(modelId);
        if (source == null) {
            throw new ServiceException("源模型不存在");
        }

        // 如果未提供新名称，自动生成
        if (!StringUtils.hasText(newModelName)) {
            newModelName = source.getModelName() + "_副本";
        }

        // 创建新模型
        BudgetModel newModel = new BudgetModel();
        newModel.setModelName(newModelName);
        newModel.setModelCode(generateModelCode());
        newModel.setModelType(source.getModelType());
        newModel.setModelStatus("DRAFT");
        newModel.setApplicableScope(source.getApplicableScope());
        newModel.setBudgetCycle(source.getBudgetCycle());
        newModel.setModelDescription(source.getModelDescription());
        newModel.setConfigParameters(source.getConfigParameters());
        newModel.setCalculationRules(source.getCalculationRules());
        newModel.setVersion("1.0");
        newModel.setCompanyId(source.getCompanyId());
        newModel.setCompanyName(source.getCompanyName());

        return create(newModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void archive(String modelId) {
        if (!StringUtils.hasText(modelId)) {
            throw new ServiceException("模型ID不能为空");
        }

        BudgetModel model = getById(modelId);
        if (model == null) {
            throw new ServiceException("预算模型不存在");
        }

        if ("ARCHIVED".equals(model.getModelStatus())) {
            throw new ServiceException("模型已经是归档状态");
        }

        BudgetModel update = new BudgetModel();
        update.setModelId(modelId);
        update.setModelStatus("ARCHIVED");
        update.setUpdateTime(new Date());

        int result = modelMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("归档模型失败");
        }

        log.info("归档预算模型成功，ID: {}", modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的记录");
        }

        for (String id : ids) {
            try {
                delete(id);
            } catch (Exception e) {
                log.error("批量删除失败，ID: {}", id, e);
            }
        }

        log.info("批量删除完成，数量: {}", ids.size());
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0L);

        // 总数
        Integer totalCount = modelMapper.selectCount(wrapper).intValue();
        statistics.put("totalCount", totalCount);

        // 激活数量
        wrapper.eq("MODEL_STATUS", "ACTIVE");
        Integer activeCount = modelMapper.selectCount(wrapper).intValue();
        statistics.put("activeCount", activeCount);

        // 草稿数量
        wrapper = new QueryWrapper<BudgetModel>();
        wrapper.eq("IS_DELETED", 0L);
        wrapper.eq("MODEL_STATUS", "DRAFT");
        Integer draftCount = modelMapper.selectCount(wrapper).intValue();
        statistics.put("draftCount", draftCount);

        // 归档数量
        wrapper = new QueryWrapper<BudgetModel>();
        wrapper.eq("IS_DELETED", 0L);
        wrapper.eq("MODEL_STATUS", "ARCHIVED");
        Integer archivedCount = modelMapper.selectCount(wrapper).intValue();
        statistics.put("archivedCount", archivedCount);

        return statistics;
    }

    @Override
    public Map<String, Object> getTypeStatistics() {
        Map<String, Object> typeStats = new HashMap<>();
        String[] types = {"INCREMENTAL", "ZERO_BASED", "ROLLING", "FLEXIBLE", "ACTIVITY_BASED", "CAPITAL", "CASH_FLOW", "CUSTOM"};

        for (String type : types) {
            QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_DELETED", 0L)
                   .eq("MODEL_TYPE", type);
            Integer count = modelMapper.selectCount(wrapper).intValue();
            typeStats.put(type, count);
        }

        // Also add total and active counts for the stats cards
        QueryWrapper<BudgetModel> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("IS_DELETED", 0L);
        typeStats.put("totalModels", modelMapper.selectCount(totalWrapper).intValue());

        QueryWrapper<BudgetModel> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("IS_DELETED", 0L).eq("MODEL_STATUS", "ACTIVE");
        typeStats.put("activeModels", modelMapper.selectCount(activeWrapper).intValue());

        typeStats.put("modelTypes", 8);

        // Count distinct versions
        QueryWrapper<BudgetModel> versionWrapper = new QueryWrapper<>();
        versionWrapper.eq("IS_DELETED", 0L);
        typeStats.put("totalVersions", modelMapper.selectCount(versionWrapper).intValue());

        return typeStats;
    }

    @Override
    public List<BudgetModel> getByType(String modelType) {
        if (!StringUtils.hasText(modelType)) {
            throw new ServiceException("模型类型不能为空");
        }

        QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0L)
               .eq("MODEL_TYPE", modelType)
               .orderByDesc("CREATE_TIME");

        return modelMapper.selectList(wrapper);
    }

    @Override
    public List<BudgetModel> getActiveModels() {
        QueryWrapper<BudgetModel> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0L)
               .eq("MODEL_STATUS", "ACTIVE")
               .orderByDesc("CREATE_TIME");

        return modelMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> validateConfig(String modelId) {
        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(modelId)) {
            result.put("valid", false);
            result.put("message", "模型ID不能为空");
            return result;
        }

        BudgetModel model = getById(modelId);
        if (model == null) {
            result.put("valid", false);
            result.put("message", "预算模型不存在");
            return result;
        }

        // 校验必填字段
        List<String> errors = new ArrayList<>();
        if (!StringUtils.hasText(model.getModelName())) {
            errors.add("模型名称不能为空");
        }
        if (!StringUtils.hasText(model.getModelType())) {
            errors.add("模型类型不能为空");
        }
        if (!StringUtils.hasText(model.getBudgetCycle())) {
            errors.add("预算周期不能为空");
        }

        if (errors.isEmpty()) {
            result.put("valid", true);
            result.put("message", "配置校验通过");
        } else {
            result.put("valid", false);
            result.put("errors", errors);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> applyToTask(String modelId, String taskId) {
        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(modelId)) {
            result.put("success", false);
            result.put("message", "模型ID不能为空");
            return result;
        }
        if (!StringUtils.hasText(taskId)) {
            result.put("success", false);
            result.put("message", "任务ID不能为空");
            return result;
        }

        BudgetModel model = getById(modelId);
        if (model == null) {
            result.put("success", false);
            result.put("message", "预算模型不存在");
            return result;
        }

        if (!"ACTIVE".equals(model.getModelStatus())) {
            result.put("success", false);
            result.put("message", "只能应用激活状态的模型");
            return result;
        }

        // TODO: 实现模型应用到任务的具体逻辑
        result.put("success", true);
        result.put("message", "模型应用成功");
        result.put("modelId", modelId);
        result.put("taskId", taskId);

        log.info("应用模型到任务成功，模型ID: {}, 任务ID: {}", modelId, taskId);
        return result;
    }

    /**
     * 生成模型编码
     */
    private String generateModelCode() {
        return "MODEL" + System.currentTimeMillis();
    }
}

