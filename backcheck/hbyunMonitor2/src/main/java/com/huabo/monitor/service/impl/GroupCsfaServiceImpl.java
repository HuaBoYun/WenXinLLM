package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.service.CsfaService;
import com.huabo.monitor.service.GroupCsfaService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTesttaskService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service

public class GroupCsfaServiceImpl extends ServiceImpl<TblGroupTestplanMapper, TblGroupTestplan> implements GroupCsfaService{

    @Resource
    TblGroupTestplanMapper  testplanMapper;

    @Resource
    YhrPageMapper  yhrPageMapper;

    @Resource
    TblTesttaskMapper   testtaskMapper;

    @Resource
    TblTesttaskAttMapper  testtaskAttMapper;

    @Resource
    TblTestelementMapper  testelementMapper;
    
    @Resource
    TblOrganizaService tblOrganizaService;
    
    @Resource
     ITblStaffService staffService;
    
    @Resource
    TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private  TblGroupTemplateDetailMapper  tblGroupTemplateDetailMapper;

    @Override
    public boolean isSJByOrgId(String userOrgid) {
        String sql = "select count(*) from TBL_ORGANIZATION where audittype = '1' and orgid = " + userOrgid;
        Long num = yhrPageMapper.queryCount(sql);
        if (num == 0) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Map<String, Object>  queryOneTestPlan(BigDecimal selectProjectid) {
    	Map<String, Object> result=new HashMap<String, Object>();
    	try {
    		TblGroupTestplan plan=testplanMapper.getOneTblGroupTestplan(selectProjectid);
    		List<TblGroupTemplateDetail> detailsList=tblGroupTemplateDetailMapper.getListByGroupid(plan.getId());
    		plan.setDetailsList(detailsList);
    		String sql="select * from TBL_ATTACHMENT where attid in (select attid from TBL_GROUPTESTPLAN_ATT where id="+selectProjectid+"  )";
    		List<TblAttachment>  atts=  tblAttachmentMapper.getListBySql(sql);
    		  //字段变活赋值realname
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(plan,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,plan ); 
    		result.put("test", plan);
    		result.put("fjList", atts);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return result;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTestPlan(BigDecimal selectProjectid) {
        this.testplanMapper.deleteTestPlan(selectProjectid);
    }
//
	@Override
	public PageInfo<TblGroupTestplan> findAllNew(TblGroupTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, BigDecimal orgid, BigDecimal staffid, Integer authorityType,TblStaffUtil user) throws Exception{
		// TODO Auto-generated method stub
		PageInfo<TblGroupTestplan> pageInfo=null;
		try {  
		List<BigDecimal> orgList= tblOrganizaService.getTblOrganizationAll(orgid);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotBlank(starttime_min)){
			 plan.setStarttime(sdf.parse(starttime_min));
		}
		if(StringUtils.isNotBlank(starttime_max)){
			 plan.setEndtime(sdf.parse(starttime_max));
		}
		  plan.setCreatid(staffid);
		 plan.setOrgids(orgList);
		 //String t,String staff,String org,String dept,
		 
		 String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), authorityType==0, "LINKORGID", "LINKDEPTID", "CREATID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
		 
		 pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllNew(plan,sql));
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}
//
	@Override
	public PageInfo<TblGroupTestplan> findAllnoSjNew(TblGroupTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, String toString, Integer authorityType,TblStaffUtil user) throws Exception{
		// TODO Auto-generated method stub
		 PageInfo<TblGroupTestplan> pageInfo=null;
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(starttime_min)){
				 plan.setStarttime(sdf.parse(starttime_min));
			}
			if(StringUtils.isNotBlank(starttime_max)){
				 plan.setEndtime(sdf.parse(starttime_max));
			}
		if(StringUtils.isNotBlank(toString)){
		plan.setCreatid(new BigDecimal(toString));
		}
		String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), authorityType==0, "LINKORGID", "LINKDEPTID", "CREATID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
		pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllnoSjNew(plan,sql));
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}

	
	
	
	@Override
	public void deleteAtt(BigDecimal attid) {
		// TODO Auto-generated method stub
		 testplanMapper.deleteAtt(attid);
	     tblAttachmentMapper.deleteEntity(attid);
	}

 
	 

	@Override
	public PageInfo<TblGroupTestplan> findAll(TblGroupTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, BigDecimal orgid, BigDecimal staffid, Integer authorityType, TblStaffUtil user)
					throws Exception {
		 PageInfo<TblGroupTestplan> pageInfo=null;
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(starttime_min)){
				 plan.setStarttime(sdf.parse(starttime_min));
			}
			if(StringUtils.isNotBlank(starttime_max)){
				 plan.setEndtime(sdf.parse(starttime_max));
			}
		String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), authorityType==0, "LINKORGID", "LINKDEPTID", "CREATID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
		pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllnoSjNew(plan,sql));
		FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
			pageInfo.getList().forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}
	 

}