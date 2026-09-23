package com.huabo.contract.service.impl;


import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollectionUtil;
import com.huabo.contract.mapper.*;
import com.huabo.contract.service.TblStaffService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BaseDaoSqlServer;
import com.hbfk.util.DateUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblContractAppendixsigning;
import com.huabo.contract.entity.TblContractBudget;
import com.huabo.contract.entity.TblContractCollection;
import com.huabo.contract.entity.TblContractContentPdf;
import com.huabo.contract.entity.TblContractInformation;
import com.huabo.contract.entity.TblContractLend;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblContractSpnode;
import com.huabo.contract.entity.TblContractTran;
import com.huabo.contract.entity.TblContractTypeof;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblOaDocumentList;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.vo.ContractTypeVO;
import com.huabo.contract.service.TblContractContentPdfService;
import com.huabo.contract.service.TblCyhwUnitService;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.util.DateUtils;
import com.huabo.contract.util.HttpClient;

import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.SqlConnRunner;
import net.sf.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TblCyhwUnitServiceImpl implements TblCyhwUnitService {

	@Autowired
	private UserProvider userProvider;
	
    @Resource
    private TblCyhwUnitMapper tblCyhwUnitMapper;

    @Resource
	private TblContractTranMapper tblContractTranMapper;
    
    @Resource
    private TblContractTypeofServiceImpl tblContractTypeofServiceImpl;
    
    @Resource
    private TblContractTypeofMapper tblContractTypeofMapper;
    
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    @Resource
    private TblStaffMapper tblStaffMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private TblContractLendMapper tblContractLendMapper;
    
    @Resource
    private TblContractInformationMapper tblContractInformationMapper;
    
    @Resource
    private TblContractPlannodeMapper tblContractPlannodeMapper;
    
    @Resource
    private TblCyhwProjectbudgetMapper tblCyhwProjectbudgetMapper;
    
    @Resource
    private TblContractPlannodeMapper contractPlannodeMapper;
    
    @Resource
    private TblContractContentPdfService tblContractContentPdfService;
    
    @Resource
    private TblContractAppendixsigningMapper tblContractAppendixsigningMapper;
    
    @Resource
    private TblContractCollectionMapper tblContractCollectionMapper;
    
    @Resource
    private TblContractPaymentMapper tblContractPaymentMapper;
    
    @Resource
    private TblCyhwUnitService tblCyhwUnitService;
    
    @Resource
    private TblOaDocumentListMapper tblOaDocumentListMapper;
    
    @Resource
    private TblContractAppendixsigningMapper appendixsigningMapper;
    
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
	@Resource
	private TblStaffService tblStaffService;

	@Override
	public Map<String, Object> findLedgerListPageInfo(String token, String staffId, String startdate, String enddate,
			Integer pageNumber, Integer pageSize, TblCyhwUnit unit) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            //新建一个map，用于存放返回的参数
            Map<String, Object> dataMap = new HashMap<String, Object>(0);
            //解析token并验证用户是否有效，如果用户失效则将相应的提示信息放入resultMap并返回。
            TblStaffUtil staff = userProvider.get();
            //根据token判断用户是否生效或者失效
            if (staff == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            //获取合同类型列表，调用tblContractTypeofMapper.findAllList()方法查询所有合同类型。
            List<TblContractTypeof> typeList = this.tblContractTypeofMapper.findAllList(staff.getCurrentOrg().getOrgid(), -1);
            //判断是否是审计部下的人员
            Integer resultCount = tblCyhwUnitMapper.findCountByUserSjb(staff.getStaffid());
            if (resultCount == 0 && !JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames()) && !JudgeRoleRight.judgeRoleRight("法务人员", staff.getRoleNames())) {
                unit.setCreateuser(staff.getStaffid());
                unit.setContractstaff(staff.getStaffid());
            }

            if (JudgeRoleRight.judgeRoleRight("法务人员", staff.getRoleNames())) {
                unit.setFatherOrgId(staff.getCurrentOrg().getOrgid());
            }
            //方法解析起始日期(startdate)和结束日期(enddate)，将其转换为Date对象，并将其设置到查询条件(unit)中。
            Date startdateNew = null;
            Date enddate1 = null;
            if (enddate != null) {
                enddate1 = DateUtil.formatDate(enddate, "yyyy-MM-dd");
            }
            if (startdate != null) {
                startdateNew = DateUtil.formatDate(startdate, "yyyy-MM-dd");
            }
            //日期信息添加到unit中
            unit.setEnddate(enddate1);
            unit.setStartdate(startdateNew);
            unit.setOrgid(staff.getCurrentOrg().getOrgid());
            unit.setStaffid(staff.getStaffid());
            
            String fatherOrgIds = null;
            if(unit.getFatherOrgId() != null) {
            	fatherOrgIds = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(staff.getCurrentOrg().getOrgid().toString());
            }
            
            //设置查询的条数以及页数
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber, pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.selectLedgerListPageInfo(page,unit,fatherOrgIds);
            
            List<TblCyhwUnit> unitList = pageList.getRecords();
            
            if(unitList != null && unitList.size() > 0) {
            	List<String> idList = unitList.stream().map(TblCyhwUnit::getContractid).map(String::valueOf).collect(Collectors.toList());
            	String idStrs = String.join(",", idList);
            	List<TblCyhwProjectbudget> budgetList = this.tblCyhwProjectbudgetMapper.selectOppsiteNamesByUnitTaiZhang(idStrs);
            	Map<String,String> budMap = new HashMap<String,String>(0);
            	this.generalBudgetMapByContract(budgetList,budMap);
            	
            	for (TblCyhwUnit contract : unitList) {
					if(budMap.containsKey(contract.getContractid().toString())) {
						contract.setBudgetname(budMap.get(contract.getContractid().toString()));
					}
				}
            }
          
            //讲查出的数据存入到dataMap中
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(unit);
            pageInfo.setTlist(unitList);
            pageInfo.setTotalRecord((int)pageList.getTotal());
            
            dataMap.put("typeList", typeList);
            dataMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultMap;
		
	}

	@Override
	public Map<String, Object> findLedgerListAllOrgPageInfo(String token, String staffId, String startdate,
			String enddate, Integer pageNumber, Integer pageSize, TblCyhwUnit unit) throws Exception {
		
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
	        Map<String, Object> dataMap = new HashMap<String, Object>(0);
	        //根据token获取用户信息
	        TblStaffUtil staff = userProvider.get();
	        BigDecimal orgid = null;
	        if (staff == null) {
	            resultMap.put("code", "0");
	            resultMap.put("msg", "用户已失效！");
	            return resultMap;
	        }
            try {
                //如果组织id不为空就直接使用，如果为空通过token获取的用户信息 获取用户当前所在的公司，然后在获取组织id
                if (unit.getOrgid() != null) {
                    orgid = unit.getOrgid();
                } else {
                    orgid = staff.getCurrentOrg().getOrgid();
                }
                //讲查询出的数据添加到typeList
                List<TblContractTypeof> typeList = this.tblContractTypeofMapper.findAllList(orgid, -1);
                Date enddate1 = null;
                Date startdateNew = null;
                
                if (enddate != null) {
                    enddate1 = DateUtil.formatDate(enddate, "yyyy-MM-dd");
                }
                if (startdate != null) {
                    startdateNew = DateUtil.formatDate(startdate, "yyyy-MM-dd");
                }
                //时间信息添加到unit
                unit.setEnddate(enddate1);
                unit.setStartdate(startdateNew);
                unit.setOrgid(orgid);
                
                String allCompanyIds = null;
                if(StringUtils.isBlank(unit.getOrgname())) {
                	allCompanyIds = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(orgid.toString());
                }
                
              //设置查询的条数以及页数
                IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber, pageSize);
                IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.selectLedgerOrgListPageInfo(page,unit,allCompanyIds);
                
                PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
                if (pageNumber == null) {
                    pageNumber = 1;
                }
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setCondition(unit);
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                dataMap.put("typeList", typeList);
                dataMap.put("pageInfo", pageInfo);
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
	}

	@Override
	public Integer findCountByUserSjb(BigDecimal staffid) throws Exception {
		return tblCyhwUnitMapper.findCountByUserSjb(staffid);
	}
	
	@Override
    public List<TblCyhwUnit> findLedgerListForExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception {
        return tblCyhwUnitMapper.findLedgerListForExport(unit,fatherOrgIds);
    }
	
	@Override
    public Map<String, Object> findListByXdf(Integer pageSize, Integer pageNumber, String budgetid) throws Exception {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            // 设置分页查询结果
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber, pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findListByXdf(page,budgetid);
            
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            
            pageInfo.setCurrentPage(pageNumber);
    		pageInfo.setPageSize(pageSize);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int) pageList.getTotal());
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
    }

	@Override
	public Map<String, Object> findCollectionChoiceContract(TblCyhwUnit unit, Integer pageNumber, Integer pageSize,
			String contractno, String contractname) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		
		IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber, pageSize);
        IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findCollectionChoiceContractPid(page, pid, contractno, contractname);
		
		// 创建一个PageInfo对象，用于存储分页信息和查询结果
		PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
		// 调用服务类方法，根据条件查询合同列表
		resultMap.put("date", pageInfo);
		return resultMap;
	}

	@Override
	public void findPaymentChoiceContract(PageInfo<TblCyhwUnit> pageInfo, TblCyhwUnit unit) throws Exception {
		IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.selectListByPageInfo(page, unit);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public Map<String, Object> getReportContractData(Integer year, Integer month) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("codes", "0");
                resultMap.put("msg", "获取用户失败！");
                return resultMap;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (year == null) {
                year = calendar.get(Calendar.YEAR);
            }
            
            String startDate = null;
            String endDate = null;
            
            if(month == null) {
            	startDate = year+"-01-01";
            	endDate = year+"-12-31";
            }else {
            	startDate = year+"-"+month+"-01";
                LocalDate date = LocalDate.of(year, month, 1); // 创建指定年月的日期对象
                int lastDay = date.lengthOfMonth(); // 获取最后一天的日期
                endDate = year+"-"+month+"-"+lastDay;
            }
            
            
            // 本年签订合同金额
            BigDecimal qdje = tblCyhwUnitMapper.getYearMoney(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 本年签订合同数量
            BigDecimal htsl = tblCyhwUnitMapper.getContractCount(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 违约的合同数量
            BigDecimal wysl = tblCyhwUnitMapper.getBreachCount(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 合作方数量
            BigDecimal hzfsl = tblCyhwUnitMapper.getCounterpartCount(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 本年应收款金额
            BigDecimal ysje = tblCyhwUnitMapper.getReceivables(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 本年实际收款金额
            BigDecimal sjskje = tblCyhwUnitMapper.getActualCollection(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 本年应付款金额
            BigDecimal yfje = tblCyhwUnitMapper.getPayable(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            // 本年实际付款金额
            BigDecimal sjfkje = tblCyhwUnitMapper.getActualPayment(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            //各种类的合同情况  如果合同类型小于8 则全部显示 如果合同类型>8 则取合同情况前六的显示
            List<ContractTypeVO> contracttype = tblCyhwUnitMapper.getContractType(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            //历月合同签订数量
            List<JSONObject> lyhtqdsl = tblCyhwUnitMapper.getContractMonthYear(user.getLinkOrg().getOrgid().toString(), year+"-01-01",year+"-12-31");
            
            String[] yue = {"jan", "feb", "march", "april", "may", "june", "july", "aug", "sept", "oct", "nov", "dec"};
            JSONArray lyhtqdslArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", i+1);
                if (lyhtqdsl!=null && lyhtqdsl.size() > 0) {
                    for (Object ob : lyhtqdsl) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", 0);
                    }
                } else {
                    obj.put("value", 0);
                }

                lyhtqdslArr.add(obj);
            }
            
            //部门合同签订数量
            List<Integer> orgcount = tblCyhwUnitMapper.getOrgCount(user.getLinkOrg().getOrgid().toString(), startDate, endDate);
            //当前公司下部门列表
            List<String> getOrgs = tblCyhwUnitMapper.getOrgs(user.getLinkOrg().getOrgid().toString());
            //下属企业合同签订数量
            //List  companyCount=tblCyhwUnitMapper.getCompanyCount(user.getCurrentOrg().getOrgid().toString(), year, month);
            //下属企业合同付款金额
            //List  companyAmount=tblCyhwUnitMapper.getCompanyAmount(user.getCurrentOrg().getOrgid().toString(), year, month);
            //下属企业列表
            List<String> getCompany = tblCyhwUnitMapper.getCompany(user.getLinkOrg().getOrgid().toString());

            String companyIds = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(user.getLinkOrg().getOrgid().toString());
            
            List<TblOrganization> CompanyOrgid = tblCyhwUnitMapper.getCompanyOrgid(user.getLinkOrg().getOrgid().toString(),companyIds);
            
            JSONArray companyCount = new JSONArray();
            for (TblOrganization o : CompanyOrgid) {
                Integer num = tblCyhwUnitMapper.getCompanyCount2(o.getOrgid().toString(), startDate, endDate);
                JSONObject obj = new JSONObject();
                //obj.put("name", o.getOrgname());
                obj.put("name", o.getOrgmeno());
                obj.put("value", num);
                companyCount.add(obj);
            }

            JSONArray companyAmount = new JSONArray();
            for (TblOrganization o : CompanyOrgid) {
                BigDecimal num = tblCyhwUnitMapper.getCompanyAmount2(o.getOrgid().toString(), startDate, endDate);
                JSONObject obj = new JSONObject();
                //obj.put("name", o.getOrgname());
                obj.put("name", o.getOrgmeno());
                obj.put("value", num == null ? 0 : num);
                companyAmount.add(obj);
            }
            resultMap.put("getCompany", getCompany);//下属企业列表
            resultMap.put("companyAmount", companyAmount);//下属企业合同付款金额
            resultMap.put("companyCount", companyCount);//下属企业合同签订数量
            resultMap.put("getOrgs", getOrgs);//当前公司下部门列表
            resultMap.put("orgcount", orgcount);//部门合同签订数量
            resultMap.put("lyhtqdsl", lyhtqdslArr);//历月合同签订数量
            resultMap.put("contracttype", contracttype);
            resultMap.put("qdje", qdje==null?0:qdje);// 本年签订合同金额
            resultMap.put("htsl", htsl);// 本年签订合同数量
            resultMap.put("wysl", wysl);// 违约的合同数量
            resultMap.put("hzfsl", hzfsl);// 合作方数量
            resultMap.put("ysje", ysje==null?0:ysje);// 本年应收款金额
            resultMap.put("sjskje", sjskje==null?0:sjskje);// 本年实际收款金额
            resultMap.put("yfje", yfje==null?0:yfje);// 本年应付款金额
            resultMap.put("sjfkje", sjfkje==null?0:sjfkje);// 本年实际付款金额
            return resultMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public TblCyhwUnit findContractById(BigDecimal contractid) throws Exception {
		 return tblCyhwUnitMapper.findContractById(contractid);
	}

	@Override
    public void modifyJiuFenContractStatus(BigDecimal historyStatus, Integer currentStatus, BigDecimal contractId)
            throws Exception {
		this.tblCyhwUnitMapper.updateJiuFenContractStatus(historyStatus, currentStatus, contractId);
    }
	
	@Override
    public void modifyContractStatus(BigDecimal historyStatus, Integer currentStatus, BigDecimal contractId) throws Exception {
		this.tblCyhwUnitMapper.updateModifyContractStatus(historyStatus, currentStatus, contractId);
    }

	@Override
	public void saveCyhwUnitTcu(TblCyhwUnit tcu) throws Exception {
		tcu.setContractid(RandomUtil.uuBigDecimalId());
		this.tblCyhwUnitMapper.insert(tcu);
	}

	@Override
	public void findLegalContractListByPageInfo(PageInfo<TblCyhwUnit> pageInfo, TblCyhwUnit tcu) throws Exception {
		IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findLegalContractListByPageInfo(page,tcu);
		
		 //findLegalContractListByPageInfo 查询数据
        pageInfo.setTlist(pageList.getRecords());
        //findLegalContractListByPageInfoCount 总条数
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public TblCyhwUnit getEntity(BigDecimal contractid) throws Exception {
		return this.tblCyhwUnitMapper.selectChangeContractInfo(contractid);
	}

	@Override
	public Map<String, Object> getContractAnalysis(Integer year) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            //根据token获取用户信息
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("codes", "0");
                resultMap.put("msg", "获取用户失败！");
                return resultMap;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (year == null) {
                year = calendar.get(Calendar.YEAR);
            }
            //历月合同签订数量
            List<JSONObject> lyhtqdsl = tblCyhwUnitMapper.getContractMonth(user.getLinkOrg().getOrgid().toString(), year);
            String[] yue = {"jan", "feb", "march", "april", "may", "june", "july", "aug", "sept", "oct", "nov", "dec"};
            JSONArray lyhtqdslArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", yue[i]);
                if (lyhtqdsl.size() > 0) {
                    for (Object ob : lyhtqdsl) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", "0");
                    }
                } else {
                    obj.put("value", "0");
                }

                lyhtqdslArr.add(obj);
            }
            resultMap.put("lyhtqdslArr", lyhtqdslArr);
            //历月合同金额
            List<JSONObject> lyhtje = tblCyhwUnitMapper.getAmountMonth(user.getLinkOrg().getOrgid().toString(), year);
            JSONArray lyhtjeArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", yue[i]);
                if (lyhtje.size() > 0) {
                    for (Object ob : lyhtje) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", "0");
                    }
                } else {
                    obj.put("value", "");
                }

                lyhtjeArr.add(obj);
            }
            resultMap.put("lyhtjeArr", lyhtjeArr);
            //历月合同付款金额
            List<JSONObject> lyfkje = tblCyhwUnitMapper.getPaymentAmount(user.getLinkOrg().getOrgid().toString(), year);
            JSONArray lyfkjeArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", yue[i]);
                if (lyfkje.size() > 0) {
                    for (Object ob : lyfkje) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", "0");
                    }
                } else {
                    obj.put("value", "");
                }

                lyfkjeArr.add(obj);
            }
            resultMap.put("lyfkjeArr", lyfkjeArr);
            //历月合同实际付款金额
            List<JSONObject> lysjfkje = tblCyhwUnitMapper.getActualPaymentAmount(user.getLinkOrg().getOrgid().toString(), year);
            JSONArray lysjfkjeArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", yue[i]);
                if (lysjfkje.size() > 0) {
                    for (Object ob : lysjfkje) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", "0");
                    }
                } else {
                    obj.put("value", "");
                }

                lysjfkjeArr.add(obj);
            }
            resultMap.put("lysjfkjeArr", lysjfkjeArr);
            //历月合同收款金额
            List<JSONObject> lyhtskje = tblCyhwUnitMapper.getCollectionAmount(user.getLinkOrg().getOrgid().toString(), year);
            JSONArray lyhtskjeArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", yue[i]);
                if (lyhtskje.size() > 0) {
                    for (Object ob : lyhtskje) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", "0");
                    }
                } else {
                    obj.put("value", "");
                }

                lyhtskjeArr.add(obj);
            }
            resultMap.put("lyhtskjeArr", lyhtskjeArr);
            //历月合同实际收款金额
            List<JSONObject> lyhtsjskje = tblCyhwUnitMapper.getActualCollectionAmount(user.getLinkOrg().getOrgid().toString(), year);
            JSONArray lyhtsjskjeArr = new JSONArray();
            for (int i = 0; i < yue.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", yue[i]);
                if (lyhtsjskje.size() > 0) {
                    for (Object ob : lyhtsjskje) {
                        JSONObject o = (JSONObject) ob;
                        if (o.get("NAME").toString().equals((i + 1) + "")) {
                            obj.put("value", o.get("VALUE"));
                        }
                    }
                    if (obj.get("value") == null) {
                        obj.put("value", "0");
                    }
                } else {
                    obj.put("value", "");
                }

                lyhtsjskjeArr.add(obj);
            }
            resultMap.put("lyhtsjskjeArr", lyhtsjskjeArr);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> getContractCollection(Integer year) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
	        try {
	            TblStaffUtil user = userProvider.get();
	            if (user == null) {
	                resultMap.put("codes", "0");
	                resultMap.put("msg", "获取用户失败！");
	                return resultMap;
	            }
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(new Date());
	            if (year == null) {
	                year = calendar.get(Calendar.YEAR);
	            }
	            //各部门实际收款金额
	            List orgCollectionAmount = tblCyhwUnitMapper.getOrgCollectionAmount(user.getLinkOrg().getOrgid().toString(), year);
	            //当前公司下部门列表
	            List getOrgs = tblCyhwUnitMapper.getOrgs(user.getLinkOrg().getOrgid().toString());
	            resultMap.put("getOrgs", getOrgs);
	            resultMap.put("orgCollectionAmount", orgCollectionAmount);
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	        return resultMap;
	}
	
	 @Override
	 public Map<String, Object> orgContractPlan(Integer year, Integer quarter) throws Exception {
	        Map<String, Object> resultMap = new HashMap<String, Object>(0);
	        try {
	            TblStaffUtil user = userProvider.get();
	            if (user == null) {
	                resultMap.put("codes", "0");
	                resultMap.put("msg", "获取用户失败！");
	                return resultMap;
	            }
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(new Date());
	            if (year == null) {
	                year = calendar.get(Calendar.YEAR);
	            }
	            //各部门计划交付金额
	            List Amount = tblCyhwUnitMapper.getOrgAmount(user.getLinkOrg().getOrgid().toString(), year, quarter);
	            //各部门计划实际交付金额
	            List factualAmount = tblCyhwUnitMapper.getOrgFactualAmount(user.getLinkOrg().getOrgid().toString(), year, quarter);
	            //当前公司下部门列表
	            List getOrgs = tblCyhwUnitMapper.getOrgs(user.getLinkOrg().getOrgid().toString());
	            resultMap.put("getOrgs", getOrgs);
	            resultMap.put("Amount", Amount);
	            resultMap.put("factualAmount", factualAmount);
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	        return resultMap;
	 }

	@Override
	public Map<String, Object> orgPayContract(Integer year, Integer quarter) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("codes", "0");
                resultMap.put("msg", "获取用户失败！");
                return resultMap;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (year == null) {
                year = calendar.get(Calendar.YEAR);
            }
            //各部门计划交付金额
            List Amount = tblCyhwUnitMapper.getOrgAmount(user.getLinkOrg().getOrgid().toString(), year, quarter);
            //各部门计划实际交付金额
            List factualAmount = tblCyhwUnitMapper.getOrgFactualAmount(user.getLinkOrg().getOrgid().toString(), year, quarter);
            //当前公司下部门列表
            List<String> getOrgs = tblCyhwUnitMapper.getOrgs(user.getLinkOrg().getOrgid().toString());
            //计划交付项目
            List plannedProject = tblCyhwUnitMapper.getPlannedProject(user.getLinkOrg().getOrgid().toString(), year, quarter);
            //交付项目
            List factualProject = tblCyhwUnitMapper.getFactualProject(user.getLinkOrg().getOrgid().toString(), year, quarter);
            //for循环起来

            JSONArray arr = new JSONArray();
            for (Object obj : getOrgs) {
                JSONObject o = new JSONObject();
                o.put("year", year);
                o.put("bm", obj);
                arr.add(o);
            }
            //交付项目
            for (Object obj : arr) {
                JSONObject ob = (JSONObject) obj;
                if (factualProject.size() > 0) {
                    for (Object o : factualProject) {
                        JSONObject o1 = (JSONObject) o;
                        if (o1.get("NAME").toString().equals(ob.get("bm").toString())) {
                            ob.put("jfsl", o1.get("VALUE"));
                        }
                    }
                    if (ob.get("jfsl") == null) {
                        ob.put("jfsl", "0");
                    }
                } else {
                    ob.put("jfsl", "");
                }
            }
            //计划交付合同数量
            for (Object obj : arr) {
                JSONObject ob = (JSONObject) obj;
                if (plannedProject.size() > 0) {
                    for (Object o : plannedProject) {
                        JSONObject o1 = (JSONObject) o;
                        if (o1.get("NAME").toString().equals(ob.get("bm").toString())) {
                            ob.put("jhjfsl", o1.get("VALUE"));
                        }
                    }
                    if (ob.get("jhjfsl") == null) {
                        ob.put("jhjfsl", "0");
                    }
                } else {
                    ob.put("jhjfsl", "");
                }
            }

            //jfje
            for (Object obj : arr) {
                JSONObject ob = (JSONObject) obj;
                if (factualAmount.size() > 0) {
                    for (Object o : factualProject) {
                        JSONObject o1 = (JSONObject) o;
                        if (o1.get("NAME") == ob.get("bm")) {
                            ob.put("jfje", o1.get("VALUE"));
                        }
                    }
                    if (ob.get("jfje") == null) {
                        ob.put("jfje", "0");
                    }
                } else {
                    ob.put("jfje", "");
                }

            }
            //jh交付金额
            for (Object obj : arr) {
                JSONObject ob = (JSONObject) obj;
                if (Amount.size() > 0) {
                    for (Object o : Amount) {
                        JSONObject o1 = (JSONObject) o;
                        if (o1.get("NAME").toString().equals(ob.get("bm").toString())) {
                            ob.put("jhjfje", o1.get("VALUE"));
                        }
                    }
                    if (ob.get("jhjfje") == null) {
                        ob.put("jhjfje", "0");
                    }
                } else {
                    ob.put("jhjfje", "");
                }

                if (StringUtils.isNotBlank(ob.get("jhjfsl").toString())) {
                    Double jhjfsl = Double.valueOf(ob.get("jhjfsl").toString());
                    Double jfsl = (ob.get("jfsl").toString().length() > 0) ? Double.valueOf(ob.get("jfsl").toString()) : 0.0;
                    if(jhjfsl == 0) {
                    	 ob.put("jhwcl", 0);
                    }else {
                    	 ob.put("jhwcl", (jfsl / jhjfsl) * 100);
                    }
                } else {
                    ob.put("jhwcl", 0);
                }
            }
            resultMap.put("getOrgs", getOrgs);
            resultMap.put("data", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> contractLegalAprStat(Integer year, Integer quarter) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("codes", "0");
                resultMap.put("msg", "获取用户失败！");
                return resultMap;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (year == null) {
                year = calendar.get(Calendar.YEAR);
            }

            //按公司，查询审批合同数量，金额
            List statData = tblCyhwUnitMapper.contractLegalAprStat(user.getLinkOrg().getOrgid().toString(), year, quarter);

            resultMap.put("statData", statData);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> legalLitigationCaseStat(Integer year, Integer quarter) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
	        try {
	            TblStaffUtil user = userProvider.get();
	            if (user == null) {
	                resultMap.put("codes", "0");
	                resultMap.put("msg", "获取用户失败！");
	                return resultMap;
	            }
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(new Date());
	            if (year == null) {
	                year = calendar.get(Calendar.YEAR);
	            }

	            //按公司，查询审批合同数量，金额
	            List statData = tblCyhwUnitMapper.contractLegalAprStat(user.getLinkOrg().getOrgid().toString(), year, quarter);

	            resultMap.put("statData", statData);
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	        return resultMap;
	}

	@Override
	public JsonBean dynamicReport(String token, Integer indexYtype, Integer indexXtype, String contractTypeText,
			String contractDeptIds, String contractStaffIds, String contractOrgIds, String startDate, String endDate,
			Integer year, Integer quarte) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
    	//查询参数准备  查询列 与 where 条件拼接
    	String column = null;
    	String whereStr = null;
    	String groupStr = null;
    	Calendar cal = Calendar.getInstance();
        //如果年份为空，就使用当前年
    	if(year == null) {
    		year = cal.get(Calendar.YEAR);
    	}
    	String quarteStartDate = null;
    	String quarteEndDate = null;
    	if(quarte != null) {
    		switch (quarte) {
			case 1:
				quarteStartDate = year+"-01-01";
				quarteEndDate = year+"-03-31";
				break;
			case 2:
				quarteStartDate = year+"-04-01";
				quarteEndDate = year+"-06-30";		
				break;
			case 3:
				quarteStartDate = year+"-07-01";
				quarteEndDate = year+"-09-30";
				break;
			case 4:
				quarteStartDate = year+"-10-01";
				quarteEndDate = year+"-12-31";
				break;
			default:
				quarteStartDate = year+"-01-01";
				quarteEndDate = year+"-12-31";
				break;
			}
    	}
    	
    	
    	switch (indexXtype) {
	    	//1.维度查询为合同类型时
			case 1:
				if(StringUtils.isBlank(contractTypeText)) {
					List<String> nameList = this.tblContractTypeofMapper.selectAllTypeNameByOrgId();
					contractTypeText = StringUtils.join(nameList,",");
				}
				contractTypeText = contractTypeText.replace(",", "','");
				column = "TCU.CONTRACTTYPE AS XINDEX,";
				whereStr = " WHERE TCU.CONTRACTTYPE IN ('"+contractTypeText+"') AND TCU.ORGID = "+staff.getCurrentOrg().getOrgid();
				groupStr = " GROUP BY TCU.CONTRACTTYPE";
				break;
			//2.维度查询为合同部门
			case 2:
				if(StringUtils.isBlank(contractDeptIds)) {
					contractDeptIds = this.tblOrganizaService.selectDeptIdStrsByFatherOrgId(staff.getCurrentOrg().getOrgid());
				}
				column = " DEPT.ORGNAME AS XINDEX,";
				whereStr = " WHERE TCU.CONTRACTDEPT IN ("+contractDeptIds+")";
				groupStr = " GROUP BY DEPT.ORGNAME";
				break;
			//3.维度查询为承办人
			case 3:
				if(StringUtils.isBlank(contractStaffIds)) {
					String deptIdStrs = this.tblOrganizaService.selectDeptIdStrsByFatherOrgId(staff.getCurrentOrg().getOrgid());
					List<String> staffIdList = this.tblStaffMapper.selectStaffIdsByCompanyId(deptIdStrs);
					contractStaffIds = StringUtils.join(staffIdList,",");
				}
				column = " STAFF.REALNAME AS XINDEX,";
				whereStr = " WHERE TCU.CONTRACTSTAFF IN ("+contractStaffIds+")";
				groupStr = " GROUP BY STAFF.REALNAME";
				break;
			//4.维度查询为单位
			case 4:
				if(StringUtils.isBlank(contractOrgIds)) {
					contractOrgIds = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(staff.getCurrentOrg().getOrgid().toString());
				}
				column = "ORG.ORGNAME AS XINDEX,";
				whereStr = " WHERE TCU.ZXUNIT IN ("+contractOrgIds+")";
				groupStr = " GROUP BY ORG.ORGNAME ";
				break;
			//5.维度查询为签订起止日期
			case 5:
				if(StringUtils.isBlank(startDate)) {
					startDate = year+"-01-01";
				}
				if(StringUtils.isBlank(endDate)) {
					endDate = year+"-12-31";
				}
				column =  DataBaseSqlConfig.getYearAllStrColumn("TCU.STARTDATE", "YYYY-MM-DD")+"||'至'||  "+DataBaseSqlConfig.getYearAllStrColumn("TCU.ENDDATE", "YYYY-MM-DD")+" AS XINDEX,";
				whereStr = "WHERE TCU.STARTDATE >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND TCU.ENDDATE <= "+DataBaseSqlConfig.getDateStrFormat(endDate);
				groupStr = " GROUP BY "+DataBaseSqlConfig.getYearAllStrColumn("TCU.STARTDATE", "YYYY-MM-DD")+","+DataBaseSqlConfig.getYearAllStrColumn("TCU.ENDDATE", "YYYY-MM-DD")
				+" ORDER BY "+DataBaseSqlConfig.getYearAllStrColumn("TCU.STARTDATE", "YYYY-MM-DD")+" ASC ,"+DataBaseSqlConfig.getYearAllStrColumn("TCU.ENDDATE", "YYYY-MM-DD")+" ASC";
				break;
			default:
				break;
		}
    	
    	//Y轴指标准备
    	switch (indexYtype) {
			case 1:
				column += "SUM(TCU.CONTRACTMONEY) AS CONTRACTMONEY";
				break;
			case 2:
				column += "COUNT(0) AS CONTRACTCOUNT";
				break;
			case 3:
				column += "SUM(TCU.CONTRACTMONEY) AS CONTRACTMONEY,COUNT(0) AS CONTRACTCOUNT";
				break;
	
			default:
				break;
		}
    	whereStr += " AND TO_CHAR(TCU.CREATETIME,'YYYY') = "+year;
    	if(quarte != null) {
    		whereStr += " AND TCU.CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(quarteStartDate)+" AND TCU.CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(quarteEndDate);
    	}
    	//拼接sql
    	String sql = "SELECT "+ column + " FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_ORGANIZATION DEPT ON TCU.CONTRACTDEPT = DEPT.ORGID LEFT JOIN TBL_STAFF STAFF ON TCU.CONTRACTSTAFF = STAFF.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON TCU.ZXUNIT = ORG.ORGID "+whereStr+groupStr;
    	List<Map<String,Object>> resultList = this.tblCyhwUnitMapper.selectDynamicReportData(sql);
    	
    	return ResponseFormat.retParam(1, 200, resultList);
	}

	@Override
	public JsonBean checkLendStatus(String token, String contractId) throws Exception {
		TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(1, 20006, null);
        }

        try {
            TblContractLend lend = this.tblContractLendMapper.selectLendRecord(user.getStaffid().toString(), contractId);
            //无记录,可以创建
            if (null == lend) {
                return ResponseFormat.retParam(1, 30005, null);
            }
            //是否有借阅记录，且归还日期在今天后
            int compareDateWithNow = DateUtils.compareDateWithNow(lend.getReturndate());
            if (compareDateWithNow == 1 && lend.getLendstatus() == 6) {
                return ResponseFormat.retParam(2, 200, null);
            }
            return ResponseFormat.retParam(1, 30006, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public Map<String, Object> findListContractBiangengBypageInfo(String token, String flowId, String staffId,
			Integer pageNumber, Integer pageSize, TblCyhwUnit cyhwUnit) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            //根据不同的flowId查询出不同的流程
            //TblFlow flow = null;
            if ("733271".equals(flowId)) {
            	cyhwUnit.setRecordtype("HTGL007");
                //flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTFB, staff.getCurrentOrg().getOrgid());
            } else if ("622316".equals(flowId)) {
            	cyhwUnit.setRecordtype("HTGL002");
                //flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTDL, staff.getCurrentOrg().getOrgid());
            } else if ("622325".equals(flowId)) {
            	cyhwUnit.setRecordtype("HTGL005");
                //flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTBG, staff.getCurrentOrg().getOrgid());
            }
            //如果未能查出flow，没有对应的流程
            /*if (flow == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "流程未定义！(733271-合同范本,622316-合同订立，622325-合同变更)");
                return resultMap;
            }*/
            
            //获取当前登录用户查看密级范围
            Integer paCount = 1;
            //typeList查询出的数据
            List<TblContractTypeof> typeList = this.tblContractTypeofMapper.findAllList(staff.getCurrentOrg().getOrgid(), -1);
            //分页查询需要的参数
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
           
            TblStaff tblStaff = tblStaffMapper.findByStaffid(staff.getStaffid().toString());
            if ("HTGL005".equals(cyhwUnit.getRecordtype()) || "HTGL002".equals(cyhwUnit.getRecordtype())) {

                //如果是"合同管理员" 将paCount设置为0
                if (JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames())) {
                    paCount = 0;
                }
//                else {
//                    if (tblStaff != null && tblStaff.getManageorgs() != null && tblStaff.getManageorgs().length() > 0) {
//                        cyhwUnit.setContractdept(tblStaff.getOrgid());
//                        paCount = 0;
//                    }
//                }
                
                IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
                IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.selectUnitListInfo(page, staff.getCurrentOrg().getOrgid().toString(), staff.getStaffid().toString(), paCount,cyhwUnit,staff.getSecrectScopeIds());
                /*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
				}*/
                //合同范本列表数据存入到Tlist
                pageInfo.setTlist(pageList.getRecords());
                //查询出总条数
                pageInfo.setTotalRecord((int)pageList.getTotal());
            }
            if ("HTGL007".equals(cyhwUnit.getRecordtype())) {
                //如果是"合同范本管理人员"或者"合同管理员" 将paCount设置为0
                if (JudgeRoleRight.judgeRoleRight("合同范本管理人员", staff.getRoleNames()) || JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames())) {
                    paCount = 0;
                }
                IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
                IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.selectUnitListByPageInfo(page, staff.getStaffid().toString(), paCount,cyhwUnit);
				/*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
				}*/
                
                //合同范本列表数据存入到Tlist
                pageInfo.setTlist(pageList.getRecords());
                //查询出总条数
                pageInfo.setTotalRecord((int)pageList.getTotal());
            }
            //

            /*List<TblCyhwUnit> tlist = pageInfo.getTlist();
            for (TblCyhwUnit unit : tlist) {
                List<TblContractPlannode> planNodeS = contractPlannodeMapper.findPlanNodeListByContractId(unit.getContractid());
                unit.setPlanNodeList(planNodeS);
            }*/
            
            pageInfo.setCondition(cyhwUnit);
            dataMap.put("typeList", typeList);
            dataMap.put("flowId", flowId);
            dataMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}
	
	

    @Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> saveCyhwUnit(TblCyhwUnit tcu, String attids, String contractxdf, String token,
			String flowId, Integer goalStatus) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);

         try {
             TblStaffUtil staff = userProvider.get();
             if (staff == null) {
                 resultMap.put("code", "0");
                 resultMap.put("msg", "用户已失效！");
                 return resultMap;
             }

             //TblFlow flow = null;
             if ("733271".equals(flowId)) {
            	 tcu.setRecordtype(TblCyhwUnit.HTFB);
                 tcu.setFlowid(new BigDecimal(733271));
                 //flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTFB, staff.getCurrentOrg().getOrgid());
             } else if ("622316".equals(flowId)) {
            	 tcu.setRecordtype(TblCyhwUnit.HTDL);
                 tcu.setFlowid(new BigDecimal(622316));
                 //flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTDL, staff.getCurrentOrg().getOrgid());
             } else if ("622325".equals(flowId)) {
            	 tcu.setRecordtype(TblCyhwUnit.HTBG);
                 tcu.setFlowid(new BigDecimal(622325));
                 //flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTBG, staff.getCurrentOrg().getOrgid());
             }

             /*if (null != flow) {
                 tcu.setRecordtype(flow.getFlownumber());
                 tcu.setFlowid(flow.getFlowid());
             }*/

             tcu.setCreateuser(staff.getStaffid());
             tcu.setOrgid(staff.getCurrentOrg().getOrgid());
             tcu.setCreatetime(new Date());
             TblCyhwUnit oldUnit = null;
             String fatherNo = null;
             String contractNo = null;
             
             if (tcu.getContractid() != null) {
                 if (tcu.getContractchildren() != null && "否".equals(tcu.getContractchildren())) {
                     tcu.setRecordparent(null);
                 }
                 oldUnit = this.tblCyhwUnitMapper.getCyhwUnitEntity(tcu.getContractid());
                 
                 if(!"HTGL007".equals(oldUnit.getRecordtype()) ) {
                	 if(!oldUnit.getContractchildren().equals(tcu.getContractchildren())) {
                    	 if("是".equals(tcu.getContractchildren())) {
                    		 oldUnit = this.tblCyhwUnitMapper.getCyhwUnitEntity(tcu.getRecordparent());
                    		 fatherNo = oldUnit.getContractno();
                    	 }
                    	 contractNo = this.tblContractTypeofServiceImpl.getAutoContractNo(new BigDecimal(flowId),fatherNo,staff);
                    	 tcu.setContractno(contractNo);
                	 }
                 }
                 
                 
                 this.tblCyhwUnitMapper.updateById(tcu);
                 /*TblCyhwUnit unit = new TblCyhwUnit();
                 unit.setContractid(tcu.getContractid());
                 unit.setContractchildren(tcu.getContractchildren());
                 unit.setRecordparent(tcu.getRecordparent());
                 unit.setContractname(tcu.getContractname());
                 this.tblCyhwUnitMapper.updateCyhwUnit(unit);*/
             } else {
            	
                 if (goalStatus != null && goalStatus == 10) {
                     tcu.setRecordtype("HTGL005");
                     oldUnit = this.tblCyhwUnitMapper.getCyhwUnitEntity(tcu.getPreContractId());
                     tcu.setContractstatus(0);
                     tcu.setContractid(RandomUtil.uuBigDecimalId());
                     tcu.setRecordparent(tcu.getPreContractId());
                     //获取新的合同编号
                	 contractNo = this.tblContractTypeofServiceImpl.getAutoContractNo(BigDecimal.valueOf(622325),oldUnit.getContractno(),staff);
                	 tcu.setContractno(contractNo);
                     
                     tblCyhwUnitMapper.insert(tcu);
                     this.tblCyhwUnitMapper.updaContractStatus(oldUnit.getContractid().toString(), goalStatus);
                     List<TblContractInformation> inforMationList = tblContractInformationMapper.getInfomationList(oldUnit.getContractid().toString());
                     List<TblContractPlannode> nodeList = tblContractPlannodeMapper.getListPlannodeForBianGeng(oldUnit.getContractid().toString());
                     TblContractPlannode node = null;
                     TblContractInformation newInfo = null;
                     Iterator var10 = inforMationList.iterator();
                     TblContractInformation info = null;
                     while (var10.hasNext()) {
                         info = (TblContractInformation) var10.next();
                         newInfo = new TblContractInformation();
                         newInfo.setInfono(info.getInfono());
                         newInfo.setInfoname(info.getInfoname());
                         newInfo.setInfotype(info.getInfotype());
                         newInfo.setInfoxh(info.getInfoxh());
                         newInfo.setInfoorg(info.getInfoorg());
                         newInfo.setInfoprice(info.getInfoprice());
                         newInfo.setInfonum(info.getInfonum());
                         newInfo.setInfodesc(info.getInfodesc());
                         newInfo.setInfomomo(info.getInfomomo());
                         newInfo.setInfopinpai(info.getInfopinpai());
                         newInfo.setInfostartdate(info.getInfostartdate());
                         newInfo.setInfoenddate(info.getInfoenddate());
                         newInfo.setProjectid(tcu.getContractid());
                         newInfo.setInfoid(RandomUtil.uuBigDecimalId());
                         this.tblContractInformationMapper.saveContractInfoMation(newInfo);
                     }
                     for (TblContractPlannode oldNode : nodeList) {
                         if (oldNode.getPlannodestatus() != null && oldNode.getPlannodestatus().compareTo(BigDecimal.valueOf(2)) == 0) {
                             continue;
                         }
                         node = new TblContractPlannode();
                         node.setNodecontent(oldNode.getNodecontent());
                         node.setPlanenddate(oldNode.getPlanenddate());
                         node.setPlanstartdate(oldNode.getPlanstartdate());
                         node.setNodepost(oldNode.getNodepost());
                         node.setNodeplanpaydate(oldNode.getNodeplanpaydate());
                         node.setProjectid(tcu.getContractid());
                         node.setDispatchstaff(oldNode.getDispatchstaff());
                         node.setNodemoney(oldNode.getNodemoney());
                         node.setDispatchdept(oldNode.getDispatchdept());
                         node.setPlannodestatus(BigDecimal.valueOf(0));
                         node.setPerformanceCategory(oldNode.getPerformanceCategory());
                         node.setGoodsCount(oldNode.getGoodsCount());
                         node.setGoodsName(oldNode.getGoodsName());
                         node.setNodeid(RandomUtil.uuBigDecimalId());
                         this.tblContractPlannodeMapper.saveContractPlannode(node);
                     }
                 } else {
                	 //获取新的合同编号
                	 if("是".equals(tcu.getContractchildren())) {
                		 oldUnit = this.tblCyhwUnitMapper.getCyhwUnitEntity(tcu.getRecordparent());
                		 fatherNo = oldUnit.getContractno();
                	 }
                	 
                	 contractNo = this.tblContractTypeofServiceImpl.getAutoContractNo(new BigDecimal(flowId),fatherNo,staff);
                	 tcu.setContractno(contractNo);
                     tcu.setContractstatus(0);
                     tcu.setContractid(RandomUtil.uuBigDecimalId());
                     tblCyhwUnitMapper.insert(tcu);
                 }
             }

             if (attids != null && !"".equals(attids)) {
                 String[] ids = attids.split(",");
                 for (String id : ids) {
                     this.tblCyhwUnitMapper.insertAttmentRelation(id, tcu.getContractid());
                 }
             }

             //相对方信息，先删除，后插入数据
             tblCyhwUnitMapper.deleteContractBudget(tcu.getContractid());
             if (contractxdf != null) {
                 List<TblContractBudget> aBudgets = JSONArray.parseArray(contractxdf, TblContractBudget.class);
//                 System.out.println("相对方信息" + aBudgets.toString());
                 for (int i = 0; i < aBudgets.size(); i++) {
                     Integer index = i;
                     TblContractBudget tblContractBudget = aBudgets.get(index);
                     List<TblContractBudget> list = this.tblCyhwUnitMapper.selectContractBudget(tcu.getContractid(), tblContractBudget.getBugetId());
//                     if (list.size() == 0) {
                         this.tblCyhwUnitMapper.insertContractBudgetOrder(tcu.getContractid(), tblContractBudget.getBugetId(), tblContractBudget.getBugetType(), tblContractBudget.getContractname(),index);
//                     }else {
//                         this.tblCyhwUnitMapper.updateContractBudget(tcu.getContractid(), tblContractBudget.getBugetId(), tblContractBudget.getBugetType(), tcu.getContractname());
//                     }
                 }
             }

             resultMap.put("code", "1");
             resultMap.put("msg", "保存成功！");
             resultMap.put("data", tcu);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return resultMap;
	}


	@Override
	public Integer getStatueById(BigDecimal contractId) {
		return this.tblCyhwUnitMapper.selectStatueById(contractId);
	}

	@Override
	public Map<String, Object> removeCyhwUnit(BigDecimal contractId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        tblCyhwUnitMapper.deletePlannode(contractId);
        tblCyhwUnitMapper.deleteattachment(contractId);
        tblCyhwUnitMapper.deleteUnitaTT(contractId);
        tblCyhwUnitMapper.deleteInformation(contractId);
        tblCyhwUnitMapper.deleteunit(contractId);
        tblCyhwUnitMapper.deleteContractXDF(contractId);
        resultMap.put("code", "0");
        resultMap.put("msg", "删除成功！");
        return resultMap;
	}

	@Override
	public Map<String, Object> findListWindowOpenByPageInfo(Integer pageNumber, Integer pageSize, String recordType,
			String token, String staffId, String budgetname, String counterpartno) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            if ("HTGL005".equals(recordType) || "HTGL003".equals(recordType) || "HTGL004".equals(recordType) || "HTGL006".equals(recordType)) {
                PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                TblCyhwUnit tcu = new TblCyhwUnit();
                tcu.setOrgid(user.getCurrentOrg().getOrgid());
                tcu.setRecordtype(recordType);
                tcu.setContractstatus(6);
                tcu.setContractno(counterpartno);
                tcu.setContractname(budgetname);
                pageInfo.setCondition(tcu);
                
                IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
                IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findListByPageInfo(page,tcu, user.getCurrentOrg().getOrgid().toString());
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("data", pageInfo);
            } else {
                PageInfo<TblCyhwProjectbudget> pageInfo = new PageInfo<TblCyhwProjectbudget>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                TblCyhwProjectbudget tcpb = new TblCyhwProjectbudget();
                //tcpb.setOrgid(user.getCurrentOrg().getOrgid());
                tcpb.setRecordtype(recordType);
                tcpb.setInspectionstatus(6);
                tcpb.setIsblack(2);
                tcpb.setBudgetname(budgetname);
                tcpb.setCounterpartno(counterpartno);
                pageInfo.setCondition(tcpb);
                
                IPage<TblCyhwProjectbudget> page = new Page<TblCyhwProjectbudget>(pageNumber,pageSize);
                IPage<TblCyhwProjectbudget> pageList = tblCyhwProjectbudgetMapper.findListByPageInfo(page, user.getCurrentOrg().getOrgid().toString(),tcpb);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("data", pageInfo);
            }

            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> checkStageInfo(BigDecimal contractId, String token) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			resultMap.put("code", "0");
	        resultMap.put("msg", "用户已失效");
	        return resultMap;
        }
		
		
        resultMap.put("code", "1");
        resultMap.put("msg", "校验通过！");
        
        TblCyhwUnit entity = tblCyhwUnitMapper.selectByContractId(contractId);
        if (entity.getContractstatus() != null && entity.getContractstatus() == 1) {
            resultMap.put("code", "0");
            resultMap.put("msg", "流程审批中！");
            return resultMap;
        }
        if (entity.getContractstatus() != null && entity.getContractstatus() == 3) {
            resultMap.put("code", "0");
            resultMap.put("msg", "流程已通过！");
            return resultMap;
        }
        if ((entity.getContractstatus() != null && entity.getContractstatus() == 4) || (entity.getContractstatus() != null && entity.getContractstatus() == 5) || (entity.getContractstatus() != null && entity.getContractstatus() == 6)) {
            resultMap.put("code", "0");
            resultMap.put("msg", "流程已完成！");
            return resultMap;
        }
        if (entity.getContractstatus() == 7) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同正在执行中！");
            return resultMap;
        } else if (entity.getContractstatus() == 8) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同已归档！");
            return resultMap;
        } else if (entity.getContractstatus() == 9) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同已暂停！");
            return resultMap;
        } else if (entity.getContractstatus() == 10) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同已变更！");
            return resultMap;
        } else if (entity.getContractstatus() == 11) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同已终止！");
            return resultMap;
        } else if (entity.getContractstatus() == 12) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同纠纷中！");
            return resultMap;
        } else if (entity.getContractstatus() == 13) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同协商中！");
            return resultMap;
        } else if (entity.getContractstatus() == 14) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同诉讼中！");
            return resultMap;
        } else if (entity.getContractstatus() == 15) {
            resultMap.put("code", "0");
            resultMap.put("msg", "合同仲裁中！");
            return resultMap;
        }


        if (entity.getDescribe() == null || "".equals(entity.getDescribe())) {
            List<TblContractContentPdf> oldPdf = this.tblContractContentPdfService.findInfoByContractId(contractId);
            if (oldPdf == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "请填写合同正文内容或上传PDF文件！");
                return resultMap;
            }
        }


        Integer hwCount = this.tblContractPlannodeMapper.findeHwCountByContractid(contractId);
        if (entity.getContractplan() != null && "是".equals(entity.getContractplan()) && hwCount == 0) {
            Integer planCount = this.contractPlannodeMapper.countPlanByContractId(contractId);
            if (planCount == 0) {
                resultMap.put("code", "0");
                resultMap.put("msg", "请填写合同履行计划！");
                return resultMap;
            }
            Integer postCount = this.contractPlannodeMapper.sumPlanPostPoint(contractId);
            if (postCount != 100 && !"无".equals(entity.getDctype())) {
                resultMap.put("code", "0");
                resultMap.put("msg", "合同履行计划收付款比例不符合标准！");
                return resultMap;
            }
        }
        
        return resultMap;
	}

	@Override
	public Map<String, Object> findeContractInfo(BigDecimal flowId, String flowname, BigDecimal contractId, Integer cflag,
			String token, String staffId, Integer goalStatus) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }


            TblCyhwUnit item = tblCyhwUnitMapper.getCyhwUnitEntity(contractId);
            if (Objects.nonNull(item)){
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
			}
            if (item.getRecordparent() != null) {
                TblCyhwUnit parent = tblCyhwUnitMapper.findContractById(item.getRecordparent());
                item.setParentname(parent.getContractname());
                item.setPreContractName(parent.getContractname());
            }
            List<TblCyhwProjectbudget> budgetList = this.tblCyhwProjectbudgetMapper.seleCyhwProjectbudgets(item.getContractid());
            item.setBudgetList(budgetList);//相对方信息
            List<TblContractPlannode> nodeList = this.contractPlannodeMapper.findPlanNodeListByContractId(item.getContractid());
            dataMap.put("nodeList", nodeList);
            List<TblContractInformation> informationList = this.tblContractInformationMapper.findInformationListById(item.getContractid());
            dataMap.put("informationList", informationList);

            List<TblContractAppendixsigning> signingList = this.tblContractAppendixsigningMapper.findFileListByContractId(item.getContractid());
            dataMap.put("signingList", signingList);
            List<TblCyhwUnit> parentList = new ArrayList<TblCyhwUnit>(0);
            if(item.getRecordparent() != null) {
            	TblCyhwUnit parentCon = this.tblCyhwUnitMapper.findAllParentList(item.getRecordparent());
            	parentList.add(parentCon);
            	this.findAllParentList(parentList,parentCon.getRecordparent());
            }
            dataMap.put("parentList", parentList);
            if ("收款".equals(item.getDctype())) {
                List<TblContractCollection> colList = this.tblContractCollectionMapper.findcollectionListByContractId(item.getContractid());
                dataMap.put("colList", colList);
            }
            if ("付款".equals(item.getDctype())) {
                List<TblContractPayment> payList = this.tblContractPaymentMapper.findpaymentListByContractId(item.getContractid());
                dataMap.put("payList", payList);
            }
            //List<TblAttachment> attList = this.tblCyhwUnitMapper.findContractSealFileList(user.getCurrentOrg().getOrgid(),contractId);

            List<TblAttachment> attList = this.tblCyhwUnitMapper.findContractSealFileList(user.getCurrentOrg().getOrgid(), contractId);

            TblContractTypeof type = this.tblContractTypeofMapper.findByName(item.getContracttype(), item.getOrgid().toString());


            if (type != null && "default".equals(type.getPageurl())) {
                dataMap.put("type", "/nbkz/cyhw/cyhwUnitDetail");
            } else {
                dataMap.put("type", type.getPageurl());
            }
            
            if (goalStatus != null && goalStatus == 10) {
                item.setPreContractId(item.getContractid());
                item.setContractid(null);
            }

            dataMap.put("tcu", item);
            dataMap.put("flowid", flowId);
            dataMap.put("attList", attList);
            dataMap.put("flowname", flowname);
            dataMap.put("cz", "view");
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	private void findAllParentList(List<TblCyhwUnit> parentList, BigDecimal recordparent) {
		if(recordparent == null) {
			return;
		}
		TblCyhwUnit parentCon = this.tblCyhwUnitMapper.findAllParentList(recordparent);
    	parentList.add(parentCon);
    	this.findAllParentList(parentList, parentCon.getRecordparent());
	}

	@Override
	public List<TblCyhwUnit> findContractTemp(String token, String contractType) throws Exception {
		 TblStaffUtil staff = userProvider.get();
         return this.tblCyhwUnitMapper.selectContractTemp(staff.getCurrentOrg().getOrgid(), contractType);
	}

	@Override
	public Map<String, Object> findContractSealList(Integer pageNumber, Integer pageSize, String cateId, String flowid,
			String isFlowdb, String choose, String token, String staffId, TblCyhwUnit unit) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user1 = userProvider.get();
            if (user1 == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            //TblStaff user = this.tblStaffMapper.findByStaffid(user1.getStaffid().toString());
            //查询流程是否存在
            /*TblFlow flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwProjectbudget.HTYY, user1.getCurrentOrg().getOrgid());
            if (flow == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "流程未定义！");
                return resultMap;
            }*/

            //Integer result = tblProcessAnalysisMapper.getByModuel(flow.getFlownumber(), user1);
            //存每页查询条数和查询第几页
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setPageSize(pageSize);
            pageInfo.setCurrentPage(pageNumber);
            //流程编号
            unit.setRecordtype(TblCyhwProjectbudget.HTYY);
            //流程ID
            //unit.setFlowid(flow.getFlowid());
            unit.setOrgid(user1.getCurrentOrg().getOrgid());

            if (!"".equals(choose) && choose != null) {
                unit.setContractname(choose);
            }
            unit.setJbstaff(user1.getStaffid());
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
            IPage<TblCyhwUnit> pageList =  tblCyhwUnitMapper.findContractSealList(page,unit);
			/*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
			}*/
            //查询条件
            pageInfo.setCondition(unit);
            //返回的数据
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            dataMap.put("pageInfo", pageInfo);
            /*dataMap.put("processName", flow.getSettingid());
            dataMap.put("searchUrl", ((flow.getFlowmappingurl() == null) ? "/nbkz/cwgl/zcgl_mainck" : flow.getFlowmappingurl()));
            dataMap.put("flow", flow);*/
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> findeFilContractList(Integer pageNumber, Integer pageSize, String flowid, String token,
			String staffId, TblCyhwUnit unit) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
        try {

            /*TblFlow flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTGD, user.getCurrentOrg().getOrgid());
            if (flow == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "流程未定义！");
                return resultMap;
            }*/

            if (JudgeRoleRight.judgeRoleRight("法务人员", user.getRoleNames())) {
                dataMap.put("isLegalStaff", 1);//是法务人员，归档页面直接点击不用判断借阅状态
                unit.setNodeCount(1);
            } else {
                unit.setContractstaff(user.getStaffid());
                unit.setCreateuser(user.getStaffid());
                dataMap.put("isLegalStaff", 0);//不是法务人员，归档页面需要判断借阅操作
            }
            if(JudgeRoleRight.judgeRoleRight("合同管理员",  user.getRoleNames())) {
            	dataMap.put("gdBtn", 1);//归档按钮为1则显示
            }else {
            	dataMap.put("gdBtn", 0);//归档按钮为0则不显示
            }

            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setCurrentPage(pageNumber);
            //unit.setFlowid(flow.getFlowid());
            unit.setStaffid(user.getStaffid());
            unit.setOrgid(user.getCurrentOrg().getOrgid());
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findeFilContractList(page,unit);
			/*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
			}*/

            pageInfo.setCondition(unit);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int) pageList.getTotal());
            dataMap.put("pageInfo", pageInfo);
            /*dataMap.put("processName", flow.getSettingid());
            dataMap.put("searchUrl", ((flow.getFlowmappingurl() == null) ? "/nbkz/cwgl/zcgl_mainck" : flow.getFlowmappingurl()));
            dataMap.put("flow", flow);*/
            dataMap.put("currentUser", user);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public JsonBean chooseLendContractList(Integer pageNumber, Integer pageSize, String token, TblCyhwUnit unit)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        unit.setStaffid(loginStaff.getStaffid());
        unit.setOrgid(loginStaff.getCurrentOrg().getOrgid());
        
        IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
        IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findChooseLendContractList(page,unit);
        
        pageInfo.setCondition(unit);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        dataMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, dataMap);
	}

	@Override
	public Map<String, Object> updBasicUnitinspectionStatue(BigDecimal contractId, String flowname) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblCyhwUnit tcu = tblCyhwUnitMapper.findContractById(contractId);
            if ("HTGL005".equals(flowname)) {
                if (tcu.getRecordparent() != null) {
                    TblCyhwUnit tcu2 = tblCyhwUnitMapper.findContractById(tcu.getRecordparent());
                    //修改原我的合同状态
                    this.tblCyhwUnitMapper.repaContractStatus(tcu2.getContractid().toString(), tcu2.getHiscontractstatus() == null ? 7 : tcu2.getHiscontractstatus().intValue());
                }
                //删除我的变更中的数据
                tblCyhwUnitService.removeCyhwUnit(contractId);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "已撤回!");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "0");
            resultMap.put("msg", "撤回失败");
            return resultMap;
        }
	}
	
	@Override
    public Map<String, Object> updateContractStatus(String contractId, String status, String bindno){
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 try {
			TblCyhwUnit tcu = tblCyhwUnitMapper.findContractById(new BigDecimal(contractId));
			 this.tblCyhwUnitMapper.updateModifyContractStatus(new BigDecimal(tcu.getContractstatus()),Integer.parseInt(status),tcu.getContractid());
			 if(StringUtils.isNotBlank(bindno)) {
				 this.tblCyhwUnitMapper.modifyContractStatus(new BigDecimal(contractId), status, bindno);
			 }
			 resultMap.put("code", "1");
			 resultMap.put("msg", "归档成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return resultMap;
    }

	@Override
	public Map<String, Object> findContractTranList(Integer pageNumber, Integer pageSize, String token,
			TblContractTran tct) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user1 = userProvider.get();
            if (user1 == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            tct.setTranOrgId(user1.getCurrentOrg().getOrgid());
            tct.setHandStaffId(user1.getStaffid());

            IPage<TblContractTran> page = new Page<TblContractTran>(pageNumber,pageSize);
            IPage<TblContractTran> pageList = tblCyhwUnitMapper.findContractTranList(page,tct);
            /*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
			}*/
            PageInfo<TblContractTran> pageInfo = new PageInfo<TblContractTran>();
            pageInfo.setPageSize(pageSize);
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setCondition(tct);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            dataMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> contractTranListSave(TblContractTran tct, String token) throws Exception {
		BigDecimal contractid = tct.getContractId();
        TblCyhwUnit contract = tblCyhwUnitMapper.selectAllInfoById(contractid);

        tct.setContractName(contract.getContractname());
        tct.setContractNo(contract.getContractno());

        if (tct.getTranId() != null) {
            //修改；
//            this.tblCyhwUnitMapper.updateTblContractTran(tct);
			tblContractTranMapper.updateById(tct);
        } else {
            //新增；
//			cp.setCreatestaffid(loginStaff.getStaffid()+"");
            tct.setCreateTime(new Date());
            tct.setTranStatus(0);
            tct.setTranId(RandomUtil.uuBigDecimalId());
//            this.tblCyhwUnitMapper.insertTblContractTran(tct);
            this.tblContractTranMapper.insert(tct);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("tct", tct);
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        return resultMap;
	}

	@Override
	public Map<String, Object> contractTranInfo(BigDecimal tranId, String token) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user1 = userProvider.get();
            if (user1 == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            TblContractTran item = tblCyhwUnitMapper.findTblContractTran(tranId);
			if (Objects.nonNull(item)){
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
			}
            dataMap.put("tct", item);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> sealedContractList(Integer pageNumber, Integer pageSize, String token, TblCyhwUnit unit)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user1 = userProvider.get();
            if (user1 == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            unit.setOrgid(user1.getCurrentOrg().getOrgid());
            unit.setJbstaff(user1.getStaffid());
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findSealedContractList(page,unit);
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setPageSize(pageSize);
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setCondition(unit);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            dataMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> contractTranDelete(BigDecimal tranId, String token) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user1 = userProvider.get();
            if (user1 == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }

            tblCyhwUnitMapper.delTblContractTran(tranId);

            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}
	
	@Override
    public Map<String, Object> cyhwUnitOAListSave(String token, String contractId, String oaList) throws Exception {
        // TODO Auto-generated method stub
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                if (staff == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
                //存储OA列表
                if (oaList != null) {
                    List<TblOaDocumentList> documents = JSONArray.parseArray(oaList, TblOaDocumentList.class);
                    System.out.println("OA公文/协同列表" + documents.toString());
                    for (TblOaDocumentList dos : documents) {
                        dos.setContractId(contractId);
                        dos.setDocumentId(RandomUtil.uuBigDecimalId());
                        tblOaDocumentListMapper.insertDocument(dos);
                    }
                }
                resultMap.put("code", "1");
                resultMap.put("msg", "保存成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return resultMap;
    }

	@Override
	public Map<String, Object> getCyhwUnitOAList(String token, String contractId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                if (staff == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
                List<TblOaDocumentList> documents = tblOaDocumentListMapper.selectDocumentList(new BigDecimal(contractId));
                resultMap.put("data", documents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return resultMap;
	}

	@Override
	public Map<String, Object> removeOADocument(String token, String documentId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                if (staff == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
                tblOaDocumentListMapper.deleteDocumentId(new BigDecimal(documentId));
                resultMap.put("code", "1");
                resultMap.put("msg", "删除成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return resultMap;
	}

	@Override
	public Map<String, Object> findeContractListByContractStaff(String flowId, Integer pageNumber, Integer pageSize,
			String token, String staffId, TblCyhwUnit unit, Integer isDept) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        String jobName = null;
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            //TblFlow flow = tblFlowMapper.findFlowInfoById(flowId);
           
            //unit.setFlowid(flow.getFlowid());
            unit.setOrgid(user.getCurrentOrg().getOrgid());
            unit.setContractstaff(user.getStaffid());
            if (isDept != null && isDept == 1) {
                unit.setContractdept(user.getLinkDetp().getOrgid());
            }
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findeContractListByContractStaff(page,unit);
            /*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
			}*/
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(unit);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            jobName = this.tblStaffMapper.selectJobNameByJobId(user.getStaffid());
            if ("正职".equals(jobName) || "副职".equals(jobName)) {
                dataMap.put("judgeJob", 1);
            } else {
                dataMap.put("judgeJob", 0);
            }

            dataMap.put("pageInfo", pageInfo);
           /* dataMap.put("flowname", flow.getFlownumber());
            dataMap.put("flow", flow);
            dataMap.put("searchUrl", ((flow.getFlowmappingurl() == null) ? "/nbkz/cwgl/zcgl_mainck" : flow.getFlowmappingurl()));*/
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> getChangeContractStaffList(Integer pageNumber, Integer pageSize, String token,
			String username, String realname) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
           
            if (pageNumber == null) {
                pageNumber = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            TblStaff staff = new TblStaff();
            staff.setOrgid(user.getLinkDetp().getOrgid());
            staff.setRealname(realname);
            staff.setUsername(username);
            
            IPage<TblStaff> page = new Page<TblStaff>(pageNumber,pageSize);
            IPage<TblStaff> pageList = tblStaffMapper.selectContractStaffPageInfo(page,staff);
            PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(staff);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());

            dataMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        return resultMap;
	}

	@Override
	public Map<String, Object> changeContractStaff(String contractId, String staffId, String token) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
        this.tblCyhwUnitMapper.updateContractStaff(staffId, contractId);
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");

        return resultMap;
	}

	@Override
	public Map<String, Object> findeContractListPerformanceTracking(String flowId, Integer pageNumber, Integer pageSize,
			String token, String staffId, TblCyhwUnit unit) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            //TblFlow flow = tblFlowMapper.findFlowInfoById(flowId);
            //unit.setFlowid(flow.getFlowid());
            unit.setOrgid(user.getCurrentOrg().getOrgid());
            unit.setContractstaff(user.getStaffid());
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findeContractListPerformanceTracking(page,unit);
            /*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
			}*/
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(unit);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            dataMap.put("pageInfo", pageInfo);
            /*dataMap.put("flowname", flow.getFlownumber());
            dataMap.put("flow", flow);
            dataMap.put("searchUrl", ((flow.getFlowmappingurl() == null) ? "/nbkz/cwgl/zcgl_mainck" : flow.getFlowmappingurl()));*/
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> findFulfillmentContract(String flowId, Integer pageNumber, Integer pageSize,
			String token, String staffId, TblCyhwUnit unit) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        TblStaffUtil user = null;
        try {
            user = userProvider.get();
            if (user == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            //TblFlow flow = tblFlowMapper.findFlowInfoById(flowId);
            unit.setOrgid(user.getCurrentOrg().getOrgid());
            unit.setJbstaff(user.getStaffid());
            
            IPage<TblCyhwUnit> page = new Page<TblCyhwUnit>(pageNumber,pageSize);
            IPage<TblCyhwUnit> pageList = tblCyhwUnitMapper.findFulfillmentContract(page,unit);
            /*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
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
			}*/
            PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(unit);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            dataMap.put("pageInfo", pageInfo);
           /* dataMap.put("flow", flow);
            dataMap.put("searchUrl", ((flow.getFlowmappingurl() == null) ? "/nbkz/cwgl/zcgl_mainck" : flow.getFlowmappingurl()));*/
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> contractExcuteContractId(String contractId, String status) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            Integer fileCount = this.appendixsigningMapper.selectCountByContractId(new BigDecimal(contractId), 0);
            if (fileCount == 0) {
                resultMap.put("code", "1");
                resultMap.put("msg", "未上传对方签署文件，无法执行！");
                return resultMap;
            } else {
                this.tblCyhwUnitMapper.modifyContractStatus(new BigDecimal(contractId), "7",null);
                resultMap.put("code", "1");
                resultMap.put("msg", "成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> findContractDes(String budgetId) {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
         String descirp = this.tblCyhwUnitMapper.findContractDes(budgetId);
         resultMap.put("code", "1");
         resultMap.put("msg", "访问接口成功");
         resultMap.put("data", descirp);
         return resultMap;
	}

	@Override
	public Map<String, Object> modifyContractStatus(String contractId, Integer goalStatus) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dateMap = new HashMap<String, Object>(0);
        dateMap.put("contractId", contractId);
        try {
            if (goalStatus == 7) {
                Integer fileCount = this.appendixsigningMapper.selectCountByContractId(new BigDecimal(contractId), 0);
                if (fileCount == 0) {
                    this.tblCyhwUnitMapper.updaContractStatus(contractId, 6);
                    resultMap.put("code", "1");
                    resultMap.put("data", dateMap);
                    return resultMap;
                }
            }
            if (goalStatus == 10) {
                BigDecimal newid = copeContractInfoForBinageng(contractId);
                dateMap.put("contractId", newid);
            }
            this.tblCyhwUnitMapper.updaContractStatus(contractId, goalStatus);
            resultMap.put("code", "1");
            resultMap.put("data", dateMap);
            resultMap.put("msg", "访问接口成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	private BigDecimal copeContractInfoForBinageng(String contractId) {
		TblCyhwUnit unit = this.tblCyhwUnitMapper.getCyhwUnitEntity(new BigDecimal(contractId));
        List<TblContractPlannode> nodeList = tblContractPlannodeMapper.getListPlannodeForBianGeng(contractId);
        //TblFlow flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwUnit.HTBG, unit.getOrgid());
        TblCyhwUnit newunit = new TblCyhwUnit();
        newunit.setCreateuser(unit.getCreateuser());
        newunit.setOrgid(unit.getOrgid());
        newunit.setCreatetime(new Date());
        newunit.setEnddate(unit.getEnddate());
        newunit.setStartdate(unit.getStartdate());
        newunit.setContractdept(unit.getContractdept());
        newunit.setLinkdept(unit.getLinkdept());
        newunit.setContractstaff(unit.getContractstaff());
        newunit.setContractxdfxinfo(unit.getContractxdfxinfo());
        newunit.setRecordparent(unit.getRecordparent());
        newunit.setJbstaff(unit.getJbstaff());
        newunit.setJbdept(unit.getJbdept());
        newunit.setJbunit(unit.getJbdept());
        newunit.setZxunit(unit.getZxunit());
        newunit.setContractdept(unit.getContractdept());
        //newunit.setFlowid(flow.getFlowid());
        newunit.setContractstatus(0);
        newunit.setContractmoney(unit.getContractmoney());
        newunit.setContractname(unit.getContractname());
        newunit.setCounterpartbank(unit.getCounterpartbank());
       /* String writen = this.tblOrganizationMapper.selectWrittenDeptByOrgId(unit.getOrgid());
        String writenDept = this.tblOrganizationMapper.selectWrittenDeptByUserId(unit.getCreateuser());
        String year = DateUtil.parseDate(new Date(), "yyyy");
        String contractno = writen+writenDept+"["+year+"]";
        String no = this.tblCyhwUnitMapper.selectCountByContractNo(contractno,contractno+"%","HTGL002");
        if(no != null) {
            no = no.replace(contractno, "");
        }else {
            no = "0";
        }
        contractno += (Integer.parseInt(no)+1);*/
        String no = "HTBG" + DateUtils.dateToUnixTimestamp();
        newunit.setContractno(no+"号");
        newunit.setDescribe(unit.getDescribe());
        newunit.setMomoconcat(unit.getMomoconcat());
        newunit.setRiskcontrol(unit.getRiskcontrol());
        newunit.setContractdatetype(unit.getContractdatetype());
        newunit.setContracttype(unit.getContracttype());
        newunit.setContractxz(unit.getContractxz());
        newunit.setContractitem(unit.getContractitem());
        newunit.setDctype(unit.getDctype());
        newunit.setContractzd(unit.getContractzd());
        newunit.setContractchildren(unit.getContractchildren());
        newunit.setTopic(unit.getTopic());
        newunit.setTopicid(unit.getTopicid());
        newunit.setContractbd(unit.getContractbd());
        newunit.setMoneytype(unit.getMoneytype());
        newunit.setHzsumowing(unit.getHzsumowing());
        newunit.setJijiatype(unit.getJijiatype());
        newunit.setContractplan(unit.getContractplan());
        newunit.setRecordtype("HTGL005");
        newunit.setRecordparent(unit.getContractid());
        newunit.setSealorgid(unit.getSealorgid());
        newunit.setSealorgname(unit.getSealorgname());
        newunit.setEntrustStaffId(unit.getEntrustStaffId());
        newunit.setEntrustStaffName(unit.getEntrustStaffName());
        newunit.setContractid(RandomUtil.uuBigDecimalId());
        tblCyhwUnitMapper.insert(newunit);
        TblContractPlannode node = null;
        TblContractSpnode spNode = null;
        List<TblContractInformation> inforMationList = tblContractInformationMapper.getInfomationList(contractId);
        TblContractInformation newInfo = null;
        Iterator var10 = inforMationList.iterator();
        TblContractInformation info = null;
        while (var10.hasNext()) {
            info = (TblContractInformation) var10.next();
            newInfo = new TblContractInformation();
            newInfo.setInfono(info.getInfono());
            newInfo.setInfoname(info.getInfoname());
            newInfo.setInfotype(info.getInfotype());
            newInfo.setInfoxh(info.getInfoxh());
            newInfo.setInfoorg(info.getInfoorg());
            newInfo.setInfoprice(info.getInfoprice());
            newInfo.setInfonum(info.getInfonum());
            newInfo.setInfodesc(info.getInfodesc());
            newInfo.setInfomomo(info.getInfomomo());
            newInfo.setInfopinpai(info.getInfopinpai());
            newInfo.setInfostartdate(info.getInfostartdate());
            newInfo.setInfoenddate(info.getInfoenddate());
            newInfo.setProjectid(newunit.getContractid());
            this.tblContractInformationMapper.saveContractInfoMation(newInfo);
        }
        for (TblContractPlannode oldNode : nodeList) {
            if (oldNode.getPlannodestatus() != null && oldNode.getPlannodestatus().intValue() == 2) {
                continue;
            }
            node = new TblContractPlannode();
            node.setNodecontent(oldNode.getNodecontent());
            node.setPlanenddate(oldNode.getPlanenddate());
            node.setPlanstartdate(oldNode.getPlanstartdate());
            node.setNodepost(oldNode.getNodepost());
            node.setNodeplanpaydate(oldNode.getNodeplanpaydate());
            node.setProjectid(newunit.getContractid());
            node.setDispatchstaff(oldNode.getDispatchstaff());
            node.setNodemoney(oldNode.getNodemoney());
            node.setDispatchdept(oldNode.getDispatchdept());
            node.setPlannodestatus(null);
            node.setPerformanceCategory(oldNode.getPerformanceCategory());
            node.setGoodsCount(oldNode.getGoodsCount());
            node.setGoodsName(oldNode.getGoodsName());
            this.tblContractPlannodeMapper.saveContractPlannode(node);
        }
        return newunit.getContractid();
	}

	@Override
	public Map<String, Object> contractFjInfo(BigDecimal contractid) throws Exception {
		// TODO Auto-generated method stub
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            JSONObject obj = new JSONObject();
            List<TblAttachment> list = tblCyhwUnitMapper.findeFileInfo(contractid);
            List<JSONObject> list2 = new ArrayList();
            for (TblAttachment t : list) {
                JSONObject o = new JSONObject();
                o.put("singingId", t.getAttid());
                o.put("singingName", t.getAttname());
                o.put("singingSize", t.getAttsize());
                byte[] inputStream = FtpUtil.getInputStreamByOa(t.getAttpath(), t.getAttname());
                o.put("fileStream", inputStream);
                list2.add(o);
            }
            // obj.put("tlist", list2);
            resultMap.put("code", "0");
            resultMap.put("msg", "访问成功");
            resultMap.put("data", list2);
        } catch (Exception e) {
            // TODO: handle exception
            resultMap.put("code", "1");
            resultMap.put("msg", "访问失败！");
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> fjInfoGetList(String fjid, String userName) throws Exception {
		// TODO Auto-generated method stub
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
        	//存在历史合同附件 先判断是否为历史合同
            TblContractAppendixsigning sign=tblContractAppendixsigningMapper.findBysingingById(fjid);
            if(sign!=null&&StringUtils.isNotBlank(sign.getOaattid())){
                String oldUrl=HttpClient.dowloadFileOaUrl+sign.getSingingPath()+"?fileName="+sign.getSingingName()+"&token="+
            	getOaToken(userName);
                System.out.println(oldUrl);
            	resultMap.put("url", oldUrl);
            }else{
//				String ftpUrl = "ftp://" + FtpUtil.Ftpip;
//				String fileName = null;
//				ftpUrl += FtpUtil.constractfilepath;
				String fileName = this.tblAttachmentMapper.selectSignFileById(fjid);
				if (fileName == null || "".equals(fileName)) {
					resultMap.put("code", "500");
					resultMap.put("msg", "访问失败！");
					return resultMap;
				}
//				ftpUrl += fileName;
//				String encodeBase64String = Base64.getEncoder().encodeToString(ftpUrl.getBytes());
//				resultMap.put("url", FtpUtil.previewurl + "?url=" + encodeBase64String);
            	int ip= FtpUtil.Ftpip.indexOf("151");
            	if(ip<0) {
            		resultMap.put("url", "https://szfk.zjzsco.com/api/contract/downloadFtp/upload?singingId="+sign.getSingingId());
            	}else {
            		resultMap.put("url", "http://192.0.2.200/api/contract/downloadFtp/upload?singingId="+sign.getSingingId());
            	}
            	
				resultMap.put("code", "200");
				resultMap.put("msg", "访问成功！");
			}
			return resultMap;
        } catch (Exception e) {
            // TODO: handle exception
            resultMap.put("code", "500");
            resultMap.put("msg", "访问失败！");
        }
        return resultMap;
	}
	
	//获取对方token数据
  	public static String getOaToken(String name) {
  		String token=null;
  		try { 
  			 String url="";
  	            CloseableHttpClient httpClient = HttpClients.createDefault();
  	            RequestConfig requestConfig = RequestConfig.custom()
  	                    .setSocketTimeout(300 * 1000)
  	                    .setConnectTimeout(300 * 1000)
  	                    .build();
  	            url = HttpClient.oaUrl+HttpClient.getToken;
  	            HttpPost post = new HttpPost(url);
  	            post.setConfig(requestConfig);
  	            JSONObject obj=new JSONObject();
  	            obj.put("userName", HttpClient.restuname);//第三方待办主键（保证唯一）
  	            obj.put("password", HttpClient.restpassword);//为第三方配置的系统注册编码
  	            if(StringUtils.isNotBlank(name)){
  	  	            obj.put("loginName", name);
  	            }
  	            post.setHeader("Content-Type","application/json;charset=utf-8");
  	            StringEntity postingString = new StringEntity(obj.toString(),
  	                    "utf-8");
  	            post.setEntity(postingString);
  	            HttpResponse response = httpClient.execute(post);
  	            String content = EntityUtils.toString(response.getEntity());
  	            com.alibaba.fastjson.JSONObject result=com.alibaba.fastjson.JSONObject.parseObject(content);
  	            token= result.get("id").toString();
  	            System.out.println(token);
  		} catch (Exception e) {
  			e.printStackTrace();
  			return token;
  		}
  		return token;
  	}

	@Override
	public Map<String, Object> oppsiteInfogetList(TblStaffUtil staff) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            //根据staffID去TblCyhwUnit表查询是不是等于当前承办人或者经办人或者起草人
            // List<TblCyhwUnit> cyhwUnitlist = tblCyhwUnitMapper.getByStaffid(staff.getStaffid().toString());
            List<JSONObject> list = null;
            if (JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames())) {
                //管理人员可以看全部
                list = tblCyhwUnitMapper.getList(null, null,null);
            } else {
                //判断权限  当前登录人只能查看自己是当前合同的经办人或者承办人或者起草人的合同信息）
                list = tblCyhwUnitMapper.getList(staff.getStaffid().toString(), null,staff.getLinkDetp().getOrgid().toString());
            }

            JSONObject obj = new JSONObject();

            List<JSONObject> list2 = new ArrayList();
            for (JSONObject j : list) {
                JSONObject i = new JSONObject();
                //
                Double CONTRACTMONEY = Double.parseDouble(j.get("CONTRACTMONEY") != null ? j.get("CONTRACTMONEY").toString() : "");
                Double paymoney = 0.0;
                Double nopaymoney = 0.0;
                if (j.get("PAYMONEY") != null) {
                    paymoney = Double.parseDouble(j.get("PAYMONEY").toString());
                }
                if (CONTRACTMONEY != 0.0) {
                    //总金额-已付金额=未付金额
                    nopaymoney = CONTRACTMONEY - paymoney;
                }
                //未付金额
                i.put("ctsnopaidamount", nopaymoney);
                //名称
                i.put("ctsname", j.get("CONTRACTNAME") != null ? j.get("CONTRACTNAME").toString() : "");
                //编号
                i.put("ctscode", j.get("CONTRACTNO") != null ? j.get("CONTRACTNO").toString() : "");
                //已付金额
                i.put("ctspaidamount", paymoney);
                //合同金额
                i.put("ctsamount", j.get("CONTRACTMONEY") != null ? j.get("CONTRACTMONEY").toString() : "");

                //合同经办人
                i.put("ctsuser", j.get("REALNAME") != null ? j.get("REALNAME").toString() : "");
                //合同经办人工号/编号
                i.put("ctsusercode", j.get("USERNAME") != null ? j.get("USERNAME").toString() : "");
//                //合同附件标识
//                i.put("ctsfileID", j.get("SINGINGID") != null ? j.get("SINGINGID").toString() : "");
//                //合同附件名称
//                i.put("ctsfileName", j.get("SINGINGNAME") != null ? j.get("SINGINGNAME").toString() : "");

                //附件列表
                // JSONArray fileArr=new JSONArray();
                String contractid = j.get("CONTRACTID").toString();
                List<TblContractAppendixsigning> FileList = tblContractAppendixsigningMapper.findFileListByContractId(new BigDecimal(contractid));
                if (FileList != null && FileList.size() > 0) {
                    i.put("ctsfileID", FileList.get(0).getSingingId());
//                   //合同附件名称
                    i.put("ctsfileName", FileList.get(0).getSingingName());
                } else {
                    i.put("ctsfileID", "");
//                   //合同附件名称
                    i.put("ctsfileName", "");
                }

                //  for(TblContractAppendixsigning file:FileList){
                //JSONObject o=new JSONObject();
                //o.put("ctsfileID", file.getSingingId());
                //o.put("ctsfileName", file.getSingingName());
                //	fileArr.add(o);
                // }
                // i.put("fileList",fileArr);
                list2.add(i);
            }
            //obj.put("tlist", list2);
            resultMap.put("code", "200");
            resultMap.put("msg", "成功");
            resultMap.put("flag", "true");
            resultMap.put("data", list2);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            resultMap.put("code", "1");
            resultMap.put("msg", "访问失败！");
        }
        return resultMap;
	}

	@Override
	public JsonBean saveFzzcSpCyhwUnit() throws Exception {
		 Connection conn = null;
	        SqlConnRunner runner;
	        List<Entity> all;
	        try {
	            conn = BaseDaoSqlServer.getInstance().getGroupConnection();
	            runner = DbUtil.newSqlConnRunner(conn);
	            TblCyhwUnit tcu;
	            all = runner.findAll(conn, "formmain_1396");
	            for (Entity en : all) {
	                tcu = new TblCyhwUnit();
	                //追溯主键
	                tcu.setHtoaid(en.getStr("id"));
	                //币种
	                tcu.setMoneytype("人民币");
	                //合同执行中
	                tcu.setContractstatus(7);
	                //合同订立
	                tcu.setRecordtype(TblCyhwUnit.HTDL);
	                //合同编号
	                tcu.setContractno(en.getStr("field0001"));
	                //合同金额
	                tcu.setContractmoney(en.getBigDecimal("field0004"));
	                //合同金额大写
	                tcu.setHzsumowing(en.getStr("field0005"));
	                //所属项目
	                tcu.setTopicname(en.getStr("field0014"));
	                //流程默认ID
	                tcu.setFlowid(new BigDecimal(796215));
	                //无合同计划
	                tcu.setContractplan("否");
	                //合同主要内容及条款陈述
	                tcu.setDescribe(en.getStr("field0007"));
	                //经办人ID
	                String yhid = en.getStr("field0016");
	                //处理用户id映射转换
	                this.dealIds(conn, runner,tcu, yhid);
	                //保存合同
	                this.dealContractByNo(tcu);
	                //盖章签署后合同附件,字段不一样
	                String signFile = en.getStr("field0012");
	                //处理盖章签署后合同附件
	                this.dealSignAttach(conn, runner, tcu, new String[]{signFile});
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(0, 0, e.getMessage());
	        } finally {
	            BaseDaoSqlServer.getInstance().close(conn, null, null);
	        }
	        return ResponseFormat.retParam(0, 200, "succ.cnt:" + all.size());
	}

	private void dealSignAttach(Connection conn, SqlConnRunner runner, TblCyhwUnit tcu, String... signFile)  throws Exception{
		List<Entity> sign = runner.findIn(conn, "ctp_attachment", "sub_reference", signFile);
        TblContractAppendixsigning qry = new TblContractAppendixsigning();
        qry.setConstractId(tcu.getContractid());
        this.appendixsigningMapper.deleteById(qry);
        for (Entity e : sign) {
            TblContractAppendixsigning appendixsigning = new TblContractAppendixsigning();
            appendixsigning.setConstractId(tcu.getContractid());
            appendixsigning.setSingingName(e.getStr("filename"));
            appendixsigning.setSingingPath(e.getStr("filename"));
            appendixsigning.setSingingSize(new BigDecimal(e.getStr("attachment_size")));
            appendixsigning.setOaattid(tcu.getHtoaid());
            appendixsigning.setSingingStatus(0);
            appendixsigning.setSingingType(0);
            appendixsigning.setUploadTime(new Date());
            appendixsigning.setSingingId(RandomUtil.uuBigDecimalId());
            this.appendixsigningMapper.insert(appendixsigning);
        }
	}

	private void dealContractByNo(TblCyhwUnit tcu) throws Exception {
		TblCyhwUnit contractDb = this.tblCyhwUnitMapper.findContractByContractno(tcu.getContractno());
        if (contractDb != null) {
            tcu.setContractid(contractDb.getContractid());
            this.tblCyhwUnitMapper.updateCyhwUnit(tcu);
        } else {
        	tcu.setContractid(RandomUtil.uuBigDecimalId());
            this.tblCyhwUnitMapper.insertCyhwUnit(tcu);
        }
	}

	private void dealIds(Connection conn, SqlConnRunner runner, TblCyhwUnit tcu, String yhid) throws Exception {
		List<Entity> user = runner.findIn(conn, "view_user", "用户ID", yhid);
        if(user.size() == 0){
            return;
        }
        Entity entity = user.stream().findFirst().get();
        TblStaff staff = this.tblStaffMapper.getStaffByUsername(entity.getStr("登录名"));
        BigDecimal orgId = this.tblOrganizationMapper.selectOrgIdByOrgName(entity.getStr("单位名称"));
        tcu.setOrgid(orgId);
        tcu.setZxunit(orgId);
        if(staff!=null){
            tcu.setStaffid(staff.getStaffid());
            tcu.setCreateuser(staff.getStaffid());
            tcu.setContractstaff(staff.getStaffid());
            tcu.setContractdept(staff.getOrgid());
            tcu.setZxstaffid(String.valueOf(staff.getStaffid()));
            tcu.setZxstaffname(entity.getStr("登录名"));
        }
	}
	
	@Override
	public JsonBean generateNo(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        
        //获取当前年份
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String no = year+"1";
        Integer count = this.tblCyhwUnitMapper.selectCountByArchiveCount(no);
        count ++;
        String c = count.toString();
        int i = c.length();
        for (  ; i < 5; i++) {
        	no += "0";
		}
        no += count;
        resultMap.put("bindno",no);
        return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@Override
	public List<TblCyhwUnit> findLedgerOrgListForExport(TblCyhwUnit unit, String allCompanyIds) throws Exception {
		return tblCyhwUnitMapper.findLedgerListOrgForExport(unit,allCompanyIds);
	}
	
	@Override
	public Map<String, String> findOppsiteNamesByUnitTaiZhangExport(TblCyhwUnit unit, String fatherOrgIds)
			throws Exception {
		Map<String, String> budMap = new HashMap<String,String>(0); 
		List<TblCyhwProjectbudget> budList = this.tblCyhwProjectbudgetMapper.selectOppsiteNamesByUnitTaiZhangExport(unit,fatherOrgIds);
		if(budList == null || budList.size() == 0) {
			return budMap;
		}
		this.generalBudgetMapByContract(budList,budMap);
		return budMap;
	}

	@Override
	public Map<String, String> findOppsiteNamesByUnit(TblCyhwUnit unit, String allCompanyIds) throws Exception {
		Map<String, String> budMap = new HashMap<String,String>(0); 
		List<TblCyhwProjectbudget> budList = this.tblCyhwProjectbudgetMapper.selectListByUnitExport(unit,allCompanyIds);
		if(budList == null || budList.size() == 0) {
			return budMap;
		}
		this.generalBudgetMapByContract(budList,budMap);
		return budMap;
	}
	
	
// 根据合同生成预算映射
	private void generalBudgetMapByContract(List<TblCyhwProjectbudget> budList, Map<String, String> budMap) {
		// 如果预算列表为空，则直接返回
		if(budList == null || budList.size() == 0) {
			return;
		}
		// 初始化合同ID
		BigDecimal preContractId = null;
		// 初始化预算名称列表
		List<String> budgetNameList = new ArrayList<String>(0);
		// 遍历预算列表
		for (TblCyhwProjectbudget bud : budList) {
			// 如果合同ID为空，则将当前预算的父ID赋值给合同ID
			if(preContractId == null) {
				preContractId = bud.getRecordparent();
			}
			// 如果当前预算的父ID与合同ID不相等，则将合同ID和预算名称列表放入budMap中，并将合同ID和预算名称列表重置
			if(bud.getRecordparent().compareTo(preContractId) != 0) {
				budMap.put(preContractId.toString(), String.join(",", budgetNameList));
				preContractId = bud.getRecordparent();
				budgetNameList = new ArrayList<String>(0);
			}
			// 将当前预算的名称添加到预算名称列表中
			budgetNameList.add(bud.getBudgetname());
		}
		// 获取预算列表中的最后一个预算
		TblCyhwProjectbudget lastBud = budList.get(budList.size()-1);
		// 如果最后一个预算的父ID与合同ID相等，则将合同ID和预算名称列表放入budMap中
		if(lastBud.getRecordparent().equals(preContractId)) {
			budMap.put(preContractId.toString(), String.join(",", budgetNameList));
		}else {
			// 如果最后一个预算的父ID与合同ID不相等，则将预算名称列表中的最后一个预算名称移除，并将合同ID和预算名称列表放入budMap中
			budgetNameList.remove(budgetNameList.size()-1);
			budMap.put(preContractId.toString(), String.join(",", budgetNameList));
			// 将最后一个预算的父ID和名称放入budMap中
			budMap.put(lastBud.getRecordparent().toString(), lastBud.getBudgetname());
		}
	}

}
