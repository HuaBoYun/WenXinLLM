package com.huabo.monitor.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.entity.TblTestelement;
import com.huabo.monitor.entity.TblTesttemplType;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.OrganizationServiceMapper;
import com.huabo.monitor.mapper.TblTestTemplateMapper;
import com.huabo.monitor.mapper.TblTestelementMapper;
import com.huabo.monitor.mapper.TblTestplanMapper;
import com.huabo.monitor.mapper.TblTesttemplTypeMapper;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblAutonoNumberService;
import com.huabo.monitor.service.TblTestTemplateService;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.PageResult;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

@Service
public class TblTestTemplateServiceImpl implements TblTestTemplateService {

    @Resource
    private TblTestTemplateMapper tblTestTemplateMapper;
    
    @Resource
    private TblTestplanMapper tblTestplanMapper;

    @Resource 
    private TblTesttemplTypeMapper tblTesttemplTypeMapper;
    
    @Resource
    private TblTestelementMapper tblTestelementMapper;
    
    @Resource
    TblAutonoNumberService  tblAtonoNumberService;
    
    @Resource
    private UserProvider userProvider;
    
    @Resource
    OrganizationServiceMapper organizationServiceMapper;
    
    @Autowired
    TblAssessService tblAssessService;
    
    @Override
    public JsonBean add(TblTestTemplate template) {
        if (template == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }
        template.setTesttemid(RandomUtil.uuBigDecimalId());
        tblTestTemplateMapper.insert(template);
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean updateById(BigDecimal testtemid) {
        if (testtemid == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }
        //tblTestTemplateMapper.updateById(testtemid);
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean deleteById(String templeNumber) {
        if (templeNumber == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }
        try {
            tblTestTemplateMapper.deleteByNumber(templeNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean selectList(String token, Integer pageNo, Integer pageSize, String templeNumber, String templename) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
//    
//    	PageInfo<TblTestTemplate> pageInfo = new PageInfo<TblTestTemplate>();
//    	TblTestTemplate temp = new TblTestTemplate();
//    	temp.setTempleNumber(templeNumber);
//    	temp.setTemplename(templename);
//    	temp.setTblComany(loginStaff.getCurrentOrg().getOrgid().toString());
    	//pageInfo.setCondition(temp);
//    	
//    	pageInfo.setPageSize(pageSize);
//    	pageInfo.setCurrentPage(pageNo);
//    	
//    	pageInfo.setTlist(tblTestTemplateMapper.selectPageInfo(pageInfo));
//    	pageInfo.setTotalRecord(tblTestTemplateMapper.selectPageCount(pageInfo));
//    	
//        return ResponseFormat.retParam(1, 200, pageInfo);
        
      //创建分页对象
       /* 
    		QueryWrapper<TblTestTemplate> wrapper = new QueryWrapper<>();
		 
		if (StringUtils.isNotBlank(templeNumber)) {
			wrapper.like("templeNumber", templeNumber);
		}
		
		if(StringUtils.isNotBlank(templename)){
			wrapper.like("templename",templename);
		}
		if(StringUtils.isNotBlank(loginStaff.getCurrentOrg().getOrgid().toString())){
			wrapper.eq("tblComany",loginStaff.getCurrentOrg().getOrgid().toString());
		}
		 wrapper.orderByDesc(true, "testtemid");*/
        TblTestTemplate temp = new TblTestTemplate();
    	temp.setTempleNumber(templeNumber);
    	temp.setTemplename(templename);
    	temp.setTblComany(loginStaff.getCurrentOrg().getOrgid());
    	
    	String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TT.TBLCOMANY", "TT.LINKDEPTID", "TT.STAFFID", "TT.SECRECTLEVELID", "TT.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
    	
		 PageInfo<TblTestTemplate> pageInfo = PageMethod.startPage(pageNo, pageSize)
	     .doSelectPageInfo(() -> tblTestTemplateMapper.findList(temp,sql));
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
						Integer count = this.tblTestplanMapper.findPlanCountByTestTempId(Integer.valueOf(entity.getTesttemid().toString()));
				        entity.setCount(count);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					} );
					
				}
		 PageResult<TblTestTemplate> build = new PageResult<TblTestTemplate>().build(pageInfo);
 	    return ResponseFormat.retParam(1, 200, build);

    }

    @Override
    public JsonBean save(String token, TblTestTemplate tblTestTemplate) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
        tblTestTemplate.setTblComany(loginStaff.getCurrentOrg().getOrgid());
        tblTestTemplate.setStaffId(loginStaff.getStaffid().toString());
        tblTestTemplate.setSource("自建");
        tblTestTemplate.setLinkdeptid(loginStaff.getLinkDetp().getOrgid());
        //tblTestTemplateMapper.insertEntity(tblTestTemplate);
        tblTestTemplateMapper.insert(tblTestTemplate);
        return ResponseFormat.retParam(1, 200, tblTestTemplate.getTesttemid());
    }

