package com.huabo.compliance.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.compliance.entity.TblAssesslevel;
import com.huabo.compliance.mapper.TblAssesslevelMapper;
import com.huabo.compliance.service.TblAssesslevelService;

@Service("TblAssesslevelService")
public class TblAssesslevelServiceImpl implements TblAssesslevelService {

    @Resource
    private TblAssesslevelMapper tblAssesslevelMapper;

    @Override
    public JsonBean findByPageBean(Integer pageNumber, Integer pageSize,String orgid) throws Exception{
        if (pageNumber == null || pageNumber <= 0){
            pageNumber = 1;
        }
        if (pageSize <= 0){
            pageSize = 15;
        }
//        Page<TblAssesslevel> page = new Page<>();
//        page.setSize(pageSize);
//        page.setCurrent(pageNumber);
//        LambdaQueryWrapper<TblAssesslevel> wrapper = Wrappers.<TblAssesslevel>lambdaQuery();
//        if (StringUtils.isNotBlank(orgid)){
//            wrapper.eq(TblAssesslevel::getTblcomany, orgid);
//        }
//        Page<TblAssesslevel> tblAssesslevelPage = tblAssesslevelMapper.selectPage(page, wrapper);
//
//        
        
        PageInfo<TblAssesslevel> pageInfo = new PageInfo<TblAssesslevel>();
        TblAssesslevel temp = new TblAssesslevel();
    	temp.setTblcomany(orgid);
    	pageInfo.setCondition(temp);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(tblAssesslevelMapper.selectPageInfo(pageInfo));
    	pageInfo.setTotalRecord(tblAssesslevelMapper.selectPageCount(pageInfo));
        return ResponseFormat.retParam(1, 200, pageInfo);
    }

    @Override
    public JsonBean findById(BigDecimal id) {
        TblAssesslevel tblAssesslevel = tblAssesslevelMapper.selectById(id);
        if (null == tblAssesslevel){
            return ResponseFormat.retParam(0,50001,"未找到该数据");
        }
        return ResponseFormat.retParam(1, 200, tblAssesslevel);
    }

    @Override
    public JsonBean add(TblAssesslevel tblAssesslevel) {
        tblAssesslevelMapper.insert(tblAssesslevel);
        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public JsonBean update(TblAssesslevel tblAssesslevel) {
        if (null == tblAssesslevel){
            return ResponseFormat.retParam(0, 10002, "参数为空，请检查后重试");
        }
        BigDecimal asslevid = tblAssesslevel.getAsslevid();
        TblAssesslevel dbAssesslevel = tblAssesslevelMapper.selectById(asslevid);
        if (null == dbAssesslevel){
            return ResponseFormat.retParam(0, 50001, "跟新数据 不存在，请检查后重试");
        }
        tblAssesslevelMapper.updateById(tblAssesslevel);
        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public JsonBean delete(BigDecimal asslevid) {
        if (null == asslevid){
            return ResponseFormat.retParam(0, 10002, "缺少参数，请检查重试");
        }
        TblAssesslevel dbAssesslevel = tblAssesslevelMapper.selectById(asslevid);
        if (null == dbAssesslevel){
            return ResponseFormat.retParam(0, 50001, "要删除的数据不存在");
        }
        tblAssesslevelMapper.deleteById(asslevid);
        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public List<TblAssesslevel> findAll(String tblCompany) {
        return null;
    }
}
