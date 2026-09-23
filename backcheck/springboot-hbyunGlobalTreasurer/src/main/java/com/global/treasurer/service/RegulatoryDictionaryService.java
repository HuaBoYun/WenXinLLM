package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryDictionary;

import java.util.List;
import java.util.Map;

/**
 * 监管数据字典服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface RegulatoryDictionaryService {

    /**
     * 分页查询数据字典列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblRegulatoryDictionary> getDictionaryList(Map<String, Object> params);

    /**
     * 根据ID查询数据字典详情
     *
     * @param dictionaryId 字典ID
     * @return 数据字典
     */
    TblRegulatoryDictionary getDictionaryById(String dictionaryId);

    /**
     * 保存数据字典（新增或更新）
     *
     * @param dictionary 数据字典
     * @return 保存后的数据字典
     */
    TblRegulatoryDictionary saveDictionary(TblRegulatoryDictionary dictionary);

    /**
     * 删除数据字典
     *
     * @param dictionaryId 字典ID
     */
    void deleteDictionary(String dictionaryId);

    /**
     * 批量删除数据字典
     *
     * @param dictionaryIds 字典ID列表
     */
    void batchDeleteDictionaries(List<String> dictionaryIds);

    /**
     * 根据字典类型查询
     *
     * @param dictType 字典类型
     * @return 数据字典列表
     */
    List<TblRegulatoryDictionary> getDictionariesByType(String dictType);
}

