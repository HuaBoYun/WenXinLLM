package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.BiPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-11
 */
public interface IBiPageService extends IService<BiPage> {
    List<BiPage> getByOrgidAndStaffid(BigDecimal orgid, BigDecimal staffid);
    List<BiPage> getInPageid(String pageids);
    
    List<String> getBiPageByCompanyid(BigDecimal companyid);
    
}
