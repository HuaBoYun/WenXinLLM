package com.huabo.legal.service.impl;

import cn.hutool.core.convert.Convert;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.TblFwglStaffExamOracle;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.oracle.mapper.TblFwglStaffExamOracleMapper;
import com.huabo.legal.service.ExamStaffService;
import com.huabo.legal.vo.TblFwglStaffExamParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamStaffServiceImpl implements ExamStaffService {
	@Autowired
	TblFwglStaffExamOracleMapper tblFwglStaffExamOracleMapper;

	@Override
	public JsonBean examUpdate(TblFwglStaffExamParam staffexamVo) {
		tblFwglStaffExamOracleMapper.examUpdate(staffexamVo.getExamId(), staffexamVo.getState());
		return ResponseFormat.retParam(1, 200, "考试状态修改成功");
	}

	@Override
	public JsonBean examStaffDelete(String staffId) {
		Integer staff = tblFwglStaffExamOracleMapper.selectPaper(staffId);
		if (staff > 0) {
			return ResponseFormat.retParam(0, "删除失败，此用户有正在进行中考试", null);
		}
		TblFwglStaffExamOracle tblFwglStaffExamOracle = new TblFwglStaffExamOracle();
		tblFwglStaffExamOracle.setStaffId(Long.valueOf(staffId));
		tblFwglStaffExamOracleMapper.delete(tblFwglStaffExamOracle);
		return ResponseFormat.retParam(1, "删除成功", null);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean addExamStaff(TblFwglStaffExamParam staffexamVo) {
		//前端传值
		Long[] staffIds = Convert.toLongArray(staffexamVo.getStaffId().split(","));
		System.out.println(staffexamVo.getStaffId());
		//数据库查询id
		List<TblFwglStaffExamOracle> list = tblFwglStaffExamOracleMapper.selectIn(staffexamVo.getStaffId(), staffexamVo.getExamId());
		//数据库已有id
		List<Long> list2 = list.stream().map(TblFwglStaffExamOracle::getStaffId).collect(Collectors.toList());
		//对比去除重复数据
		List<Long> aIntegers = Arrays.stream(staffIds).filter(o -> !list2.contains(o)).collect(Collectors.toList());
		for (Long staffId : aIntegers) {
			tblFwglStaffExamOracleMapper.addExamStaff(staffId, staffexamVo.getExamId());
		}
		return ResponseFormat.retParam(1, 200, "下发成功");
	}

	@Override
	public JsonBean ExamStaffList(TblFwglStaffExamParam staffexamVo) {
		return ResponseFormat.retParam(1, 200, tblFwglStaffExamOracleMapper.ExamStaffList(staffexamVo.getExamId()));
	}

	@Override
	public JsonBean ExamStaffInfo(TblFwglStaffExamParam staffexamVo, Integer pageNumber, Integer pageSize) {
		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		PageInfo<TblStaffOracle> pageInfo = new PageInfo<TblStaffOracle>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblFwglStaffExamOracleMapper.selectListByPageInfo(pageInfo, staffexamVo));
		pageInfo.setTotalRecord(this.tblFwglStaffExamOracleMapper.selectCountByPageInfo(pageInfo, staffexamVo));
		pageInfo.getTotalPage();
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public Integer getExamStatus(String examId) {
		return tblFwglStaffExamOracleMapper.getExamStatus(examId);
	}

}
