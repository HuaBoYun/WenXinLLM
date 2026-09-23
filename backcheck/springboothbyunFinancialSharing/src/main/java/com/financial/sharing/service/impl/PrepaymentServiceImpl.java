package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dto.param.PrepaymentQueryParam;
import com.financial.sharing.mysql.mapper.PrepaymentMapper;
import com.financial.sharing.mysql.entity.TblPrepayment;
import com.financial.sharing.service.PrepaymentService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PrepaymentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 预付款Service实现类
 * @author system
 * @since 2025-01-13
 */
@Slf4j
@Service
public class PrepaymentServiceImpl implements PrepaymentService {

    @Resource
    private PrepaymentMapper prepaymentMapper;

    @Override
    public PageResult<PrepaymentVO> queryPage(PrepaymentQueryParam param) {
        log.info("分页查询预付款列表，参数: {}", param);

        // 计算偏移量
        int offset = (param.getPageNo() - 1) * param.getPageSize();

        // 查询数据列表
        List<PrepaymentVO> list = prepaymentMapper.selectPrepaymentPage(
            param,
            offset,
            param.getPageSize()
        );

        // 查询总数
        Long total = prepaymentMapper.selectPrepaymentCount(param);

        // 构建分页结果
        PageResult<PrepaymentVO> pageResult = new PageResult<>();
        pageResult.setTlist(list);
        pageResult.setTotalRecord(total.intValue()); // Long 转换为 Integer
        pageResult.setCurrentPage(param.getPageNo());
        pageResult.setPageSize(param.getPageSize());

        log.info("查询完成，共{}条记录", total);
        return pageResult;
    }

    @Override
    public PrepaymentVO getDetail(String prepaymentId) {
        log.info("查询预付款详情，prepaymentId={}", prepaymentId);

        PrepaymentVO detail = prepaymentMapper.selectPrepaymentById(prepaymentId);

        if (detail == null) {
            log.warn("预付款不存在，prepaymentId={}", prepaymentId);
            return null;
        }

        log.info("查询成功，预付款单号={}", detail.getPrepaymentNo());
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offsetPrepayment(String prepaymentId, String documentId, BigDecimal offsetAmount) {
        log.info("预付款冲销，prepaymentId={}, documentId={}, offsetAmount={}", prepaymentId, documentId, offsetAmount);

        // 1. 查询预付款记录
        TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
        if (prepayment == null) {
            throw new RuntimeException("预付款记录不存在");
        }

        // 2. 校验冲销金额
        if (prepayment.getRemainingAmount().compareTo(offsetAmount) < 0) {
            throw new RuntimeException("冲销金额不能超过剩余金额");
        }

        // 3. 更新预付款的已冲销金额和剩余金额
        BigDecimal newOffsetAmount = prepayment.getOffsetAmount().add(offsetAmount);
        BigDecimal newRemainingAmount = prepayment.getRemainingAmount().subtract(offsetAmount);

        prepayment.setOffsetAmount(newOffsetAmount);
        prepayment.setRemainingAmount(newRemainingAmount);

        // 4. 更新预付款状态
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            prepayment.setPrepaymentStatus(2); // 全部冲销
        } else if (newOffsetAmount.compareTo(BigDecimal.ZERO) > 0) {
            prepayment.setPrepaymentStatus(1); // 部分冲销
        }

        prepaymentMapper.updateById(prepayment);

        log.info("预付款冲销成功，prepaymentNo={}, 新已冲销金额={}, 新剩余金额={}, 新状态={}",
            prepayment.getPrepaymentNo(), newOffsetAmount, newRemainingAmount, prepayment.getPrepaymentStatus());
    }
}
