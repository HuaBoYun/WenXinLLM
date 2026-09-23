package com.huabo.audit.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.AuditSuggestion2LEntity;
import com.huabo.audit.oracle.entity.BalanceProjectEntity;
import com.huabo.audit.oracle.mapper.BalanceProjectMapper;
import com.huabo.audit.service.BalanceProjectService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.util.PageInfoUtil;
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
 * @ClassName  BalanceProjectServiceImpl
 * @Description
 * @DATE 2023/9/30
 */
@Service
public class BalanceProjectServiceImpl implements  BalanceProjectService {

    @Autowired
    private  BalanceProjectMapper balanceProjectMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String planNo, String projectName, String contractNo) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         BalanceProjectEntity balanceProjectEntity = new  BalanceProjectEntity();


        if(StringUtil.isNotEmpty(planNo)){
            balanceProjectEntity.setPlanNo(planNo);
        }

        if(StringUtil.isNotEmpty(projectName)){
            balanceProjectEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(contractNo)){
            balanceProjectEntity.setContractNo(contractNo);
        }

        Page<BalanceProjectEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> balanceProjectMapper.selectByEntity(balanceProjectEntity));

        PageInfo<BalanceProjectEntity> pageInfo = new PageInfoUtil<BalanceProjectEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        BalanceProjectEntity balanceProjectEntity = balanceProjectMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", balanceProjectEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( BalanceProjectEntity balanceProjectEntity) throws Exception{
        balanceProjectMapper.updateEntity(balanceProjectEntity);
        balanceProjectMapper.deleteAttachmentByIds(balanceProjectEntity.getId()+"");
        if(StringUtil.isNotEmpty(balanceProjectEntity.getAttIds())){
            String[] ids = balanceProjectEntity.getAttIds().split(",");
            for (String attId : ids){
                balanceProjectMapper.insertAttachmentsWidthId(balanceProjectEntity.getId(),attId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  BalanceProjectEntity balanceProjectEntity) throws Exception{
        balanceProjectMapper.insertEntity(balanceProjectEntity);
        if(StringUtil.isNotEmpty(balanceProjectEntity.getAttIds())){
            String[] ids = balanceProjectEntity.getAttIds().split(",");
            for (String attId : ids){
                balanceProjectMapper.insertAttachmentsWidthId(balanceProjectEntity.getId(),attId);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        balanceProjectMapper.deleteEntity(ids);
        balanceProjectMapper.deleteAttachmentByIds(ids);

    }


}
