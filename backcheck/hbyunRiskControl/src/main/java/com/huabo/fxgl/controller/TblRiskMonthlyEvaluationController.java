package com.huabo.fxgl.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ExportUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.RiskDto;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.RiskCoping;
import com.huabo.fxgl.entity.TblControlEntries;
import com.huabo.fxgl.entity.TblRiskMonthlyEvaluationEntity;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.service.IControlmatrixService;
import com.huabo.fxgl.service.IRiskCopingService;
import com.huabo.fxgl.service.TblRiskMonthlyEvaluationService;
import com.huabo.fxgl.util.FillAttribute;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 月度评估控制器
 * <p>提供风险月度评估及月度评估汇总的列表查询、填报、导出等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/monthlyEvaluation")
@RestController
@Tag(name="月度评估/月度评估汇总",description="月度评估/月度评估汇总")
@Slf4j
public class TblRiskMonthlyEvaluationController {

    @Resource
    private TblRiskMonthlyEvaluationService tblRiskMonthlyEvaluationService;

    @Resource
    private UserProvider userProvider;
    @Autowired
    private IRiskCopingService copingService;

    @Autowired
    private  TblControlEntriesMapper tblControlEntriesMapper;

    @Autowired
    private IControlmatrixService controlmatrixService;

    @Value("${application.administrators:}")
	private String administrators;

