package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblRoleMySql;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 角色表管理
 * rid:主键ID,自动增长；
 * rname:角色名称；
 * rdesc:角色描述；
 * rstatus:角色状态，是否启用 Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblRoleMySqlMapper extends BaseMapper<TblRoleMySql> {

	@Results({
		@Result(column="RID",property="rid"),
		@Result(column="RNAME",property="rname"),
		@Result(column="RDESC",property="rdesc"),
		@Result(column="RSTATUS",property="rstatus"),
		@Result(column="PKYMORGID",property="pkYmOrgId"),
		@Result(column="PKYMROLEID",property="pkYmRoleId"),
		@Result(column="ORGNAME",property="orgName"),
	})
	@SelectProvider(method = "selectAllRoleListToYM", type = TblRoleMySqlMapperConfig.class)
	List<TblRoleMySql> selectAllRoleListToYM(Integer orgId) throws Exception;

	@Update("UPDATE TBL_ROLE SET PKYMROLEID = #{pkYmRoleId} WHERE RID = #{rid}")
	void updatePkYmbyRoleId(String pkYmRoleId, BigDecimal rid) throws Exception;

	@Results({
		@Result(column="RID",property="rid"),
		@Result(column="RNAME",property="rname"),
		@Result(column="RDESC",property="rdesc"),
		@Result(column="RSTATUS",property="rstatus"),
		@Result(column="PKYMORGID",property="pkYmOrgId"),
		@Result(column="PKYMROLEID",property="pkYmRoleId"),
		@Result(column="ORGNAME",property="orgName"),
	})
	@SelectProvider(method = "selectAllRoleInfoToYM", type = TblRoleMySqlMapperConfig.class)
	TblRoleMySql selectAllRoleInfoToYM(BigDecimal rid) throws Exception;
}
