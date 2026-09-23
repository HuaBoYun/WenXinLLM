package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblStaff;
import com.huabo.compliance.mapper.TblStaffMapper;
import com.huabo.compliance.service.ITblStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service

public class TblStaffServiceImpl extends ServiceImpl<TblStaffMapper, TblStaff> implements ITblStaffService {
    @Resource
    TblStaffMapper  staffMapper;


    @Override
    public void findStaffByOrgid(IPage<TblStaff> page, BigDecimal orgid) {
        staffMapper.findStaffByOrgid(page,orgid);
    }
    
    @Override
    public TblStaff getStaff(BigDecimal staffid) {
        return staffMapper.selectById(staffid);
    }
}
