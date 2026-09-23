package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankAcceptanceDTO;
import com.global.treasurer.dto.BankAcceptanceQueryDTO;
import com.global.treasurer.entity.TblBankAcceptance;
import com.global.treasurer.service.IBankAcceptanceService;
import com.global.treasurer.vo.BankAcceptanceVO;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 银行承兑汇票管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/bill/bank-acceptance")
@Api(tags = "银行承兑汇票管理")
public class BillBankAcceptanceController {
    private static final Logger log = LoggerFactory.getLogger(BillBankAcceptanceController.class);

    @Autowired
    private IBankAcceptanceService bankAcceptanceService;

    /**
     * 获取银行承兑汇票列表(分页)
     */
    @PostMapping("/list")
    @ApiOperation("获取银行承兑汇票列表(分页)")
    public String getBankAcceptanceList(@FlexibleRequestBody BankAcceptanceQueryDTO queryDTO) {
        try {
            PageInfo<BankAcceptanceVO> pageInfo = bankAcceptanceService.selectBankAcceptanceList(queryDTO);
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取银行承兑汇票列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取银行承兑汇票详情
     */
    @PostMapping("/detail/{acceptanceId}")
    @ApiOperation("获取银行承兑汇票详情")
    public String getBankAcceptanceDetail(@PathVariable Long acceptanceId) {
        try {
            BankAcceptanceVO vo = bankAcceptanceService.selectBankAcceptanceById(acceptanceId);
            if (vo == null) {
                return JsonBean.error("承兑汇票不存在");
            }
            return new JsonBean(1, "查询成功", vo).toString();
        } catch (Exception e) {
            log.error("获取银行承兑汇票详情失败, acceptanceId={}", acceptanceId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增银行承兑汇票申请
     */
    @PostMapping("/save")
    @ApiOperation("新增银行承兑汇票申请")
    public String saveBankAcceptance(@Validated @FlexibleRequestBody BankAcceptanceDTO dto) {
        try {
            TblBankAcceptance acceptance = bankAcceptanceService.insertBankAcceptance(dto);
            return new JsonBean(1, "新增成功", acceptance).toString();
        } catch (Exception e) {
            log.error("新增银行承兑汇票失败", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改银行承兑汇票申请
     */
    @PostMapping("/update")
    @ApiOperation("修改银行承兑汇票申请")
    public String updateBankAcceptance(@Validated @FlexibleRequestBody BankAcceptanceDTO dto) {
        try {
            TblBankAcceptance acceptance = bankAcceptanceService.updateBankAcceptance(dto);
            return new JsonBean(1, "修改成功", acceptance).toString();
        } catch (Exception e) {
            log.error("修改银行承兑汇票失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除银行承兑汇票
     */
    @PostMapping("/delete")
    @ApiOperation("删除银行承兑汇票")
    public String deleteBankAcceptance(@RequestBody Long[] acceptanceIds) {
        try {
            boolean result = bankAcceptanceService.deleteBankAcceptanceByIds(acceptanceIds);
            if (result) {
                return new JsonBean(1, "删除成功", null).toString();
            }
            return JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除银行承兑汇票失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 提交承兑申请
     */
    @PostMapping("/submit/{acceptanceId}")
    @ApiOperation("提交承兑申请")
    public String submitAcceptanceApplication(@PathVariable Long acceptanceId) {
        try {
            boolean result = bankAcceptanceService.submitAcceptanceApplication(acceptanceId);
            if (result) {
                return new JsonBean(1, "提交成功", null).toString();
            }
            return JsonBean.error("提交失败");
        } catch (Exception e) {
            log.error("提交承兑申请失败, acceptanceId={}", acceptanceId, e);
            return JsonBean.error("提交失败: " + e.getMessage());
        }
    }

    /**
     * 承兑申请审批
     */
    @PostMapping("/approve/{acceptanceId}")
    @ApiOperation("承兑申请审批")
    public String approveAcceptanceApplication(@PathVariable Long acceptanceId,
                                               @RequestParam Map<String, Object> approvalData) {
        try {
            boolean result = bankAcceptanceService.approveAcceptanceApplication(acceptanceId, approvalData);
            if (result) {
                return new JsonBean(1, "审批成功", null).toString();
            }
            return JsonBean.error("审批失败");
        } catch (Exception e) {
            log.error("承兑申请审批失败, acceptanceId={}", acceptanceId, e);
            return JsonBean.error("审批失败: " + e.getMessage());
        }
    }

    /**
     * 开立承兑汇票
     */
    @PostMapping("/issue/{acceptanceId}")
    @ApiOperation("开立承兑汇票")
    public String issueAcceptance(@PathVariable Long acceptanceId) {
        try {
            boolean result = bankAcceptanceService.issueAcceptance(acceptanceId);
            if (result) {
                return new JsonBean(1, "开立成功", null).toString();
            }
            return JsonBean.error("开立失败");
        } catch (Exception e) {
            log.error("开立承兑汇票失败, acceptanceId={}", acceptanceId, e);
            return JsonBean.error("开立失败: " + e.getMessage());
        }
    }

    /**
     * 保证金管理
     */
    @PostMapping("/margin/{acceptanceId}")
    @ApiOperation("保证金管理")
    public String manageMargin(@PathVariable Long acceptanceId,
                               @RequestParam Map<String, Object> marginData) {
        try {
            boolean result = bankAcceptanceService.manageMargin(acceptanceId, marginData);
            if (result) {
                return new JsonBean(1, "保证金管理成功", null).toString();
            }
            return JsonBean.error("保证金管理失败");
        } catch (Exception e) {
            log.error("保证金管理失败, acceptanceId={}", acceptanceId, e);
            return JsonBean.error("保证金管理失败: " + e.getMessage());
        }
    }

    /**
     * 获取保证金信息
     */
    @PostMapping("/margin-info/{acceptanceId}")
    @ApiOperation("获取保证金信息")
    public String getMarginInfo(@PathVariable Long acceptanceId) {
        try {
            Map<String, Object> marginInfo = bankAcceptanceService.getMarginInfo(acceptanceId);
            return new JsonBean(1, "查询成功", marginInfo).toString();
        } catch (Exception e) {
            log.error("获取保证金信息失败, acceptanceId={}", acceptanceId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
