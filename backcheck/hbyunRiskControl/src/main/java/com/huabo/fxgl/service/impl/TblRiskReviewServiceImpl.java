package com.huabo.fxgl.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import com.huabo.fxgl.entity.TblRiskReview;
import com.huabo.fxgl.entity.TblRiskReviewOpinion;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblRiskReviewOpinionMapper;
import com.huabo.fxgl.mapper.TblRiskReviewOracleMapper;
import com.huabo.fxgl.service.TblRiskReviewService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

@Service
public class TblRiskReviewServiceImpl extends ServiceImpl<TblRiskReviewOracleMapper,TblRiskReview> implements TblRiskReviewService {

    @Autowired
    private TblRiskReviewOracleMapper tblRiskReviewOracleMapper;
    
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private TblRiskReviewOpinionMapper  tblRiskReviewOpinionMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public PageInfo<TblRiskReview>  riskReviewList(String token,Integer pageNumber,Integer pageSize,String mattername,String mattercode,String riskreviewcode,String state,
			BigDecimal staffid,Integer authorityType) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		final TblRiskReview entity = new TblRiskReview();
		entity.setStaffid(staffid.toString());
		entity.setState(state);
		entity.setRiskreviewcode(riskreviewcode);
		entity.setMattercode(mattercode);
		entity.setMattername(mattername);
		entity.setLinkorgid(loginStaff.getLinkOrg().getOrgid());
		com.github.pagehelper.PageInfo<TblRiskReview> pageInfo=null;
		try {
		//	String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "r.LINKORGID", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
		 String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(loginStaff.getCurrentOrg().getUseSecrect(), false, "r.LINKORGID", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds(),authorityType));
             pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblRiskReviewOracleMapper.getQueryList(entity, authorityType,sql));
			FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(item->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId field=new fieldOrgStaffId();
						BeanUtils.copyProperties(item,field); 
						fieldOrgStaffName nameEntity=ment.setOpenName(field);
						BeanUtils.copyProperties(nameEntity,item ); 
						field=null; // 处理并解除引用
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
    public Integer updateRiskReview(TblRiskReview tblRiskReview) {
        return tblRiskReviewOracleMapper.updateById(tblRiskReview);
    }

    @Override
    public Integer insertRiskReview(TblRiskReview tblRiskReview) {
        return tblRiskReviewOracleMapper.insert(tblRiskReview);
    }

    @Override
    public TblRiskReview riskReviewDetails(String reviewid) {
    	TblRiskReview entity=null;
    	try {
    		entity=tblRiskReviewOracleMapper.getDetalById(reviewid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return entity;
    }

    @Override
    public Integer riskReviewDelete(String reviewid) {
        return tblRiskReviewOracleMapper.deleteById(reviewid);
    }
;
    @Override
    public Long queryCodeNumber(String projectCode) {
        QueryWrapper<TblRiskReview> queryWrapper = new QueryWrapper<TblRiskReview>();
        queryWrapper.eq("MATTERPROJECTCODE",projectCode);
        return tblRiskReviewOracleMapper.selectCount(queryWrapper);
    }

	@Override
	public Map<String, Object> getCompanyRiskReview(String token, String year) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		 try {
			List<Organization> orgList=tblRiskReviewOracleMapper.getReviewCompanyList();
			int[] yList=new int[orgList.size()];
			String[] xList=new String[orgList.size()];
			for(int i=0;i<orgList.size();i++){
				Organization o=orgList.get(i);
//				Integer s=tblRiskReviewOracleMapper.getReviewByCompanyYear(o.getOrgid(),year);
				Integer s=tblRiskReviewOracleMapper.getReviewByCompany(o.getOrgid());
				yList[i]=s;
				xList[i]=o.getOrgname();
			}
			map.put("x", xList);
			map.put("y", yList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public TblRiskReviewOpinion riskReviewOpinionDetails(String id) throws Exception {
		TblRiskReviewOpinion opion=new TblRiskReviewOpinion();
		try {
			opion=tblRiskReviewOpinionMapper.getOneDetail(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return opion;
	}

	@Override
	public Map<String, Object> saveReviewOpinion(TblRiskReviewOpinion opin) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		try {
			if(opin.getId()!=null){
				tblRiskReviewOpinionMapper.updateById(opin);
			}else{
				tblRiskReviewOpinionMapper.insert(opin);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.put("opion", opin);
		return map;
	}
	
    @Override
    public Integer riskReviewOpinionDelete(String id) {
        return tblRiskReviewOpinionMapper.delOneById(id);
    }

	@Override
	public PageInfo<TblRiskReview> riskReviewOpinionList(String token, Integer pageNumber, Integer pageSize,
			String mattername, String mattercode, String riskreviewcode, String state, BigDecimal staffid,
			Integer authorityType,String unitname) throws Exception {

		final TblRiskReview entity = new TblRiskReview();
		entity.setStaffid(staffid.toString());
		entity.setState(state);
		entity.setRiskreviewcode(riskreviewcode);
		entity.setMattercode(mattercode);
		entity.setMattername(mattername);
	   if(org.apache.commons.lang.StringUtils.isNotBlank(unitname)){
		entity.setStaffunitname(unitname);
	   }
		com.github.pagehelper.PageInfo<TblRiskReview> pageInfo=null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
//			entity.setLinkorgid(loginStaff.getLinkOrg().getOrgid());
			String  sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "r.LINKORGID", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
	     pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblRiskReviewOracleMapper.getQueryList(entity, authorityType,sql));
	     FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(item->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId field=new fieldOrgStaffId();
						BeanUtils.copyProperties(item,field); 
						fieldOrgStaffName nameEntity=ment.setOpenName(field);
						BeanUtils.copyProperties(nameEntity,item); 
						field=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
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
	public List<TblRiskReview> getRiskReviewListExport(String token, String mattername, String mattercode,
			String riskreviewcode, String state, BigDecimal staffid, Integer authorityType) throws Exception {
		// TODO Auto-generated method stub
		//集团查看全部数据； 分公司查看自己的数据；
    	TblStaffUtil loginStaff = userProvider.get();
		final TblRiskReview entity = new TblRiskReview();
		entity.setStaffid(staffid.toString());
		entity.setState(state);
		entity.setRiskreviewcode(riskreviewcode);
		entity.setMattercode(mattercode);
		entity.setMattername(mattername);
		entity.setLinkorgid(loginStaff.getLinkOrg().getOrgid());
		List<TblRiskReview> pageInfo=null;
		try {
		
			//String  sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "r.LINKORGID", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
			 String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(loginStaff.getCurrentOrg().getUseSecrect(), false, "r.LINKORGID", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds(),authorityType));
			pageInfo= tblRiskReviewOracleMapper.getQueryList(entity, authorityType,sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}

	 
}
