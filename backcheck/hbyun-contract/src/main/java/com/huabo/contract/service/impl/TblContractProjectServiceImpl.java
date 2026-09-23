package com.huabo.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblContractProject;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.mapper.TblAttachmentMapper;
import com.huabo.contract.mapper.TblContractProjectMapper;
import com.huabo.contract.mapper.TblLegalDisputregistrationMapper;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.service.TblContractProjectService;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import com.huabo.contract.vo.TblContractProjectVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TblContractProjectServiceImpl implements TblContractProjectService {
	@Autowired
	private TblContractProjectMapper tblContractProjectMapper;

	@Resource
	private TblLegalDisputregistrationMapper tblLegalDisputregistrationMapper;

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;

	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	@Resource
	private TblStaffService tblStaffService;
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	private UserProvider userProvider;

//  @Autowired
//  private ActivityPluginsService activityPluginsService;



	@Override
	public JsonBean tball(String token,List<TblContractProject> list ) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

    	if(list!=null && list.size()>0) {
    		for (TblContractProject tblContractProject : list) {
    			List<TblContractProject> pros = tblContractProjectMapper.selectbyxmid(tblContractProject.getUniuqeid());
    			if(pros!=null && pros.size()>0) {
    				tblContractProject.setUndertakestaffid(loginStaff.getStaffid().toString());
    				tblContractProject.setCreatestaffid(loginStaff.getStaffid()+"");
    				tblContractProjectMapper.updateEntitytb(tblContractProject);
    			}else {
    				TblOrganization organization = tblOrganizationMapper.findByname(tblContractProject.getXmorgname());
    				if(organization!=null ) {
    					tblContractProject.setUndertakeorgid(organization.getOrgid().toString());
    				}else {
    					tblContractProject.setUndertakeorgid(loginStaff.getLinkOrg().getOrgid().toString());
    				}
    				tblContractProject.setUndertakestaffid(loginStaff.getStaffid().toString());
    				tblContractProject.setMemo(tblContractProject.getProjectname());
    				tblContractProject.setCreatestaffid(loginStaff.getStaffid()+"");
    				tblContractProject.setCreatetime(new Date());
    				tblContractProject.setSTATE("0");
    				tblContractProject.setProjectid(RandomUtil.uuBigDecimalId());
    				tblContractProjectMapper.insertEntity(tblContractProject);
    			}
			}
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	resultMap.put("data", list);
    	return ResponseFormat.retParam(1,"同步成功！",null);
	}



	@Override
	public JsonBean cpPageList(String token, Integer pageNumber, Integer pageSize,
			TblContractProjectVo tblContractProjectVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		//Integer staffid = loginStaff.getStaffid().intValue(); 
		Integer staffid = loginStaff.getCurrentOrg().getOrgid().intValue();
		Integer bmorgid = loginStaff.getStaffid().intValue();
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);


    	IPage<TblContractProject> page = new Page<TblContractProject>(pageNumber,pageSize);
    	IPage<TblContractProject> pageList = this.tblContractProjectMapper.selectListByPageInfo(page,tblContractProjectVo,staffid,bmorgid);
    	/*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
			pageList.getRecords().forEach(item -> {
				if (StringUtils.isNotBlank(item.getUndertakestaffid())) {
					item.setUndertakestaffname(tblStaffService.getStaffName(Long.valueOf(item.getUndertakestaffid())));
				}
				if (StringUtils.isNotBlank(item.getUndertakeorgid())) {
					item.setUndertakeorgname(tblOrganizaService.getOrgName(Long.valueOf(item.getUndertakeorgid())));
				}
				if (Objects.nonNull(item.getStaffid1())) {
					item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
				}
				if (Objects.nonNull(item.getStaffid2())) {
					item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
				}
				if (Objects.nonNull(item.getStaffid3())) {
					item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
				}
				if (Objects.nonNull(item.getStaffid4())) {
					item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
				}
				if (Objects.nonNull(item.getStaffid5())) {
					item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
				}
				if (StringUtils.isNotBlank(item.getStaffids1())) {
					item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
				}
				if (StringUtils.isNotBlank(item.getStaffids2())) {
					item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
				}
				if (StringUtils.isNotBlank(item.getStaffids3())) {
					item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
				}
				if (StringUtils.isNotBlank(item.getStaffids4())) {
					item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
				}
				if (StringUtils.isNotBlank(item.getStaffids5())) {
					item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
				}
				if (Objects.nonNull(item.getOrgid1())) {
					item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
				}
				if (Objects.nonNull(item.getOrgid2())) {
					item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
				}
				if (Objects.nonNull(item.getOrgid3())) {
					item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
				}
				if (Objects.nonNull(item.getOrgid4())) {
					item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
				}
				if (Objects.nonNull(item.getOrgid5())) {
					item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
				}
				if (StringUtils.isNotBlank(item.getOrgids1())) {
					item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
				}
				if (StringUtils.isNotBlank(item.getOrgids2())) {
					item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
				}
				if (StringUtils.isNotBlank(item.getOrgids3())) {
					item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
				}
				if (StringUtils.isNotBlank(item.getOrgids4())) {
					item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
				}
				if (StringUtils.isNotBlank(item.getOrgids5())) {
					item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
				}
			});
		}*/

    	PageInfo<TblContractProject> pageInfo = new PageInfo<TblContractProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageList.getRecords());
    	pageInfo.setTotalRecord((int)pageList.getTotal());
    	pageInfo.getTotalPage();
