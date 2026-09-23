package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskRectifysolution;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.util.PageEx;

import javax.websocket.Session;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskRectifysolutionService extends IService<RiskRectifysolution> {

    Integer findSolutioncode(String solutioncode);

    RiskRectifysolution getRiskRectifysolution(String rectsolid);

    IPage<RiskRectifysolution> getRiskRectifysolutionPage(IPage page, String eventid, String code, String name, String userName);


}
