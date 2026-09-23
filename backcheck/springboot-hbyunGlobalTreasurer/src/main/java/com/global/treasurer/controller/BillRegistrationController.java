package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillRegistrationDTO;
import com.global.treasurer.dto.BillRegistrationQueryDTO;
import com.global.treasurer.entity.TblBillRegistration;
import com.global.treasurer.service.IBillRegistrationService;
import com.global.treasurer.vo.BillRegistrationVO;
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
 * 票据登记管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/registration")
@Api(tags = "票据登记管理")
public class BillRegistrationController {
    private static final Logger log = LoggerFactory.getLogger(BillRegistrationController.class);

    @Autowired
    private IBillRegistrationService billRegistrationService;

    /**
     * 查询票据登记列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询票据登记列表", notes = "分页查询票据登记列表")
    public String list(BillRegistrationQueryDTO queryDTO) {
        try {
            PageInfo<BillRegistrationVO> pageInfo = billRegistrationService.selectBillRegistrationList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据登记列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询票据登记
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据登记", notes = "分页查询票据登记列表")
    public String page(BillRegistrationQueryDTO queryDTO) {
        try {
            PageInfo<BillRegistrationVO> pageInfo = billRegistrationService.selectBillRegistrationList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据登记列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取票据登记详情
     */
    @PostMapping("/detail/{billId}")
    @ApiOperation(value = "获取票据登记详情", notes = "根据票据ID获取详细信息")
    public String detail(@PathVariable Long billId) {
        try {
            BillRegistrationVO vo = billRegistrationService.selectBillRegistrationById(billId);
            if (vo == null) {
                return JsonBean.error("票据不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取票据登记详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取票据登记详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增票据登记
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增票据登记", notes = "新增票据登记信息")
    public String save(@Valid @FlexibleRequestBody BillRegistrationDTO dto) {
        try {
            TblBillRegistration bill = billRegistrationService.insertBillRegistration(dto);
            return JsonBean.success("票据登记成功", bill);
        } catch (BizException e) {
            log.warn("新增票据登记失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增票据登记失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 修改票据登记
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改票据登记", notes = "修改票据登记信息")
    public String update(@Valid @FlexibleRequestBody BillRegistrationDTO dto) {
        try {
            if (dto.getBillId() == null) {
                return JsonBean.error("票据ID不能为空");
            }
            TblBillRegistration bill = billRegistrationService.updateBillRegistration(dto);
            return JsonBean.success("票据修改成功", bill);
        } catch (BizException e) {
            log.warn("修改票据登记失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改票据登记失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除票据登记
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除票据登记", notes = "批量删除票据登记(逻辑删除)")
    public String delete(@RequestParam(value = "billIds", required = false) Long[] billIds) {
        try {
            boolean result = billRegistrationService.deleteBillRegistrationByIds(billIds);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (BizException e) {
            log.warn("删除票据登记失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除票据登记失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入票据
     */
    @PostMapping("/batch-import")
    @ApiOperation(value = "批量导入票据", notes = "批量导入票据登记信息")
    public String batchImport(@RequestParam(value = "billList", required = false) List<BillRegistrationDTO> billList) {
        try {
            int successCount = billRegistrationService.batchImportBills(billList);
            return JsonBean.success("成功导入 " + successCount + " 条票据");
        } catch (BizException e) {
            log.warn("批量导入票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量导入票据失败", e);
            return JsonBean.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 作废票据
     */
    @PostMapping("/cancel/{billId}")
    @ApiOperation(value = "作废票据", notes = "作废指定票据")
    public String cancel(@PathVariable Long billId, String reason) {
        try {
            boolean result = billRegistrationService.cancelBill(billId, reason);
            if (result) {
                return JsonBean.success("票据作废成功");
            } else {
                return JsonBean.error("票据作废失败");
            }
        } catch (BizException e) {
            log.warn("作废票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("作废票据失败", e);
            return JsonBean.error("作废失败: " + e.getMessage());
        }
    }

    /**
     * 导出票据登记数据
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出票据登记", notes = "导出票据登记数据到Excel")
    public void export(BillRegistrationQueryDTO queryDTO, javax.servlet.http.HttpServletResponse response) {
        try {
            billRegistrationService.exportBillRegistration(queryDTO, response);
        } catch (Exception e) {
            log.error("导出票据登记数据失败", e);
        }
    }

    /**
     * 获取票据登记统计数据
     */
    @PostMapping("/statistics")
    @ApiOperation(value = "获取票据登记统计", notes = "获取票据登记统计数据")
    public String statistics(BillRegistrationQueryDTO queryDTO) {
        try {
            Map<String, Object> statistics = billRegistrationService.getBillRegistrationStatistics(queryDTO);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取票据登记统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

