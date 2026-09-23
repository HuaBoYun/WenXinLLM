package com.huabo.system.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblLoginType;
import com.huabo.system.entity.TblOrganization;

import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.websocket.Session;
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
public interface TblLoginTypeMapper extends BaseMapper<TblLoginType> {

    @Select("SELECT LOGINID FROM TBL_LOGINTYPE where LOGINURL = #{loginType}")
    TblLoginType selectLoginTypeInfo(@Param("loginType") String logintype) throws Exception;



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
    IPage<TblLoginType> selectListByPageInfo(IPage<TblLoginType> page,@Param("orgid") BigDecimal orgid)throws Exception;


    @Select("select count(*) from TBL_ORGANIZATION where ORGTYPE < 100 and STATUS = 0 ")
    TblOrganization selectcOunt();

    @Select("select ORGNAME,ORGNUMBER,ORGID from TBL_ORGANIZATION where ORGTYPE = 100  ORDER BY orderid ASC")
    List<TblOrganization> selectIndustry();

    @Select("SELECT * FROM TBL_LOGINTYPE where LOGINID= #{loginid}")
    TblLoginType findByLoginId(String loginid);


    @Delete("DELETE FROM TBL_LOGINTYPE WHERE LOGINID = #{loginid}")
    void deleteByLoginId(String loginid);

    @UpdateProvider(type=TblLoginTypeMapperSqlConfig.class,method="updatetblLoginType")
    void updatetblLoginType(TblLoginType tblt);

    @InsertProvider(method="saveTblLoginType",type=TblLoginTypeMapperSqlConfig.class)
    void saveTblLoginType(TblLoginType tnt);
}
