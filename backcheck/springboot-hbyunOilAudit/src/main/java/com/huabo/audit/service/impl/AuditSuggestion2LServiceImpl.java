package com.huabo.audit.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper. AuditSuggestion2LMapper;
import com.huabo.audit.service. AuditSuggestion2LService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.util.PageInfoUtil;
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
 * @ClassName  AuditSuggestion2LServiceImpl
 * @Description
 * @DATE 2023/9/23
 */
@Service
public class AuditSuggestion2LServiceImpl implements  AuditSuggestion2LService {

    @Autowired
    private  AuditSuggestion2LMapper auditSuggestion2LMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String org, String name) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         AuditSuggestion2LEntity auditSuggestion2LEntity = new  AuditSuggestion2LEntity();

        if(StringUtil.isNotEmpty(org)){
            TblOrganization tblOrganization = new TblOrganization();
            tblOrganization.setOrgname(org);
            auditSuggestion2LEntity.setOrg(tblOrganization);
        }

        if(StringUtil.isNotEmpty(name)){
            auditSuggestion2LEntity.setName(name);
        }


        Page<AuditSuggestion2LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> auditSuggestion2LMapper.selectByEntity(auditSuggestion2LEntity));

        PageInfo<AuditSuggestion2LEntity> pageInfo = new PageInfoUtil<AuditSuggestion2LEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }
    
    @Override
	public JsonBean findListDraftPlan(String token, Integer pageNumber, Integer pageSize, String org, String name)
			throws Exception {
    	TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         AuditSuggestion2LEntity auditSuggestion2LEntity = new  AuditSuggestion2LEntity();

        if(StringUtil.isNotEmpty(org)){
            TblOrganization tblOrganization = new TblOrganization();
            tblOrganization.setOrgname(org);
            auditSuggestion2LEntity.setOrg(tblOrganization);
        }

        if(StringUtil.isNotEmpty(name)){
            auditSuggestion2LEntity.setName(name);
        }

        Page<AuditSuggestion2LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> auditSuggestion2LMapper.selectListDraftPlan(auditSuggestion2LEntity));

        PageInfo<AuditSuggestion2LEntity> pageInfo = new PageInfoUtil<AuditSuggestion2LEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
	}

    @Override
    public JsonBean findById(String id) throws Exception{

         AuditSuggestion2LEntity auditSuggestion2LEntity = auditSuggestion2LMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", auditSuggestion2LEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity( AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception{
        auditSuggestion2LMapper.updateEntity(auditSuggestion2LEntity);
    }

    @Override
    public void saveEntity(String token,  AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            auditSuggestion2LEntity.setCreateUser(tblStaff);
        }
        auditSuggestion2LMapper.insertEntity(auditSuggestion2LEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        auditSuggestion2LMapper.deleteEntity(ids);
    }


    /**
     * 通过ids查询 二级单位及成员单位离任审计
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public   List<AuditSuggestion2LEntity> findByIds(String ids)   {
        // 二级单位及成员单位离任审计 通过ids
        List<AuditSuggestion2LEntity> beanList = auditSuggestion2LMapper.findByIds(ids);
        return beanList;
    }

}
