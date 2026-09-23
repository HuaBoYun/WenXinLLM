package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.RiskAttWord;
import com.huabo.fxgl.mapper.RiskAttWordMapper;
import com.huabo.fxgl.service.IRiskAttWordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-19
 */
@Service
public class RiskAttWordServiceImpl extends ServiceImpl<RiskAttWordMapper, RiskAttWord> implements IRiskAttWordService {

    @Autowired
    private RiskAttWordMapper riskAttWordMapper;

    @Override
    public RiskAttWord getFile(String type, String orgid, String id) {

        List<RiskAttWord> list = riskAttWordMapper.getFile(type,orgid,id);
        if (list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }
}
