package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuVerifyEntity;
import com.huabo.audit.oracle.mapper.TblYqnsAuditMyManuVerifyMapper;
import com.huabo.audit.oracle.service.TblYqnsAuditMyManuVerifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMyManuVerifyServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿-下方审计查证事实 serviceImpl
 */
@Service
public class TblYqnsAuditMyManuVerifyServiceImpl extends ServiceImpl<TblYqnsAuditMyManuVerifyMapper, TblYqnsAuditMyManuVerifyEntity>
        implements TblYqnsAuditMyManuVerifyService {



    @Resource
    private TblYqnsAuditMyManuVerifyMapper tblYqnsAuditMyManuVerifyMapper;
    
    @Resource
    private UserProvider userProvider;




    /**
     * 获取单独一个我的底稿-下方审计查证事实记录
     *
     * @param token
     * @param myManuscriptId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getListByMyManuscriptId(String token, String myManuscriptId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsAuditMyManuVerifyEntity>  list= this.getListByMyManuscriptId(myManuscriptId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        return ResponseFormat.retParam(1, "查询成功", result);
    }


    /**
     * 我的底稿-下方审计查证事实-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsAuditMyManuVerifyEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(null);
        }else {
        	entity.setId(RandomUtil.uuLongId());
        }
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



    /**
     * 我的底稿-下方审计查证事实-增加修改
     *
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdateList(String token,  List<TblYqnsAuditMyManuVerifyEntity> voList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        voList.stream().forEach(vo -> {
            // check id is null
        	
            if (null != vo.getId()) {
                vo.setUpdateTime(new Date());
                vo.setUpdateUser(null);
            }
            vo.setStatus(1);
            if(vo.getAuditConclusion()!=null && vo.getAuditConclusion().length()>0) {
            	this.saveOrUpdate(vo);
            }
        });
//        boolean ret = this.saveOrUpdateBatch(voList);
//        if (!ret) {
//            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
//        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }




    /**
     * 删除我的底稿-下方审计查证事实(直接删除)
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (id != null) {
            boolean ret = this.removeById(id);
            if (!ret) {
                return ResponseFormat.retParam(0, -1, Boolean.FALSE);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 查询我的底稿-下方审计查证事实
     * @param myManuscriptId
     * @return
     */
    @Override
    public List<TblYqnsAuditMyManuVerifyEntity> getListByMyManuscriptId(String myManuscriptId) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsAuditMyManuVerifyEntity> query = new LambdaQueryWrapper<TblYqnsAuditMyManuVerifyEntity>()
                .eq(StringUtil.isNotEmpty(myManuscriptId), TblYqnsAuditMyManuVerifyEntity::getMyManuscriptId, myManuscriptId).isNotNull(TblYqnsAuditMyManuVerifyEntity::getVerificationDescription);
//                .eq(TblYqnsAuditMyManuVerifyEntity::getStatus, 1);
        List<TblYqnsAuditMyManuVerifyEntity>  list= tblYqnsAuditMyManuVerifyMapper.selectList(query);
        return list;
    }
}
