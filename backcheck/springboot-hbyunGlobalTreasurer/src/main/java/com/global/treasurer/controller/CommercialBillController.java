package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CommercialBillDTO;
import com.global.treasurer.dto.CommercialBillQueryDTO;
import com.global.treasurer.entity.TblCommercialBill;
import com.global.treasurer.service.ICommercialBillService;
import com.global.treasurer.vo.CommercialBillVO;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 商业汇票管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/commercial")
@Api(tags = "商业汇票管理")
public class CommercialBillController {
    private static final Logger log = LoggerFactory.getLogger(CommercialBillController.class);

    @Autowired
    private ICommercialBillService commercialBillService;

    /**
     * 分页查询商业汇票
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询商业汇票列表", notes = "分页查询商业汇票列表")
    public String list(CommercialBillQueryDTO queryDTO) {
        try {
            PageInfo<CommercialBillVO> pageInfo = commercialBillService.selectCommercialBillList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询商业汇票列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取商业汇票详情
     */
    @PostMapping("/detail/{billId}")
    @ApiOperation(value = "获取商业汇票详情", notes = "根据汇票ID获取详细信息")
    public String detail(@PathVariable Long billId) {
        try {
            CommercialBillVO vo = commercialBillService.selectCommercialBillById(billId);
            if (vo == null) {
                return JsonBean.error("商业汇票不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取商业汇票详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取商业汇票详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建商业汇票
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增商业汇票", notes = "新增商业汇票信息")
    public String save(@Valid CommercialBillDTO dto) {
        try {
            TblCommercialBill bill = commercialBillService.insertCommercialBill(dto);
            return JsonBean.success("商业汇票创建成功", bill);
        } catch (BizException e) {
            log.warn("新增商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增商业汇票失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新商业汇票
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改商业汇票", notes = "修改商业汇票信息")
    public String update(@Valid CommercialBillDTO dto) {
        try {
            if (dto.getBillId() == null) {
                return JsonBean.error("汇票ID不能为空");
            }
            TblCommercialBill bill = commercialBillService.updateCommercialBill(dto);
            return JsonBean.success("商业汇票修改成功", bill);
        } catch (BizException e) {
            log.warn("修改商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改商业汇票失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除商业汇票
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除商业汇票", notes = "批量删除商业汇票(逻辑删除)")
    public String delete(Long[] billIds) {
        try {
            boolean result = commercialBillService.deleteCommercialBillByIds(billIds);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (BizException e) {
            log.warn("删除商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除商业汇票失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 开立汇票
     */
    @PostMapping("/issue")
    @ApiOperation(value = "开立商业汇票", notes = "开立新的商业汇票")
    public String issue(Map<String, Object> issueData) {
        try {
            boolean result = commercialBillService.issueBill(issueData);
            return result ? JsonBean.success("汇票开立成功") : JsonBean.error("汇票开立失败");
        } catch (BizException e) {
            log.warn("开立商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("开立商业汇票失败", e);
            return JsonBean.error("开立失败: " + e.getMessage());
        }
    }

    /**
     * 承兑汇票
     */
    @PostMapping("/accept/{billId}")
    @ApiOperation(value = "承兑商业汇票", notes = "承兑指定的商业汇票")
    public String accept(@PathVariable Long billId, Map<String, Object> acceptData) {
        try {
            boolean result = commercialBillService.acceptBill(billId, acceptData);
            return result ? JsonBean.success("汇票承兑成功") : JsonBean.error("汇票承兑失败");
        } catch (BizException e) {
            log.warn("承兑商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("承兑商业汇票失败", e);
            return JsonBean.error("承兑失败: " + e.getMessage());
        }
    }

    /**
     * 背书汇票
     */
    @PostMapping("/endorse/{billId}")
    @ApiOperation(value = "背书商业汇票", notes = "将商业汇票背书转让给其他方")
    public String endorse(@PathVariable Long billId, Map<String, Object> endorseData) {
        try {
            boolean result = commercialBillService.endorseBill(billId, endorseData);
            return result ? JsonBean.success("汇票背书成功") : JsonBean.error("汇票背书失败");
        } catch (BizException e) {
            log.warn("背书商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("背书商业汇票失败", e);
            return JsonBean.error("背书失败: " + e.getMessage());
        }
    }

    /**
     * 贴现汇票
     */
    @PostMapping("/discount/{billId}")
    @ApiOperation(value = "贴现商业汇票", notes = "将商业汇票进行贴现")
    public String discount(@PathVariable Long billId, Map<String, Object> discountData) {
        try {
            boolean result = commercialBillService.discountBill(billId, discountData);
            return result ? JsonBean.success("汇票贴现成功") : JsonBean.error("汇票贴现失败");
        } catch (BizException e) {
            log.warn("贴现商业汇票失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("贴现商业汇票失败", e);
            return JsonBean.error("贴现失败: " + e.getMessage());
        }
    }

    /**
     * 到期处理
     */
    @PostMapping("/mature/{billId}")
    @ApiOperation(value = "商业汇票到期处理", notes = "处理到期的商业汇票")
    public String mature(@PathVariable Long billId) {
        try {
            boolean result = commercialBillService.matureBill(billId);
            return result ? JsonBean.success("汇票到期处理成功") : JsonBean.error("汇票到期处理失败");
        } catch (BizException e) {
            log.warn("商业汇票到期处理失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("商业汇票到期处理失败", e);
            return JsonBean.error("到期处理失败: " + e.getMessage());
        }
    }
}
