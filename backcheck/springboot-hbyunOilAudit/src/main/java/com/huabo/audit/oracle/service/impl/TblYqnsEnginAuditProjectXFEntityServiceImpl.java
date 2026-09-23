package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectXFEntity;
import com.huabo.audit.oracle.mapper.TblYqnsEnginAuditProjectXFEntityMapper;
import com.huabo.audit.oracle.service.TblYqnsEnginAuditProjectXFEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CGJ
 * @description 针对表【-工程审计项目-分发表】的数据库操作service
 * @Entity TblYqnsProposalNoticeXfServiceImpl
 */
@Service
public class TblYqnsEnginAuditProjectXFEntityServiceImpl extends ServiceImpl<TblYqnsEnginAuditProjectXFEntityMapper, TblYqnsEnginAuditProjectXFEntity>
        implements TblYqnsEnginAuditProjectXFEntityService {

    @Resource
    private TblYqnsEnginAuditProjectXFEntityMapper tblYqnsEnginAuditProjectXFEntityMapper;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 通过工程审计项目id 查询分发人员
     * @param token
     * @param enginId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getListByEnginId(String token,String enginId) throws Exception{
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblYqnsEnginAuditProjectXFEntity> query = new LambdaQueryWrapper<TblYqnsEnginAuditProjectXFEntity>()
                .eq(TblYqnsEnginAuditProjectXFEntity::getEnginId, enginId);
        List<TblYqnsEnginAuditProjectXFEntity> entityList = tblYqnsEnginAuditProjectXFEntityMapper.selectList(query);
        result.put("List",entityList);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 进行人员 分发
     * @param token
     * @param EnginId 1
     * @param userIds [1,2]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, String EnginId, String userIds) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.delete(token,EnginId);

        List<Long> useridList = Arrays.stream(userIds.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());
        // 进行分发保存
        useridList.stream().forEach(userId ->{
            TblYqnsEnginAuditProjectXFEntity  vo=new TblYqnsEnginAuditProjectXFEntity();
            try {
                if (null != vo.getId()) {
                    vo.setUpdateTime(new Date());
                    vo.setUpdateUser(loginStaff.getStaffid().toString());
                } else {
                    vo.setCreateUser(loginStaff.getStaffid().toString());
                }
                vo.setEnginId(new BigDecimal(EnginId));
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
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除所有分发人员
     *
     * @param enginId 1
     * @return
     */
    @Override
    @Transactional
    public JsonBean delete(String token, String enginId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        LambdaQueryWrapper<TblYqnsEnginAuditProjectXFEntity> query = new LambdaQueryWrapper<TblYqnsEnginAuditProjectXFEntity>()
                .eq(TblYqnsEnginAuditProjectXFEntity::getEnginId,new BigDecimal(enginId));
        // 判断是否删除成功
        if(!StringUtils.isEmpty(enginId)) {
            Integer result = this.tblYqnsEnginAuditProjectXFEntityMapper.delete(query);
            return result == 0 ? ResponseFormat.retParam(0, 50001,"未删除成功") : ResponseFormat.retParam(1, 200);
        }
        return ResponseFormat.retParam(0,10002,null);
    }


}




