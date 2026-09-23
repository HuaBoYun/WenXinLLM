package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceDataService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 财务数据控制器
 * 处理会计科目表、凭证库、辅助账等功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/finance")
@CrossOrigin
public class FinanceDataController {

    @Autowired
    private FinanceDataService financeDataService;

    // ==================== 会计科目表 ====================

    /**
     * 会计科目表
     */
    @GetMapping("/account/getFinanceDataList")
    public MyJsonBean getKmList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getKmList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取会计科目表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 凭证库
     */
    @GetMapping("/voucher/getFinanceDataList")
    public MyJsonBean getPzkList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getPzkList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取凭证库失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助账管理 ====================

    /**
     * 获取辅助账左侧树信息表
     */
    @GetMapping("/auxiliary/getAccAssTreeList")
    public MyJsonBean getAuxiliaryInfoTree(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getAuxiliaryInfoTree(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取辅助账左侧树信息表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取辅助账余额表
     */
    @GetMapping("/auxiliary/getFinanceAccAssBalanceList")
    public MyJsonBean getAuxiliaryBalanceTable(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getAuxiliaryBalanceTable(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取辅助账余额表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取辅助账信息表
     */
    @GetMapping("/auxiliary/getFinanceAccAssInfoList")
    public MyJsonBean getAuxiliaryInfoTable(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getAuxiliaryInfoTable(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取辅助账信息表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取辅助账总表
     */
    @GetMapping("/auxiliary/getFinanceAccAssGeneralLedgerList")
    public MyJsonBean getAuxiliaryTotalTable(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getAuxiliaryTotalTable(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取辅助账总表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 账簿管理 ====================

    /**
     * 获取明细账
     */
    @GetMapping("/detailBook/getFinanceDataList")
    public MyJsonBean getAuxiliaryDetailTable(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getAuxiliaryDetailTable(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取明细账失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取日记账
     */
    @GetMapping("/diaryBook/getFinanceDataList")
    public MyJsonBean getDiaryBookList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getDiaryBookList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取日记账失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取总分类账
     */
    @GetMapping("/sumTotal/getFinanceDataList")
    public MyJsonBean getTotalAccountList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getTotalAccountList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取总分类账失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 余额表
     */
    @GetMapping("/balance/getFinanceDataList")
    public MyJsonBean getYebList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getYebList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取余额表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 凭证明细信息
     */
    @GetMapping("/detail/getFinanceDataList")
    public MyJsonBean getFinanceDataList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeDataService.getFinanceDataList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取凭证明细信息失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}