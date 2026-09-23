package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.InvestigationEntity;
import com.huabo.audit.oracle.mapper.InvestigationMapper;
import com.huabo.audit.service.InvestigationService;
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
 * @ClassName  InvestigationServiceImpl
 * @Description
 * @DATE 2023/9/30
 */
@Service
public class InvestigationServiceImpl implements  InvestigationService {

    @Autowired
    private  InvestigationMapper investigationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String planNo, String projectName) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         InvestigationEntity investigationEntity = new  InvestigationEntity();


        if(StringUtil.isNotEmpty(planNo)){
            investigationEntity.setPlanNo(planNo);
        }

        if(StringUtil.isNotEmpty(projectName)){
            investigationEntity.setProjectName(projectName);
        }

        Page< InvestigationEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> investigationMapper.selectByEntity(investigationEntity));
		PageInfo< InvestigationEntity> pageInfo = new PageInfoUtil< InvestigationEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        InvestigationEntity investigationEntity = investigationMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", investigationEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( InvestigationEntity investigationEntity) throws Exception{
        investigationMapper.updateEntity(investigationEntity);
        investigationMapper.deleteAttachmentByIds(investigationEntity.getId()+"");
        if(StringUtil.isNotEmpty(investigationEntity.getAttIds())){
            String[] ids = investigationEntity.getAttIds().split(",");
            for (String attId : ids){
                investigationMapper.insertAttachmentsWidthId(investigationEntity.getId(),attId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  InvestigationEntity investigationEntity) throws Exception{
        investigationMapper.insertEntity(investigationEntity);
        if(StringUtil.isNotEmpty(investigationEntity.getAttIds())){
            String[] ids = investigationEntity.getAttIds().split(",");
            for (String attId : ids){
                investigationMapper.insertAttachmentsWidthId(investigationEntity.getId(),attId);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        investigationMapper.deleteEntity(ids);
        investigationMapper.deleteAttachmentByIds(ids);

    }


}
