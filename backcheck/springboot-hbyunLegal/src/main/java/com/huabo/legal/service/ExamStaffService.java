package com.huabo.legal.service;

import java.lang.reflect.InvocationTargetException;

import com.hbfk.util.JsonBean;
import com.huabo.legal.vo.TblFwglStaffExamParam;

public interface ExamStaffService {

	public JsonBean addExamStaff(TblFwglStaffExamParam staffexamVo) throws IllegalAccessException, InvocationTargetException;

	public JsonBean ExamStaffInfo(TblFwglStaffExamParam staffexamVo,Integer pageNumber,Integer pageSize);

	public JsonBean ExamStaffList(TblFwglStaffExamParam staffexamVo) throws IllegalAccessException, InvocationTargetException;

	public JsonBean examStaffDelete(String staffId);

	public JsonBean examUpdate(TblFwglStaffExamParam staffexamVo) throws IllegalAccessException, InvocationTargetException;

	public Integer getExamStatus(String examId);
}
