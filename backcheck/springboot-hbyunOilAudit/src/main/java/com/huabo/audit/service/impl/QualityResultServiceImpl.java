package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.QualityResultEntity;
import com.huabo.audit.oracle.entity.QualityItemEntity;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.mapper.QualityResultMapper;
import com.huabo.audit.service.QualityResultService;
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
 * @ClassName  QualityResultServiceImpl
 * @Description
 * @DATE 2023/10/16
 */
@Service
public class QualityResultServiceImpl implements  QualityResultService {

    @Autowired
    private  QualityResultMapper qualityResultMapper;
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


        QualityResultEntity qualityResultEntity = new  QualityResultEntity();

        if(StringUtil.isNotEmpty(projectName)){
        	ImplementPlanEntity tblNbsjProject = new ImplementPlanEntity();
            tblNbsjProject.setProjectName(projectName);
            qualityResultEntity.setProject(tblNbsjProject);
        }


        Page< QualityResultEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> qualityResultMapper.selectByEntity(qualityResultEntity));
		PageInfo< QualityResultEntity> pageInfo = new PageInfoUtil< QualityResultEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());

        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{
        QualityResultEntity qualityResultEntity = qualityResultMapper.selectById(id);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(qualityResultEntity);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", qualityResultEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( QualityResultEntity qualityResultEntity) throws Exception{
//        qualityResultMapper.updateEntity(qualityResultEntity);
        qualityResultMapper.updateByPrimaryKeySelective(qualityResultEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  QualityResultEntity qualityResultEntity) throws Exception{
//        qualityResultMapper.insertEntity(qualityResultEntity);
    	qualityResultEntity.setId(RandomUtil.uuBigDecimalId());
        qualityResultMapper.insertSelective(qualityResultEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{
        qualityResultMapper.deleteEntity(ids);
    }


}
