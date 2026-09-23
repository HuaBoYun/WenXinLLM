package com.huabo.audit.oracle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.dto.TblYqnsAuditProjectDto;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditProjectDetailEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditProjectEntity;
import com.huabo.audit.oracle.mapper.AuditProjectDetailMapper;
import com.huabo.audit.oracle.mapper.AuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.service.AuditProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-07 21:36
 **/
@Service
@Slf4j
public class AuditProjectServiceImpl implements AuditProjectService {

    @Autowired
    private AuditProjectMapper auditProjectMapper;
    @Autowired
    private AuditProjectDetailMapper auditProjectDetailMapper;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
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
    public JsonBean getProjectList(String token,Integer pageNumber, Integer pageSize,String name,String auditUnitId,String sceneApproveStaerTime,String sceneApproveEndTime) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        HashMap<String, Object> result = new HashMap<>();

        com.huabo.audit.util.PageInfo<TblYqnsAuditProjectEntity> info = new com.huabo.audit.util.PageInfo<>();
        PageInfo<TblYqnsAuditProjectEntity> pageInfo;
        QueryWrapper<TblYqnsAuditProjectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("DELETED", 0);
        wrapper.eq("PROJECTID", projectId);
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("NAME", name);
        }
        
        if (StringUtils.isNotEmpty(auditUnitId)) {
            wrapper.eq("AUDITUNITID", auditUnitId);
        }
        
        if (StringUtils.isNotEmpty(sceneApproveStaerTime)) {
        	LocalDate startlocalDate = LocalDate.parse(sceneApproveStaerTime.trim(), DateTimeFormatter.ISO_DATE);
            wrapper.eq("SCENEAPPROVESTAERTIME", startlocalDate);
        }
        
        if (StringUtils.isNotEmpty(sceneApproveEndTime)) {
        	LocalDate endlocalDate = LocalDate.parse(sceneApproveEndTime.trim(), DateTimeFormatter.ISO_DATE);
            wrapper.eq("SCENEAPPROVEENDTIME", endlocalDate);
        }
        
        pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> auditProjectMapper.selectList(wrapper));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);

        return ResponseFormat.retParam(1, "查询成功", result);
    }

    @Override
    public JsonBean getProjectById(String token,Long projectId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsAuditProjectEntity tblYqnsAuditProjectEntity = auditProjectMapper.selectOne(new QueryWrapper<TblYqnsAuditProjectEntity>()
            .eq("DELETED", 0)
            .eq("ID", projectId));
        if (tblYqnsAuditProjectEntity == null) {
            return ResponseFormat.retParam(0, "暂无此数据", null);
        }
        log.info("审计项目情况内容:{}", JSONObject.toJSONString(tblYqnsAuditProjectEntity));
        TblYqnsAuditProjectDto tblYqnsAuditProjectDto = new TblYqnsAuditProjectDto();
        BeanUtil.copyProperties(tblYqnsAuditProjectEntity, tblYqnsAuditProjectDto);
        List<TblYqnsAuditProjectDetailEntity> auditProjectDetailEntities = auditProjectDetailMapper.selectList(
                new QueryWrapper<TblYqnsAuditProjectDetailEntity>()
                .eq("AUDITPROJECTID", tblYqnsAuditProjectDto.getId()));
        tblYqnsAuditProjectDto.setAuditProjectDetailEntityList(auditProjectDetailEntities);
        return ResponseFormat.retParam(1, "查询成功", tblYqnsAuditProjectDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveOrUpdate(String token,TblYqnsAuditProjectDto param) throws Exception {

        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        JsonBean jsonBean = null;
        if (param.getId() == null) {
            TblYqnsAuditProjectEntity auditProjectEntity = new TblYqnsAuditProjectEntity();
            BeanUtil.copyProperties(param, auditProjectEntity);
//            auditProjectEntity.setId(auditProjectMapper.getNextSequenceValue());
            auditProjectEntity.setId(RandomUtil.uuLongId());
            auditProjectEntity.setProjectId(projectId);
            auditProjectEntity.setCreater(loginStaff.getRealname());
            auditProjectMapper.insert(auditProjectEntity);
            List<TblYqnsAuditProjectDetailEntity> auditProjectDetailEntityList = param.getAuditProjectDetailEntityList();
            if (CollectionUtils.isNotEmpty(auditProjectDetailEntityList)) {
                for (TblYqnsAuditProjectDetailEntity tblYqnsAuditProjectDetailEntity : auditProjectDetailEntityList) {
//                    tblYqnsAuditProjectDetailEntity.setId(auditProjectDetailMapper.getNextSequenceValue());
                	tblYqnsAuditProjectDetailEntity.setId(RandomUtil.uuLongId());
                    tblYqnsAuditProjectDetailEntity.setAuditProjectId(auditProjectEntity.getId());
                    auditProjectDetailMapper.insert(tblYqnsAuditProjectDetailEntity);
                }
            }
            jsonBean = ResponseFormat.retParam(1, "添加成功");
        } else {
            TblYqnsAuditProjectEntity auditProjectEntity = new TblYqnsAuditProjectEntity();
            BeanUtil.copyProperties(param, auditProjectEntity);
            auditProjectMapper.updateById(auditProjectEntity);
            auditProjectDetailMapper.delete(new QueryWrapper<TblYqnsAuditProjectDetailEntity>()
                .eq("AUDITPROJECTID", param.getId()));
            List<TblYqnsAuditProjectDetailEntity> auditProjectDetailEntityList = param.getAuditProjectDetailEntityList();
            if (CollectionUtils.isNotEmpty(auditProjectDetailEntityList)) {
                for (TblYqnsAuditProjectDetailEntity tblYqnsAuditProjectDetailEntity : auditProjectDetailEntityList) {
//                    tblYqnsAuditProjectDetailEntity.setId(auditProjectDetailMapper.getNextSequenceValue());
                	tblYqnsAuditProjectDetailEntity.setId(RandomUtil.uuLongId());
                    tblYqnsAuditProjectDetailEntity.setAuditProjectId(param.getId());
                    auditProjectDetailMapper.insert(tblYqnsAuditProjectDetailEntity);
                }
            }
            jsonBean = ResponseFormat.retParam(1, "成功");
        }
        return jsonBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean delete(String token,Integer id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsAuditProjectEntity auditProjectEntity = new TblYqnsAuditProjectEntity();
        auditProjectEntity.setDeleted(BigDecimal.ONE);
        auditProjectEntity.setId(Long.valueOf(id));
        auditProjectMapper.updateById(auditProjectEntity);
        auditProjectDetailMapper.delete(new QueryWrapper<TblYqnsAuditProjectDetailEntity>()
                .eq("AUDITPROJECTID", id));
        return ResponseFormat.retParam(1, "删除成功", "");
    }
}
