package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblFormDirectory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单目录Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormDirectoryMapper extends BaseMapper<TblFormDirectory> {

    /**
     * 查询表单目录树形结构
     * 
     * @param tenantId 租户ID
     * @param parentDirectoryId 父目录ID
     * @return 表单目录列表
     */
    List<TblFormDirectory> selectDirectoryTree(@Param("tenantId") String tenantId, 
                                                @Param("parentDirectoryId") String parentDirectoryId);

    /**
     * 查询子目录数量
     * 
     * @param directoryId 目录ID
     * @param tenantId 租户ID
     * @return 子目录数量
     */
    int countChildren(@Param("directoryId") String directoryId, 
                      @Param("tenantId") String tenantId);

    /**
     * 更新目录层级
     * 
     * @param directoryId 目录ID
     * @param directoryLevel 目录层级
     */
    void updateDirectoryLevel(@Param("directoryId") String directoryId, 
                              @Param("directoryLevel") Integer directoryLevel);
}

