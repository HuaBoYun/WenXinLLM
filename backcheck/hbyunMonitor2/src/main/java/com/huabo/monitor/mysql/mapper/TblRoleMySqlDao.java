package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblRole;
import com.huabo.monitor.mysql.entity.TblRoleMySql;
import com.huabo.monitor.mysql.entity.TblStaffMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblRoleMySqlDao extends BaseMapper<TblRoleMySql> {

    @Select("SELECT * from TBL_ROLE where RSTATUS= '1' and COMPANYID = #{companyid}")
    List<TblRoleMySql> findBysql(BigDecimal companyid);

    //    @Insert("INSERT INTO TBL_ROLE (RNAME,RDESC,RSTATUS,COMPANYID) VALUES(#{rname},#{rdesc},#{rstatus},#{companyid})")
    @InsertProvider(method = "saveTblRole", type = TblRoleDaoSqlMySqlConfig.class)
    void saveTblRole(TblRole tr);

    @Select("SELECT * from TBL_ROLE where RID= #{rid}")
    List<TblRoleMySql> findByRid(String rid);

    @Select("SELECT * from TBL_STAFF where ROLEID=#{rid} AND (STATUS is NULL or STATUS != 0)")
    List<TblStaffMySql> findBysqlobj(String rid);

    //    @Update("UPDATE SET TBL_STAFF RSTATUS = #{rstatus} WHERE RID = #{rid}")
    @UpdateProvider(type = TblRoleDaoSqlMySqlConfig.class, method = "updateTblRole")
    void updateTblRole(TblRole role);

//    @Delete("DELETE FROM TBL_STAFF RID =#{role}")
//    void deleteTblRole(TblRole role);


    @SelectProvider(method = "selectListByPageInfo", type = TblRoleDaoSqlMySqlConfig.class)
    List<TblRoleMySql> selectListByPageInfo(PageInfo<TblRoleMySql> pageInfo, BigDecimal companyid, TblRole role);

    //    @SelectProvider(method = "selectListByPageInfoCount",type = TblRoleDaoSqlConfig.class)
    @Select("SELECT count(*) from TBL_ROLE  WHERE  COMPANYID = #{companyid}")
    Integer selectListByPageInfoCount(PageInfo<TblRoleMySql> pageInfo, BigDecimal companyid);

//    @Delete("DELETE FROM TBL_ROLE WHERE RID = #{role}")
//    void deleteRole(TblRole role);

    @Delete("DELETE FROM TBL_ROLE WHERE RID = #{rid}")
    void deleteByRid(Integer rid);

    @Delete("DELETE FROM TBL_ROLE WHERE RID = #{rid}")
    void deleteRole(BigDecimal rid);

//    @Delete("DELETE FROM TBL_ROLE WHERE RID = #{rid}")
//    void deleteRid(BigDecimal rid);
//

}
