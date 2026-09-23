package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.mapper.TblContractInvoicesmanagemenMapper;
import com.huabo.contract.service.TblContractInvoicesmanagemenService;

@Service
public class TblContractInvoicesmanagemenServiceImpl implements TblContractInvoicesmanagemenService {

	@Autowired
	private UserProvider userProvider;
	
    @Resource
    private TblContractInvoicesmanagemenMapper tblContractInvoicesmanagemenMapper;
    
    
	@Override
	public Map<String, Object> findInvoiceInfoListForCollection(Integer pageNumber, Integer pageSize,
			BigDecimal budgetId, TblContractInvoicesmanagemen invoice) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		
		TblStaffUtil staff = userProvider.get();
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		invoice.setInvoiceogr(pid);
		TblCyhwProjectbudget budget = new TblCyhwProjectbudget();
		budget.getBudgetid();
		invoice.setBudget(budget);
		invoice.setBudgetId(budgetId);
		
		IPage<TblContractInvoicesmanagemen> page = new Page<TblContractInvoicesmanagemen>(pageNumber,pageSize);
		IPage<TblContractInvoicesmanagemen> pageList = tblContractInvoicesmanagemenMapper.findInvoiceInfoListForCollection(page, invoice);
		
		PageInfo<TblContractInvoicesmanagemen> pageInfo = new PageInfo<TblContractInvoicesmanagemen>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		 //分页查询结果添加
        pageInfo.setTlist(pageList.getRecords());
        //总条数
        pageInfo.setTotalRecord((int) pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("date", pageInfo);
		return resultMap;
	}


	@Override
	public void findContractInvociesManaeMenPageInfo(PageInfo<TblContractInvoicesmanagemen> pageInfo,
			TblContractInvoicesmanagemen invoice) throws Exception {
		IPage<TblContractInvoicesmanagemen> page = new Page<TblContractInvoicesmanagemen>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblContractInvoicesmanagemen> pageList = tblContractInvoicesmanagemenMapper.findContractInvociesManaeMenPageInfo(page, invoice);
		
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}


	@Override
	public String mengerInvoicemanageMen(BigDecimal budgetId, String startdate1, String enddate1,
			TblContractInvoicesmanagemen invoice) throws Exception {
		
		TblStaffUtil staff = userProvider.get();
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		BigDecimal staffid= staff.getStaffid();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if(invoice.getInvoiceid() != null) {
			TblContractInvoicesmanagemen oldInvoice = this.tblContractInvoicesmanagemenMapper.getInvoiceid(invoice.getInvoiceid());
			Integer count = this.tblContractInvoicesmanagemenMapper.selectListCount(oldInvoice);
			if (count > 0) {
				return JsonBean.error("-1");
            }
			oldInvoice.setInvoiceogr(orgid);
			oldInvoice.setCreatestaff(staffid);
			oldInvoice.setInvoicedate(df.parse(startdate1));
			oldInvoice.setInvoicespdate(df.parse(enddate1));
			oldInvoice.setInvoiceheadtext(invoice.getInvoiceheadtext());
			oldInvoice.setInvoiceno(invoice.getInvoiceno());
			oldInvoice.setInvoicemoney(invoice.getInvoicemoney());
			oldInvoice.setInvoicepost(invoice.getInvoicepost());
			oldInvoice.setInvoicecontent(invoice.getInvoicecontent());
			oldInvoice.setInvoicetype(invoice.getInvoicetype());
			oldInvoice.setInvoicestatus(invoice.getInvoicestatus());
			oldInvoice.setInvoicesporg(invoice.getInvoicesporg());
			oldInvoice.setInvoicekporg(invoice.getInvoicekporg());
			oldInvoice.setTinumber(invoice.getTinumber());
			oldInvoice.setBudgetId(budgetId);
			this.tblContractInvoicesmanagemenMapper.updateById(oldInvoice);
			return JsonBean.success("修改成功");
		}else {
			invoice.setInvoiceogr(orgid);
			invoice.setCreatestaff(staffid);
			invoice.setInvoicedate(df.parse(startdate1));
			invoice.setInvoicespdate(df.parse(enddate1));
			invoice.setInvoiceid(RandomUtil.uuBigDecimalId());
			invoice.setBudgetId(budgetId);
			this.tblContractInvoicesmanagemenMapper.insert(invoice);
			return JsonBean.success("新增成功");
		}
	}


	@Override
	public void modifyInvoiceStatus(BigDecimal invoiceId, Integer status) throws Exception {
		this.tblContractInvoicesmanagemenMapper.updateModifyInvoiceStatus(invoiceId, status);
	}


	@Override
	public void removeInvoiceInfo(BigDecimal invoiceId) throws Exception {
		this.tblContractInvoicesmanagemenMapper.removeInvoiceInfo(invoiceId);
	}


	@Override
	public TblContractInvoicesmanagemen findInvoiceInfoByInvoiceId(BigDecimal invoiceId) throws Exception {
		return this.tblContractInvoicesmanagemenMapper.getInvoiceid(invoiceId);
	}

}
