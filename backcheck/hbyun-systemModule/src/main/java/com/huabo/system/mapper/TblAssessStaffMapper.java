package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblAssessStaff;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblAssessStaffMapper extends BaseMapper<TblAssessStaff> {

    @Select("SELECT  DISTINCT AM.STATE FROM tbl_assess_staff ast LEFT JOIN tbl_assess_mark am ON ast.assmarkid = am.assmarkid WHERE ast.staffid  = #{staffid} AND am.assid = #{assid} AND ast.orgid = #{orgid}")
    String getStatusByUserAsss(BigDecimal staffid, BigDecimal assid, BigDecimal orgid);
}
