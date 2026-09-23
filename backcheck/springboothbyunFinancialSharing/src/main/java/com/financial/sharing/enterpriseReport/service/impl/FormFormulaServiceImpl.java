package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.FormFormulaQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormFormula;
import com.financial.sharing.enterpriseReport.mapper.FormFormulaMapper;
import com.financial.sharing.enterpriseReport.service.FormFormulaService;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 表单公式Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class FormFormulaServiceImpl extends ServiceImpl<FormFormulaMapper, TblFormFormula> 
        implements FormFormulaService {

    @Override
    public List<TblFormFormula> getList(FormFormulaQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        return baseMapper.selectFormFormulaList(
            tenantId,
            param.getTemplateId(),
            param.getFormulaName(),
            param.getFormulaType(),
            param.getStatus()
        );
    }

    @Override
    public TblFormFormula getDetail(String formulaId) {
        TblFormFormula formFormula = this.getById(formulaId);
        if (formFormula != null) {
            // 补充关联信息
            String tenantId = UserUtils.getUser().getOrgid().toString();
            List<TblFormFormula> list = baseMapper.selectFormFormulaList(
                tenantId, null, null, null, null
            );
            for (TblFormFormula formula : list) {
                if (formula.getFormulaId().equals(formulaId)) {
                    formFormula.setTemplateName(formula.getTemplateName());
                    break;
                }
            }
        }
        return formFormula;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFormFormula(TblFormFormula formFormula) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        formFormula.setTenantId(tenantId);
        
        // 检查公式编码是否重复
        int count = baseMapper.checkFormulaCodeExists(
            formFormula.getFormulaCode(),
            tenantId,
            formFormula.getFormulaId()
        );
        if (count > 0) {
            throw new RuntimeException("公式编码已存在");
        }
        
        if (StringUtils.isEmpty(formFormula.getFormulaId())) {
            // 新增
            formFormula.setCreateUser(userId);
            formFormula.setCreateTime(now);
            formFormula.setUpdateUser(userId);
            formFormula.setUpdateTime(now);
            
            // 设置默认排序号
            if (formFormula.getSortNo() == null) {
                formFormula.setSortNo(0);
            }
            
            // 设置默认状态
            if (StringUtils.isEmpty(formFormula.getStatus())) {
                formFormula.setStatus("ACTIVE");
            }
            
            return this.save(formFormula);
        } else {
            // 修改
            formFormula.setUpdateUser(userId);
            formFormula.setUpdateTime(now);
            
            return this.updateById(formFormula);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFormFormula(String formulaId) {
        // TODO: 检查是否有报表数据关联
        
        return this.removeById(formulaId);
    }

    @Override
    public List<TblFormFormula> getListByTemplateId(String templateId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        QueryWrapper<TblFormFormula> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        wrapper.eq("TEMPLATE_ID", templateId);
        wrapper.eq("STATUS", "ACTIVE");
        wrapper.orderByAsc("SORT_NO", "CREATE_TIME");
        
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<String> formulaIds) {
        if (formulaIds == null || formulaIds.isEmpty()) {
            return false;
        }
        
        // TODO: 检查是否有报表数据关联
        
        return this.removeByIds(formulaIds);
    }
}

