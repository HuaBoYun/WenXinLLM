package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.RiskClaim;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskClaimService extends IService<RiskClaim> {

    JsonBean getByEventId(String riseveid);
}
