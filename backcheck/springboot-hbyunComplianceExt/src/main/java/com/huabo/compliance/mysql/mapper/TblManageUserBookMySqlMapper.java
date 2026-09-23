package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblManageUserBookMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface TblManageUserBookMySqlMapper extends BaseMapper<TblManageUserBookMySql> {


    @Select("SELECT COUNT(*) FROM TBL_MANAGE_USER_BOOK where STAFFID = #{staffid} AND BOOKID = #{bookid}")
    Integer findCount(@Param("staffid") BigDecimal staffid, @Param("bookid") String bookid);

    @InsertProvider(method = "saveEntity", type = TblManageUserBookMapperSqlMySqlConfig.class)
    void saveEntity(TblManageUserBookMySql manage);

    @Delete("DELETE FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staff} AND BOOKID = #{acctid}")
    void deleteSidAndAcctid(@Param("staff") String staff, @Param("acctid") String acctid);

//    @Delete("DELETE FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staff} AND BOOKID = #{acctid}")
//    //@Options(useGeneratedKeys=true, keyProperty="staffid", keyColumn="STAFFID")
//    TblManageUserBook deleteSidAndAcctid(String staff, String acctid);

//    @UpdateProvider(type=TblManageUserBookMapperSqlConfig.class,method="updateBySql")
//    void updateBySql(TblManageUserBook staffid);
}
