package com.huabo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowdes;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowdesMapper extends BaseMapper<TblFlowdes> {

    @Select("select * from TBL_FLOWDES where FLOWID= #{flowid}  ORDER BY POSITION ")
    List<TblFlowdes> findBysqlFlowds(String flowid);

    @Delete("DELETE FROM TBL_FLOWDES WHERE FLOWDESID = #{id}")
    void deleteByid(String id);
}
