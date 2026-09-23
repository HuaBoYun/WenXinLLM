package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblRegulatoryDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 监管数据字典Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface RegulatoryDictionaryMapper extends BaseMapper<TblRegulatoryDictionary> {

    /**
     * 分页查询数据字典列表
     *
     * @param params 查询参数
     * @return 数据字典列表
     */
    List<TblRegulatoryDictionary> selectDictionaryList(Map<String, Object> params);

    /**
     * 根据ID查询数据字典详情
     *
     * @param dictionaryId 字典ID
     * @return 数据字典
     */
    TblRegulatoryDictionary selectDictionaryById(@Param("dictionaryId") String dictionaryId);

    /**
     * 根据字典代码查询
     *
     * @param dictCode 字典代码
     * @return 数据字典
     */
    TblRegulatoryDictionary selectByDictCode(@Param("dictCode") String dictCode);

    /**
     * 根据字典类型查询
     *
     * @param dictType 字典类型
     * @return 数据字典列表
     */
    List<TblRegulatoryDictionary> selectByDictType(@Param("dictType") String dictType);

    /**
     * 批量删除数据字典（逻辑删除）
     *
     * @param dictionaryIds 字典ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("dictionaryIds") List<String> dictionaryIds);
}

