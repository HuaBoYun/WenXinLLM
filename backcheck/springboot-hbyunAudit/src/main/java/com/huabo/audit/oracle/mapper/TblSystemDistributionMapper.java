package com.huabo.audit.oracle.mapper;



import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblSystemDistribution;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 系统业务单据下发通知表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
public interface TblSystemDistributionMapper extends Mapper<TblSystemDistribution> {


}
