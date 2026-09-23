package com.huabo.compliance.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.TblTestTemplate;
import com.huabo.compliance.entity.TblTestelement;
import com.huabo.compliance.entity.TblTesttemplType;
import com.huabo.compliance.mapper.OrganizationServiceMapper;
import com.huabo.compliance.mapper.TblTestTemplateMapper;
import com.huabo.compliance.mapper.TblTestelementMapper;
import com.huabo.compliance.mapper.TblTestplanMapper;
import com.huabo.compliance.mapper.TblTesttemplTypeMapper;
import com.huabo.compliance.service.TblTestTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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
    OrganizationServiceMapper organizationServiceMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public JsonBean add(TblTestTemplate template) {
        if (template == null) {
            return ResponseFormat.retParam(0, 10002, null);
        }
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
    public JsonBean selectList(String token, Integer pageNumber, Integer pageSize, String templeNumber, String templename) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
    	PageInfo<TblTestTemplate> pageInfo = new PageInfo<TblTestTemplate>();
    	TblTestTemplate temp = new TblTestTemplate();
    	temp.setTempleNumber(templeNumber);
    	temp.setTemplename(templename);
    	temp.setTblComany(loginStaff.getCurrentOrg().getOrgid().toString());
    	pageInfo.setCondition(temp);
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	pageInfo.setTlist(tblTestTemplateMapper.selectPageInfo(pageInfo));
    	pageInfo.setTotalRecord(tblTestTemplateMapper.selectPageCount(pageInfo));
    	
        return ResponseFormat.retParam(1, 200, pageInfo);
    }

    @Override
    public JsonBean save(String token, TblTestTemplate tblTestTemplate) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
        tblTestTemplate.setTblComany(loginStaff.getCurrentOrg().getOrgid().toString());
        tblTestTemplate.setStaffId(loginStaff.getStaffid().toString());
        tblTestTemplate.setSource("自建");
        
        tblTestTemplateMapper.insertEntity(tblTestTemplate);
        
        return ResponseFormat.retParam(1, 200, tblTestTemplate.getTesttemid());
    }

	@Override
	public JsonBean modify(String token, TblTestTemplate tblTestTemplate) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
        tblTestTemplateMapper.updateEntity(tblTestTemplate);
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
		String sql = "DELETE FROM TBL_COM_EXT_TESTELEMENT WHERE templId = "+templId;
		this.tblTestTemplateMapper.executeDelSql(sql);
		sql = "DELETE FROM TBL_COM_EXT_TESTTEMPL_TYPE WHERE testTempletaId = "+templId;
		this.tblTestTemplateMapper.executeDelSql(sql);
		sql = "DELETE FROM TBL_COM_EXT_TESTTEMPLE WHERE TESTTEMID = "+templId;
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
			tblTestTemplateMapper.updateEntity(oldTemp);
			for (String orgid : orgs) {
				//已经下发的数据不能重复下发，下发模板的当前公司不能下发该模板数据
				if(tblTestTemplateMapper.selectCountForNumber(oldTemp.getTempleNumber(),orgid)==0){
				newTemp = new TblTestTemplate();
				newTemp.setMemo(oldTemp.getMemo());
				newTemp.setSource(org.getOrgname());
				newTemp.setStaffId(loginStaff.getStaffid().toString());
				newTemp.setTblComany(orgid);
				newTemp.setTempleDesc(oldTemp.getTempleDesc());
				newTemp.setTemplename(oldTemp.getTemplename());
				newTemp.setTempleNumber(oldTemp.getTempleNumber());
				this.tblTestTemplateMapper.insertEntity(newTemp);
				tempTypeList = this.tblTesttemplTypeMapper.selectRootListByTempId(tempId);
				for (TblTesttemplType type : tempTypeList) {
					newType = new TblTesttemplType();
					newType.setTypecode(type.getTypecode());
					newType.setTesttempletaid(newTemp.getTesttemid());
					newType.setTypedesc(type.getTypedesc());
					newType.setTypename(type.getTypename());
					this.tblTesttemplTypeMapper.insertEntity(newType);
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
						this.tblTestelementMapper.insertEntity(newEle);
					}
					this.copyTemptypeChrildernInfo(type,newTemp.getTesttemid());
				}
				}
			}
		}
		
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
			this.tblTesttemplTypeMapper.insertEntity(newType);
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
				this.tblTestelementMapper.insertEntity(newEle);
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
       
        return ResponseFormat.retParam(1, 200, temp);
	}

}
