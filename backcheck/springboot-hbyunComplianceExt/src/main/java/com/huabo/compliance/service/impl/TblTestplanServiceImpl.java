package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.compliance.entity.TblTestTemplate;
import com.huabo.compliance.mapper.TblTestplanMapper;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.service.ITblTestplanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
@Service
@Transactional
public class TblTestplanServiceImpl implements ITblTestplanService {

    @Resource
    private TblTestplanMapper tblTestplanMapper;

    @Override
    public void save(TblTestplan tblTestplan) throws Exception{
        tblTestplanMapper.insertEntity(tblTestplan);
    }
    
    @Override
    public void update(TblTestplan tblTestplan) throws Exception{
        tblTestplanMapper.updateEntity(tblTestplan);
    }

    @Override
    public void add(TblTestplan tblTestplan) throws Exception{
        tblTestplanMapper.insert(tblTestplan);
    }


    @Override
    public TblTestplan getById(BigDecimal testplanid) {

        return tblTestplanMapper.findById(testplanid);
    }

    @Override
    public JsonBean selectList(String plannumber, String planname, String planstatus, Date starttime_min, Date starttime_max, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public JsonBean updateById(BigDecimal testplanid) {
        if (testplanid==null){
            return ResponseFormat.retParam(0,10002,null);
        }
        tblTestplanMapper.updateById(testplanid);
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public void deleteById(BigDecimal testplanid) {
        tblTestplanMapper.deleteById(testplanid);
    }

    @Override
    public JsonBean saveAll(List<String> plans) {
        for (String plan : plans) {
            if (plan==null){
                return ResponseFormat.retParam(0,10002,null);
            }
            tblTestplanMapper.insert(plan);
        }
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public JsonBean select(String planname, String planyear, Integer pageNumber, Integer pageSize) {
        Page<TblTestTemplate> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNumber);
        LambdaQueryWrapper<TblTestplan> wrapper = Wrappers.<TblTestplan>lambdaQuery();
        if (planname!=null){
            wrapper.eq(TblTestplan::getPlanname,planname);
        }
        if (StringUtils.isNotBlank(planyear)){
            wrapper.eq(TblTestplan::getPlanyear,planyear);
        }
        tblTestplanMapper.selectPage(page,wrapper);
        return ResponseFormat.retParam(1,200,null);
    }
   
	
}
