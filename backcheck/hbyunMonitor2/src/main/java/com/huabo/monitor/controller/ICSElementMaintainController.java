package com.huabo.monitor.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAssEleCategory;
import com.huabo.monitor.entity.TblAssesselement;
import com.huabo.monitor.entity.TblFlow;
import com.huabo.monitor.service.ITblAssesselementService;
import com.huabo.monitor.service.TblAssEleCategoryService;
import com.huabo.monitor.service.TblAutonoNumberService;
import com.huabo.monitor.service.TblFlowService;
import com.huabo.monitor.util.ExcelUtil;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.util.PageBean;
import com.huabo.monitor.vo.param.fieldOrgStaffId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * ICS: Internal Control Setting
 * element maintain controller
 */
@RestController
@Slf4j
@Tag(name="内控设置-要素维护",description="内控设置-要素维护")
@RequestMapping(value = "/nbkz")
public class ICSElementMaintainController {

	@Resource
    public PageBean pageBean;

    @Resource
    ITblAssesselementService tblAssessElementService;

    @Resource
    TblAutonoNumberService tblAutonoNumberService;

    @Resource
    TblFlowService tblFlowService;

    @Resource
    TblAssEleCategoryService tblAssEleCategoryService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "要素维护-列表查询成功",
            busType = "内控设置",
            fail = "要素维护-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-列表")
    @GetMapping(value = "/gzdg/def_basic_list")
    public JsonBean gzdg_def_basic_list(
            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页行数") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @Parameter(name = "choiceSearch", description = "区域显隐控制") @RequestParam(value = "choiceSearch", required = false) String choiceSearch,
            @Parameter(name = "elementname", description = "要素名称")@RequestParam(required = false) String elementname,
            @Parameter(name = "businessattribute", description = "businessattribute")@RequestParam(required = false) String businessattribute,
            @Parameter(name = "businesstype", description = "业务类别")@RequestParam(required = false) String businesstype,
            @Parameter(name = "elementnumber", description = "要素编号")@RequestParam(required = false) String elementnumber,
            @Parameter(name = "auditpoint", description = "审查要点")@RequestParam(required = false) String auditpoint,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        //增加查询条件
        TblAssesselement assesselement =new TblAssesselement();
        assesselement.setElementname(elementname);
        assesselement.setBusinessattribute(businessattribute);
        assesselement.setBusinesstype(businesstype);
        assesselement.setElementnumber(elementnumber);
        assesselement.setAuditpoint(auditpoint);
     //IPage<TblAssesselement> byPageBean = tblAssessElementService.findByPageBean(userToken.getCurrentOrg().getOrgid().toString(), pageNumber, assesselement,pageSize);
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }

        PageInfo<TblAssesselement> pageinfo=tblAssessElementService.getPageBean(userToken.getCurrentOrg().getOrgid().toString(), pageNumber, assesselement,pageSize,userToken);
        IPageResult<TblAssesselement> pageInfo=new IPageResult<TblAssesselement>().buildIpage(pageinfo);

        Map<String, Object> map = new HashMap<>();
        map.put("pageBean", pageInfo);
        map.put("assesselement", assesselement);
        map.put("choiceSearch", choiceSearch);
        return ResponseFormat.retParam(1, 200, map);
    }

    @OperationLog(
            success = "要素维护-新增-编号获取查询成功",
            busType = "内控设置",
            fail = "要素维护-新增-编号获取查询失败",
            operationType = OperationType.SELECT,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-新增-编号获取")
    @GetMapping(value = "/code/findAutoNum")
    public JsonBean findAutoNum(HttpServletResponse response,
                                   @Parameter(name = "tblName", description = "tblName") @RequestParam(value = "tblName") String tblName,
                                   @Parameter(name = "column", description = "column") @RequestParam(value = "column") String column,
                                   @Parameter(name = "orgCol", description = "orgCol") @RequestParam(value = "orgCol") String orgCol,
                                   @Parameter(name = "noId", description = "noId") @RequestParam(value = "noId") Integer noId,
                                   @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        JsonBean flowNextId = null;
        try {
            flowNextId = tblAutonoNumberService.findFlowNextId(tblName, column, orgCol, noId, null, null, null, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.reset();

        return flowNextId == null ? ResponseFormat.retParam(1, 200, flowNextId) : flowNextId;
    }

    @OperationLog(
            success = "要素维护-保存成功",
            busType = "内控设置",
            fail = "要素维护-保存失败",
            operationType = OperationType.ADD,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-保存")
    @PostMapping(value = "/gzdg/def_basic_save", produces = "application/json; charset=utf-8")
    public JsonBean gzdg_def_basic_save(@Parameter(name = "assesselement", description = "TblAssesselement实体类") @RequestBody TblAssesselement assesselement,
                                        @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        String orgid = userToken.getCurrentOrg().getOrgid().toString();
        if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
            TblAssesselement tblAssesselement = this.tblAssessElementService.getNumber(assesselement.getElementnumber(), orgid);
            if (null == tblAssesselement) {
                assesselement.setTblcomany(orgid);
                assesselement.setAsseleid(RandomUtil.uuBigDecimalId());
                assesselement.setLinkdeptid(userToken.getLinkDetp().getOrgid());
                assesselement.setCreatestaffid(userToken.getStaffid());
                assesselement.setCreatetime(new Date());
                this.tblAssessElementService.add(assesselement);
                return ResponseFormat.retParam(1, 200, "success");
            } else if (tblAssesselement.getAsseleid().equals(assesselement.getAsseleid())) {
                tblAssesselement.setTblcomany(tblAssesselement.getTblcomany());
                tblAssesselement.setAssessrules(assesselement.getAssessrules());
                tblAssesselement.setAuditpoint(assesselement.getAuditpoint());
                tblAssesselement.setBusinesstype(assesselement.getBusinesstype());
                tblAssesselement.setBusinessattribute(assesselement.getBusinessattribute());
                tblAssesselement.setElementname(assesselement.getElementname());
                tblAssesselement.setStatus(assesselement.getStatus());
              
                tblAssesselement.setSecrectLevelId(assesselement.getSecrectLevelId());
                tblAssesselement.setStaffScopeIds(assesselement.getStaffScopeIds());
                tblAssesselement.setStaffScopeNames(assesselement.getStaffScopeNames());
                //灵活字段赋值
            	fieldOrgStaffId item=new fieldOrgStaffId();
				BeanUtils.copyProperties(assesselement,item); 
				BeanUtils.copyProperties(item,tblAssesselement); 
                this.tblAssessElementService.Update(tblAssesselement);
                return ResponseFormat.retParam(1, 200, "success");
            } else {
                return ResponseFormat.retParam(0, 202, null);
            }
        }
        return ResponseFormat.retParam(0, 90009, null);
    }


    @OperationLog(
            success = "要素维护-复制成功",
            busType = "内控设置",
            fail = "要素维护-复制失败",
            operationType = OperationType.ADD,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-复制")
    @PostMapping(value = "/gzdg/def_basic_copy", produces = "application/json; charset=utf-8")
    public JsonBean def_basic_copy(@Parameter(name = "assesselement", description = "TblAssesselement实体类") @RequestBody TblAssesselement assesselement,
                                        @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        String orgid = userToken.getCurrentOrg().getOrgid().toString();
        if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
            TblAssesselement tblAssesselement = this.tblAssessElementService.getNumber(assesselement.getElementnumber(), orgid);
            if (null == tblAssesselement) {
                assesselement.setTblcomany(orgid);
                assesselement.setAsseleid(RandomUtil.uuBigDecimalId());
                this.tblAssessElementService.add(assesselement);
                return ResponseFormat.retParam(1, 200, "success");
            }  else {
                return ResponseFormat.retParam(0, 202, null);
            }
        }
        return ResponseFormat.retParam(0, 90009, null);
    }

    @OperationLog(
            success = "要素维护-修改时详情获取查询成功",
            busType = "内控设置",
            fail = "要素维护-修改时详情获取查询失败",
            operationType = OperationType.SELECT,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-修改时详情获取")
    @GetMapping(value = "/gzdg/def_modify")
    public JsonBean gzdg_def_modify(
            @Parameter(name = "basicId", description = "basicId") @RequestParam(value = "basicId") BigDecimal basicId,
            @Parameter(name = "choiceSearch", description = "区域显隐控制") @RequestParam(value = "choiceSearch") String choiceSearch,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblAssesselement assesselement = this.tblAssessElementService.findById(basicId);
        BigDecimal orgid = userToken.getCurrentOrg().getOrgid();

        String sql = "SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY =" + orgid;
        List<TblFlow> flows = new ArrayList<TblFlow>();
        List list = tblFlowService.findBySql(sql);
        if (list != null) {
            for (Object object : list) {
                Object[] oa = (Object[]) object;
                TblFlow flow = new TblFlow();
                flow.setFlowid(oa[0] != null ? new BigDecimal(oa[0].toString()) : null);
                flow.setFlowname(oa[1] != null ? oa[1].toString() : "");
                flows.add(flow);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("conTocat", assesselement.getBusinesstype());
        map.put("flows", flows);
        map.put("assesselement", assesselement);
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return ResponseFormat.retParam(1, 200, map);
    }

    @OperationLog(
            success = "要素维护-删除成功",
            busType = "内控设置",
            fail = "要素维护-删除失败",
            operationType = OperationType.DELETE,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-删除")
    @DeleteMapping(value = "/gzdg/def_basic_del", produces = "application/json; charset=utf-8")
    public JsonBean def_basic_del(
            @Parameter(name = "deleteId", description = "deleteId") @RequestParam(value = "deleteId") String deleteId,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        if (StringUtils.isNotBlank(deleteId)) {
            String[] ids = deleteId.split(",");
            StringBuffer sb = new StringBuffer();
            for (String id : ids) {
                List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryService.getAssesscategoryBytmplId(new BigDecimal(id));

                if (assEleCategories.size() > 0) {
                	TblAssesselement ass=this.tblAssEleCategoryService.getAssesselementBycatid(assEleCategories.get(0).getAsseleid());
                	assEleCategories.get(0).setAssesselement(ass);
                    sb.append("," + assEleCategories.get(0).getAssesselement().getElementnumber());
                } else {
                    this.tblAssessElementService.delete(this.tblAssessElementService.get(new BigDecimal(id)));
                }
            }
            if (sb.length() > 0) {
                return ResponseFormat.retParam(0, 90007, null);
            }
            return ResponseFormat.retParam(1, 200, "删除成功");
        }
        return ResponseFormat.retParam(0, 201, "fail");
    }


    @OperationLog(
            success = "要素维护-点击修改按钮之前调用-判断是否已经被引用查询成功",
            busType = "内控设置",
            fail = "要素维护-点击修改按钮之前调用-判断是否已经被引用查询失败",
            operationType = OperationType.SELECT,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-点击修改按钮之前调用-判断是否已经被引用")
    @RequestMapping(value = "/gzdg/def_basic_check", produces = "application/json; charset=utf-8")
    public JsonBean def_basic_check(
            @Parameter(name = "id", description = "id") @RequestParam(value = "id") String id,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if (StringUtils.isNotBlank(id)) {
            String[] ids = id.split(",");
            StringBuffer sb = new StringBuffer();
            for (String i : ids) {
                List<TblAssEleCategory> assEleCategories = this.tblAssEleCategoryService.getAssesscategoryBytmplId(new BigDecimal(i));
                if (assEleCategories.size() > 0) {
                	TblAssesselement ass=this.tblAssEleCategoryService.getAssesselementBycatid(assEleCategories.get(0).getAsseleid());
                	assEleCategories.get(0).setAssesselement(ass);
                    sb.append("," + assEleCategories.get(0).getAssesselement().getElementnumber());
                }
            }
            if (sb.length() > 0) {
                return ResponseFormat.retParam(0, 90010, null);
            }
            return ResponseFormat.retParam(1, 200, null);
        }
        return ResponseFormat.retParam(0, 201, null);
    }


    @OperationLog(
            success = "要素维护-导出(有选择项则导出选择的，没有则导出全部)成功",
            busType = "内控设置",
            fail = "要素维护-导出(有选择项则导出选择的，没有则导出全部)失败",
            operationType = OperationType.EXPORT,
            subType = "要素维护"
    )
    @Operation(summary = "要素维护-导出(有选择项则导出选择的，没有则导出全部)")
    @GetMapping(value = "/gzdg/yswhexport", produces = "application/json; charset=utf-8")
    public void exportYswh(HttpServletRequest request, HttpServletResponse response,
            @Parameter(name = "eleid", description = "eleid") @RequestParam(value = "eleid") String eleid,
            @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
        	return ;
        }
    	  HSSFWorkbook wb =new HSSFWorkbook();
        BigDecimal orgid = userToken.getCurrentOrg().getOrgid();

        List<TblAssesselement> list = null;
        if (StringUtils.isNotBlank(eleid)) {
            list = tblAssessElementService.getAssEssByIn(eleid);
        } else {
            list = tblAssessElementService.getComany(orgid.toString());
        }
        String[] cNames = {"要素编号", "要素名称", "业务类别", "评分规则", "审查要点"};
        ExcelUtil exportUtil = new ExcelUtil("控制矩阵", cNames);
        HSSFCell cell = null;
        HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
//
//        for (int j = 0; j < list.size(); j++) {
//            HSSFRow bodyRow = exportUtil.getNextRow(j + 1);
//            TblAssesselement c = (TblAssesselement) list.get(j);
//            // 要素编号
//            cell = bodyRow.createCell(0);
//            cell.setCellStyle(bodyStyle);
//            cell.setCellValue(c.getElementnumber());
//            // 要素名称
//            cell = bodyRow.createCell(1);
//            cell.setCellStyle(bodyStyle);
//            cell.setCellValue(c.getElementname());
//            // 业务类别
//            cell = bodyRow.createCell(2);
//            cell.setCellStyle(bodyStyle);
//            cell.setCellValue(c.getBusinesstype());
//            // 评分规则
//            cell = bodyRow.createCell(3);
//            cell.setCellStyle(bodyStyle);
//            cell.setCellValue(c.getAssessrules());
//            // 审查要点
//            cell = bodyRow.createCell(4);
//            cell.setCellStyle(bodyStyle);
//            cell.setCellValue(c.getAuditpoint());
//        }
//
        List<Object[]> blacklist = new ArrayList<Object[]>(0);
		Object[] objs = null;
		int i = 0;
		for (TblAssesselement budget : list) {
			objs = new Object[5];
			objs[0] = budget.getElementnumber();
			objs[1] = budget.getElementname();
			objs[2] = budget.getBusinesstype();
			objs[3] = budget.getAssessrules();
			objs[4] = budget.getAuditpoint();
			blacklist.add(objs);
		}
	    response.setContentType("application/octet-stream;charset=UTF-8");
    	try {
			 response.addHeader("Content-Disposition", "attachment;filename=" + new String("yswh".getBytes(), "iso-8859-1") + ".xlsx");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        ServletOutputStream outputStream = response.getOutputStream();
        ImportOrExportExcelUtil.exportExcel(cNames, blacklist, outputStream, null);

//       // exportUtil.writeOut(response, "要素维护.xls");
//		response.reset();
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        ServletOutputStream os = null;
//    	try {
//			 response.addHeader("Content-Disposition", "attachment;filename=" + new String("yswh".getBytes(), "iso-8859-1") + ".xlsx");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			os = response.getOutputStream();
//			wb.write(os);
//			os.flush();
//			os.close();
//			wb.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(os != null) {
//                    os.close();
//                }
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
    }




}
