package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.entity.TblTravelArchive;
import com.financial.sharing.entity.TblTravelArchivePrice;
import com.financial.sharing.entity.TblTravelArchiveAgreement;
import com.financial.sharing.entity.TblTravelArchiveEvaluation;
import com.financial.sharing.mapper.TblTravelArchiveMapper;
import com.financial.sharing.mapper.TblTravelArchivePriceMapper;
import com.financial.sharing.mapper.TblTravelArchiveAgreementMapper;
import com.financial.sharing.mapper.TblTravelArchiveEvaluationMapper;
import com.financial.sharing.service.TblTravelArchiveService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 商旅档案服务实现类
 *
 * @author Financial Sharing System
 * @since 2025-01-30
 */
@Slf4j
@Service
public class TblTravelArchiveServiceImpl implements TblTravelArchiveService {

    @Autowired
    private TblTravelArchiveMapper travelArchiveMapper;

    @Autowired
    private TblTravelArchivePriceMapper priceMapper;

    @Autowired
    private TblTravelArchiveAgreementMapper agreementMapper;

    @Autowired
    private TblTravelArchiveEvaluationMapper evaluationMapper;

    @Override
    public MyJsonBean<PageResult<TblTravelArchive>> getList(Object param) {
        try {
            log.info("查询商旅档案列表，参数: {}", param);

            // 解析查询参数
            Map<String, Object> paramMap = (Map<String, Object>) param;

            // 支持 page/size 和 pageNo/pageSize 两种参数格式
            Integer pageNo = null;
            Integer pageSize = null;

            if (paramMap.get("page") != null) {
                pageNo = (Integer) paramMap.get("page") + 1; // 前端传的是从0开始，需要+1
            } else if (paramMap.get("pageNo") != null) {
                pageNo = (Integer) paramMap.get("pageNo");
            } else {
                pageNo = 1;
            }

            if (paramMap.get("size") != null) {
                pageSize = (Integer) paramMap.get("size");
            } else if (paramMap.get("pageSize") != null) {
                pageSize = (Integer) paramMap.get("pageSize");
            } else {
                pageSize = 10;
            }

            String archiveName = (String) paramMap.get("archiveName");
            String archiveType = (String) paramMap.get("archiveType");
            String providerName = (String) paramMap.get("providerName");
            Integer isEnabled = paramMap.get("isEnabled") != null ? (Integer) paramMap.get("isEnabled") : null;

            // 构建查询条件
            LambdaQueryWrapper<TblTravelArchive> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(StringUtils.hasText(archiveName), TblTravelArchive::getArchiveName, archiveName)
                    .eq(StringUtils.hasText(archiveType), TblTravelArchive::getArchiveType, archiveType)
                    .like(StringUtils.hasText(providerName), TblTravelArchive::getProviderName, providerName)
                    .eq(isEnabled != null, TblTravelArchive::getIsEnabled, isEnabled)
                    .orderByDesc(TblTravelArchive::getCreateTime);

            // 分页查询
            Page<TblTravelArchive> page = new Page<>(pageNo, pageSize);
            IPage<TblTravelArchive> pageResult = travelArchiveMapper.selectPage(page, queryWrapper);

            // 封装返回结果
            PageResult<TblTravelArchive> result = new PageResult<>();
            result.setTotalRecord((int) pageResult.getTotal());
            result.setCurrentPage((int) pageResult.getCurrent());
            result.setTotalPage((int) pageResult.getPages());
            result.setPageSize((int) pageResult.getSize());
            result.setTlist(pageResult.getRecords());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询商旅档案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<TblTravelArchive> getById(String archiveId) {
        try {
            log.info("查询商旅档案详情，archiveId: {}", archiveId);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            // 调用Mapper查询数据库
            TblTravelArchive archive = travelArchiveMapper.selectById(archiveId);

            if (archive == null) {
                return MyJsonBean.errorData("档案不存在");
            }

            return MyJsonBean.successData(archive);
        } catch (Exception e) {
            log.error("查询商旅档案详情失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(Object param) {
        try {
            log.info("保存或更新商旅档案，参数: {}", param);

            // 解析保存参数
            Map<String, Object> paramMap = (Map<String, Object>) param;

            TblTravelArchive archive = new TblTravelArchive();
            archive.setArchiveId((String) paramMap.get("archiveId"));
            archive.setArchiveCode((String) paramMap.get("archiveCode"));
            archive.setArchiveName((String) paramMap.get("archiveName"));
            archive.setArchiveType((String) paramMap.get("archiveType"));
            archive.setProviderName((String) paramMap.get("providerName"));
            archive.setProviderCode((String) paramMap.get("providerCode"));
            archive.setContactPerson((String) paramMap.get("contactPerson"));
            archive.setContactPhone((String) paramMap.get("contactPhone"));
            archive.setContactEmail((String) paramMap.get("contactEmail"));
            archive.setAddress((String) paramMap.get("address"));
            archive.setStarLevel(paramMap.get("starLevel") != null ? (Integer) paramMap.get("starLevel") : null);
            archive.setBusinessLicense((String) paramMap.get("businessLicense"));
            archive.setTaxNumber((String) paramMap.get("taxNumber"));
            archive.setBankAccount((String) paramMap.get("bankAccount"));
            archive.setBankName((String) paramMap.get("bankName"));

            // 处理 isEnabled 字段：前端传来的可能是 Boolean 类型，需要转换为 Integer (1/0)
            Object isEnabledObj = paramMap.get("isEnabled");
            if (isEnabledObj != null) {
                if (isEnabledObj instanceof Boolean) {
                    archive.setIsEnabled((Boolean) isEnabledObj ? 1 : 0);
                } else if (isEnabledObj instanceof Integer) {
                    archive.setIsEnabled((Integer) isEnabledObj);
                } else {
                    archive.setIsEnabled(1); // 默认启用
                }
            } else {
                archive.setIsEnabled(1); // 默认启用
            }

            archive.setRemark((String) paramMap.get("description"));

            // 判断是新增还是更新
            if (archive.getArchiveId() == null || archive.getArchiveId().trim().isEmpty()) {
                // 新增
                archive.setArchiveId(UUID.randomUUID().toString().replace("-", ""));
                archive.setCreateTime(LocalDateTime.now());
                archive.setCreateUser("system"); // TODO: 从上下文获取当前用户
                travelArchiveMapper.insert(archive);
            } else {
                // 更新
                archive.setUpdateTime(LocalDateTime.now());
                archive.setUpdateUser("system"); // TODO: 从上下文获取当前用户
                travelArchiveMapper.updateById(archive);
            }

            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存或更新商旅档案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String archiveId) {
        try {
            log.info("删除商旅档案，archiveId: {}", archiveId);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            // 验证档案是否存在
            TblTravelArchive archive = travelArchiveMapper.selectById(archiveId);
            if (archive == null) {
                return MyJsonBean.errorData("档案不存在");
            }

            // 调用Mapper删除数据
            int result = travelArchiveMapper.deleteById(archiveId);
            if (result > 0) {
                return MyJsonBean.successMsg("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除商旅档案失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String archiveId, Integer isEnabled) {
        try {
            log.info("更新商旅档案状态，archiveId: {}, isEnabled: {}", archiveId, isEnabled);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            if (isEnabled == null) {
                return MyJsonBean.errorData("状态值不能为空");
            }

            // 验证档案是否存在
            TblTravelArchive archive = travelArchiveMapper.selectById(archiveId);
            if (archive == null) {
                return MyJsonBean.errorData("档案不存在");
            }

            // 更新状态
            archive.setIsEnabled(isEnabled);
            archive.setUpdateTime(LocalDateTime.now());
            archive.setUpdateUser("system"); // TODO: 从上下文获取当前用户

            int result = travelArchiveMapper.updateById(archive);
            if (result > 0) {
                return MyJsonBean.successMsg("状态更新成功");
            } else {
                return MyJsonBean.errorData("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新商旅档案状态失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getPriceList(String archiveId) {
        try {
            log.info("获取商旅档案价格列表，archiveId: {}", archiveId);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            // 调用Mapper查询关联的价格表
            List<TblTravelArchivePrice> prices = priceMapper.selectByArchiveId(archiveId);

            // 转换为Map列表
            List<Map<String, Object>> priceList = new ArrayList<>();
            if (prices != null && !prices.isEmpty()) {
                for (TblTravelArchivePrice price : prices) {
                    Map<String, Object> priceMap = new HashMap<>();
                    priceMap.put("priceId", price.getPriceId());
                    priceMap.put("archiveId", price.getArchiveId());
                    priceMap.put("priceType", price.getPriceType());
                    priceMap.put("priceDescription", price.getPriceDescription());
                    priceMap.put("unitPrice", price.getUnitPrice());
                    priceMap.put("currency", price.getCurrency());
                    priceMap.put("effectiveDate", price.getEffectiveDate());
                    priceMap.put("expiryDate", price.getExpiryDate());
                    priceMap.put("remark", price.getRemark());
                    priceMap.put("isEnabled", price.getIsEnabled());
                    priceMap.put("createTime", price.getCreateTime());
                    priceMap.put("createUser", price.getCreateUser());
                    priceMap.put("updateTime", price.getUpdateTime());
                    priceMap.put("updateUser", price.getUpdateUser());
                    priceList.add(priceMap);
                }
            }

            return MyJsonBean.successData(priceList);
        } catch (Exception e) {
            log.error("获取商旅档案价格列表失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean savePriceList(String archiveId, List<Map<String, Object>> prices) {
        try {
            log.info("保存商旅档案价格列表，archiveId: {}, prices: {}", archiveId, prices);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            if (prices == null || prices.isEmpty()) {
                return MyJsonBean.errorData("价格列表不能为空");
            }

            // 验证档案是否存在
            TblTravelArchive archive = travelArchiveMapper.selectById(archiveId);
            if (archive == null) {
                return MyJsonBean.errorData("档案不存在");
            }

            // 保存价格信息到价格表
            for (Map<String, Object> priceMap : prices) {
                TblTravelArchivePrice price = new TblTravelArchivePrice();

                // 设置价格ID
                String priceId = (String) priceMap.get("priceId");
                if (priceId == null || priceId.trim().isEmpty()) {
                    // 新增
                    price.setPriceId(UUID.randomUUID().toString().replace("-", ""));
                    price.setCreateTime(LocalDateTime.now());
                    price.setCreateUser("system"); // TODO: 从上下文获取当前用户
                } else {
                    // 更新
                    price.setPriceId(priceId);
                    price.setUpdateTime(LocalDateTime.now());
                    price.setUpdateUser("system"); // TODO: 从上下文获取当前用户
                }

                // 设置其他字段
                price.setArchiveId(archiveId);
                price.setPriceType((String) priceMap.get("priceType"));
                price.setPriceDescription((String) priceMap.get("priceDescription"));

                // 处理单价
                Object unitPriceObj = priceMap.get("unitPrice");
                BigDecimal unitPriceValue = null;
                if (unitPriceObj != null) {
                    if (unitPriceObj instanceof BigDecimal) {
                        unitPriceValue = (BigDecimal) unitPriceObj;
                    } else {
                        unitPriceValue = new BigDecimal(unitPriceObj.toString());
                    }
                    price.setUnitPrice(unitPriceValue);
                    // 同时设置PRICE字段（向后兼容）
                    price.setPrice(unitPriceValue);
                }

                price.setCurrency((String) priceMap.get("currency"));

                // 处理日期
                Object effectiveDateObj = priceMap.get("effectiveDate");
                if (effectiveDateObj != null) {
                    if (effectiveDateObj instanceof LocalDate) {
                        price.setEffectiveDate((LocalDate) effectiveDateObj);
                    } else {
                        price.setEffectiveDate(LocalDate.parse(effectiveDateObj.toString()));
                    }
                }

                Object expiryDateObj = priceMap.get("expiryDate");
                if (expiryDateObj != null) {
                    if (expiryDateObj instanceof LocalDate) {
                        price.setExpiryDate((LocalDate) expiryDateObj);
                    } else {
                        price.setExpiryDate(LocalDate.parse(expiryDateObj.toString()));
                    }
                }

                price.setRemark((String) priceMap.get("remark"));
                price.setIsEnabled(1); // 默认启用

                // 保存或更新
                if (priceId == null || priceId.trim().isEmpty()) {
                    priceMapper.insert(price);
                } else {
                    priceMapper.updateById(price);
                }
            }

            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存商旅档案价格列表失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getAgreements(String archiveId) {
        try {
            log.info("获取商旅档案合作协议，archiveId: {}", archiveId);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            // 调用Mapper查询关联的协议表
            List<TblTravelArchiveAgreement> agreements = agreementMapper.selectByArchiveId(archiveId);

            // 转换为Map列表
            List<Map<String, Object>> agreementList = new ArrayList<>();
            if (agreements != null && !agreements.isEmpty()) {
                for (TblTravelArchiveAgreement agreement : agreements) {
                    Map<String, Object> agreementMap = new HashMap<>();
                    agreementMap.put("agreementId", agreement.getAgreementId());
                    agreementMap.put("archiveId", agreement.getArchiveId());
                    agreementMap.put("agreementName", agreement.getAgreementName());
                    agreementMap.put("agreementType", agreement.getAgreementType());
                    agreementMap.put("signDate", agreement.getSignDate());
                    agreementMap.put("effectiveDate", agreement.getEffectiveDate());
                    agreementMap.put("expiryDate", agreement.getExpiryDate());
                    agreementMap.put("agreementAmount", agreement.getAgreementAmount());
                    agreementMap.put("paymentTerms", agreement.getPaymentTerms());
                    agreementMap.put("agreementContent", agreement.getAgreementContent());
                    agreementMap.put("attachmentUrl", agreement.getAttachmentUrl());
                    agreementMap.put("isEnabled", agreement.getIsEnabled());
                    agreementMap.put("createTime", agreement.getCreateTime());
                    agreementMap.put("createUser", agreement.getCreateUser());
                    agreementMap.put("updateTime", agreement.getUpdateTime());
                    agreementMap.put("updateUser", agreement.getUpdateUser());
                    agreementMap.put("remark", agreement.getRemark());
                    agreementList.add(agreementMap);
                }
            }

            return MyJsonBean.successData(agreementList);
        } catch (Exception e) {
            log.error("获取商旅档案合作协议失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveAgreementList(String archiveId, List<Map<String, Object>> agreements) {
        try {
            log.info("保存商旅档案合作协议列表，archiveId: {}, agreements: {}", archiveId, agreements);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            if (agreements == null || agreements.isEmpty()) {
                return MyJsonBean.errorData("协议列表不能为空");
            }

            // 验证档案是否存在
            TblTravelArchive archive = travelArchiveMapper.selectById(archiveId);
            if (archive == null) {
                return MyJsonBean.errorData("档案不存在");
            }

            // 保存协议信息到协议表
            for (Map<String, Object> agreementMap : agreements) {
                TblTravelArchiveAgreement agreement = new TblTravelArchiveAgreement();

                // 设置协议ID
                String agreementId = (String) agreementMap.get("agreementId");
                if (agreementId == null || agreementId.trim().isEmpty()) {
                    // 新增
                    agreement.setAgreementId(UUID.randomUUID().toString().replace("-", ""));
                    agreement.setCreateTime(LocalDateTime.now());
                    agreement.setCreateUser("system"); // TODO: 从上下文获取当前用户
                } else {
                    // 更新
                    agreement.setAgreementId(agreementId);
                    agreement.setUpdateTime(LocalDateTime.now());
                    agreement.setUpdateUser("system"); // TODO: 从上下文获取当前用户
                }

                // 设置其他字段
                agreement.setArchiveId(archiveId);
                agreement.setAgreementName((String) agreementMap.get("agreementName"));
                agreement.setAgreementType((String) agreementMap.get("agreementType"));

                // 处理日期
                Object signDateObj = agreementMap.get("signDate");
                if (signDateObj != null) {
                    if (signDateObj instanceof LocalDate) {
                        agreement.setSignDate((LocalDate) signDateObj);
                    } else {
                        agreement.setSignDate(LocalDate.parse(signDateObj.toString()));
                    }
                }

                Object effectiveDateObj = agreementMap.get("effectiveDate");
                if (effectiveDateObj != null) {
                    if (effectiveDateObj instanceof LocalDate) {
                        agreement.setEffectiveDate((LocalDate) effectiveDateObj);
                    } else {
                        agreement.setEffectiveDate(LocalDate.parse(effectiveDateObj.toString()));
                    }
                }

                Object expiryDateObj = agreementMap.get("expiryDate");
                if (expiryDateObj != null) {
                    if (expiryDateObj instanceof LocalDate) {
                        agreement.setExpiryDate((LocalDate) expiryDateObj);
                    } else {
                        agreement.setExpiryDate(LocalDate.parse(expiryDateObj.toString()));
                    }
                }

                // 处理协议金额
                Object agreementAmountObj = agreementMap.get("agreementAmount");
                if (agreementAmountObj != null) {
                    if (agreementAmountObj instanceof BigDecimal) {
                        agreement.setAgreementAmount((BigDecimal) agreementAmountObj);
                    } else {
                        agreement.setAgreementAmount(new BigDecimal(agreementAmountObj.toString()));
                    }
                }

                agreement.setPaymentTerms((String) agreementMap.get("paymentTerms"));
                agreement.setAgreementContent((String) agreementMap.get("agreementContent"));
                agreement.setAttachmentUrl((String) agreementMap.get("attachmentUrl"));
                agreement.setRemark((String) agreementMap.get("remark"));
                agreement.setIsEnabled(1); // 默认启用

                // 保存或更新
                if (agreementId == null || agreementId.trim().isEmpty()) {
                    agreementMapper.insert(agreement);
                } else {
                    agreementMapper.updateById(agreement);
                }
            }

            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存商旅档案合作协议列表失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getEvaluations(String archiveId) {
        try {
            log.info("获取商旅档案评价记录，archiveId: {}", archiveId);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            // TODO: 实现评价记录查询逻辑
            // 1. 调用Mapper查询关联的评价表
            // 2. 封装返回结果

            List<Map<String, Object>> evaluations = new ArrayList<>();

            return MyJsonBean.successData(evaluations);
        } catch (Exception e) {
            log.error("获取商旅档案评价记录失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveEvaluation(String archiveId, Map<String, Object> evaluation) {
        try {
            log.info("保存商旅档案评价，archiveId: {}, evaluation: {}", archiveId, evaluation);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            if (evaluation == null || evaluation.isEmpty()) {
                return MyJsonBean.errorData("评价信息不能为空");
            }

            // 验证档案是否存在
            TblTravelArchive archive = travelArchiveMapper.selectById(archiveId);
            if (archive == null) {
                return MyJsonBean.errorData("档案不存在");
            }

            // 创建评价实体
            TblTravelArchiveEvaluation eval = new TblTravelArchiveEvaluation();
            eval.setEvaluationId(UUID.randomUUID().toString().replace("-", ""));
            eval.setArchiveId(archiveId);

            // 提取评分
            Integer serviceScore = convertToInteger(evaluation.get("serviceScore"));
            Integer priceScore = convertToInteger(evaluation.get("priceScore"));
            Integer environmentScore = convertToInteger(evaluation.get("environmentScore"));

            eval.setServiceScore(serviceScore);
            eval.setPriceScore(priceScore);
            eval.setEnvironmentScore(environmentScore);

            // 计算综合评分（三项评分的平均值）
            if (serviceScore != null && priceScore != null && environmentScore != null) {
                BigDecimal avg = new BigDecimal(serviceScore + priceScore + environmentScore)
                    .divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
                eval.setOverallScore(avg);
            }

            eval.setContent((String) evaluation.get("evaluationContent"));
            eval.setEvaluationUser("system"); // TODO: 从上下文获取当前用户
            eval.setEvaluationUserName("system"); // TODO: 从上下文获取当前用户名
            eval.setEvaluationTime(LocalDateTime.now());
            eval.setCreateTime(LocalDateTime.now());

            // 保存到数据库
            evaluationMapper.insert(eval);

            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存商旅档案评价失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 转换为Integer类型
     */
    private Integer convertToInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public MyJsonBean<BigDecimal> getAvgScore(String archiveId) {
        try {
            log.info("获取商旅档案平均评分，archiveId: {}", archiveId);

            if (archiveId == null || archiveId.trim().isEmpty()) {
                return MyJsonBean.errorData("档案ID不能为空");
            }

            // TODO: 实现平均评分计算逻辑
            // 1. 调用Mapper查询评价记录
            // 2. 计算平均评分
            // 3. 封装返回结果

            BigDecimal avgScore = BigDecimal.ZERO;

            return MyJsonBean.successData(avgScore);
        } catch (Exception e) {
            log.error("获取商旅档案平均评分失败，archiveId: {}", archiveId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
