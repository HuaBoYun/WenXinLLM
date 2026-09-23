package com.huabo.fxgl.service;


import com.huabo.fxgl.entity.Criterion;
import com.huabo.fxgl.entity.NbkzRisk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.mapper.NbkzRiskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
public interface INbkzRiskService extends IService<NbkzRisk> {
    NbkzRisk get(String riskid);
    NbkzRisk getBycode(String risknumber,String type,String orgid);
    List<Criterion> findAll(String orgid);
}
