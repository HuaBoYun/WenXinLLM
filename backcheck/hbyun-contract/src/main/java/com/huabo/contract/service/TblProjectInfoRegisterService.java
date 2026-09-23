package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.TblProjectInfoRegister;
import com.huabo.contract.vo.ProjectInfoRegisterQueryParam;

import java.util.List;

/**
 * 项目信息登记表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-25
 */
public interface TblProjectInfoRegisterService extends IService<TblProjectInfoRegister> {

    /**
     * 分页查询项目信息登记列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblProjectInfoRegister> getProjectInfoRegisterList(ProjectInfoRegisterQueryParam param);

    /**
     * 保存项目信息登记（新增或修改）
     * 
     * @param projectInfo 项目信息
     * @return 保存结果
     */
    boolean saveProjectInfoRegister(TblProjectInfoRegister projectInfo);

    /**
     * 根据ID获取项目信息详情
     * 
     * @param projectId 项目ID
     * @return 项目信息
     */
    TblProjectInfoRegister getProjectInfoRegisterById(String projectId);

    /**
     * 根据登记编号获取项目信息
     * 
     * @param registerNo 登记编号
     * @return 项目信息
     */
    TblProjectInfoRegister getProjectInfoRegisterByRegisterNo(String registerNo);

    /**
     * 删除项目信息登记
     * 
     * @param projectId 项目ID
     * @return 删除结果
     */
    boolean deleteProjectInfoRegister(String projectId);

    /**
     * 批量删除项目信息登记
     * 
     * @param projectIds 项目ID列表
     * @return 删除结果
     */
    boolean batchDeleteProjectInfoRegister(List<String> projectIds);

    /**
     * 生成项目登记编号
     * 
     * @return 登记编号
     */
    String generateRegisterNo();

    /**
     * 检查登记编号是否存在
     * 
     * @param registerNo 登记编号
     * @param excludeProjectId 排除的项目ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsRegisterNo(String registerNo, String excludeProjectId);

    /**
     * 更新项目状态
     * 
     * @param projectId 项目ID
     * @param projectStatus 项目状态
     * @return 更新结果
     */
    boolean updateProjectStatus(String projectId, Integer projectStatus);

    /**
     * 批量更新项目状态
     * 
     * @param projectIds 项目ID列表
     * @param projectStatus 项目状态
     * @return 更新结果
     */
    boolean batchUpdateProjectStatus(List<String> projectIds, Integer projectStatus);

    /**
     * 更新报备状态
     * 
     * @param projectId 项目ID
     * @param reportStatus 报备状态
     * @return 更新结果
     */
    boolean updateReportStatus(String projectId, Integer reportStatus);

    /**
     * 批量更新报备状态
     * 
     * @param projectIds 项目ID列表
     * @param reportStatus 报备状态
     * @return 更新结果
     */
    boolean batchUpdateReportStatus(List<String> projectIds, Integer reportStatus);

    /**
     * 获取需要首谈报备的项目列表
     * 
     * @return 需要首谈报备的项目列表
     */
    List<TblProjectInfoRegister> getFirstTalkReportProjects();

    /**
     * 检查项目重复
     * 
     * @param projectName 项目名称
     * @param contractorFullName 发包方全称
     * @param excludeProjectId 排除的项目ID
     * @return 重复项目列表
     */
    List<TblProjectInfoRegister> checkDuplicateProjects(String projectName, String contractorFullName, String excludeProjectId);

    /**
     * 自动设置首谈报备标识
     * 
     * @param projectInfo 项目信息
     */
    void autoSetFirstTalkReport(TblProjectInfoRegister projectInfo);
}
