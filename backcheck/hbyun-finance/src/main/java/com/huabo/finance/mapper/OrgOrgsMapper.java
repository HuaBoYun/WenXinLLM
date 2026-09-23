package com.huabo.finance.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.huabo.finance.entity.caiji.OrgOrgs;
import com.huabo.finance.mappersql.OrgOrgsMapperSqlConfig;
import com.huabo.finance.vo.OrgOrgsVo;
import com.huabo.finance.vr.OrgOrgVr;

/**
 * <p>
 * 财务组织信息表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-14
 */
public interface OrgOrgsMapper extends BaseMapper<OrgOrgs> {

	@Update("UPDATE ORG_ORGS SET COMPARISONORGID = #{orgId} WHERE PK_ORG = #{pkOrg}")
	void updateCompanyInfo(@Param("pkOrg")String pkOrg,@Param("orgId") BigDecimal orgId) throws Exception;

	@SelectProvider(type = OrgOrgsMapperSqlConfig.class , method = "selectAllList")
	List<OrgOrgVr> selectAllList(OrgOrgsVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

	@Select("SELECT PK_ORG FROM ORG_ORGS WHERE COMPARISONORGID = #{orgid}")
	String selectFinanceOrgIdByOrgId(@Param("orgid")BigDecimal orgid);

	@Update("UPDATE ORG_ORGS SET COMPARISONORGID = NULL WHERE COMPARISONORGID = #{orgId}")
	void removeOtherCompanyInfo(@Param("orgId")BigDecimal orgId) throws Exception;

}
