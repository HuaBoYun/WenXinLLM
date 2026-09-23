package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

    @Select("SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    public Integer selectUniqueColumn(BigDecimal orgid);


    /**
     * 模型当中用来获取大量列表的方法
     * 老系统方法:findOrgTreeObjByHY
     * @param orgid 编号
     */
    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,level from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where o.orgtype = 100  " +
            " start with  o.fatherorgid = -1 connect by prior o.orgid= o.fatherorgid")
    List<Organization> findOrganizationByOrgid(@Param("orgid") String orgid);

    /**
     * 用来生成模型左侧树
     * 行业模型库---》左侧菜单
     * @return
     */
    @Select("select ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,level from TBL_ORGANIZATION where orgtype = 100 " +
            " start with  fatherorgid = -1 connect by prior orgid= fatherorgid")
    List<Organization> findOrganizationAll();

    /**
     * 行业模型库---》左侧菜单
     * 获取组织
     * @return
     */
    @Select("<script>" +
            "select * from TBL_ORGANIZATION where orgtype = #{orgtype}" +
            "<if test='hyzskType != null'> and HYZSKTYPE = #{hyzskType}</if>" +
            " and icode = #{orgid} and orgtype != 100 and (STATUS != 1 or STATUS IS NULL) start with  FATHERORGID = -1 connect by prior ORGID= FATHERORGID ORDER BY orgid ASC" +
            "</script>")
    List<Organization> findOrgTreeByOrgtypeAndOrgid(@Param("orgid")String orgid, @Param("orgtype")int orgtype, @Param("hyzskType")String hyzskType);

    @Select("select * from TBL_ORGANIZATION where orgtype < 100 and STATUS = 0  ORDER BY orderid ASC")
    List<Organization> query_orgtype();


    /**
     * 指标管理左侧菜单
     */
    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 " +
            " START WITH o.ORGID = #{orgid} CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc")
    List<Object[]>  belongToCompany(@Param("orgid")String orgid);

}
