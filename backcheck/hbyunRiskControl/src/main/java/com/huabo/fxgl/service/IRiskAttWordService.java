package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskAttWord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-19
 */
public interface IRiskAttWordService extends IService<RiskAttWord> {
    RiskAttWord getFile(String type,String orgid,String id);
}
