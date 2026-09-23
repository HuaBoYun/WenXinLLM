package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.ElectronicBillDTO;
import com.global.treasurer.dto.ElectronicBillQueryDTO;
import com.global.treasurer.entity.TblElectronicBill;
import com.global.treasurer.service.IElectronicBillService;
import com.global.treasurer.vo.ElectronicBillVO;
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
import java.util.Map;
import java.util.List;

/**
 * 电子票据管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/electronic")
@Api(tags = "电子票据管理")
public class ElectronicBillController {
    private static final Logger log = LoggerFactory.getLogger(ElectronicBillController.class);

    @Autowired
    private IElectronicBillService electronicBillService;

    /**
     * 分页查询电子票据
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询电子票据列表", notes = "分页查询电子票据列表")
    public String list(@FlexibleRequestBody ElectronicBillQueryDTO queryDTO) {
        try {
            if (queryDTO == null) {
                queryDTO = new ElectronicBillQueryDTO();
            }
            log.info("查询电子票据列表, 参数: billNumber={}, billType={}, billStatus={}",
                queryDTO.getBillNumber(), queryDTO.getBillType(), queryDTO.getBillStatus());
            PageInfo<ElectronicBillVO> pageInfo = electronicBillService.selectElectronicBillList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询电子票据列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询电子票据
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询电子票据", notes = "分页查询电子票据列表")
    public String page(@FlexibleRequestBody ElectronicBillQueryDTO queryDTO) {
        try {
            if (queryDTO == null) {
                queryDTO = new ElectronicBillQueryDTO();
            }
            log.info("分页查询电子票据, 参数: billNumber={}, billType={}, billStatus={}, pageNum={}, pageSize={}",
                queryDTO.getBillNumber(), queryDTO.getBillType(), queryDTO.getBillStatus(),
                queryDTO.getPageNum(), queryDTO.getPageSize());
            PageInfo<ElectronicBillVO> pageInfo = electronicBillService.selectElectronicBillList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询电子票据列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取电子票据详情
     */
    @PostMapping("/detail/{billId}")
    @ApiOperation(value = "获取电子票据详情", notes = "根据电子票据ID获取详细信息")
    public String detail(@PathVariable Long billId) {
        try {
            ElectronicBillVO vo = electronicBillService.selectElectronicBillById(billId);
            if (vo == null) {
                return JsonBean.error("电子票据不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取电子票据详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取电子票据详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建电子票据
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增电子票据", notes = "新增电子票据信息")
    public String save(@Valid @FlexibleRequestBody ElectronicBillDTO dto) {
        try {
            TblElectronicBill bill = electronicBillService.insertElectronicBill(dto);
            return JsonBean.success("电子票据创建成功", bill);
        } catch (BizException e) {
            log.warn("新增电子票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增电子票据失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新电子票据
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改电子票据", notes = "修改电子票据信息")
    public String update(@Valid @FlexibleRequestBody ElectronicBillDTO dto) {
        try {
            if (dto.getBillId() == null) {
                return JsonBean.error("电子票据ID不能为空");
            }
            TblElectronicBill bill = electronicBillService.updateElectronicBill(dto);
            return JsonBean.success("电子票据修改成功", bill);
        } catch (BizException e) {
            log.warn("修改电子票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改电子票据失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除电子票据
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除电子票据", notes = "批量删除电子票据(逻辑删除)")
    public String delete(@RequestParam(value = "billIds", required = false) Long[] billIds) {
        try {
            if (billIds == null || billIds.length == 0) {
                return JsonBean.error("请选择要删除的电子票据");
            }
            boolean result = electronicBillService.deleteElectronicBillByIds(billIds);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (BizException e) {
            log.warn("删除电子票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除电子票据失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 数字签名电子票据
     */
    @PostMapping("/sign")
    @ApiOperation(value = "数字签名电子票据", notes = "对电子票据进行数字签名")
    public String sign(@RequestParam Map<String, Object> signData) {
        try {
            boolean result = electronicBillService.signElectronicBill(signData);
            return result ? JsonBean.success("数字签名成功") : JsonBean.error("数字签名失败");
        } catch (BizException e) {
            log.warn("数字签名电子票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("数字签名电子票据失败", e);
            return JsonBean.error("签名失败: " + e.getMessage());
        }
    }

    /**
     * 验证电子票据
     */
    @PostMapping("/verify")
    @ApiOperation(value = "验证电子票据", notes = "验证电子票据签名的有效性")
    public String verify(@RequestParam Map<String, Object> verifyData) {
        try {
            Map<String, Object> result = electronicBillService.verifyElectronicBill(verifyData);
            return JsonBean.success(result);
        } catch (BizException e) {
            log.warn("验证电子票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("验证电子票据失败", e);
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 追踪电子票据流转
     */
    @PostMapping("/track/{billId}")
    @ApiOperation(value = "追踪电子票据流转", notes = "查询电子票据的流转历史记录")
    public String track(@PathVariable Long billId) {
        try {
            Map<String, Object> trackData = electronicBillService.trackElectronicBill(billId);
            return JsonBean.success(trackData);
        } catch (BizException e) {
            log.warn("追踪电子票据流转失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("追踪电子票据流转失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取电子票据流转记录
     */
    @PostMapping("/circulation-records/{billId}")
    @ApiOperation(value = "获取电子票据流转记录", notes = "查询电子票据的流转历史记录")
    public String getCirculationRecords(@PathVariable Long billId,
                                       @RequestParam(required = false) Map<String, Object> params) {
        try {
            if (billId == null) {
                return JsonBean.error("票据ID不能为空");
            }
            java.util.List<com.global.treasurer.vo.CirculationRecordVO> records =
                electronicBillService.selectCirculationRecords(billId, params);
            return JsonBean.success(records);
        } catch (BizException e) {
            log.warn("获取电子票据流转记录失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取电子票据流转记录失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 导出电子票据数据
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出电子票据", notes = "导出电子票据数据到Excel")
    public void export(ElectronicBillQueryDTO queryDTO, javax.servlet.http.HttpServletResponse response) {
        try {
            electronicBillService.exportElectronicBill(queryDTO, response);
        } catch (Exception e) {
            log.error("导出电子票据数据失败", e);
        }
    }

    /**
     * 获取电子票据统计数据
     */
    @PostMapping("/statistics")
    @ApiOperation(value = "获取电子票据统计", notes = "获取电子票据统计数据")
    public String statistics(ElectronicBillQueryDTO queryDTO) {
        try {
            Map<String, Object> statistics = electronicBillService.getElectronicBillStatistics(queryDTO);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取电子票据统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取票据趋势分析数据
     */
    @PostMapping("/trend-analysis")
    @ApiOperation(value = "获取票据趋势分析", notes = "获取票据趋势分析数据")
    public String trendAnalysis(Map<String, Object> params) {
        try {
            Map<String, Object> trendData = electronicBillService.getBillTrendAnalysis(params);
            return JsonBean.success(trendData);
        } catch (Exception e) {
            log.error("获取票据趋势分析数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 背书转让
     */
    @PostMapping("/endorse")
    @ApiOperation(value = "背书转让", notes = "电子票据背书转让")
    public String endorse(Map<String, Object> endorseData) {
        try {
            boolean result = electronicBillService.endorseElectronicBill(endorseData);
            return result ? JsonBean.success("背书转让成功") : JsonBean.error("背书转让失败");
        } catch (BizException e) {
            log.warn("背书转让失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("背书转让失败", e);
            return JsonBean.error("背书转让失败: " + e.getMessage());
        }
    }

    /**
     * 票据贴现
     */
    @PostMapping("/discount")
    @ApiOperation(value = "票据贴现", notes = "电子票据贴现")
    public String discount(Map<String, Object> discountData) {
        try {
            boolean result = electronicBillService.discountElectronicBill(discountData);
            return result ? JsonBean.success("票据贴现成功") : JsonBean.error("票据贴现失败");
        } catch (BizException e) {
            log.warn("票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("票据贴现失败", e);
            return JsonBean.error("票据贴现失败: " + e.getMessage());
        }
    }
}
