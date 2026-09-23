package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.YyTeam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IYyTeamService extends IService<YyTeam> {
    List<YyTeam> getByOrgidAndStaffid(BigDecimal orgid, BigDecimal staffid,String fxjktype);
}
