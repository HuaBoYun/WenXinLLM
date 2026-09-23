package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.vo.param.fieldOrgStaffId;
import com.huabo.audit.vo.param.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.oracle.entity.TblNbsjBugEntity;
import com.huabo.audit.oracle.mapper.TblNbsjBugCriterionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBugMapper;
import com.huabo.audit.service.TblNbsjBugCriterionService;
import com.huabo.audit.util.FiexibleNameAssignment;
import com.huabo.audit.util.PageInfo;
@Service
public class TblNbsjBugCriterionServiceImpl implements TblNbsjBugCriterionService {

	@Resource
	private TblNbsjBugCriterionMapper tblNbsjBugCriterionMapper;
	
	@Resource
	private TblNbsjBugMapper tblNbsjBugMapper;
	
	@Resource
    private UserProvider userProvider;
	
//	@Override
//	public JsonBean getNbsjBugCriterionListForMerge(String token) throws Exception {
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return ResponseFormat.retParam(0,20006,null);
//		}
//		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		List<TblNbsjBugCriterion> typeList = this.tblNbsjBugCriterionMapper.selectNbsjBugCriterionListForMerge(loginStaff.getCurrentOrg().getOrgid());
//		resultMap.put("typeList", typeList);
//		return ResponseFormat.retParam(1,200,resultMap);
//	}

	@Override
	public JsonBean saveNbsjBugCriterion(TblNbsjBugCriterion con, String token) throws Exception {
		try {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if (con != null && con.getBugcriid() == null) {
			con.setVersion(1);
			if(loginStaff.getCurrentOrg()!=null&&loginStaff.getCurrentOrg().getOrgid()!=null){
				con.setOrgid(loginStaff.getCurrentOrg().getOrgid());
			}
			con.setCreatestaffid(loginStaff.getStaffid());
			con.setCreatetime(new Date());
			con.setLinkdeptid(loginStaff.getLinkDetp().getOrgid());
			con.setBugcriid(RandomUtil.uuBigDecimalId());
			tblNbsjBugCriterionMapper.insert(con);
			}else {
				TblNbsjBugCriterion nbsjBug=tblNbsjBugCriterionMapper.selectTblNbsjCriterion(con.getBugcriid().toString());
				con.setVersion(nbsjBug.getVersion()+1);
				if(loginStaff.getCurrentOrg()!=null&&loginStaff.getCurrentOrg().getOrgid()!=null){
					con.setOrgid(loginStaff.getCurrentOrg().getOrgid());
				}
				tblNbsjBugCriterionMapper.updateById(con);
 		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblNbsjBugCriterion",con);
		return ResponseFormat.retParam(1,200,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public  JsonBean  SelectNbsjBugCriterion(String bugid, String token) throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjBugCriterion nbsjBug=tblNbsjBugCriterionMapper.selectTblNbsjCriterion(bugid);
		FiexibleNameAssignment ment=new FiexibleNameAssignment();
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(nbsjBug,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,nbsjBug ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		resultMap.put("date", nbsjBug);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delNbsjBugCriterion(String bugcriid, String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
   			return ResponseFormat.retParam(1,20006,null);
		}
		
		TblNbsjBugCriterion nbsjBug=tblNbsjBugCriterionMapper.selectTblNbsjCriterion(bugcriid);
		List<TblNbsjBugEntity> findByCriterionId = tblNbsjBugMapper.findByCriterionId(nbsjBug.getBugcriid());
		if(null!=findByCriterionId&&findByCriterionId.size()>0) {
    			return ResponseFormat.retParam(1,"缺陷标准使用中,不能删除！",null);
		}
		
		 tblNbsjBugCriterionMapper.delTblBugCriterion(bugcriid);
 		return ResponseFormat.retParam(1,70003,null);
	}

	@Override
	public JsonBean selectNbsjBugCriterionByPageInfo(String token, Integer pageNumber, Integer pageSize,String bugtype) throws Exception {
    	
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    /*	PageInfo<TblNbsjBugCriterion> pageInfo = new PageInfo<TblNbsjBugCriterion>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjBugCriterionMapper.selectNbsjBugCriterionListByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid().toString()));
    	pageInfo.setTotalRecord(this.tblNbsjBugCriterionMapper.selectNbsjBugCriterionListCountByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid().toString()));
    	pageInfo.getTotalPage();*/
        String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "orgid", "linkdeptid", "createstaffid", "SECRECTLEVELID", "STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()); 
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjBugCriterion> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjBugCriterionMapper.selectNbsjBugCriterionListByPageInfoXml(loginStaff.getCurrentOrg().getOrgid().toString(),bugtype,sql));
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
		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjBugCriterion> build = new PageResult<TblNbsjBugCriterion>().build(pageInfo);
    	resultMap.put("pageInfo", build);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	  

}