//    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
//    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getContractProjectNo(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		String writen = this.tblOrganizationMapper.selectWrittenDeptByOrgId(loginStaff.getLinkOrg().getOrgid());

	    String counterpartno = writen+"-XM-";

	    Integer autoNum = this.tblContractProjectMapper.findAutoNumber(counterpartno,loginStaff.getCurrentOrg().getOrgid());
	    if (autoNum != null) {
	     autoNum++;
	    }else {
	     autoNum = 1;
	    }

	    counterpartno += autoNum;
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("projectcode", counterpartno);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean cpAdd(TblContractProject cp, String token, String attIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		Integer count = this.tblContractProjectMapper.selectPlanCodeByOrgid(cp);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}

		cp.setCreatestaffid(loginStaff.getStaffid()+"");
		cp.setCreatetime(new Date());
		cp.setSTATE("0");
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；

		if(cp.getProjectid() != null) {
			//修改；
//			this.tblContractProjectMapper.updateEntity(cp);
			tblContractProjectMapper.updateById(cp);
		}else {
			//新增；
			cp.setProjectid(RandomUtil.uuBigDecimalId());
//			this.tblContractProjectMapper.insertEntity(cp);
			tblContractProjectMapper.insert(cp);
		}
		if(attIds != null && !"".equals(attIds)) {
			String[] attId = attIds.split(",");
			for (String id : attId) {
				tblLegalDisputregistrationMapper.saveAttacheMent(5, cp.getProjectid(),new BigDecimal(id));
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("cp",cp);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean cpDelete(BigDecimal projectid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblContractProject plan = this.tblContractProjectMapper.selectEntityById(projectid);

		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}

//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblContractProjectMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(0,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblContractProjectMapper.deleteFileRelation(projectid);
		this.tblContractProjectMapper.deleteById(projectid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findContractProjectDetail(String token, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		TblContractProject plan = this.tblContractProjectMapper.selectEntityById(projectid);
		if (Objects.nonNull(plan)) {
			if (StringUtils.isNotBlank(plan.getUndertakestaffid())) {
				plan.setUndertakestaffname(tblStaffService.getStaffName(Long.valueOf(plan.getUndertakestaffid())));
			}
			if (StringUtils.isNotBlank(plan.getUndertakeorgid())) {
				plan.setUndertakeorgname(tblOrganizaService.getOrgName(Long.valueOf(plan.getUndertakeorgid())));
			}
			if (Objects.nonNull(plan.getStaffid1())) {
				plan.setRealname1(tblStaffService.getStaffName(plan.getStaffid1()));
			}
			if (Objects.nonNull(plan.getStaffid2())) {
				plan.setRealname2(tblStaffService.getStaffName(plan.getStaffid2()));
			}
			if (Objects.nonNull(plan.getStaffid3())) {
				plan.setRealname3(tblStaffService.getStaffName(plan.getStaffid3()));
			}
			if (Objects.nonNull(plan.getStaffid4())) {
				plan.setRealname4(tblStaffService.getStaffName(plan.getStaffid4()));
			}
			if (Objects.nonNull(plan.getStaffid5())) {
				plan.setRealname5(tblStaffService.getStaffName(plan.getStaffid5()));
			}
			if (StringUtils.isNotBlank(plan.getStaffids1())) {
				plan.setRealnames1(tblStaffService.getStaffNames(plan.getStaffids1()));
			}
			if (StringUtils.isNotBlank(plan.getStaffids2())) {
				plan.setRealnames2(tblStaffService.getStaffNames(plan.getStaffids2()));
			}
			if (StringUtils.isNotBlank(plan.getStaffids3())) {
				plan.setRealnames3(tblStaffService.getStaffNames(plan.getStaffids3()));
			}
			if (StringUtils.isNotBlank(plan.getStaffids4())) {
				plan.setRealnames4(tblStaffService.getStaffNames(plan.getStaffids4()));
			}
			if (StringUtils.isNotBlank(plan.getStaffids5())) {
				plan.setRealnames5(tblStaffService.getStaffNames(plan.getStaffids5()));
			}
			if (Objects.nonNull(plan.getOrgid1())) {
				plan.setOrgname1(tblOrganizaService.getOrgName(plan.getOrgid1()));
			}
			if (Objects.nonNull(plan.getOrgid2())) {
				plan.setOrgname2(tblOrganizaService.getOrgName(plan.getOrgid2()));
			}
			if (Objects.nonNull(plan.getOrgid3())) {
				plan.setOrgname3(tblOrganizaService.getOrgName(plan.getOrgid3()));
			}
			if (Objects.nonNull(plan.getOrgid4())) {
				plan.setOrgname4(tblOrganizaService.getOrgName(plan.getOrgid4()));
			}
			if (Objects.nonNull(plan.getOrgid5())) {
				plan.setOrgname5(tblOrganizaService.getOrgName(plan.getOrgid5()));
			}
			if (StringUtils.isNotBlank(plan.getOrgids1())) {
				plan.setOrgnames1(tblOrganizaService.getOrgNames(plan.getOrgids1()));
			}
			if (StringUtils.isNotBlank(plan.getOrgids2())) {
				plan.setOrgnames2(tblOrganizaService.getOrgNames(plan.getOrgids2()));
			}
			if (StringUtils.isNotBlank(plan.getOrgids3())) {
				plan.setOrgnames3(tblOrganizaService.getOrgNames(plan.getOrgids3()));
			}
			if (StringUtils.isNotBlank(plan.getOrgids4())) {
				plan.setOrgnames4(tblOrganizaService.getOrgNames(plan.getOrgids4()));
			}
			if (StringUtils.isNotBlank(plan.getOrgids5())) {
				plan.setOrgnames5(tblOrganizaService.getOrgNames(plan.getOrgids5()));
			}
		}
		resultMap.put("cp", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean removeFile(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		TblAttachment tblAttachmentEntity = this.tblAttachmentMapper.findById(attId);
        if (tblAttachmentEntity == null) {
        	return ResponseFormat.retParam(0,203,null);
        }
        FtpUtil.removeFile(tblAttachmentEntity.getAttpath());
        this.tblContractProjectMapper.deleteFileId(attId);
        this.tblAttachmentMapper.deleteAtt(attId);
        return ResponseFormat.retParam(1,200,null);
	}

}
