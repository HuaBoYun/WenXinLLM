package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.util.BaseDaoSqlServer;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblContractAppendixsigning;
import com.huabo.contract.entity.TblContractContentPdf;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.mapper.TblContractAppendixsigningMapper;
import com.huabo.contract.mapper.TblContractContentPdfMapper;
import com.huabo.contract.mapper.TblContractPaymentMapper;
import com.huabo.contract.mapper.TblCounterpartBankinfoMapper;
import com.huabo.contract.mapper.TblCyhwProjectbudgetMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.mapper.TblStaffMapper;
import com.huabo.contract.service.SynchronizeOaService;

import lombok.extern.slf4j.Slf4j;

@Service("synchronizeOaServiceImpl")
@Slf4j
public class SynchronizeOaServiceImpl implements SynchronizeOaService {
	
	@Resource
	private TblCyhwUnitMapper tblCyhwUnitMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private TblCyhwProjectbudgetMapper tblCyhwProjectbudgetMapper;
	
	@Resource
	private TblCounterpartBankinfoMapper tblCounterpartBankinfoMapper;
	
	@Resource
	private TblStaffMapper tblStaffMapper;
	
	@Resource
	private TblContractPaymentMapper tblContractPaymentMapper;
	
	@Resource
	private TblContractContentPdfMapper tblContractContentPdfMapper;
	
	@Resource
	private TblContractAppendixsigningMapper tblContractAppendixsigningMapper;
	
