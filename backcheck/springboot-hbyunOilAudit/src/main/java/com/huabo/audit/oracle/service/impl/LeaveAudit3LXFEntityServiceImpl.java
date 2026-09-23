package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.LeaveAudit3LXFEntity;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.mapper.LeaveAudit3LXFEntityMapper;
import com.huabo.audit.oracle.service.LeaveAudit3LXFEntityService;

import com.huabo.audit.service.impl.ReservePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CGJ
 * @description 针对表【-三级单位离任审计-分发表】的数据库操作service
 * @Entity LeaveAudit3LXFEntityServiceImpl
 */
@Service
public class LeaveAudit3LXFEntityServiceImpl extends ServiceImpl<LeaveAudit3LXFEntityMapper, LeaveAudit3LXFEntity>
        implements LeaveAudit3LXFEntityService {

    @Resource
    private LeaveAudit3LXFEntityMapper LeaveAudit3LXFEntityMapper;
    

    @Autowired
    private LeaveAudit3LMapper leaveAudit3LMapper;

    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 通过三级单位离任审计 id 查询分发人员
     *
     * @param token
     * @param auditId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getListByAuditId(String token, String auditId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<LeaveAudit3LXFEntity> query = new LambdaQueryWrapper<LeaveAudit3LXFEntity>()
                .eq(LeaveAudit3LXFEntity::getAuditId, auditId);
        List<LeaveAudit3LXFEntity> entityList = LeaveAudit3LXFEntityMapper.selectList(query);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(entityList);
        result.put("List", entityList);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 进行人员 分发
     *
     * @param token
     * @param auditIds 1,2
     * @param userIds [1,2]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, String auditIds, String userIds) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<Long> list = leaveAudit3LMapper.selectByIds(auditIds);
        this.delete(token, auditIds);

        List<Long> useridList = Arrays.stream(userIds.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());
        List<Long> auditIdList = Arrays.stream(auditIds.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());

        
        auditIdList.addAll(list);
        auditIdList.stream().forEach(auditId -> {
            // 进行分发保存
            useridList.stream().forEach(userId -> {
            	
                LeaveAudit3LXFEntity vo = new LeaveAudit3LXFEntity();
                try {
                	leaveAudit3LMapper.updateDisFirstPerson(userId, auditId);
                    if (null != vo.getId()) {
                        vo.setUpdateTime(new Date());
                        vo.setUpdateUser(loginStaff.getStaffid().toString());
                    } else {
                        vo.setCreateUser(loginStaff.getStaffid().toString());
                    }
                    vo.setAuditId(new BigDecimal(auditId));
                    vo.setUserId(String.valueOf(userId));
                    if(vo.getId()==null) {
                    	vo.setId(RandomUtil.uuLongId());
                    }
                    boolean ret = this.saveOrUpdate(vo);
                    if (!ret) {
                        throw new RuntimeException("Failed to save");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        });
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除所有分发人员
     *
     * @param auditIds 1
     * @return
     */
    @Override
    @Transactional
    public JsonBean delete(String token, String auditIds) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<Long> auditIdList = Arrays.stream(auditIds.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());
        // 判断是否删除成功
        if(auditIdList.size() >0) {
            Integer result = LeaveAudit3LXFEntityMapper.deleteBatchIds(auditIdList);
            return result == 0 ? ResponseFormat.retParam(0, 50001, "未删除成功") : ResponseFormat.retParam(1, 200);
        }
        return ResponseFormat.retParam(0, 10002, null);
    }


}




