package com.global.treasurer.service.impl;

import com.global.treasurer.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<Map<String, Object>> getFinancingCategories() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> saveFinancingCategory(Map<String, Object> category) {
        return category;
    }

    @Override
    public void deleteFinancingCategory(Long categoryId) {
    }

    @Override
    public List<Map<String, Object>> getCreditCategories() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> saveCreditCategory(Map<String, Object> category) {
        return category;
    }

    @Override
    public void deleteCreditCategory(Long categoryId) {
    }

    @Override
    public List<Map<String, Object>> getGuaranteeCategories() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> saveGuaranteeCategory(Map<String, Object> category) {
        return category;
    }

    @Override
    public void deleteGuaranteeCategory(Long categoryId) {
    }

    @Override
    public List<Map<String, Object>> getCollateralCategories() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> saveCollateralCategory(Map<String, Object> category) {
        return category;
    }

    @Override
    public void deleteCollateralCategory(Long categoryId) {
    }

    @Override
    public List<Map<String, Object>> getBankCategories() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> saveBankCategory(Map<String, Object> category) {
        return category;
    }

    @Override
    public void deleteBankCategory(Long categoryId) {
    }

    @Override
    public List<Map<String, Object>> getCurrencyCategories() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> saveCurrencyCategory(Map<String, Object> category) {
        return category;
    }

    @Override
    public void deleteCurrencyCategory(Long categoryId) {
    }
}

