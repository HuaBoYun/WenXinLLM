package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.mapper.IndicatorMapper;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
public class IndicatorServiceImpl extends ServiceImpl<IndicatorMapper, Indicator> implements IIndicatorService {

    @Autowired
    IndicatorMapper indicatorMapper;

    public Organization getHYFirst(){
        List<Organization> list = indicatorMapper.QueryFATHERORGID();
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyStatusRun(List<Indicator> list) {
        for (Indicator tblIndicator : list) {
            tblIndicator.setRunstatus(new BigDecimal("1"));
            if(indicatorMapper.selectById(tblIndicator.getIndicatorid()) != null){
                indicatorMapper.updateById(tblIndicator);
            }else{
                indicatorMapper.insert(tblIndicator);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyStatusRunError(List<Indicator> list) {
        for (Indicator tblIndicator : list) {
            tblIndicator.setRunstatus(new BigDecimal("3"));
            if(indicatorMapper.selectById(tblIndicator.getIndicatorid()) != null){
                indicatorMapper.updateById(tblIndicator);
            }else{
                indicatorMapper.insert(tblIndicator);
            }
        }
    }

    @Override
    public List<Indicator> showAllWithYj(String id) {
        return indicatorMapper.showAllWithYj(id);
    }

    public Integer selectIndicatorNumber(String number) {
        Integer count = indicatorMapper.listBySqlPageCount(number);
        return count;
    }

}
