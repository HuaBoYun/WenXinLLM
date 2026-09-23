package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.RevenueContractEntity;
import com.financial.sharing.oracle.mapper.RevenueContractMapper;
import com.financial.sharing.service.RevenueContractService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.RevenueContractVO;
import com.financial.sharing.vo.param.RevenueContractQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入合同服务实现类
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class RevenueContractServiceImpl implements RevenueContractService {

    @Autowired
    @Qualifier("oracleRevenueContractMapper")
    private RevenueContractMapper revenueContractMapper;

    @Override
    public PageResult<RevenueContractVO> getRevenueContractList(RevenueContractQueryParam param) {
        try {
            log.info("开始查询收入合同列表, 参数: pageNum={}, pageSize={}", param.getPageNum(), param.getPageSize());

            // 检查Mapper是否正确注入
            if (revenueContractMapper == null) {
                log.error("revenueContractMapper未正确注入");
                throw new RuntimeException("系统配置错误：Mapper未正确注入");
            }

            // 查询所有数据
            List<RevenueContractEntity> allRecords = revenueContractMapper.selectRevenueContractList(param);

            // 检查查询结果是否为null
            if (allRecords == null) {
                log.warn("查询结果为null，返回空列表");
                allRecords = new ArrayList<>();
            }

            log.info("查询成功，总记录数: {}", allRecords.size());

            // 手动分页
            int pageNum = param.getPageNum() != null ? param.getPageNum() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;
            int total = allRecords.size();
            int totalPages = (total + pageSize - 1) / pageSize;

            // 计算分页数据
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<RevenueContractEntity> pagedRecords;
            if (fromIndex >= total) {
                pagedRecords = new ArrayList<>();
            } else {
                pagedRecords = allRecords.subList(fromIndex, toIndex);
            }

            // 转换为VO
            List<RevenueContractVO> voList = new ArrayList<>();
            for (RevenueContractEntity entity : pagedRecords) {
                RevenueContractVO vo = new RevenueContractVO();

                // 手动转换字段,处理类型不匹配问题
                vo.setContractId(entity.getContractId() != null ? String.valueOf(entity.getContractId()) : null);
                vo.setContractNo(entity.getContractNo());
                vo.setContractName(entity.getContractName());
                vo.setCustomerId(entity.getCustomerId() != null ? String.valueOf(entity.getCustomerId()) : null);
                vo.setContractAmount(entity.getContractAmount());
                vo.setContractStatus(entity.getContractStatus() != null ? String.valueOf(entity.getContractStatus()) : null);
                vo.setStatusDesc(getContractStatusName(entity.getContractStatus()));

                // 转换日期
                if (entity.getSignDate() != null) {
                    vo.setSignDate(java.sql.Date.valueOf(entity.getSignDate()));
                }
                if (entity.getEffectiveDate() != null) {
                    vo.setEffectiveDate(java.sql.Date.valueOf(entity.getEffectiveDate()));
                }
                if (entity.getExpiryDate() != null) {
                    vo.setExpiryDate(java.sql.Date.valueOf(entity.getExpiryDate()));
                }
                if (entity.getCreateTime() != null) {
                    vo.setCreateTime(java.sql.Timestamp.valueOf(entity.getCreateTime()));
                }
                if (entity.getUpdateTime() != null) {
                    vo.setUpdateTime(java.sql.Timestamp.valueOf(entity.getUpdateTime()));
                }

                voList.add(vo);
            }

            // 构建返回结果
            PageResult<RevenueContractVO> result = new PageResult<>();
            result.setTlist(voList);
            result.setTotalRecord(total);
            result.setCurrentPage(pageNum);
            result.setPageSize(pageSize);
            result.setTotalPage(totalPages);

            return result;
        } catch (Exception e) {
            log.error("查询收入合同列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取合同状态名称
     */
    private String getContractStatusName(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "待生效";
            case 2: return "履行中";
            case 3: return "已完成";
            case 4: return "已终止";
            default: return "未知";
        }
    }

    /**
     * 获取确认方法名称
     */
    private String getRecognitionMethodName(Integer method) {
        if (method == null) return "未知";
        switch (method) {
            case 1: return "时点法";
            case 2: return "时段法";
            default: return "未知";
        }
    }
}

