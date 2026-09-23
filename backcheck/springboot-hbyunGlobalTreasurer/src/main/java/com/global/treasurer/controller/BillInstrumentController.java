package com.global.treasurer.controller;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillInstrumentDTO;
import com.global.treasurer.dto.BillInstrumentQueryDTO;
import com.global.treasurer.entity.TblBillInstrument;
import com.global.treasurer.service.IBillInstrumentService;
import com.global.treasurer.vo.BillInstrumentVO;
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

/**
 * 票据管理通用Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@RestController
@RequestMapping("/bill/instrument")
@Api(tags = "票据管理通用接口")
public class BillInstrumentController {
    private static final Logger log = LoggerFactory.getLogger(BillInstrumentController.class);

    @Autowired
    private IBillInstrumentService billInstrumentService;

    @PostMapping("/list")
    @ApiOperation(value = "查询票据列表", notes = "分页查询票据列表")
    public String list(@FlexibleRequestBody BillInstrumentQueryDTO queryDTO) {
        try {
            PageInfo<BillInstrumentVO> pageInfo = billInstrumentService.selectBillInstrumentList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{instrumentId}")
    @ApiOperation(value = "获取票据详情", notes = "根据票据ID获取详细信息")
    public String detail(@PathVariable Long instrumentId) {
        try {
            BillInstrumentVO vo = billInstrumentService.selectBillInstrumentById(instrumentId);
            if (vo == null) {
                return JsonBean.error("票据不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取票据详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取票据详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增票据", notes = "新增票据信息")
    public String save(@Valid @FlexibleRequestBody BillInstrumentDTO dto) {
        try {
            TblBillInstrument instrument = billInstrumentService.insertBillInstrument(dto);
            return JsonBean.success("票据创建成功", instrument);
        } catch (BizException e) {
            log.warn("新增票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增票据失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改票据", notes = "修改票据信息")
    public String update(@Valid @FlexibleRequestBody BillInstrumentDTO dto) {
        try {
            if (dto.getInstrumentId() == null) {
                return JsonBean.error("票据ID不能为空");
            }
            TblBillInstrument instrument = billInstrumentService.updateBillInstrument(dto);
            return JsonBean.success("票据修改成功", instrument);
        } catch (BizException e) {
            log.warn("修改票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改票据失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除票据", notes = "批量删除票据(逻辑删除)")
    public String delete(Long[] instrumentIds) {
        try {
            boolean result = billInstrumentService.deleteBillInstrumentByIds(instrumentIds);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (BizException e) {
            log.warn("删除票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除票据失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/endorse/{instrumentId}")
    @ApiOperation(value = "票据背书", notes = "将票据背书转让给其他方")
    public String endorse(@PathVariable Long instrumentId, @RequestParam Map<String, Object> endorseData) {
        try {
            boolean result = billInstrumentService.endorseBill(instrumentId, endorseData);
            return result ? JsonBean.success("票据背书成功") : JsonBean.error("票据背书失败");
        } catch (BizException e) {
            log.warn("票据背书失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("票据背书失败", e);
            return JsonBean.error("背书失败: " + e.getMessage());
        }
    }

    @PostMapping("/discount/{instrumentId}")
    @ApiOperation(value = "票据贴现", notes = "将票据进行贴现")
    public String discount(@PathVariable Long instrumentId, @RequestParam Map<String, Object> discountData) {
        try {
            boolean result = billInstrumentService.discountBill(instrumentId, discountData);
            return result ? JsonBean.success("票据贴现成功") : JsonBean.error("票据贴现失败");
        } catch (BizException e) {
            log.warn("票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("票据贴现失败", e);
            return JsonBean.error("贴现失败: " + e.getMessage());
        }
    }
}

