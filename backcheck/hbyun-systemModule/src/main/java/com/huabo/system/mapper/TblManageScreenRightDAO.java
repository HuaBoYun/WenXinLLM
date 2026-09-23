package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblManageScreenRight;


public interface TblManageScreenRightDAO extends BaseMapper<TblManageScreenRight> {

	@Delete("DELETE FROM TBL_SCREEN_ROLE_RIGHT WHERE ROLEID =  #{roleId}")
	void deleteRoleId(String roleId);
	
	@Insert("INSERT INTO TBL_SCREEN_ROLE_RIGHT (ROLEID,RIGHTID) VALUES ( #{roleId},#{rightId})")
	void insertRoleId(@Param("roleId")String roleId,@Param("rightId") String rightId);
	
	@Select("SELECT DISTINCT TMSR.RIGHTID,TMSR.RIGHTNAME,TMSR.RIGHTCODE,TMSR.IMGRUL,TMSR.RIGHTTYPE,TMSR.FATHERID,TMSR.RIGHTORDER,TMSR.RITHTMEMO,TMSR.RIGHTSTATUS FROM TBL_MANAGE_SCREEN_RIGHT TMSR LEFT JOIN TBL_SCREEN_ROLE_RIGHT TSRR ON TMSR.RIGHTID = TSRR.RIGHTID  AND TSRR.ROLEID IN (${roleIdStrs}) WHERE TMSR.FATHERID = #{rightId} AND TSRR.ROLEID IN (${roleIdStrs}) AND TMSR.RIGHTSTATUS = 1")
	List<TblManageScreenRight> selectChildrenRightListByRole(@Param("rightId")BigDecimal rightId,@Param("roleIdStrs")String roleIdStrs);
	 
	//新方法 
	 
    @Delete("DELETE FROM TBL_MANAGE_USER_SCREEN WHERE STAFFID =  #{userid}")
    void deleteUserId(String userid);
    @Insert("INSERT INTO TBL_MANAGE_USER_SCREEN (STAFFID,RIGTHID) VALUES ( #{userid},#{pri})")
    void insertUserId(@Param("userid") String userid, @Param("pri")String pri);

    @Select("SELECT TMSR.RIGHTID,TMSR.RIGHTNAME,TMSR.FATHERID, (SELECT STAFFID FROM TBL_MANAGE_USER_SCREEN WHERE RIGTHID = TMSR.RIGHTID AND STAFFID = #{staffid} ) STAFFID  FROM TBL_MANAGE_SCREEN_RIGHT TMSR WHERE TMSR.RIGHTSTATUS = 1 AND TMSR.FATHERID = -1")
    List<TblManageScreenRight> findListByGetRight(BigDecimal staffid);

    @Select("SELECT TMSR.RIGHTID,TMSR.RIGHTNAME,TMSR.FATHERID,(SELECT STAFFID FROM TBL_MANAGE_USER_SCREEN WHERE RIGTHID = TMSR.RIGHTID AND STAFFID = #{staffid}) STAFFID  FROM TBL_MANAGE_SCREEN_RIGHT TMSR WHERE TMSR.RIGHTSTATUS = 1 AND TMSR.FATHERID = #{rightId}")
    List<TblManageScreenRight> findListByGet(BigDecimal staffid, BigDecimal rightId);
    
    @Select("SELECT TMSR.RIGHTID,TMSR.RIGHTNAME,TMSR.FATHERID  FROM TBL_MANAGE_SCREEN_RIGHT TMSR WHERE TMSR.RIGHTSTATUS = 1 AND TMSR.FATHERID = #{rightId}")
    List<TblManageScreenRight> selectAllRightListByRole(BigDecimal rightId);
    
    @Select("SELECT COUNT(0) FROM TBL_SCREEN_ROLE_RIGHT WHERE RIGHTID = #{rightId} AND ROLEID = #{roleId}")
	Integer judgeScreenRightByRoleId(@Param("rightId")BigDecimal rightId,@Param("roleId") BigDecimal roleId);
}
