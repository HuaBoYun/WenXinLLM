package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblOrganization;
import com.huabo.fxgl.mapper.RiskAssplanRiskMapper;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import cn.hutool.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-11
 */
@Service
public class RiskAssplanRiskServiceImpl extends ServiceImpl<RiskAssplanRiskMapper, RiskAssplanRisk> implements IRiskAssplanRiskService {

    @Autowired
    private RiskAssplanRiskMapper riskAssplanRiskMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public List<RiskAssplanRisk> findRiskByRiskType(String typeId, BigDecimal planId) {
        return riskAssplanRiskMapper.findRiskByRiskType(typeId, planId);
    }

    @Override
	public List<RiskAssplanRisk> findRiskByPlanId(BigDecimal planId) {
    	 return riskAssplanRiskMapper.findRiskByPlanId(planId);
	}
    
    @Override
    public IPage<RiskAssplanRisk> findRiskInRiskIdAndAssId(String ids, BigDecimal planId, IPage pageBean) {
        return riskAssplanRiskMapper.findRiskInRiskIdAndAssId(ids, planId, pageBean);
    }

    @Override
    public List<RiskAssplanRisk> findRiskByRiskid(BigDecimal id) {
        return baseMapper.findRiskByRiskid(id);
    }

    @Override
    public IPage<RiskAssplanRisk> findRiskByAssplanidAndStaffid(BigDecimal planId, BigDecimal staffId, IPage pageBean) {
    	IPage<RiskAssplanRisk> pageinfo=baseMapper.findRiskandRiskAssPlan(planId, pageBean, staffId);
    	try {
    		FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageinfo.getRecords())){
				pageinfo.getRecords().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity.getRisk(),item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity.getRisk() ); 
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
        return pageinfo;
    }

    @Override
    public List<RiskAssplanRisk> getByPlanid(BigDecimal planid) {
        return this.riskAssplanRiskMapper.get1(planid);
    }

    @Override
    public List<RiskAssplanRisk> get(BigDecimal planid, BigDecimal riskid) {
        return this.riskAssplanRiskMapper.get2(planid, riskid);
    }

    @Override
    public void delete(RiskAssplanRisk assPlanRisk) {
        this.deleteRiskAssPlanRisk(assPlanRisk);
    }

    public void deleteRiskAssPlanRisk(RiskAssplanRisk riskeventMatrix) {
        baseMapper.deleteById(riskeventMatrix.getAssriskid());  /*  */
    }

    @Override
    public List<RiskAssplanRisk> findRiskByRisk(BigDecimal id) {
        return this.riskAssplanRiskMapper.findRiskByRisk(id);
    }

	@Override
	public JsonBean findRiskInRiskIdAndAssId(String token, Integer pageNumber, Integer pageSize,
			String riskIds, BigDecimal planId) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        
        PageInfo<RiskAssplanRisk> pageInfo = new PageInfo<RiskAssplanRisk>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        RiskAssplanRisk ass = new RiskAssplanRisk();
        ass.setAssplanid(planId);
        ass.setRiskIds(riskIds.substring(0, riskIds.length()-1));
        pageInfo.setCondition(ass);
        
		pageInfo.setTlist(this.riskAssplanRiskMapper.selectRiskInRiskIdAndAssId(pageInfo));
		pageInfo.setTotalRecord(this.riskAssplanRiskMapper.selectRiskInRiskIdCount(pageInfo));
		
		resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public IPage findRiskByRiskAndLevel(BigDecimal planId, BigDecimal staffid, IPage pageBean, String level)
			throws Exception {
		return baseMapper.findRiskByRiskAndLevel(planId, pageBean, staffid,level);
	}

	@Override
	public IPage fingRiskByAssIdAndRiskId(BigDecimal assrisks, BigDecimal riskid, IPage page) {
		return baseMapper.fingRiskByAssIdAndRiskId(assrisks, riskid, page);
	}

	@Override
	public JsonBean findRiskAssplanRiskByRiskId(String token, BigDecimal riskid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<RiskAssplanRisk> assPlanList = this.riskAssplanRiskMapper.selectRiskAssplanRiskByRiskIdList(riskid);
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(assPlanList)){
			assPlanList.forEach(entity->{
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
		Map<String, Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("riskid", riskid);
		resultMap.put("assPlanList", assPlanList);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean findRiskAssplanRiskCountByRiskId(String token, BigDecimal riskid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		return ResponseFormat.retParam(1, 200, this.riskAssplanRiskMapper.selectRiskAssplanRiskByRiskIdCount(riskid));
	}


	@Override
	public Map<String, Object> getRiskCatnameAnalysis(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
			final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
			List<Map<String, Object>> list=riskAssplanRiskMapper.getRiskCatnameAnalysis(company);
			String[] nameList=new String[list.size()];
			Long[] valueList=new Long[list.size()];
			for(int i=0;i<list.size();i++){
				Map<String, Object> o=list.get(i);
				nameList[i]=(String) o.get("NAME");
				valueList[i]=(Long) o.get("VALUE");
			}
			result.put("nameList", nameList);
			result.put("valueList", valueList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	


	@Override
	public Map<String, Object> getRiskAnalysis(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
			final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
 			List<Map<String, Object>> list=riskAssplanRiskMapper.getRiskAnalysis(company);
 			String[] nameList=new String[list.size()];
			Long[] valueList=new Long[list.size()];
			for(int i=0;i<list.size();i++){
				Map<String, Object> o=list.get(i);
				nameList[i]=(String) o.get("NAME");
				valueList[i]=(Long) o.get("VALUE");
			}
			result.put("nameList", nameList);
			result.put("valueList", valueList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	

	@Override
	public Map<String, Object> getRiskAreasAnalysis(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
			final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
			Map<String, String> type=new HashMap<String, String>();
			type.put("1", "战略风险");
			type.put("2", "财务风险");
			type.put("3", "市场风险");
			type.put("4", "运营风险");
			type.put("5", "法律风险");
 			List<Map<String, Object>> list=riskAssplanRiskMapper.getRiskAreasAnalysis(company);
 			String[] nameList=new String[list.size()];
			Long[] valueList=new Long[list.size()];
			for(int i=0;i<list.size();i++){
				Map<String, Object> o=list.get(i);
				String name=(String) o.get("NAME");
				StringBuffer b=new StringBuffer();
				for(String s:name.split(",")){
					if(type.get(s)==null){
					 b.append(s+",");
					}else{
					b.append(type.get(s)+",");
					}
				}
				nameList[i]=b.toString().substring(0,b.toString().length()-1);
				valueList[i]=(Long) o.get("VALUE");
			}
			result.put("nameList", nameList);
			result.put("valueList", valueList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> getRiskByDepartmentAnalysis(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
			final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
			//根据公司查询已评估的风险数据中责任部门
			
			List<TblOrganization> orgList=riskAssplanRiskMapper.getRiskDepartment(company);
			if(orgList!=null){
			List<JSONObject> obj=new ArrayList<JSONObject>();
		    Map<String, int[]> map=new HashMap<>();
		    int[] c1=new int[orgList.size()];
		    int[] c2=new int[orgList.size()];
		    int[] c3=new int[orgList.size()];
		    int[] c4=new int[orgList.size()];
		    int[] c5=new int[orgList.size()];
		    String[] orgName=new String[orgList.size()];
            for(int i=0;i<orgList.size();i++){
            	TblOrganization o=orgList.get(i);
            	int count1=riskAssplanRiskMapper.getLevelCount(o.getOrgid(),1);
            	int count2=riskAssplanRiskMapper.getLevelCount(o.getOrgid(),2);
            	int count3=riskAssplanRiskMapper.getLevelCount(o.getOrgid(),3);
            	int count4=riskAssplanRiskMapper.getLevelCount(o.getOrgid(),4);
            	int count5=riskAssplanRiskMapper.getLevelCount(o.getOrgid(),5);
            	c1[i]=count1;
            	c2[i]=count2;
            	c3[i]=count3;
            	c4[i]=count4;
            	c5[i]=count5;
            	orgName[i]=o.getOrgname();
            }
            map.put("很高", c5);
            map.put("较高", c4);
            map.put("中等", c3);
            map.put("较低", c2);
            map.put("很低", c1);
            result.put("yAxis", map);
            result.put("xAxis", orgName);
			}
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> getRiskByCompanyAnalysis(String token,String company) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> result=new HashMap<String,Object>(); 
		final TblStaffUtil tblStaffUtil = userProvider.get();
	try {
		List<TblOrganization> orgList=riskAssplanRiskMapper.getRiskCompany();
		if(orgList!=null){
		List<JSONObject> obj=new ArrayList<JSONObject>();
	    Map<String, int[]> map=new HashMap<>();
	    int[] c1=new int[orgList.size()];
	    int[] c2=new int[orgList.size()];
	    int[] c3=new int[orgList.size()];
	    int[] c4=new int[orgList.size()];
	    int[] c5=new int[orgList.size()];
	    String[] orgName=new String[orgList.size()];
        for(int i=0;i<orgList.size();i++){
        	TblOrganization o=orgList.get(i);
        	int count1=riskAssplanRiskMapper.getLevelCountCompany(o.getOrgid(),1);
        	int count2=riskAssplanRiskMapper.getLevelCountCompany(o.getOrgid(),2);
        	int count3=riskAssplanRiskMapper.getLevelCountCompany(o.getOrgid(),3);
        	int count4=riskAssplanRiskMapper.getLevelCountCompany(o.getOrgid(),4);
        	int count5=riskAssplanRiskMapper.getLevelCountCompany(o.getOrgid(),5);
        	c1[i]=count1;
        	c2[i]=count2;
        	c3[i]=count3;
        	c4[i]=count4;
        	c5[i]=count5;
        	orgName[i]=o.getOrgname();
        }
        map.put("很高", c5);
        map.put("较高", c4);
        map.put("中等", c3);
        map.put("较低", c2);
        map.put("很低", c1);
        result.put("yAxis", map);
        result.put("xAxis", orgName);
		}
		 
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	return result;
	}

	@Override
	public Map<String, Object> getRiskCompanyList(String token) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
		try {
			final TblStaffUtil tblStaffUtil = userProvider.get();
			List<TblOrganization> orgList=riskAssplanRiskMapper.getRiskCompanyList();
			result.put("data", orgList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IPage findRiskResultById(BigDecimal riskid, BigDecimal staffid, IPage page) throws Exception {
		// TODO Auto-generated method stub
		return baseMapper.findRiskResultById(riskid, page, staffid);

	}
}
