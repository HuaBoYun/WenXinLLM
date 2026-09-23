package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 全球司库配置API控制器
 * 专门处理前端Vue页面调用的API接口
 * 路径映射: /centralaudit/treasury/*
 *
 * @author 华博云开发团队
 * @since 2026-01-05
 */
@RestController
@RequestMapping({"/centralaudit/treasury"})
@Api(tags = "全球司库配置API")
public class TreasuryConfigApiController {
    private static final Logger log = LoggerFactory.getLogger(TreasuryConfigApiController.class);

    @Autowired
    private TcEticketAccountService tcEticketAccountService;

    @Autowired
    private TblSealUsageRecordService tblSealUsageRecordService;

    @Autowired
    private TblSecurityParameterService tblSecurityParameterService;

    @Autowired
    private TblThirdPartyAccountService tblThirdPartyAccountService;

    @Autowired
    private TcSealCombinationService tcSealCombinationService;

    @Autowired
    private TcSealTypeService tcSealTypeService;

    // @Autowired
    // private TcSealArchiveService tcSealArchiveService;  // 暂时注释,Service不存在

    // @Autowired
    // private TcUkeyVendorService tcUkeyVendorService;  // 暂时注释,Service不存在

    @Resource
    private UserProvider userProvider;

    // ==================== 3. 电票账户配置模块 ====================

