package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import com.global.treasurer.dto.CreditContractDTO;
import com.global.treasurer.dto.CreditContractQueryDTO;
import com.global.treasurer.entity.TblCreditContract;
import com.global.treasurer.service.CreditContractService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 授信合同管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@RestController
@RequestMapping("/financial/rzgl/credit/contract")
@Api(tags = "授信合同管理")
public class CreditContractController {

    private static final Logger log = LoggerFactory.getLogger(CreditContractController.class);

    @Autowired
    private CreditContractService creditContractService;

    @PostMapping("/page")
    @ApiOperation("授信合同分页查询")
    public String page(@FlexibleRequestBody CreditContractQueryDTO queryDTO) {
        try {
            log.info("授信合同分页查询, queryDTO: {}", queryDTO);
            PageInfo<TblCreditContract> pageInfo = creditContractService.getContractList(queryDTO);
            
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("授信合同分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("授信合同详情查询")
    public String detail(@ApiParam("合同ID") @PathVariable Long id) {
        try {
            log.info("查询授信合同详情, id: {}", id);
            TblCreditContract contract = creditContractService.getContractById(id);
            return new JsonBean(1, "查询成功", contract).toString();
        } catch (Exception e) {
            log.error("查询授信合同详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping(value = {"", "/add"})
    @ApiOperation("新增授信合同")
    public String add(@FlexibleRequestBody CreditContractDTO dto) {
        try {
            log.info("新增授信合同, dto: {}", dto);
            TblCreditContract contract = creditContractService.saveContract(dto);
            return new JsonBean(1, "新增成功", contract).toString();
        } catch (Exception e) {
            log.error("新增授信合同失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping(value = {"", "/update"})
    @ApiOperation("更新授信合同")
    public String update(@FlexibleRequestBody CreditContractDTO dto) {
        try {
            log.info("更新授信合同, dto: {}", dto);
            TblCreditContract contract = creditContractService.saveContract(dto);
            return new JsonBean(1, "更新成功", contract).toString();
        } catch (Exception e) {
            log.error("更新授信合同失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除授信合同")
    public String delete(@ApiParam("合同ID") @PathVariable Long id) {
        try {
            log.info("删除授信合同, id: {}", id);
            creditContractService.deleteContract(id);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除授信合同失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/sign/{id}")
    @ApiOperation("签署授信合同")
    public String sign(
            @ApiParam("合同ID") @PathVariable Long id,
            @ApiParam("签署日期") @RequestParam(required = false) Date signDate,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        try {
            log.info("签署授信合同, id: {}, signDate: {}, userId: {}", id, signDate, userId);
            creditContractService.signContract(id);
            return new JsonBean(1, "签署成功").toString();
        } catch (Exception e) {
            log.error("签署授信合同失败, id: {}", id, e);
            return new JsonBean(0, "签署失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/terminate/{id}")
    @ApiOperation("终止授信合同")
    public String terminate(
            @ApiParam("合同ID") @PathVariable Long id,
            @ApiParam("终止日期") @RequestParam(required = false) Date terminateDate,
            @ApiParam("终止原因") @RequestParam(required = false) String reason,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        try {
            log.info("终止授信合同, id: {}, terminateDate: {}, reason: {}, userId: {}", id, terminateDate, reason, userId);
            creditContractService.terminateContract(id, reason);
            return new JsonBean(1, "终止成功").toString();
        } catch (Exception e) {
            log.error("终止授信合同失败, id: {}", id, e);
            return new JsonBean(0, "终止失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出授信合同")
    public void exportContract(@FlexibleRequestBody CreditContractQueryDTO queryDTO,
                              javax.servlet.http.HttpServletResponse response) {
        try {
            log.info("导出授信合同, queryDTO: {}", queryDTO);
            creditContractService.exportContract(queryDTO, response);
        } catch (Exception e) {
            log.error("导出授信合同失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage()).toString());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
}

