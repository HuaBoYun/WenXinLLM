package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblLogintype;
import com.huabo.compliance.entity.TblOrganization;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblLoginTypeMapper extends BaseMapper<TblLogintype> {

    @Select("SELECT LOGINID FROM TBL_LOGINTYPE where LOGINURL = #{loginType}")
    TblLogintype selectLoginTypeInfo(@Param("loginType") String logintype) throws Exception;



    @SelectProvider(type=TblLoginTypeMapperSqlConfig.class,method="selectListByPageInfo")
    @Results({
            @Result(column="LOGINID",property="loginid"),
            @Result(column="LOGINURL",property="loginurl"),
            @Result(column="SALEHOTLINE",property="salehotline"),
            @Result(column="ECHNICALSUPPORTPHONE",property="echnicalsupportphone"),
            @Result(column="LOGINPAGE",property="loginpage"),
            @Result(column="LOGINPAGETWO",property="loginpagetwo"),
            @Result(column="LOGINPAGETHREE",property="loginpagethree"),
            @Result(column="HOMEPAGE",property="homepage"),
            @Result(column="HOMEPAGEPIC",property="homepagepic"),
            @Result(column="LOGINPIC",property="loginpic"),
            @Result(column="LOGINNAME",property="loginname"),
            @Result(column="ORGID",property="orgid"),
    })
    List<TblLogintype> selectListByPageInfo(PageInfo<TblLogintype> pageInfo, @Param("orgid") BigDecimal orgid)throws Exception;


    @Select("select count(*) from TBL_ORGANIZATION where ORGTYPE < 100 and STATUS = 0 ")
    TblOrganization selectcOunt();

    @Select("select ORGNAME,ORGNUMBER,ORGID from TBL_ORGANIZATION where ORGTYPE = 100  ORDER BY orderid ASC")
    List<TblOrganization> selectIndustry();

    @Select("SELECT COUNT(*) FROM TBL_LOGINTYPE where 1=1 and ORGID= #{orgid}")
    Integer selectCountByPageInfo(BigDecimal orgid);

    @Select("SELECT * FROM TBL_LOGINTYPE where 1=1 and LOGINID= #{loginid}")
    TblLogintype findByLoginId(String loginid);


    @Delete("DELETE FROM TBL_LOGINTYPE WHERE LOGINID= #{loginid}")
    void deleteByLoginId(String loginid);

    @UpdateProvider(type=TblLoginTypeMapperSqlConfig.class,method="updatetblLoginType")
    void updatetblLoginType(TblLogintype tblt);

    @InsertProvider(method="saveTblLoginType",type=TblLoginTypeMapperSqlConfig.class)
    void saveTblLoginType(TblLogintype tnt);
}
