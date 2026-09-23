package com.huabo.monitor.mapper;


import com.huabo.monitor.entity.Tree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TreeMapper {




    @Select("${sql}")
    List<Tree> queryTree(@Param("sql") String sql);

}
