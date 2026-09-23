package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.FormGroupQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormGroup;

import java.util.List;

/**
 * 表单组Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormGroupService extends IService<TblFormGroup> {

    /**
     * 查询表单组列表
     * 
     * @param param 查询参数
     * @return 表单组列表
     */
    List<TblFormGroup> getList(FormGroupQueryParam param);

    /**
     * 根据ID查询表单组详情
     * 
     * @param groupId 表单组ID
     * @return 表单组详情
     */
    TblFormGroup getDetail(String groupId);

    /**
     * 保存表单组(新增或修改)
     * 
     * @param formGroup 表单组信息
     * @return 保存结果
     */
    boolean saveFormGroup(TblFormGroup formGroup);

    /**
     * 删除表单组
     * 
     * @param groupId 表单组ID
     * @return 删除结果
     */
    boolean deleteFormGroup(String groupId);

    /**
     * 更新表单组排序
     * 
     * @param groupId 表单组ID
     * @param sortNo 排序号
     * @return 更新结果
     */
    boolean updateSort(String groupId, Integer sortNo);

    /**
     * 根据目录ID查询表单组列表
     * 
     * @param directoryId 目录ID
     * @return 表单组列表
     */
    List<TblFormGroup> getListByDirectoryId(String directoryId);
}

