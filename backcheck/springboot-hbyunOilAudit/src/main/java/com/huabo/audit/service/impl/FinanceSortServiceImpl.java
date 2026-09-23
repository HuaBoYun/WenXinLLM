package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.FinanceSortEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.FinanceSortMapper;
import com.huabo.audit.service.FinanceSortService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName FinanceSortServiceImpl
 * @Description
 * @DATE 2023/9/23
 */
@Service
public class FinanceSortServiceImpl implements FinanceSortService {

    @Autowired
    private FinanceSortMapper financeSortMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        FinanceSortEntity financeSortEntity = new FinanceSortEntity();

        if(StringUtil.isNotEmpty(projectName)){
            financeSortEntity.setProjectName(projectName);
        }

        Page<FinanceSortEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> financeSortMapper.selectByEntity(financeSortEntity));
		PageInfo<FinanceSortEntity> pageInfo = new PageInfoUtil<FinanceSortEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        FinanceSortEntity financeSortEntity = financeSortMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", financeSortEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(FinanceSortEntity financeSortEntity) throws Exception{
        financeSortMapper.updateEntity(financeSortEntity);
    }

    @Override
    public void saveEntity(String token, FinanceSortEntity financeSortEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            financeSortEntity.setCreateUser(tblStaff);
        }
        financeSortMapper.insertEntity(financeSortEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        financeSortMapper.deleteEntity(ids);
    }


}
