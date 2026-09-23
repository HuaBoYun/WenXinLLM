package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.BiPage;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.YyCompany;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
public interface YyCompanyMapper extends BaseMapper<YyCompany> {
    int insertBiPageSet(@Param("companyId") BigDecimal companyId, @Param("biPageSet") Set<BiPage> biPageSet);

    /**
     * 直接用 stepid 字符串列表插入 TBL_YY_COMPANY_PAGE，不依赖 TBL_BI_PAGE 查询
     */
    int insertPageIdsByStrList(@Param("companyId") BigDecimal companyId, @Param("pageIdList") List<String> pageIdList);

    int deleteComPageByCompanyId(@Param("companyId") BigDecimal companyId);
    
    int deleteComPageByTeamId(@Param("teamid") BigDecimal teamid);

    @Select(" SELECT COUNT(1) FROM TBL_YY_COMPANY WHERE FXTYPE='正常' AND ORGID=#{orgid}")
    int queryNormalNumber(@Param("orgid")BigDecimal orgid);

    @Select(" SELECT COUNT(1) FROM TBL_YY_COMPANY WHERE FXTYPE NOT IN '正常' AND ORGID=#{orgid}")
    int queryAbnormalNumber(@Param("orgid")BigDecimal orgid);
    
    
  List<YyCompany> findCompanyList(@Param("ew") Wrapper<T> queryWrapper);

}
