package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.huabo.system.entity.TblAutonoInfo;

import tk.mybatis.mapper.common.BaseMapper;

public interface TblAutonoNumberDao extends BaseMapper<TblAutonoInfo> {

    @Select("SELECT * FROM TBL_AUTONO_INFO WHERE NOID IN ( SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID = #{orgid}) and PARENTID= #{parentid}")
    List<TblAutonoInfo> listBySql(BigDecimal orgid, BigDecimal parentid);

    @Select("SELECT NOID FROM TBL_AUTONO_INFO WHERE PARENTID IS NOT NULL AND PARENTID != 1 AND PARENTID != -1 AND NOID NOT IN (SELECT NOID FROM TBL_ORG_NO WHERE ORGID = #{orgid}) ORDER BY NOID")
    List<String> selectNoIdListByOrgId(BigDecimal orgid);

    @Delete("DELETE FROM TBL_AUTONO_INFO WHERE NOID = #{orgid}")
    int deleteBy(BigDecimal orgid);
}
