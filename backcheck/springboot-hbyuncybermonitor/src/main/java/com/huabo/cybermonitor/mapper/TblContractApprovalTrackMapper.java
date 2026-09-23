package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.TblContractApprovalTrack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TblContractApprovalTrackMapper extends BaseMapper<TblContractApprovalTrack> {

    @Select("SELECT * FROM TBL_CONTRACT_APPROVAL_TRACK WHERE CONTRACT_ID = #{contractId} ORDER BY STEP_ORDER ASC")
    List<TblContractApprovalTrack> selectByContractId(@Param("contractId") String contractId);
}
