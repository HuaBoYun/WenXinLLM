package com.huabo.system.controller;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.TblOrganizationInfo;
import com.huabo.system.entity.TblPersonalTrain;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.service.TblJobService;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblStaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计人员管理控制器
 * <p>提供审计人员的新增、查询、修改、分页列表等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "审计人员管理接口Controller", description = "审计人员管理")
public class ZcSynchronizationController {
    private static final Log logger = LogFactory.getLog(ZcSynchronizationController.class);
	@Resource
	private TblStaffService tblStaffService;

	@Resource
	private TblJobService  tblJobService;

	@Resource
	private TblOrganizaService TblOrganizaService;

	//人力资源同步
    @RequestMapping(value = "/saveOrUpdateStaff", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计人员管理-新增/修改")
    public JsonBean saveOrUpdateStaff(HttpServletRequest request,
    		@Parameter(name="data",description="用户实体表",required=true)@RequestParam(value="data", required = true)String data,
    		@Parameter(name="attIds",description="附件主键数组 示例1,2,3,4",required=false) @RequestParam(value="attIds", required = false) String attIds,
    	    @Parameter(name="birth",description="出生年月 格式年-月-日",required=false)@RequestParam(value = "birth", required = false) String birth,
    	    @Parameter(name="workDate",description="参加工作时间 格式年-月-日",required=false)@RequestParam(value = "workDate", required = false) String workDate
    		) {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffService.mergeStaffManageInfo(data,attIds,birth,workDate);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }

    @RequestMapping(value = "/getStaffPageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计人员管理列表分页功能")
    public JsonBean getAuditPlanPageList(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="当前右上角切换登录的公司",required=true) @RequestParam("orgId")BigDecimal orgId,
    	@Parameter(name="pageNumber",description="分页当前页数",required=false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name="pageSize",description="每页记录数",required=false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name="realName",description="姓名",required=false) @RequestParam(value = "realName", required = false)String realName,
   		@Parameter(name="major",description="专业",required=false) @RequestParam(value = "major", required = false)String major,
   		@Parameter(name="education",description="学历",required=false) @RequestParam(value = "education", required = false)String education,
   		@Parameter(name="jobExperiences",description="经验",required=false) @RequestParam(value = "jobExperiences", required = false)String jobExperiences,@Parameter(name="jobName",description="岗位",required=false) @RequestParam(value = "jobName", required = false)String jobName
   		) {
    	JsonBean jsonBean = null;
    	try {
    		TblStaff staff=new TblStaff();
    		staff.setRealname(realName);
    		staff.setMajor(major);
    		staff.setEducation(education);
    		staff.setJobName(jobName);
    		staff.setJobexperiences(jobExperiences);
			jsonBean = tblStaffService.getStaffList(orgId,token,pageNumber,pageSize,staff);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
        return jsonBean;
    }

    @RequestMapping(value = "/getyrkStaffPageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计人员管理列表分页功能")
    public JsonBean getyrkStaffPageList(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="当前右上角切换登录的公司",required=true) @RequestParam("orgId")BigDecimal orgId,
    	@Parameter(name="pageNumber",description="分页当前页数",required=false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name="pageSize",description="每页记录数",required=false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name="realName",description="姓名",required=false) @RequestParam(value = "realName", required = false)String realName,
   		@Parameter(name="major",description="专业",required=false) @RequestParam(value = "major", required = false)String major,
   		@Parameter(name="education",description="学历",required=false) @RequestParam(value = "education", required = false)String education,
   		@Parameter(name="jobExperiences",description="经验",required=false) @RequestParam(value = "jobExperiences", required = false)String jobExperiences,@Parameter(name="jobName",description="岗位",required=false) @RequestParam(value = "jobName", required = false)String jobName
   		) {
    	JsonBean jsonBean = null;
    	try {
    		TblStaff staff=new TblStaff();
    		staff.setRealname(realName);
    		staff.setMajor(major);
    		staff.setEducation(education);
    		staff.setJobName(jobName);
    		staff.setJobexperiences(jobExperiences);
			jsonBean = tblStaffService.getStaffList(orgId,token,pageNumber,pageSize,staff);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
        return jsonBean;
    }

    @RequestMapping(value = "/updateStaff", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary="审计人员管理- 删除")
   	public JsonBean updateStaff(HttpServletRequest request,
   			@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="staffId",description="审计人员id",required=true)@RequestParam(value = "staffId", required = true) String staffId)throws Exception{
   		 JsonBean jsonBean = null;
   			try {
   				jsonBean = tblStaffService.updateStaff(token,staffId);
   			} catch (Exception e) {
   				ResponseFormat.retParam(1,1000,e.getMessage());
   			}
   			return jsonBean;
   	}
    @RequestMapping(value = "/getStaffInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计人员管理--查看详情及修改页面回显数据")
    public JsonBean getStaffInfo(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="staffId",description="审计人员id",required=true)@RequestParam(value = "staffId", required = true) String staffId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblStaffService.findSjStaffDetail(token,staffId);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
        return jsonBean;
    }


    @RequestMapping(value = "/mergePlanProjectManageInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="审计人员管理- 培训信息新增或修改")
	public JsonBean mergePlanProjectManageInfo(HttpServletRequest request,@Parameter(name="data",description="人员培训信息实体",required=true)@RequestParam(value = "data", required = true)String data,
			 @Parameter(name="staffId",description="审计人员主键",required=false) @RequestParam(value = "staffId", required = false) String staffId,
			 @Parameter(name="trainattIds",description="附件主键数组",required=false) @RequestParam(value="trainattIds", required = false) String trainattIds,
			  @Parameter(name="traindate",required=false)@RequestParam(value="traindate", required = false)String traindate){
		     JsonBean jsonBean = null;
			try {
				net.sf.json.JSONObject json=net.sf.json.JSONObject.fromObject(data);
				TblPersonalTrain train = (TblPersonalTrain) net.sf.json.JSONObject.toBean(json,TblPersonalTrain.class);
				train.setStaffid(staffId);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(traindate != null && !"".equals(traindate)) {
					train.setTraintime(sdf.parse(traindate));
				}
				jsonBean = this.tblStaffService.saveOrUpdateTrain(train, staffId,trainattIds);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	}

    @RequestMapping(value = "/removeTrain", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="审计人员管理- 培训信息删除")
	public JsonBean removePlanProjectInfo(HttpServletRequest request,
			 @RequestParam(value = "trainId", required = true)@Parameter(name="trainId",description="人员培训信息主键",required=true)String trainId,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = tblStaffService.deleteTrain(token,trainId);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	}

    @RequestMapping(value = "/getStaffAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="审计人员管理- 获取人员信息附件列表")
    public JsonBean getAuditPlanAttInfo(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="staffId",description="用户id ",required=true) @RequestParam(value = "staffId", required = false) String staffId) {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffService.getStaffAttInfo(token,staffId);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }

    /**
     * @description 根据attid删除附件
     * @author lyz
     * @date 2022/4/19 9:04
     */
    @RequestMapping(value = "/deleteStaffFileById", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计人员管理- 删除人员附件")
    public JsonBean deleteStaffFileById(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="attId",description="附件主键ID",required=true) @RequestParam("attId") String attId) throws Exception {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffService.deleStaffAttInfo(token,attId);

		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }


    /**
     * @description 根据attid删除附件
     * @author lyz
     * @date 2022/4/19 9:04
     */
    @RequestMapping(value = "/deleteFileById", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计人员管理- 删除培训资料附件")
    public JsonBean deleteFileById(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="attId",description="附件主键ID",required=true) @RequestParam("attId") String attId) throws Exception {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffService.deleTrainAttInfo(token,attId);

		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }


    @RequestMapping(value = "/findAllLeader", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="查询为公司领导的数据")
    public JsonBean findAllLeader(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="pageNumber",description="分页当前页数",required=false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
       		@Parameter(name="pageSize",description="每页记录数",required=false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
    		@Parameter(name="orgId",description="ID",required=false) @RequestParam(value = "orgId", required = false) String orgId) throws Exception {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffService.findAllLeader(token, orgId, pageNumber, pageSize);
		} catch (Exception e) {
            return ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }

    @RequestMapping(value = "/organInfoDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计机构管理-单位基本情况查看详情及修改页面回显数据")
    public JsonBean organInfoDetail(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="左侧树orgid",required=true)@RequestParam(value = "orgId", required = true) String orgId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = TblOrganizaService.findOrganInfoDetail(token,orgId);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
        return jsonBean;
    }



    @RequestMapping(value = "/getAuditorInformationList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内部审计人员配备基本情况")
    public JsonBean getAuditorInformationList(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="所属机构",required=true)@RequestParam(value = "orgId", required = true) String orgId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblStaffService.getAuditorInformationList(token,orgId);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
        return jsonBean;
    }


  //审计机构管理
    @RequestMapping(value = "/saveOrUpdateOrgan", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计机构管理-单位基本情况-新增/修改")
    public JsonBean saveOrUpdateOrgan(HttpServletRequest request,@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		TblOrganizationInfo info,@Parameter(name="fill",required=false)@RequestParam(value="fill", required = false)String fill) {
        JsonBean jsonBean = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(fill != null && !"".equals(fill)) {
				info.setFillingdate(sdf.parse(fill));
			}
		 	jsonBean = this.TblOrganizaService.updateOrganInfo(token,info);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
}
