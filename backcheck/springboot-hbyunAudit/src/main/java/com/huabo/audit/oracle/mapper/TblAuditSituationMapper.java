package com.huabo.audit.oracle.mapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface TblAuditSituationMapper  extends BaseMapper<TblAuditSituationEntity>{

	@SelectProvider(type = TblAuditSituationMapperSqlConfig.class , method = "selectPageInfoList")
	List<TblAuditSituationEntity> selectPageInfoList(Page<TblAuditSituationEntity> page, String year, TblStaffUtil loginStaff);


}
