package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcBankDirectConnection;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface TcBankDirectConnectionMapper {
    // 自定义查询方法，避免与 MyBatis-Plus 的自动生成方法冲突
    List<TcBankDirectConnection> selectByParamsCustom(Map<String, Object> params);
    List<TcBankDirectConnection> selectByParams(Map<String, Object> params);
    TcBankDirectConnection selectByPrimaryKey(String id);
    Map<String, Object> selectStatistics();
    int updateByPrimaryKey(TcBankDirectConnection connection);
    int updateByPrimaryKeySelective(TcBankDirectConnection connection);
    int insertSelective(TcBankDirectConnection connection);
    int deleteByPrimaryKey(String id);
}

