package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblAutonoInfoMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblAutonoNumberMySqlDao extends BaseMapper<TblAutonoInfoMySql> {

    @Select("SELECT * FROM TBL_AUTONO_INFO WHERE NOID IN ( SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID = #{orgid}) and PARENTID= #{parentid}")
    List<TblAutonoInfoMySql> listBySql(BigDecimal orgid, BigDecimal parentid);

    @Select("SELECT NOID FROM TBL_AUTONO_INFO WHERE PARENTID IS NOT NULL AND PARENTID != 1 AND PARENTID != -1 AND NOID NOT IN (SELECT NOID FROM TBL_ORG_NO WHERE ORGID = #{orgid}) ORDER BY NOID")
    List<String> selectNoIdListByOrgId(BigDecimal orgid);

    @Delete("DELETE FROM TBL_AUTONO_INFO WHERE NOID = #{orgid}")
    int deleteBy(BigDecimal orgid);
}
