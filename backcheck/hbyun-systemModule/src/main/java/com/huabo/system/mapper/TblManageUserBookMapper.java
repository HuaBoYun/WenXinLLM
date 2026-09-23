package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblManageUserBook;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblManageUserBookMapper extends BaseMapper<TblManageUserBook> {


    @Select("SELECT COUNT(*) FROM TBL_MANAGE_USER_BOOK where STAFFID = #{staffid} AND BOOKID = #{bookid}")
    Integer findCount(@Param("staffid") BigDecimal staffid, @Param("bookid") String bookid);

    @InsertProvider(method = "saveEntity",type = TblManageUserBookMapperSqlConfig.class)
    void saveEntity(TblManageUserBook manage);

    @Delete("DELETE FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staff} AND BOOKID = #{acctid}")
    void deleteSidAndAcctid(@Param("staff")String staff, @Param("acctid")String acctid);

}
