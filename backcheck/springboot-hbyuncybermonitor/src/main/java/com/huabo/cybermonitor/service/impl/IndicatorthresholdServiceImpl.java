package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.Indicatorthreshold;
import com.huabo.cybermonitor.mapper.IndicatorthresholdMapper;
import com.huabo.cybermonitor.service.IIndicatorthresholdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Service
@Transactional
public class IndicatorthresholdServiceImpl extends ServiceImpl<IndicatorthresholdMapper, Indicatorthreshold> implements IIndicatorthresholdService {

    @Autowired
    IndicatorthresholdMapper indicatorthresholdMapper;

    @Override
    public List<Indicatorthreshold> QueryByIndicatorId(String indicator) {
        String sql = "select * from TBL_INDICATORTHRESHOLD where INDICATORID = "+indicator;
        sql+=" ORDER BY SEQUENCENUMBER ";
        return indicatorthresholdMapper.QueryByIndicatorId(Integer.parseInt(indicator));
    }

}
