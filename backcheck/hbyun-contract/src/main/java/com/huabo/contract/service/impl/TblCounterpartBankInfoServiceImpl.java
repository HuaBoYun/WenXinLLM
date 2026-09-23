package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.mapper.TblCounterpartBankinfoMapper;
import com.huabo.contract.service.TblCounterpartBankInfoService;

@Service
public class TblCounterpartBankInfoServiceImpl implements TblCounterpartBankInfoService {

	@Autowired
	private UserProvider userProvider;
	
    @Resource
    private TblCounterpartBankinfoMapper tblCounterpartBankinfoMapper;

    
    @Override
    public Map<String, Object> findAllListByBankInfo(BigDecimal budgetId, Integer pageNumber, Integer pageSize, TblCounterpartBankinfo bank) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 // 设置银行的预算ID
		 bank.setBudgetid(budgetId);
		 
		 IPage<TblCounterpartBankinfo> page = new Page<TblCounterpartBankinfo>(pageNumber, pageSize);
		 IPage<TblCounterpartBankinfo> pageList = tblCounterpartBankinfoMapper.findAllListByBankInfo(page,bank);
		 
		 
		 // 创建一个PageInfo对象，用于存储分页信息和查询结果
		 PageInfo<TblCounterpartBankinfo> pageInfo = new PageInfo<TblCounterpartBankinfo>();
		 pageInfo.setPageSize(pageSize);
		 pageInfo.setCurrentPage(pageNumber);
		 pageInfo.setCondition(bank);
		 // 调用tblCounterpartBankinfoMapper的方法查询银行信息列表和总记录数
		 pageInfo.setTlist(pageList.getRecords());
		 pageInfo.setTotalRecord((int) pageList.getTotal());
		 // 设置结果Map的属性
		 resultMap.put("code", "1");
		 resultMap.put("msg", "成功");
		 resultMap.put("data", pageInfo);
		 return resultMap;
    }


	@Override
	public Map<String, Object> saveCounterPartBankInfo(BigDecimal budgetId, TblCounterpartBankinfo bank) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            // 如果用户令牌无效，设置结果Map的属性并返回
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
        // 判断银行ID是否存在，如果存在则更新银行信息，否则插入新的银行记录
        if (bank.getBankid() != null) {
            this.tblCounterpartBankinfoMapper.updateById(bank);
        } else {
            bank.setBudgetid(budgetId);
            bank.setCreatetime(new Date());
            bank.setCreatestaff(staff.getStaffid());
            bank.setBankid(RandomUtil.uuBigDecimalId());
            this.tblCounterpartBankinfoMapper.insert(bank);
        }
        // 设置结果Map的属性
        resultMap.put("code", "1");
        resultMap.put("msg", "保存成功");
        resultMap.put("data", bank);
        return resultMap;
	}
	
	@Override
    public Map<String, Object> removeBank(String bankId) throws Exception {
		  Map<String, Object> resultMap = new HashMap<String, Object>(0);
		  // 调用tblCounterpartBankinfoMapper.selectCountByUser方法检查银行账户是否被使用
		  Integer collectionCount = this.tblCounterpartBankinfoMapper.selectCountByUser(bankId);
		  Integer paymentCount = this.tblCounterpartBankinfoMapper.selectpaymentCountByBankId(bankId);
		  if ((collectionCount+paymentCount) > 0) {
		      resultMap.put("code", "0");
		      resultMap.put("msg", "该账户已使用，无法删除");
		      resultMap.put("data", "");
		      return resultMap;
		  }
		  // 调用tblCounterpartBankinfoMapper.removeBank方法删除银行账户
		  this.tblCounterpartBankinfoMapper.removeBank(bankId);
		  // 设置结果Map的属性
		  resultMap.put("code", "1");
		  resultMap.put("msg", "删除成功");
		  resultMap.put("data", "");
		  return resultMap;
    }
	
	@Override
    public Map<String, Object> modifyBankStatus(String bankId, Integer bankstatus) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 // 调用tblCounterpartBankinfoMapper.updateBankStatus方法更新银行状态
		 this.tblCounterpartBankinfoMapper.modifyBankStatus(bankId, bankstatus);
		 // 设置结果Map的属性
		 resultMap.put("code", "1");
		 resultMap.put("msg", "更新成功");
		 resultMap.put("data", "");
		 return resultMap;
    }


	@Override
	public Map<String, Object> findListByPageInfo(PageInfo<TblCounterpartBankinfo> pageInfo,
			TblCounterpartBankinfo bank) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		IPage<TblCounterpartBankinfo> page = new Page<TblCounterpartBankinfo>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblCounterpartBankinfo> pageList = tblCounterpartBankinfoMapper.findListByPageInfo(page, bank);
        //分页查询结果存入Tlist
        pageInfo.setTlist(pageList.getRecords());
        //设置查询记录总数
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
	}

}
