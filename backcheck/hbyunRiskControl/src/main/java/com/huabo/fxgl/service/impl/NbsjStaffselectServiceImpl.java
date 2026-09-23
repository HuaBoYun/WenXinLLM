package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.NbsjStaffselect;
import com.huabo.fxgl.mapper.NbsjStaffselectMapper;
import com.huabo.fxgl.service.INbsjStaffselectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Service
public class NbsjStaffselectServiceImpl extends ServiceImpl<NbsjStaffselectMapper, NbsjStaffselect> implements INbsjStaffselectService {

    @Override
    public List<NbsjStaffselect> getByUserId(BigDecimal staffid) {
        QueryWrapper<NbsjStaffselect> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("STAFFID",staffid);
        return this.list(queryWrapper);
    }
}
