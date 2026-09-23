package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblContractAppendixsigning;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.mapper.TblContractAppendixsigningMapper;
import com.huabo.contract.mapper.TblCyhwProjectbudgetMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.service.TblContractAppendixsigningService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblContractAppendixsigningServiceImpl implements TblContractAppendixsigningService {

    @Resource
    private TblContractAppendixsigningMapper contractAppendixsigningMapper;

    @Resource
    private TblCyhwUnitMapper tblCyhwUnitMapper;
    
    @Resource
	private TblCyhwProjectbudgetMapper tblCyhwProjectbudgetMapper;
    
    @Override
    public Map<String, Object> findFileListByContractIdPageInfo(BigDecimal contractId, Integer pageNumber, Integer pageSize, TblContractAppendixsigning tca) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		tca.setConstractId(contractId);
		
		IPage<TblContractAppendixsigning> page = new Page<TblContractAppendixsigning>(pageNumber,pageSize);
		IPage<TblContractAppendixsigning> pageList = this.contractAppendixsigningMapper.findFileListByContractIdPageInfo(page,tca);
		
		PageInfo<TblContractAppendixsigning> pageInfo = new PageInfo<TblContractAppendixsigning>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setCondition(tca);
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
		resultMap.put("code", "1");
		resultMap.put("msg", "数据访问成功");
		resultMap.put("data", pageInfo);
		return resultMap;
    }

    @Override
    public TblContractAppendixsigning findInfoById(BigDecimal singingId) {
    	return this.contractAppendixsigningMapper.findBysingingId(singingId);
    }
 
    
    @Override
    public void removeInfo(BigDecimal singingId) {
    	this.contractAppendixsigningMapper.deleteBySingingId(singingId);
    }

    @Override
    public Map<String, Object> saveEntity(TblContractAppendixsigning signing,BigDecimal budgetid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
			try {
				signing.setSingingId(RandomUtil.uuBigDecimalId());
				this.contractAppendixsigningMapper.saveEntity(signing);
				//tblCyhwUnitMapper.modifyContractStatus(	signing.getConstractId(), "7");
				TblCyhwProjectbudget tcpb=new TblCyhwProjectbudget();
				tcpb.setBudgetid(budgetid);
				tcpb.setInspectionstatus(6);
				this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(tcpb,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
            resultMap.put("code", "1");
            resultMap.put("msg", "上传成功");
            resultMap.put("data", signing);
            return resultMap;
    }
}
