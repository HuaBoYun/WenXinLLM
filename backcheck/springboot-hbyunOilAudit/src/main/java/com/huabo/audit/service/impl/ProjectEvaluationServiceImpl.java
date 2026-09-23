package com.huabo.audit.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.ProjectEvaluationMapper;
import com.huabo.audit.service.ProjectEvaluationService;
import com.huabo.audit.util.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationServiceImpl
 * @Description
 * @DATE 2024/04/13
 */
@Service
public class ProjectEvaluationServiceImpl implements ProjectEvaluationService {

    @Resource
    private ProjectEvaluationMapper projectEvaluationMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize,String projectName) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }


        ProjectEvaluationEntity qualityEntity = new  ProjectEvaluationEntity();


        if(StringUtil.isNotEmpty(projectName)){
            ImplementPlanEntity project = new ImplementPlanEntity();
            project.setProjectName(projectName);
//            qualityEntity.setProject(project);
        }


        Page< QualityEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> projectEvaluationMapper.selectByEntity(qualityEntity));
		PageInfo< QualityEntity> pageInfo = new PageInfoUtil< QualityEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());

        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{
        if (StringUtil.isEmpty(id)){
            ResponseFormat.retParam(0, -1, "id不正确");
        }
        BigDecimal bigDecimal = new BigDecimal(id);
        ProjectEvaluationEntity entity = projectEvaluationMapper.selectById(bigDecimal);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(entity);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", entity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( ProjectEvaluationEntity qualityEntity) throws Exception{
        projectEvaluationMapper.updateEntity(qualityEntity);
        projectEvaluationMapper.deleteQualityItemsById(qualityEntity.getId()+"");
        if(qualityEntity.getQualityItems() != null && qualityEntity.getQualityItems().size() > 0){
            for (ProjectEvaluationItemEntity qualityItemEntity : qualityEntity.getQualityItems()){
                projectEvaluationMapper.insertQualityItem(qualityEntity.getId(),qualityItemEntity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  ProjectEvaluationEntity qualityEntity) throws Exception{
        projectEvaluationMapper.insertEntity(qualityEntity);
        if(qualityEntity.getQualityItems() != null && qualityEntity.getQualityItems().size() > 0){
            for (ProjectEvaluationItemEntity qualityItemEntity : qualityEntity.getQualityItems()){
                projectEvaluationMapper.insertQualityItem(qualityEntity.getId(),qualityItemEntity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{
        projectEvaluationMapper.deleteEntity(ids);
        projectEvaluationMapper.deleteQualityItemsById(ids);
    }


}
