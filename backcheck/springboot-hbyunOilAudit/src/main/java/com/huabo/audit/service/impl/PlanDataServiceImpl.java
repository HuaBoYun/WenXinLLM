package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.PlanDataEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.PlanDataMapper;
import com.huabo.audit.service.PlanDataService;
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
 * @ClassName  PlanDataServiceImpl
 * @Description
 * @DATE 2023/9/30
 */
@Service
public class PlanDataServiceImpl implements  PlanDataService {

    @Autowired
    private  PlanDataMapper planDataMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String name) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         PlanDataEntity planDataEntity = new  PlanDataEntity();


        if(StringUtil.isNotEmpty(name)){
            planDataEntity.setName(name);
        }


        Page< PlanDataEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> planDataMapper.selectByEntity(planDataEntity));
		PageInfo< PlanDataEntity> pageInfo = new PageInfoUtil< PlanDataEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        PlanDataEntity planDataEntity = planDataMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", planDataEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( PlanDataEntity planDataEntity) throws Exception{
        planDataMapper.updateEntity(planDataEntity);
        planDataMapper.deleteAttachmentByIds(planDataEntity.getId()+"");
        if(StringUtil.isNotEmpty(planDataEntity.getAttIds())){
            String[] ids = planDataEntity.getAttIds().split(",");
            for (String attId : ids){
                planDataMapper.insertAttachmentsWidthId(planDataEntity.getId(),attId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  PlanDataEntity planDataEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            planDataEntity.setCreateUser(tblStaff);
        }
        planDataMapper.insertEntity(planDataEntity);
        if(StringUtil.isNotEmpty(planDataEntity.getAttIds())){
            String[] ids = planDataEntity.getAttIds().split(",");
            for (String attId : ids){
                planDataMapper.insertAttachmentsWidthId(planDataEntity.getId(),attId);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        planDataMapper.deleteEntity(ids);
        planDataMapper.deleteAttachmentByIds(ids);

    }


}
