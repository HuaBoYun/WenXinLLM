package com.huabo.system.mapper;

import com.huabo.system.entity.TblUserRolerelation;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户角色关系表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2024-12-30
 */
public interface TblUserRolerelationMapper extends BaseMapper<TblUserRolerelation> {

	@Select("SELECT ROLEID FROM TBL_USER_ROLERELATION WHERE STAFFID = #{staffId} AND (ENDTIME IS NULL OR ENDTIME = NULL) ")
	List<String> selectRoleIdsByStaffIsUse(@Param("staffId")BigDecimal staffId) throws Exception;

}
