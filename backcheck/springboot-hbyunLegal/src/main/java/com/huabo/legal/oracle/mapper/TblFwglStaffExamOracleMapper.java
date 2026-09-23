package com.huabo.legal.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.hbfk.util.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglStaffExamOracle;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.service.impl.TblFwglStaffExamOracleMapperConfig;
import com.huabo.legal.vo.TblFwglStaffExamParam;

import tk.mybatis.mapper.common.Mapper;

public interface TblFwglStaffExamOracleMapper extends Mapper<TblFwglStaffExamOracle> {

	@Insert("INSERT INTO TBL_FWGL_STAFF_EXAM VALUES (HIBERNATE_SEQUENCE.nextval,#{examId},#{staffId})")
	void addExamStaff(Long staffId, String examId);
	
	@SelectProvider(method="selectListByPageInfo",type=TblFwglStaffExamOracleMapperConfig.class)
	List<TblStaffOracle> selectListByPageInfo(PageInfo<TblStaffOracle> pageInfo, TblFwglStaffExamParam staffexamVo);
	
	@SelectProvider(method="selectListByPageInfoCount",type=TblFwglStaffExamOracleMapperConfig.class)
	Integer selectCountByPageInfo(PageInfo<TblStaffOracle> pageInfo, TblFwglStaffExamParam staffexamVo);
	
	@Select("SELECT * FROM TBL_FWGL_STAFF_EXAM  WHERE EXAMID = #{examId}")
	List<TblFwglStaffExamOracle> ExamStaffList(String examId);
	
	@Delete("delete from tbl_fwgl_staff_exam WHERE EXAMID = #{examId}")
	void deleteExamStaff(String examId);
	
	@Update("UPDATE TBL_FWGL_EXAM  SET STATE =#{state}  WHERE ID =#{examId}")
	int examUpdate(String examId,Integer state);
	
	@Select("SELECT COUNT(0) FROM TBL_FWGL_PAPER WHERE USERID = #{staffId} ")
	int selectPaper(String staffId);
	
	@Select("SELECT STATE FROM TBL_FWGL_EXAM  WHERE STATE = #{status}")
	int getExamStatus(String status);

	@Select("SELECT STAFFID FROM TBL_FWGL_STAFF_EXAM  WHERE STAFFID IN ((select regexp_substr(#{staffId},'[^,]+', 1, level) as id_arr from dual connect by level <= length(#{staffId})-length(regexp_replace(#{staffId}, ','))+1)) AND EXAMID = #{examId}")
	List<TblFwglStaffExamOracle> selectIn(String staffId,String examId);

}
