package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblContract;
import com.financial.sharing.business.entity.TblContractPaymentPlan;
import com.financial.sharing.business.mapper.ContractMapper;
import com.financial.sharing.business.mapper.ContractPaymentPlanMapper;
import com.financial.sharing.business.service.ContractService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 合同服务实现类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractPaymentPlanMapper contractPaymentPlanMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public MyJsonBean getList(Map<String, Object> param) {
        try {
            log.info("查询合同列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? (Integer) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;

            Page<TblContract> page = new Page<>(pageNum, pageSize);

            com.baomidou.mybatisplus.core.metadata.IPage<TblContract> resultPage =
                contractMapper.selectContractPage(page,
                    (String) param.get("contractCode"),
                    (String) param.get("contractName"),
                    (String) param.get("contractType"),
                    (String) param.get("contractStatus"),
                    (String) param.get("startDate") != null ? java.time.LocalDate.parse((String) param.get("startDate")) : null,
                    (String) param.get("endDate") != null ? java.time.LocalDate.parse((String) param.get("endDate")) : null);

            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("tlist", resultPage.getRecords());
            pageResult.put("totalRecord", (int) resultPage.getTotal());
            pageResult.put("pageNo", pageNum);
            pageResult.put("pageSize", pageSize);
            pageResult.put("totalPage", (int) resultPage.getPages());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询合同列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String contractId) {
        try {
            if (!StringUtils.hasText(contractId)) {
                return MyJsonBean.errorData("合同ID不能为空");
            }

            log.info("查询合同详情，contractId={}", contractId);

            TblContract contract = contractMapper.selectById(contractId);
            if (contract == null) {
                return MyJsonBean.errorData("合同不存在");
            }

            return MyJsonBean.successData("查询成功", contract);
        } catch (Exception e) {
            log.error("查询合同详情失败，contractId={}", contractId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblContract contract) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            log.info("保存或更新合同，参数：{}", contract);

            LocalDateTime now = LocalDateTime.now();

            if (!StringUtils.hasText(contract.getContractId())) {
                // 新增
                contract.setContractId(UUID.randomUUID().toString().replace("-", ""));
                contract.setCreateTime(now);
                contract.setCreateUser(currentUser.getStaffid().toString());
                contract.setContractStatus("DRAFT");

                // 生成合同编号
                contract.setContractCode("CON" + System.currentTimeMillis());

                // 设置申请人信息
                contract.setApplicantId(currentUser.getStaffid().toString());
                contract.setApplicantName(currentUser.getUsername());
                contract.setApplicantDeptId(currentUser.getLinkDetp().getOrgid().toString());
                contract.setApplicantDeptName(currentUser.getLinkDetp().getOrgname());
            } else {
                // 更新
                contract.setUpdateTime(now);
                contract.setUpdateUser(currentUser.getStaffid().toString());
            }

            int result = StringUtils.hasText(contract.getContractId()) &&
                contractMapper.selectById(contract.getContractId()) != null
                ? contractMapper.updateById(contract)
                : contractMapper.insert(contract);

            if (result > 0) {
                return MyJsonBean.successData("保存成功", contract.getContractId());
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新合同失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String contractId) {
        try {
            if (!StringUtils.hasText(contractId)) {
                return MyJsonBean.errorData("合同ID不能为空");
            }

            log.info("删除合同，contractId={}", contractId);

            TblContract contract = contractMapper.selectById(contractId);
            if (contract == null) {
                return MyJsonBean.errorData("合同不存在");
            }

            // 只有草稿状态可以删除
            if (!"DRAFT".equals(contract.getContractStatus())) {
                return MyJsonBean.errorData("只有草稿状态的合同可以删除");
            }

            int result = contractMapper.deleteById(contractId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除合同失败，contractId={}", contractId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDelete(List<String> contractIds) {
        try {
            if (contractIds == null || contractIds.isEmpty()) {
                return MyJsonBean.errorData("合同ID列表不能为空");
            }

            log.info("批量删除合同，contractIds={}", contractIds);

            int count = 0;
            for (String contractId : contractIds) {
                TblContract contract = contractMapper.selectById(contractId);
                if (contract != null && "DRAFT".equals(contract.getContractStatus())) {
                    count += contractMapper.deleteById(contractId);
                }
            }

            return MyJsonBean.successData("批量删除成功，共删除 " + count + " 条记录");
        } catch (Exception e) {
            log.error("批量删除合同失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submit(String contractId) {
        try {
            if (!StringUtils.hasText(contractId)) {
                return MyJsonBean.errorData("合同ID不能为空");
            }

            log.info("提交合同，contractId={}", contractId);

            TblContract contract = contractMapper.selectById(contractId);
            if (contract == null) {
                return MyJsonBean.errorData("合同不存在");
            }

            if (!"DRAFT".equals(contract.getContractStatus())) {
                return MyJsonBean.errorData("只有草稿状态的合同可以提交");
            }

            contract.setContractStatus("SUBMITTED");

            int result = contractMapper.updateById(contract);
            if (result > 0) {
                return MyJsonBean.successData("提交成功");
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交合同失败，contractId={}", contractId, e);
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approve(String contractId, String action, String opinion) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(contractId)) {
                return MyJsonBean.errorData("合同ID不能为空");
            }

            log.info("审批合同，contractId={}, action={}, opinion={}", contractId, action, opinion);

            TblContract contract = contractMapper.selectById(contractId);
            if (contract == null) {
                return MyJsonBean.errorData("合同不存在");
            }

            if (!"SUBMITTED".equals(contract.getContractStatus()) && !"APPROVING".equals(contract.getContractStatus())) {
                return MyJsonBean.errorData("只有待审批状态的合同可以审批");
            }

            LocalDateTime now = LocalDateTime.now();
            contract.setApproveTime(now);
            contract.setApproveOpinion(opinion);
            contract.setApproverId(currentUser.getStaffid().toString());
            contract.setApproverName(currentUser.getUsername());

            if ("APPROVE".equals(action)) {
                contract.setContractStatus("APPROVED");
            } else if ("REJECT".equals(action)) {
                contract.setContractStatus("REJECTED");
            } else {
                return MyJsonBean.errorData("无效的审批动作");
            }

            int result = contractMapper.updateById(contract);
            if (result > 0) {
                return MyJsonBean.successData("审批成功");
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批合同失败，contractId={}", contractId, e);
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getPaymentPlans(String contractId) {
        try {
            if (!StringUtils.hasText(contractId)) {
                return MyJsonBean.errorData("合同ID不能为空");
            }

            log.info("查询合同收付款计划，contractId={}", contractId);

            List<TblContractPaymentPlan> plans = contractPaymentPlanMapper.selectByContractId(contractId);

            return MyJsonBean.successData("查询成功", plans);
        } catch (Exception e) {
            log.error("查询合同收付款计划失败，contractId={}", contractId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean savePaymentPlans(String contractId, List<TblContractPaymentPlan> plans) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(contractId)) {
                return MyJsonBean.errorData("合同ID不能为空");
            }

            if (plans == null || plans.isEmpty()) {
                return MyJsonBean.errorData("收付款计划列表不能为空");
            }

            log.info("保存合同收付款计划，contractId={}, plans count={}", contractId, plans.size());

            // 先删除旧的收付款计划
            contractPaymentPlanMapper.delete(new QueryWrapper<TblContractPaymentPlan>().eq("CONTRACT_ID", contractId));

            // 插入新的收付款计划
            LocalDateTime now = LocalDateTime.now();
            for (TblContractPaymentPlan plan : plans) {
                plan.setPlanId(UUID.randomUUID().toString().replace("-", ""));
                plan.setContractId(contractId);
                plan.setCreateTime(now);
                plan.setCreateUser(currentUser.getStaffid().toString());
                contractPaymentPlanMapper.insert(plan);
            }

            return MyJsonBean.successData("保存成功，共保存 " + plans.size() + " 条计划");
        } catch (Exception e) {
            log.error("保存合同收付款计划失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getByContractCode(String contractCode) {
        try {
            if (!StringUtils.hasText(contractCode)) {
                return MyJsonBean.errorData("合同编号不能为空");
            }

            log.info("根据合同编号查询，contractCode={}", contractCode);

            TblContract contract = contractMapper.selectByContractCode(contractCode);
            if (contract == null) {
                return MyJsonBean.errorData("合同不存在");
            }

            return MyJsonBean.successData("查询成功", contract);
        } catch (Exception e) {
            log.error("根据合同编号查询失败，contractCode={}", contractCode, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        log.warn("ContractService.export 暂未实现，param={}", param);
        return MyJsonBean.errorData("导出功能开发中");
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        log.warn("ContractService.getStatistics 暂未实现，param={}", param);
        return MyJsonBean.successData("查询成功", new HashMap<>());
    }

    @Override
    public MyJsonBean fulfill(String contractId, Map<String, Object> fulfillData) {
        log.warn("fulfill 暂未实现，contractId={}", contractId);
        return MyJsonBean.errorData("合同履约功能开发中");
    }

    @Override
    public MyJsonBean paymentRequest(String contractId, Map<String, Object> paymentData) {
        log.warn("paymentRequest 暂未实现，contractId={}", contractId);
        return MyJsonBean.errorData("付款申请功能开发中");
    }

    @Override
    public MyJsonBean modify(String contractId, Map<String, Object> modifyData) {
        log.warn("modify 暂未实现，contractId={}", contractId);
        return MyJsonBean.errorData("合同变更功能开发中");
    }

    @Override
    public MyJsonBean terminate(String contractId, Map<String, Object> terminateData) {
        log.warn("terminate 暂未实现，contractId={}", contractId);
        return MyJsonBean.errorData("合同终止功能开发中");
    }

    @Override
    public MyJsonBean getExecutionAnalysis(String contractId) {
        log.warn("getExecutionAnalysis 暂未实现，contractId={}", contractId);
        return MyJsonBean.successData("查询成功", new HashMap<>());
    }
}
