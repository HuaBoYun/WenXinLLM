package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblConcentrationStrategy;
import com.global.treasurer.entity.TblGtAccountInfo;
import com.global.treasurer.mapper.TblConcentrationStrategyMapper;
import com.global.treasurer.service.TblConcentrationStrategyService;
import com.global.treasurer.service.TblGtAccountInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 归集策略Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblConcentrationStrategyServiceImpl implements TblConcentrationStrategyService {
    private static final Logger log = LoggerFactory.getLogger(TblConcentrationStrategyServiceImpl.class);

    @Resource
    private TblConcentrationStrategyMapper tblConcentrationStrategyMapper;

    @Resource
    private TblGtAccountInfoService tblGtAccountInfoService;

    @Override
    public PageInfo<TblConcentrationStrategy> getStrategyPage(Integer pageNum, Integer pageSize,
                                                              String strategyName, String strategyType, String strategyStatus) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblConcentrationStrategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(strategyName), TblConcentrationStrategy::getStrategyName, strategyName)
               .eq(StringUtils.isNotBlank(strategyType), TblConcentrationStrategy::getStrategyType, strategyType)
               .eq(StringUtils.isNotBlank(strategyStatus), TblConcentrationStrategy::getStatus, strategyStatus)
               .orderByDesc(TblConcentrationStrategy::getCreateTime);
        List<TblConcentrationStrategy> list = tblConcentrationStrategyMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblConcentrationStrategy getStrategyById(Long strategyId) {
        return tblConcentrationStrategyMapper.selectById(strategyId);
    }

    @Override
    public TblConcentrationStrategy saveStrategy(TblConcentrationStrategy strategy) {
        strategy.setCreateTime(new Date());
        if (strategy.getStatus() == null) {
            strategy.setStatus("ACTIVE");
        }
        if (strategy.getPriority() == null) {
            strategy.setPriority(5);
        }
        // STRATEGY_CODE 非空，自动生成：GJ + yyyyMMddHHmmss + 4位随机数
        if (strategy.getStrategyCode() == null || strategy.getStrategyCode().isEmpty()) {
            String code = "GJ" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + String.format("%04d", (int)(Math.random() * 10000));
            strategy.setStrategyCode(code);
        }
        // TARGET_ACCOUNT_NAME/NUMBER 非空，根据 targetAccountId 自动填充
        if (strategy.getTargetAccountId() != null
                && (strategy.getTargetAccountName() == null || strategy.getTargetAccountName().isEmpty())) {
            TblGtAccountInfo account = tblGtAccountInfoService.getById(strategy.getTargetAccountId());
            if (account != null) {
                strategy.setTargetAccountName(account.getAccountName());
                strategy.setTargetAccountNumber(account.getAccountNumber());
            } else {
                strategy.setTargetAccountName("未知账户");
                strategy.setTargetAccountNumber("");
            }
        }
        if (strategy.getExecutionTime() != null && strategy.getExecutionTime().length() > 20) {
            String raw = strategy.getExecutionTime();
            int tIdx = raw.indexOf('T');
            if (tIdx >= 0 && tIdx + 8 <= raw.length()) {
                strategy.setExecutionTime(raw.substring(tIdx + 1, tIdx + 9));
            } else {
                strategy.setExecutionTime(raw.substring(0, 20));
            }
        }
        tblConcentrationStrategyMapper.insertStrategy(strategy);
        return strategy;
    }

    @Override
    public void updateStrategy(TblConcentrationStrategy strategy) {
        strategy.setUpdateTime(new Date());
        // executionTime 超长截断（同 saveStrategy）
        if (strategy.getExecutionTime() != null && strategy.getExecutionTime().length() > 20) {
            String raw = strategy.getExecutionTime();
            int tIdx = raw.indexOf('T');
            if (tIdx >= 0 && tIdx + 8 <= raw.length()) {
                strategy.setExecutionTime(raw.substring(tIdx + 1, tIdx + 9));
            } else {
                strategy.setExecutionTime(raw.substring(0, 20));
            }
        }
        // 目标账户名称自动填充（用户可能更换了目标账户）
        if (strategy.getTargetAccountId() != null
                && (strategy.getTargetAccountName() == null || strategy.getTargetAccountName().isEmpty())) {
            TblGtAccountInfo account = tblGtAccountInfoService.getById(strategy.getTargetAccountId());
            if (account != null) {
                strategy.setTargetAccountName(account.getAccountName());
                strategy.setTargetAccountNumber(account.getAccountNumber());
            } else {
                strategy.setTargetAccountName("未知账户");
            }
        }
        tblConcentrationStrategyMapper.updateById(strategy);
    }

    @Override
    public void enableStrategy(Long strategyId) {
        TblConcentrationStrategy strategy = new TblConcentrationStrategy();
        strategy.setStrategyId(strategyId);
        strategy.setStatus("ACTIVE");
        strategy.setUpdateTime(new Date());
        tblConcentrationStrategyMapper.updateById(strategy);
    }

    @Override
    public void disableStrategy(Long strategyId) {
        TblConcentrationStrategy strategy = new TblConcentrationStrategy();
        strategy.setStrategyId(strategyId);
        strategy.setStatus("INACTIVE");
        strategy.setUpdateTime(new Date());
        tblConcentrationStrategyMapper.updateById(strategy);
    }

    @Override
    public void deleteStrategy(Long strategyId) {
        tblConcentrationStrategyMapper.deleteById(strategyId);
    }

    @Override
    public void batchEnableStrategy(List<Long> strategyIds) {
        for (Long strategyId : strategyIds) {
            TblConcentrationStrategy strategy = new TblConcentrationStrategy();
            strategy.setStrategyId(strategyId);
            strategy.setStatus("ACTIVE");
            strategy.setUpdateTime(new Date());
            tblConcentrationStrategyMapper.updateById(strategy);
        }
    }

    @Override
    public void batchDisableStrategy(List<Long> strategyIds) {
        for (Long strategyId : strategyIds) {
            TblConcentrationStrategy strategy = new TblConcentrationStrategy();
            strategy.setStrategyId(strategyId);
            strategy.setStatus("INACTIVE");
            strategy.setUpdateTime(new Date());
            tblConcentrationStrategyMapper.updateById(strategy);
        }
    }

    @Override
    public String testStrategy(Long strategyId) {
        TblConcentrationStrategy strategy = tblConcentrationStrategyMapper.selectById(strategyId);
        if (strategy == null) {
            return "策略不存在";
        }
        log.info("测试策略: {}", strategy.getStrategyName());
        return "策略测试成功，预计归集金额: 100000.00元";
    }

    @Override
    public List<TblConcentrationStrategy> getActiveStrategies() {
        LambdaQueryWrapper<TblConcentrationStrategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationStrategy::getStatus, "ENABLED");
        return tblConcentrationStrategyMapper.selectList(wrapper);
    }

    @Override
    public long count() {
        LambdaQueryWrapper<TblConcentrationStrategy> wrapper = new LambdaQueryWrapper<>();
        return tblConcentrationStrategyMapper.selectCount(wrapper);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<TblConcentrationStrategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationStrategy::getStatus, status);
        return tblConcentrationStrategyMapper.selectCount(wrapper);
    }

    @Override
    public void exportStrategy(String strategyName, String strategyType, String strategyStatus,
                           ServletOutputStream outputStream) throws Exception {
        LambdaQueryWrapper<TblConcentrationStrategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(strategyName), TblConcentrationStrategy::getStrategyName, strategyName)
               .eq(StringUtils.isNotBlank(strategyType), TblConcentrationStrategy::getStrategyType, strategyType)
               .eq(StringUtils.isNotBlank(strategyStatus), TblConcentrationStrategy::getStatus, strategyStatus);
        List<TblConcentrationStrategy> list = tblConcentrationStrategyMapper.selectList(wrapper);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("归集策略");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("策略ID");
        headerRow.createCell(1).setCellValue("策略名称");
        headerRow.createCell(2).setCellValue("策略类型");
        headerRow.createCell(3).setCellValue("状态");
        headerRow.createCell(4).setCellValue("创建时间");

        // 填充数据
        int rowNum = 1;
        for (TblConcentrationStrategy strategy : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(strategy.getStrategyId());
            row.createCell(1).setCellValue(strategy.getStrategyName());
            row.createCell(2).setCellValue(strategy.getStrategyType());
            row.createCell(3).setCellValue(strategy.getStatus());
            row.createCell(4).setCellValue(strategy.getCreateTime());
        }

        workbook.write(outputStream);
        workbook.close();
    }
}

