package com.global.treasurer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGuaranteeContract;
import com.global.treasurer.mapper.GuaranteeContractMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 担保合同管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/rzgl/guarantee/contract", "/financial/rzgl/guarantee/contract", "/centralaudit/financial/rzgl/guarantee/contract"})
@Api(tags = "担保合同管理")
public class GuaranteeContractController {
    private static final Logger log = LoggerFactory.getLogger(GuaranteeContractController.class);

    @Resource
    private GuaranteeContractMapper guaranteeContractMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询担保合同列表（GET请求）
     */
    @GetMapping("/page")
    @ResponseBody
    @ApiOperation("分页查询担保合同列表(GET)")
    public String getGuaranteeContractPageByGet(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String guaranteeType,
            @RequestParam(required = false) String contractStatus,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) Long orgId,
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        return getGuaranteeContractPage(pageNum, pageSize, contractNo, contractType,
                guaranteeType, contractStatus, riskLevel, orgId, token, response);
    }

    /**
     * 分页查询担保合同列表（POST请求）
     */
    @PostMapping("/page")
    @ResponseBody
    @ApiOperation("分页查询担保合同列表(POST)")
    public String getGuaranteeContractPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String guaranteeType,
            @RequestParam(required = false) String contractStatus,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) Long orgId,
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 使用PageHelper进行分页
            PageHelper.startPage(pageNum, pageSize);

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("contractNo", contractNo);
            params.put("contractType", contractType);
            params.put("guaranteeType", guaranteeType);
            params.put("contractStatus", contractStatus);
            params.put("riskLevel", riskLevel);

            // 如果前端没有传orgId，使用当前用户的orgId
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }
            params.put("orgId", orgId);

            // 调用Mapper的条件查询方法
            List<TblGuaranteeContract> list = guaranteeContractMapper.selectContractList(params);

            // 返回分页结果
            PageInfo<TblGuaranteeContract> pageInfo = new PageInfo<>(list);

            // 构建前端期望的响应格式
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("rows", pageInfo.getList());
            resultMap.put("total", pageInfo.getTotal());

            return JsonBean.success(resultMap);
        } catch (Exception e) {
            log.error("查询担保合同列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询担保合同详情
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("根据ID查询担保合同详情")
    public String getGuaranteeContractById(@PathVariable Long id) {
        try {
            TblGuaranteeContract contract = guaranteeContractMapper.selectContractById(id);
            if (contract != null) {
                return JsonBean.success(contract);
            } else {
                return new JsonBean(0, "未找到对应的担保合同记录", null).toJson();
            }
        } catch (Exception e) {
            log.error("查询担保合同详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增担保合同
     */
    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增担保合同")
    public String addGuaranteeContract(@FlexibleRequestBody TblGuaranteeContract contract) {
        try {
            log.info("新增担保合同, contract: {}", contract);
            // 设置默认值
            if (contract.getContractStatus() == null) {
                contract.setContractStatus("DRAFT");
            }
            contract.setDeleteFlag(0);
            contract.setCreatedTime(new java.util.Date());
            contract.setUpdatedTime(new java.util.Date());
            // 生成合同编号
            if (contract.getContractNo() == null || contract.getContractNo().isEmpty()) {
                contract.setContractNo("GC" + System.currentTimeMillis());
            }
            guaranteeContractMapper.insert(contract);
            return JsonBean.success("新增成功");
        } catch (Exception e) {
            log.error("新增担保合同失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改担保合同
     */
    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改担保合同")
    public String updateGuaranteeContract(@FlexibleRequestBody TblGuaranteeContract contract) {
        try {
            log.info("修改担保合同, contract: {}", contract);
            contract.setUpdatedTime(new java.util.Date());
            guaranteeContractMapper.updateById(contract);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改担保合同失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除担保合同
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("删除担保合同")
    public String deleteGuaranteeContract(@PathVariable Long id) {
        try {
            log.info("删除担保合同, id: {}", id);
            // 逻辑删除
            TblGuaranteeContract contract = guaranteeContractMapper.selectContractById(id);
            if (contract != null) {
                contract.setDeleteFlag(1);
                contract.setUpdatedTime(new java.util.Date());
                guaranteeContractMapper.updateById(contract);
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "未找到对应的担保合同记录", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除担保合同失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除担保合同
     */
    @DeleteMapping("/batch")
    @ResponseBody
    @ApiOperation("批量删除担保合同")
    public String batchDeleteGuaranteeContract(@RequestParam(value = "ids", required = false) List<Long> ids) {
        try {
            log.info("批量删除担保合同, ids: {}", ids);
            guaranteeContractMapper.batchDeleteByIds(ids);
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除担保合同失败, ids: {}", ids, e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 提交担保合同审批
     */
    @PostMapping("/{id}/submit")
    @ResponseBody
    @ApiOperation("提交担保合同审批")
    public String submitGuaranteeContract(@PathVariable Long id) {
        try {
            log.info("提交担保合同审批, id: {}", id);
            guaranteeContractMapper.updateContractStatus(id, "PENDING");
            return JsonBean.success("提交成功");
        } catch (Exception e) {
            log.error("提交担保合同审批失败, id: {}", id, e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批通过担保合同
     */
    @PostMapping("/{id}/approve")
    @ResponseBody
    @ApiOperation("审批通过担保合同")
    public String approveGuaranteeContract(@PathVariable Long id, @RequestParam(required = false) String approvalComments) {
        try {
            log.info("审批通过担保合同, id: {}, comments: {}", id, approvalComments);
            guaranteeContractMapper.updateContractStatus(id, "APPROVED");
            return JsonBean.success("审批通过");
        } catch (Exception e) {
            log.error("审批通过担保合同失败, id: {}", id, e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 签署担保合同
     */
    @PostMapping("/{id}/sign")
    @ResponseBody
    @ApiOperation("签署担保合同")
    public String signGuaranteeContract(@PathVariable Long id) {
        try {
            log.info("签署担保合同, id: {}", id);
            guaranteeContractMapper.updateContractStatus(id, "SIGNED");
            return JsonBean.success("签署成功");
        } catch (Exception e) {
            log.error("签署担保合同失败, id: {}", id, e);
            return new JsonBean(0, "签署失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 生效担保合同
     */
    @PostMapping("/{id}/activate")
    @ResponseBody
    @ApiOperation("生效担保合同")
    public String activateGuaranteeContract(@PathVariable Long id) {
        try {
            log.info("生效担保合同, id: {}", id);
            guaranteeContractMapper.updateContractStatus(id, "EFFECTIVE");
            return JsonBean.success("生效成功");
        } catch (Exception e) {
            log.error("生效担保合同失败, id: {}", id, e);
            return new JsonBean(0, "生效失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 终止担保合同
     */
    @PostMapping("/{id}/terminate")
    @ResponseBody
    @ApiOperation("终止担保合同")
    public String terminateGuaranteeContract(@PathVariable Long id, @RequestParam(required = false) String terminationReason) {
        try {
            log.info("终止担保合同, id: {}, reason: {}", id, terminationReason);
            guaranteeContractMapper.updateContractStatus(id, "TERMINATED");
            return JsonBean.success("终止成功");
        } catch (Exception e) {
            log.error("终止担保合同失败, id: {}", id, e);
            return new JsonBean(0, "终止失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出担保合同
     */
    @PostMapping("/export")
    @ApiOperation("导出担保合同")
    public void exportGuaranteeContract(
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String guaranteeType,
            @RequestParam(required = false) String contractStatus,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) Long orgId,
            HttpServletResponse response) {
        try {
            log.info("导出担保合同, contractNo: {}, contractType: {}, guaranteeType: {}, contractStatus: {}",
                    contractNo, contractType, guaranteeType, contractStatus);

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("contractNo", contractNo);
            params.put("contractType", contractType);
            params.put("guaranteeType", guaranteeType);
            params.put("contractStatus", contractStatus);
            params.put("riskLevel", riskLevel);
            params.put("orgId", orgId);

            // 查询数据
            List<TblGuaranteeContract> dataList = guaranteeContractMapper.selectContractList(params);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("担保合同列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用EasyExcel导出
            com.alibaba.excel.EasyExcel.write(response.getOutputStream(), TblGuaranteeContract.class)
                    .sheet("担保合同")
                    .doWrite(dataList);

        } catch (Exception e) {
            log.error("导出担保合同失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage()).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
}
