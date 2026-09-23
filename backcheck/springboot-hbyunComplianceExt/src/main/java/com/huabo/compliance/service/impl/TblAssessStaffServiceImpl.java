package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblAssessStaff;
import com.huabo.compliance.mapper.TblAssessStaffMapper;
import com.huabo.compliance.mapper.YhrPageMapper;
import com.huabo.compliance.service.ITblAssessStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service
public class TblAssessStaffServiceImpl extends ServiceImpl<TblAssessStaffMapper, TblAssessStaff> implements ITblAssessStaffService {

    @Resource
    YhrPageMapper  yhrPageMapper;


    @Override
    public void updateAttidNullById(BigDecimal bigDecimal) {
        String sql="update TBL_ASSESS_STAFF set attid=null where assstaffid="+bigDecimal;
        yhrPageMapper.update(sql);
    }
}
