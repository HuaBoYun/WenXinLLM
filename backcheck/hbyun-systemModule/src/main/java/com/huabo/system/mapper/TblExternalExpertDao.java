package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblExternalExpert;
import com.huabo.system.mappersql.TblExternalExpertDaoSqlConfig;

public interface TblExternalExpertDao extends BaseMapper<TblExternalExpert> {

    @Select("SELECT * FROM TBL_EXTERNAL_EXPERT WHERE EXTERID = #{exterid}")
    TblExternalExpert get(BigDecimal exterid);

    @SelectProvider(method="selectTblExternal",type=TblExternalExpertDaoSqlConfig.class)
    @Results({
            @Result(column="EXTERID",property="exterid"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "EXPERTISE",property = "expertise"),
            @Result(column = "QUALIFICATION",property = "qualification"),
            @Result(column = "STAFFID",property = "staff.staffid",id=true),
            @Result(column = "REALNAME",property = "staff.realname"),
            @Result(column = "FIXEDPHONE",property = "fixedphone"),
            @Result(column = "ADDRESS",property = "address"),
            @Result(column = "EMAIL",property = "email"),
            @Result(column = "MIBLEPHONE",property = "miblephone"),
            @Result(column = "MEMO",property = "memo"),
            @Result(column = "USERNAME",property = "username"),
            @Result(column = "PASSWORD",property = "password"),
            @Result(column = "JOBID",property = "jobid"),
            @Result(column = "CREATETIME",property = "createDate"),
            @Result(column = "STATUS",property = "status"),
            @Result(column = "ORGID",property = "orgid"),
            @Result(column = "OUTSIDEID",property = "outSideId"),
            @Result(column = "OUTSIDEOPENID",property = "outSideOpenId"),
            @Result(column = "ORGNAME",property = "orgname"),
            @Result(column = "FATHERORGID",property = "fatherorgid"),
            @Result(column = "ORGNUMBER",property = "orgnumber"),
            @Result(column = "ORGMENO",property = "orgmeno"),
            @Result(column = "ICODE",property = "icode"),
            @Result(column = "ORGTYPE",property = "orgtype"),
            @Result(column = "AUDITTYPE",property = "audittype"),
            @Result(column = "STATUS",property = "status"),
            @Result(column = "ISZY",property = "iszy"),
            @Result(column = "HYZSKTYPE",property = "hyzsktype"),
            @Result(column = "ORDERID",property = "orderid"),
            @Result(column = "OUTSIDEID",property = "outsideid"),
            @Result(column = "OUTSIDEOPENDID",property = "outsideopendid"),
            @Result(column = "ISAUTONUMBER",property = "isautonumber"),
            @Result(column = "ORGCREATE",property = "orgcreate"),
            @Result(column = "ISINITIALIZATION",property = "isinitialization"),
            @Result(column = "DUTIES",property = "duties"),
            @Result(column = "INDUSTRYID",property = "industryid"),
            @Result(column = "BYWX",property = "bywx"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "HISTORYCODE",property = "historycode"),
            @Result(column = "HISTORYDEPARTMENTID",property = "historydepartmentid"),
    })
    IPage<TblExternalExpert> selectTblExternal(IPage<TblExternalExpert> page, BigDecimal orgid, Find find,String company);

   @SelectProvider(method="selectExternal",type=TblExternalExpertDaoSqlConfig.class)
    Integer selectExternal(BigDecimal orgid, Find find,String company);

    @UpdateProvider(type=TblExternalExpertDaoSqlConfig.class,method="updateTblExternalExpert")
    void updateTblExternalExpert(TblExternalExpert tee);

    @InsertProvider(method = "saveTblExternalExpert",type = TblExternalExpertDaoSqlConfig.class)
    void saveTblExternalExpert(TblExternalExpert tee);


}
