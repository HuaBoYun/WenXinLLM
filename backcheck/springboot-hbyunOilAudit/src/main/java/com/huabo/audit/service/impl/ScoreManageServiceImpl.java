package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ScoreManageEntity;
import com.huabo.audit.oracle.entity.ScoreManageItemEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.ScoreManageMapper;
import com.huabo.audit.service.ScoreManageService;
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
 * @ClassName  ScoreManageServiceImpl
 * @Description
 * @DATE 2023/10/9
 */
@Service
public class ScoreManageServiceImpl implements  ScoreManageService {

    @Autowired
    private  ScoreManageMapper scoreManageMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, Integer type, Integer status) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         ScoreManageEntity scoreManageEntity = new  ScoreManageEntity();

        if(type != null){
            scoreManageEntity.setType(type);
        }

        if(status != null){
            scoreManageEntity.setStatus(status);
        }

        Page< ScoreManageEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> scoreManageMapper.selectByEntity(scoreManageEntity));
		PageInfo< ScoreManageEntity> pageInfo = new PageInfoUtil< ScoreManageEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());

        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        ScoreManageEntity scoreManageEntity = scoreManageMapper.selectById(id);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(scoreManageEntity);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", scoreManageEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity( ScoreManageEntity scoreManageEntity) throws Exception{
        scoreManageMapper.updateEntity(scoreManageEntity);
        scoreManageMapper.deleteScoreItemsById(scoreManageEntity.getId()+"");
        if(scoreManageEntity.getScoreItems() != null && scoreManageEntity.getScoreItems().size() > 0){
            for (int i = 0; i < scoreManageEntity.getScoreItems().size(); i++) {
                ScoreManageItemEntity scoreItem = scoreManageEntity.getScoreItems().get(i);
                scoreItem.setSort(i);
                scoreItem.setSmId(scoreManageEntity.getId());
                scoreManageMapper.insertScoreItem(scoreItem);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(String token,  ScoreManageEntity scoreManageEntity) throws Exception{
        if(scoreManageEntity.getType() == null){
            throw new RuntimeException("类型不能为空");
        }
        int count = scoreManageMapper.selectCountByType(scoreManageEntity.getType());
        if(count > 0){
            throw new RuntimeException("存在同类型的评分项");
        }
        if(scoreManageEntity.getStatus() == null){
            scoreManageEntity.setStatus(1);
        }
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            scoreManageEntity.setCreateUser(tblStaff);
        }
        scoreManageMapper.insertEntity(scoreManageEntity);
        if(scoreManageEntity.getScoreItems() != null && scoreManageEntity.getScoreItems().size() > 0){
            for (int i = 0; i < scoreManageEntity.getScoreItems().size(); i++) {
                ScoreManageItemEntity scoreItem = scoreManageEntity.getScoreItems().get(i);
                scoreItem.setSort(i);
                scoreItem.setSmId(scoreManageEntity.getId());
                scoreManageMapper.insertScoreItem(scoreItem);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        scoreManageMapper.deleteEntity(ids);
        scoreManageMapper.deleteScoreItemsById(ids);

    }

    @Override
    public JsonBean findAvailableScoreItemsByType(Integer type) throws Exception {
        List<ScoreManageItemEntity> list = scoreManageMapper.selectAvailableScoreItemsByType(type);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", list);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntityStatus(ScoreManageEntity scoreManageEntity) {
        if(scoreManageEntity.getStatus() != null){
            scoreManageMapper.updateStatus(scoreManageEntity.getId(),scoreManageEntity.getStatus());
        }
    }


}
