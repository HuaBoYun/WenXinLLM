package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.SubContractEntity;
import com.huabo.audit.oracle.mapper.SubContractMapper;
import com.huabo.audit.service.SubContractService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName SubContractServiceImpl
 * @Description
 * @DATE 2023/9/30
 */
@Service
public class SubContractServiceImpl implements SubContractService {

    @Autowired
    private SubContractMapper subContractMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName, String subContractNo) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        SubContractEntity subContractEntity = new SubContractEntity();

        if(StringUtil.isNotEmpty(projectName)){
            subContractEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(subContractNo)){
            subContractEntity.setSubContractNo(subContractNo);
        }

        Page<SubContractEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> subContractMapper.selectByEntity(subContractEntity));
		PageInfo<SubContractEntity> pageInfo = new PageInfoUtil<SubContractEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

       SubContractEntity subContractEntity = subContractMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", subContractEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity(SubContractEntity subContractEntity) throws Exception{
        subContractMapper.updateEntity(subContractEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token, SubContractEntity subContractEntity) throws Exception{
        subContractMapper.insertEntity(subContractEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        subContractMapper.deleteEntity(ids);

    }


}
