package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LetterOfCreditDTO;
import com.global.treasurer.dto.LetterOfCreditQueryDTO;
import com.global.treasurer.entity.TblLetterOfCredit;
import com.global.treasurer.service.ILetterOfCreditService;
import com.global.treasurer.vo.LetterOfCreditVO;
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
import java.util.List;
import java.util.Map;

/**
 * 信用证管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/bill/letters-of-credit")
@Api(tags = "信用证管理")
public class BillLetterOfCreditController {
    private static final Logger log = LoggerFactory.getLogger(BillLetterOfCreditController.class);

    @Autowired
    private ILetterOfCreditService letterOfCreditService;

    /**
     * 获取信用证列表(分页)
     */
    @PostMapping("/list")
    @ApiOperation("获取信用证列表(分页)")
    public String getLetterOfCreditList(@FlexibleRequestBody LetterOfCreditQueryDTO queryDTO) {
        try {
            PageInfo<LetterOfCreditVO> pageInfo = letterOfCreditService.selectLetterOfCreditList(queryDTO);
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取信用证列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取信用证详情
     */
    @PostMapping("/detail/{lcId}")
    @ApiOperation("获取信用证详情")
    public String getLetterOfCreditDetail(@PathVariable Long lcId) {
        try {
            LetterOfCreditVO vo = letterOfCreditService.selectLetterOfCreditById(lcId);
            if (vo == null) {
                return JsonBean.error("信用证不存在");
            }
            return new JsonBean(1, "查询成功", vo).toString();
        } catch (Exception e) {
            log.error("获取信用证详情失败, lcId={}", lcId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增信用证申请
     */
    @PostMapping("/save")
    @ApiOperation("新增信用证申请")
    public String saveLetterOfCredit(@Validated @FlexibleRequestBody LetterOfCreditDTO dto) {
        try {
            TblLetterOfCredit lc = letterOfCreditService.insertLetterOfCredit(dto);
            return new JsonBean(1, "新增成功", lc).toString();
        } catch (Exception e) {
            log.error("新增信用证失败", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改信用证申请
     */
    @PostMapping("/update")
    @ApiOperation("修改信用证申请")
    public String updateLetterOfCredit(@Validated @FlexibleRequestBody LetterOfCreditDTO dto) {
        try {
            TblLetterOfCredit lc = letterOfCreditService.updateLetterOfCredit(dto);
            return new JsonBean(1, "修改成功", lc).toString();
        } catch (Exception e) {
            log.error("修改信用证失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除信用证
     */
    @PostMapping("/delete")
    @ApiOperation("删除信用证")
    public String deleteLetterOfCredit(@RequestParam(value = "lcIds", required = false) Long[] lcIds) {
        try {
            boolean result = letterOfCreditService.deleteLetterOfCreditByIds(lcIds);
            if (result) {
                return new JsonBean(1, "删除成功", null).toString();
            }
            return JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除信用证失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 提交信用证申请
     */
    @PostMapping("/submit/{lcId}")
    @ApiOperation("提交信用证申请")
    public String submitLCApplication(@PathVariable Long lcId) {
        try {
            boolean result = letterOfCreditService.submitLCApplication(lcId);
            if (result) {
                return new JsonBean(1, "提交成功", null).toString();
            }
            return JsonBean.error("提交失败");
        } catch (Exception e) {
            log.error("提交信用证申请失败, lcId={}", lcId, e);
            return JsonBean.error("提交失败: " + e.getMessage());
        }
    }

    /**
     * 信用证申请审批
     */
    @PostMapping("/approve/{lcId}")
    @ApiOperation("信用证申请审批")
    public String approveLCApplication(@PathVariable Long lcId,
                                       @RequestParam Map<String, Object> approvalData) {
        try {
            boolean result = letterOfCreditService.approveLCApplication(lcId, approvalData);
            if (result) {
                return new JsonBean(1, "审批成功", null).toString();
            }
            return JsonBean.error("审批失败");
        } catch (Exception e) {
            log.error("信用证申请审批失败, lcId={}", lcId, e);
            return JsonBean.error("审批失败: " + e.getMessage());
        }
    }

    /**
     * 开立信用证
     */
    @PostMapping("/issue/{lcId}")
    @ApiOperation("开立信用证")
    public String issueLetterOfCredit(@PathVariable Long lcId) {
        try {
            boolean result = letterOfCreditService.issueLetterOfCredit(lcId);
            if (result) {
                return new JsonBean(1, "开立成功", null).toString();
            }
            return JsonBean.error("开立失败");
        } catch (Exception e) {
            log.error("开立信用证失败, lcId={}", lcId, e);
            return JsonBean.error("开立失败: " + e.getMessage());
        }
    }

    /**
     * 修改信用证(修订)
     */
    @PostMapping("/amend/{lcId}")
    @ApiOperation("修改信用证(修订)")
    public String amendLetterOfCredit(@PathVariable Long lcId,
                                      @RequestParam Map<String, Object> amendData) {
        try {
            boolean result = letterOfCreditService.amendLetterOfCredit(lcId, amendData);
            if (result) {
                return new JsonBean(1, "修改成功", null).toString();
            }
            return JsonBean.error("修改失败");
        } catch (Exception e) {
            log.error("修改信用证失败, lcId={}", lcId, e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 单据处理
     */
    @PostMapping("/documents/{lcId}")
    @ApiOperation("单据处理")
    public String processDocuments(@PathVariable Long lcId,
                                   @RequestParam Map<String, Object> documentData) {
        try {
            boolean result = letterOfCreditService.processDocuments(lcId, documentData);
            if (result) {
                return new JsonBean(1, "单据处理成功", null).toString();
            }
            return JsonBean.error("单据处理失败");
        } catch (Exception e) {
            log.error("单据处理失败, lcId={}", lcId, e);
            return JsonBean.error("单据处理失败: " + e.getMessage());
        }
    }

    /**
     * 获取单据列表
     */
    @PostMapping("/documents-list/{lcId}")
    @ApiOperation("获取单据列表")
    public String getDocumentList(@PathVariable Long lcId,
                                  @RequestParam(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> documentList = letterOfCreditService.getDocumentList(lcId, params);
            return new JsonBean(1, "查询成功", documentList).toString();
        } catch (Exception e) {
            log.error("获取单据列表失败, lcId={}", lcId, e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
