package com.huabo.monitor.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblEvaluationInterview;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblEvaluationInterviewService;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@Tag(name="评价管理-评价访谈",description="评价管理-评价访谈")
@RequestMapping(value = "/pjft")
public class PjftController {
	  @Autowired
	    TblEvaluationInterviewService tblEvaluationInterviewService;

	    @Autowired
	    ITblStaffService iTblStaffService;
	    
	    @Resource
	    private UserProvider userProvider;
	    

		@Value("${application.administrators:}")
		private String administrators;
	 

	    @OperationLog(
	            success = "评价访谈-主页查询成功",
	            busType = "评价管理",
	            fail = "评价访谈-主页查询失败",
	            operationType = OperationType.SELECT,
	            subType = "评价访谈"
	    )
	    @GetMapping(value = "/getHomepage_List")
	    @Operation(summary = "评评价访谈-主页查询")
	    public JsonBean getHomepage_List(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
	    		@Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
	                                 @Parameter(name = "module", description = "业务模块") @RequestParam(value = "module", required = false) String module,
	                                 @Parameter(name = "content", description = "访谈内容") @RequestParam(value = "content", required = false) String content,
	                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	    ) throws Exception {
	    	TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
			Integer authorityType;
			if (JudgeRoleRight.judgeRoleRight(administrators, staff.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
			TblEvaluationInterview def=new TblEvaluationInterview();
			def.setModule(module);
			def.setContent(content);
			def.setUnit(staff.getCurrentOrg().getOrgid());
			def.setCreatestaffid(staff.getStaffid());
	        PageInfo<TblEvaluationInterview> pageInfo=tblEvaluationInterviewService.getHomepage_List(pageNumber,pageSize, authorityType,def,staff);
	        Map<String, Object> mv = new HashMap<>();
	        mv.put("module", module);
	        mv.put("content", content);
	        mv.put("pageBean", pageInfo);
	        return new JsonBean(200, "success", mv);
	    }
	 
	 
	    @OperationLog(
	            success = "评价访谈-新建页面-保存成功",
	            busType = "评价管理",
	            fail = "评价访谈-新建页面-保存失败",
	            operationType = OperationType.ADD,
	            subType = "评价访谈"
	    )
	    @PostMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8")
	    @Operation(summary = "评价访谈-新建/修改功能")
	    public JsonBean addPjgl(
	            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	            @RequestBody TblEvaluationInterview entity,
	            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids
	    )throws Exception  {
	    	 Map<String, Object> map = new HashMap<>();
	    	try {
	    		System.out.println("附件id-------------------------："+attids+"-----------:"+entity.getAttids());
	    		if(entity.getAttids()!=null && entity.getAttids().length()>0) {
	    			attids=entity.getAttids();
	    		}
				map=tblEvaluationInterviewService.saveOrUpdate(token,entity,attids);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return ResponseFormat.retParam(200, "success", map);
	    }
	    
	    
	    @Operation(summary = "评价访谈-附件删除")
	    @PostMapping(value = "/del_fj")
	    public JsonBean updatesp_del_fj(
	            @Parameter(name = "attid", description = "单个附件id") @RequestParam(value = "attid") BigDecimal attid,
	            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	    ) throws Exception {
	    	TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
	        this.tblEvaluationInterviewService.delAssessAtt(attid);
	        return new JsonBean(200, "success", "删除附件成功");
	    }
	    
	    
	  
	 
	    @OperationLog(
	            success = "评价访谈-删除成功",
	            busType = "评价管理",
	            fail = "评价访谈-删除失败",
	            operationType = OperationType.DELETE,
	            subType = "评价访谈"
	    )
	    @PostMapping(value = "/deleteQx")
	    @Operation(summary = "评价访谈-删除")
	    public @ResponseBody String deleteQx(
	            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	            @Parameter(name="id",description="id") @RequestParam(value = "id")BigDecimal id) throws Exception{
	    	TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
	        String result="";
	        try {
	        	result= this.tblEvaluationInterviewService.deleteById(id);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return result;

	    }
	 
	    @OperationLog(
	            success = "评价访谈-详情查询成功",
	            busType = "评价管理",
	            fail = "评价访谈-详情查询失败",
	            operationType = OperationType.SELECT,
	            subType = "评价访谈"
	    )
	    @GetMapping(value = "/getDetails")
	    @Operation(summary = "评价访谈-详情")
	    public  JsonBean plan_detail(
	            @Parameter(name = "id", description = "id") @RequestParam(value = "id")  BigDecimal id,
	            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
	    	TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
	        Map<String, Object> map = new HashMap<String, Object>();
			try {
				if (null != id) {
					TblEvaluationInterview entity = tblEvaluationInterviewService.getDetatilsById(id);
					  FiexibleNameAssignment ment=new FiexibleNameAssignment();
			        	//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
					 List<TblAttachment> list=tblEvaluationInterviewService.getAttList(id);
					 map.put("atts",list);
					//
					// //查询负责人
					// final TblStaff fuzeren =
					// this.iTblStaffService.getById(assess.getLeaderid());
					// List<TblAttachment>
					// list=tblAssessService.getAssessAttListByAssid(selectedPlans);
					// map.put("fuzeren", fuzeren);
					  map.put("entity",entity);
					  map.put("attInfo","");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

	        return new JsonBean(200,"success", map);
	    }
	    
	    
	    @OperationLog(
	            success = "评价访谈导出成功",
	            busType = "评价管理",
	            fail = "评价访谈导出失败",
	            operationType = OperationType.EXPORT,
	            subType = "评价访谈"
	    )
	    @RequestMapping(value = "/exportPjft",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
		@ResponseBody
	    @Operation(summary = "评价访谈导出")
		public void  exportPjqx(HttpServletRequest request, HttpServletResponse response,
				 @Parameter(name = "module", description = "业务模块") @RequestParam(value = "module", required = false) String module,
                 @Parameter(name = "content", description = "访谈内容") @RequestParam(value = "content", required = false) String content,
												 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
											 ) {
			try {
				log.info("评价管理-评价访谈-导出Excel");
				response.setContentType("application/binary;charset=UTF-8");
				TblStaffUtil staff = userProvider.get();
		        if (staff == null) {
		            return;
		        }
					Integer authorityType;
					if (JudgeRoleRight.judgeRoleRight(administrators, staff.getRoleNames())) {
						authorityType = 1;
					} else {
						authorityType = 0;
					}
					TblEvaluationInterview def=new TblEvaluationInterview();
					def.setModule(module);
					def.setContent(content);
					def.setUnit(staff.getCurrentOrg().getOrgid());
					def.setCreatestaffid(staff.getStaffid());
			        List<TblEvaluationInterview> pageInfo=tblEvaluationInterviewService.exportPjft(authorityType,def);
				String[] cNames = {"时间","被访谈人","联系方式", "隶属部门","职务","业务模块", "提问人","记录人", "对应控制标准表", "参加人", "一般访谈内容","总结"};
				List<Object[]> contractlist = new ArrayList<Object[]>(0);
				Object[] objs = null;
				for (TblEvaluationInterview task : pageInfo) {
					objs = new Object[12];
					objs[0] = task.getTimes();
					objs[1] = task.getInterviewee();
					objs[2] = task.getContact();
					objs[3] = task.getDeptname();
					objs[4] = task.getPost();
					objs[5] = task.getModule();
					objs[6] = task.getQuestioner();
					objs[7] = task.getStaffname();
					objs[8] = task.getStandardtable();
					objs[9] =task.getParticipants();
					objs[10] = task.getContent();
					objs[11] = task.getSummary();
					contractlist.add(objs);
				}
				response.setHeader("Content-Disposition", "attachment;filename=" + new String("评价访谈".getBytes(),"UTF-8") + ".xlsx");
				ServletOutputStream outputStream = response.getOutputStream();
				ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	    
}