	@Override
	public JsonBean modify(String token, TblTestTemplate tblTestTemplate) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
       // tblTestTemplateMapper.updateEntity(tblTestTemplate);
        tblTestTemplateMapper.updateById(tblTestTemplate);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean remove(String token, Integer templId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
		Integer count = this.tblTestplanMapper.findPlanCountByTestTempId(templId);
		TblTestTemplate ele = this.tblTestTemplateMapper.selectEntityById(templId.toString());
         if(ele.getIssued()!=null&&ele.getIssued().equals("是")){
			 return ResponseFormat.retParam(0, "模板已下发不能删除！", null);
         }
		if(count > 0 ){
			 return ResponseFormat.retParam(0, "模板已使用不能删除！", null);
		}
		String sql = "DELETE FROM TBL_TESTELEMENT WHERE templId = "+templId;
		this.tblTestTemplateMapper.executeDelSql(sql);
		sql = "DELETE FROM TBL_TESTTEMPL_TYPE WHERE testTempletaId = "+templId;
		this.tblTestTemplateMapper.executeDelSql(sql);
		sql = "DELETE FROM TBL_TESTTEMPLE WHERE TESTTEMID = "+templId;
		this.tblTestTemplateMapper.executeDelSql(sql);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean saveissued(String token, String tempIds, String orgIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil org = loginStaff.getCurrentOrg();
        
		String[] temps = tempIds.split(",");
		String[] orgs = orgIds.split(",");
		
		TblTestTemplate oldTemp = null;
		TblTestTemplate newTemp = null;
		List<TblTesttemplType> tempTypeList = null;
		TblTesttemplType newType = null;
		List<TblTestelement> eleList = null;
		TblTestelement newEle = null;
		for (String tempId : temps) {
			oldTemp = tblTestTemplateMapper.selectEntityById(tempId);
			oldTemp.setIssued("是"); //记录是否已经下发 
			tblTestTemplateMapper.updateById(oldTemp);
			for (String orgid : orgs) {
				//已经下发的数据不能重复下发，下发模板的当前公司不能下发该模板数据
				if(tblTestTemplateMapper.selectCountForNumber(oldTemp.getTempleNumber(),orgid)==0){
				newTemp = new TblTestTemplate();
				newTemp.setMemo(oldTemp.getMemo());
				newTemp.setSource(org.getOrgname());
				newTemp.setStaffId(loginStaff.getStaffid().toString());
				newTemp.setTblComany(new BigDecimal(orgid));
				newTemp.setTempleDesc(oldTemp.getTempleDesc());
				newTemp.setTemplename(oldTemp.getTemplename());
				newTemp.setTempleNumber(oldTemp.getTempleNumber());
				newTemp.setTesttemid(RandomUtil.uuBigDecimalId());
				newTemp.setCreatetime(new Date());
				newTemp.setSecrectLevelId(oldTemp.getSecrectLevelId());
				this.tblTestTemplateMapper.insert(newTemp);
				tempTypeList = this.tblTesttemplTypeMapper.selectRootListByTempId(tempId);
				for (TblTesttemplType type : tempTypeList) {
					newType = new TblTesttemplType();
					newType.setTypecode(type.getTypecode());
					newType.setTesttempletaid(newTemp.getTesttemid());
					newType.setTypedesc(type.getTypedesc());
					newType.setTypename(type.getTypename());
					newType.setTypeid(RandomUtil.uuBigDecimalId());
					this.tblTesttemplTypeMapper.insert(newType);
					eleList = this.tblTestelementMapper.selectEleListByTypeId(type.getTypeid());
					for (TblTestelement ele : eleList) {
						newEle = new TblTestelement();
						newEle.setBusinessdesc(ele.getBusinessdesc());
						newEle.setCheckmethod(ele.getCheckmethod());
						newEle.setControlmeasures(ele.getControlmeasures());
						newEle.setControlmethod(ele.getControlmethod());
						newEle.setControlreq(ele.getControlreq());
						newEle.setControltarget(ele.getControltarget());
						newEle.setControltype(ele.getControltype());
						newEle.setElementcode(ele.getElementcode());
						newEle.setMaterial(ele.getMaterial());
						newEle.setRisktype(ele.getRisktype());
						newEle.setTemplid(newTemp.getTesttemid());
						newEle.setTypeid(newType.getTypeid());
						newEle.setElementid(RandomUtil.uuBigDecimalId());
						this.tblTestelementMapper.insert(newEle);
					}
					this.copyTemptypeChrildernInfo(type,newTemp.getTesttemid());
				}
				}
			}
		}
		
		 return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean copyIssued(String token, String tempIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil org = loginStaff.getCurrentOrg();
		TblTestTemplate oldTemp = null;
		List<TblTesttemplType> tempTypeList = null;
		TblTesttemplType newType = null;
		List<TblTestelement> eleList = null; 
		TblTestelement newEle = null;
			oldTemp = tblTestTemplateMapper.selectById(tempIds);
		        TblTestTemplate newTemp = new TblTestTemplate();
				newTemp.setMemo(oldTemp.getMemo());
				newTemp.setSource("自建");
				newTemp.setStaffId(loginStaff.getStaffid().toString());
				newTemp.setTblComany(loginStaff.getLinkOrg().getOrgid());
				newTemp.setTempleDesc(oldTemp.getTempleDesc());
				newTemp.setTemplename(oldTemp.getTemplename());
		     	String number=	tblAssessService.findFlowNextIdNk("TBL_TESTTEMPLE", "TEMPLENUMBER", "TBLCOMANY",  loginStaff.getLinkOrg().getOrgid(), 317,
	                    null, null, null); 
				newTemp.setTempleNumber(number);
				newTemp.setTesttemid(RandomUtil.uuBigDecimalId());
				newTemp.setCreatetime(new Date());
				newTemp.setSecrectLevelId(oldTemp.getSecrectLevelId());
				newTemp.setLinkdeptid(loginStaff.getLinkDetp().getOrgid());
				this.tblTestTemplateMapper.insert(newTemp);
				tempTypeList = this.tblTesttemplTypeMapper.selectRootListByTempId(tempIds);
				for (TblTesttemplType type : tempTypeList) {
					newType = new TblTesttemplType();
					String levelNumber=tblAtonoNumberService.findNumberLevelNextId("TBL_TESTTEMPL_TYPE", "TYPECODE", null,
							loginStaff.getLinkOrg().getOrgid(), 317, "TBL_TESTTEMPLE", "TESTTEMID", newTemp.getTesttemid().toString(), "TBL_TESTTEMPLE",
		  					"TESTTEMID", "TBLCOMANY", "TESTTEMPLETAID", "TEMPLENUMBER",token);
					newType.setTypecode(levelNumber);
					newType.setTesttempletaid(newTemp.getTesttemid());
					newType.setTypedesc(type.getTypedesc());
					newType.setTypename(type.getTypename());
					newType.setTypeid(RandomUtil.uuBigDecimalId());
					this.tblTesttemplTypeMapper.insert(newType);
					eleList = this.tblTestelementMapper.selectEleListByTypeId(type.getTypeid());
					for (TblTestelement ele : eleList) {
						newEle = new TblTestelement();
						newEle.setBusinessdesc(ele.getBusinessdesc());
						newEle.setCheckmethod(ele.getCheckmethod());
						newEle.setControlmeasures(ele.getControlmeasures());
						newEle.setControlmethod(ele.getControlmethod());
						newEle.setControlreq(ele.getControlreq());
						newEle.setControltarget(ele.getControltarget());
						newEle.setControltype(ele.getControltype());
					   String eleNumber=tblAtonoNumberService.findRootNumberByParentIdLevel("ELEMENTCODE", "TBL_TESTELEMENT", "TYPEID",
								"TESTTEMID", "TBL_TESTTEMPLE", "TBLCOMANY", null, newType.getTypeid().toString(), loginStaff.getLinkOrg().getOrgid(), 317,
								"TBL_TESTTEMPL_TYPE", "TYPEID", "TESTTEMPLETAID", "TYPEID", "TYPECODE", token);
						newEle.setElementcode(eleNumber);
						newEle.setMaterial(ele.getMaterial());
						newEle.setRisktype(ele.getRisktype());
						newEle.setTemplid(newTemp.getTesttemid());
						newEle.setTypeid(newType.getTypeid());
						newEle.setElementid(RandomUtil.uuBigDecimalId());
						this.tblTestelementMapper.insert(newEle);
					}
				//	this.copyTemptypeChrildernInfo(type,newTemp.getTesttemid());
				}
				//}
//			}
//		}
		
		 return ResponseFormat.retParam(1, 200, null);
	}
	
	/**
	 * 递归插入子级 类型节点
	 * @param typeid 
	 * @param testtemid 
	 * @param type
	 */
	private void copyTemptypeChrildernInfo(TblTesttemplType temptype, BigDecimal testtemid) throws Exception {
		List<TblTesttemplType> tempTypeList = null;
		TblTesttemplType newType = null;
		List<TblTestelement> eleList = null;
		TblTestelement newEle = null;
		
		List<TblTesttemplType> chilTypeList = this.tblTesttemplTypeMapper.selectChildrenListByTempId(temptype.getTypeid());
		for (TblTesttemplType type : chilTypeList) {
			newType = new TblTesttemplType();
			newType.setTypecode(type.getTypecode());
			newType.setTesttempletaid(testtemid);
			newType.setTypedesc(type.getTypedesc());
			newType.setTypename(type.getTypename());
			newType.setTypeid(RandomUtil.uuBigDecimalId());
			this.tblTesttemplTypeMapper.insert(newType);
			eleList = this.tblTestelementMapper.selectEleListByTypeId(type.getTypeid());
			for (TblTestelement ele : eleList) {
				newEle = new TblTestelement();
				newEle.setBusinessdesc(ele.getBusinessdesc());
				newEle.setCheckmethod(ele.getCheckmethod());
				newEle.setControlmeasures(ele.getControlmeasures());
				newEle.setControlmethod(ele.getControlmethod());
				newEle.setControlreq(ele.getControlreq());
				newEle.setControltarget(ele.getControltarget());
				newEle.setControltype(ele.getControltype());
				newEle.setElementcode(ele.getElementcode());
				newEle.setMaterial(ele.getMaterial());
				newEle.setRisktype(ele.getRisktype());
				newEle.setTemplid(testtemid);
				newEle.setTypeid(newType.getTypeid());
				newEle.setElementid(RandomUtil.uuBigDecimalId());
				this.tblTestelementMapper.insert(newEle);
			}
			this.copyTemptypeChrildernInfo(type,testtemid);
		}
		
	}

	@Override
	public JsonBean getInfo(String token, BigDecimal testtemid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
        TblTestTemplate temp = tblTestTemplateMapper.selectEntityById(testtemid.toString());
		if (temp != null) {
			FiexibleNameAssignment ment = new FiexibleNameAssignment();
			// 对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item = new fieldOrgStaffId();
			BeanUtils.copyProperties(temp, item);
			fieldOrgStaffName nameEntity = ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity, temp);
		}
        return ResponseFormat.retParam(1, 200, temp);
	}
	
	@Override
	public JsonBean getDetail(BigDecimal  testtemid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
        TblTestTemplate temp = tblTestTemplateMapper.selectEntityById(testtemid.toString());
		if (temp != null) {
			FiexibleNameAssignment ment = new FiexibleNameAssignment();
			// 对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item = new fieldOrgStaffId();
			BeanUtils.copyProperties(temp, item);
			fieldOrgStaffName nameEntity = ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity, temp);
		}
		  List<TblTesttemplType> typeList = this.tblTesttemplTypeMapper.selectTreeListInfo(null,testtemid);
	      Map<String, Object> resultMap = new HashMap<String, Object>(0);
	      resultMap.put("template", temp);
	      resultMap.put("typeList", typeList);
        return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@Override
	public JsonBean csmb_editcheck(Integer testtemid) throws Exception {
        Integer temp = tblTestTemplateMapper.csmb_editcheck(testtemid);
       
        return ResponseFormat.retParam(1, 200, temp);
	}

}
