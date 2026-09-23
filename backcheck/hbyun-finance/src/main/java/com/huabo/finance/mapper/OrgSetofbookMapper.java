package com.huabo.finance.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.caiji.OrgSetofbook;
import com.huabo.finance.vr.OrgSetofbookVr;

/**
 * <p>
 * 账簿类型 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
public interface OrgSetofbookMapper extends BaseMapper<OrgSetofbook> {

	
	@Select("SELECT OSB.*,BAS.NAME AS PKACCSYSTEMNAME,BCT.NAME AS STANDARDCURRNAME,BES.NAME AS ACCPERIODSCHEMENAME,BAPS.NAME AS ACCPERIODSCHEMENAME FROM ORG_SETOFBOOK OSB " + 
			" LEFT JOIN BD_ACCSYSTEM BAS ON OSB.PK_ACCSYSTEM = BAS.PK_ACCSYSTEM" + 
			" LEFT JOIN BD_CURRTYPE BCT ON OSB.PK_STANDARDCURR = BCT.PK_CURRTYPE" + 
			" LEFT JOIN BD_EXRATESCHEME BES ON OSB.PK_EXRATESCHEME = BES.PK_EXRATESCHEME" + 
			" LEFT JOIN BD_ACCPERIODSCHEME BAPS ON OSB.PK_ACCPERIODSCHEME = BAPS.PK_ACCPERIODSCHEME WHERE OSB.PK_SETOFBOOK = #{pkSetofbook}")
	OrgSetofbookVr selectEntityById(@Param("pkSetofbook")String pkSetofbook) throws Exception;

}
