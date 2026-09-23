package com.huabo.compliance.mapper.Provider;


import com.hbfk.entity.TblStaffUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;

/**
 * @author meng
 * @date 2022-08-26 10:23
 * @description
 */
public class TblAssessProvider {

    public String findPageBean(@Param("staffid") BigDecimal staffid,
                               @Param("projkey") String projkey,
                               @Param("projname") String projname,
                               @Param("startDate") String startDate,
                               @Param("endDate") String endDate,
                               @Param("assstatus") String assstatus) {

        //select distinct *
        //from TBL_ASSESS t
        //    inner join TBL_ASSESS_MARK TAM on t.ASSID = TAM.ASSID
        //    left join TBL_ASSESS_STAFF TAS on TAM.ASSMARKID = TAS.ASSMARKID
        //    where TAS.STAFFID = ? or t.LEADERID = ?
        //提供一些字段
        SQL sql = new SQL().SELECT_DISTINCT(
                "t.*","ta.templename"
//                "ASSESSID",
//                "LEADERID",
//                "STARTDATE",
//                "ENDDATE"
        ).FROM(" TBL_ASSESS t " +
                " inner join TBL_ASSESSTEMPLE ta on t.asstemid=ta.asstemid "+
                " inner join TBL_ASSESS_MARK TAM on t.ASSID = TAM.ASSID " +
                " left join TBL_ASSESS_STAFF TAS on TAM.ASSMARKID = TAS.ASSMARKID "

        );

        sql.WHERE("TAS.STAFFID = #{staffid} or t.LEADERID = #{staffid} ");
        sql.AND().WHERE("t.ASSSTATUS = #{assstatus}");

        if (StringUtils.isNotBlank(projkey)) {
            sql.AND().WHERE("t.ASSESSID like concat('%',concat(#{projkey},'%'))");
        }
        if (StringUtils.isNotBlank(projname)) {
            sql.AND().WHERE("t.ASSESSNAME like concat('%',concat(#{projname},'%'))");
        }

        if (!StringUtils.isBlank(startDate)) {
            sql.AND().WHERE("t.STARTDATE >= to_date(#{startDate},'yyyy/mm/dd')");
        }
        if (!StringUtils.isBlank(endDate)) {
            sql.AND().WHERE("t.ENDDATE <= to_date(#{endDate},'yyyy/mm/dd')");
        }
        System.out.println(sql.toString());
        return sql.toString();
    }

    /*
       评价立项 和 跟踪列表
     */
    public String findPageBeanPJLX(
            @Param("isAudit") boolean isAudit,
            @Param("orgid") BigDecimal orgid,
            @Param("staffid") BigDecimal staffid,
            @Param("realName") String realName,
            @Param("assNumnber") String assNumnber,
                                      @Param("assName") String assName,
                                      @Param("startDate") String startDate,
                                      @Param("startDates") String startDates,
                                      @Param("endDate") String endDate,
                                      @Param("endDates") String endDates
                                     ) {

        //select distinct *
        //from TBL_ASSESS t
        //    inner join TBL_ASSESS_MARK TAM on t.ASSID = TAM.ASSID
        //    left join TBL_ASSESS_STAFF TAS on TAM.ASSMARKID = TAS.ASSMARKID
        //    where TAS.STAFFID = ? or t.LEADERID = ?
        //提供一些字段
        SQL sql = new SQL().SELECT(
                "t.*","ta.templename"
//                "ASSESSID",
//                "LEADERID",
//                "STARTDATE",
//                "ENDDATE"
        ).FROM(" TBL_ASSESS t " +
                " inner join TBL_ASSESSTEMPLE ta on t.asstemid=ta.asstemid "
        );



        sql.WHERE("tblcomany="+orgid);


        if (!isAudit) {
            sql.AND().WHERE(" (t.asssponsor = '"+realName+"'  or t.leaderid="+staffid+") ");

        }
        if (StringUtils.isNotBlank(assNumnber)) {
            sql.AND().WHERE(" t.ASSESSID like concat('%',concat(#{assNumnber},'%')) ");
        }

        if (StringUtils.isNotBlank(assName)) {
            sql.AND().WHERE(" t.assessname like concat('%',concat(#{assName},'%')) ");

        }

        if (!StringUtils.isBlank(startDate)) {
            sql.AND().WHERE(" t.startdate >= to_date(#{startDate},'yyyy/mm/dd')");

        }

        if (!StringUtils.isBlank(startDates)) {
            sql.AND().WHERE(" t.startdate <= to_date(#{startDates},'yyyy/mm/dd')");

        }

        if (!StringUtils.isBlank(endDate)) {
            sql.AND().WHERE(" t.enddate >= to_date(#{endDate},'yyyy/mm/dd')");


        }
        if (!StringUtils.isBlank(endDates)) {
            sql.AND().WHERE(" t.enddate <= to_date(#{endDates},'yyyy/mm/dd')");

        }
        sql.ORDER_BY("t.assstatus asc,t.assid desc");

        System.out.println(sql.toString());
        return sql.toString();
    }


    /*
       评价结果列表
     */
    public String findPageBeanPJJG(   @Param("assNumnber") String assNumnber,
                                      @Param("assName") String assName,
                                      @Param("startDate") String startDate,
                                      @Param("startDates") String startDates,
                                      @Param("endDate") String endDate,
                                      @Param("endDates") String endDates,
                                      @Param("staff") TblStaffUtil staff) {
        //提供一些字段
        SQL sql = new SQL().SELECT_DISTINCT(
                "t.*","ta.templename"

        ).FROM(" TBL_ASSESS t " +
                " inner join TBL_ASSESSTEMPLE ta on t.asstemid=ta.asstemid "+
                " left join TBL_ASSESS_MARK TAM on t.ASSID = TAM.ASSID "
        );


        sql.WHERE(" t.assstatus in (3,4) and tblcomany='"+staff.getCurrentOrg().getOrgid()+"'");


        if (StringUtils.isNotBlank(assNumnber)) {
            sql.AND().WHERE(" t.ASSESSID like concat('%',concat(#{assNumnber},'%')) ");
        }

        if (StringUtils.isNotBlank(assName)) {
            sql.AND().WHERE(" t.assessname like concat('%',concat(#{assName},'%')) ");

        }

        if (!StringUtils.isBlank(startDate)) {
            sql.AND().WHERE(" t.startdate >= to_date(#{startDate},'yyyy/mm/dd')");

        }

        if (!StringUtils.isBlank(startDates)) {
            sql.AND().WHERE(" t.startdate <= to_date(#{startDates},'yyyy/mm/dd')");

        }

        if (!StringUtils.isBlank(endDate)) {
            sql.AND().WHERE(" t.enddate >= to_date(#{endDate},'yyyy/mm/dd')");


        }
        if (!StringUtils.isBlank(endDates)) {
            sql.AND().WHERE(" t.enddate <= to_date(#{endDates},'yyyy/mm/dd')");

        }
        sql.ORDER_BY("t.assstatus asc,t.assid desc");

        System.out.println(sql.toString());
        return sql.toString();
    }

}
