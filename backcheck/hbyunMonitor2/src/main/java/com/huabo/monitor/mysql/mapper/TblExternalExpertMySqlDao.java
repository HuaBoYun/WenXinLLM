package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.mysql.entity.TblExternalExpertMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblExternalExpertMySqlDao extends BaseMapper<TblExternalExpertMySql> {

    @Select("SELECT * FROM TBL_EXTERNAL_EXPERT WHERE EXTERID = #{exterid}")
    @Options(useGeneratedKeys = true, keyProperty = "exterid", keyColumn = "EXTERID")
    TblExternalExpertMySql get(BigDecimal exterid);

    @SelectProvider(method = "selectTblExternal", type = TblExternalExpertDaoSqlMySqlConfig.class)
    @Results({
            @Result(column = "EXTERID", property = "exterid"),
            @Result(column = "COMPANY", property = "company"),
            @Result(column = "EXPERTISE", property = "expertise"),
            @Result(column = "QUALIFICATION", property = "qualification"),
            @Result(column = "STAFFID", property = "staff.staffid", id = true),
            @Result(column = "REALNAME", property = "staff.realname"),
            @Result(column = "FIXEDPHONE", property = "fixedphone"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "MIBLEPHONE", property = "miblephone"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "PASSWORD", property = "password"),
            @Result(column = "JOBID", property = "jobid"),
            @Result(column = "CREATETIME", property = "createDate"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "OUTSIDEID", property = "outSideId"),
            @Result(column = "OUTSIDEOPENID", property = "outSideOpenId"),
            @Result(column = "ORGNAME", property = "orgname"),
            @Result(column = "FATHERORGID", property = "fatherorgid"),
            @Result(column = "ORGNUMBER", property = "orgnumber"),
            @Result(column = "ORGMENO", property = "orgmeno"),
            @Result(column = "ICODE", property = "icode"),
            @Result(column = "ORGTYPE", property = "orgtype"),
            @Result(column = "AUDITTYPE", property = "audittype"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ISZY", property = "iszy"),
            @Result(column = "HYZSKTYPE", property = "hyzsktype"),
            @Result(column = "ORDERID", property = "orderid"),
            @Result(column = "OUTSIDEID", property = "outsideid"),
            @Result(column = "OUTSIDEOPENDID", property = "outsideopendid"),
            @Result(column = "ISAUTONUMBER", property = "isautonumber"),
            @Result(column = "ORGCREATE", property = "orgcreate"),
            @Result(column = "ISINITIALIZATION", property = "isinitialization"),
            @Result(column = "DUTIES", property = "duties"),
            @Result(column = "INDUSTRYID", property = "industryid"),
            @Result(column = "BYWX", property = "bywx"),
            @Result(column = "DATASOURCE", property = "datasource"),
            @Result(column = "HISTORYCODE", property = "historycode"),
            @Result(column = "HISTORYDEPARTMENTID", property = "historydepartmentid"),


    })
    List<TblExternalExpertMySql> selectTblExternal(PageInfo<TblExternalExpertMySql> pageInfo, BigDecimal orgid, Find find, String company);

    // @Select("select COUNT(*) from TBL_EXTERNAL_EXPERT")
    @SelectProvider(method = "selectExternal", type = TblExternalExpertDaoSqlMySqlConfig.class)
    Integer selectExternal(BigDecimal orgid, Find find, String company);

    @UpdateProvider(type = TblExternalExpertDaoSqlMySqlConfig.class, method = "updateTblExternalExpert")
    void updateTblExternalExpert(TblExternalExpertMySql tee);

    @InsertProvider(method = "saveTblExternalExpert", type = TblExternalExpertDaoSqlMySqlConfig.class)
    void saveTblExternalExpert(TblExternalExpertMySql tee);


//    @Select("SELECT * FROM TBL_EXTERNAL_EXPERT ee,TblStaff s where ee.staffid = s.staffid and s.tblOrganization.orgid = ? " +
//    "AND s.realname like '% #{userName} %' ")
//    @SelectProvider(method = "selectListByPageInfoo",type = TblExternalExpertDaoSqlConfig.class)
//    List<TblExternalExpert> selectListByPageInfoo(String userName);

}
