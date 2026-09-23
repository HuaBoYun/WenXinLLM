package com.huabo.financialdata.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.financialdata.entity.dto.gbi.GbiTableDataQuery;
import com.huabo.financialdata.entity.entity.GbiApplicationConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.financialdata.entity.entity.GbiBatch;
import com.huabo.financialdata.entity.entity.GbiKnowledge;
import com.huabo.financialdata.entity.entity.GbiTableColumns;
import com.huabo.financialdata.entity.vo.gbi.GbiBatchListVO;
import com.huabo.financialdata.entity.vo.gbi.GbiKnowledgeRequestVO;
import com.huabo.financialdata.entity.vo.gbi.GbiTableListVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用配置表 服务类
 * </p>
 *
 * @author 
 * @since 2024-04-20
 */
public interface GbiApplicationService extends IService<GbiApplicationConfig> {

    /**
     * 获取应用配置
     * @return
     */
    GbiApplicationConfig getApplicationConfig(BigDecimal userId);

    /**
     * 保存更新应用配置
     * @return
     */
    Boolean saveApplicationConfig(TblStaffUtil staff, GbiApplicationConfig requestVO);

    /**
     * 知识列表
     * @param userId
     * @return
     */
    List<GbiKnowledge> getKnowledgeList(BigDecimal userId);

    /**
     * 添加知识
     * @param staff
     * @param param
     * @return
     */
    Boolean addKnowledge(TblStaffUtil staff, GbiKnowledgeRequestVO param);

    /**
     * 修改知识
     * @param param
     * @return
     */
    Boolean updateKnowledge(GbiKnowledgeRequestVO param);

    /**
     * 删除知识
     * @param knowledgeId
     * @return
     */
    Boolean deleteKnowledge(String knowledgeId);

    // ----------大模型数据导入-------------------

    /**
     * 分页查询导入批次信息
     * @param userId
     * @return
     */
    IPage<GbiBatchListVO> queryBatch(BigDecimal userId, Page<GbiBatch> page);

    /**
     * 查询用户导入记录（查询导入数据的批次记录）
     * @param userId
     * @return
     */
    List<GbiBatchListVO> getBatchList(BigDecimal userId);

    /**
     * 查询tableList
     * @param userId
     * @param batchId
     * @return
     */
    List<GbiTableListVO> getTableList(BigDecimal userId, String batchId);

    /**
     * 查询表字段
     * @param tableId
     * @return
     */
    List<GbiTableColumns> getTableColumns(String tableId);

    /**
     * 数据详情
     * @param staffUtil
     * @param query
     * @return
     */
    IPage<Map> getTableData(TblStaffUtil staffUtil, GbiTableDataQuery query);

    /**
     * 修改表、字段信息
     * @param gbiTableListVOS
     * @return
     */
    boolean updateTableAndSettings(List<GbiTableListVO> gbiTableListVOS);

    /**
     * 上传数据
     * @param staff
     * @param file
     * @return
     * @throws Exception
     */
    String uploadData(TblStaffUtil staff, MultipartFile file) throws Exception;
}
