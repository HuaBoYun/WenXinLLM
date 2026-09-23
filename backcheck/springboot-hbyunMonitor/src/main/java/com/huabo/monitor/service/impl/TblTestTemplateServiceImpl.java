package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.monitor.oracle.entity.TblTestTemplate;
import com.huabo.monitor.oracle.mapper.TblTestTemplateMapper;
import com.huabo.monitor.service.TblTestTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class TblTestTemplateServiceImpl implements TblTestTemplateService {



    @Resource
    private TblTestTemplateMapper tblTestTemplateMapper;


    @Override
    public JsonBean getTestTemp(String token, Integer pageNumber, TblTestTemplate tblTestTemplate) {
        //return this.tblTestTemplateDao.findByPageBean(token, pageNumber, tblTestTemplate);
        return null;
    }

    @Override
    public JsonBean add(TblTestTemplate template) {
        if (template == null){
            return ResponseFormat.retParam(0,10002,null);
        }
        tblTestTemplateMapper.insert(template);
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public JsonBean updateById(BigDecimal testtemid) {
        if (testtemid == null){
            return ResponseFormat.retParam(0,10002,null);
        }
        tblTestTemplateMapper.updateById(testtemid);
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public JsonBean deleteById(String templeNumber) {
        if (templeNumber == null){
            return ResponseFormat.retParam(0,10002,null);
        }
        tblTestTemplateMapper.deleteById(templeNumber);
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public JsonBean selectList(String token, Integer pageNumber, Integer pageSize,Integer templeNumber, String templename) {
        if (templeNumber == null){
            return ResponseFormat.retParam(0,10002,null);
        }
        Page<TblTestTemplate>page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNumber);
        LambdaQueryWrapper<TblTestTemplate> wrapper = Wrappers.<TblTestTemplate>lambdaQuery();
        if (templeNumber!=null){
            wrapper.eq(TblTestTemplate::getTempleNumber,templeNumber);
        }
        if (StringUtils.isNotBlank(templename)){
            wrapper.eq(TblTestTemplate::getTemplename,templename);
        }
        tblTestTemplateMapper.selectPage(page,wrapper);
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public JsonBean save(TblTestTemplate tblTestTemplate) {
        return null;
    }

}
