package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 类别管理服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface CategoryService {

    List<Map<String, Object>> getFinancingCategories();

    Map<String, Object> saveFinancingCategory(Map<String, Object> category);

    void deleteFinancingCategory(Long categoryId);

    List<Map<String, Object>> getCreditCategories();

    Map<String, Object> saveCreditCategory(Map<String, Object> category);

    void deleteCreditCategory(Long categoryId);

    List<Map<String, Object>> getGuaranteeCategories();

    Map<String, Object> saveGuaranteeCategory(Map<String, Object> category);

    void deleteGuaranteeCategory(Long categoryId);

    List<Map<String, Object>> getCollateralCategories();

    Map<String, Object> saveCollateralCategory(Map<String, Object> category);

    void deleteCollateralCategory(Long categoryId);

    List<Map<String, Object>> getBankCategories();

    Map<String, Object> saveBankCategory(Map<String, Object> category);

    void deleteBankCategory(Long categoryId);

    List<Map<String, Object>> getCurrencyCategories();

    Map<String, Object> saveCurrencyCategory(Map<String, Object> category);

    void deleteCurrencyCategory(Long categoryId);
}

