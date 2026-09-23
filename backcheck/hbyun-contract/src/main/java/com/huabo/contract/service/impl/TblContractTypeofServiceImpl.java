package com.huabo.contract.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractTypeof;
import com.huabo.contract.entity.TblFlow;
import com.huabo.contract.mapper.TblContractTypeofMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.mapper.TblFlowMapper;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.service.TblContractTypeofService;
import com.huabo.contract.util.DateUtils;
import com.huabo.contract.util.Tree;

@Service
public class TblContractTypeofServiceImpl implements TblContractTypeofService {
	@Resource
	private TblContractTypeofMapper tblContractTypeofMapper;

	@Resource
	private TblFlowMapper tblFlowMapper;

	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private TblCyhwUnitMapper tblCyhwUnitMapper;
	@Resource
	private TblStaffService tblStaffService;
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	private UserProvider userProvider;
	
    @Override
    public Map<String, Object> findAllListToCreateContract(String token, String staffId) {
		//resultMap用来存放返回数据
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			//用户是否失效判断
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			//typeofList查询结果
			List<TblContractTypeof> typeofList = tblContractTypeofMapper.selectAllList();//staff.getCurrentOrg().getOrgid()
			if(typeofList!=null && typeofList.size()>0) {
				for (TblContractTypeof of : typeofList) {
					//
					List<TblContractTypeof> list = tblContractTypeofMapper.selectListByParent(of.getTypeid());
					of.setChildrenList(list);
				}
			}
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("typeofList", typeofList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
    }

    @Override
    public Map<String, Object> findPageInfoList(Integer pageNumber, Integer pageSize,String typeId, String choiceTypeName, String token, String staffId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			
			TblContractTypeof condition = new TblContractTypeof();
			condition.setOrgid(new BigDecimal(staff.getCurrentOrg().getOrgid().intValue()));
			if (choiceTypeName != null && !"".equals(choiceTypeName)) {
				condition.setTypename(choiceTypeName);
			}
			if (typeId != null) {
				condition.setTypeid(new BigDecimal(typeId));
			}
			
			IPage<TblContractTypeof> page = new Page<TblContractTypeof>(pageNumber,pageSize);
			IPage<TblContractTypeof> pageList = tblContractTypeofMapper.selectLedgerListPageInfo(page,condition);
			pageList.getRecords().forEach(item->{
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
			PageInfo<TblContractTypeof> pageInfo = new PageInfo<TblContractTypeof>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setCondition(condition);
			pageInfo.setPageSize(pageSize);
			pageInfo.setTlist(pageList.getRecords());
			pageInfo.setTotalRecord((int)pageList.getTotal());
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("data", pageInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
    }

    @Override
    public List<Tree> getFatherTree() {//BigDecimal orgid
			List<Tree> trees = new ArrayList<Tree>();
			List<Tree> ctrees = new ArrayList<Tree>();
			Tree tree = null;
			List<TblContractTypeof> list = this.tblContractTypeofMapper.selectAllList();//orgid
			for (TblContractTypeof type : list) {
				tree = new Tree();
				tree.setName(type.getTypename());
				tree.setId(type.getTypeid());
				tree.setpId(new BigDecimal(0));
				tree.setOpen(false);
				tree.setIsParent(false);
				ctrees.add(tree);
			}
			Tree ftree = new Tree();
			ftree.setName("全部");
			ftree.setId(new BigDecimal(0));
			ftree.setpId(null);
			ftree.setOpen(true);
			ftree.setIsParent(true);
			ftree.setChildren(ctrees);
			trees.add(ftree);
			return trees;
    }

    @Override
    public Map<String, Object> saveContractType(String[] typeNameArr, BigDecimal parentid, String token, String staffId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			List<TblContractTypeof> typeList = new ArrayList<TblContractTypeof>(0);
			TblContractTypeof type = null;
			String typeNameStr = "";
			StringBuffer nameSb = new StringBuffer();
			if (parentid == null) {
				for (int i = 0; i < typeNameArr.length; i++) {
					type = new TblContractTypeof();
					type.setCreatestaff(user.getStaffid());
					type.setCreatetime(new Date());
					type.setOrgid(user.getCurrentOrg().getOrgid());
					type.setTypename(typeNameArr[i]);
					typeList.add(type);
					nameSb.append("'" + typeNameArr[i] + "',");
				}
			} else {
				for (int i = 0; i < typeNameArr.length; i++) {
					type = new TblContractTypeof();
					type.setCreatestaff(user.getStaffid());
					type.setCreatetime(new Date());
					type.setOrgid(user.getCurrentOrg().getOrgid());
					type.setTypename(typeNameArr[i]);
					type.setPageurl("default");
					type.setParentid(parentid);
					typeList.add(type);
					nameSb.append("'" + typeNameArr[i] + "',");
				}
			}
		
			nameSb.deleteCharAt(nameSb.length() - 1);
			typeNameStr = nameSb.toString();
			List<TblContractTypeof> retypeList = this.tblContractTypeofMapper.selectRepeatTypeName(user.getCurrentOrg().getOrgid(), typeNameStr);
			if (retypeList != null && retypeList.size() > 0) {
				resultMap.put("code", "0");
				resultMap.put("msg", "合同类型重复！");
				return resultMap;
			}
			for (TblContractTypeof typeof : typeList) {
				typeof.setTypeid(RandomUtil.uuBigDecimalId());
//				this.tblContractTypeofMapper.saveContractTypeof(typeof);
				tblContractTypeofMapper.insert(typeof);
			}
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
    }

    @Override
    public Map<String, Object> modifyContractType(String token, String staffId, String typeId, String typeName) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			Integer count = this.tblContractTypeofMapper.selectContractCountByContractType(user.getCurrentOrg().getOrgid(), typeId);
			if (count > 0) {
				resultMap.put("code", "-2");
				resultMap.put("msg", "已被使用无法删除！");
				return resultMap;
			}
			Integer reCount = this.tblContractTypeofMapper.selectRepeatCount(typeName, user.getCurrentOrg().getOrgid(), typeId);
			if (reCount > 0) {
				resultMap.put("code", "-1");
				resultMap.put("msg", "合同类型重复！");
				return resultMap;
			}
		
			TblContractTypeof type = this.tblContractTypeofMapper.selectEntityById(typeId);
			type.setTypename(typeName);
			type.setUpdatestaff(user.getStaffid());
			type.setUpdatetime(new Date());
//			this.tblContractTypeofMapper.updateContractTypeof(type);
			tblContractTypeofMapper.updateById(type);
			resultMap.put("code", "1");
			resultMap.put("msg", "更新成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
    }

    @Override
    public Map<String, Object> removeContractType(String typeId, String token, String staffId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			Integer count = this.tblContractTypeofMapper.selectContractCountByType(user.getCurrentOrg().getOrgid(), typeId);
		
			if (count > 0) {
				resultMap.put("code", "0");
				resultMap.put("msg", "已被使用无法删除！");
				return resultMap;
			}
			this.tblContractTypeofMapper.deleteContractTypeByParentId(typeId);
			this.tblContractTypeofMapper.deleteContractTypeByTypeId(typeId);
			resultMap.put("code", "1");
			resultMap.put("msg", "删除成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
    }

    @Override
	public Map<String, Object> toAddContract(BigDecimal flowId, BigDecimal typeId, String token, String fatherNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			String contractNo = this.getAutoContractNo(flowId,fatherNo,user);
			
		    dataMap.put("contractno", contractNo);
			dataMap.put("flowid", flowId);
			dataMap.put("createDate", DateUtil.getIntDateStr(new Date(), DateUtil.DATE_SMALL_STR));
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
    
    @Override
    public List<TblContractTypeof> findAllList(BigDecimal orgid, int parentId) {
    	return tblContractTypeofMapper.findAllList(orgid, parentId);
    }
	
	/***
	 * 合同获取自动生成
	 * @param flowId
//	 * @param typeId
	 * @param fatherNo
	 * @param user
	 * @return
	 * @throws Exception
	 */
    @Override
    public String getAutoContractNo(BigDecimal flowId,String fatherNo, TblStaffUtil user) throws Exception {
			String choiceNo = null;
			String writen = this.tblOrganizationMapper.selectWrittenDeptByOrgId(user.getLinkOrg().getOrgid());
			String year = DateUtil.parseDate(new Date(), "yyyy");
			String yearMM = DateUtil.parseDate(new Date(), "yyyyMM");
			String contractno = null;
			Integer noCount = 0;
			if(StringUtils.isNotBlank(fatherNo)) {
	          	Integer count = 0;
	          	count++;
	          	String no = "";
	          	String c = "";
	          	int i = 0;
	          	//判断是不是子合同编号
	          	if(fatherNo.split("-").length > 4) {
	          		//子合同处理编号
	          		fatherNo = fatherNo.substring(0,fatherNo.lastIndexOf("-")+1);
	              	count = this.tblCyhwUnitMapper.selectContractNoCountByNo(fatherNo);
	              	count++;
	              	no = count.toString();
	              	c = "";
	              	i = no.length();
	              	for (  ; i < 2; i++) {
	                   	c += "0";
	           		}
	              	 c += no;
	              	 contractno = fatherNo+c;
	          	}else {
	          		//不是子合同处理编号
	              	count = this.tblCyhwUnitMapper.selectContractNoCountByNo(fatherNo+"-");
	              	count++;
	              	no = count.toString();
	                  i = no.length();
	                  for (  ; i < 2; i++) {
	                  	c += "0";
	          		}
	                  c += no;
	                  contractno = fatherNo+"-"+c;
	          	}
          	
          }else {
          	if(flowId.compareTo(BigDecimal.valueOf(733271)) == 0) {
  				//合同范本
  				contractno = writen+"-HGFB-"+yearMM+"-";
  				choiceNo = writen+"-HGFB-"+year;
  			}else {
  				//合同订立 合同变更
  				contractno = writen+"-HG-"+yearMM+"-";
  				choiceNo = writen+"-HG-"+year;
  			}
  			
          	noCount = this.tblCyhwUnitMapper.selectCountByContractNo(contractno,choiceNo);
  				if(noCount != null) {
  					noCount ++;
  				}else {
  					noCount = 1;
  				}
  		        contractno += noCount;
          }
			return contractno;
	  }
}
