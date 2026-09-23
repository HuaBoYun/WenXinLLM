package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TcSealArchive;

import java.util.Map;

/**
 * 印鉴档案Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
public interface TblSealArchiveService extends IService<TcSealArchive> {

    /**
     * 分页查询印鉴档案
     */
    IPage<TcSealArchive> getSealArchivePage(Integer page, Integer limit,
            String sealCode, String sealName, Long sealTypeId, String ownerName, Integer isActive);

    /**
     * 获取印鉴统计数据
     */
    Map<String, Object> getSealStatistics();
}
