package com.huabo.system.mapper;

import com.huabo.system.entity.TblSecrectLevel;

import lombok.experimental.PackagePrivate;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 密级信息配置表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2024-12-20
 */
public interface TblSecrectLevelMapper extends BaseMapper<TblSecrectLevel> {

	@Select("SELECT SECRECYSTAFFSCOPE FROM TBL_SECRECT_LEVEL WHERE LEVELID = #{formSecrectId}")
	String selectStaffScopeById(@Param("formSecrectId")BigDecimal formSecrectId) throws Exception;

	@Select("SELECT SECRECYMENUSCOPE FROM TBL_SECRECT_LEVEL WHERE LEVELID = #{secrectLevelId}")
	String selectMenuScopeById(@Param("secrectLevelId")BigDecimal secrectLevelId) throws Exception;

	@SelectProvider(type = TblSecrectLevelMapperSqlConfig.class , method = "selectListByLoginUser")
	List<TblSecrectLevel> selectListByLoginUser(BigDecimal secrectLevelId) throws Exception;

}
