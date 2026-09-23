package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillIdentificationDTO;
import com.global.treasurer.dto.BillIdentificationQueryDTO;
import com.global.treasurer.entity.TblBillIdentification;
import com.global.treasurer.service.IBillIdentificationService;
import com.global.treasurer.vo.BillIdentificationVO;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 票据标识管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@RestController
@RequestMapping("/bill/identification")
@Api(tags = "票据标识管理")
public class BillIdentificationController {
    @Autowired
    private IBillIdentificationService billIdentificationService;

    /**
     * 分页查询票据标识列表
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据标识列表", notes = "根据条件分页查询票据标识")
    public String page(@FlexibleRequestBody BillIdentificationQueryDTO queryDTO) {
        try {
            PageInfo<BillIdentificationVO> pageInfo = billIdentificationService.selectBillIdentificationList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取票据标识详情
     */
    @PostMapping("/detail/{id}")
    @ApiOperation(value = "获取票据标识详情", notes = "根据ID获取票据标识详情")
    public String detail(@PathVariable("id") Long id) {
        try {
            BillIdentificationVO vo = billIdentificationService.selectBillIdentificationById(id);
            if (vo == null) {
                return JsonBean.error("标识不存在");
            }
            return JsonBean.success(vo);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增票据标识
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增票据标识", notes = "新增票据标识信息")
    public String save(@Valid @FlexibleRequestBody BillIdentificationDTO dto) {
        try {
            TblBillIdentification entity = billIdentificationService.insertBillIdentification(dto);
            return JsonBean.success("新增成功");
        } catch (Exception e) {
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改票据标识
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改票据标识", notes = "修改票据标识信息")
    public String update(@Valid @FlexibleRequestBody BillIdentificationDTO dto) {
        try {
            billIdentificationService.updateBillIdentification(dto);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除票据标识
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除票据标识", notes = "批量删除票据标识")
    public String delete(@RequestParam(value = "ids", required = false) Long[] ids) {
        try {
            billIdentificationService.deleteBillIdentificationByIds(ids);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取票据标识树形结构
     */
    @PostMapping("/tree")
    @ApiOperation(value = "获取票据标识树形结构", notes = "获取票据标识的树形结构数据")
    public String tree(BillIdentificationQueryDTO queryDTO) {
        try {
            if (queryDTO == null) {
                queryDTO = new BillIdentificationQueryDTO();
            }
            List<BillIdentificationVO> tree = billIdentificationService.selectIdentificationTree(queryDTO);
            return JsonBean.success(tree);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据类型查询标识列表
     */
    @PostMapping("/listByType")
    @ApiOperation(value = "根据类型查询标识列表", notes = "根据标识类型查询启用的标识列表")
    public String listByType(@RequestParam String type) {
        try {
            List<BillIdentificationVO> list = billIdentificationService.selectByType(type);
            return JsonBean.success(list);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 批量启用标识
     */
    @PostMapping("/batch-enable")
    @ApiOperation(value = "批量启用标识", notes = "批量启用票据标识")
    public String batchEnable(@RequestParam(value = "ids", required = false) Long[] ids) {
        try {
            billIdentificationService.batchUpdateEnabled(ids, 1);
            return JsonBean.success("启用成功");
        } catch (Exception e) {
            return JsonBean.error("启用失败: " + e.getMessage());
        }
    }

    /**
     * 批量禁用标识
     */
    @PostMapping("/batch-disable")
    @ApiOperation(value = "批量禁用标识", notes = "批量禁用票据标识")
    public String batchDisable(@RequestParam(value = "ids", required = false) Long[] ids) {
        try {
            billIdentificationService.batchUpdateEnabled(ids, 0);
            return JsonBean.success("禁用成功");
        } catch (Exception e) {
            return JsonBean.error("禁用失败: " + e.getMessage());
        }
    }
}