    /**
     * 获取电票账户列表(分页) - 前端路径: /eTicket/list
     */
    @RequestMapping(value = "/eTicket/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取电票账户列表")
    public JsonBean getETicketAccountList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String eTicketSystem,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) String bankCode) {

        try {
            // 构建查询条件
            LambdaQueryWrapper<TcEticketAccount> queryWrapper = new LambdaQueryWrapper<>();

            // 已删除accountNumber和accountName字段，使用accountNo代替
            if (!StringUtils.isEmpty(accountNumber)) {
                queryWrapper.like(TcEticketAccount::getAccountNo, accountNumber);
            }
            if (!StringUtils.isEmpty(accountName)) {
                queryWrapper.like(TcEticketAccount::getAccountNo, accountName);
            }
            // 字段已从数据库表中移除: ETICKET_SYSTEM, ACCOUNT_TYPE
            // if (!StringUtils.isEmpty(eTicketSystem)) {
            //     queryWrapper.eq(TcEticketAccount::getETicketSystem, eTicketSystem);
            // }
            // if (!StringUtils.isEmpty(accountType)) {
            //     queryWrapper.eq(TcEticketAccount::getAccountType, accountType);
            // }
            if (!StringUtils.isEmpty(bankCode)) {
                queryWrapper.eq(TcEticketAccount::getBankCode, bankCode);
            }

            // 分页查询
            Page<TcEticketAccount> pageParam = new Page<>(page, limit);
            IPage<TcEticketAccount> pageResult = tcEticketAccountService.page(pageParam, queryWrapper);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageResult.getRecords());
            result.put("totalRecord", pageResult.getTotal());
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取电票账户列表成功", result);

        } catch (Exception e) {
            log.error("获取电票账户列表失败", e);
            return new JsonBean(0, "获取电票账户列表失败: " + e.getMessage(), null);
        }
    }

    // ==================== 3.5 第三方账户配置模块 ====================

    /**
     * 获取第三方账户列表(分页) - 前端路径: /thirdParty/account/list
     */
    @RequestMapping(value = "/thirdParty/account/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取第三方账户列表")
    public JsonBean getThirdPartyAccountList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String accountCode,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String thirdPartySystem,
            @RequestParam(required = false) String accountType) {

        try {
            // 构建查询条件
            LambdaQueryWrapper<TblThirdPartyAccount> queryWrapper = new LambdaQueryWrapper<>();

            if (!StringUtils.isEmpty(accountCode)) {
                queryWrapper.like(TblThirdPartyAccount::getAccountCode, accountCode);
            }
            if (!StringUtils.isEmpty(accountName)) {
                queryWrapper.like(TblThirdPartyAccount::getAccountName, accountName);
            }
            if (!StringUtils.isEmpty(thirdPartySystem)) {
                queryWrapper.eq(TblThirdPartyAccount::getThirdPartySystem, thirdPartySystem);
            }
            if (!StringUtils.isEmpty(accountType)) {
                queryWrapper.eq(TblThirdPartyAccount::getAccountType, accountType);
            }

            // 分页查询
            Page<TblThirdPartyAccount> pageParam = new Page<>(page, limit);
            IPage<TblThirdPartyAccount> pageResult = tblThirdPartyAccountService.page(pageParam, queryWrapper);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageResult.getRecords());
            result.put("totalRecord", pageResult.getTotal());
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取第三方账户列表成功", result);

        } catch (Exception e) {
            log.error("获取第三方账户列表失败", e);
            return new JsonBean(0, "获取第三方账户列表失败: " + e.getMessage(), null);
        }
    }

    // ==================== 4. 印鉴档案管理模块 ====================

    /**
     * 获取印鉴档案列表(分页) - 前端路径: /seal/archive/list
     */
    @RequestMapping(value = "/seal/archive/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴档案列表")
    public JsonBean getSealArchiveList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String sealCode,
            @RequestParam(required = false) String sealName,
            @RequestParam(required = false) String sealTypeId,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) String isActive) {

        try {
            // 暂时返回空列表,因为TcSealArchiveService可能未实现
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", new ArrayList<>());
            result.put("totalRecord", 0);
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取印鉴档案列表成功", result);

        } catch (Exception e) {
            log.error("获取印鉴档案列表失败", e);
            return new JsonBean(0, "获取印鉴档案列表失败: " + e.getMessage(), null);
        }
    }

    // ==================== 5. 印鉴组合配置模块 ====================

    /**
     * 获取印鉴组合列表(分页) - 前端路径: /seal/combination/list
     */
    @RequestMapping(value = "/seal/combination/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴组合列表")
    public JsonBean getSealCombinationList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String combinationCode,
            @RequestParam(required = false) String combinationName,
            @RequestParam(required = false) String combinationType,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String authorityLevel) {

        try {
            // 构建查询条件
            LambdaQueryWrapper<TcSealCombination> queryWrapper = new LambdaQueryWrapper<>();

            if (!StringUtils.isEmpty(combinationCode)) {
                queryWrapper.like(TcSealCombination::getCombinationCode, combinationCode);
            }
            if (!StringUtils.isEmpty(combinationName)) {
                queryWrapper.like(TcSealCombination::getCombinationName, combinationName);
            }
            // 已删除combinationType, businessType, authorityLevel字段，暂时不查询这些条件
            // if (!StringUtils.isEmpty(combinationType)) {
            //     queryWrapper.eq(TcSealCombination::getCombinationType, combinationType);
            // }
            // if (!StringUtils.isEmpty(businessType)) {
            //     queryWrapper.eq(TcSealCombination::getBusinessType, businessType);
            // }
            // if (!StringUtils.isEmpty(authorityLevel)) {
            //     queryWrapper.eq(TcSealCombination::getAuthorityLevel, authorityLevel);
            // }

            // 分页查询
            Page<TcSealCombination> pageParam = new Page<>(page, limit);
            IPage<TcSealCombination> pageResult = tcSealCombinationService.page(pageParam, queryWrapper);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageResult.getRecords());
            result.put("totalRecord", pageResult.getTotal());
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取印鉴组合列表成功", result);

        } catch (Exception e) {
            log.error("获取印鉴组合列表失败", e);
            return new JsonBean(0, "获取印鉴组合列表失败: " + e.getMessage(), null);
        }
    }

    // ==================== 6. 印鉴类型管理模块 ====================

    /**
     * 获取印鉴类型列表(分页) - 前端路径: /seal/type/list
     */
    @RequestMapping(value = "/seal/type/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴类型列表")
    public JsonBean getSealTypePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sealLevel,
            @RequestParam(required = false) String status) {

        try {
            // 构建查询条件
            LambdaQueryWrapper<TcSealType> queryWrapper = new LambdaQueryWrapper<>();

            if (!StringUtils.isEmpty(name)) {
                queryWrapper.like(TcSealType::getTypeName, name);
            }
            // 已删除permissionLevel和status字段，暂时不查询这些条件
            // if (!StringUtils.isEmpty(sealLevel)) {
            //     queryWrapper.eq(TcSealType::getIsActive, sealLevel);
            // }
            // if (!StringUtils.isEmpty(status)) {
            //     queryWrapper.eq(TcSealType::getIsActive, status);
            // }

            // 分页查询
            Page<TcSealType> pageParam = new Page<>(page, limit);
            IPage<TcSealType> pageResult = tcSealTypeService.page(pageParam, queryWrapper);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageResult.getRecords());
            result.put("totalRecord", pageResult.getTotal());
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取印鉴类型列表成功", result);

        } catch (Exception e) {
            log.error("获取印鉴类型列表失败", e);
            return new JsonBean(0, "获取印鉴类型列表失败: " + e.getMessage(), null);
        }
    }

    // ==================== 7. 印鉴使用记录模块 ====================

    /**
     * 获取印鉴使用记录列表(分页) - 前端路径: /seal/usage/list
     */
    @RequestMapping(value = "/seal/usage/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴使用记录列表")
    public JsonBean getSealUsageRecordList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String recordNumber,
            @RequestParam(required = false) String sealCode,
            @RequestParam(required = false) String sealName,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String usageStatus) {

        try {
            // 构建查询条件
            LambdaQueryWrapper<TblSealUsageRecord> queryWrapper = new LambdaQueryWrapper<>();

            if (!StringUtils.isEmpty(recordNumber)) {
                queryWrapper.like(TblSealUsageRecord::getRecordNumber, recordNumber);
            }
            if (!StringUtils.isEmpty(sealCode)) {
                queryWrapper.like(TblSealUsageRecord::getSealCode, sealCode);
            }
            if (!StringUtils.isEmpty(sealName)) {
                queryWrapper.like(TblSealUsageRecord::getSealName, sealName);
            }
            if (!StringUtils.isEmpty(operatorName)) {
                queryWrapper.like(TblSealUsageRecord::getOperatorName, operatorName);
            }
            if (!StringUtils.isEmpty(businessType)) {
                queryWrapper.eq(TblSealUsageRecord::getBusinessType, businessType);
            }
            if (!StringUtils.isEmpty(usageStatus)) {
                queryWrapper.eq(TblSealUsageRecord::getUsageStatus, usageStatus);
            }

            // 分页查询
            Page<TblSealUsageRecord> pageParam = new Page<>(page, limit);
            IPage<TblSealUsageRecord> pageResult = tblSealUsageRecordService.page(pageParam, queryWrapper);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageResult.getRecords());
            result.put("totalRecord", pageResult.getTotal());
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取印鉴使用记录列表成功", result);

        } catch (Exception e) {
            log.error("获取印鉴使用记录列表失败", e);
            return new JsonBean(0, "获取印鉴使用记录列表失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取印鉴使用统计数据 - 前端路径: /seal/usage/statistics
     */
    @GetMapping("/seal/usage/statistics")
    @ApiOperation("获取印鉴使用统计")
    public JsonBean getSealUsageStatistics() {

        try {
            Map<String, Object> statistics = new HashMap<>();

            // 统计总记录数
            long totalCount = tblSealUsageRecordService.count();
            statistics.put("totalCount", (int)totalCount);

            // TODO: 添加更多统计信息

            return new JsonBean(1, "获取印鉴使用统计成功", statistics);

        } catch (Exception e) {
            log.error("获取印鉴使用统计失败", e);
            return new JsonBean(0, "获取印鉴使用统计失败: " + e.getMessage(), null);
        }
    }

    // ==================== 8. 安全参数配置模块 ====================

    /**
     * 获取参数配置列表(分页) - 前端路径: /security/parameter/list
     */
    @RequestMapping(value = "/security/parameter/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取参数配置列表")
    public JsonBean getTreasuryParameterList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String paramName,
            @RequestParam(required = false) String paramType,
            @RequestParam(required = false) String isEnabled) {

        try {
            // 构建查询条件
            LambdaQueryWrapper<TblSecurityParameter> queryWrapper = new LambdaQueryWrapper<>();

            if (!StringUtils.isEmpty(paramName)) {
                queryWrapper.like(TblSecurityParameter::getParamName, paramName);
            }
            if (!StringUtils.isEmpty(paramType)) {
                queryWrapper.eq(TblSecurityParameter::getParamType, paramType);
            }
            if (!StringUtils.isEmpty(isEnabled)) {
                queryWrapper.eq(TblSecurityParameter::getIsEnabled, isEnabled);
            }

            // 分页查询
            Page<TblSecurityParameter> pageParam = new Page<>(pageNum, pageSize);
            IPage<TblSecurityParameter> pageResult = tblSecurityParameterService.page(pageParam, queryWrapper);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageResult.getRecords());
            result.put("totalRecord", pageResult.getTotal());
            result.put("pageNo", pageNum);
            result.put("pageSize", pageSize);

            return new JsonBean(1, "获取参数配置列表成功", result);

        } catch (Exception e) {
            log.error("获取参数配置列表失败", e);
            return new JsonBean(0, "获取参数配置列表失败: " + e.getMessage(), null);
        }
    }

    // ==================== 10. UKey厂商管理模块 ====================

    /**
     * 获取UKey厂商列表(分页) - 前端路径: /ukey/vendor/list
     */
    @RequestMapping(value = "/ukey/vendor/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取UKey厂商列表")
    public JsonBean getUkeyVendorList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String vendorName,
            @RequestParam(required = false) String vendorType,
            @RequestParam(required = false) String certificationStatus,
            @RequestParam(required = false) String cooperationStatus) {

        try {
            // 暂时返回空列表,因为TcUkeyVendorService可能未实现
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", new ArrayList<>());
            result.put("totalRecord", 0);
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return new JsonBean(1, "获取UKey厂商列表成功", result);

        } catch (Exception e) {
            log.error("获取UKey厂商列表失败", e);
            return new JsonBean(0, "获取UKey厂商列表失败: " + e.getMessage(), null);
        }
    }
}
