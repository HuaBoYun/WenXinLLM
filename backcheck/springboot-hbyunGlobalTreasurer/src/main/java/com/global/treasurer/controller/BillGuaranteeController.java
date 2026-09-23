package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeDTO;
import com.global.treasurer.dto.GuaranteeQueryDTO;
import com.global.treasurer.entity.TblGuarantee;
import com.global.treasurer.service.IGuaranteeService;
import com.global.treasurer.vo.GuaranteeVO;
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
 * 保函管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/bill/guarantees")
@Api(tags = "保函管理")
public class BillGuaranteeController {
    private static final Logger log = LoggerFactory.getLogger(BillGuaranteeController.class);

    @Autowired
    private IGuaranteeService guaranteeService;

    /**
     * 获取保函列表(分页)
     */
    @PostMapping("/list")
    @ApiOperation("获取保函列表(分页)")
    public String getGuaranteeList(@FlexibleRequestBody GuaranteeQueryDTO queryDTO) {
        try {
            PageInfo<GuaranteeVO> pageInfo = guaranteeService.selectGuaranteeList(queryDTO);
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取保函列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取保函详情
     */
    @PostMapping("/detail/{guaranteeId}")
    @ApiOperation("获取保函详情")
    public String getGuaranteeDetail(@PathVariable String guaranteeId) {
        try {
            GuaranteeVO vo = guaranteeService.selectGuaranteeById(guaranteeId);
            if (vo == null) {
                return JsonBean.error("保函不存在");
            }
            return new JsonBean(1, "查询成功", vo).toString();
        } catch (Exception e) {
            log.error("获取保函详情失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增保函申请
     */
    @PostMapping("/save")
    @ApiOperation("新增保函申请")
    public String saveGuarantee(@Validated @FlexibleRequestBody GuaranteeDTO dto) {
        try {
            TblGuarantee guarantee = guaranteeService.insertGuarantee(dto);
            return new JsonBean(1, "新增成功", guarantee).toString();
        } catch (Exception e) {
            log.error("新增保函失败", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改保函申请
     */
    @PostMapping("/update")
    @ApiOperation("修改保函申请")
    public String updateGuarantee(@Validated @FlexibleRequestBody GuaranteeDTO dto) {
        try {
            TblGuarantee guarantee = guaranteeService.updateGuarantee(dto);
            return new JsonBean(1, "修改成功", guarantee).toString();
        } catch (Exception e) {
            log.error("修改保函失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除保函
     */
    @PostMapping("/delete")
    @ApiOperation("删除保函")
    public String deleteGuarantee(@RequestParam(value = "guaranteeIds", required = false) String[] guaranteeIds) {
        try {
            boolean result = guaranteeService.deleteGuaranteeByIds(guaranteeIds);
            if (result) {
                return new JsonBean(1, "删除成功", null).toString();
            }
            return JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除保函失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 提交保函申请
     */
    @PostMapping("/submit/{guaranteeId}")
    @ApiOperation("提交保函申请")
    public String submitGuaranteeApplication(@PathVariable String guaranteeId) {
        try {
            boolean result = guaranteeService.submitGuaranteeApplication(guaranteeId);
            if (result) {
                return new JsonBean(1, "提交成功", null).toString();
            }
            return JsonBean.error("提交失败");
        } catch (Exception e) {
            log.error("提交保函申请失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("提交失败: " + e.getMessage());
        }
    }

    /**
     * 保函申请审批
     */
    @PostMapping("/approve/{guaranteeId}")
    @ApiOperation("保函申请审批")
    public String approveGuaranteeApplication(@PathVariable String guaranteeId,
                                              @RequestParam Map<String, Object> approvalData) {
        try {
            boolean result = guaranteeService.approveGuaranteeApplication(guaranteeId, approvalData);
            if (result) {
                return new JsonBean(1, "审批成功", null).toString();
            }
            return JsonBean.error("审批失败");
        } catch (Exception e) {
            log.error("保函申请审批失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("审批失败: " + e.getMessage());
        }
    }

    /**
     * 开立保函
     */
    @PostMapping("/issue/{guaranteeId}")
    @ApiOperation("开立保函")
    public String issueGuarantee(@PathVariable String guaranteeId) {
        try {
            boolean result = guaranteeService.issueGuarantee(guaranteeId);
            if (result) {
                return new JsonBean(1, "开立成功", null).toString();
            }
            return JsonBean.error("开立失败");
        } catch (Exception e) {
            log.error("开立保函失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("开立失败: " + e.getMessage());
        }
    }

    /**
     * 索赔保函
     */
    @PostMapping("/claim/{guaranteeId}")
    @ApiOperation("索赔保函")
    public String claimGuarantee(@PathVariable String guaranteeId,
                                 @RequestParam Map<String, Object> claimData) {
        try {
            boolean result = guaranteeService.claimGuarantee(guaranteeId, claimData);
            if (result) {
                return new JsonBean(1, "索赔成功", null).toString();
            }
            return JsonBean.error("索赔失败");
        } catch (Exception e) {
            log.error("索赔保函失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("索赔失败: " + e.getMessage());
        }
    }

    /**
     * 解除保函
     */
    @PostMapping("/release/{guaranteeId}")
    @ApiOperation("解除保函")
    public String releaseGuarantee(@PathVariable String guaranteeId,
                                   @RequestParam Map<String, Object> releaseData) {
        try {
            String releaseReason = (String) releaseData.get("releaseReason");
            boolean result = guaranteeService.releaseGuarantee(guaranteeId, releaseReason);
            if (result) {
                return new JsonBean(1, "解除成功", null).toString();
            }
            return JsonBean.error("解除失败");
        } catch (Exception e) {
            log.error("解除保函失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("解除失败: " + e.getMessage());
        }
    }

    /**
     * 取消保函
     */
    @PostMapping("/cancel/{guaranteeId}")
    @ApiOperation("取消保函")
    public String cancelGuarantee(@PathVariable String guaranteeId,
                                  @RequestParam Map<String, Object> cancelData) {
        try {
            String cancelReason = (String) cancelData.get("cancelReason");
            boolean result = guaranteeService.cancelGuarantee(guaranteeId, cancelReason);
            if (result) {
                return new JsonBean(1, "取消成功", null).toString();
            }
            return JsonBean.error("取消失败");
        } catch (Exception e) {
            log.error("取消保函失败, guaranteeId={}", guaranteeId, e);
            return JsonBean.error("取消失败: " + e.getMessage());
        }
    }
}
