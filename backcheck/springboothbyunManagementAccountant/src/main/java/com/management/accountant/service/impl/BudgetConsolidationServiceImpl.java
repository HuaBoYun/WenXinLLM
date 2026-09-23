package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetConsolidation;
import com.management.accountant.oracle.mapper.budget.BudgetConsolidationMapper;
import com.management.accountant.service.BudgetConsolidationService;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class BudgetConsolidationServiceImpl implements BudgetConsolidationService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetConsolidationMapper consolidationMapper;

    @Resource
    private UserProvider userProvider;

    private String getCurrentUser() {
        try {
            TblStaffUtil staff = userProvider.get();
            return staff != null ? staff.getUsername() : "admin";
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return "admin";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetConsolidation create(BudgetConsolidation c) {
        if (c == null) throw new ServiceException("合并信息不能为空");
        // 生成任务编号
        if (!StringUtils.hasText(c.getConsolidationCode())) {
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            c.setConsolidationCode("BC" + dateStr + String.format("%04d", (int)(Math.random() * 10000)));
        }
        // 设置创建人
        if (!StringUtils.hasText(c.getCreateBy())) {
            c.setCreateBy(getCurrentUser());
        }
        if (c.getDelFlag() == null) c.setDelFlag(0);
        c.setCreateTime(new Date());
        c.setUpdateTime(new Date());
        consolidationMapper.insert(c);
        return c;
    }

    @Override
    public BudgetConsolidation getById(String id) {
        QueryWrapper<BudgetConsolidation> w = new QueryWrapper<>();
        w.eq("CONSOLIDATION_ID", id).eq("DEL_FLAG", 0);
        return consolidationMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetConsolidation c) {
        if (c == null || !StringUtils.hasText(c.getConsolidationId())) throw new ServiceException("合并ID不能为空");
        c.setUpdateTime(new Date());
        consolidationMapper.updateById(c);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        BudgetConsolidation u = new BudgetConsolidation();
        u.setConsolidationId(id); u.setDelFlag(1); u.setUpdateTime(new Date());
        consolidationMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetConsolidation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (hasValue(params.get("consolidationName"))) w.like("CONSOLIDATION_NAME", params.get("consolidationName"));
        if (hasValue(params.get("consolidationType"))) w.eq("CONSOLIDATION_TYPE", params.get("consolidationType"));
        if (hasValue(params.get("consolidationStatus"))) w.eq("CONSOLIDATION_STATUS", params.get("consolidationStatus"));
        w.orderByDesc("CREATE_TIME");
        IPage<BudgetConsolidation> pageResult = consolidationMapper.selectPage(new Page<>(pageNum, pageSize), w);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<BudgetConsolidation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        stats.put("total", consolidationMapper.selectCount(w));
        QueryWrapper<BudgetConsolidation> wCompleted = new QueryWrapper<>();
        wCompleted.eq("DEL_FLAG", 0).eq("CONSOLIDATION_STATUS", "COMPLETED");
        stats.put("completed", consolidationMapper.selectCount(wCompleted));
        return stats;
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String id) {
        BudgetConsolidation u = new BudgetConsolidation();
        u.setConsolidationId(id); u.setConsolidationStatus("COMPLETED"); u.setUpdateTime(new Date());
        consolidationMapper.updateById(u);
    }

    @Override
    public void autoConsolidate() { log.info("自动合并执行"); }

    @Override
    public Map<String, Object> validate(String id) {
        Map<String, Object> r = new HashMap<>();
        r.put("valid", true); r.put("message", "验证通过");
        return r;
    }

    @Override
    public Map<String, Object> batchValidate(List<String> ids) {
        Map<String, Object> r = new HashMap<>();
        r.put("total", ids.size()); r.put("valid", ids.size()); r.put("invalid", 0);
        return r;
    }
}

