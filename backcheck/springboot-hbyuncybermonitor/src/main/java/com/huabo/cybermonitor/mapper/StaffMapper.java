package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
public interface StaffMapper extends BaseMapper<Staff> {


      /*
         yhr 2022-08-12  分页查询 监控执行 -推送查用户信息
     */
     @Select(

             "SELECT * FROM (SELECT A.*, ROWNUM RN FROM "+
                   "  ( select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID " +
                     "             where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=#{org.orgid} AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0) order by STAFFID desc" +
                     ") A "+
                    "       WHERE ROWNUM <= #{end,jdbcType=DECIMAL}) WHERE RN > #{start,jdbcType=DECIMAL}"

     )
     public List<Map<String,Object>> getList(@Param("start") Long start, @Param("end") Long end,@Param("org") Organization organization);

     @Select("select count(*) c from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID "
             + " where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=#{org.orgid} AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0) ")
     public long  getCount(@Param("org") Organization organization);
    /*
          分页查询情况2 -推送查用户信息
     */

    @Select(

            "SELECT * FROM (SELECT A.*, ROWNUM RN FROM "+
                    "  ( select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID " +
                    "         where STA.ORGID =#{org.orgid} AND (STA.STATUS is NULL or STA.STATUS != 0) order by STAFFID desc" +
                    ") A "+
                    "       WHERE ROWNUM <= #{end,jdbcType=DECIMAL}) WHERE RN > #{start,jdbcType=DECIMAL}"

    )

    public List<Map<String,Object>> getList2(@Param("start") Long start, @Param("end") Long end,@Param("org") Organization organization);


    @Select("select count(*) c from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID "
            + " where STA.ORGID =#{org.orgid} AND (STA.STATUS is NULL or STA.STATUS != 0)")
    public long  getCount2(@Param("org") Organization organization);

}
