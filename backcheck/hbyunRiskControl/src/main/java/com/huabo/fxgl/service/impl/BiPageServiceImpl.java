package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.BiPage;
import com.huabo.fxgl.mapper.BiPageMapper;
import com.huabo.fxgl.service.IBiPageService;
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
 * @since 2022-08-11
 */
@Service
public class BiPageServiceImpl extends ServiceImpl<BiPageMapper, BiPage> implements IBiPageService {

    @Override
    public List<BiPage> getByOrgidAndStaffid(BigDecimal orgid, BigDecimal staffid) {
        return baseMapper.selectList(orgid, staffid);
    }

    @Override
    public List<BiPage> getInPageid(String pageids) {
        return baseMapper.selectListInPageid(pageids);
    }
    
    @Override
    public List<String> getBiPageByCompanyid(BigDecimal companyid) {
        return baseMapper.getBiPageByCompanyid(companyid);
    }
    
}
