package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.huabo.audit.oracle.entity.TblZgzzRctevaluation;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzRctevaluationVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 整改评价信息表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-28
 */
public interface TblZgzzRctevaluationMapper extends Mapper<TblZgzzRctevaluation> {

	/**
	 * 通过方案主键 获取已填写评价的整改清单的落实信息
	 * @param planId 整改方案主键
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT COUNT(0) FROM TBL_ZGZZ_RCTEVALUATION WHERE IMPLID IN ( SELECT IMPLID FROM TBL_ZGZZ_RECTIFICATIONIMPL WHERE RELAID IN ( "
			+ " SELECT TRI.RELAID FROM TBL_RECTIFICATION_ISSUES TRI INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION "
			+ " WHERE TRI.PLANID = #{planId} ) )")
	Integer selectAllocatedList(@Param("planId") String planId) throws Exception;

	@Insert("INSERT INTO TBL_RECTVAL_ATT(EVALID,ATTID) VALUES (#{evalId},#{attId})")
	void insertAttRelation(@Param("evalId")String evalId,@Param("attId") String attId) throws Exception;

	@Delete("DELETE FROM TBL_RECTVAL_ATT WHERE ATTID = #{attId} AND EVALID = #{evalId}")
	void deleteAttRelation(@Param("attId")String attId,@Param("evalId") String evalId) throws Exception;

	@Select("SELECT * FROM TBL_ZGZZ_RCTEVALUATION WHERE EVALID = #{evalId}")
	TblZgzzRctevaluationVo selectEntityById(@Param("evalId") String evalId);

	@Select("SELECT * FROM TBL_ZGZZ_RCTEVALUATION WHERE IMPLID = #{implId}")
	TblZgzzRctevaluationVo selectEntityByImplId(@Param("implId")String implId) throws Exception;

}
