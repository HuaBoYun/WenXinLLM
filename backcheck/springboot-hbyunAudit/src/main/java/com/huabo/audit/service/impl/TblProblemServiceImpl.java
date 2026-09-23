package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.huabo.audit.oracle.entity.TblProblemEntity;
import com.huabo.audit.service.TblProblemService;
@Service
public class TblProblemServiceImpl implements TblProblemService {

	@Override
	public void add(TblProblemEntity tblProblem) {
		// TODO Auto-generated method stub

	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblProblemEntity findById(BigDecimal id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(BigDecimal id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modify(TblProblemEntity tblProblem) {
		// TODO Auto-generated method stub

	}

	@Override
	public List search(String problemname, String businessname, String finder_disp1, String startdt, String enddt,
			String finder_disp2, String ocurrdt_min, String ocurrdt_max, String sort, String sort_type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(String problemnum, String proborgs, String problemname, String bussinessbelongto,
			String riskbelongsto, String probfrom, String discoveryperson, String personincharge, String sort,
			String sort_type, String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findByneed(String orgid, String orgtype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAllReform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findByneed(String number, String fstart, String fend, String uname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblProblemEntity> findBysql(String sql) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Object> getAllReformProBySlouidNbsjCXList(TblProblemEntity pro, String solutionid, String orgid,
			String orgtype) {
		// TODO Auto-generated method stub
		return null;
	}


}
