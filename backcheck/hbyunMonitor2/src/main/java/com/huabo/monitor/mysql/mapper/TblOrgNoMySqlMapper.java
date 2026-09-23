package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblOrgNoMySql;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblOrgNoMySqlMapper extends BaseMapper<TblOrgNoMySql> {


    //@Select("SELECT DISTINCT TON.ORGID,TON.NOID,TON.NOCODE,TON.NOSEPARTOR,TON.NONUMBER,TON.ISUSEDEFAULT,TAI.NONAME FROM TBL_ORG_NO TON LEFT JOIN TBL_AUTONO_INFO TAI ON TON.NOID=TAI.NOID WHERE FIND_IN_SET(TAI.PARENTID,getAutoInfoList(#{orgid},#{fatherrightid})) and (TON.ORGID = #{orgid} OR TON.ORGID IS NULL)")
//    @SelectProvider(method="selectListByPageInfo",type=TblOrganizationMapperSqlConifg.class)
//    List selectListByPageInfo(BigDecimal orgid, BigDecimal fatherrightid);

    @Update("update TBL_ORG_NO set nocode = #{nocode},isusedefault = #{isusedefault},noSepartor= #{noSepartor},noNumber = #{noNumber} where orgid = #{orgid} AND noid = #{noid}")
    void updateNumber(TblOrgNoMySql tblOrgNo);

    @SelectProvider(method = "selectListByPageInfo", type = TblOrgNoMapperSqlMySqlConfig.class)
    List<TblOrgNoMySql> selectListByPageInfo(PageInfo<TblOrgNoMySql> pageInfo, BigDecimal orgid, BigDecimal fatherrightid);

    @SelectProvider(method = "selectCountByPageInfo", type = TblOrgNoMapperSqlMySqlConfig.class)
    Integer selectCountByPageInfo(BigDecimal orgid, BigDecimal fatherrightid);


    //@InsertProvider(method = "save",type = TblOrgNoMapperSqlConfig.class)
    @Insert("Insert into TBL_ORG_NO (NOID,ORGID,ISUSEDEFAULT)VALUES (#{noid},#{orgid},#{isusedefault}) ")
    void save(TblOrgNoMySql orgNo);
}
