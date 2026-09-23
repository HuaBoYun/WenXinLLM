package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.dto.imports.ImportBusinessSystemDTO;
import com.global.treasurer.dto.imports.ImportResultDTO;
import com.global.treasurer.entity.TcBusinessSystem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 业务系统注册Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
public interface TcBusinessSystemService extends IService<TcBusinessSystem> {

    /**
     * 分页查询业务系统
     *
     * @param page 页码
     * @param limit 每页数量
     * @param systemCode 系统编码
     * @param systemName 系统名称
     * @param systemType 系统类型
     * @param connectionStatus 连接状态
     * @param status 状态
     * @return 分页结果
     */
    IPage<TcBusinessSystem> getBusinessSystemPage(Integer page, Integer limit, String systemCode, String systemName, String systemType, String connectionStatus, String status);

    /**
     * 测试系统连接
     *
     * @param id 系统ID
     * @return 测试结果
     */
    boolean testSystemConnection(Long id);

    /**
     * 同步系统状态
     */
    void syncSystemStatus();

    /**
     * 批量导入业务系统
     *
     * @param file Excel文件
     * @param currentUser 当前用户
     * @return 导入结果
     */
    ImportResultDTO importBusinessSystems(MultipartFile file, String currentUser);

    /**
     * 检查系统编码是否存在
     *
     * @param systemCode 系统编码
     * @return true-存在，false-不存在
     */
    boolean existsBySystemCode(String systemCode);
}