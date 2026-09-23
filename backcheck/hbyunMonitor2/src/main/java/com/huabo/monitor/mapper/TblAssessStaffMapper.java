package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblAssessStaff;
import com.huabo.monitor.entity.TblAssessStaffVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblAssessStaffMapper extends BaseMapper<TblAssessStaff> {
/**
    @Select("select distinct * " +
            "from TBL_ASSESS_STAFF st " +
            "where st.ASSSTAFFID in " +
            "(select max (ast.ASSSTAFFID) " +
            "from  TBL_ASSESS_STAFF ast " +
            "left join TBL_ASSESS_MARK am on ast.ASSMARKID = am.ASSMARKID " +
            "where ast.STAFFID = #{userStaffid} " +
            "and am.ASSID = #{selectedPlans} group by ast.ORGID  )")
    <P extends IPage<TblAssessStaff>> P getTblAssessGroupOrg(
            P page,
            @Param("selectedPlans") BigDecimal selectedPlans,
            @Param("userStaffid") BigDecimal userStaffid);

*/

@Select("\n" +
        "select st.*,T.ORGNAME,mar.ASSID,ts.STAFFID markstaffid ,ts.REALNAME markREALNAME from tbl_assess_staff st\n" +
        "inner join    TBL_ORGANIZATION T on st.ORGID = T.ORGID\n" +
        "inner JOIN    TBL_ASSESS_MARK mar on st.ASSMARKID=mar.ASSMARKID\n" +
        "inner join    TBL_STAFF ts on mar.STAFFID = ts.STAFFID\n" +
        "where st.ASSSTAFFID\n" +
        "          in (select max(ast.ASSSTAFFID)  from  tbl_assess_staff ast\n" +
        "              left join tbl_assess_mark am on ast.assmarkid = am.assmarkid\n" +
        "          where ast.staffid = #{userStaffid} and am.assid = #{selectedPlans} group by ast.ORGID)")
<P extends IPage<TblAssessStaffVo>> P getTblAssessGroupOrg(
        P page,
        @Param("selectedPlans") BigDecimal selectedPlans,
        @Param("userStaffid") BigDecimal userStaffid);



    /*
       yhr

     */

    @Delete("delete from TBL_ASSESS_STAFF where assmarkid=#{smarkid}")
    int  deleteBySmarkid( @Param("smarkid")BigDecimal smarkid);




    @Select("    select t.* from TBL_ASSESS_STAFF t\n" +
            "    inner join TBL_ASSESS_MARK TAM on t.ASSMARKID = TAM.ASSMARKID\n" +
            "    where t.STATUS=0 and tam.SUITABLE=1 and\n" +
            "    t.ORGID=#{orgId} and t.STAFFID=#{userId}  and tam.ASSID=#{assId}")
    List<TblAssessStaff> checkSubmit(@Param("assId") BigDecimal assId, @Param("orgId")BigDecimal orgId, @Param("userId")BigDecimal userId);





    @Select("    select t.* from TBL_ASSESS_STAFF t\n" +
            "    inner join TBL_ASSESS_MARK TAM on t.ASSMARKID = TAM.ASSMARKID\n" +
            "    where t.STATUS=1 and tam.SUITABLE=1 and\n" +
            "    t.ORGID=#{orgId} and t.STAFFID=#{userId}  and tam.ASSID=#{assId}")
    List<TblAssessStaff> checkdoSubmint(@Param("assId") BigDecimal assId, @Param("orgId")BigDecimal orgId, @Param("userId")BigDecimal userId);





    @Select("    select t.* from TBL_ASSESS_STAFF t\n" +
            "    inner join TBL_ASSESS_MARK TAM on t.ASSMARKID = TAM.ASSMARKID\n" +
            "    where t.STATUS!=2 and tam.SUITABLE=1 and tam.ASSID=#{assId}")
    List<TblAssessStaff> checkSubmitAll(@Param("assId") BigDecimal assId);


}