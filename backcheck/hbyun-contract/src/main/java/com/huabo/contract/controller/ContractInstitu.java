package com.huabo.contract.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblNbsjInnerrule;
import com.huabo.contract.service.TblNbsjInnerRuleService;
import com.huabo.contract.util.FileUtil;
import com.huabo.contract.util.FreeMarkerUtil;
import com.huabo.contract.vo.Result;
import com.huabo.contract.vo.TblNbsjInnerRuleVo;

import io.swagger.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同制度
 */
@RestController
@Slf4j
@Tag(name="合同制度",description="合同制度")
@RequestMapping(value = "/institu")
public class ContractInstitu {
	
	@Resource
	public TblNbsjInnerRuleService tblNbsjInnerRuleService;
	
	@Resource
	public FreeMarkerConfig freeMarkerConfig;

	@Resource
	private UserProvider userProvider;
	
	/**
	 * 合同配置 - 合同制度
	 * @param request
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param tblNbsjInnerRuleVo
	 * @return
	 */
	@RequestMapping(value = "/getInnerRulePageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-列表页")
    @ApiOperationSupport(order = 71)
    public JsonBean getInnerRulePageList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
    		TblNbsjInnerRuleVo tblNbsjInnerRuleVo) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = tblNbsjInnerRuleService.selectInnerrulePageInfo(token,pageNumber,pageSize,tblNbsjInnerRuleVo);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return jsonBean;
    }

	/**
	 * 合同配置 - 合同制度 - 新建
	 * @param request
	 * @param token
	 * @param attIds
	 * @param tblNbsjInnerrule
	 * @param pulishdate
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/mergeInnerRule", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-新增、修改")
    @ApiOperationSupport(order = 72)
    public JsonBean mergeInnerRule(HttpServletRequest request
    		,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token
    		,@Parameter(name = "attIds", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value="attIds", required = false) String attIds
    		,@Parameter(name = "tblNbsjInnerrule", description = "管理制度", required = true)TblNbsjInnerrule tblNbsjInnerrule
       		,@Parameter(name = "pulishdate", description = "生效日期 格式年-月-日", required = false)@RequestParam(value = "pulishdate", required = false) String pulishdate)throws Exception{
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = tblNbsjInnerRuleService.mergeInnerruleInfo(tblNbsjInnerrule,token,attIds,pulishdate);
    	} catch (Exception e) {
    		ResponseFormat.retParam(1,1000,e.getMessage());
    	}
    	return jsonBean;
    }

	/**
	 * 合同配置 - 合同制度 - 点击发文文号查询详细
	 * @param request
	 * @param innerid
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/selectInnerRuleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-查询单个信息")
    @ApiOperationSupport(order = 73)
    public JsonBean selectInnerRuleInfo(HttpServletRequest request
    		,@Parameter(name = "innerid", description = "主键", required = true) @RequestHeader("innerid")String innerid)throws Exception{
    	JsonBean jsonBean = null;
    	try {
    		TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
    		
    		jsonBean = this.tblNbsjInnerRuleService.selectInfo(innerid);
    	} catch (Exception e) {
    		ResponseFormat.retParam(1,1000,e.getMessage());
    	}
    	return jsonBean;
    }

	/**
	 * 合同配置 - 合同制度 - 删除
	 * @param request
	 * @param innerid
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/deleteInnerRuleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-删除")
    @ApiOperationSupport(order = 74)
    public JsonBean deleteInnerRuleInfo(HttpServletRequest request
    		,@Parameter(name = "innerid", description = "主键", required = true) @RequestHeader("innerid")String innerid)throws Exception{
    	JsonBean jsonBean = null;
    	try {
    		TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
    		
    		jsonBean = this.tblNbsjInnerRuleService.deleteInfo(innerid);
    	} catch (Exception e) {
    		ResponseFormat.retParam(1,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    
    /**
     * 管理制度-附件删除
     */
    @GetMapping("/deleteInnerRuleAtt")
    @Operation(summary = "管理制度-附件删除")
    public Result deleteInnerRuleAtt(HttpServletRequest request, HttpServletResponse response,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjInnerRuleService.removeAttInfoByAttId(token, attId);
    }
    
	/**
	 * 管理制度-导出
	 * @param request
	 * @param response
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "/expInnerRuleFile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "管理制度-导出")
    @ApiOperationSupport(order = 74)
	public void expInnerRuleFile(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "id", description = "主键", required = true) @RequestHeader("id")String id)throws Exception{
		
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ;
        }
		Map<String, String> map = new HashMap<String,String>();
		TblNbsjInnerrule info = this.tblNbsjInnerRuleService.getInfo(id);
		
		map.put("repdesc", info.getBodyinfo());
		String fileName =info.getRulename()+".doc";
		
		String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
		Boolean flag =(Boolean)FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
		if(!flag){//如何静态文件不存在，重新生成
			FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
		}
		String fileName1 = FREEMARKER_PATH +"/"+ fileName;
		FileUtil.downLoad(fileName1, response, false, fileName);
		FileUtil.deleteFile(fileName);
	}
	
	//自动编号-合同制度
    @RequestMapping(value = "/getAutoCodeByHtzd", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-合同制度")
	public JsonBean getAutoCodeByGlzd(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjInnerRuleService.getAutoCodeByHtzd(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
	
}
