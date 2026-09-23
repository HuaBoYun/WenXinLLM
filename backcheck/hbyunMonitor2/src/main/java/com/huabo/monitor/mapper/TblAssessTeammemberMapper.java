package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblAssEleCategory;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessPlanVo;
import com.huabo.monitor.entity.TblAssessTeammember;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.mapper.Provider.TblAssessProvider;

@Mapper
public interface TblAssessTeammemberMapper extends BaseMapper<TblAssessTeammember> {
}
