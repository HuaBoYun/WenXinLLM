package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Accbook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface AccbookMapper extends BaseMapper<Accbook> {


    @Select("SELECT * FROM TBL_ACCBOOK WHERE ORGID = #{hbOrgEntityOrgid} AND BOOKID in (SELECT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staffid})")
    List<Accbook> findBookIdByUserAll(@Param("staffid") BigDecimal staffid, @Param("hbOrgEntityOrgid") String hbOrgEntityOrgid);

    @Select("SELECT * FROM TBL_ACCBOOK WHERE BOOKID = (SELECT DISTINCT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE BOOKID IN (SELECT BOOKID FROM TBL_ACCBOOK WHERE ORGID = #{orgid} AND BOOKDESC IS NULL) AND STATUS = 0 AND STAFFID = #{staffid})")
    List<Accbook> findBookByUser1(@Param("staffid") BigDecimal staffid, @Param("orgid") BigDecimal orgid);

    @Select("SELECT * FROM TBL_ACCBOOK WHERE ORGID = #{orgid} AND BOOKDESC IS NULL  AND BOOKYEAR = (SELECT MAX(BOOKYEAR) FROM TBL_ACCBOOK WHERE ORGID = #{orgid} AND BOOKDESC IS NULL) AND BOOKID IN (SELECT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE STAFFID =  #{staffid})")
    List<Accbook> findBookByUser2(@Param("staffid") BigDecimal staffid, @Param("orgid") BigDecimal orgid);

}
