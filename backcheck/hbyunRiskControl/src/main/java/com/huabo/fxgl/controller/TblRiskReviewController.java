package com.huabo.fxgl.controller;


import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.TblRiskReview;
import com.huabo.fxgl.entity.TblRiskReviewOpinion;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.TblRiskReviewService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.util.WordExporter;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险审查控制器
 * <p>提供风险审查的列表查询、审查操作、审查结果等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/review")
@RestController
@Tag(name="风险审查",description="风险审查")
@Slf4j
public class TblRiskReviewController {

    @Autowired
    private TblRiskReviewService tblRiskReviewService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Resource
    private UserProvider userProvider;

	@Value("${application.administrators:}")
	private String administrators;


    @OperationLog(
            success = "风险审查列表",
            busType = "风险审查",
            fail = "风险审查列表",
            operationType = OperationType.SELECT,
            subType = "风险审查"
    )
    @RequestMapping(value = "/riskReviewList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查列表")
    public JsonBean riskReviewList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                   @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(name = "mattername", description = "事项名称") @RequestParam(value = "mattername", required = false) String mattername,
                                   @Parameter(name = "mattercode", description = "三重一大事项编码") @RequestParam(value = "mattercode", required = false) String mattercode,
                                   @Parameter(name = "riskreviewcode", description = "风险审查报告编码") @RequestParam(value = "riskreviewcode", required = false) String riskreviewcode,
                                   @Parameter(name = "state", description = "状态") @RequestParam(value = "state", required = false) String state
                                   ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            String groupCompanyID = SysConfig.get("groupCompanyID");
			Integer authorityType;
			//集团查看全部数据； 分公司查看自己的数据；
	    	//风险管理员看自己公司的数据
			//JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())
			if (groupCompanyID.equals(staffUtil.getLinkOrg().getOrgid().toString())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
			PageInfo<TblRiskReview> pageInfo = tblRiskReviewService.riskReviewList(token, pageNumber, pageSize,mattername,mattercode,riskreviewcode,state,staffUtil.getStaffid(),authorityType);
			PageResult<TblRiskReview> build = new PageResult<TblRiskReview>().build(pageInfo);

			hashMap.put("pageInfo",pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }


    @OperationLog(
            success = "风险审查修改",
            busType = "风险审查",
            fail = "风险审查修改",
            operationType = OperationType.UPDATE,
            subType = "风险审查"
    )
    @RequestMapping(value = "/updateRiskReview",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查修改")
    public JsonBean updateRiskReview(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     TblRiskReview tblRiskReview) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            //只有创建人能修改、提交自己的数据
            if(staffUtil.getStaffid().equals(tblRiskReview.getCreatestaffid())){
            tblRiskReviewService.updateRiskReview(tblRiskReview);
            }else{
            	hashMap.put("data", "只有创建人能修改、提交自己的数据!");
                return  ResponseFormat.retParam(0, 10001, hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    @OperationLog(
            success = "风险审查新增",
            busType = "风险审查",
            fail = "风险审查新增",
            operationType = OperationType.ADD,
            subType = "风险审查"
    )
    @RequestMapping(value = "/insertRiskReview",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查新增")
    public JsonBean insertRiskReview(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     TblRiskReview tblRiskReview) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskReview.setCreatetime(new Date());
            tblRiskReview.setCreatestaffid(staffUtil.getStaffid());
            tblRiskReview.setLinkorgid(staffUtil.getLinkOrg().getOrgid());
            tblRiskReview.setLinkdeptid(staffUtil.getLinkDetp().getOrgid());
            tblRiskReviewService.insertRiskReview(tblRiskReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }



    @OperationLog(
            success = "风险审查详情【{{#reviewId}}】",
            busType = "风险审查",
            fail = "风险审查详情【{{#reviewId}}】",
            operationType = OperationType.SELECT,
            subType = "风险审查"
    )
    @RequestMapping(value = "/riskReviewDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查详情")
    public JsonBean riskReviewDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "reviewId", description = "风险审查主键", required = true) @RequestParam(value = "reviewId", required = true) String reviewId
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            TblRiskReview riskReview = tblRiskReviewService.riskReviewDetails(reviewId);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
            if(riskReview!=null){
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(riskReview,item);
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,riskReview );
            }
            List<Attachment>  attachments=new ArrayList<>();
            if (riskReview!=null&&!StringUtils.isEmpty(riskReview.getMatterfileids())){
                  attachments  =  attachmentService.findAttachmentFiles(riskReview.getMatterfileids());
            }
          //项目审查意见附件：
            TblRiskReviewOpinion Opinion = tblRiskReviewService.riskReviewOpinionDetails(reviewId);
            if (Opinion!=null&&!StringUtils.isEmpty(Opinion.getMatterfileids())){
            	List<Attachment>  aaa  =  attachmentService.findAttachmentFiles(Opinion.getMatterfileids());
            	attachments.addAll(aaa);
          }
            hashMap.put("attachments",attachments);


            hashMap.put("riskReview",riskReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }


    @OperationLog(
            success = "风险审查删除【{{#reviewId}}】",
            busType = "风险审查",
            fail = "风险审查删除【{{#reviewId}}】",
            operationType = OperationType.DELETE,
            subType = "风险审查"
    )
    @RequestMapping(value = "/riskReviewDelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查删除")
    public JsonBean riskReviewDelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "reviewId", description = "风险审查主键", required = true) @RequestParam(value = "reviewId", required = true) String reviewId
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
          tblRiskReviewService.riskReviewDelete(reviewId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }


    @OperationLog(
            success = "根据项目编码获取风险审查报告编码",
            busType = "风险审查",
            fail = "根据项目编码获取风险审查报告编码",
            operationType = OperationType.SELECT,
            subType = "风险审查"
    )
    @RequestMapping(value = "/riskReviewCode",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "根据项目编码获取风险审查报告编码")
    public JsonBean riskReviewCode(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "projectCode", description = "项目编码", required = true) @RequestParam(value = "projectCode", required = true) String projectCode
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            String yearStr = String.valueOf(DateUtil.thisYear());
            String num = "-";
            Long codeNumber = tblRiskReviewService.queryCodeNumber(projectCode);

            if (codeNumber < 10){
                codeNumber ++;
                num +="0"+codeNumber;
                projectCode+="-"+"FK"+"-"+"23"+"-"+yearStr+num;
            }else {
                codeNumber ++;
                num += codeNumber;
                projectCode+="-"+"FK"+"-"+"23"+"-"+yearStr+num;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, projectCode);
    }




    @OperationLog(
            success = "风险审查意见列表",
            busType = "风险审查",
            fail = "风险审查意见列表",
            operationType = OperationType.SELECT,
            subType = "风险审查意见"
    )
    @RequestMapping(value = "/riskReviewOpinionList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查意见列表")
    public JsonBean riskReviewOpinionList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                   @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(name = "mattername", description = "事项名称") @RequestParam(value = "mattername", required = false) String mattername,
                                   @Parameter(name = "mattercode", description = "三重一大事项编码") @RequestParam(value = "mattercode", required = false) String mattercode,
                                   @Parameter(name = "riskreviewcode", description = "风险审查报告编码") @RequestParam(value = "riskreviewcode", required = false) String riskreviewcode,
                                   @Parameter(name = "unitname", description = "首页穿透查询条件") @RequestParam(value = "unitname", required = false) String unitname

                                   ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
			Integer authorityType;
			if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
			PageInfo<TblRiskReview> pageInfo = tblRiskReviewService.riskReviewOpinionList(token, pageNumber, pageSize,mattername,mattercode,riskreviewcode,"6",staffUtil.getStaffid(),authorityType,unitname);
			PageResult<TblRiskReview> build = new PageResult<TblRiskReview>().build(pageInfo);

			hashMap.put("pageInfo",pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }



    @OperationLog(
            success = "风险审查意见新增",
            busType = "风险审查",
            fail = "风险审查意见新增",
            operationType = OperationType.ADD,
            subType = "风险审查意见"
    )
    @RequestMapping(value = "/insertRiskReviewOpinion",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查意见新增/修改")
    public JsonBean insertRiskReviewOpinion(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     TblRiskReviewOpinion tblRiskReview) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskReview.setCreatetime(new Date());
            tblRiskReview.setCreatestaffid(staffUtil.getStaffid());
            tblRiskReview.setLinkdeptid(staffUtil.getLinkDetp().getOrgid());
            tblRiskReview.setLinkorg(staffUtil.getLinkOrg().getOrgid());
            tblRiskReviewService.saveReviewOpinion(tblRiskReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, tblRiskReview);
    }



    @OperationLog(
            success = "风险审查意见详情【{{#id}}】",
            busType = "风险审查",
            fail = "风险审查意见详情【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "风险审查意见"
    )
    @RequestMapping(value = "/riskReviewOpinionDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查意见详情")
    public JsonBean riskReviewOpinionDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            TblRiskReviewOpinion riskReview = tblRiskReviewService.riskReviewOpinionDetails(id);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
        	//对灵活字段中的姓名名称及机构名称赋值
            if(riskReview!=null){
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(riskReview,item);
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,riskReview );
            }
            if (riskReview!=null&&!StringUtils.isEmpty(riskReview.getMatterfileids())){
                List<Attachment>  attachments  =  attachmentService.findAttachmentFiles(riskReview.getMatterfileids());
                hashMap.put("attachments",attachments);
            }
            hashMap.put("opinion",riskReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }



    @OperationLog(
            success = "风险审查意见删除【{{#id}}】",
            busType = "风险审查",
            fail = "风险审查意见删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "风险审查意见"
    )
    @RequestMapping(value = "/riskReviewOpinionDelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险审查意见删除")
    public JsonBean riskReviewOpinionDelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
          tblRiskReviewService.riskReviewOpinionDelete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }




    @OperationLog(
            success = "项目风险审查导出",
            busType = "风险审查",
            fail = "项目风险审查导出",
            operationType = OperationType.EXPORT,
            subType = "项目风险审查"
    )
    @RequestMapping(value = "/exportRiskReviewList",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
    @Operation(summary = "项目风险审查导出")
	public void  exportRiskReviewList(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(name = "mattername", description = "事项名称") @RequestParam(value = "mattername", required = false) String mattername,
            @Parameter(name = "mattercode", description = "三重一大事项编码") @RequestParam(value = "mattercode", required = false) String mattercode,
            @Parameter(name = "riskreviewcode", description = "风险审查报告编码") @RequestParam(value = "riskreviewcode", required = false) String riskreviewcode,
            @Parameter(name = "state", description = "状态") @RequestParam(value = "state", required = false) String state  ) throws Exception {
		try {
			log.info("项目风险审查-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
			  TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
	            String groupCompanyID = SysConfig.get("groupCompanyID");
				Integer authorityType;
				if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())||groupCompanyID.equals(staffUtil.getLinkOrg().getOrgid().toString())) {
					authorityType = 1;
				} else {
					authorityType = 0;
				}
				List<TblRiskReview> objList = tblRiskReviewService.getRiskReviewListExport(token,mattername,mattercode,riskreviewcode,state,staffUtil.getStaffid(),authorityType);
 				XWPFDocument document = new XWPFDocument();
//				 // 2. 添加标题
 		        XWPFParagraph titlePara = document.createParagraph();
 		        titlePara.setAlignment(ParagraphAlignment.CENTER);
 		        XWPFRun titleRun = titlePara.createRun();
 		        Calendar calendar = Calendar.getInstance();
 		        int year = calendar.get(Calendar.YEAR);
  		        titleRun.setText(year+"年度专项风险评估情况统计表 ");
 		        titleRun.setBold(true);
 		        titleRun.setFontSize(20);
 		        document.createParagraph();
 		        XWPFTable table = document.createTable();
//		     // 设置表格宽度（A4纸宽度）
 		        CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
 		        width.setType(STTblWidth.DXA);
 		        width.setW(BigInteger.valueOf(9000));
//		     // 4. 创建表头
 		        String[] headers = {"序号", "项目名称", "专项风险评估单位(部门)", "专项风险评估报告程序性合规审查单位(部门)", "项目决策主体"};
 		       XWPFTableRow headerRow = table.getRow(0); // 获取第一行
 		        for (int i = 0; i < headers.length; i++) {
 		            XWPFTableCell cell = headerRow.getCell(i);
 		            if (cell == null) {
 		                cell = headerRow.createCell(); // 如果单元格不存在，创建新单元格
 		            }
 		           WordExporter.setCellText(cell, headers[i], true);
 		        }
 		          int total = 0;
	        	 for (int i = 0; i < objList.size(); i++) {
	        		 TblRiskReview ob=objList.get(i);
 		             XWPFTableRow row = table.createRow();
	                 total += objList.size();

	                 WordExporter. setCellText(row.getCell(0), String.valueOf(i + 1), false);
	                 WordExporter.setCellText(row.getCell(1), ob.getMattername(), false);
	                 WordExporter.setCellText(row.getCell(2), ob.getStaffunitname(), false);
	                 WordExporter.setCellText(row.getCell(3), "审计法务部", false);
	                 WordExporter.setCellText(row.getCell(4), ob.getDecisionmaking(), false);
	        	}
	        	 // 6. 添加统计行
	             XWPFTableRow totalRow = table.createRow();
	             WordExporter.setCellText(totalRow.getCell(0), "合计", true);
	             WordExporter.setCellText(totalRow.getCell(1), "总数", true);
	             WordExporter.setCellText(totalRow.getCell(2), objList.size()+"", true);
                 System.out.println(objList.size()+"***");
	             // 7. 设置表格边框
	             WordExporter.setTableBorders(table);
	             // 8. 添加统计年限注释
	             XWPFParagraph notePara = document.createParagraph();
	             notePara.setAlignment(ParagraphAlignment.LEFT);
	             XWPFRun noteRun = notePara.createRun();
	             noteRun.setText("(说明:专业化公司、直属单位需汇总所属成员单位的专项风险评估项目数量和清单。)");
	             noteRun.setItalic(true);
	             noteRun.setFontSize(10);
	            response.reset();
	            response.setContentType("application/binary;charset=UTF-8");
	            response.setHeader("Content-disposition",
	            "attachment;filename=user_world_" + System.currentTimeMillis() + ".docx");
	            OutputStream os = response.getOutputStream();
	            document.write(os);
	            os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
