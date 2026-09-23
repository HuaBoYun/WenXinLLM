package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblProjectInfoRegister;
import com.huabo.contract.vo.ProjectInfoRegisterQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目信息登记表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-25
 */
@Mapper
public interface TblProjectInfoRegisterMapper extends BaseMapper<TblProjectInfoRegister> {

    /**
     * 分页查询项目信息登记列表
     * 
     * @param param 查询参数
     * @return 项目信息登记列表
     */
    List<TblProjectInfoRegister> selectProjectInfoRegisterList(@Param("param") ProjectInfoRegisterQueryParam param);

    /**
     * 查询项目信息登记总数
     * 
     * @param param 查询参数
     * @return 总数
     */
    Long selectProjectInfoRegisterCount(@Param("param") ProjectInfoRegisterQueryParam param);

    /**
     * 根据登记编号查询项目信息
     * 
     * @param registerNo 登记编号
     * @return 项目信息
     */
    TblProjectInfoRegister selectByRegisterNo(@Param("registerNo") String registerNo);

    /**
     * 检查登记编号是否存在
     * 
     * @param registerNo 登记编号
     * @param excludeProjectId 排除的项目ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsRegisterNo(@Param("registerNo") String registerNo, @Param("excludeProjectId") String excludeProjectId);

    /**
     * 获取需要首谈报备的项目列表
     * 
     * @return 需要首谈报备的项目列表
     */
    List<TblProjectInfoRegister> selectFirstTalkReportProjects();

    /**
     * 批量更新项目状态
     * 
     * @param projectIds 项目ID列表
     * @param projectStatus 项目状态
     * @return 更新数量
     */
    int batchUpdateProjectStatus(@Param("projectIds") List<String> projectIds, @Param("projectStatus") Integer projectStatus);

    /**
     * 批量更新报备状态
     * 
     * @param projectIds 项目ID列表
     * @param reportStatus 报备状态
     * @return 更新数量
     */
    int batchUpdateReportStatus(@Param("projectIds") List<String> projectIds, @Param("reportStatus") Integer reportStatus);

    /**
     * 根据项目名称和发包方查询重复项目
     * 
     * @param projectName 项目名称
     * @param contractorFullName 发包方全称
     * @param excludeProjectId 排除的项目ID
     * @return 重复项目列表
     */
    List<TblProjectInfoRegister> selectDuplicateProjects(@Param("projectName") String projectName, 
                                                         @Param("contractorFullName") String contractorFullName,
                                                         @Param("excludeProjectId") String excludeProjectId);

    /**
     * 获取最新的登记编号（用于生成新编号）
     * 
     * @param prefix 编号前缀
     * @return 最新编号
     */
    String selectLatestRegisterNo(@Param("prefix") String prefix);
}
