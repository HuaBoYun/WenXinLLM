package com.huabo.contract.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.contract.mapper.TblInvestmentProjectMapper;
import com.huabo.contract.service.TblInvestmentProjectService;

import lombok.extern.slf4j.Slf4j;

@Service("TblInvestmentProjectServiceImpl")
@Slf4j
public class TblInvestmentProjectServiceImpl implements TblInvestmentProjectService {
 
	@Resource
	private TblInvestmentProjectMapper  tblInvestmentProjectMapper;

	public void saveTblInvestmentProject(String id, String no, String name)
			throws Exception {
			try {
				int size = tblInvestmentProjectMapper.selectTblInvestmentProject(id);
				if (size > 0) {
					tblInvestmentProjectMapper.updateTblInvestmentProject(id, no, name);
				} else {
					tblInvestmentProjectMapper.insertTblInvestmentProject(id, no, name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void updateTblInvestmentProject(String id, String no, String name)
			throws Exception {
			try {
				tblInvestmentProjectMapper.updateTblInvestmentProject(id, no, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
