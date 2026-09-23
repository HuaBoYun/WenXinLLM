package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.DataModelQueryDTO;
import com.huabo.fxgl.dto.DataModelSaveDTO;
import com.huabo.fxgl.entity.TblDataModel;
import com.hbfk.entity.TblStaffUtil;

import java.util.List;
import java.util.Map;

/**
 * 数据模型管理服务接口
 * 
 * @author 华博云
 * @since 2025-01-21
 */
public interface IDataModelService extends IService<TblDataModel> {

    /**
     * 分页查询数据模型列表
     * 
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    JsonBean getDataModelList(DataModelQueryDTO queryDTO);

    /**
     * 保存数据模型
     *
     * @param saveDTO 保存参数
     * @param currentUser 当前用户
     * @return 保存结果
     */
    JsonBean saveDataModel(DataModelSaveDTO saveDTO, String currentUser);

    /**
     * 删除数据模型
     *
     * @param modelId 模型ID
     * @return 删除结果
     */
    JsonBean deleteDataModel(String modelId);

    /**
     * 批量删除数据模型
     *
     * @param modelIds 模型ID列表
     * @return 删除结果
     */
    JsonBean batchDeleteDataModel(List<String> modelIds);

    /**
     * 获取数据模型详情
     *
     * @param modelId 模型ID
     * @return 模型详情
     */
    JsonBean getDataModelDetail(String modelId);

    /**
     * 复制数据模型
     *
     * @param modelId 源模型ID
     * @param newModelName 新模型名称
     * @param currentUser 当前用户
     * @return 复制结果
     */
    JsonBean copyDataModel(String modelId, String newModelName, String currentUser);

    /**
     * 获取模型版本列表
     *
     * @param modelCode 模型编码
     * @return 版本列表
     */
    JsonBean getDataModelVersions(String modelCode);

    /**
     * 创建新版本
     *
     * @param modelId 模型ID
     * @param changeDescription 变更说明
     * @param currentUser 当前用户
     * @return 创建结果
     */
    JsonBean createDataModelVersion(String modelId, String changeDescription, String currentUser);

    /**
     * 更新模型状态
     *
     * @param modelId 模型ID
     * @param status 状态
     * @param currentUser 当前用户
     * @return 更新结果
     */
    JsonBean updateDataModelStatus(String modelId, String status, String currentUser);

    /**
     * 发布模型
     *
     * @param modelId 模型ID
     * @param currentUser 当前用户
     * @return 发布结果
     */
    JsonBean publishDataModel(String modelId, String currentUser);

    /**
     * 测试模型SQL
     *
     * @param dataSourceId 数据源ID
     * @param sqlStatement SQL语句
     * @param parameters 参数列表
     * @param staff 当前用户
     * @param indicatorCode 组合指标编码
     * @return 测试结果
     */
    JsonBean testDataModelSql(String dataSourceId, String sqlStatement, List<Map<String, Object>> parameters, TblStaffUtil staff, String indicatorCode);

    /**
     * 执行模型
     *
     * @param modelId 模型ID
     * @param parameters 参数
     * @param currentUser 当前用户
     * @return 执行结果
     */
    JsonBean executeDataModel(String modelId, Map<String, Object> parameters, String currentUser);

    /**
     * 获取模型统计信息
     *
     * @return 统计信息
     */
    JsonBean getDataModelStatistics();

    /**
     * 搜索数据模型
     *
     * @param keyword 关键词
     * @param modelType 模型类型
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 搜索结果
     */
    JsonBean searchDataModels(String keyword, String modelType, Integer pageNum, Integer pageSize);

    /**
     * 获取热门模型
     *
     * @param limit 限制数量
     * @return 热门模型列表
     */
    JsonBean getPopularDataModels(Integer limit);

    /**
     * 生成模型代码
     *
     * @param modelId 模型ID
     * @return 生成的代码
     */
    JsonBean generateDataModelCode(String modelId);

    /**
     * 验证模型编码是否唯一
     * 
     * @param modelCode 模型编码
     * @param modelId 模型ID(更新时传入)
     * @return 是否唯一
     */
    boolean isModelCodeUnique(String modelCode, String modelId);

    /**
     * 解析SQL语句结构
     *
     * @param sqlStatement SQL语句
     * @param dataSourceId 数据源ID
     * @return 解析结果
     */
    JsonBean parseSQLStatement(String sqlStatement, String dataSourceId);

    /**
     * 生成SQL模板
     * 将数据模型转换为可复用的SQL模板
     *
     * @param modelId 模型ID
     * @param currentUser 当前用户
     * @return 生成结果
     */
    JsonBean generateSqlTemplate(String modelId, String currentUser);

    /**
     * 从组合指标批量同步指标到数据模型（#TASK-2026-08-01-DATA-MODEL-SYNC）。
     * <p>规则：
     * <ul>
     *   <li>combinationIds 为空 → 同步全部组合</li>
     *   <li>每个组合按 executionOrder 升序，跳过最后一个指标不同步</li>
     *   <li>onlyEnabled=true 时仅同步启用状态的指标</li>
     *   <li>幂等：以 SOURCE_INDICATOR_CONFIG_ID 为键 upsert</li>
     * </ul>
     *
     * @param combinationIds 组合ID列表，可为 null/空
     * @param onlyEnabled 是否仅同步启用的指标
     * @param currentUser 当前用户
     * @return 同步统计结果 {total, insertCount, updateCount, skipCount, details}
     */
    JsonBean syncFromCombinations(java.util.List<String> combinationIds, boolean onlyEnabled, String currentUser);

    /**
     * 单条 upsert：把一个组合指标同步为一条数据模型。
     * 供 CombinationService.addIndicatorToCombination 结束时复用。
     *
     * @param indicator 组合指标记录（driven by combination.indicators[i]）
     * @param combination 所属组合信息
     * @param currentUser 当前用户
     * @return 是否新插入（true=insert / false=update）
     */
    boolean upsertFromIndicator(java.util.Map<String, Object> indicator,
                                java.util.Map<String, Object> combination,
                                String currentUser);
}