	@Override
	public JsonBean SynchronizeGroupContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_0359");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectGroupCompanyContractInfo(i);
			this.delaGroupContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}

	@Override
	public JsonBean SynchronizeFzzcContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_0944");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectFzzcContractInfo(i);
			this.delafzzcContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}
	

	@Override
	public JsonBean SynchronizeRzzlContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_3193");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectRzzlContractInfo(i);
			this.delaRzzlContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}
	
	

	@Override
	public JsonBean SynchronizeyyjtContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_4401");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectyyjtCompanyContractInfo(i);
			this.delayyjtContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}

	@Override
	public JsonBean SynchronizeFuZhezcContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_1377");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectFuZhezcCompanyContractInfo(i);
			this.delaFuZhezcContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}
	
	@Override
	public JsonBean SynchronizFzkjContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_6134");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectFzkjCompanyContractInfo(i);
			this.delaFzkjContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}

	@Override
	public JsonBean SynchronizfzzbContractInfo() throws Exception {
		//1.获取OA系统中合同历史信息
		Integer totalCount = this.selectGroupCompanyContractCount("formmain_1204");
		Integer page = totalCount/100;
		if((totalCount%100)!=0) {page++;};
		log.info("本次同步合同记录数："+totalCount+",总页数（100）："+page);
		//分页查询历史合同数据
		List<TblCyhwUnit> contractList = null;
		for (int i = 0; i < page; i++) {
			contractList = this.selectFzzbCompanyContractInfo(i);
			this.delaFzzbContractInfo(contractList);
			
		}
		return ResponseFormat.retParam(1, 200, "本次同步合同记录数："+totalCount+",总页数（100）："+page);
	}
	
	private void delaFzzbContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		BigDecimal orgid = null;
		TblStaff staff = null;
		List<TblCyhwProjectbudget> budgetList = null;
		List<TblCyhwProjectbudget> conputerList = null;
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		TblCounterpartBankinfo bankInfo = null; //银行账户信息
		int i = 0;
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			unit.setContractid(RandomUtil.uuBigDecimalId());
			
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			 
			//3.同步相对方信息 
			budgetList = this.selectFzkjBudgetInfoByContrctid(unit.getHtoaid());
			for (TblCyhwProjectbudget budget : budgetList) {
				if(StringUtils.isBlank(budget.getBudgetname())) {
					continue;
				}
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(budget.getBudgetname());
				if(conputerList != null){
					//直接查询相对方
					conputer = conputerList.get(0);
					conputer.setOutsideId(budget.getOutsideId());
					this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(conputer, null);
					budget.setBudgetid(conputer.getBudgetid());
					log.info("该相对方已存在，相对方id："+budget.getBudgetid());
				}else {
					//新增相对方
					budget.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(budget);
					log.info("相对方维护成功：相对方Id："+budget.getBudgetid());
				}
				//维护相对方和银行账户的关系
				if(i == 0 && StringUtils.isNotBlank(unit.getBankaccount())) {
					bankInfo = this.tblCounterpartBankinfoMapper.selectBankInfoByAccount(unit.getBankaccount());
					if(bankInfo == null) {
						bankInfo = new TblCounterpartBankinfo();
						bankInfo.setBankaccount(unit.getBankaccount());
						bankInfo.setOutsideid(unit.getBankaccount());
						bankInfo.setBankkhyh(unit.getBankid());
						bankInfo.setBankstatus(new BigDecimal(1));
						bankInfo.setBanknature(new BigDecimal(1));
						bankInfo.setBankaccname(unit.getBankaccount());
						bankInfo.setBudgetid(budget.getBudgetid());
						bankInfo.setBankid(RandomUtil.uuBigDecimalId());
						this.tblCounterpartBankinfoMapper.saveBank(bankInfo);
						log.info("银行账号保存成功："+bankInfo.getBankaccount());
					}
				}
				i++;
				//保存合同和相对方的关联关系
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), budget.getBudgetid().toString(), budget.getBudgettype()!=null?budget.getBudgettype().length()>1?budget.getBudgettype().substring(0,1):budget.getBudgettype():null, unit.getContractname());
				log.info("相对方与合同关系维护成功");
			}
			
			//4.新增合同用印信息
			conputer = new TblCyhwProjectbudget();
			conputer.setCounterpartcode("公司公章");
			conputer.setCounterparthank(unit.getZxunitname());
			conputer.setRecordtype("HTGL003");
			conputer.setInspectionstatus(6);
			conputer.setFlowid(new BigDecimal(796216));
			conputer.setOrgid(unit.getOrgid());
			conputer.setBudgetid(RandomUtil.uuBigDecimalId());
			conputer.setRecordparent(unit.getContractid());
			this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(StringUtils.isNotBlank(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setConstractId(unit.getContractid());
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
			}
		}
	}

	private List<TblCyhwUnit> selectFzzbCompanyContractInfo(int page) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0001,CONTRACT.field0002,CONTRACT.field0005,CONTRACT.field0007,CONTRACT.field0008,CONTRACT.field0009,CONTRACT.field0010,CONTRACT.field0011,CONTRACT.field0012,CONTRACT.field0019,CONTRACT.field0024,CONTRACT.field0030,CONTRACT.field0031,CONTRACT.field0060,CONTRACT.field0061,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名 FROM formmain_1204 CONTRACT LEFT JOIN view_user staff ON CONTRACT.field0005 = staff.用户ID WHERE CONTRACT.ID NOT IN ( SELECT TOP "+(page*100)+" ID FROM formmain_1204 ORDER BY ID ) ORDER BY CONTRACT.ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0001"));
				contract.setContractname(rs.getString("field0002"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractbd(rs.getString("field0008"));
				contract.setContractmoney(rs.getBigDecimal("field0009")!=null?rs.getBigDecimal("field0009"):new BigDecimal(0));
				contract.setHzsumowing(rs.getString("field0010"));
				contract.setDescribe(rs.getString("field0007"));
				contract.setStartdate(rs.getDate("field0011"));
				contract.setEnddate(rs.getDate("field0012"));
				contract.setBankaccount(rs.getString("field0031")); //银行账号
				contract.setBankid(rs.getString("field0030"));  //开户银行
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterpartaddress(rs.getString("field0060"));//盖章后合同文本
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				contract.setProjectstagegoal(rs.getString("field0061"));//合同文本附件
				contract.setCounterpartphone(rs.getString("field0019"));//招标文件附件
				contract.setCounterpartcode(rs.getString("field0024"));//中标文件附件
				contractList.add(contract);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}

	private void delaFzkjContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		BigDecimal orgid = null;
		TblStaff staff = null;
		List<TblCyhwProjectbudget> budgetList = null;
		List<TblCyhwProjectbudget> conputerList = null ;
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		TblCounterpartBankinfo bankInfo = null; //银行账户信息
		int i = 0;
		BigDecimal paymoney = null;
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			paymoney = new BigDecimal(0);
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			
			unit.setContractid(RandomUtil.uuBigDecimalId());
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			 
			//3.同步相对方信息 
			budgetList = this.selectFzzbBudgetInfoByContrctid(unit.getHtoaid());
			for (TblCyhwProjectbudget budget : budgetList) {
				if(StringUtils.isBlank(budget.getBudgetname())) {
					continue;
				}
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(budget.getBudgetname());
				if(conputerList != null){
					//直接查询相对方
					conputer = conputerList.get(0);
					conputer.setOutsideId(budget.getOutsideId());
					this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(conputer, null);
					budget.setBudgetid(conputer.getBudgetid());
					log.info("该相对方已存在，相对方id："+budget.getBudgetid());
				}else {
					//新增相对方
					budget.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(budget);
					log.info("相对方维护成功：相对方Id："+budget.getBudgetid());
				}
				//维护相对方和银行账户的关系
				if(i == 0 && unit.getBankaccount() != null) {
					bankInfo = this.tblCounterpartBankinfoMapper.selectBankInfoByAccount(unit.getBankaccount());
					if(bankInfo == null) {
						bankInfo = new TblCounterpartBankinfo();
						bankInfo.setBankaccount(unit.getBankaccount());
						bankInfo.setOutsideid(unit.getBankaccount());
						bankInfo.setBankkhyh(unit.getBankid());
						bankInfo.setBankstatus(new BigDecimal(1));
						bankInfo.setBanknature(new BigDecimal(1));
						bankInfo.setBankaccname(unit.getBankaccount());
						bankInfo.setBudgetid(budget.getBudgetid());
						bankInfo.setBankid(RandomUtil.uuBigDecimalId());
						this.tblCounterpartBankinfoMapper.saveBank(bankInfo);
						log.info("银行账号保存成功："+bankInfo.getBankaccount());
					}
				}
				i++;
				//保存合同和相对方的关联关系
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), budget.getBudgetid().toString(), budget.getBudgettype()!=null?budget.getBudgettype().length()>1?budget.getBudgettype().substring(0,1):budget.getBudgettype():null, unit.getContractname());
				log.info("相对方与合同关系维护成功");
			}
			
			//4.新增合同用印信息
			conputer = new TblCyhwProjectbudget();
			conputer.setCounterpartcode("公司公章");
			conputer.setCounterparthank(unit.getZxunitname());
			conputer.setRecordtype("HTGL003");
			conputer.setInspectionstatus(6);
			conputer.setFlowid(new BigDecimal(796216));
			conputer.setOrgid(unit.getOrgid());
			conputer.setRecordparent(unit.getContractid());
			conputer.setBudgetid(RandomUtil.uuBigDecimalId());
			this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(StringUtils.isNotBlank(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setConstractId(unit.getContractid());
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
			}
		}
	}

	private List<TblCyhwProjectbudget> selectFzzbBudgetInfoByContrctid(String htoaid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwProjectbudget> budgetList = new ArrayList<TblCyhwProjectbudget>(0);
		TblCyhwProjectbudget budget = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT BUDGET.ID,BUDGET.field0036,CEP.SHOWVALUE FROM formson_1205 BUDGET LEFT JOIN CTP_ENUM_ITEM CEP ON BUDGET.FIELD0035 = CEP.ID WHERE BUDGET.formmain_id = '"+htoaid+"'");
			
			rs = ps.executeQuery();
			while (rs.next()) {
				budget = new TblCyhwProjectbudget();
				budget.setBudgetname(rs.getString("field0035"));
				budget.setOutsideId(rs.getString("ID"));
				budget.setRecordtype("HTGL001");
				budget.setInspectionstatus(6);
				budget.setFlowid(new BigDecimal(796214));
				budget.setBudgettype(rs.getString("SHOWVALUE"));
				budgetList.add(budget);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return budgetList;
	}

	private List<TblCyhwProjectbudget> selectFzkjBudgetInfoByContrctid(String htoaid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwProjectbudget> budgetList = new ArrayList<TblCyhwProjectbudget>(0);
		TblCyhwProjectbudget budget = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT BUDGET.ID,BUDGET.field0035,CEP.SHOWVALUE FROM formson_6135 BUDGET LEFT JOIN CTP_ENUM_ITEM CEP ON BUDGET.FIELD0034 = CEP.ID WHERE BUDGET.formmain_id = '"+htoaid+"'");
			
			rs = ps.executeQuery();
			while (rs.next()) {
				budget = new TblCyhwProjectbudget();
				budget.setBudgetname(rs.getString("field0035"));
				budget.setOutsideId(rs.getString("ID"));
				budget.setRecordtype("HTGL001");
				budget.setInspectionstatus(6);
				budget.setFlowid(new BigDecimal(796214));
				budget.setBudgettype(rs.getString("SHOWVALUE"));
				budgetList.add(budget);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return budgetList;
	}

	private List<TblCyhwUnit> selectFzkjCompanyContractInfo(int page) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0001,CONTRACT.field0002,CONTRACT.field0005,CONTRACT.field0007,CONTRACT.field0008,CONTRACT.field0009,CONTRACT.field0010,CONTRACT.field0011,CONTRACT.field0012,CONTRACT.field0019,CONTRACT.field0024,CONTRACT.field0027,CONTRACT.field0028,CONTRACT.field0029,CONTRACT.field0037,CONTRACT.field0041,CEI.SHOWVALUE,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名 FROM formmain_6134 CONTRACT LEFT JOIN view_user staff ON CONTRACT.field0005 = staff.用户ID LEFT JOIN CTP_ENUM_ITEM CEI ON CONTRACT.FIELD0042 = CEI.ID WHERE CONTRACT.ID NOT IN ( SELECT TOP "+(page*100)+" ID FROM formmain_6134 ORDER BY ID ) ORDER BY CONTRACT.ID");rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0001"));
				contract.setContractname(rs.getString("field0002"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractbd(rs.getString("field0008"));
				contract.setContractmoney(rs.getBigDecimal("field0009")!=null?rs.getBigDecimal("field0009"):new BigDecimal(0));
				contract.setHzsumowing(rs.getString("field0010"));
				contract.setDescribe(rs.getString("field0007"));
				contract.setStartdate(rs.getDate("field0011"));
				contract.setEnddate(rs.getDate("field0012"));
				contract.setBankaccount(rs.getString("field0028")); //银行账号
				contract.setBankid(rs.getString("field0027"));  //开户银行
				contract.setMomoconcat(rs.getString("field0029"));
				contract.setMoneytype(rs.getString("field0041"));
				contract.setContractxz(rs.getString("SHOWVALUE"));
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterpartaddress(rs.getString("field0037"));//盖章后合同文本
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				contract.setCounterpartphone(rs.getString("field0019"));//招标文件附件
				contract.setCounterpartcode(rs.getString("field0024"));//中标文件附件
				contractList.add(contract);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}

	private void delaFuZhezcContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		BigDecimal orgid = null;
		TblStaff staff = null;
		List<TblCyhwProjectbudget> budgetList = null;
		List<TblCyhwProjectbudget> conputerList = null ;//数据库存储的记录数
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		TblCounterpartBankinfo bankInfo = null; //银行账户信息
		int i = 0;
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			
			unit.setContractid(RandomUtil.uuBigDecimalId());
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			 
			//3.同步相对方信息 
			budgetList = this.selectFuZhezcBudgetInfoByContrctid(unit.getHtoaid());
			for (TblCyhwProjectbudget budget : budgetList) {
				if(StringUtils.isBlank(budget.getBudgetname())) {
					continue;
				}
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(budget.getBudgetname());
				if(conputerList != null){
					//直接查询相对方
					conputer = conputerList.get(0);
					conputer.setOutsideId(budget.getOutsideId());
					this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(conputer, null);
					budget.setBudgetid(conputer.getBudgetid());
					log.info("该相对方已存在，相对方id："+budget.getBudgetid());
				}else {
					//新增相对方
					budget.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(budget);
					log.info("相对方维护成功：相对方Id："+budget.getBudgetid());
				}
				//维护相对方和银行账户的关系
				if(i == 0 && StringUtils.isNotBlank(unit.getBankaccount())) {
					bankInfo = this.tblCounterpartBankinfoMapper.selectBankInfoByAccount(unit.getBankaccount());
					if(bankInfo == null) {
						bankInfo = new TblCounterpartBankinfo();
						bankInfo.setBankaccount(unit.getBankaccount());
						bankInfo.setOutsideid(unit.getBankaccount());
						bankInfo.setBankkhyh(unit.getBankid());
						bankInfo.setBankstatus(new BigDecimal(1));
						bankInfo.setBanknature(new BigDecimal(1));
						bankInfo.setBankaccname(unit.getBankaccount());
						bankInfo.setBudgetid(budget.getBudgetid());
						this.tblCounterpartBankinfoMapper.saveBank(bankInfo);
						log.info("银行账号保存成功："+bankInfo.getBankaccount());
					}
				}
				i++;
				//保存合同和相对方的关联关系
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), budget.getBudgetid().toString(), budget.getBudgettype()!=null?budget.getBudgettype().length()>1?budget.getBudgettype().substring(0,1):budget.getBudgettype():null, unit.getContractname());
				log.info("相对方与合同关系维护成功");
			}
			
			//4.新增合同用印信息
			conputer = new TblCyhwProjectbudget();
			conputer.setCounterpartcode("公司公章");
			conputer.setCounterparthank(unit.getZxunitname());
			conputer.setRecordtype("HTGL003");
			conputer.setInspectionstatus(6);
			conputer.setFlowid(new BigDecimal(796216));
			conputer.setOrgid(unit.getOrgid());
			conputer.setRecordparent(unit.getContractid());
			conputer.setBudgetid(RandomUtil.uuBigDecimalId());
			this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(StringUtils.isNotBlank(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setConstractId(unit.getContractid());
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
			}
		}
	}

	private List<TblCyhwProjectbudget> selectFuZhezcBudgetInfoByContrctid(String htoaid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwProjectbudget> budgetList = new ArrayList<TblCyhwProjectbudget>(0);
		TblCyhwProjectbudget budget = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT BUDGET.ID,BUDGET.field0035,CEP.SHOWVALUE FROM formson_1378 BUDGET LEFT JOIN CTP_ENUM_ITEM CEP ON BUDGET.FIELD0034 = CEP.ID WHERE BUDGET.formmain_id = '"+htoaid+"'");
			
			rs = ps.executeQuery();
			while (rs.next()) {
				budget = new TblCyhwProjectbudget();
				budget.setBudgetname(rs.getString("field0035"));
				budget.setOutsideId(rs.getString("ID"));
				budget.setRecordtype("HTGL001");
				budget.setInspectionstatus(6);
				budget.setFlowid(new BigDecimal(796214));
				budget.setBudgettype(rs.getString("SHOWVALUE"));
				budgetList.add(budget);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return budgetList;
	}

	private List<TblCyhwUnit> selectFuZhezcCompanyContractInfo(int page) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0001,CONTRACT.field0002,CONTRACT.field0005,CONTRACT.field0007,CONTRACT.field0008,CONTRACT.field0009,CONTRACT.field0010,CONTRACT.field0011,CONTRACT.field0012,CONTRACT.field0019,CONTRACT.field0024,CONTRACT.field0027,CONTRACT.field0028,CONTRACT.field0029,CONTRACT.field0037,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名 FROM formmain_1377 CONTRACT LEFT JOIN view_user staff ON CONTRACT.field0005 = staff.用户ID WHERE CONTRACT.ID NOT IN ( SELECT TOP "+(page*100)+" ID FROM formmain_4401 ORDER BY ID ) ORDER BY CONTRACT.ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0001"));
				contract.setContractname(rs.getString("field0002"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractbd(rs.getString("field0008"));
				contract.setContractmoney(rs.getBigDecimal("field0009")!=null?rs.getBigDecimal("field0009"):new BigDecimal(0));
				contract.setHzsumowing(rs.getString("field0010"));
				contract.setDescribe(rs.getString("field0007"));
				contract.setStartdate(rs.getDate("field0011"));
				contract.setEnddate(rs.getDate("field0012"));
				contract.setBankaccount(rs.getString("field0028")); //银行账号
				contract.setBankid(rs.getString("field0027"));  //开户银行
				contract.setMomoconcat(rs.getString("field0029"));
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterpartaddress(rs.getString("field0037"));//盖章后合同文本
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				
				contract.setCounterpartphone(rs.getString("field0019"));//招标文件附件
				contract.setCounterpartcode(rs.getString("field0024"));//中标文件附件
				
				contractList.add(contract);
				
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}

	private void delayyjtContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		BigDecimal orgid = null;
		TblStaff staff = null;
		List<TblCyhwProjectbudget> budgetList = null;
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		List<TblCyhwProjectbudget> conputerList = null ;//数据库存储的记录数
		TblCounterpartBankinfo bankInfo = null; //银行账户信息
		int i = 0;
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			
			unit.setContractid(RandomUtil.uuBigDecimalId());
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			
			//3.同步相对方信息
			if(StringUtils.isNotBlank(unit.getMiblephone())){
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(unit.getMiblephone());
				if(conputerList == null){
					//新增相对方
					//新增相对方
					conputer = new TblCyhwProjectbudget();
					conputer.setBudgetname(unit.getMiblephone());
					conputer.setOutsideId(unit.getMiblephone());
					conputer.setRecordtype("HTGL001");
					conputer.setInspectionstatus(6);
					conputer.setFlowid(new BigDecimal(796214));
					conputer.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
					log.info("相对方维护成功：相对方Id："+conputer.getBudgetid());
				}
				
				if(StringUtils.isNotBlank(unit.getBankaccount())) {
					bankInfo = this.tblCounterpartBankinfoMapper.selectBankInfoByAccount(unit.getBankaccount());
					if(bankInfo == null) {
						bankInfo = new TblCounterpartBankinfo();
						bankInfo.setBankaccount(unit.getBankaccount());
						bankInfo.setOutsideid(unit.getBankaccount());
						bankInfo.setBankkhyh(unit.getBankid());
						bankInfo.setBankstatus(new BigDecimal(1));
						bankInfo.setBanknature(new BigDecimal(1));
						bankInfo.setBankaccname(unit.getBankaccount());
						bankInfo.setBudgetid(conputer.getBudgetid());
						bankInfo.setBankid(RandomUtil.uuBigDecimalId());
						this.tblCounterpartBankinfoMapper.saveBank(bankInfo);
						log.info("银行账号保存成功："+bankInfo.getBankaccount());
					}
				}
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), conputer.getBudgetid().toString(),"甲", unit.getContractname());
				
			}
			
			if(StringUtils.isNotBlank(unit.getMemo())) {
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(unit.getMemo());
				if(conputerList == null){
					conputer = new TblCyhwProjectbudget();
					conputer.setBudgetname(unit.getMemo());
					conputer.setOutsideId(unit.getMemo());
					conputer.setRecordtype("HTGL001");
					conputer.setInspectionstatus(6);
					conputer.setFlowid(new BigDecimal(796214));
					conputer.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
					log.info("相对方维护成功：相对方Id："+conputer.getBudgetid());
				}
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), conputer.getBudgetid().toString(),"乙", unit.getContractname());
			}
			
			if(StringUtils.isNotBlank(unit.getUsername())) {
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(unit.getUsername());
				if(conputerList == null){
					//新增相对方
					conputer = new TblCyhwProjectbudget();
					conputer.setBudgetname(unit.getUsername());
					conputer.setOutsideId(unit.getUsername());
					conputer.setRecordtype("HTGL001");
					conputer.setInspectionstatus(6);
					conputer.setFlowid(new BigDecimal(796214));
					conputer.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
					log.info("相对方维护成功：相对方Id："+conputer.getBudgetid());
				}
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), conputer.getBudgetid().toString(),"丙", unit.getContractname());
			}
			
			//4.新增合同用印信息
			conputer = new TblCyhwProjectbudget();
			conputer.setCounterpartcode("公司公章");
			conputer.setCounterparthank(unit.getZxunitname());
			conputer.setRecordtype("HTGL003");
			conputer.setInspectionstatus(6);
			conputer.setFlowid(new BigDecimal(796216));
			conputer.setOrgid(unit.getOrgid());
			conputer.setRecordparent(unit.getContractid());
			conputer.setBudgetid(RandomUtil.uuBigDecimalId());
			this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(StringUtils.isNotBlank(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setConstractId(unit.getContractid());
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
				
			}
		}
	}

	private List<TblCyhwUnit> selectyyjtCompanyContractInfo(int page) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0002,CONTRACT.field0003,CONTRACT.field0006,CONTRACT.field0054,CONTRACT.field0007,CONTRACT.field0008,CONTRACT.field0010,CONTRACT.field0011,CONTRACT.field0012,CONTRACT.field0013,CONTRACT.field0014,CONTRACT.field0015,CONTRACT.field0016,CONTRACT.field0017,CONTRACT.field0019,CONTRACT.field0027,CONTRACT.field0033,CONTRACT.field0035,CONTRACT.field0037,CONTRACT.field0044,CONTRACT.field0049,CONTRACT.field0052,CONTRACT.field0053,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名,CEI.SHOWVALUE FROM formmain_4401 CONTRACT LEFT JOIN view_user staff ON CONTRACT.field0002 = staff.用户ID LEFT JOIN CTP_ENUM_ITEM CEI ON CEI.ID = CONTRACT.field0015 WHERE CONTRACT.ID NOT IN ( SELECT TOP "+(page*100)+" ID FROM formmain_4401 ORDER BY ID ) ORDER BY CONTRACT.ID");
					
			rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0007"));
				contract.setContractname(rs.getString("field0008"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractbd(rs.getString("field0011"));
				contract.setContractmoney(rs.getBigDecimal("field0012")!=null?rs.getBigDecimal("field0012"):new BigDecimal(0));
				contract.setHzsumowing(rs.getString("field0013"));
				contract.setStartdate(rs.getDate("field0016"));
				contract.setEnddate(rs.getDate("field0017"));
				contract.setBankaccount(rs.getString("field0053")); //银行账号
				contract.setBankid(rs.getString("field0052"));  //开户银行
				contract.setMoneytype(rs.getString("field0014"));
				contract.setContractxz(rs.getString("SHOWVALUE"));
				contract.setMomoconcat(rs.getString("field0019"));
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterpartaddress(rs.getString("field0027"));//盖章后合同文本
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				
				contract.setProjectstagegoal(rs.getString("field0010"));//合同文本附件
				contract.setCounterpartphone(rs.getString("field0044"));//招标文件附件
				contract.setCounterpartcode(rs.getString("field0049"));//中标文件附件
				
				contract.setMiblephone(rs.getString("field0033")); //相对方甲方名称
				contract.setMemo(rs.getString("field0035")); //相对方乙方名称
				contract.setUsername(rs.getString("field0037"));//相对方丙方名称;
				
				contractList.add(contract);
				
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}

	private void delaGroupContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		
		BigDecimal orgid = null;
		TblStaff staff = null;
		List<TblCyhwProjectbudget> budgetList = null;
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		List<TblCyhwProjectbudget> conputerList = null;
		TblCounterpartBankinfo bankInfo = null; //银行账户信息
		int i = 0;
		BigDecimal paymoney = new BigDecimal(0);//已付款金额累计
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			paymoney = new BigDecimal(0);//已付款金额累计
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			
			unit.setContractid(RandomUtil.uuBigDecimalId());
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			 
			//3.同步相对方信息 
			budgetList = this.selectGroupBudgetInfoByContrctid(unit.getHtoaid());
			for (TblCyhwProjectbudget budget : budgetList) {
				if(StringUtils.isBlank(budget.getBudgetname())) {
					continue;
				}
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(budget.getBudgetname());
				if(conputerList != null){
					//直接查询相对方
					conputer = conputerList.get(0);
					conputer.setOutsideId(budget.getOutsideId());
					this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(conputer, null);
					budget.setBudgetid(conputer.getBudgetid());
					log.info("该相对方已存在，相对方id："+budget.getBudgetid());
				}else {
					//新增相对方
					budget.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(budget);
					log.info("相对方维护成功：相对方Id："+budget.getBudgetid());
				}
				//维护相对方和银行账户的关系
				if(i == 0 && StringUtils.isNotBlank(unit.getBankaccount())) {
					bankInfo = this.tblCounterpartBankinfoMapper.selectBankInfoByAccount(unit.getBankaccount());
					if(bankInfo == null) {
						bankInfo = new TblCounterpartBankinfo();
						bankInfo.setBankaccount(unit.getBankaccount());
						bankInfo.setOutsideid(unit.getBankaccount());
						bankInfo.setBankkhyh(unit.getBankid());
						bankInfo.setBankstatus(new BigDecimal(1));
						bankInfo.setBanknature(new BigDecimal(1));
						bankInfo.setBankaccname(unit.getBankaccount());
						bankInfo.setBudgetid(budget.getBudgetid());
						bankInfo.setBankid(RandomUtil.uuBigDecimalId());
						this.tblCounterpartBankinfoMapper.saveBank(bankInfo);
						log.info("银行账号保存成功："+bankInfo.getBankaccount());
					}
				}
				i++;
				//保存合同和相对方的关联关系
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), budget.getBudgetid().toString(), budget.getBudgettype()!=null?budget.getBudgettype().length()>1?budget.getBudgettype().substring(0,1):budget.getBudgettype():null, unit.getContractname());
				log.info("相对方与合同关系维护成功");
			}
			
			//4.维护付款信息
			List<TblContractPayment> paymentList = this.selectGroupPaymentByContractid(unit.getHtoaid());
			for (TblContractPayment payment : paymentList) {
				payment.setLinkorg(unit.getOrgid());
				payment.setContractid(unit.getContractid());
				payment.setAccumulatedpayments(paymoney);
				paymoney = paymoney.add(payment.getPaymenmoney());
				payment.setNoPaymoney(unit.getContractmoney().subtract(paymoney));
				payment.setPaymentid(RandomUtil.uuBigDecimalId());
				this.tblContractPaymentMapper.savePaymenInfo(payment);
				log.info("合同付款信息维护成功，付款ID："+payment.getPaymentid());
			}
			
			
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(StringUtils.isNotBlank(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setConstractId(unit.getContractid());
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
			}
		}
	}
	

	private TblContractAppendixsigning selectSignFileInf(String counterpartaddress) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TblContractAppendixsigning signFile = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT ID,FILENAME,FILE_URL,CREATEDATE,ATTACHMENT_SIZE FROM CTP_ATTACHMENT WHERE SUB_REFERENCE = '"+counterpartaddress+"'");
			rs = ps.executeQuery();
			while (rs.next()) {
				signFile = new TblContractAppendixsigning();
				signFile.setOaattid(rs.getString("ID"));
				signFile.setSingingName(rs.getString("FILENAME"));
				signFile.setSingingPath(rs.getString("FILE_URL"));
				signFile.setSingingSize(rs.getBigDecimal("ATTACHMENT_SIZE"));
				signFile.setUploadTime(rs.getDate("CREATEDATE"));
				signFile.setSingingStatus(0);
				signFile.setSingingType(0);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return signFile;
	}

	private List<TblContractContentPdf> selectGroupContractPdf(String projectstagegoal, String counterpartphone,
			String counterpartcode, String counterparthank) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblContractContentPdf> pdfList = new ArrayList<TblContractContentPdf>(0);
		TblContractContentPdf pdf = null;
		try {
			String ids = "";
			if(StringUtils.isNotBlank(projectstagegoal)) {
				ids += "'"+projectstagegoal +"',";
			}
			if(StringUtils.isNotBlank(counterpartphone)) {
				ids += "'"+counterpartphone +"',";
			}
			if(StringUtils.isNotBlank(counterpartcode)) {
				ids += "'"+counterpartcode +"',";
			}
			if(StringUtils.isNotBlank(counterparthank)) {
				ids += "'"+counterparthank +"',";
			}
			if(StringUtils.isNotBlank(ids)) {
				ids = ids.substring(0,ids.length()-1);
			}else {
				return pdfList;
			}
			String sql = "SELECT ID,FILENAME,FILE_URL,CREATEDATE,ATTACHMENT_SIZE FROM CTP_ATTACHMENT WHERE SUB_REFERENCE IN ("+ids+")";
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				pdf = new TblContractContentPdf();
				pdf.setOaattid(rs.getString("ID"));
				pdf.setContentPdfName(rs.getString("FILENAME"));
				pdf.setContentPdfPath(rs.getString("FILE_URL"));
				pdf.setContentPdfSize(rs.getBigDecimal("ATTACHMENT_SIZE"));
				pdf.setUploadTime(rs.getDate("CREATEDATE"));
				pdfList.add(pdf);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return pdfList;
	}

	private List<TblContractPayment> selectGroupPaymentByContractid(String htoaid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblContractPayment> paymentList = new ArrayList<TblContractPayment>(0);
		TblContractPayment payment = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT ID,field0044,field0045,field0046,field0047,field0048 FROM formson_0362 WHERE formmain_id = '"+htoaid+"' ORDER BY field0043 ");
			rs = ps.executeQuery();
			while (rs.next()) {
				payment = new TblContractPayment();
				payment.setPaymentmoney(rs.getBigDecimal("field0046")!=null?rs.getBigDecimal("field0046"):new BigDecimal(0));
				payment.setPaymenmoney(rs.getBigDecimal("field0046")!=null?rs.getBigDecimal("field0046"):new BigDecimal(0));
				payment.setPaymentmemo(rs.getString("field0048"));
				payment.setPaymentlatedate(rs.getDate("field0047"));
				payment.setApplydate(rs.getDate("field0047"));
				payment.setPaymentstatus((long)6);
				payment.setPaymenttitle(rs.getString("field0044"));
				payment.setPaymentmemo(payment.getPaymentmemo()!=null?rs.getString("field0045")+payment.getPaymentmemo():rs.getString("field0045"));
				paymentList.add(payment);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return paymentList;
	}
	
	private List<TblCyhwProjectbudget> selectGroupBudgetInfoByContrctid(String htoaid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwProjectbudget> budgetList = new ArrayList<TblCyhwProjectbudget>(0);
		TblCyhwProjectbudget budget = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT BUDGET.ID,BUDGET.sort,BUDGET.field0036,CEI.SHOWVALUE FROM formson_0360 BUDGET LEFT JOIN CTP_ENUM_ITEM CEI ON BUDGET.field0035 = CEI.ID WHERE formmain_id = '"+htoaid+"' ORDER BY BUDGET.sort");
			rs = ps.executeQuery();
			while (rs.next()) {
				budget = new TblCyhwProjectbudget();
				budget.setBudgetname(rs.getString("field0036"));
				budget.setOutsideId(rs.getString("ID"));
				budget.setRecordtype("HTGL001");
				budget.setInspectionstatus(6);
				budget.setFlowid(new BigDecimal(796214));
				budget.setBudgettype(rs.getString("SHOWVALUE"));
				budgetList.add(budget);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return budgetList;
	}

	private Integer selectGroupCompanyContractCount(String tableName) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer count = 0;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT COUNT(0) FROM "+tableName);
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return count;
	}

	private List<TblCyhwUnit> selectGroupCompanyContractInfo(int page) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0001,CONTRACT.field0002,CONTRACT.field0007,CONTRACT.field0019,CONTRACT.field0024,CONTRACT.field0008,CONTRACT.field0009,CONTRACT.field0010,CONTRACT.field0011,CONTRACT.field0012,CONTRACT.field0014,CONTRACT.field0062,CEI.SHOWVALUE,CONTRACT.field0030,CONTRACT.field0031,CONTRACT.field0060,CONTRACT.field0061,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名 FROM formmain_0359 CONTRACT LEFT JOIN view_user staff on CONTRACT.field0005 = staff.用户ID LEFT JOIN CTP_ENUM_ITEM CEI ON CEI.ID = CONTRACT.field0063 WHERE CONTRACT.ID NOT IN (SELECT TOP "+(page*100)+" ID FROM formmain_0359 ORDER BY ID ) ORDER BY CONTRACT.ID");
					
			rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0001"));
				contract.setContractname(rs.getString("field0002"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractbd(rs.getString("field0008"));
				contract.setContractmoney(rs.getBigDecimal("field0009")!=null?rs.getBigDecimal("field0009"):new BigDecimal(0));
				contract.setHzsumowing(rs.getString("field0010"));
				contract.setStartdate(rs.getDate("field0011"));
				contract.setEnddate(rs.getDate("field0012"));
				contract.setTopicname(rs.getString("field0014"));
				contract.setBankaccount(rs.getString("field0031"));
				contract.setBankid(rs.getString("field0030"));
				contract.setMoneytype(rs.getString("field0062"));
				contract.setContractxz(rs.getString("SHOWVALUE"));
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterpartaddress(rs.getString("field0060"));//盖章后合同文本
				contract.setCounterparthank(rs.getString("field0061"));//合同原件
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				
				contract.setProjectstagegoal(rs.getString("field0007"));//合同文本附件
				contract.setCounterpartphone(rs.getString("field0019"));//招标文件附件
				contract.setCounterpartcode(rs.getString("field0024"));//中标文件附件
				
				contractList.add(contract);
				
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}
	
	private void delafzzcContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		BigDecimal orgid = null;
		TblStaff staff = null;
		List<TblCyhwProjectbudget> budgetList = null;
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		List<TblCyhwProjectbudget> conputerList = null ;//数据库存储的记录数
		TblCounterpartBankinfo bankInfo = null; //银行账户信息
		int i = 0;
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			
			unit.setContractid(RandomUtil.uuBigDecimalId());
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			
			//3.同步相对方信息
			budgetList = this.selectFzzcBudgetInfoByContrctid(unit.getHtoaid());
			for (TblCyhwProjectbudget budget : budgetList) {
				if(StringUtils.isBlank(budget.getBudgetname()) || unit.getOrgid() == null) {
					continue;
				}
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(budget.getBudgetname());
				if(conputerList != null){
					//直接查询相对方
					conputer.setOutsideId(budget.getOutsideId());
					this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(conputer, null);
					budget.setBudgetid(conputer.getBudgetid());
					log.info("该相对方已存在，相对方id："+budget.getBudgetid());
				}else {
					//新增相对方
					budget.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(budget);
					log.info("相对方维护成功：相对方Id："+budget.getBudgetid());
				}
				//维护相对方和银行账户的关系
				if(i == 0 && StringUtils.isNotBlank(unit.getBankaccount())) {
					bankInfo = this.tblCounterpartBankinfoMapper.selectBankInfoByAccount(unit.getBankaccount());
					if(bankInfo == null) {
						bankInfo = new TblCounterpartBankinfo();
						bankInfo.setBankaccount(unit.getBankaccount());
						bankInfo.setOutsideid(unit.getBankaccount());
						bankInfo.setBankkhyh(unit.getBankid());
						bankInfo.setBankstatus(new BigDecimal(1));
						bankInfo.setBanknature(new BigDecimal(1));
						bankInfo.setBankaccname(unit.getBankaccount());
						bankInfo.setBudgetid(budget.getBudgetid());
						bankInfo.setBankid(RandomUtil.uuBigDecimalId());
						this.tblCounterpartBankinfoMapper.saveBank(bankInfo);
						log.info("银行账号保存成功："+bankInfo.getBankaccount());
					}
				}
				i++;
				//保存合同和相对方的关联关系
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), budget.getBudgetid().toString(), budget.getBudgettype()!=null?budget.getBudgettype().length()>1?budget.getBudgettype().substring(0,1):budget.getBudgettype():null, unit.getContractname());
				log.info("相对方与合同关系维护成功");
			}
			
			//4.新增合同用印信息
			conputer = new TblCyhwProjectbudget();
			conputer.setCounterpartcode("公司公章");
			conputer.setCounterparthank(unit.getZxunitname());
			conputer.setRecordtype("HTGL003");
			conputer.setInspectionstatus(6);
			conputer.setFlowid(new BigDecimal(796216));
			conputer.setOrgid(unit.getOrgid());
			conputer.setRecordparent(unit.getContractid());
			conputer.setBudgetid(RandomUtil.uuBigDecimalId());
			this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(unit.getCounterpartaddress() != null && !"".equals(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setConstractId(unit.getContractid());
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
				
			}
		}
	}

	private List<TblCyhwUnit> selectFzzcContractInfo(int page) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0001,CONTRACT.field0014,CONTRACT.field0002,CONTRACT.field0009,CONTRACT.field0010,CONTRACT.field0012,CONTRACT.field0013,CONTRACT.field0019,CONTRACT.field0027,CONTRACT.field0028,CONTRACT.field0029,CONTRACT.field0008,CONTRACT.field0050,CONTRACT.field0024,CONTRACT.field0019,CONTRACT.field0007,CONTRACT.field0035,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名 FROM formmain_0944 CONTRACT LEFT JOIN view_user staff on CONTRACT.field0050 = staff.用户ID WHERE CONTRACT.ID NOT IN (SELECT TOP "+(page*100)+" ID FROM formmain_0944 ORDER BY ID ) ORDER BY CONTRACT.ID");
					
			rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0001"));
				contract.setContractname(rs.getString("field0002"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractmoney(rs.getBigDecimal("field0009")!=null?rs.getBigDecimal("field0009"):new BigDecimal(0));
				contract.setHzsumowing(rs.getString("field0010"));
				contract.setStartdate(rs.getDate("field0013"));
				contract.setEnddate(rs.getDate("field0012"));
				contract.setTopicname(rs.getString("field0014"));
				contract.setBankaccount(rs.getString("field0028"));
				contract.setBankid(rs.getString("field0027"));
				contract.setMoneytype("人民币");
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterpartaddress(rs.getString("field0035"));//盖章后合同文本
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				
				contract.setCounterpartphone(rs.getString("field0019"));//招标文件附件
				contract.setCounterpartcode(rs.getString("field0024"));//中标文件附件
				contractList.add(contract);
				
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}
	
	private List<TblCyhwProjectbudget> selectFzzcBudgetInfoByContrctid(String htoaid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwProjectbudget> budgetList = new ArrayList<TblCyhwProjectbudget>(0);
		TblCyhwProjectbudget budget = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT BUDGET.ID,BUDGET.sort,BUDGET.field0038 FROM formson_0945 BUDGET WHERE formmain_id = '"+htoaid+"' ORDER BY BUDGET.sort");
			rs = ps.executeQuery();
			while (rs.next()) {
				budget = new TblCyhwProjectbudget();
				budget.setBudgetname(rs.getString("field0038"));
				budget.setOutsideId(rs.getString("ID"));
				budget.setRecordtype("HTGL001");
				budget.setInspectionstatus(6);
				budget.setFlowid(new BigDecimal(796214));
				budgetList.add(budget);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return budgetList;
	}
	
	private void delaRzzlContractInfo(List<TblCyhwUnit> contractList) throws Exception {
		BigDecimal orgid = null;
		TblStaff staff = null;
		TblCyhwProjectbudget conputer = null ;//数据库存储的记录数
		List<TblCyhwProjectbudget> conputerList = null;
		int i = 0;
		List<TblContractContentPdf> pdfList = null;
		TblContractAppendixsigning signFile = null;
		for (TblCyhwUnit unit : contractList) {
			i = 0;
			if(StringUtils.isBlank(unit.getZxunitname())) {
				continue;
			}
			unit.setContracttype("其他合同");
			
			//1. 根据公司名称 获取所属公司
			orgid = this.tblOrganizationMapper.selectOrgIdByOrgName(unit.getZxunitname());
			unit.setOrgid(orgid);
			unit.setZxunit(orgid);
			
			//2.根据用户登录名 获取用户主键和 所属部门 对应放入 承办人和承办部门中
			staff = this.tblStaffMapper.selectStaffInfoByUsername(unit.getZxstaffname());
			if(staff != null) {
				unit.setContractstaff(staff.getStaffid());
				unit.setContractdept(staff.getOrgid());
				unit.setCreateuser(staff.getStaffid());
			}
			
			unit.setContractid(RandomUtil.uuBigDecimalId());
			this.tblCyhwUnitMapper.insert(unit);
			log.info("历史合同数据同步成功：合同ID-"+unit.getContractid());
			
			//3.同步相对方信息
			if(StringUtils.isNotBlank(unit.getMiblephone())){
				conputerList = this.tblCyhwProjectbudgetMapper.selectCountByOutSideIdCompany(unit.getMiblephone());
				if(conputerList == null){
					//新增相对方
					conputer = new TblCyhwProjectbudget();
					conputer.setBudgetname(unit.getMiblephone());
					conputer.setOutsideId(unit.getMiblephone());
					conputer.setRecordtype("HTGL001");
					conputer.setInspectionstatus(6);
					conputer.setFlowid(new BigDecimal(796214));
					conputer.setBudgetid(RandomUtil.uuBigDecimalId());
					this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
					log.info("相对方维护成功：相对方Id："+conputer.getBudgetid());
				}
				this.tblCyhwUnitMapper.insertContractBudget(unit.getContractid(), conputer.getBudgetid().toString(),"甲", unit.getContractname());
				
			}
			
			//4.新增合同用印信息
			conputer = new TblCyhwProjectbudget();
			conputer.setCounterpartcode("公司公章");
			conputer.setCounterparthank(unit.getZxunitname());
			conputer.setRecordtype("HTGL003");
			conputer.setInspectionstatus(6);
			conputer.setFlowid(new BigDecimal(796216));
			conputer.setOrgid(unit.getOrgid());
			conputer.setRecordparent(unit.getContractid());
			conputer.setBudgetid(RandomUtil.uuBigDecimalId());
			this.tblCyhwProjectbudgetMapper.insertOppositePartyOld(conputer);
			
			
			//5.维护合同附件  合同文本附件 \招标文件附件\中标文件附件\合同原件
			pdfList = this.selectGroupContractPdf(unit.getProjectstagegoal(),unit.getCounterpartphone(),unit.getCounterpartcode(),unit.getCounterparthank());
			for (TblContractContentPdf pdf : pdfList) {
				pdf.setConstractId(unit.getContractid());
				pdf.setContentPdfStatus(0);
				pdf.setContentPdfType(0);
				pdf.setContentPdfId(RandomUtil.uuBigDecimalId());
				this.tblContractContentPdfMapper.insert(pdf);
				log.info("合同附件维护成功：文件名称："+pdf.getContentPdfName());
			}
			
			//5.2 维护合同用印文件
			if(StringUtils.isNotBlank(unit.getCounterpartaddress())) {
				signFile = this.selectSignFileInf(unit.getCounterpartaddress());
				if(signFile != null) {
					signFile.setSingingId(RandomUtil.uuBigDecimalId());
					signFile.setConstractId(unit.getContractid());
					this.tblContractAppendixsigningMapper.insert(signFile);
					log.info("用印文件维护成功，文件名称："+ signFile.getSingingName());
				}	
			}
		}
		
	}

	private List<TblCyhwUnit> selectRzzlContractInfo(int page) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TblCyhwUnit> contractList = new ArrayList<>(0);
		TblCyhwUnit contract = null;
		try {
			con = BaseDaoSqlServer.getInstance().getGroupConnection();
			ps = con.prepareStatement("SELECT TOP 100 CONTRACT.ID,CONTRACT.field0001,CONTRACT.field0003,CONTRACT.field0004,CONTRACT.field0006,CONTRACT.field0008,CONTRACT.field0009,CONTRACT.field0016,CONTRACT.field0026,staff.用户ID,staff.登录名,staff.单位ID,staff.单位名称,staff.部门ID,staff.部门名 FROM formmain_3193 CONTRACT LEFT JOIN view_user staff on CONTRACT.field0008 = staff.用户ID WHERE CONTRACT.ID NOT IN (SELECT TOP "+(page*100)+" ID FROM formmain_3193 ORDER BY ID ) ORDER BY CONTRACT.ID");				
			rs = ps.executeQuery();
			while (rs.next()) {
				contract = new TblCyhwUnit();
				contract.setContractno(rs.getString("field0001"));
				contract.setContractname(rs.getString("field0003"));
				contract.setHtoaid(rs.getString("ID"));
				contract.setContractstatus(7);
				contract.setRecordtype("HTGL002");
				contract.setFlowid(new BigDecimal(796215));
				contract.setContractplan("否");
				contract.setCounterparthank(rs.getString("field0009"));//合同原件
				contract.setChoicejbunitid(rs.getString("部门ID"));
				contract.setMomoconcat(rs.getString("field0006"));
				contract.setChoicejbunitname(rs.getString("部门名"));
				contract.setZxunitid(rs.getString("单位ID"));
				contract.setZxunitname(rs.getString("单位名称"));
				contract.setZxstaffid(rs.getString("用户ID"));
				contract.setZxstaffname(rs.getString("登录名"));
				contract.setMiblephone(rs.getString("field0004")); //相对方甲方名称
				contractList.add(contract);
			}
		}finally {
			BaseDaoSqlServer.getInstance().close(con,rs,ps);
		}
		return contractList;
	}

}
