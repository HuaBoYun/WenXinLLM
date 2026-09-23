package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.QualityEvaluateEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.QualityEvaluateMapper;
import com.huabo.audit.service.QualityEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName  QualityEvaluateServiceImpl
 * @Description
 * @DATE 2023/10/9
 */
@Service
public class QualityEvaluateServiceImpl implements  QualityEvaluateService {

    @Autowired
    private  QualityEvaluateMapper qualityEvaluateMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String name, Integer status) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         QualityEvaluateEntity qualityEvaluateEntity = new  QualityEvaluateEntity();

        if(StringUtil.isNotEmpty(name)){
            qualityEvaluateEntity.setName(name);
        }

        if(status != null){
            qualityEvaluateEntity.setStatus(status);
        }

        Page< QualityEvaluateEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> qualityEvaluateMapper.selectByEntity(qualityEvaluateEntity));
		PageInfo< QualityEvaluateEntity> pageInfo = new PageInfoUtil< QualityEvaluateEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());

        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        QualityEvaluateEntity qualityEvaluateEntity = qualityEvaluateMapper.selectById(id);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(qualityEvaluateEntity);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", qualityEvaluateEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( QualityEvaluateEntity qualityEvaluateEntity) throws Exception{
        qualityEvaluateMapper.updateEntity(qualityEvaluateEntity);
        qualityEvaluateMapper.deleteQualityEvaluateItemsById(qualityEvaluateEntity.getId()+"");
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getScoreIds())){
            String[] idArr = qualityEvaluateEntity.getScoreIds().split(",");
            for (String smId : idArr){
                qualityEvaluateMapper.insertQualityEvaluateItem(qualityEvaluateEntity.getId(),new BigDecimal(smId));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  QualityEvaluateEntity qualityEvaluateEntity) throws Exception{
        if(qualityEvaluateEntity.getStatus() == null){
            qualityEvaluateEntity.setStatus(2);
        }
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            qualityEvaluateEntity.setCreateUser(tblStaff);
        }
        qualityEvaluateMapper.insertEntity(qualityEvaluateEntity);
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getScoreIds())){
            String[] idArr = qualityEvaluateEntity.getScoreIds().split(",");
            for (String smId : idArr){
                qualityEvaluateMapper.insertQualityEvaluateItem(qualityEvaluateEntity.getId(),new BigDecimal(smId));
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{
        qualityEvaluateMapper.deleteEntity(ids);
        qualityEvaluateMapper.deleteQualityEvaluateItemsById(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntityStatus(QualityEvaluateEntity qualityEvaluateEntity) throws Exception {
        /**
         * 状态:1.使用 2.未使用
         * 修改逻辑：只能有一个状态为使用状态，其他均为未使用，修改时状态互斥
         * */

        if(qualityEvaluateEntity.getStatus() != null){
            if( qualityEvaluateEntity.getStatus() == 1){
                //将全部状态置为 2 未使用
                qualityEvaluateMapper.batchResetStatus(2);
            }
            qualityEvaluateMapper.updateStatus(qualityEvaluateEntity);
        }
    }


}
