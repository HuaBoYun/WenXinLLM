package com.huabo.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.monitor.oracle.entity.TblAssesselement;
import com.huabo.monitor.oracle.entity.TblOrganization;
import com.huabo.monitor.service.TblAssEleCategoryService;
import com.huabo.monitor.service.TblAssessElementService;
import com.huabo.monitor.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "内部控制", tags = {"内部控制相关"})
@RequestMapping(value = "/nbkz")
public class FactorsController {
    @Resource
    private TblAssessElementService tblAssessElementService;

    @Resource
    private TblAssEleCategoryService tblAssEleCategoryService;


    /**
     * 要素维护 列表
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/gzdg/def_basic_list")
    @ApiOperation("要素维护列表-内容页面分页功能")
    public List<String> getgzdgBasicList(HttpServletRequest request,
                                     @ApiParam(name = "token", value = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @ApiParam(name = "pageNumber", value = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @ApiParam(name = "pageSize", value = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                     @ApiParam(name = "businesstype", value = "业务类别", required = false) @RequestParam(value = "businesstype", required = false) String businesstype,
                                     @ApiParam(name = "elementNumber", value = "要素编号", required = false) @RequestParam(value = "elementNumber", required = false) String elementNumber,
                                     @ApiParam(name = "elementname", value = "要素名称", required = false) @RequestParam(value = "elementname", required = false) String elementname,
                                     @ApiParam(name = "auditpoint", value = "审查要点", required = false) @RequestParam(value = "auditpoint", required = false) String auditpoint) {
        List<String> result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblAssesselement assesselement = new TblAssesselement();
            assesselement.setBusinesstype(businesstype);
            assesselement.setElementname(elementname);
            assesselement.setElementNumber(elementNumber);
            assesselement.setAuditpoint(auditpoint);
            List<String> btnList= tblAssessElementService.findByPageBean(pageNumber, pageSize, assesselement);
            resultMap.put("btnList", btnList);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = Collections.singletonList(jsonObjectMV.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return result;
    }


    /**
     * 要素删除
     *
     * @return
     */
    @RequestMapping(value = "/gzdg/def_basic_deleteIds", produces = "application/json; charset=utf-8")
    public JsonBean def_basic_del(@RequestBody List deleteIds) {
        return tblAssessElementService.deleteByIds(deleteIds);
    }

    /**
     * 要素保存
     *
     * @param assesselement
     * @return
     */
    @RequestMapping(value = "/gzdg/def_basic_save", produces = "application/json; charset=utf-8")
    public JsonBean gzdg_def_basic_save(@RequestBody TblAssesselement assesselement) {
        return tblAssessElementService.add(assesselement);
    }

    /**
     * 修改要素
     *
     * @return
     */
    @PostMapping(value = "/gzdg/def_basic_modify")
    public JsonBean gzdg_def_basic_modify(@RequestBody TblAssesselement tblAssesselement) {
        return tblAssessElementService.update(tblAssesselement);
    }

    /**
     * 要素维护导出 有选择项则导出选择的，没有则导出全部
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/gzdg/yswhexport", produces = "application/json; charset=utf-8")
    public void exportYswh(HttpServletRequest request, HttpServletResponse response) {
        String eleid = request.getParameter("eleid");
        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
        List<TblAssesselement> list = null;
        if (eleid != null && StringUtils.isNotBlank(eleid)) {
            list = this.tblAssessElementService.getAssEssByIn(eleid);
        } else {
            list = this.tblAssessElementService.getComany(attribute.getOrgid().toString());
        }
        String[] cNames = {"要素编号", "要素名称", "业务类别", "评分规则", "审查要点"};
        ExcelUtil exportUtil = new ExcelUtil("控制矩阵", cNames);
        HSSFCell cell = null;
        HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

        for (int j = 0; j < list.size(); j++) {
            HSSFRow bodyRow = exportUtil.getNextRow(j + 1);
            TblAssesselement c = (TblAssesselement) list.get(j);
            // 要素编号
            cell = bodyRow.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(c.getElementNumber());
            // 要素名称
            cell = bodyRow.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(c.getElementname());
            // 业务类别
            cell = bodyRow.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(c.getBusinesstype());
            // 评分规则
            cell = bodyRow.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(c.getAssessrules());
            // 审查要点
            cell = bodyRow.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(c.getAuditpoint());
        }
        exportUtil.writeOut(response, "要素维护.xls");
    }

}
