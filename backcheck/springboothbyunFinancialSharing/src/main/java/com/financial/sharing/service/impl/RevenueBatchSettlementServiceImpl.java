package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.RevenueBatchSettlementEntity;
import com.financial.sharing.oracle.mapper.RevenueBatchSettlementMapper;
import com.financial.sharing.service.RevenueBatchSettlementService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.RevenueBatchSettlementQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收入批量结账Service实现类
 * 
 * @author AI Agent
 * @since 2025-11-29
 */
@Slf4j
@Service
public class RevenueBatchSettlementServiceImpl implements RevenueBatchSettlementService {
    
    @Autowired(required = false)
    @Qualifier("oracleRevenueBatchSettlementMapper")
    private RevenueBatchSettlementMapper revenueBatchSettlementMapper;
    
    @Override
    public PageResult<Map<String, Object>> getSettlementPage(RevenueBatchSettlementQueryParam param) {
        log.info("查询收入批量结账列表，参数: {}", param);
        PageResult<Map<String, Object>> result = new PageResult<>();

        try {
            if (revenueBatchSettlementMapper == null) {
                log.warn("revenueBatchSettlementMapper未注入，返回空数据");
                result.setTotalRecord(0);
                result.setCurrentPage(param.getPageNum());
                result.setPageSize(param.getPageSize());
                result.setTotalPage(0);
                result.setTlist(new ArrayList<>());
                return result;
            }

            // 使用 selectSettlementList 查询所有数据，然后手动分页
            List<Map<String, Object>> allRecords = revenueBatchSettlementMapper.selectSettlementList(param);

            if (allRecords == null) {
                allRecords = new ArrayList<>();
            }

            // 转换字段名为 camelCase（达梦数据库返回大写字段名）
            List<Map<String, Object>> convertedRecords = new ArrayList<>();
            for (Map<String, Object> record : allRecords) {
                convertedRecords.add(convertKeysToCamelCase(record));
            }

            int total = convertedRecords.size();
            int pageNum = param.getPageNum() != null ? param.getPageNum() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;
            int totalPages = (total + pageSize - 1) / pageSize;

            // 手动分页
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);
            List<Map<String, Object>> pageRecords = fromIndex < total ?
                    convertedRecords.subList(fromIndex, toIndex) : new ArrayList<>();

            result.setTotalRecord(total);
            result.setCurrentPage(pageNum);
            result.setPageSize(pageSize);
            result.setTotalPage(totalPages);
            result.setTlist(pageRecords);

            log.info("查询成功，总记录数: {}", total);
        } catch (Exception e) {
            log.error("查询收入批量结账列表失败", e);
            result.setTotalRecord(0);
            result.setCurrentPage(param.getPageNum());
            result.setPageSize(param.getPageSize());
            result.setTotalPage(0);
            result.setTlist(new ArrayList<>());
        }
        return result;
    }
    
    @Override
    public Map<String, Object> getSettlementById(Long settlementId) {
        log.info("查询结账详情，ID: {}", settlementId);
        if (revenueBatchSettlementMapper == null) {
            log.warn("revenueBatchSettlementMapper未注入");
            return null;
        }
        return revenueBatchSettlementMapper.selectSettlementById(settlementId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doSettlement(Long settlementId, Long userId, String userName) {
        log.info("执行结账，ID: {}, 用户: {}", settlementId, userName);
        if (revenueBatchSettlementMapper == null) {
            log.error("revenueBatchSettlementMapper未注入");
            return false;
        }
        
        RevenueBatchSettlementEntity entity = revenueBatchSettlementMapper.selectById(settlementId);
        if (entity == null) {
            log.error("结账记录不存在，ID: {}", settlementId);
            return false;
        }
        entity.setSettlementStatus(1);
        entity.setSettlementUserId(userId);
        entity.setSettlementUserName(userName);
        entity.setSettlementTime(LocalDateTime.now());

        return revenueBatchSettlementMapper.updateById(entity) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelSettlement(Long settlementId, Long userId, String userName) {
        log.info("取消结账，ID: {}, 用户: {}", settlementId, userName);
        if (revenueBatchSettlementMapper == null) {
            log.error("revenueBatchSettlementMapper未注入");
            return false;
        }
        
        RevenueBatchSettlementEntity entity = revenueBatchSettlementMapper.selectById(settlementId);
        if (entity == null) {
            log.error("结账记录不存在，ID: {}", settlementId);
            return false;
        }
        entity.setSettlementStatus(0);
        entity.setCancelUserId(userId);
        entity.setCancelUserName(userName);
        entity.setCancelTime(LocalDateTime.now());

        return revenueBatchSettlementMapper.updateById(entity) > 0;
    }

    /**
     * 将 Map 的 key 从大写下划线格式转换为 camelCase 格式
     * 例如: SETTLEMENT_ID -> settlementId, ORG_CODE -> orgCode
     */
    private Map<String, Object> convertKeysToCamelCase(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String camelCaseKey = toCamelCase(key);
            result.put(camelCaseKey, entry.getValue());
        }
        return result;
    }

    /**
     * 将大写格式的字符串转换为 camelCase 格式
     * 例如: SETTLEMENTID -> settlementId, ORGCODE -> orgCode
     * 支持的格式：
     * - 全大写无下划线: ORGCODE -> orgCode
     * - 大写下划线: ORG_CODE -> orgCode
     */
    private String toCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // 定义字段名映射表（全大写 -> 驼峰格式）
        java.util.Map<String, String> fieldMapping = new java.util.HashMap<>();
        fieldMapping.put("SETTLEMENTID", "settlementId");
        fieldMapping.put("ORGCODE", "orgCode");
        fieldMapping.put("ORGNAME", "orgName");
        fieldMapping.put("PERIODSCHEME", "periodScheme");
        fieldMapping.put("ACCOUNTINGPERIOD", "accountingPeriod");
        fieldMapping.put("SETTLEMENTSTATUS", "settlementStatus");
        fieldMapping.put("SETTLEMENTSTATUSNAME", "settlementStatusName");
        fieldMapping.put("SETTLEMENTUSERID", "settlementUserId");
        fieldMapping.put("SETTLEMENTUSERNAME", "settlementUserName");
        fieldMapping.put("SETTLEMENTTIME", "settlementTime");
        fieldMapping.put("CANCELUSERID", "cancelUserId");
        fieldMapping.put("CANCELUSERNAME", "cancelUserName");
        fieldMapping.put("CANCELTIME", "cancelTime");
        fieldMapping.put("CREATETIME", "createTime");
        fieldMapping.put("UPDATETIME", "updateTime");
        fieldMapping.put("BOOKID", "bookId");
        fieldMapping.put("TENANTID", "tenantId");
        fieldMapping.put("ISDELETED", "isDeleted");

        // 先尝试从映射表中查找
        String upperStr = str.toUpperCase();
        if (fieldMapping.containsKey(upperStr)) {
            return fieldMapping.get(upperStr);
        }

        // 如果映射表中没有，使用通用转换逻辑
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }
}

