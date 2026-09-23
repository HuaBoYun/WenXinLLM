package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.NbsjStaffselect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
public interface INbsjStaffselectService extends IService<NbsjStaffselect> {



    List<NbsjStaffselect> getByUserId(BigDecimal staffid);

    }
