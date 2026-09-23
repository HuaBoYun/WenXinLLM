package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryDictionary;
import com.global.treasurer.mapper.RegulatoryDictionaryMapper;
import com.global.treasurer.service.RegulatoryDictionaryService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 监管数据字典服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class RegulatoryDictionaryServiceImpl implements RegulatoryDictionaryService {
    @Autowired
    private RegulatoryDictionaryMapper dictionaryMapper;

    @Override
    public PageInfo<TblRegulatoryDictionary> getDictionaryList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblRegulatoryDictionary> list = dictionaryMapper.selectDictionaryList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblRegulatoryDictionary getDictionaryById(String dictionaryId) {
        TblRegulatoryDictionary dictionary = dictionaryMapper.selectDictionaryById(dictionaryId);
        if (dictionary == null) {
            throw new ServiceException(404, "数据字典不存在");
        }
        return dictionary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryDictionary saveDictionary(TblRegulatoryDictionary dictionary) {
        if (dictionary.getDictionaryId() == null || dictionary.getDictionaryId().isEmpty()) {
            dictionary.setDeleteFlag(0);
            dictionary.setIsEnabled(1);
            dictionary.setCreatedTime(new Date());
            dictionaryMapper.insert(dictionary);
        } else {
            dictionary.setUpdatedTime(new Date());
            dictionaryMapper.updateById(dictionary);
        }
        return dictionary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictionary(String dictionaryId) {
        TblRegulatoryDictionary dictionary = getDictionaryById(dictionaryId);
        dictionary.setDeleteFlag(1);
        dictionary.setUpdatedTime(new Date());
        dictionaryMapper.updateById(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDictionaries(List<String> dictionaryIds) {
        dictionaryMapper.batchDeleteByIds(dictionaryIds);
    }

    @Override
    public List<TblRegulatoryDictionary> getDictionariesByType(String dictType) {
        return dictionaryMapper.selectByDictType(dictType);
    }
}

