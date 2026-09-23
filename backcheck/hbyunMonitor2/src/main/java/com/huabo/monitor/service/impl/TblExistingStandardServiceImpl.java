package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.TblAttachmentMapper;
import com.huabo.monitor.mapper.TblExistingStandardAttMapper;
import com.huabo.monitor.mapper.TblExistingStandardMapper;
import com.huabo.monitor.service.TblExistingStandardService;
import com.huabo.monitor.util.ConstClass;
import com.hbfk.util.redis.Random.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TblExistingStandardServiceImpl implements TblExistingStandardService {
    @Autowired
    private TblExistingStandardMapper tblExistingStandardMapper;
    @Autowired
    private TblExistingStandardAttMapper tblExistingStandardAttMapper;
    @Autowired
    private TblAttachmentMapper tblAttachmentMapper;
    @Resource
    private UserProvider userProvider;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean insertOrUpdate(TblExistingStandardVo entity) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (entity.getId() == null||entity.getId().compareTo(BigDecimal.ZERO) == 0) {
            entity.setId(RandomUtil.uuBigDecimalId());
            if (StringUtils.isBlank(entity.getRuleCode())){
                return new JsonBean(0, "请输入文件编号", null);
            }
            if (StringUtils.isBlank(entity.getRuleName())){
                return new JsonBean(0, "请输入文件名称", null);
            }
            if (StringUtils.isBlank(entity.getRuleNumber())){
                return new JsonBean(0, "请输入发文文号", null);
            }
            if (StringUtils.isBlank(entity.getPublishOrg())||entity.getCreateOrgId() == null||entity.getCreateOrgId().compareTo(BigDecimal.ZERO) == 0){
                return new JsonBean(0, "请选择发文部门", null);
            }
            if (StringUtils.isBlank(entity.getTimeLiness())){
                return new JsonBean(0, "请选择时效性", null);
            }
            if (entity.getPublishDate() == null){
                return new JsonBean(0, "请选择发文日期", null);
            }
            if (entity.getTakeEffectTime() == null){
                return new JsonBean(0, "请选择生效日期", null);
            }
            if (StringUtils.isBlank(entity.getEnteringPerson())||entity.getCreateStaffId() == null||entity.getCreateStaffId().compareTo(BigDecimal.ZERO) == 0){
                return new JsonBean(0, "请选择录入人", null);
            }
            if (StringUtils.isBlank(entity.getSummaryInfo())){
                return new JsonBean(0, "请输入摘要信息", null);
            }
            entity.setIsDelete(0);
            entity.setCreateTime(new Date());
            TblExistingStandard tblExistingStandard = new TblExistingStandard();
            BeanUtils.copyProperties(entity,tblExistingStandard);
            int result = tblExistingStandardMapper.insert(tblExistingStandard);
            if(entity.getAttids() != null && !"".equals(entity.getAttids())) {
                String[] attId = entity.getAttids().split(",");
                for (String aid : attId) {
                    TblExistingStandardAtt tblExistingStandardAtt = new TblExistingStandardAtt();
                    tblExistingStandardAtt.setAttId(new BigDecimal(aid));
                    tblExistingStandardAtt.setTesId(tblExistingStandard.getId());
                    tblExistingStandardAttMapper.insert(tblExistingStandardAtt);
                }
            }
            if (result > 0) {
                return new JsonBean(1, "新增成功", tblExistingStandard);
            }
                return new JsonBean(0, "新增失败", null);
        }else {
            //修改
            TblExistingStandard tblExistingStandard = tblExistingStandardMapper.selectById(entity.getId());
            if (tblExistingStandard.getId() == null) {
                return new JsonBean(0, "现行标准不存在", null);
            }
            System.out.println("_________________________________________________");
            TblExistingStandard tblExistingStandardNew = new TblExistingStandard();
            BeanUtils.copyProperties(entity,tblExistingStandardNew);
            int result = tblExistingStandardMapper.updateById(tblExistingStandardNew);
            QueryWrapper<TblExistingStandardAtt> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("TESID",tblExistingStandard.getId());
            tblExistingStandardAttMapper.delete(queryWrapper);
            if(entity.getAttids() != null && !"".equals(entity.getAttids())) {
                String[] attId = entity.getAttids().split(",");
                for (String aid : attId) {
                    TblExistingStandardAtt tblExistingStandardAtt = new TblExistingStandardAtt();
                    tblExistingStandardAtt.setAttId(new BigDecimal(aid));
                    tblExistingStandardAtt.setTesId(tblExistingStandard.getId());
                    tblExistingStandardAttMapper.insert(tblExistingStandardAtt);
                }
            }
            if (result > 0) {
                return new JsonBean(1, "修改成功", entity);
            }else
                return new JsonBean(0, "修改失败", null);
        }
    }

    @Override
    public JsonBean deleteById(BigDecimal id) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblExistingStandard tblExistingStandard = tblExistingStandardMapper.selectById(id);
        if (tblExistingStandard == null) {
            return new JsonBean(0, "现行标准不存在", null);
        }
        tblExistingStandard.setIsDelete(1);
        int result = tblExistingStandardMapper.updateById(tblExistingStandard);
        if (result > 0) {
            return new JsonBean(1, "删除成功", null);
        }
            return new JsonBean(0, "删除失败", null);
    }

    @Override
    public JsonBean selectList(String ruleNumber, String ruleName, String summaryInfo,Integer pageNumber,Integer pageSize) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblExistingStandard> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(ruleNumber)) {
            queryWrapper.like("RULENUMBER", ruleNumber);
        }
        if (StringUtils.isNotBlank(ruleName)) {
            queryWrapper.like("RULENAME", ruleName);
        }
        if (StringUtils.isNotBlank(summaryInfo)) {
            queryWrapper.like("SUMMARYINFO", summaryInfo);
        }
        queryWrapper.eq("IS_DELETE",0);
        queryWrapper.orderByDesc("ID");
        PageHelper.startPage(pageNumber,pageSize);
        PageInfo<TblExistingStandard> pageInfo = new PageInfo<>(tblExistingStandardMapper.selectList(queryWrapper));
        return new JsonBean(1, "查询成功", pageInfo);
    }

    @Override
    public JsonBean selectById(BigDecimal id) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblExistingStandard tblExistingStandard = tblExistingStandardMapper.selectById(id);
        if (tblExistingStandard == null) {
            return new JsonBean(0, "现行标准不存在", null);
        }
        QueryWrapper<TblExistingStandardAtt> attQueryWrapper = new QueryWrapper<>();
        attQueryWrapper.eq("TESID",id);
        List<TblExistingStandardAtt> tblExistingStandardAttList = tblExistingStandardAttMapper.selectList(attQueryWrapper);

        List<TblAttachment> tblAttachmentList = null;
        if (!tblExistingStandardAttList.isEmpty()) {
            QueryWrapper<TblAttachment> tblAttachmentQueryWrapper = new QueryWrapper<>();
            tblAttachmentQueryWrapper.in("ATTID",tblExistingStandardAttList.stream().map(TblExistingStandardAtt::getAttId).collect(Collectors.toList()));
            tblAttachmentList = tblAttachmentMapper.selectList(tblAttachmentQueryWrapper);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data",tblExistingStandard);
        map.put("attList",tblAttachmentList);
        return new JsonBean(1, "查询成功", map);
    }

    @Override
    public JsonBean previewById(BigDecimal id) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblExistingStandard tblExistingStandard = tblExistingStandardMapper.selectById(id);
        if (tblExistingStandard == null) {
            return new JsonBean(0, "现行标准不存在", null);
        }
            return new JsonBean(1, "查询成功", tblExistingStandard.getSummaryInfo());
    }

    @Override
    public JsonBean deleteAttachment(BigDecimal attid, BigDecimal tesId, String token) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 参数验证
        if (attid == null) {
            return new JsonBean(0, "附件ID不能为空", null);
        }

        if (tesId == null) {
            return new JsonBean(0, "现行标准ID不能为空", null);
        }

        try {
            // 精准删除：必须同时匹配附件ID和现行标准ID
            QueryWrapper<TblExistingStandardAtt> wrapper = new QueryWrapper<>();
            wrapper.eq("ATTID", attid);
            wrapper.eq("TESID", tesId);
            int deleteCount = tblExistingStandardAttMapper.delete(wrapper);

            if (deleteCount > 0) {
                return new JsonBean(1, "删除附件关联成功", null);
            } else {
                return new JsonBean(0, "未找到要删除的附件关联记录", null);
            }
        } catch (Exception e) {
            return new JsonBean(0, "删除附件关联失败: " + e.getMessage(), null);
        }
    }
}
