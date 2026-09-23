package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuVerifyEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.service.TblYqnsAuditOverseeRecordsService;

import com.huabo.audit.service.impl.ReservePropertyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeRecordsServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-审计实施-审计督导记录 serviceImpl
 */
@Service
public class TblYqnsAuditOverseeRecordsServiceImpl extends ServiceImpl<TblYqnsAuditOverseeRecordsMapper, TblYqnsAuditOverseeRecordsEntity>
        implements TblYqnsAuditOverseeRecordsService {

    @Resource
    private TblYqnsAuditOverseeRecordsMapper tblYqnsAuditOverseeRecordsMapper;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
    private ImplementPlanMapper implementPlanMapper;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
    private TblStaffMapper tblStaffMapper;
    
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private FundAuditProjectMapper fundAuditProjectMapper;
    
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
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

    /**
     * 获取分页的审计督导记录列表
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getRecordsList(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditOverseeRecordsEntity entity, Integer xmnd, BigDecimal staffId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        if(xmnd == null && staffId == null ) {
        	if(entity.getProjectId()==null) {
        		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
                if(tnp == null) {
                    return ResponseFormat.retParam(0,30003,null);
                }
                BigDecimal projectId = tnp.getId();
                if(null == projectId) {
                    return ResponseFormat.retParam(0,30003,null);
                }
                entity.setProjectId(projectId);
        	}
        	
        }else {
        	entity.setStatus(6);
        }
        

        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditWorkRecordsEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditWorkRecordsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectRecordsList(entity,xmnd,staffId, entity.getProjectId()));

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getList());

        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 获取单独一个审计工作记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getRecordsById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) { 
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsAuditOverseeRecordsEntity bean = this.getById(id);
        if(bean!=null) {
        	ImplementPlanEntity plan = implementPlanMapper.selectById(bean.getProjectId().toString());

			if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", plan.getXmapbid()));
	        	if(fund!=null) {
	        		TblYqnsXmdq xmqd=new TblYqnsXmdq();
		        	xmqd.setGljhxmid(fund.getGljhxmid());
		        	xmqd.setPlanid(fund.getPlanid());
		        	xmqd.setPlanname(fund.getPlanname());
		        	xmqd.setGljhxmlx(fund.getGljhxmlx());
		        	xmqd.setZsstaffid(fund.getApproverId());
		        	xmqd.setZsname(fund.getApprover());
		        	xmqd.setSiteEndTime(fund.getXcendtime());
		        	xmqd.setXmname(fund.getName());
		        	xmqd.setAssistApprover(fund.getAssistApprover());
		         	xmqd.setAssistApproverId(fund.getAssistApproverId());
		         	xmqd.setFzzStafffId(fund.getFzzStafffId());
		         	xmqd.setFzzName(fund.getFzzName());
		         	xmqd.setSsorgname(fund.getExePhraseUnit());
		         	xmqd.setSsorgid(fund.getExePhraseUnitId());
		         	xmqd.setGroupLeader(fund.getGroupLeader());
		         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
		        	plan.setXmqd(xmqd);
		        	bean.setXmqd(xmqd);
	        	}
	        	
	        }
	        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", plan.getXmapbid()));
	        	 if(endin!=null) {
	        		 TblYqnsXmdq xmqd=new TblYqnsXmdq();
			         	xmqd.setGljhxmid(endin.getGljhxmid());
			         	xmqd.setPlanid(endin.getPlanid()); 
			         	xmqd.setPlanname(endin.getPlanname());
			         	xmqd.setGljhxmlx(endin.getGljhxmlx());
			         	xmqd.setZsstaffid(endin.getApproverId());
			         	xmqd.setZsname(endin.getApprover());
			         	xmqd.setSiteEndTime(endin.getXcendtime());
			         	xmqd.setXmname(endin.getName());
			         	xmqd.setAssistApprover(endin.getAssistApprover());
			         	xmqd.setAssistApproverId(endin.getAssistApproverId());
			         	xmqd.setFzzStafffId(endin.getFzzStafffId());
			         	xmqd.setFzzName(endin.getFzzName());
			         	xmqd.setSsorgname(endin.getExePhraseUnit());
			         	xmqd.setSsorgid(endin.getExePhraseUnitId());
			         	xmqd.setGroupLeader(endin.getGroupLeader());
			         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
			         	plan.setXmqd(xmqd);
			         	bean.setXmqd(xmqd);
	        	 }
	        	 
	        }
		
        }
        if (Objects.nonNull(bean.getOrgId())){
			bean.setTblOrganization(tblOrganizationMapper.findById(bean.getOrgId()));
		}
        if (Objects.nonNull(bean.getAuditStaffId())){
			bean.setTblStaff(tblStaffMapper.getById(bean.getAuditStaffId().toString()));
		}
        
        List<TblAttachment> attlist = tblAttachmentMapper.selectDUDAOAttInfoList(id);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);

        Map<String,Object> res = new HashMap<String, Object>();
        res.put("bean", bean);
        res.put("attlist", attlist);
        
        return ResponseFormat.retParam(1, 200, res);
    }

    /**
     * 审计督导记录-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsAuditOverseeRecordsEntity entity,String attids) throws Exception {
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
        if(entity.getProjectId()!=null) {
        	projectId=entity.getProjectId();
        }
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        entity.setProjectId(projectId);
        entity.setCreateUser(loginStaff.getRealname());
        entity.setCreateStaffId(loginStaff.getStaffid());
        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(loginStaff.getStaffid().toString());
            if(attids!=null && attids.length()>0) {
                tblYqnsAuditOverseeRecordsMapper.delFileRelation(entity.getId());
                String[] stins = attids.split(",");
                for (String attid : stins) {
                    tblYqnsAuditOverseeRecordsMapper.insetFileRelation(attid, entity.getId());
                }

            }
        }else {
        	entity.setId(RandomUtil.uuLongId());
        }

        boolean ret = this.saveOrUpdate(entity);
		if(attids!=null && attids.length()>0) {
			String[] stins = attids.split(",");
			for (String attid : stins) {
				tblYqnsAuditOverseeRecordsMapper.insetFileRelation(attid, entity.getId());
			}

		}
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, entity);
    }

    /**
     * 删除审计督导记录(直接删除)
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
            tblYqnsAuditOverseeRecordsMapper.delFileRelation(id);
            if (!ret) {
                return ResponseFormat.retParam(0, -1, Boolean.FALSE);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 查询审计督导记录
     *
     * @param entity
     * @param staffId 
     * @param xmnd 
     * @return
     */
    public List<TblYqnsAuditOverseeRecordsEntity> selectRecordsList(TblYqnsAuditOverseeRecordsEntity entity, Integer xmnd, BigDecimal staffId,BigDecimal projectid) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsAuditOverseeRecordsEntity> query = new LambdaQueryWrapper<TblYqnsAuditOverseeRecordsEntity>();
        
        if(StringUtils.isNotBlank(entity.getProjectName())) {
        	query.like(TblYqnsAuditOverseeRecordsEntity::getProjectName, entity.getProjectName());
        }
        if(StringUtils.isNotBlank(entity.getSxnumber())) {
        	query.like(TblYqnsAuditOverseeRecordsEntity::getSxnumber, entity.getSxnumber());
        }
        if(entity.getStatus() != null) {
        	query.eq(TblYqnsAuditOverseeRecordsEntity::getStatus, entity.getStatus());
        }
        if(entity.getProjectId() != null) {
        	query.eq(TblYqnsAuditOverseeRecordsEntity::getProjectId, entity.getProjectId());
        }
        if(staffId != null) {
        	query.and(wq -> wq.eq(TblYqnsAuditOverseeRecordsEntity::getCreateStaffId, staffId).or().eq(TblYqnsAuditOverseeRecordsEntity::getSupervisionParticipantsId, staffId)
                	.or().eq(TblYqnsAuditOverseeRecordsEntity::getEconomicId, staffId));
        }
        if(xmnd != null) {
        	query.ge(TblYqnsAuditOverseeRecordsEntity::getCreateTime, DateUtil.getYearStartDate(xmnd)).le(TblYqnsAuditOverseeRecordsEntity::getCreateTime, DateUtil.getYearEndDate(xmnd));
        }
        
        return tblYqnsAuditOverseeRecordsMapper.selectList(query);
    }
}
