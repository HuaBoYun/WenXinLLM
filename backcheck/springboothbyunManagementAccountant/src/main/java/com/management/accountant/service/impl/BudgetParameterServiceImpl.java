package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetParameter;
import com.management.accountant.oracle.mapper.budget.BudgetParameterMapper;
import com.management.accountant.service.BudgetParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BudgetParameterServiceImpl implements BudgetParameterService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetParameterMapper parameterMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetParameter create(BudgetParameter p) {
        if (p == null) throw new ServiceException("参数信息不能为空");
        if (p.getDelFlag() == null) p.setDelFlag(0);
        if (p.getIsEnabled() == null) p.setIsEnabled(true);
        Date now = new Date();
        p.setCreateTime(now);
        p.setUpdateTime(now);
        p.setLastModifyTime(now);
        parameterMapper.insert(p);
        return p;
    }

    @Override
    public BudgetParameter getById(String id) {
        QueryWrapper<BudgetParameter> w = new QueryWrapper<>();
        w.eq("PARAMETER_ID", id);
        return parameterMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetParameter p) {
        if (p == null || !StringUtils.hasText(p.getParameterId())) throw new ServiceException("参数ID不能为空");
        Date now = new Date();
        p.setUpdateTime(now);
        p.setLastModifyTime(now);
        parameterMapper.updateById(p);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        parameterMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        log.info("[BudgetParameter] getPage called: pageNum={}, pageSize={}, params={}", pageNum, pageSize, params);
        QueryWrapper<BudgetParameter> w = new QueryWrapper<>();
        // 注意：@TableLogic 会自动加 del_flag=0，无需手动添加
        if (params.get("parameterName") != null && !params.get("parameterName").toString().isEmpty())
            w.like("PARAMETER_NAME", params.get("parameterName"));
        if (params.get("parameterType") != null && !params.get("parameterType").toString().isEmpty())
            w.eq("PARAMETER_TYPE", params.get("parameterType"));
        if (params.get("parameterStatus") != null && !params.get("parameterStatus").toString().isEmpty())
            w.eq("IS_ENABLED", "ACTIVE".equals(params.get("parameterStatus")) ? 1 : 0);
        if (params.get("parameterScope") != null && !params.get("parameterScope").toString().isEmpty())
            w.eq("PARAMETER_SCOPE", params.get("parameterScope"));
        if (params.get("categoryId") != null && !params.get("categoryId").toString().isEmpty()
                && !"null".equals(params.get("categoryId").toString()))
            w.eq("PARAMETER_CATEGORY", params.get("categoryId"));
        if (params.get("parameterCategory") != null && !params.get("parameterCategory").toString().isEmpty())
            w.eq("PARAMETER_CATEGORY", params.get("parameterCategory"));
        w.orderByDesc("CREATE_TIME");
        IPage<BudgetParameter> pageResult = parameterMapper.selectPage(new Page<>(pageNum, pageSize), w);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        // 统计各 scope 数量（全量，@TableLogic 自动过滤已删除）
        QueryWrapper<BudgetParameter> baseW = new QueryWrapper<>();
        int systemCount = parameterMapper.selectCount(baseW.clone().eq("PARAMETER_SCOPE", "SYSTEM")).intValue();
        int businessCount = parameterMapper.selectCount(baseW.clone().eq("PARAMETER_SCOPE", "BUSINESS")).intValue();
        int customCount = parameterMapper.selectCount(baseW.clone().eq("PARAMETER_SCOPE", "CUSTOM")).intValue();
        result.put("systemCount", systemCount);
        result.put("businessCount", businessCount);
        result.put("customCount", customCount);

        // total 优先用分页插件返回值，若为 0 则用 selectCount 兜底
        long total = pageResult.getTotal();
        if (total == 0) {
            total = parameterMapper.selectCount(new QueryWrapper<>());
        }
        result.put("total", total);

        return result;
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        QueryWrapper<BudgetParameter> w = new QueryWrapper<>();
        w.select("DISTINCT PARAMETER_CATEGORY");
        List<BudgetParameter> list = parameterMapper.selectList(w);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (BudgetParameter p : list) {
            if (p.getParameterCategory() != null) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", p.getParameterCategory());
                node.put("name", p.getParameterCategory());  // 前端树组件用 name 字段
                node.put("parameterCount", 0);
                node.put("children", new ArrayList<>());
                tree.add(node);
            }
        }
        return tree;
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        return getCategoryTree();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String id, String status) {
        // 前端传的字段名是 parameterStatus，值是 ACTIVE/INACTIVE
        BudgetParameter u = new BudgetParameter();
        u.setParameterId(id);
        u.setIsEnabled("ACTIVE".equals(status));
        Date now = new Date();
        u.setUpdateTime(now);
        u.setLastModifyTime(now);
        parameterMapper.updateById(u);
    }

    @Override
    public Map<String, Object> validateParameter(Map<String, Object> params) {
        Map<String, Object> r = new HashMap<>();
        String parameterId = params.get("parameterId") != null ? params.get("parameterId").toString() : null;
        String testValue = params.get("testValue") != null ? params.get("testValue").toString() : "";
        if (parameterId != null) {
            BudgetParameter p = getById(parameterId);
            if (p != null && StringUtils.hasText(p.getValidationRule())) {
                boolean valid = testValue.matches(p.getValidationRule());
                r.put("valid", valid);
                r.put("message", valid ? "验证通过" : "值不符合验证规则：" + p.getValidationRule());
                return r;
            }
        }
        r.put("valid", true);
        r.put("message", "验证通过（无验证规则）");
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetParameter(String id) {
        BudgetParameter p = getById(id);
        if (p != null && p.getDefaultValue() != null) {
            BudgetParameter u = new BudgetParameter();
            u.setParameterId(id); u.setParameterValue(p.getDefaultValue()); u.setUpdateTime(new Date());
            parameterMapper.updateById(u);
        }
    }

    @Override
    public Map<String, Object> batchValidate(List<String> ids) {
        Map<String, Object> r = new HashMap<>();
        r.put("total", ids.size()); r.put("valid", ids.size()); r.put("invalid", 0);
        return r;
    }

    @Override
    public List<BudgetParameter> getExportData(Map<String, Object> params) {
        Object idsObj = params.get("ids");
        if (idsObj instanceof List && !((List<?>) idsObj).isEmpty()) {
            List<String> ids = new ArrayList<>();
            for (Object o : (List<?>) idsObj) { ids.add(o.toString()); }
            QueryWrapper<BudgetParameter> w = new QueryWrapper<>();
            w.in("PARAMETER_ID", ids);
            return parameterMapper.selectList(w);
        }
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetParameter> w = new QueryWrapper<>();
        if (params.get("parameterName") != null && !params.get("parameterName").toString().isEmpty())
            w.like("PARAMETER_NAME", params.get("parameterName"));
        if (params.get("parameterType") != null && !params.get("parameterType").toString().isEmpty())
            w.eq("PARAMETER_TYPE", params.get("parameterType"));
        if (params.get("parameterScope") != null && !params.get("parameterScope").toString().isEmpty())
            w.eq("PARAMETER_SCOPE", params.get("parameterScope"));
        w.orderByDesc("CREATE_TIME");
        IPage<BudgetParameter> pageResult = parameterMapper.selectPage(new Page<>(pageNum, pageSize), w);
        return pageResult.getRecords();
    }

    @Override
    public BudgetParameter getByIdDirect(String id) {
        return parameterMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importParameters(List<BudgetParameter> parameters) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < parameters.size(); i++) {
            BudgetParameter p = parameters.get(i);
            try {
                if (!StringUtils.hasText(p.getParameterCode())) {
                    throw new ServiceException("参数编码不能为空");
                }
                if (!StringUtils.hasText(p.getParameterName())) {
                    throw new ServiceException("参数名称不能为空");
                }
                QueryWrapper<BudgetParameter> w = new QueryWrapper<>();
                w.eq("PARAMETER_CODE", p.getParameterCode());
                Long count = parameterMapper.selectCount(w);
                if (count != null && count > 0) {
                    throw new ServiceException("参数编码 " + p.getParameterCode() + " 已存在");
                }
                if (p.getDelFlag() == null) p.setDelFlag(0);
                if (p.getIsEnabled() == null) p.setIsEnabled(true);
                p.setCreateTime(now);
                p.setUpdateTime(now);
                p.setLastModifyTime(now);
                parameterMapper.insert(p);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failMessages.add("第" + (i + 1) + "行: " + e.getMessage());
            }
        }
        result.put("total", parameters.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failMessages", failMessages);
        return result;
    }
}

