package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblLegalExecumgr;
import com.huabo.contract.mapper.TblLegalExecumgrMapper;
import com.huabo.contract.service.TblLegalExecumgrService;
import com.huabo.contract.service.TblOrganizaService;

@Service
public class TblLegalExecumgrServiceImpl implements TblLegalExecumgrService{
	
	@Resource
    private TblLegalExecumgrMapper tblLegalExecumgrMapper;

	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Override
	public void findListByPage(PageInfo<TblLegalExecumgr> pageInfo, BigDecimal litigationid, BigDecimal arbitraid,TblLegalExecumgr tla) {
		try {
			String companyIds = tblOrganizaService.selectChidrenIdStrsByFatherOrgId(tla.getOrgid().toString());
			
			IPage<TblLegalExecumgr> page = new Page<TblLegalExecumgr>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
			IPage<TblLegalExecumgr> pageList = tblLegalExecumgrMapper.findListByPage(page, litigationid, arbitraid,tla,companyIds);
			
			//findListByPage分页查询
			pageInfo.setTlist(pageList.getRecords());
			//findListByPageCount查询总条数
			pageInfo.setTotalRecord((int)pageList.getTotal());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void findListBydisputeid( BigDecimal disputeid,Map<String,Object> resultMap) {
		List<TblLegalExecumgr> list=tblLegalExecumgrMapper.findListBydisputeid(disputeid);
        resultMap.put("date", list);
	}
	

	@Override
	public void addLegalExecumgr(TblLegalExecumgr tla) {
		BigDecimal id = tla.getId();
		if(null == id) {
			//新增
			tla.setId(RandomUtil.uuBigDecimalId());
			this.tblLegalExecumgrMapper.addEntity(tla);
		}else {
			//修改
			this.tblLegalExecumgrMapper.updateEntity(tla);
		}
	}

	@Override
	public TblLegalExecumgr getLegalExecumgrById(BigDecimal id) {
		return this.tblLegalExecumgrMapper.findById(id);
	}

	@Override
	public void deleteLegalExecumgrById(BigDecimal id) {
		this.tblLegalExecumgrMapper.deleteEntityById(id);
		
	}
}
