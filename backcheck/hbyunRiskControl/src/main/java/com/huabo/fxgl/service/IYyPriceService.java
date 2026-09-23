package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.YyPrice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
public interface IYyPriceService extends IService<YyPrice> {
    public List<YyPrice> getAll();
}
