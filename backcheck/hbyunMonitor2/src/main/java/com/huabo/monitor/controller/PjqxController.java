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
import com.huabo.monitor.entity.TblEvaluateDefects;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblEvaluateDefectsService;
import com.huabo.monitor.util.DateUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@Tag(name="评价管理-评价缺陷",description="评价管理-评价缺陷")
@RequestMapping(value = "/pjqx")
public class PjqxController {

    @Autowired
    TblEvaluateDefectsService tblEvaluateDefectsService;

    @Autowired
    ITblStaffService iTblStaffService;
    
    @Resource
    private UserProvider userProvider;
    

	@Value("${application.administrators:}")
	private String administrators;
 

    @OperationLog(
            success = "评价缺陷-主页查询成功",
            busType = "评价管理",
            fail = "评价缺陷-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "评价缺陷"
    )
    @GetMapping(value = "/getHomepage_List")
    @Operation(summary = "评价缺陷-主页查询")
    public JsonBean getHomepage_List(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
    		@Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @Parameter(name = "defectscode", description = "缺陷编号") @RequestParam(value = "defectscode", required = false) String defectscode,
                                 @Parameter(name = "defectsname", description = "缺陷名称") @RequestParam(value = "defectsname", required = false) String defectsname,
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
		TblEvaluateDefects def=new TblEvaluateDefects();
		def.setDefectsname(defectsname);
		def.setDefectscode(defectscode);
		def.setUnit(staff.getCurrentOrg().getOrgid());
		def.setCreatestaffid(staff.getStaffid());
        PageInfo<TblEvaluateDefects> pageInfo=tblEvaluateDefectsService.getHomepage_List(pageNumber,pageSize, authorityType,def);
        Map<String, Object> mv = new HashMap<>();
        mv.put("defectsname", defectsname);
        mv.put("defectscode", defectscode);
        mv.put("pageBean", pageInfo);
        return new JsonBean(200, "success", mv);
    }
 
 
    @OperationLog(
            success = "评价缺陷-新建页面-保存成功",
            busType = "评价管理",
            fail = "评价缺陷-新建页面-保存失败",
            operationType = OperationType.ADD,
            subType = "评价缺陷"
    )
    @PostMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "评价缺陷-新建/修改功能")
    public JsonBean addPjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblEvaluateDefects entity,
            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids)throws Exception  {
    	 Map<String, Object> map = new HashMap<>();
    	try {
    		TblStaffUtil user = userProvider.get();
            if (user == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
			map=tblEvaluateDefectsService.saveOrUpdate(token,entity,attids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ResponseFormat.retParam(200, "success", map);
    }
  
 
    @OperationLog(
            success = "评价缺陷-删除成功",
            busType = "评价管理",
            fail = "评价缺陷-删除失败",
            operationType = OperationType.DELETE,
            subType = "评价缺陷"
    )
    @PostMapping(value = "/deleteQx")
    @Operation(summary = "评价缺陷-删除")
    public @ResponseBody String deleteQx(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="id",description="id") @RequestParam(value = "id")BigDecimal id) throws Exception{
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return JsonBean.error("用户已失效");
        }
        String result="";
        try {
        	result= this.tblEvaluateDefectsService.deleteById(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return result;

    }
 
    @OperationLog(
            success = "评价缺陷-详情查询成功",
            busType = "评价管理",
            fail = "评价缺陷-详情查询失败",
            operationType = OperationType.SELECT,
            subType = "评价缺陷"
    )
    @GetMapping(value = "/getDetails")
    @Operation(summary = "评价缺陷-详情")
    public  JsonBean plan_detail(
            @Parameter(name = "id", description = "id") @RequestParam(value = "id")  BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (null != id) {
				TblEvaluateDefects entity = tblEvaluateDefectsService.getDetatilsById(id);
				 List<TblAttachment> list=tblEvaluateDefectsService.getAttList(id);
				//
				// //查询负责人
				// final TblStaff fuzeren =
				// this.iTblStaffService.getById(assess.getLeaderid());
				// List<TblAttachment>
				// list=tblAssessService.getAssessAttListByAssid(selectedPlans);
				// map.put("fuzeren", fuzeren);
				 map.put("atts",list);
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
            success = "评价缺陷导出成功",
            busType = "评价管理",
            fail = "评价缺陷导出失败",
            operationType = OperationType.EXPORT,
            subType = "评价缺陷"
    )
	@RequestMapping(value = "/exportPjqx",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
    @Operation(summary = "评价缺陷导出")
	public void  exportPjqx(HttpServletRequest request, HttpServletResponse response,
			 @Parameter(name = "defectscode", description = "缺陷编号") @RequestParam(value = "defectscode", required = false) String defectscode,
             @Parameter(name = "defectsname", description = "缺陷名称") @RequestParam(value = "defectsname", required = false) String defectsname,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
										 ) {
		try {
			log.info("评价管理-评价缺陷-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ;
	        }
				Integer authorityType;
				if (JudgeRoleRight.judgeRoleRight(administrators, staff.getRoleNames())) {
					authorityType = 1;
				} else {
					authorityType = 0;
				}
				TblEvaluateDefects def=new TblEvaluateDefects();
				def.setDefectsname(defectsname);
				def.setDefectscode(defectscode);
				def.setUnit(staff.getCurrentOrg().getOrgid());
				def.setCreatestaffid(staff.getStaffid());
		       List<TblEvaluateDefects> pageInfo=tblEvaluateDefectsService.exportPjqx(authorityType,def);
		       Map<Integer, String>  qxlb=new HashMap<Integer, String>();
		       Map<Integer, String>  qxdj=new HashMap<Integer, String>();
		       Map<Integer, String>  qxzl=new HashMap<Integer, String>();
               qxlb.put(1, "内控体系设计缺陷");
               qxlb.put(2, "内控制度缺陷");
               qxlb.put(3, "内控执行缺陷");
               qxlb.put(4, "内控监督缺陷");
               qxlb.put(5, "重大风险防控缺陷");
               qxlb.put(6, "其他内控缺陷");
		       qxdj.put(1, "一般缺陷");
		       qxdj.put(2, "终于缺陷");
		       qxdj.put(3, "重大缺陷");
		       qxzl.put(1,"财报缺陷");
		       qxzl.put(2,"非财报缺陷");
			String[] cNames = {"缺陷编号","缺陷名称", "缺陷描述及依据","发生时间","涉及金额(万元)", "原因分析","缺陷类别", "缺陷等级", "缺陷种类", "是否涉诉","是否境外"};
			List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			for (TblEvaluateDefects task : pageInfo) {
				objs = new Object[11];
				objs[0] = task.getDefectscode();
				objs[1] = task.getDefectsname();
				objs[2] = task.getDescription();
				objs[3] = task.getOccurrencedate()==null?"":DateUtils.parseDate(task.getOccurrencedate(), "yyyy-MM-dd");;
				objs[4] = task.getAmount();
				objs[5] = task.getCauseanalysis();
				objs[6] = task.getDefectcategory()!=null?qxzl.get(task.getDefectcategory()):"";
				objs[7] = task.getDefectlevel()!=null?qxdj.get(task.getDefectlevel()):"";
				objs[8] = task.getDefecttype()!=null?qxzl.get(task.getDefecttype()):"";
				objs[9] =task.getLitigation()!=null?(task.getLitigation()==new BigDecimal("0")?"否":"是"):"";
				objs[10] = task.getOverseas()!=null?(task.getOverseas()==new BigDecimal("0")?"否":"是"):"";;
				contractlist.add(objs);
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("评价缺陷".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
}
