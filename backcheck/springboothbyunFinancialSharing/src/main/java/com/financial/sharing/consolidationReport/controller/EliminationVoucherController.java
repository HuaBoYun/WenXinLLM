package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.EliminationVoucherQueryParam;
import com.financial.sharing.consolidationReport.dto.VoucherGenerateParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationVoucher;
import com.financial.sharing.consolidationReport.service.EliminationVoucherService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 抵消凭证Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-抵消凭证生成")
@RestController
@RequestMapping("/consolidationReport/eliminationVoucher")
public class EliminationVoucherController {

    @Autowired
    private EliminationVoucherService eliminationVoucherService;

    /**
     * 生成抵消凭证
     */
    @ApiOperation("生成抵消凭证")
    @PostMapping("/generateVouchers")
    public MyJsonBean generateVouchers(@RequestBody VoucherGenerateParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            if (param.getModelId() == null || param.getModelId().isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (param.getPeriod() == null || param.getPeriod().isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            Map<String, Object> result = eliminationVoucherService.generateVouchers(param);
            return MyJsonBean.ok(result.get("message").toString(), result);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    /**
     * 查询抵消凭证列表
     */
    @ApiOperation("查询抵消凭证列表")
    @PostMapping("/getVoucherList")
    public MyJsonBean getVoucherList(@RequestBody EliminationVoucherQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            PageInfo<TblEliminationVoucher> pageInfo = eliminationVoucherService.getVoucherList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据凭证号查询抵消凭证列表
     */
    @ApiOperation("根据凭证号查询抵消凭证列表")
    @PostMapping("/getVouchersByNo")
    public MyJsonBean getVouchersByNo(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String voucherNo = params.get("voucherNo");
            if (voucherNo == null || voucherNo.isEmpty()) {
                return MyJsonBean.errorData("凭证号不能为空");
            }
            
            List<TblEliminationVoucher> list = eliminationVoucherService.getVouchersByNo(voucherNo);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据模型ID和期间查询凭证号列表
     */
    @ApiOperation("根据模型ID和期间查询凭证号列表")
    @PostMapping("/getVoucherNoList")
    public MyJsonBean getVoucherNoList(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            List<String> list = eliminationVoucherService.getVoucherNoList(modelId, period);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除抵消凭证
     */
    @ApiOperation("删除抵消凭证")
    @PostMapping("/deleteVouchers")
    public MyJsonBean deleteVouchers(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            eliminationVoucherService.deleteVouchers(modelId, period);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 确认抵消凭证
     */
    @ApiOperation("确认抵消凭证")
    @PostMapping("/confirmVouchers")
    public MyJsonBean confirmVouchers(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            eliminationVoucherService.confirmVouchers(modelId, period);
            return MyJsonBean.ok("确认成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("确认失败: " + e.getMessage());
        }
    }
}

