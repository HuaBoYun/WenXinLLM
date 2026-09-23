package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.QualityMapper;
import com.huabo.audit.service.QualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName  QualityServiceImpl
 * @Description
 * @DATE 2023/10/9
 */
@Service
public class QualityServiceImpl implements  QualityService {

    @Autowired
    private  QualityMapper qualityMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize,String projectName, Integer type) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        if(qualityMapper.selectConfScoreManageCount(type) == 0){
            return ResponseFormat.retParam(30001,"无该类型评分标准",null);
        }

        QualityEntity qualityEntity = new  QualityEntity();

        qualityEntity.setType(type);

        if(StringUtil.isNotEmpty(projectName)){
            ImplementPlanEntity project = new ImplementPlanEntity();
            project.setProjectName(projectName);
            qualityEntity.setProject(project);
        }


        Page< QualityEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> qualityMapper.selectByEntity(qualityEntity));
		PageInfo< QualityEntity> pageInfo = new PageInfoUtil< QualityEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());

        return ResponseFormat.retParam(1,200,pageInfo);
    }
 
    @Override
    public JsonBean findById(String id) throws Exception{
        QualityEntity qualityEntity = qualityMapper.selectById(id);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(qualityEntity);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", qualityEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( QualityEntity qualityEntity) throws Exception{
        qualityMapper.updateEntity(qualityEntity);
        qualityMapper.deleteQualityItemsById(qualityEntity.getId()+"");
        if(qualityEntity.getQualityItems() != null && qualityEntity.getQualityItems().size() > 0){
            for (QualityItemEntity qualityItemEntity : qualityEntity.getQualityItems()){
                qualityMapper.insertQualityItem(qualityEntity.getId(),qualityItemEntity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  QualityEntity qualityEntity) throws Exception{
        qualityMapper.insertEntity(qualityEntity);
        if(qualityEntity.getQualityItems() != null && qualityEntity.getQualityItems().size() > 0){
            for (QualityItemEntity qualityItemEntity : qualityEntity.getQualityItems()){
                qualityMapper.insertQualityItem(qualityEntity.getId(),qualityItemEntity);
            }
        }
    }
 
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{
        qualityMapper.deleteEntity(ids);
        qualityMapper.deleteQualityItemsById(ids);
    }

    @Override
    public List<QualityEntity> selectQualityByProjectId(BigDecimal projectId) {
        List<QualityEntity> qualityEntities = qualityMapper.selectQualityByProjectId(projectId);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(qualityEntities);
        return qualityEntities;
    }


}
