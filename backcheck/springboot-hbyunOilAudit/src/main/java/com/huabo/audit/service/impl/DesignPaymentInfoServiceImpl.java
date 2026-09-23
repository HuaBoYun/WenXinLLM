package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.DesignPaymentInfoEntity;
import com.huabo.audit.oracle.mapper.DesignPaymentInfoMapper;
import com.huabo.audit.service.DesignPaymentInfoService;
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
 * @ClassName  DesignPaymentInfoServiceImpl
 * @Description
 * @DATE 2023/9/30
 */
@Service
public class DesignPaymentInfoServiceImpl implements  DesignPaymentInfoService {

    @Autowired
    private  DesignPaymentInfoMapper designPaymentInfoMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String planId, String contractNo) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         DesignPaymentInfoEntity designPaymentInfoEntity = new  DesignPaymentInfoEntity();


        if(StringUtil.isNotEmpty(planId)){
            designPaymentInfoEntity.setPlanId(planId);
        }

        if(StringUtil.isNotEmpty(contractNo)){
            designPaymentInfoEntity.setContractNo(contractNo);
        }

        Page< DesignPaymentInfoEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> designPaymentInfoMapper.selectByEntity(designPaymentInfoEntity));
		PageInfo< DesignPaymentInfoEntity> pageInfo = new PageInfoUtil< DesignPaymentInfoEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        DesignPaymentInfoEntity designPaymentInfoEntity = designPaymentInfoMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", designPaymentInfoEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( DesignPaymentInfoEntity designPaymentInfoEntity) throws Exception{
        designPaymentInfoMapper.updateEntity(designPaymentInfoEntity);
        designPaymentInfoMapper.deleteAttachmentByIds(designPaymentInfoEntity.getId()+"");
        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getAttIds())){
            String[] ids = designPaymentInfoEntity.getAttIds().split(",");
            for (String attId : ids){
                designPaymentInfoMapper.insertAttachmentsWidthId(designPaymentInfoEntity.getId(),attId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  DesignPaymentInfoEntity designPaymentInfoEntity) throws Exception{
        designPaymentInfoMapper.insertEntity(designPaymentInfoEntity);
        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getAttIds())){
            String[] ids = designPaymentInfoEntity.getAttIds().split(",");
            for (String attId : ids){
                designPaymentInfoMapper.insertAttachmentsWidthId(designPaymentInfoEntity.getId(),attId);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        designPaymentInfoMapper.deleteEntity(ids);
        designPaymentInfoMapper.deleteAttachmentByIds(ids);

    }


}
