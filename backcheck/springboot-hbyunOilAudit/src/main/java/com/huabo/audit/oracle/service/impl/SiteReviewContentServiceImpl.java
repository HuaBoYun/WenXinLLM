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
import com.huabo.audit.oracle.dto.TblYqnsSiteReviewAttEntityDto;
import com.huabo.audit.oracle.dto.TblYqnsSiteReviewContentDetailDto;
import com.huabo.audit.oracle.dto.TblYqnsSiteReviewContentDto;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSiteReviewAttEntity;
import com.huabo.audit.oracle.entity.TblYqnsSiteReviewContentEntity;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.service.SiteReviewContentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-09 14:34
 **/
@Service
@Slf4j
public class SiteReviewContentServiceImpl implements SiteReviewContentService {

    @Resource
    private SiteReviewContentMapper siteReviewContentMapper;
    @Resource
    private SiteReviewAttMapper siteReviewAttMapper;
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private UserProvider userProvider;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveOrUpdate(String token,TblYqnsSiteReviewContentDto param) throws Exception {
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
        log.info("保存/更新现场审查主要内容入参:{}", JSONObject.toJSONString(param));
        JsonBean jsonBean = null;
        if (param.getId() == null) {
            TblYqnsSiteReviewContentEntity siteReviewContentEntity = new TblYqnsSiteReviewContentEntity();
            BeanUtil.copyProperties(param, siteReviewContentEntity);
//            siteReviewContentEntity.setId(siteReviewContentMapper.getNextSequenceValue());
            siteReviewContentEntity.setId(RandomUtil.uuLongId());
            siteReviewContentEntity.setProjectId(projectId);
            siteReviewContentEntity.setTemplateId(param.getTemplateId());
            siteReviewContentMapper.insert(siteReviewContentEntity);
            if (CollectionUtils.isNotEmpty(param.getSiteReviewAttEntityList())) {
                for (TblYqnsSiteReviewAttEntity tblYqnsSiteReviewAttEntity : param.getSiteReviewAttEntityList()) {
                    tblYqnsSiteReviewAttEntity.setId(siteReviewAttMapper.getNextSequenceValue());
                    tblYqnsSiteReviewAttEntity.setSiteReviewContentId(siteReviewContentEntity.getId());
                    siteReviewAttMapper.insert(tblYqnsSiteReviewAttEntity);
                }
            }
            jsonBean = ResponseFormat.retParam(1, "添加成功",siteReviewContentEntity);
        } else {
            TblYqnsSiteReviewContentEntity siteReviewContentEntity = new TblYqnsSiteReviewContentEntity();
            BeanUtil.copyProperties(param, siteReviewContentEntity);
            siteReviewContentMapper.updateById(siteReviewContentEntity);
            siteReviewAttMapper.delete(new QueryWrapper<TblYqnsSiteReviewAttEntity>()
                    .eq("SITEREVIEWCONTENTID", siteReviewContentEntity.getId()));
            if (CollectionUtils.isNotEmpty(param.getSiteReviewAttEntityList())) {
                for (TblYqnsSiteReviewAttEntity tblYqnsSiteReviewAttEntity : param.getSiteReviewAttEntityList()) {
                    tblYqnsSiteReviewAttEntity.setId(siteReviewAttMapper.getNextSequenceValue());
                    tblYqnsSiteReviewAttEntity.setSiteReviewContentId(siteReviewContentEntity.getId());
                    siteReviewAttMapper.insert(tblYqnsSiteReviewAttEntity);
                }
            }
            jsonBean = ResponseFormat.retParam(1, "成功",siteReviewContentEntity);
        }
        return jsonBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean delete(String token,Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsSiteReviewContentEntity siteReviewContentEntity = new TblYqnsSiteReviewContentEntity();
        siteReviewContentEntity.setDeleted(BigDecimal.ONE);
        siteReviewContentEntity.setId(id);
        siteReviewContentMapper.updateById(siteReviewContentEntity);
        siteReviewAttMapper.delete(new QueryWrapper<TblYqnsSiteReviewAttEntity>()
                .eq("SITEREVIEWCONTENTID", id));
        return ResponseFormat.retParam(1, "删除成功", "");
    }

    //获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }

    @Override
    public JsonBean findSiteReviewContentListByParam(String token,String projectName, String settleProjectNum, 
    		Integer pageNumber, Integer pageSize, BigDecimal templateId,BigDecimal projectId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        if(projectId==null) {
        	ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if(tnp == null) {
                return ResponseFormat.retParam(0,30003,null);
            }
            projectId = tnp.getId();
        }
        
        HashMap<String, Object> result = new HashMap<>();
        com.huabo.audit.util.PageInfo<TblYqnsSiteReviewContentEntity> info = new com.huabo.audit.util.PageInfo<>();
        PageInfo<TblYqnsSiteReviewContentEntity> pageInfo;
        QueryWrapper<TblYqnsSiteReviewContentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("DELETED", 0);
        wrapper.eq("PROJECTID", projectId);
        if (StringUtils.isNotEmpty(settleProjectNum)) {
            wrapper.like("SETTLEPROJECTNUM", settleProjectNum);
        }
        if (StringUtils.isNotEmpty(projectName)) {
            wrapper.like("PROJECTNAME", projectName);
        }
        
        if(templateId != null) {
        	wrapper.eq("TEMPLATEID", templateId);
        }
        
        pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> siteReviewContentMapper.selectList(wrapper));

        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);

