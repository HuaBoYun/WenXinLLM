package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblFlowdesMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowdesMySqlMapper extends BaseMapper<TblFlowdesMySql> {

    @Select("select * from TBL_FLOWDES where FLOWID= #{flowid}  ORDER BY POSITION ")
    List<TblFlowdesMySql> findBysqlFlowds(String flowid);

    @Delete("DELETE FROM TBL_FLOWDES WHERE FLOWDESID = #{id}")
    void deleteByid(String id);
}
