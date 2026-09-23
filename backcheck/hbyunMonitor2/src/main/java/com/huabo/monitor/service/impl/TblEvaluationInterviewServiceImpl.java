package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblAssessPlan;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblEvaluationInterview;
import com.huabo.monitor.entity.TblEvaluationInterview;
import com.huabo.monitor.mapper.TblAssessMapperSqlConfig;
import com.huabo.monitor.mapper.TblAssessTeammemberMapper;
import com.huabo.monitor.mapper.TblAssesslevelMapper;
import com.huabo.monitor.mapper.TblEvaluationInterviewMapper;
import com.huabo.monitor.mapper.TblEvaluationInterviewMapper;
import com.huabo.monitor.service.ITblAssesslevelService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblEvaluationInterviewService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  
 * @since  
 */
@Service
public class TblEvaluationInterviewServiceImpl extends ServiceImpl<TblEvaluationInterviewMapper, TblEvaluationInterview> implements TblEvaluationInterviewService {

	@Resource 
	TblEvaluationInterviewMapper tblEvaluationInterviewMapper;
	
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    @Resource
	private ITblStaffService staffService;
    
    @Resource
    private UserProvider userProvider;
	
	@Override
	public PageInfo<TblEvaluationInterview> getHomepage_List(Integer pageNumber,
			Integer pageSize, Integer authorityType, TblEvaluationInterview def,TblStaffUtil staff) throws Exception {
		// TODO Auto-generated method stub
		PageInfo<TblEvaluationInterview>  pageInfo=null;
		try {
			String sql = GeneralSQLConcatConfig.concatSecrectSql(staff.getCurrentOrg().getUseSecrect(), authorityType==0, "e.unit", "e.linkDeptId","e.createstaffid", "e.SECRECTLEVELID", "e.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds());
	        pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblEvaluationInterviewMapper.getHomepage_List(def,authorityType,sql));
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
	public Map<String, Object> saveOrUpdate(String token, TblEvaluationInterview entity, String attids) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		try {
			TblStaffUtil staff = userProvider.get();
			if(entity.getId()!=null&&entity.getId().compareTo(new BigDecimal("0"))!=0){
				tblEvaluationInterviewMapper.updateById(entity);
				if (StringUtils.isNotBlank(attids)) {
                    String[] ids = attids.split(",");
                    for (int i = 0; i < ids.length; i++) {
                    	tblEvaluationInterviewMapper.insertAtt(ids[i], entity.getId());
                    }
                 }
			}else{
				entity.setCreatestaffid(staff.getStaffid());
				entity.setUnit(staff.getLinkOrg().getOrgid());
				entity.setCreatetime(new Date());
				entity.setLinkDeptId(staff.getLinkDetp().getOrgid());
				tblEvaluationInterviewMapper.insert(entity);
				
				if (StringUtils.isNotBlank(attids)) {
                    String[] ids = attids.split(",");
                    for (int i = 0; i < ids.length; i++) {
                    	tblEvaluationInterviewMapper.insertAtt(ids[i], entity.getId());
                    }
                 }
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TblEvaluationInterview getDetatilsById(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		TblEvaluationInterview entity=null;
		try {
			entity=tblEvaluationInterviewMapper.getDetatilsById(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String deleteById(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		try {
			tblEvaluationInterviewMapper.deleteById(id);
			
		      this.tblEvaluationInterviewMapper.removeAttById(id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JsonBean.error();
		}
		return JsonBean.success();
	}

	@Override
	public List<TblEvaluationInterview> exportPjft(Integer authorityType, TblEvaluationInterview entity)
			throws Exception {
		List<TblEvaluationInterview> list=null;
		try {
			list=tblEvaluationInterviewMapper.getHomepage_List(entity,authorityType,"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TblAttachment> getAttList(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		List<TblAttachment> attlist=null;
		try {
			attlist=tblEvaluationInterviewMapper.getAttList(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return attlist;
	}

	@Override
	public void delAssessAtt(BigDecimal attid) {
		tblEvaluationInterviewMapper.removeAttByAttid(attid);
		
	}

	 

}
