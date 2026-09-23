package com.huabo.financialdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.financialdata.entity.entity.AccBook;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;


/**
 * @author lee
 */
public interface AccBookMapper extends BaseMapper<AccBook> {

//
//    @Select("SELECT * FROM TBL_ACCBOOK WHERE ORGID = #{hbOrgEntityOrgid} AND BOOKID in (SELECT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staffid})")
//    List<AccBook> findBookIdByUserAll(@Param("staffid") BigDecimal staffid, @Param("hbOrgEntityOrgid") String hbOrgEntityOrgid);

    /**
     * 获取登录用户选中的账套ID
     *
     * @param staffId 登录用户ID
     * @param orgId   组织ID
     * @return 选中的账套ID
     */
    @Select("SELECT TBL_ACCBOOK.* FROM TBL_MANAGE_USER_BOOK  LEFT JOIN TBL_ACCBOOK ON TBL_ACCBOOK.BOOKID = TBL_MANAGE_USER_BOOK.BOOKID " +
            "WHERE TBL_MANAGE_USER_BOOK.STAFFID=#{staffId} AND TBL_MANAGE_USER_BOOK.STATUS=0 ")
    AccBook getSelectedBookByStaffId(@Param("staffId") BigDecimal staffId);

    /**
     * 通过账套id获取账套信息
     * @param bookid
     * @return
     */
    @Select("SELECT bookid,bookname,orgid,orgname,acctid,bookdesc,bookyear,balancesheeturl,incomestatementsurl,cashflowstatementsurl FROM TBL_ACCBOOK where BOOKID=#{bookid}")
    AccBook getSelectedId(String bookid);
}
