package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillRegistrationDTO;
import com.global.treasurer.dto.BillRegistrationQueryDTO;
import com.global.treasurer.entity.TblBillRegistration;
import com.global.treasurer.mapper.BillRegistrationMapper;
import com.global.treasurer.service.IBillRegistrationService;
import com.global.treasurer.vo.BillRegistrationVO;
import com.hbfk.util.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 票据登记Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillRegistrationServiceImpl extends ServiceImpl<BillRegistrationMapper, TblBillRegistration>
        implements IBillRegistrationService {
    private static final Logger log = LoggerFactory.getLogger(BillRegistrationServiceImpl.class);

    @Override
    public PageInfo<BillRegistrationVO> selectBillRegistrationList(BillRegistrationQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<BillRegistrationVO> list = baseMapper.selectBillRegistrationList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BillRegistrationVO selectBillRegistrationById(Long billId) {
        if (billId == null) {
            throw new BizException("票据ID不能为空");
        }
        return baseMapper.selectBillRegistrationById(billId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillRegistration insertBillRegistration(BillRegistrationDTO dto) {
        // 校验票据号码是否已存在
        TblBillRegistration existBill = baseMapper.selectByBillNumber(dto.getBillNumber());
        if (existBill != null) {
            throw new BizException("票据号码已存在: " + dto.getBillNumber());
        }

        // 校验到期日期必须大于出票日期
        if (dto.getMaturityDate().before(dto.getIssueDate())) {
            throw new BizException("到期日期必须大于出票日期");
        }

        TblBillRegistration bill = new TblBillRegistration();
        BeanUtils.copyProperties(dto, bill);
        
        // 设置默认值
        if (bill.getCurrency() == null || bill.getCurrency().isEmpty()) {
            bill.setCurrency("CNY");
        }
        if (bill.getBillStatus() == null || bill.getBillStatus().isEmpty()) {
            bill.setBillStatus("HOLDING");
        }
        bill.setDeleteFlag(0);
        bill.setCreateTime(new Date());
        bill.setUpdateTime(new Date());
        
        // 生成票据ID(使用雪花算法或序列)
        // 这里简化处理,实际应该使用分布式ID生成器
        bill.setBillId(System.currentTimeMillis());
        
        baseMapper.insert(bill);
        return bill;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillRegistration updateBillRegistration(BillRegistrationDTO dto) {
        if (dto.getBillId() == null) {
            throw new BizException("票据ID不能为空");
        }

        // 检查票据是否存在
        TblBillRegistration existBill = baseMapper.selectById(dto.getBillId());
        if (existBill == null || existBill.getDeleteFlag() == 1) {
            throw new BizException("票据不存在或已删除");
        }

        // 如果修改了票据号码,检查新号码是否已存在
        if (!existBill.getBillNumber().equals(dto.getBillNumber())) {
            TblBillRegistration duplicateBill = baseMapper.selectByBillNumber(dto.getBillNumber());
            if (duplicateBill != null && !duplicateBill.getBillId().equals(dto.getBillId())) {
                throw new BizException("票据号码已存在: " + dto.getBillNumber());
            }
        }

        // 校验到期日期必须大于出票日期
        if (dto.getMaturityDate().before(dto.getIssueDate())) {
            throw new BizException("到期日期必须大于出票日期");
        }

        TblBillRegistration bill = new TblBillRegistration();
        BeanUtils.copyProperties(dto, bill);
        bill.setUpdateTime(new Date());
        
        baseMapper.updateById(bill);
        return bill;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBillRegistrationByIds(Long[] billIds) {
        if (billIds == null || billIds.length == 0) {
            throw new BizException("请选择要删除的票据");
        }

        int result = baseMapper.deleteBillRegistrationByIds(billIds);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchImportBills(List<BillRegistrationDTO> billList) {
        if (billList == null || billList.isEmpty()) {
            throw new BizException("导入数据不能为空");
        }

        int successCount = 0;
        for (BillRegistrationDTO dto : billList) {
            try {
                // 校验票据号码是否已存在
                TblBillRegistration existBill = baseMapper.selectByBillNumber(dto.getBillNumber());
                if (existBill != null) {
                    continue; // 跳过已存在的票据
                }

                TblBillRegistration bill = new TblBillRegistration();
                BeanUtils.copyProperties(dto, bill);

                // 设置默认值
                if (bill.getCurrency() == null || bill.getCurrency().isEmpty()) {
                    bill.setCurrency("CNY");
                }
                if (bill.getBillStatus() == null || bill.getBillStatus().isEmpty()) {
                    bill.setBillStatus("HOLDING");
                }
                bill.setDeleteFlag(0);
                bill.setCreateTime(new Date());
                bill.setUpdateTime(new Date());
                bill.setBillId(System.currentTimeMillis() + successCount);

                baseMapper.insert(bill);
                successCount++;
            } catch (Exception e) {
                // 记录失败的票据，继续处理下一条
                log.warn("导入票据失败: {}, 原因: {}", dto.getBillNumber(), e.getMessage());
            }
        }
        return successCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelBill(Long billId, String reason) {
        if (billId == null) {
            throw new BizException("票据ID不能为空");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new BizException("作废原因不能为空");
        }

        // 检查票据是否存在
        TblBillRegistration bill = baseMapper.selectById(billId);
        if (bill == null || bill.getDeleteFlag() == 1) {
            throw new BizException("票据不存在或已删除");
        }

        // 检查票据状态是否允许作废
        if ("CANCELLED".equals(bill.getBillStatus())) {
            throw new BizException("票据已作废，不能重复操作");
        }
        if ("TRANSFERRED".equals(bill.getBillStatus()) || "DISCOUNTED".equals(bill.getBillStatus())) {
            throw new BizException("票据已流转，不能作废");
        }

        // 更新票据状态为作废
        bill.setBillStatus("CANCELLED");
        bill.setRemark(reason);
        bill.setUpdateTime(new Date());

        return baseMapper.updateById(bill) > 0;
    }
}

