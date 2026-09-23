package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblAssessStaffMySql;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblAssessStaffMySqlMapper extends BaseMapper<TblAssessStaffMySql> {

    @Select("SELECT  DISTINCT AM.STATE FROM tbl_assess_staff ast LEFT JOIN tbl_assess_mark am ON ast.assmarkid = am.assmarkid WHERE ast.staffid  = #{staffid} AND am.assid = #{assid} AND ast.orgid = #{orgid}")
    String getStatusByUserAsss(BigDecimal staffid, BigDecimal assid, BigDecimal orgid);
}
