package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.huabo.audit.oracle.entity.TblZgzzRectificationimpl;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 整改落实落实信息表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-25
 */
public interface TblZgzzRectificationimplMapper extends Mapper<TblZgzzRectificationimpl> {

	@Insert("INSERT INTO TBL_RECTIFICATIONIMPL_ATT(IMPLID,ATTID) VALUES(#{implId},#{attId})")
	void insertAttRelation(@Param("implId")String implId,@Param("attId") String attId) throws Exception;

	@Delete("DELETE FROM TBL_RECTIFICATIONIMPL_ATT WHERE IMPLID = #{implId} AND ATTID = #{attId}")
	void deleteAttRelation(@Param("implId") String implId,@Param("attId") String attId);

	@Select("SELECT TZR.*,CTS.REALNAME AS CREATESTAFFNAME FROM TBL_ZGZZ_RECTIFICATIONIMPL TZR LEFT JOIN TBL_STAFF CTS ON TZR.CREATESTAFF = CTS.STAFFID WHERE TZR.RELAID = #{relaId} ORDER BY TZR.CREATETIME ASC")
	List<TblZgzzRectificationimplVo> selectEntityByRelaId(@Param("relaId") String relaId) throws Exception;

	@Select("SELECT TZR.*,CTS.REALNAME AS CREATESTAFFNAME FROM TBL_ZGZZ_RECTIFICATIONIMPL TZR LEFT JOIN TBL_STAFF CTS ON TZR.CREATESTAFF = CTS.STAFFID WHERE TZR.IMPLID = #{implId} AND ROWNUM = 1 ORDER BY TZR.CREATETIME ASC")
	TblZgzzRectificationimplVo selectEntityByImplId(@Param("implId") String implId) throws Exception;

}
