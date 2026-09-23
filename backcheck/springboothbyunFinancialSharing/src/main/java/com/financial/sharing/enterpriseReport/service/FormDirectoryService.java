package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.FormDirectoryQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormDirectory;

import java.util.List;

/**
 * 表单目录Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormDirectoryService extends IService<TblFormDirectory> {

    /**
     * 查询表单目录树形结构
     * 
     * @param param 查询参数
     * @return 表单目录树
     */
    List<TblFormDirectory> getDirectoryTree(FormDirectoryQueryParam param);

    /**
     * 查询表单目录列表(分页)
     * 
     * @param param 查询参数
     * @return 表单目录列表
     */
    List<TblFormDirectory> getList(FormDirectoryQueryParam param);

    /**
     * 根据ID查询表单目录详情
     * 
     * @param directoryId 目录ID
     * @return 表单目录详情
     */
    TblFormDirectory getDetail(String directoryId);

    /**
     * 保存表单目录(新增或修改)
     * 
     * @param directory 表单目录信息
     * @return 保存结果
     */
    boolean saveDirectory(TblFormDirectory directory);

    /**
     * 删除表单目录
     * 
     * @param directoryId 目录ID
     * @return 删除结果
     */
    boolean deleteDirectory(String directoryId);

    /**
     * 移动表单目录
     * 
     * @param directoryId 目录ID
     * @param targetParentId 目标父目录ID
     * @return 移动结果
     */
    boolean moveDirectory(String directoryId, String targetParentId);

    /**
     * 更新目录排序
     * 
     * @param directoryId 目录ID
     * @param sortNo 排序号
     * @return 更新结果
     */
    boolean updateSort(String directoryId, Integer sortNo);
}

