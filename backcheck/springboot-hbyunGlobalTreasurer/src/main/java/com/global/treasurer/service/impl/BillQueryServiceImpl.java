package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblBillInstrument;
import com.global.treasurer.mapper.BillInstrumentMapper;
import com.global.treasurer.service.IBillQueryService;
import com.hbfk.util.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 票据查询Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class BillQueryServiceImpl implements IBillQueryService {
    @Autowired
    private BillInstrumentMapper billInstrumentMapper;

    @Override
    public Map<String, Object> getInstrumentLedger(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 获取分页参数
        int pageNum = 1;
        int pageSize = 10;
        if (params != null) {
            Object pageNumObj = params.get("pageNum");
            Object pageSizeObj = params.get("pageSize");
            if (pageNumObj != null) {
                pageNum = Integer.parseInt(pageNumObj.toString());
            }
            if (pageSizeObj != null) {
                pageSize = Integer.parseInt(pageSizeObj.toString());
            }
        }

        // 查询票据列表
        QueryWrapper<TblBillInstrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_FLAG", 0);

        // 添加查询条件
        if (params != null) {
            // 票据编号 - 支持前端字段名 instrumentNo 和 instrumentNumber
            Object instrumentNo = params.get("instrumentNo");
            if (instrumentNo == null) {
                instrumentNo = params.get("instrumentNumber");
            }
            if (instrumentNo != null && !"".equals(instrumentNo)) {
                queryWrapper.like("INSTRUMENT_NO", instrumentNo);
            }
            // 票据类型
            if (params.get("instrumentType") != null && !"".equals(params.get("instrumentType"))) {
                queryWrapper.eq("BILL_TYPE", params.get("instrumentType"));
            }
            // 票据状态
            if (params.get("instrumentStatus") != null && !"".equals(params.get("instrumentStatus"))) {
                queryWrapper.eq("BILL_STATUS", params.get("instrumentStatus"));
            }
            // 出票人 - 支持前端字段名 issuer 和 drawer
            Object issuer = params.get("issuer");
            if (issuer == null) {
                issuer = params.get("drawer");
            }
            if (issuer != null && !"".equals(issuer)) {
                queryWrapper.like("DRAWER", issuer);
            }
            // 收款人
            if (params.get("payee") != null && !"".equals(params.get("payee"))) {
                queryWrapper.like("PAYEE", params.get("payee"));
            }
            // 币种
            if (params.get("currencyCode") != null && !"".equals(params.get("currencyCode"))) {
                queryWrapper.eq("CURRENCY_CODE", params.get("currencyCode"));
            }
            // 到期日期范围（将字符串转为 java.sql.Date，避免 Oracle 日期比较类型不匹配）
            if (params.get("maturityDateStart") != null && !"".equals(params.get("maturityDateStart"))) {
                try {
                    queryWrapper.ge("MATURITY_DATE", Date.valueOf(params.get("maturityDateStart").toString()));
                } catch (IllegalArgumentException e) {
                    queryWrapper.ge("MATURITY_DATE", params.get("maturityDateStart"));
                }
            }
            if (params.get("maturityDateEnd") != null && !"".equals(params.get("maturityDateEnd"))) {
                try {
                    queryWrapper.le("MATURITY_DATE", Date.valueOf(params.get("maturityDateEnd").toString()));
                } catch (IllegalArgumentException e) {
                    queryWrapper.le("MATURITY_DATE", params.get("maturityDateEnd"));
                }
            }
        }

        queryWrapper.orderByDesc("CREATE_TIME");

        // 使用 MyBatis-Plus 分页查询
        Page<TblBillInstrument> page = new Page<>(pageNum, pageSize);
        IPage<TblBillInstrument> pageResult = billInstrumentMapper.selectPage(page, queryWrapper);

        // 转换为前端需要的格式
        List<Map<String, Object>> dataList = pageResult.getRecords().stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("instrumentId", item.getInstrumentId());
            // 前端表格使用 instrumentNo 字段
            map.put("instrumentNo", item.getInstrumentNumber());
            map.put("instrumentNumber", item.getInstrumentNumber());
            map.put("instrumentType", item.getInstrumentType());
            // 前端表格使用 faceAmount 字段
            map.put("faceAmount", item.getInstrumentAmount());
            map.put("instrumentAmount", item.getInstrumentAmount());
            map.put("currencyCode", item.getCurrency());
            map.put("issueDate", item.getIssueDate());
            map.put("maturityDate", item.getMaturityDate());
            // 前端表格使用 issuer 字段
            map.put("issuer", item.getDrawer());
            map.put("drawer", item.getDrawer());
            map.put("payee", item.getPayee());
            map.put("acceptor", item.getAcceptor());
            map.put("instrumentStatus", item.getInstrumentStatus());
            map.put("remark", item.getRemark());
            map.put("createTime", item.getCreateTime());
            return map;
        }).collect(Collectors.toList());

        // 返回分页信息
        result.put("total", pageResult.getTotal());
        result.put("list", dataList);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("pages", pageResult.getPages());
        return result;
    }

    @Override
    public Map<String, Object> getInstrumentStatistics(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount", 0);
        result.put("totalCount", 0);
        result.put("byType", new ArrayList<>());
        result.put("byStatus", new ArrayList<>());
        // TODO: 实现票据统计分析逻辑
        return result;
    }

    @Override
    public List<Map<String, Object>> getMaturityAlerts(Map<String, Object> params) {
        // TODO: 实现票据到期提醒逻辑
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getMaturityCalendar(Map<String, Object> params) {
        // TODO: 实现票据到期日历逻辑
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getInstrumentReport(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        // TODO: 实现票据报表查询逻辑
        return result;
    }

    @Override
    public Map<String, Object> getInstrumentDetail(Long instrumentId) {
        if (instrumentId == null) {
            throw new BizException("票据ID不能为空");
        }
        TblBillInstrument instrument = billInstrumentMapper.selectById(instrumentId);
        if (instrument == null) {
            throw new BizException("票据不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("instrumentId", instrument.getInstrumentId());
        result.put("instrumentNumber", instrument.getInstrumentNumber());
        result.put("instrumentType", instrument.getInstrumentType());
        result.put("instrumentAmount", instrument.getInstrumentAmount());
        result.put("currencyCode", instrument.getCurrency());
        result.put("issueDate", instrument.getIssueDate());
        result.put("maturityDate", instrument.getMaturityDate());
        result.put("drawer", instrument.getDrawer());
        result.put("payee", instrument.getPayee());
        result.put("acceptor", instrument.getAcceptor());
        result.put("instrumentStatus", instrument.getInstrumentStatus());
        result.put("remark", instrument.getRemark());
        return result;
    }

    @Override
    public List<Map<String, Object>> getInstrumentHistory(Long instrumentId) {
        if (instrumentId == null) {
            throw new BizException("票据ID不能为空");
        }
        // TODO: 实现票据流转历史查询逻辑
        return new ArrayList<>();
    }
}

