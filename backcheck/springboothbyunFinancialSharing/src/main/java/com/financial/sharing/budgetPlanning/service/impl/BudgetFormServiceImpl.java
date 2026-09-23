package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.BudgetFormQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetForm;
import com.financial.sharing.budgetPlanning.mapper.BudgetFormMapper;
import com.financial.sharing.budgetPlanning.service.BudgetFormService;
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
 * 预算表单配置Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class BudgetFormServiceImpl implements BudgetFormService {

    @Autowired
    private BudgetFormMapper formMapper;

    @Override
    public PageInfo<TblBudgetForm> getFormList(BudgetFormQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetForm> list = formMapper.selectFormList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblBudgetForm> getFormListNoPage(BudgetFormQueryParam param) {
        return formMapper.selectFormList(param);
    }

    @Override
    public TblBudgetForm getFormById(String formId) {
        return formMapper.selectById(formId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addForm(TblBudgetForm form) {
        // 检查表单编码是否存在
        int count = formMapper.checkFormCodeExists(form.getFormCode(), null);
        if (count > 0) {
            throw new RuntimeException("表单编码已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        form.setFormId(UUID.randomUUID().toString().replace("-", ""));
        form.setStatus("DRAFT");
        form.setOrgId(orgId);
        form.setCreateUser(userId);
        form.setCreateTime(now);
        form.setUpdateUser(userId);
        form.setUpdateTime(now);

        formMapper.insert(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForm(TblBudgetForm form) {
        // 检查表单编码是否存在
        int count = formMapper.checkFormCodeExists(form.getFormCode(), form.getFormId());
        if (count > 0) {
            throw new RuntimeException("表单编码已存在");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        form.setUpdateUser(userId);
        form.setUpdateTime(now);

        formMapper.updateById(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForm(String formId) {
        // TODO: 检查是否有关联数据
        formMapper.deleteById(formId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteForm(List<String> formIds) {
        for (String formId : formIds) {
            deleteForm(formId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyForm(String formId, String newFormCode, String newFormName) {
        // 查询源表单
        TblBudgetForm sourceForm = formMapper.selectById(formId);
        if (sourceForm == null) {
            throw new RuntimeException("源表单不存在");
        }

        // 检查新表单编码是否存在
        int count = formMapper.checkFormCodeExists(newFormCode, null);
        if (count > 0) {
            throw new RuntimeException("新表单编码已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 创建新表单
        TblBudgetForm newForm = new TblBudgetForm();
        newForm.setFormId(UUID.randomUUID().toString().replace("-", ""));
        newForm.setModelId(sourceForm.getModelId());
        newForm.setFormCode(newFormCode);
        newForm.setFormName(newFormName);
        newForm.setFormType(sourceForm.getFormType());
        newForm.setFormConfig(sourceForm.getFormConfig());
        newForm.setStatus("DRAFT");
        newForm.setDescription("复制自: " + sourceForm.getFormName());
        newForm.setSortOrder(sourceForm.getSortOrder());
        newForm.setOrgId(orgId);
        newForm.setCreateUser(userId);
        newForm.setCreateTime(now);
        newForm.setUpdateUser(userId);
        newForm.setUpdateTime(now);

        formMapper.insert(newForm);

        return newForm.getFormId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableForm(String formId) {
        TblBudgetForm form = formMapper.selectById(formId);
        if (form == null) {
            throw new RuntimeException("表单不存在");
        }

        form.setStatus("ACTIVE");
        form.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        form.setUpdateTime(new Date());

        formMapper.updateById(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableForm(String formId) {
        TblBudgetForm form = formMapper.selectById(formId);
        if (form == null) {
            throw new RuntimeException("表单不存在");
        }

        form.setStatus("INACTIVE");
        form.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        form.setUpdateTime(new Date());

        formMapper.updateById(form);
    }
}

