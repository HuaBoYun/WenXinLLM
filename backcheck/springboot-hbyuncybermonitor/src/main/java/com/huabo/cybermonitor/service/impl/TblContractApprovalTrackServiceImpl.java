package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblContractApprovalTrack;
import com.huabo.cybermonitor.entity.TblContractRecord;
import com.huabo.cybermonitor.mapper.TblContractApprovalTrackMapper;
import com.huabo.cybermonitor.mapper.TblContractRecordMapper;
import com.huabo.cybermonitor.service.ITblContractApprovalTrackService;
import com.huabo.cybermonitor.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同审批追踪服务实现类
 */
@Service
public class TblContractApprovalTrackServiceImpl
        extends ServiceImpl<TblContractApprovalTrackMapper, TblContractApprovalTrack>
        implements ITblContractApprovalTrackService {

    @Autowired
    private TblContractRecordMapper contractRecordMapper;

    @Override
    public PageResult<Map<String, Object>> selectApprovalList(Map<String, Object> params) {
        // 手动解析参数（和controller其他接口保持一致风格）
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
        String contractName = params.get("contractName") != null ? params.get("contractName").toString() : null;
        String approvalStatus = params.get("approvalStatus") != null ? params.get("approvalStatus").toString() : null;

        LambdaQueryWrapper<TblContractRecord> wrapper = new LambdaQueryWrapper<>();

        // 合同名称模糊查询
        if (StringUtils.isNotBlank(contractName)) {
            wrapper.like(TblContractRecord::getContractName, contractName);
        }

        wrapper.orderByDesc(TblContractRecord::getCreateTime);

        // 判断是否需要按审批状态筛选
        boolean needStatusFilter = StringUtils.isNotBlank(approvalStatus);

        if (!needStatusFilter) {
            // 无状态筛选，直接数据库分页
            Page<TblContractRecord> page = new Page<>(pageNumber, pageSize);
            Page<TblContractRecord> result = contractRecordMapper.selectPage(page, wrapper);

            List<Map<String, Object>> list = new ArrayList<>();
            for (TblContractRecord record : result.getRecords()) {
                list.add(buildApprovalItem(record));
            }

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(list);
            return pageResult;
        }

        // 有状态筛选：先查全部符合名称条件的记录，Java层派生状态后过滤，再手动分页
        List<TblContractRecord> allRecords = contractRecordMapper.selectList(wrapper);

        // 按派生状态过滤
        List<Map<String, Object>> filteredList = new ArrayList<>();
        for (TblContractRecord record : allRecords) {
            String derivedStatus = deriveApprovalStatus(record);
            if (approvalStatus.equals(derivedStatus)) {
                filteredList.add(buildApprovalItem(record));
            }
        }

        // 手动分页
        int total = filteredList.size();
        int fromIndex = Math.min((pageNumber - 1) * pageSize, total);
        int toIndex = Math.min(pageNumber * pageSize, total);
        List<Map<String, Object>> pageList = filteredList.subList(fromIndex, toIndex);

        PageResult<Map<String, Object>> pageResult = new PageResult<>();
        pageResult.setTotalRecord(total);
        pageResult.setCurrentPage(pageNumber);
        pageResult.setPageNumber(pageNumber);
        pageResult.setTotalPage(pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0);
        pageResult.setPageSize(pageSize);
        pageResult.setTlist(pageList);
        return pageResult;
    }

    /**
     * 构建审批追踪列表项
     */
    private Map<String, Object> buildApprovalItem(TblContractRecord record) {
        Map<String, Object> map = new HashMap<>();
        map.put("contractId", record.getContractId());
        map.put("contractNo", record.getContractCode() != null ? record.getContractCode() : record.getContractId());
        map.put("contractName", record.getContractName());
        map.put("contractAmount", record.getContractAmount());
        map.put("applicant", record.getCompanyName() != null ? record.getCompanyName() : "");
        map.put("applyDate", record.getCreateTime() != null ? record.getCreateTime().toString() : "");
        map.put("currentApprover", record.getApprovalLevel() != null ? record.getApprovalLevel() : "");
        map.put("approvalStatus", deriveApprovalStatus(record));
        map.put("signReversed", deriveSignReversed(record));
        return map;
    }

    @Override
    public List<TblContractApprovalTrack> getApprovalFlow(String contractId) {
        return baseMapper.selectByContractId(contractId);
    }

    /**
     * 根据合同记录派生审批状态
     * 处理NULL值：NULL视为未审核（等同于'0'）
     */
    private String deriveApprovalStatus(TblContractRecord record) {
        String contractStatus = record.getContractStatus();
        String hasLegal = record.getHasLegalReview();
        String hasFinance = record.getHasFinanceReview();

        // NULL 统一视为未审核
        boolean legalPassed = "1".equals(hasLegal);
        boolean financePassed = "1".equals(hasFinance);

        if ("TERMINATED".equals(contractStatus)) {
            return "REJECTED";
        }
        if (legalPassed && financePassed) {
            return "APPROVED";
        }
        if ("DRAFT".equals(contractStatus)) {
            return "PENDING";
        }
        // 只有一个通过 → 审批中
        if ((legalPassed && !financePassed) || (!legalPassed && financePassed)) {
            return "IN_PROGRESS";
        }
        // 两个都未通过（包括NULL）→ 待审批
        return "PENDING";
    }

    /**
     * 派生签署反转标识
     * 合同状态为EXECUTING且法务或财务审核未通过（含NULL）时标记为Y
     */
    private String deriveSignReversed(TblContractRecord record) {
        if ("EXECUTING".equals(record.getContractStatus())
                && (!"1".equals(record.getHasLegalReview()) || !"1".equals(record.getHasFinanceReview()))) {
            return "Y";
        }
        return "N";
    }
}
