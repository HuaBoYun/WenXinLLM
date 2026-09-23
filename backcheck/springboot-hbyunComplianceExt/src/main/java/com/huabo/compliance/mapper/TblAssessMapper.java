package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.compliance.entity.TblAssEleCategory;
import com.huabo.compliance.entity.TblAssess;
import com.huabo.compliance.entity.TblAssessVo;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.mapper.Provider.TblAssessProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblAssessMapper extends BaseMapper<TblAssess> {

    @SelectProvider(type = TblAssessProvider.class,method = "findPageBean" )
    <P extends IPage<TblAssessVo>> P findPageBean(
            P page,
            @Param("staffid") BigDecimal staffid,
            @Param("projkey") String projkey,
            @Param("projname") String projname,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("assstatus") String assstatus);

    /**
     *  yhr
     * @param <P>
     * @return
     */
    @SelectProvider(type = TblAssessProvider.class,method = "findPageBeanPJLX" )
    <P extends IPage<TblAssessVo>> P findPageBeanPJLX(
            P page,
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
          );


    @SelectProvider(type = TblAssessProvider.class,method = "findPageBeanPJJG" )
    <P extends IPage<TblAssessVo>> P findPageBeanPJJG(
            P page,
            @Param("assNumnber") String assNumnber,
            @Param("assName") String assName,
            @Param("startDate") String startDate,
            @Param("startDates") String startDates,
            @Param("endDate") String endDate,
            @Param("endDates") String endDates,
            @Param("staff") TblStaffUtil staff);

    List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id);


    @Select("select * from tbl_organization o where o.orgid in(select max(assorgid)  from TBL_ASSESS_MARK t where t.ASSID =#{assId} group by assorgid) and o.status = 0 ")
    List<TblOrganization> getOrgByassId(@Param("assId") BigDecimal assId);




    @Select("select Ta.*,Ta2.TEMPLENAME,T.ORGNAME,T.ORGID , TS.REALNAME,Ts.USERNAME from TBL_ASSESS_TARGET tat inner join TBL_ASSESS TA on tat.ASSID = TA.ASSID\n" +
            "inner join TBL_ORGANIZATION T on tat.ORGID = T.ORGID inner join TBL_STAFF  TS on ta.LEADERID=ts.STAFFID\n" +
            "inner  join TBL_ASSESSTEMPLE TA2 on TA2.ASSTEMID=Ta.ASSTEMID\n" +
            "where tat.ASSID=#{assId} and tat.ORGID=#{orgid}")
    TblAssessVo queryTblAssessVoByAssidAndOrgid(@Param("assId") BigDecimal assId,@Param("orgid") BigDecimal orgid);



    @Select("select count(0) from TBL_ASSESS WHERE asstemid=#{asstemid}")
    Integer findTblAssessByTempid(@Param("asstemid")BigDecimal asstemid);


}
