package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillEndorsementDTO;
import com.global.treasurer.dto.BillEndorsementQueryDTO;
import com.global.treasurer.entity.TblBillEndorsement;
import com.global.treasurer.service.IBillEndorsementService;
import com.global.treasurer.vo.BillEndorsementVO;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 票据背书管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/endorsement")
@Api(tags = "票据背书管理")
public class BillEndorsementController {
    private static final Logger log = LoggerFactory.getLogger(BillEndorsementController.class);

    @Autowired
    private IBillEndorsementService billEndorsementService;

    @PostMapping("/list")
    @ApiOperation(value = "查询票据背书列表", notes = "分页查询票据背书列表")
    public String list(BillEndorsementQueryDTO queryDTO) {
        try {
            PageInfo<BillEndorsementVO> pageInfo = billEndorsementService.selectBillEndorsementList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据背书列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据背书", notes = "分页查询票据背书列表")
    public String page(BillEndorsementQueryDTO queryDTO) {
        try {
            PageInfo<BillEndorsementVO> pageInfo = billEndorsementService.selectBillEndorsementList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据背书列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{endorsementId}")
    @ApiOperation(value = "获取票据背书详情", notes = "根据背书ID获取详细信息")
    public String detail(@PathVariable Long endorsementId) {
        try {
            BillEndorsementVO vo = billEndorsementService.selectBillEndorsementById(endorsementId);
            if (vo == null) {
                return JsonBean.error("背书记录不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取票据背书详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取票据背书详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增票据背书", notes = "新增票据背书申请")
    public String save(@Valid @FlexibleRequestBody BillEndorsementDTO dto) {
        try {
            TblBillEndorsement endorsement = billEndorsementService.insertBillEndorsement(dto);
            return JsonBean.success("背书申请提交成功", endorsement);
        } catch (BizException e) {
            log.warn("新增票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增票据背书失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改票据背书", notes = "修改票据背书信息")
    public String update(@Valid @FlexibleRequestBody BillEndorsementDTO dto) {
        try {
            if (dto.getEndorsementId() == null) {
                return JsonBean.error("背书ID不能为空");
            }
            TblBillEndorsement endorsement = billEndorsementService.updateBillEndorsement(dto);
            return JsonBean.success("背书修改成功", endorsement);
        } catch (BizException e) {
            log.warn("修改票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改票据背书失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除票据背书", notes = "批量删除票据背书(逻辑删除)")
    public String delete(@RequestParam(value = "endorsementIds", required = false) Long[] endorsementIds) {
        try {
            boolean result = billEndorsementService.deleteBillEndorsementByIds(endorsementIds);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (BizException e) {
            log.warn("删除票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除票据背书失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{endorsementId}")
    @ApiOperation(value = "审批票据背书", notes = "审批票据背书申请")
    public String approve(@PathVariable Long endorsementId, @RequestParam(required = false) Map<String, Object> approvalData) {
        try {
            boolean result = billEndorsementService.approveBillEndorsement(endorsementId, approvalData);
            return result ? JsonBean.success("审批成功") : JsonBean.error("审批失败");
        } catch (BizException e) {
            log.warn("审批票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("审批票据背书失败", e);
            return JsonBean.error("审批失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute/{endorsementId}")
    @ApiOperation(value = "执行票据背书", notes = "执行已审批的票据背书")
    public String execute(@PathVariable Long endorsementId) {
        try {
            boolean result = billEndorsementService.executeBillEndorsement(endorsementId);
            return result ? JsonBean.success("执行成功") : JsonBean.error("执行失败");
        } catch (BizException e) {
            log.warn("执行票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("执行票据背书失败", e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{endorsementId}")
    @ApiOperation(value = "撤销票据背书", notes = "撤销票据背书申请")
    public String cancel(@PathVariable Long endorsementId, String reason) {
        try {
            boolean result = billEndorsementService.cancelBillEndorsement(endorsementId, reason);
            return result ? JsonBean.success("撤销成功") : JsonBean.error("撤销失败");
        } catch (BizException e) {
            log.warn("撤销票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("撤销票据背书失败", e);
            return JsonBean.error("撤销失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-approve")
    @ApiOperation(value = "批量审批票据背书", notes = "批量审批多个票据背书申请")
    public String batchApprove(@RequestParam Map<String, Object> batchData) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> endorsementIds = (List<Long>) batchData.get("endorsementIds");
            @SuppressWarnings("unchecked")
            Map<String, Object> approvalData = (Map<String, Object>) batchData.get("approvalData");
            boolean result = billEndorsementService.batchApproveBillEndorsement(endorsementIds, approvalData);
            return result ? JsonBean.success("批量审批成功") : JsonBean.error("批量审批失败");
        } catch (BizException e) {
            log.warn("批量审批票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量审批票据背书失败", e);
            return JsonBean.error("批量审批失败: " + e.getMessage());
        }
    }

    @PostMapping("/available-bills")
    @ApiOperation(value = "获取可用票据列表", notes = "获取可用于背书的票据列表")
    public String getAvailableBills(@RequestParam(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> bills = billEndorsementService.getAvailableBillsForEndorsement(params);
            return JsonBean.success(bills);
        } catch (Exception e) {
            log.error("获取可用票据列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

