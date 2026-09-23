package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetDataQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.financial.sharing.budgetPlanning.mapper.BudgetDataMapper;
import com.financial.sharing.budgetPlanning.service.BudgetDataService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 预算数据编制Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class BudgetDataServiceImpl implements BudgetDataService {

    @Autowired
    private BudgetDataMapper dataMapper;

    @Override
    public PageInfo<TblBudgetData> getDataList(BudgetDataQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetData> list = dataMapper.selectDataList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblBudgetData> getDataListNoPage(BudgetDataQueryParam param) {
        return dataMapper.selectDataList(param);
    }

    @Override
    public TblBudgetData getDataById(String dataId) {
        return dataMapper.selectById(dataId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addData(TblBudgetData data) {
        // 检查数据是否存在
        int count = dataMapper.checkDataExists(
            data.getModelId(),
            data.getFormId(),
            data.getPeriod(),
            data.getVersion(),
            data.getSubjectCode(),
            data.getOrganizationCode(),
            null
        );
        if (count > 0) {
            throw new RuntimeException("该维度组合的数据已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        data.setDataId(UUID.randomUUID().toString().replace("-", ""));
        data.setStatus("DRAFT");
        data.setOrgId(orgId);
        data.setCreateUser(userId);
        data.setCreateTime(now);
        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        dataMapper.insert(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateData(TblBudgetData data) {
        // 检查数据是否存在
        int count = dataMapper.checkDataExists(
            data.getModelId(),
            data.getFormId(),
            data.getPeriod(),
            data.getVersion(),
            data.getSubjectCode(),
            data.getOrganizationCode(),
            data.getDataId()
        );
        if (count > 0) {
            throw new RuntimeException("该维度组合的数据已存在");
        }

        // 只有草稿状态的数据才能修改
        TblBudgetData existData = dataMapper.selectById(data.getDataId());
        if (existData == null) {
            throw new RuntimeException("数据不存在");
        }
        if (!"DRAFT".equals(existData.getStatus())) {
            throw new RuntimeException("只有草稿状态的数据才能修改");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        dataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(String dataId) {
        // 只有草稿状态的数据才能删除
        TblBudgetData data = dataMapper.selectById(dataId);
        if (data == null) {
            throw new RuntimeException("数据不存在");
        }
        if (!"DRAFT".equals(data.getStatus())) {
            throw new RuntimeException("只有草稿状态的数据才能删除");
        }

        dataMapper.deleteById(dataId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteData(List<String> dataIds) {
        for (String dataId : dataIds) {
            deleteData(dataId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitData(String dataId) {
        TblBudgetData data = dataMapper.selectById(dataId);
        if (data == null) {
            throw new RuntimeException("数据不存在");
        }
        if (!"DRAFT".equals(data.getStatus())) {
            throw new RuntimeException("只有草稿状态的数据才能提交");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        data.setStatus("SUBMITTED");
        data.setSubmitUser(userId);
        data.setSubmitTime(now);
        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        dataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawData(String dataId) {
        TblBudgetData data = dataMapper.selectById(dataId);
        if (data == null) {
            throw new RuntimeException("数据不存在");
        }
        if (!"SUBMITTED".equals(data.getStatus())) {
            throw new RuntimeException("只有已提交状态的数据才能撤回");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        data.setStatus("DRAFT");
        data.setSubmitUser(null);
        data.setSubmitTime(null);
        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        dataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveData(String dataId) {
        TblBudgetData data = dataMapper.selectById(dataId);
        if (data == null) {
            throw new RuntimeException("数据不存在");
        }
        if (!"SUBMITTED".equals(data.getStatus())) {
            throw new RuntimeException("只有已提交状态的数据才能审批");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        data.setStatus("APPROVED");
        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        dataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSubmitData(List<String> dataIds) {
        for (String dataId : dataIds) {
            submitData(dataId);
        }
    }
}