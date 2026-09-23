package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblOrgBankaccount;
import com.huabo.contract.mapper.TblOrgBankaccountMapper;
import com.huabo.contract.service.TblOrgBankAccountService;


@Service
public class TblOrgBankAccountServiceImpl implements TblOrgBankAccountService {

	@Autowired
	private UserProvider userProvider;
	
    @Resource
    private TblOrgBankaccountMapper tblOrgBankaccountMapper;

	@Override
	public Map<String, Object> findListByPageInfo(Integer pageNumber, Integer pageSize, TblOrgBankaccount bank)
			throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		
		bank.setBankstatus(BigDecimal.valueOf(1));
		bank.setBankstate(BigDecimal.valueOf(0));
		bank.setOrgid(pid);
		IPage<TblOrgBankaccount> page = new Page<TblOrgBankaccount>(pageNumber,pageSize);
		IPage<TblOrgBankaccount> pageList = tblOrgBankaccountMapper.findPlanNodeListForCollection(page, bank);
		
		PageInfo<TblOrgBankaccount> pageInfo = new PageInfo<TblOrgBankaccount>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setCondition(bank);
		 //分页查询结果
        pageInfo.setTlist(pageList.getRecords());
        //总条数
        pageInfo.setTotalRecord((int) pageList.getTotal());
		resultMap.put("date", pageInfo);
		return resultMap;
	}

	@Override
	public TblOrgBankaccount findById(BigDecimal bankId) throws Exception {
		return tblOrgBankaccountMapper.findByBanId(bankId);
	}

	@Override
	public void findListByPageInfoPid(PageInfo<TblOrgBankaccount> pageInfo, BigDecimal pid, TblOrgBankaccount bank)
			throws Exception {
		IPage<TblOrgBankaccount> page = new Page<TblOrgBankaccount>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblOrgBankaccount> pageList =  tblOrgBankaccountMapper.findListByPageInfoPid(page, pid, bank);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
		
	}

	@Override
	public TblOrgBankaccount findByBankId(BigDecimal bankid) throws Exception {
		return tblOrgBankaccountMapper.findByBankId(bankid);
	}

	@Override
	public void UpdateModifyBankInfo(TblOrgBankaccount oldEntity) throws Exception {
		 tblOrgBankaccountMapper.updateById(oldEntity);
	}

	@Override
	public void savebankInfo(TblOrgBankaccount bank) throws Exception {
		tblOrgBankaccountMapper.insert(bank);
	}

	@Override
	public String removeOrgBankInfo(BigDecimal bankId) throws Exception {
		return tblOrgBankaccountMapper.removeOrgBankInfo(bankId);
	}

    
}
