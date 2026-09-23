package com.huabo.contract.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.service.FileManager;
import com.huabo.contract.service.TblCyhwUnitService;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.util.DateUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同台账控制器
 * <p>提供合同台账的列表查询、详情查看、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="合同台账Controller",description="合同台账Controller")
public class ContractAccountController {

    @Autowired
    private TblCyhwUnitService tblCyhwUnitService;
    
    @Resource
	private FileManager fileManager;
    
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    
    @Resource
    private UserProvider userProvider;
    
    
    /**
     * 合同台账列表
     */
    @RequestMapping(value = "/contract/contractLedgerList", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
    @Operation(summary = "合同台账列表")
    public String contract_contractLedgerList(HttpServletRequest request,
                                              @Parameter(description = "startdate", required = false) String startdate,
                                              @Parameter(description = "enddate", required = false) String enddate,
        @RequestParam(value = "pageNumber",required=false,defaultValue="1")@Parameter(name="pageNumber",description="pageNumber",required=false) Integer pageNumber,
        @RequestParam(value = "pageSize",required=false,defaultValue="20")@Parameter(name="pageSize",description="pageSize",required=false) Integer pageSize,
											  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "staffId", description = "用户Id主键", required = false) String staffId,
                                              TblCyhwUnit unit) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblCyhwUnitService.findLedgerListPageInfo(token, staffId, startdate, enddate, pageNumber, pageSize, unit);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 合同台账导出
     *
     * @param request
     * @return
     */
	@Operation(summary="合同台账导出")
	@RequestMapping(value = "/contract/exploredInfo",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
	public void  contract_contractLedgerList(HttpServletRequest request, HttpServletResponse response,
											 TblCyhwUnit unit,
											 @Parameter(description = "startdate", required = false) String startdate,
                                             @Parameter(description = "enddate", required = false) String enddate,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											 @Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId) {
		try {
			response.setContentType("application/binary;charset=UTF-8");
			TblStaffUtil staff = userProvider.get();
			
			if(staff == null) {
				return;
			}
			
			
			BigDecimal orgid = null;
			if(unit.getOrgid() != null) {
				orgid = unit.getOrgid();
			}else {
				orgid = staff.getCurrentOrg().getOrgid();
			}
			
			
			unit.setOrgid(orgid);
			Integer resultCount = tblCyhwUnitService.findCountByUserSjb(staff.getStaffid());
            if (resultCount == 0 && !JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames()) && !JudgeRoleRight.judgeRoleRight("法务人员", staff.getRoleNames())) {
                unit.setCreateuser(staff.getStaffid());
                unit.setContractstaff(staff.getStaffid());
            }

            if (JudgeRoleRight.judgeRoleRight("法务人员", staff.getRoleNames())) {
                unit.setFatherOrgId(staff.getCurrentOrg().getOrgid());
            }
			Date endQuerydate = null;
			if(enddate == null) {
				//endQuerydate = new Date();
			}else {
				endQuerydate = DateUtil.formatDate(enddate, "yyyy-MM-dd");
			}
			Date startQuerydate = null;
			if(startdate == null) {
				/*Calendar cal = Calendar.getInstance();
				startQuerydate = DateUtil.formatDate(cal.get(Calendar.YEAR)+"-01-01","yyyy-MM-dd");*/
			}else {
				startQuerydate = DateUtil.formatDate(startdate,"yyyy-MM-dd");
			}
			unit.setEnddate(endQuerydate);
			unit.setStartdate(startQuerydate);
			String fatherOrgIds = null;
            if(unit.getFatherOrgId() != null) {
            	fatherOrgIds = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(staff.getCurrentOrg().getOrgid().toString());
            }
			List<TblCyhwUnit> unitList = this.tblCyhwUnitService.findLedgerListForExport(unit,fatherOrgIds);
			Map<String,String> budgetMap = this.tblCyhwUnitService.findOppsiteNamesByUnitTaiZhangExport(unit,fatherOrgIds);
			String[] cNames = { "合同编号", "合同名称","相对方名称","合同类型", "项目名称","收付款方向", "合同金额", "创建日期", "开始日期","结束日期","我方签署主体","承办部门","执行人","附件数","用印日期","状态","是否违约"};
			List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			
			for (TblCyhwUnit ui : unitList) {
				objs = new Object[17];
				objs[0] = ui.getContractno();
				objs[1] = ui.getContractname();
				if(budgetMap.containsKey(ui.getContractid().toString())){
					objs[2] = budgetMap.get(ui.getContractid().toString());
				}else {
					objs[2] = "";
				}
				objs[3] = ui.getContracttype();
				objs[4] = ui.getTopicname();
				objs[5] = ui.getDctype();
				objs[6] = ui.getContractmoney();
				objs[7] = ui.getCreatetime()==null?"":DateUtil.parseDate(ui.getCreatetime(), "yyyy-MM-dd");
				objs[8] = ui.getStartdate()==null?"":DateUtil.parseDate(ui.getStartdate(), "yyyy-MM-dd");
				objs[9] = ui.getEnddate()==null?"":DateUtil.parseDate(ui.getEnddate(), "yyyy-MM-dd");
				objs[10] = ui.getChoicejbunitid();
				objs[11] = ui.getChoicecontractDeptId();
				objs[12] = ui.getTopic();
				objs[13] = ui.getNodeCount();
				objs[14] = ui.getYongyintime()==null?"":DateUtil.parseDate(ui.getYongyintime(), "yyyy-MM-dd");
				objs[15] = this.getContractStatusText(ui.getContractstatus());
				objs[16] = ui.getIsWy();
				contractlist.add(objs);
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("合同台账".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 合同台账列表 - 查询下属公司
     */
    @RequestMapping(value = "/contract/contractLedgerListAllOrg", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
    @Operation(summary = "合同台账列表")
    public String contract_contractLedgerListAllOrg(HttpServletRequest request,
                                              @Parameter(description = "startdate", required = false) String startdate,
                                              @Parameter(description = "enddate", required = false) String enddate,
        @RequestParam(value = "pageNumber",required=false,defaultValue="1")@Parameter(name="pageNumber",description="pageNumber",required=false) Integer pageNumber,
        @RequestParam(value = "pageSize",required=false,defaultValue="20")@Parameter(name="pageSize",description="pageSize",required=false) Integer pageSize,
											  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "staffId", description = "用户Id主键", required = false) String staffId,
                                              TblCyhwUnit unit) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblCyhwUnitService.findLedgerListAllOrgPageInfo(token, staffId, startdate, enddate, pageNumber, pageSize, unit);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 合同台账导出
     *
     * @param request
     * @return
     */
	@Operation(summary="合同台账导出")
	@RequestMapping(value = "/contract/exploredOrgInfo",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
	public void  contract_exploredOrgInfo(HttpServletRequest request, HttpServletResponse response,
											 TblCyhwUnit unit,
											 @Parameter(description = "startdate", required = false) String startdate,
                                             @Parameter(description = "enddate", required = false) String enddate,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											 @Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId) {
		try {
			response.setContentType("application/binary;charset=UTF-8");
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
	            return ;
	        }
			BigDecimal orgid = null;
			if(unit.getOrgid() != null) {
				orgid = unit.getOrgid();
			}else {
				orgid = staff.getCurrentOrg().getOrgid();
			}
			unit.setOrgid(orgid);
			Date endQueryDate = null;
			if(enddate == null) {
				//endQueryDate = new Date();
			}else {
				endQueryDate = DateUtils.parse(enddate, "yyyy-MM-dd");
			}
			Date startQueryDate = null;
			if(startdate == null) {
				/*Calendar cal = Calendar.getInstance();
				startQueryDate = DateUtils.parse(cal.get(Calendar.YEAR)+"-01-01","yyyy-MM-dd");*/
			}else {
				startQueryDate = DateUtils.parse(startdate,"yyyy-MM-dd");
			}
			unit.setEnddate(endQueryDate);
			unit.setStartdate(startQueryDate);
			String allCompanyIds = null;
	        if(StringUtils.isBlank(unit.getOrgname())) {
	        	allCompanyIds = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(unit.getOrgid().toString());
	        }
			List<TblCyhwUnit> unitList = this.tblCyhwUnitService.findLedgerOrgListForExport(unit,allCompanyIds);
			Map<String,String> budgetMap = this.tblCyhwUnitService.findOppsiteNamesByUnit(unit,allCompanyIds);
			
			
			String[] cNames = { "合同编号", "合同名称","相对方名称","合同类型", "项目名称","收付款方向", "合同金额", "创建日期", "开始日期","结束日期","我方签署主体","承办部门","执行人","附件数","用印日期","状态","所属公司","是否违约"};
			List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			
			for (TblCyhwUnit ui : unitList) {
				objs = new Object[18];
				objs[0] = ui.getContractno();
				objs[1] = ui.getContractname();
				if(budgetMap.containsKey(ui.getContractid().toString())){
					objs[2] = budgetMap.get(ui.getContractid().toString());
				}else {
					objs[2] = "";
				}
				objs[3] = ui.getContracttype();
				objs[4] = ui.getTopicname();
				objs[5] = ui.getDctype();
				objs[6] = ui.getContractmoney();
				objs[7] = ui.getCreatetime()==null?"":DateUtils.parseDate(ui.getCreatetime(), "yyyy-MM-dd");
				objs[8] = ui.getStartdate()==null?"":DateUtils.parseDate(ui.getStartdate(), "yyyy-MM-dd");
				objs[9] = ui.getEnddate()==null?"":DateUtils.parseDate(ui.getEnddate(), "yyyy-MM-dd");
				objs[10] = ui.getChoicejbunitid();
				objs[11] = ui.getChoicecontractDeptId();
				objs[12] = ui.getTopic();
				objs[13] = ui.getNodeCount();
				objs[14] = ui.getYongyintime()==null?"":DateUtils.parseDate(ui.getYongyintime(), "yyyy-MM-dd");
				objs[15] = this.getContractStatusText(ui.getContractstatus());
				objs[16] = ui.getOrgname();
				objs[17] = ui.getIsWy();
				contractlist.add(objs);
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("合同台账".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
	public String getContractStatusText(Integer cstatus) {
        String result = null;

        switch (cstatus) {
            case 1:
                result = "审批中";
                break;
            case 2:
                result = "需调整";
                break;
            case 3:
                result = "已通过";
                break;
            case 4:
                result = "已终止";
                break;
            case 5:
                result = "已跟踪";
                break;
            case 6:
                result = "已完成";
                break;
            case 7:
                result = "执行中";
                break;
            case 8:
                result = "已归档";
                break;
            case 9:
                result = "已暂停";
                break;
            case 10:
                result = "已变更";
                break;
            case 11:
                result = "已终止";
                break;
            default:
                result = "未审批";
                break;
        }
        return result;
    }
	
	@Operation(summary = "上传图片接口")
	@RequestMapping(value = "/upload",method = {RequestMethod.POST} , produces = "application/html; charset=utf-8")
	public String upload(MultipartFile file,
						 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ) {
		String result = null;
		try {
			Map<String,Object>  resultMap = fileManager.upload(file,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
