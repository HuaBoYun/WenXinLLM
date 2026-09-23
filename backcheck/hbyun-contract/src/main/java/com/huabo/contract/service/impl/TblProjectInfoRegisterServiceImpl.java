package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.TblProjectInfoRegister;
import com.huabo.contract.mapper.TblProjectInfoRegisterMapper;
import com.huabo.contract.service.TblProjectInfoRegisterService;
import com.huabo.contract.vo.ProjectInfoRegisterQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 项目信息登记表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TblProjectInfoRegisterServiceImpl extends ServiceImpl<TblProjectInfoRegisterMapper, TblProjectInfoRegister> 
        implements TblProjectInfoRegisterService {

    @Autowired
    private TblProjectInfoRegisterMapper projectInfoRegisterMapper;

    @Override
    public PageInfo<TblProjectInfoRegister> getProjectInfoRegisterList(ProjectInfoRegisterQueryParam param) {
        log.info("分页查询项目信息登记列表，参数：{}", param);
        
        // 设置分页参数
        PageHelper.startPage(param.getPageNumber(), param.getPageSize());
        
        // 查询数据
        List<TblProjectInfoRegister> list = projectInfoRegisterMapper.selectProjectInfoRegisterList(param);
        
        // 返回分页结果
        return new PageInfo<>(list);
    }

    @Override
    public boolean saveProjectInfoRegister(TblProjectInfoRegister projectInfo) {
        log.info("保存项目信息登记，项目ID：{}", projectInfo.getProjectId());
        
        try {
            // 自动设置首谈报备标识
            autoSetFirstTalkReport(projectInfo);
            
            Date now = new Date();
            
            if (StringUtils.hasText(projectInfo.getProjectId())) {
                // 修改
                projectInfo.setUpdateTime(now);
                return updateById(projectInfo);
            } else {
                // 新增
                projectInfo.setCreateTime(now);
                projectInfo.setUpdateTime(now);
                projectInfo.setRegisterTime(now);
                
                // 如果没有登记编号，自动生成
                if (!StringUtils.hasText(projectInfo.getRegisterNo())) {
                    projectInfo.setRegisterNo(generateRegisterNo());
                }
                
                // 设置默认状态
                if (projectInfo.getProjectStatus() == null) {
                    projectInfo.setProjectStatus(1); // 默认为登记状态
                }
                if (projectInfo.getReportStatus() == null) {
                    projectInfo.setReportStatus(0); // 默认为未报备
                }
                
                return save(projectInfo);
            }
        } catch (Exception e) {
            log.error("保存项目信息登记失败", e);
            throw new RuntimeException("保存项目信息登记失败：" + e.getMessage());
        }
    }

    @Override
    public TblProjectInfoRegister getProjectInfoRegisterById(String projectId) {
        log.info("根据ID获取项目信息详情，项目ID：{}", projectId);
        return getById(projectId);
    }

    @Override
    public TblProjectInfoRegister getProjectInfoRegisterByRegisterNo(String registerNo) {
        log.info("根据登记编号获取项目信息，登记编号：{}", registerNo);
        return projectInfoRegisterMapper.selectByRegisterNo(registerNo);
    }

    @Override
    public boolean deleteProjectInfoRegister(String projectId) {
        log.info("删除项目信息登记，项目ID：{}", projectId);
        
        try {
            return removeById(projectId);
        } catch (Exception e) {
            log.error("删除项目信息登记失败", e);
            throw new RuntimeException("删除项目信息登记失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteProjectInfoRegister(List<String> projectIds) {
        log.info("批量删除项目信息登记，项目ID列表：{}", projectIds);
        
        try {
            return removeByIds(projectIds);
        } catch (Exception e) {
            log.error("批量删除项目信息登记失败", e);
            throw new RuntimeException("批量删除项目信息登记失败：" + e.getMessage());
        }
    }

    @Override
    public String generateRegisterNo() {
        log.info("生成项目登记编号");
        
        try {
            // 生成格式：XMXX + YYYYMMDD + 001
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String prefix = "XMXX" + dateStr;
            
            // 查询当天最新的编号
            String latestNo = projectInfoRegisterMapper.selectLatestRegisterNo(prefix);
            
            int sequence = 1;
            if (StringUtils.hasText(latestNo) && latestNo.length() >= prefix.length() + 3) {
                try {
                    String sequenceStr = latestNo.substring(prefix.length());
                    sequence = Integer.parseInt(sequenceStr) + 1;
                } catch (NumberFormatException e) {
                    log.warn("解析序号失败，使用默认序号1", e);
                }
            }
            
            return prefix + String.format("%03d", sequence);
        } catch (Exception e) {
            log.error("生成项目登记编号失败", e);
            throw new RuntimeException("生成项目登记编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsRegisterNo(String registerNo, String excludeProjectId) {
        log.info("检查登记编号是否存在，登记编号：{}，排除项目ID：{}", registerNo, excludeProjectId);
        return projectInfoRegisterMapper.existsRegisterNo(registerNo, excludeProjectId);
    }

    @Override
    public boolean updateProjectStatus(String projectId, Integer projectStatus) {
        log.info("更新项目状态，项目ID：{}，项目状态：{}", projectId, projectStatus);
        
        try {
            TblProjectInfoRegister projectInfo = new TblProjectInfoRegister();
            projectInfo.setProjectId(projectId);
            projectInfo.setProjectStatus(projectStatus);
            projectInfo.setUpdateTime(new Date());
            
            return updateById(projectInfo);
        } catch (Exception e) {
            log.error("更新项目状态失败", e);
            throw new RuntimeException("更新项目状态失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateProjectStatus(List<String> projectIds, Integer projectStatus) {
        log.info("批量更新项目状态，项目ID列表：{}，项目状态：{}", projectIds, projectStatus);
        
        try {
            int count = projectInfoRegisterMapper.batchUpdateProjectStatus(projectIds, projectStatus);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新项目状态失败", e);
            throw new RuntimeException("批量更新项目状态失败：" + e.getMessage());
        }
    }

    @Override
    public boolean updateReportStatus(String projectId, Integer reportStatus) {
        log.info("更新报备状态，项目ID：{}，报备状态：{}", projectId, reportStatus);
        
        try {
            TblProjectInfoRegister projectInfo = new TblProjectInfoRegister();
            projectInfo.setProjectId(projectId);
            projectInfo.setReportStatus(reportStatus);
            projectInfo.setUpdateTime(new Date());
            
            return updateById(projectInfo);
        } catch (Exception e) {
            log.error("更新报备状态失败", e);
            throw new RuntimeException("更新报备状态失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateReportStatus(List<String> projectIds, Integer reportStatus) {
        log.info("批量更新报备状态，项目ID列表：{}，报备状态：{}", projectIds, reportStatus);
        
        try {
            int count = projectInfoRegisterMapper.batchUpdateReportStatus(projectIds, reportStatus);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新报备状态失败", e);
            throw new RuntimeException("批量更新报备状态失败：" + e.getMessage());
        }
    }

    @Override
    public List<TblProjectInfoRegister> getFirstTalkReportProjects() {
        log.info("获取需要首谈报备的项目列表");
        return projectInfoRegisterMapper.selectFirstTalkReportProjects();
    }

    @Override
    public List<TblProjectInfoRegister> checkDuplicateProjects(String projectName, String contractorFullName, String excludeProjectId) {
        log.info("检查项目重复，项目名称：{}，发包方：{}，排除项目ID：{}", projectName, contractorFullName, excludeProjectId);
        return projectInfoRegisterMapper.selectDuplicateProjects(projectName, contractorFullName, excludeProjectId);
    }

    @Override
    public void autoSetFirstTalkReport(TblProjectInfoRegister projectInfo) {
        // 自动判断是否需要首谈报备（项目金额大于等于500万元）
        if (projectInfo.getProjectAmount() != null && 
            projectInfo.getProjectAmount().compareTo(new BigDecimal("5000000")) >= 0) {
            projectInfo.setIsFirstTalkReport(1);
        } else {
            projectInfo.setIsFirstTalkReport(0);
        }
    }
}
