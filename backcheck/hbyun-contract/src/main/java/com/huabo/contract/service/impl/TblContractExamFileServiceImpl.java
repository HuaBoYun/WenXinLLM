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
import com.huabo.contract.entity.TblContractExamFile;
import com.huabo.contract.mapper.TblContractExamFileMapper;
import com.huabo.contract.service.TblContractExamFileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblContractExamFileServiceImpl implements TblContractExamFileService {

    @Resource
    private TblContractExamFileMapper tblContractExamFileMapper;

    
    @Override
    public Map<String, Object> findFileListByContractIdPageInfo(BigDecimal contractId, Integer pageNumber, Integer pageSize, TblContractExamFile file) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		file.setConstractId(contractId);
		
		IPage<TblContractExamFile> page = new Page<TblContractExamFile>(pageNumber,pageSize);
		IPage<TblContractExamFile> pageList = this.tblContractExamFileMapper.findFileListByContractIdPageInfo(page,file);
		PageInfo<TblContractExamFile> pageInfo = new PageInfo<TblContractExamFile>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setCondition(file);
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
		resultMap.put("code", "1");
		resultMap.put("msg", "数据访问成功");
		resultMap.put("data", pageInfo);
		return resultMap;
    }

    @Override
    public TblContractExamFile findInfoById(BigDecimal contentId) {
    	return this.tblContractExamFileMapper.findBycontentId(contentId);
    }
 
    
    @Override
    public void removeInfo(BigDecimal contentId) {
    	this.tblContractExamFileMapper.deleteById(contentId);
    }

    @Override
    public Map<String, Object> saveEntity(TblContractExamFile signing) {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 signing.setId(RandomUtil.uuBigDecimalId());
		 this.tblContractExamFileMapper.saveEntity(signing);
		 resultMap.put("code", "1");
		 resultMap.put("msg", "上传成功");
		 resultMap.put("data", signing);
		 return resultMap;
    }
    
    
    @Override
    public List<TblContractExamFile> findInfoByContractId(BigDecimal contractId) {
    	return this.tblContractExamFileMapper.findBycontractId(contractId);
    }
}
