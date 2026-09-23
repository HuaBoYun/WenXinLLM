package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.entity.TblInteriorExpert;
import com.huabo.monitor.mysql.entity.TblInteriorExpertMySql;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblInteriorExpertMySqlDao extends BaseMapper<TblInteriorExpertMySql> {
    @Select("SELECT * FROM TBL_INTERIOR_EXPERT WHERE INTERIORID = #{interior}")
    List<TblInteriorExpertMySql> findInterior(TblInteriorExpertMySql interior);

    //    @Insert("INSERT INTO TBL_INTERIOR_EXPERT(INTERIORID,POSITION,PROFESSIONAL,QUALIFICATION,ORGID) VALUES" +
//            "(#{interiorid},#{position},#{professional},#{orgId})")
    @InsertProvider(method = "insertInteriorExpert", type = TblInteriorExpertDaoSqlMySqlConfig.class)
    void insertInteriorExpert(TblInteriorExpert tie);

    //    @Update("UPDATE TBL_INTERIOR_EXPERT SET INTERIORID=#{interiorid},POSITION=#{position},PROFESSIONAL=#{professional},QUALIFICATION=#{qualification},ORGID=#{orgId} " +
//            "WHERE ORGID = #{orgid}")
    @UpdateProvider(type = TblInteriorExpertDaoSqlMySqlConfig.class, method = "updateInteriorExpert")
    void updateInteriorExpert(TblInteriorExpert tblinter);

    @SelectProvider(method = "selectListByPageInfoo", type = TblInteriorExpertDaoSqlMySqlConfig.class)
    @Results({
            @Result(column = "REALNAME", property = "staff.realname"),
            @Result(column = "ORGNAME", property = "organization.orgname"),
    })
    List<TblInteriorExpertMySql> selectListByPageInfoo(PageInfo<TblInteriorExpertMySql> pageInfo, BigDecimal orgid, Find find);

    @SelectProvider(method = "selectListByPageInfoCount", type = TblInteriorExpertDaoSqlMySqlConfig.class)
    Integer selectListByPageInfoCount(PageInfo<TblInteriorExpertMySql> pageInfo, BigDecimal orgid, Find find);

    @Select("select * from TBL_INTERIOR_EXPERT where INTERIORID = #{userid}")
    List<TblInteriorExpertMySql> findByRid(BigDecimal userid);

    @Delete("DELETE FROM TBL_INTERIOR_EXPERT WHERE INTERIORID = #{interiorid}")
    void deleteNbzj(BigDecimal interiorid);


//    @Delete("DELETE FROM TBL_INTERIOR_EXPERT where INTERIORID = #{userid}")
//    void deleteByINTERIORID(String id);
}
