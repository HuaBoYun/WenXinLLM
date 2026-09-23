package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcCloudConnectionContract;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface TcCloudConnectionContractMapper extends BaseMapper<TcCloudConnectionContract> {
    List<TcCloudConnectionContract> selectByParams(Map<String, Object> params);
    Map<String, Object> selectStatistics();
    List<TcCloudConnectionContract> selectRenewalAlerts();
    List<TcCloudConnectionContract> selectRenewalAlertsByDays(Integer days);
    TcCloudConnectionContract selectByPrimaryKey(String id);
    int insertSelective(TcCloudConnectionContract contract);
    int updateByPrimaryKeySelective(TcCloudConnectionContract contract);
    int deleteByPrimaryKey(String id);
}

