package com.global.treasurer.controller;

import com.global.treasurer.dto.BillPledgeFinancingDTO;
import com.global.treasurer.dto.BillPoolDTO;
import com.global.treasurer.dto.BillPoolQueryDTO;
import com.global.treasurer.entity.TblBillPool;
import com.global.treasurer.entity.TblPledgeFinancing;
import com.global.treasurer.service.IBillPoolService;
import com.global.treasurer.service.IPledgeFinancingService;
import com.global.treasurer.vo.BillPoolVO;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 票据池管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping({"/bill/pool", "/financial/bill/pool"})
@Api(tags = "票据池管理")
public class BillPoolController {
    private static final Logger log = LoggerFactory.getLogger(BillPoolController.class);

    @Autowired
    private IBillPoolService billPoolService;

    @Autowired
    private IPledgeFinancingService pledgeFinancingService;

    @Resource
    private UserProvider userProvider;

    /**
     * 权限验证方法
     */
    private boolean validateUser(HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return false;
        }
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询票据池列表", notes = "分页查询票据池列表")
    public String list(BillPoolQueryDTO queryDTO,
                      HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            PageInfo<BillPoolVO> pageInfo = billPoolService.selectBillPoolList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据池列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据池", notes = "分页查询票据池列表")
    public String page(BillPoolQueryDTO queryDTO,
                      HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            PageInfo<BillPoolVO> pageInfo = billPoolService.selectBillPoolList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据池列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{poolId}")
    @ApiOperation(value = "获取票据池详情", notes = "根据票据池ID获取详细信息")
    public String detail(@PathVariable Long poolId,
                         HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            BillPoolVO poolVO = billPoolService.selectBillPoolById(poolId);
            return JsonBean.success(poolVO);
        } catch (Exception e) {
            log.error("获取票据池详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "创建票据池", notes = "创建新的票据池")
    public String save(@FlexibleRequestBody BillPoolDTO dto,
                      HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            TblBillPool pool = billPoolService.insertBillPool(dto);
            return JsonBean.success(pool);
        } catch (Exception e) {
            log.error("创建票据池失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新票据池", notes = "更新票据池信息")
    public String update(@FlexibleRequestBody BillPoolDTO dto,
                        HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            TblBillPool pool = billPoolService.updateBillPool(dto);
            return JsonBean.success(pool);
        } catch (Exception e) {
            log.error("更新票据池失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除票据池", notes = "批量删除票据池")
    public String delete(@RequestParam(value = "poolIds", required = false) Long[] poolIds,
                       HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            boolean result = billPoolService.deleteBillPool(poolIds);
            return JsonBean.success(result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            log.error("删除票据池失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/add-bills")
    @ApiOperation(value = "添加票据到池", notes = "向票据池添加票据")
    public String addBills(@RequestParam Map<String, Object> params,
                          HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            Long poolId = Long.valueOf(params.get("poolId").toString());
            Long[] billIds = (Long[]) params.get("billIds");
            boolean result = billPoolService.addBillsToPool(poolId, billIds);
            return JsonBean.success(result ? "添加成功" : "添加失败");
        } catch (Exception e) {
            log.error("添加票据到池失败", e);
            return JsonBean.error("添加失败: " + e.getMessage());
        }
    }

    @PostMapping("/remove-bills")
    @ApiOperation(value = "从池中移除票据", notes = "从票据池移除票据")
    public String removeBills(@RequestParam Map<String, Object> params,
                             HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            Long poolId = Long.valueOf(params.get("poolId").toString());
            Long[] billIds = (Long[]) params.get("billIds");
            boolean result = billPoolService.removeBillsFromPool(poolId, billIds);
            return JsonBean.success(result ? "移除成功" : "移除失败");
        } catch (Exception e) {
            log.error("从池中移除票据失败", e);
            return JsonBean.error("移除失败: " + e.getMessage());
        }
    }

    @PostMapping("/bills/{poolId}")
    @ApiOperation(value = "获取票据池内的票据", notes = "获取票据池内的所有票据")
    public String getBills(@PathVariable Long poolId,
                          HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            List<Map<String, Object>> bills = billPoolService.getBillsInPool(poolId);
            return JsonBean.success(bills);
        } catch (Exception e) {
            log.error("获取票据池内的票据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/pledge-financing")
    @ApiOperation(value = "质押融资", notes = "票据池质押融资")
    public String pledgeFinancing(@FlexibleRequestBody BillPledgeFinancingDTO dto,
                                HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            TblPledgeFinancing financing = pledgeFinancingService.insertPledgeFinancing(dto);
            return JsonBean.success(financing);
        } catch (Exception e) {
            log.error("质押融资失败", e);
            return JsonBean.error("融资失败: " + e.getMessage());
        }
    }

    @PostMapping("/financing-records/{poolId}")
    @ApiOperation(value = "获取票据池融资记录", notes = "获取票据池的融资记录")
    public String getFinancingRecords(@PathVariable Long poolId,
                                    HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            List<com.global.treasurer.vo.PledgeFinancingRecordVO> records =
                pledgeFinancingService.selectFinancingRecordsByPoolId(poolId);
            return JsonBean.success(records);
        } catch (Exception e) {
            log.error("获取票据池融资记录失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 导出票据池数据
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出票据池数据", notes = "导出票据池数据到Excel")
    public void export(BillPoolQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return;
            }
            billPoolService.exportBillPool(queryDTO, response);
        } catch (Exception e) {
            log.error("导出票据池数据失败", e);
        }
    }

    /**
     * 获取票据池统计数据
     */
    @PostMapping("/statistics")
    @ApiOperation(value = "获取票据池统计", notes = "获取票据池统计数据")
    public String statistics(BillPoolQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> statistics = billPoolService.getBillPoolStatistics(queryDTO);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取票据池统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取可入池票据列表
     */
    @PostMapping("/available-bills")
    @ApiOperation(value = "获取可入池票据", notes = "获取可用于入池的票据列表")
    public String getAvailableBills(@RequestParam(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            List<Map<String, Object>> bills = billPoolService.getAvailableBillsForPool(params);
            return JsonBean.success(bills);
        } catch (Exception e) {
            log.error("获取可入池票据列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 冻结票据池
     */
    @PostMapping("/freeze/{poolId}")
    @ApiOperation(value = "冻结票据池", notes = "冻结指定票据池")
    public String freeze(@PathVariable Long poolId, HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            boolean result = billPoolService.freezeBillPool(poolId);
            return result ? JsonBean.success("冻结成功") : JsonBean.error("冻结失败");
        } catch (Exception e) {
            log.error("冻结票据池失败", e);
            return JsonBean.error("冻结失败: " + e.getMessage());
        }
    }

    /**
     * 关闭票据池
     */
    @PostMapping("/close/{poolId}")
    @ApiOperation(value = "关闭票据池", notes = "关闭指定票据池")
    public String close(@PathVariable Long poolId, HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            boolean result = billPoolService.closeBillPool(poolId);
            return result ? JsonBean.success("关闭成功") : JsonBean.error("关闭失败");
        } catch (Exception e) {
            log.error("关闭票据池失败", e);
            return JsonBean.error("关闭失败: " + e.getMessage());
        }
    }

    /**
     * 生成票据池报告
     */
    @PostMapping("/report/{poolId}")
    @ApiOperation(value = "生成票据池报告", notes = "根据票据池ID生成完整报告，包含池基本信息、池内票据、融资记录等")
    public String generateReport(@PathVariable Long poolId, HttpServletResponse response) {
        try {
            if (!validateUser(response)) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> report = billPoolService.generatePoolReport(poolId);
            return JsonBean.success(report);
        } catch (Exception e) {
            log.error("生成票据池报告失败", e);
            return JsonBean.error("生成报告失败: " + e.getMessage());
        }
    }
}

