package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProjectConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 项目配置Mapper
 */
public interface TblProjectConfigMapper extends BaseMapper<TblProjectConfig> {
    
    /**
     * 根据项目编码查询
     */
    TblProjectConfig selectByProjectCode(@Param("projectCode") String projectCode);
    
    /**
     * 根据项目经理ID查询
     */
    List<TblProjectConfig> selectByProjectManagerId(@Param("projectManagerId") String projectManagerId);
    
    /**
     * 根据部门ID查询
     */
    List<TblProjectConfig> selectByDepartmentId(@Param("departmentId") String departmentId);
    
    /**
     * 根据项目状态查询
     */
    List<TblProjectConfig> selectByProjectStatus(@Param("projectStatus") String projectStatus);
}

