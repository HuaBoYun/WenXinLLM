package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblReportBug;
import com.huabo.monitor.mapper.TblReportBugMapper;
import com.huabo.monitor.service.ITblReportBugService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

 
@Service
public class TblReportBugServiceImpl extends ServiceImpl<TblReportBugMapper, TblReportBug> implements ITblReportBugService {

	 
	@Override
	public void saveEntity(TblReportBug entity) throws Exception{
		try {
			this.baseMapper.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}
	@Override
	public  void deleteReportBug(BigDecimal bugId,BigDecimal reportId)throws Exception{
		try {
			this.baseMapper.deleteByEntityId(reportId, bugId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	
	
	@Override
	public List<TblReportBug> getListByReportid(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		List<TblReportBug>  list=null;
		try {
			list=this.baseMapper.getListByReportid(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void deleteByReportId(BigDecimal reportId) throws Exception {
		// TODO Auto-generated method stub
		try {
			this.baseMapper.deleteByReportId(reportId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}



}
