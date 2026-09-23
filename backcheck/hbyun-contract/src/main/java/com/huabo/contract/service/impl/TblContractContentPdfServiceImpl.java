package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblContractContentPdf;
import com.huabo.contract.mapper.TblContractContentPdfMapper;
import com.huabo.contract.service.TblContractContentPdfService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblContractContentPdfServiceImpl implements TblContractContentPdfService {

    @Resource
    private TblContractContentPdfMapper tblContractContentPdfMapper;

    
    @Override
    public Map<String, Object> findFileListByContractIdPageInfo(BigDecimal contractId, Integer pageNumber, Integer pageSize, TblContractContentPdf tca) {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 tca.setConstractId(contractId);
		 
		 IPage<TblContractContentPdf> page = new Page<TblContractContentPdf>(pageNumber,pageSize);
		 IPage<TblContractContentPdf> pageList = this.tblContractContentPdfMapper.findFileListByContractIdPageInfo(page,tca);
		 PageInfo<TblContractContentPdf> pageInfo = new PageInfo<TblContractContentPdf>();
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
    public TblContractContentPdf findInfoById(BigDecimal contentPdfId) {
    	return this.tblContractContentPdfMapper.findBycontentPdfId(contentPdfId);
    }
 
    
    @Override
    public void removeInfo(BigDecimal contentPdfId) {
    	this.tblContractContentPdfMapper.deleteBySingingId(contentPdfId);
    }

    @Override
    public Map<String, Object> saveEntity(TblContractContentPdf signing) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		signing.setContentPdfId(RandomUtil.uuBigDecimalId());
		this.tblContractContentPdfMapper.saveEntity(signing);
		resultMap.put("code", "1");
		resultMap.put("msg", "上传成功");
		resultMap.put("data", signing);
		return resultMap;
    }
    
    
    @Override
    public List<TblContractContentPdf> findInfoByContractId(BigDecimal contractId) {
    	return this.tblContractContentPdfMapper.findBycontractId(contractId);
    }
}
