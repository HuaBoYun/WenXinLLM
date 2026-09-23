package com.huabo.audit.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.AuditProjectZkEntity;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.AuditProjectZkMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.service.AuditProjectZkService;
import com.huabo.audit.util.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;

import javax.annotation.Resource;

/**
 * @author Rui
 * @ClassName  AuditProjectZkServiceImpl
 * @Description
 * @DATE 2023/10/17
 */
@Service
public class AuditProjectZkServiceImpl implements  AuditProjectZkService {

    @Autowired
    private  AuditProjectZkMapper auditProjectZkMapper;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Autowired
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private UserProvider userProvider;

    //获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize,String projectName, BigDecimal money) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(user.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        AuditProjectZkEntity auditProjectZkEntity = new  AuditProjectZkEntity();
        auditProjectZkEntity.setProjectId(projectId);
        if(money != null){
            auditProjectZkEntity.setMoney(money);
        }

        if(StringUtil.isNotEmpty(projectName)){
            auditProjectZkEntity.setProjectName(projectName);
        }

        Page<AuditProjectZkEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> auditProjectZkMapper.selectByEntity(auditProjectZkEntity));

        PageInfo<AuditProjectZkEntity> pageInfo = new PageInfoUtil<AuditProjectZkEntity>().parsePageInfo(page);

        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{
        AuditProjectZkEntity auditProjectZkEntity = auditProjectZkMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", auditProjectZkEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( AuditProjectZkEntity auditProjectZkEntity) throws Exception{
        auditProjectZkMapper.updateEntity(auditProjectZkEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  AuditProjectZkEntity auditProjectZkEntity) throws Exception{
        auditProjectZkMapper.insertEntity(auditProjectZkEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{
        auditProjectZkMapper.deleteEntity(ids);
    }


}