        return ResponseFormat.retParam(1, "查询成功", result);
    }

    @Override
    public JsonBean findOneSiteReviewById(String token,Long siteReviewId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsSiteReviewContentEntity siteReviewContentEntity = siteReviewContentMapper.selectById(siteReviewId);
        if (siteReviewContentEntity == null) {
            return ResponseFormat.retParam(0, "暂无此数据", null);
        }
        log.info("现场审查主要内容:{}", JSONObject.toJSONString(siteReviewContentEntity));
        TblYqnsSiteReviewContentDetailDto siteReviewContentDetailDto = new TblYqnsSiteReviewContentDetailDto();
        BeanUtil.copyProperties(siteReviewContentEntity, siteReviewContentDetailDto);
        List<TblYqnsSiteReviewAttEntity> siteReviewAttEntityList = siteReviewAttMapper.selectList(new QueryWrapper<TblYqnsSiteReviewAttEntity>()
                .eq("SITEREVIEWCONTENTID", siteReviewContentEntity.getId()));
        if (CollectionUtils.isEmpty(siteReviewAttEntityList)) {
            return ResponseFormat.retParam(1, "查询成功", siteReviewContentDetailDto);
        }
        List<TblYqnsSiteReviewAttEntityDto> siteReviewAttEntityDtos = new ArrayList<>();
        for (TblYqnsSiteReviewAttEntity siteReviewAttEntity : siteReviewAttEntityList) {
            TblYqnsSiteReviewAttEntityDto siteReviewAttEntityDto = new TblYqnsSiteReviewAttEntityDto();
            BeanUtil.copyProperties(siteReviewAttEntity, siteReviewAttEntityDto);
            try {
                TblAttachment tblAttachment = tblAttachmentMapper.selectEntityById(siteReviewAttEntityDto.getAttachmentId());
                siteReviewAttEntityDto.setAttname(tblAttachment.getAttname());
                siteReviewAttEntityDto.setAttsize(tblAttachment.getAttsize());
                siteReviewAttEntityDto.setUploader(tblAttachment.getUploader());
                siteReviewAttEntityDto.setAttid(tblAttachment.getAttid());
            } catch (Exception e) {
                log.error("查询附件信息异常:", e);
            }
            siteReviewAttEntityDtos.add(siteReviewAttEntityDto);
        }
        siteReviewContentDetailDto.setSiteReviewAttEntityDtoList(siteReviewAttEntityDtos);
        return ResponseFormat.retParam(1, "查询成功", siteReviewContentDetailDto);
    }
}
