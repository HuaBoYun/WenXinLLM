package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.NbsjProject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Staff;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
public interface INbsjProjectService extends IService<NbsjProject> {


//    NbsjProject getSelectProject(Staff staff);


    NbsjProject getSelectProject(BigDecimal staffId);
}