    @Value("${application.auditlegaldepartment:}")
  	private String auditlegaldepartment;
    /**
     * 分公司上报到总公司
     * @param token
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "上报审批的时候调用【{{#id}}】",
            busType = "重大风险",
            fail = "上报审批的时候调用【{{#id}}】",
            operationType = OperationType.UPDATE,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/monthlyEvaluationSubmit",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "月度评估-上报审批的时候调用")
    public JsonBean monthlyEvaluationSubmit(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id) throws Exception {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskMonthlyEvaluationService.monthlyEvaluationSubmit(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }



    /**
     * 总公司查询所有上报的风险评估汇总列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "月度评估汇总列表",
            busType = "重大风险",
            fail = "月度评估汇总列表",
            operationType = OperationType.SELECT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/queryRiskAssessmentSummaryList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "月度评估汇总列表")
    public JsonBean queryRiskAssessmentSummaryList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                          @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                          @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
			@Parameter(name = "createUnitidName", description = "单位名称", required = false) @RequestParam(value = "createUnitidName", required = false) String createUnitidName,
			@Parameter(name = "deptName", description = "部门名称", required = false) @RequestParam(value = "deptName", required = false) String deptName,
			@Parameter(name = "year", description = "年度", required = false) @RequestParam(value = "year", required = false) String year,
			@Parameter(name = "sftb", description = "是否填报", required = false) @RequestParam(value = "sftb", required = false) String sftb,
			@Parameter(name = "risknumber", description = "风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
			@Parameter(name = "monthL", description = "查询条件 -上报月份LEFT", required = false) @RequestParam(value = "monthL", required = false) BigDecimal monthL,
	        @Parameter(name = "monthR", description = "查询条件 -上报月份RIGHT", required = false) @RequestParam(value = "monthR", required = false) BigDecimal monthR,
			@Parameter(name = "oneRisk", description = "一级风险", required = false) @RequestParam(value = "oneRisk", required = false) String oneRisk,
			@Parameter(name = "twoRisk", description = "二级风险", required = false) @RequestParam(value = "twoRisk", required = false) String twoRisk,
			@Parameter(name = "threeRisk", description = "三级风险", required = false) @RequestParam(value = "threeRisk", required = false) String threeRisk
    		) throws Exception {

        JsonBean jsonBean = null;
        try {
        	TblRiskMonthlyEvaluationEntity entity=new TblRiskMonthlyEvaluationEntity();
        	entity.setRisknumber(risknumber);
        	entity.setMonthL(monthL);
        	entity.setMonthR(monthR);
        	entity.setCreateUnitidName(createUnitidName);
        	entity.setOneRisk(oneRisk);
        	entity.setTwoRisk(twoRisk);
        	entity.setThreeRisk(threeRisk);
        	entity.setUnitDeptidName(deptName);
        	entity.setYear(year);
            jsonBean = tblRiskMonthlyEvaluationService.queryRiskAssessmentSummaryList(token, pageNumber, pageSize,entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }

    /**
     * 月度评估-根据风险点查看评估列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "根据风险点查看评估列表",
            busType = "重大风险",
            fail = "根据风险点查看评估列表",
            operationType = OperationType.SELECT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/queryMonthlyEvaluationList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "月度评估-根据风险点查看评估列表")
    public JsonBean monthlyEvaluationList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                          @Parameter(name = "riskid", description = "关联风险点id") @RequestParam(value = "riskid", required = false) BigDecimal riskid) throws Exception {


        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskMonthlyEvaluationService.queryMonthlyEvaluationList(token, pageNumber, pageSize,riskid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }

    /**
     * 月度评估新增
     * @param token
     * @param tblRiskMonthlyEvaluationEntity
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "月度评估表新增",
            busType = "重大风险",
            fail = "月度评估表新增",
            operationType = OperationType.ADD,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/insertOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "月度评估表新增")
    public JsonBean ReportingInsertOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblRiskMonthlyEvaluationEntity tblRiskMonthlyEvaluationEntity, String attIds) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonthlyEvaluationService.insertOrUpdateReporting(tblRiskMonthlyEvaluationEntity, token,attIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 单位月度风险评估表详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "单位月度风险评估表详情【{{#id}}】",
            busType = "重大风险",
            fail = "单位月度风险评估表详情【{{#id}}】",
            operationType = OperationType.ADD,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/monthlyEvaluationDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "单位月度风险评估表详情")
    public JsonBean monthlyEvaluationDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonthlyEvaluationService.monthlyEvaluationDetails(id, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }


    @OperationLog(
            success = "月度评估汇总导出【{{#id}}】",
            busType = "重大风险",
            fail = "月度评估汇总导出【{{#id}}】",
            operationType = OperationType.EXPORT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/exportMonthlyEvaluationSummary",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
    @Operation(summary = "月度评估汇总导出")
	public void  exportMonthlyEvaluationSummary(HttpServletRequest request, HttpServletResponse response,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
											 @Parameter(name = "id", description = "相关风险事件主键", required = false) @RequestParam(value = "id", required = false) String id,
											 @Parameter(name = "createUnitidName", description = "单位名称", required = false) @RequestParam(value = "createUnitidName", required = false) String createUnitidName,
											 @Parameter(name = "deptName", description = "部门名称", required = false) @RequestParam(value = "deptName", required = false) String deptName,
												@Parameter(name = "year", description = "年度", required = false) @RequestParam(value = "year", required = false) String year,
												@Parameter(name = "sftb", description = "是否填报", required = false) @RequestParam(value = "sftb", required = false) String sftb,
											 @Parameter(name = "risknumber", description = "风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
												@Parameter(name = "monthL", description = "查询条件 -上报月份LEFT", required = false) @RequestParam(value = "monthL", required = false) BigDecimal monthL,
										        @Parameter(name = "monthR", description = "查询条件 -上报月份RIGHT", required = false) @RequestParam(value = "monthR", required = false) BigDecimal monthR,
												@Parameter(name = "oneRisk", description = "一级风险", required = false) @RequestParam(value = "oneRisk", required = false) String oneRisk,
												@Parameter(name = "twoRisk", description = "二级风险", required = false) @RequestParam(value = "twoRisk", required = false) String twoRisk,
												@Parameter(name = "threeRisk", description = "三级风险", required = false) @RequestParam(value = "threeRisk", required = false) String threeRisk,
											 Object task
										 ) {
		try {
			log.info("月度评估汇总-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
			TblRiskMonthlyEvaluationEntity ent=new TblRiskMonthlyEvaluationEntity();
        	ent.setRisknumber(risknumber);
        	ent.setMonthL(monthL);
        	ent.setMonthR(monthR);
        	ent.setCreateUnitidName(createUnitidName);
        	ent.setOneRisk(oneRisk);
        	ent.setTwoRisk(twoRisk);
        	ent.setThreeRisk(threeRisk);
        	ent.setUnitDeptidName(deptName);
        	ent.setYear(year);
		    List<TblRiskMonthlyEvaluationEntity>   entityList = tblRiskMonthlyEvaluationService.exportMonthlyEvaluationSummary(id,ent,token);
		    String[] cNames = {"上报单位","关联风险点","责任部门","一级风险","二级风险", "三级风险","风险描述","风险源分析(导致风险发生的潜在因素)",
					"现有应对措施","相关制度和规程索引", "典型风险事件描述", "风险发生可能性评价标准", "对应分值","风险影响程度评价标准",
					"对应分值","风险评分","风险等级" ,"风险变化趋势","上升/下降原因","是否新增风险","上报月份","具体控制措施","预计完成时间","责任人","有限公司领导","配合单位或部门	","是否完成","措施完成时间","管控措施是否逾期","本月风险管控措施及实施情况","下月风险管控措施"};
		   List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			 for (TblRiskMonthlyEvaluationEntity entity : entityList) {
					objs = new Object[22];
					objs[0] = entity.getCreateUnitidName();
					objs[1] = entity.getRisknumber();
					objs[2] = entity.getUnitDeptidName();
					objs[3] = entity.getOneRisk();
					objs[4] = entity.getTwoRisk();
					objs[5] = entity.getThreeRisk();
					objs[6] =entity.getDetailRisk();
					objs[7] =entity.getAnalysisRisk();
					objs[8] = entity.getAnalysisSol();
					objs[9] = entity.getAnalysisRel();
					objs[10] = entity.getAnalysisEve();
					objs[11] =entity.getAssessStan();
					objs[12] =entity.getAssessScoreOne();
					objs[13] =entity.getAssessInf();
					objs[14] = entity.getAssessScoreTwo();
					objs[15] = entity.getAssessSco();
					objs[16] = FillAttribute.assessGradeMap.get(entity.getAssessGrade());
	  				objs[17] = FillAttribute.riskChangeMap.get(entity.getRiskChange());
	  				objs[18] =entity.getReason();
	  				objs[19] = FillAttribute.isNewRiskMap.get(entity.getIsNewRisk());
	  			    objs[20] = FillAttribute.monthMap.get(entity.getMonth()); ;
					QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
		            queryWrapper.eq("RISKID", entity.getRiskid());
		            List<RiskCoping> copings = copingService.list(queryWrapper);
		            Map<String, Object> result = new HashMap<String, Object>();
		            if(null!=copings && copings.size()>0){
		                RiskCoping coping = copings.get(0);
		                List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
		               if(cons!=null&&cons.size()>0){
		            	   Controlmatrix con=cons.get(0);
		            	   List<TblControlEntries> entitys=tblControlEntriesMapper.getList(con.getConmatid());
			                objs[21] = entitys;
		               }else{
		            	   objs[21] = "";
		               }
		                }
				contractlist.add(objs);
		 }
			  response.setHeader("Content-Disposition", "attachment;filename=" + new String("月度评估汇总".getBytes(),"UTF-8") + ".xlsx");
			 fillData(cNames,contractlist,response.getOutputStream(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  public void fillData(String[] cNames, List<Object[]> objList1, ServletOutputStream outputStream, String fileUrl){
	  try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			ExportUtil exportUtil = new ExportUtil(workbook, sheet);
			XSSFCellStyle titleStyle = null;
			XSSFFont titleFont = workbook.createFont();
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setBorderTop(CellStyle.BORDER_THIN); // 设置边框
			titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
			titleStyle.setBorderRight(CellStyle.BORDER_THIN);
			titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
			titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
			titleStyle.setFont(titleFont);
			titleStyle.setWrapText(true); // 关键设置：启用自动换行
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			 Row headerRow = sheet.createRow(0);
	     	 headerRow.setHeightInPoints(60);
	   	    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	        for (int i = 0; i < cNames.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(cNames[i]);
	            cell.setCellStyle(titleStyle); // 应用标题样式
	           // sheet.setColumnWidth(i, cWidths[i]);
	        }
	  	      int rowIndex = 1; // 从第 2 行开始写入数据
	  	      int sizeNum = 0;
			  XSSFCell cell = null;
			 if (objList1 != null && objList1.size() > 0) {
				for (int j = sizeNum; j < objList1.size(); j++) {
					XSSFRow bodyRow = sheet.createRow(rowIndex);
					Object[] obj = objList1.get(j);
					for (int i = 0; i < obj.length; i++) {
						   ObjectMapper mapper = new ObjectMapper();
						   String arr=obj[obj.length-1].toString();
						   List<TblControlEntries> list=null;
						   if(StringUtils.isNotBlank(arr)){
		                   list =(List<TblControlEntries>) obj[obj.length-1];
		     	  	        if (list.size() > 1&&i < (obj.length-1)) { //合并行数据， 第21行是json数据行，此行不合并
			                 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+list.size()-1, i==obj.length?obj.length-1:i,i==obj.length?obj.length-1:i));
		     	  	        }
						   }
						 if(i==(obj.length-1)&&obj[obj.length-1].toString().length()>4){//判断是json数据行，且json有数据
							 for(int k=0;k<list.size();k++){
								 TblControlEntries map=list.get(k);
								    cell = bodyRow.createCell(21);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField1() == null ? "" : map.getField1());
									cell = bodyRow.createCell(22);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField2()== null ? "" : format.format(map.getField2()));
									cell = bodyRow.createCell(23);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField3() == null ? "" : map.getField3());
									cell = bodyRow.createCell(24);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField6()== null ? "" : map.getField6());
									cell = bodyRow.createCell(25);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField7() == null ? "" : map.getField7());
									cell = bodyRow.createCell(26);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField4() == null ? "" : map.getField4());
									cell = bodyRow.createCell(27);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField5() == null ? "" : format.format(map.getField5()));
									cell = bodyRow.createCell(28);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField11() == null ? "" : map.getField11());
									cell = bodyRow.createCell(29);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField10() == null ? "" : map.getField10());
									cell = bodyRow.createCell(30);
									cell.setCellStyle(bodyStyle);
									cell.setCellValue(map.getField12() == null ? "" : map.getField12());
									if(list.size()>1&&k<(list.size()-1)){
										rowIndex++;
									  bodyRow = sheet.createRow(rowIndex);
									  for (int e = 0; e <20; e++) {
						                     cell = bodyRow.createCell(e);
						                     cell.setCellStyle(bodyStyle);
						                 }
									}
							 }
						}else if(i==(obj.length-1)&&obj[obj.length-1].toString().length()<4){ //渲染可能没有json的数据的框
								  for (int e = 21; e <31; e++) {
					                     cell = bodyRow.createCell(e);
					                     cell.setCellStyle(bodyStyle);
					                 }
						}else{//普通数据写入
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						}
					}
					rowIndex++;
					sizeNum++;
				}
			}
		  	      if (StringUtils.isNotBlank(fileUrl) && !fileUrl.equals("1")) {
						File file = new File(fileUrl);
						file.createNewFile();
						FileOutputStream stream = null;
						stream = FileUtils.openOutputStream(file);
						workbook.write(stream);
					} else {
						workbook.write(outputStream);
					}
		  	    workbook.close();
	  	        outputStream.close();
	  } catch (Exception e) {
			// TODO: handle exception
		  e.printStackTrace();
		}
		  	        System.out.println("Excel 文件导出成功！");
  }



    @OperationLog(
            success = "查看评估列表导出【{{#id}}】",
            busType = "重大风险",
            fail = "查看评估列表导出【{{#id}}】",
            operationType = OperationType.EXPORT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/exportMonthlyEvaluation",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
  	@ResponseBody
      @Operation(summary = "月度评估节点-查看评估列表导出")
  	public void  exportMonthlyEvaluation(HttpServletRequest request, HttpServletResponse response,
  											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
  											 @Parameter(name = "id", description = "相关风险事件主键", required = false) @RequestParam(value = "id", required = false) String id,
  											 @Parameter(name = "createUnitidName", description = "单位名称", required = false) @RequestParam(value = "createUnitidName", required = false) String createUnitidName,
  											 @Parameter(name = "riskid", description = "关联风险点名称", required = false) @RequestParam(value = "riskid", required = false) String riskid , Object task
  										 ) {
  		try {
  			log.info("单位月度风险评估表-导出Excel");
  			response.setContentType("application/binary;charset=UTF-8");

  		   List<TblRiskMonthlyEvaluationEntity>   entityList = tblRiskMonthlyEvaluationService.exportMonthlyEvaluation(token,id,createUnitidName,riskid);
  		   String[] cNames = {"关联风险点","责任部门","一级风险","二级风险", "三级风险","风险描述","风险源分析(导致风险发生的潜在因素)",
  					"现有应对措施","相关制度和规程索引", "典型风险事件描述", "风险发生可能性评价标准", "对应分值","风险影响程度评价标准",
  					"对应分值","风险评分","风险等级","风险变化趋势","上升/下降原因","是否新增风险","上报月份","上报单位"};
			int[] cWidths=new int[]{3500,3500,3500,3500,3500,3600,12500,12500,3700,3700,3700,3700,3700,3700,3700,3700,3700,3700,3700,3700,3700};
  		   List<Object[]> contractlist = new ArrayList<Object[]>(0);
  			Object[] objs = null;
  			 for (TblRiskMonthlyEvaluationEntity entity : entityList) {
  				objs = new Object[21];
  				objs[0] = entity.getRisknumber();
  				objs[1] = entity.getUnitDeptidName();
  				objs[2] = entity.getOneRisk();
  				objs[3] = entity.getTwoRisk();
  				objs[4] = entity.getThreeRisk();
  				objs[5] =entity.getDetailRisk();
  				objs[6] =entity.getAnalysisRisk();
  				objs[7] = entity.getAnalysisSol();
  				objs[8] = entity.getAnalysisRel();
  				objs[9] = entity.getAnalysisEve();
  				objs[10] =entity.getAssessStan();
  				objs[11] =entity.getAssessScoreOne();
  				objs[12] =entity.getAssessInf();
  				objs[13] = entity.getAssessScoreTwo();
  				objs[14] = entity.getAssessSco();
  			 	objs[15] = FillAttribute.assessGradeMap.get(entity.getAssessGrade());
  				objs[16] = FillAttribute.riskChangeMap.get(entity.getRiskChange());
  				objs[17] =entity.getReason();
  				objs[18] = FillAttribute.isNewRiskMap.get(entity.getIsNewRisk());
  			    objs[19] = FillAttribute.monthMap.get(entity.getMonth()); ;
  				objs[20] = entity.getCreateUnitidName();
  				contractlist.add(objs);
  		 }
  			response.setHeader("Content-Disposition", "attachment;filename=" + new String("单位月度风险评估表导出".getBytes(),"UTF-8") + ".xlsx");
  			ServletOutputStream outputStream = response.getOutputStream();
  			ImportOrExportExcelUtil.exportExcelSetWith(cNames, contractlist, outputStream,cWidths, null);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
    /**
     * 单位月度风险评估表详情的附件
     * @param request
     * @param token
     * @param id
     * @return
     */

