package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.YyPrice;
import com.huabo.fxgl.mapper.YyPriceMapper;
import com.huabo.fxgl.service.IYyPriceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
@Service
public class YyPriceServiceImpl extends ServiceImpl<YyPriceMapper, YyPrice> implements IYyPriceService {

    @Override
    public List<YyPrice> getAll() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ge("PRICEID", 2);
        queryWrapper.orderByAsc("PRICEID");
        return list(queryWrapper);
    }
}