    @OperationLog(
            success = "月度风险评估表详情的附件",
            busType = "重大风险",
            fail = "月度风险评估表详情的附件",
            operationType = OperationType.SELECT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/getMonthlyEvaluationAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "单位月度风险评估表详情的附件")
    public JsonBean getMonthlyEvaluationAttInfo(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                            @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblRiskMonthlyEvaluationService.getMonthlyEvaluationAttInfo(token,id);
        } catch (Exception e) {
            ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 单位月度风险评估表详情删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "单位月度风险评估表详情删除【{{#id}}】",
            busType = "重大风险",
            fail = "单位月度风险评估表详情删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/monthlyEvaluationADelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "单位月度风险评估表详情删除")
    public JsonBean monthlyEvaluationADelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskMonthlyEvaluationService.monthlyEvaluationADelete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 月度评估节点--外层显示已经评估的风险点
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "外层显示已经评估的风险点",
            busType = "重大风险",
            fail = "外层显示已经评估的风险点",
            operationType = OperationType.SELECT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/queryAssessedRiskList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "月度评估节点--外层显示已经评估的风险点")
    public JsonBean  queryAssessedRiskList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                          @Parameter(name = "id", description = "导出的时候传入的id") @RequestParam(value = "id", required = false) String id,
                                          @ModelAttribute RiskDto dto) throws Exception {


        JsonBean jsonBean = null;
        try {
        	 TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        	  //用于判断是否为风险管理员；该角色能看到本公司所有的月度评估信息
     		Integer authorityType;
//     		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())||JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
     		if(staffUtil.getRoleNames().contains(administrators) || staffUtil.getRoleNames().contains(auditlegaldepartment)) {
     			authorityType = 1;
     		} else {
     			authorityType = 0;
     		}
        	  Risk risk = new Risk();
              risk.setBelongsto(dto.getBelongsto());
              risk.setRisknumber(dto.getRisknumber());
              risk.setRiskname(dto.getRiskname());
      		  risk.setStaffid(staffUtil.getStaffid());
      		  risk.setReportmonth(dto.getReportmonth());
      		  risk.setUnitname(dto.getUnitname());
            jsonBean = tblRiskMonthlyEvaluationService.queryAssessedRiskList(token,risk,id, pageNumber, pageSize,authorityType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }


    @OperationLog(
            success = "外层已经评估的风险点列表导出",
            busType = "重大风险",
            fail = "外层已经评估的风险点列表导出",
            operationType = OperationType.EXPORT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/exportRiskEvaluated",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
   	@ResponseBody
       @Operation(summary = "月度评估节点--外层已经评估的风险点列表导出")
   	public void  exportRiskEvaluated(HttpServletRequest request, HttpServletResponse response,
   											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
   											@Parameter(name = "id", description = "导出的时候传入的id") @RequestParam(value = "id", required = false) String id,
                                            @ModelAttribute RiskDto dto
   										 ) {
   		try {
   			log.info("月度评估节点--外层已经评估的风险点列表-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
       	    TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
       	 Integer authorityType;
  		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())||JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
  			authorityType = 1;
  		} else {
  			authorityType = 0;
  		}
     	  Risk risk = new Risk();
           risk.setBelongsto(dto.getBelongsto());
           risk.setRisknumber(dto.getRisknumber());
           risk.setRiskname(dto.getRiskname());
   		  risk.setStaffid(staffUtil.getStaffid());
   		  risk.setReportmonth(dto.getReportmonth());
   		  risk.setUnitname(dto.getUnitname());
   		   List<Risk>   entityList = tblRiskMonthlyEvaluationService.exportRiskEvaluated(token,risk,id,authorityType);
   		   String[] cNames = {"风险编号","风险名称","风险描述","所属公司","最新上报月份","创建时间","关闭审批状态","是否关闭"};
			int[] cWidths=new int[]{3500,3500,13500,3500,3500,3500,3500,3500};
   			List<Object[]> contractlist = new ArrayList<Object[]>(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   			Object[] objs = null;

   			for (Risk entity : entityList) {
   				objs = new Object[8];
   				objs[0] = entity.getRisknumber();
   				objs[1] = entity.getRiskname();
   				objs[2] = entity.getRiskdes();
   				objs[3] = entity.getUnitname();
   				objs[4] = entity.getReportmonth()!=null?entity.getReportmonth():"";
   				objs[5] = entity.getRiskcreatedt()!=null?sdf.format(entity.getRiskcreatedt()):"";
   				objs[6] = entity.getClosestatus()!=null?FillAttribute.statusMap.get(entity.getClosestatus()):"未审批";
   				objs[7] = entity.getRiskstatus()!=null?FillAttribute.riskStatusMap.get(entity.getRiskstatus()):"未关闭";
   				contractlist.add(objs);
   		 }
   			response.setHeader("Content-Disposition", "attachment;filename=" + new String("已评估风险点导出".getBytes(),"UTF-8") + ".xlsx");
   			ServletOutputStream outputStream = response.getOutputStream();
   			ImportOrExportExcelUtil.exportExcelSetWith(cNames, contractlist, outputStream,cWidths, null);

   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   	}

    @OperationLog(
            success = "15号定时任务进行月度评估",
            busType = "重大风险",
            fail = "15号定时任务进行月度评估",
            operationType = OperationType.SELECT,
            subType = "月度评估汇总"
    )
    @RequestMapping(value = "/monthlyEvaluation",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "15号定时任务进行月度评估")
    public void monthlyEvaluation(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                      ) throws Exception {
        JsonBean jsonBean = null;
        try {
        	tblRiskMonthlyEvaluationService.isReportVersionT();
        	} catch (Exception e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/remindMonthlyEvaluation",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "测试10-14号提醒风控负责人进行月度评估")
    public JsonBean remindMonthlyEvaluation(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                      ) throws Exception {
        JsonBean jsonBean = null;
        try {
        	jsonBean=tblRiskMonthlyEvaluationService.remindMonthlyEvaluation(token);
        	} catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @RequestMapping(value = "/testSQL",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "测试")
    public JsonBean testSQL(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
    		@Parameter(name = "id", description = "导出的时候传入的id") @RequestParam(value = "id", required = false) int id
    		) throws Exception {
        JsonBean jsonBean = null;
        try {
        	jsonBean=tblRiskMonthlyEvaluationService.test(token,id);
        	} catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
}
