package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjMb;
import com.huabo.audit.service.TblNbsjMbService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 审计模板控制器
 * <p>提供审计模板的分页查询、新增、修改、删除等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="审计模板",description="审计模板")
@RequestMapping(value = "/audit/mb")
public class TblNbsjMbController {

    @Autowired
    TblNbsjMbService tblNbsjMbService;


	@OperationLog(
			success = "审计模板库列表",
			busType = "智能审计",
			fail = "审计模板库列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计文书模板库列表信息查询"
	)
    @GetMapping("/getmbList")
    @Operation(summary = "审计模板库-列表")
    public JsonBean getmbList(HttpServletRequest request,
    	@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name = "code", description = "模板编号", required = false)@RequestParam(value = "code", required = false) String code,
   		@Parameter(name = "name", description = "模板名称", required = false)@RequestParam(value = "name", required = false) String name,
   		@Parameter(name = "auditype", description = "审计类型", required = false)@RequestParam(value = "auditype", required = false) String auditype) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjMbService.findAll(code, name, pageNumber, pageSize, token, auditype);
		} catch (Exception e) {
			e.printStackTrace();
			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }

	@OperationLog(
			success = "审计模板库列表新增或修改",
			busType = "智能审计",
			fail = "审计模板库列表新增或修改",
			operationType = OperationType.ADD,
			subType = "基础配置——审计文书模板库新增修改列表信息"
	)
    @RequestMapping(value = "/mergembInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "审计模板库-新增或修改")
   	public JsonBean mergembInfo(HttpServletRequest request,
   			 @Parameter(name = "mbid", description = "主键 ，如果主键为空则新增信息", required = false) @RequestParam(value="mbid", required = false) BigDecimal mbid,
   			 @Parameter(name = "mb", description = "审计模板实体", required = true)TblNbsjMb mb,
   			 @Parameter(name = "attids", description = "上传附件的ID", required = false) @RequestParam(value="attids", required = false) String attids,
   			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
   		 JsonBean jsonBean = null;
   			try {
   				jsonBean = tblNbsjMbService.saveOrUpdate(mb, token, mbid, attids);
   			} catch (Exception e) {
   				e.printStackTrace();
   				jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
   			}
   			return jsonBean;
   	}

	@OperationLog(
			success = "审计模板库列表记录详细信息",
			busType = "智能审计",
			fail = "审计模板库列表记录详细信息",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计文书模板库列表查看指定记录详细信息"
	)
    @GetMapping("/getmbDetail")
    @Operation(summary = "审计模板库-查看详情信息")
    public JsonBean getmbDetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "mbid", description = "模板主键id", required = true)@RequestParam(value = "mbid", required = true) BigDecimal mbid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean =tblNbsjMbService.findByMbid(mbid, token);
		} catch (Exception e) {
			e.printStackTrace();
			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }



	@OperationLog(
			success = "审计模板库列表记录删除记录",
			busType = "智能审计",
			fail = "审计模板库列表记录删除记录",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计文书模板库列表——删除指定记录"
	)
    @RequestMapping(value = "/delMbInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "审计模板库-删除信息")
       public JsonBean delMbInfo(HttpServletRequest request,
       		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
       		@Parameter(name = "mbid", description = "模板主键id", required = true)@RequestParam(value = "mbid", required = true) BigDecimal mbid
       		) {
       	JsonBean jsonBean = null;
   		try {
   			jsonBean =tblNbsjMbService.delete(mbid, token);
   		} catch (Exception e) {
   			e.printStackTrace();
   			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
   		}
   		return jsonBean;
       }

	@OperationLog(
			success = "审计模板库附件列表",
			busType = "智能审计",
			fail = "审计模板库附件列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计文书模板库列表指定记录——查询附件信息"
	)
    @RequestMapping(value = "/getMbnAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "审计模板库获取所属的附件")
       public JsonBean getMbnAttInfo(HttpServletRequest request,
       		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
       		@Parameter(name = "mbid", description = "模板主键id", required = true)@RequestParam(value = "mbid", required = true) BigDecimal mbid) {
       	JsonBean jsonBean = null;
   		try {
   			jsonBean = tblNbsjMbService.getAttListByMbId(token, mbid);
   		} catch (Exception e) {
   			e.printStackTrace();
   			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
   		}
   		return jsonBean;
       }


	@OperationLog(
			success = "审计模板库附件删除",
			busType = "智能审计",
			fail = "审计模板库附件删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计文书模板库列表——删除指定记录指定的附件"
	)
       @RequestMapping(value = "/delMbAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "审计模板库删除选择的附件")
       public JsonBean delMbAttInfo(HttpServletRequest request,
       		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
       		@Parameter(name = "attid", description = "附件ID ", required = true)@RequestParam(value = "attid", required = true)  BigDecimal attid) {
       	JsonBean jsonBean = null;
   		try {
   			jsonBean = tblNbsjMbService.delAttListByattId(token, attid);
   		} catch (Exception e) {
   			e.printStackTrace();
   			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
   		}
   		return jsonBean;
       }

	@OperationLog(
			success = "根据资料id获取关联模板内容",
			busType = "智能审计",
			fail = "根据资料id获取关联模板内容",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计文书模板库根据资料id获取关联模板内容"
	)
       @RequestMapping(value = "/getMbBydatapreid", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
      	@Operation(summary = "根据资料id获取关联模板内容")
          public JsonBean getMbBydatapreid(HttpServletRequest request,
          		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
          		@Parameter(name = "dataperid", description = "资料主键id", required = true)@RequestParam(value = "dataperid", required = true) BigDecimal dataperid) {
          	JsonBean jsonBean = null;
      		try {
      			jsonBean = tblNbsjMbService.findAllByDatapreID(token, dataperid);
      		} catch (Exception e) {
      			e.printStackTrace();
      			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
      		}
      		return jsonBean;
          }

	@OperationLog(
			success = "根据id删除关联模板内容",
			busType = "智能审计",
			fail = "根据id删除关联模板内容",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计文书模板库根据id删除关联模板内容"
	)
       @RequestMapping(value = "/delBymbid", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    	@Operation(summary = "根据id删除关联模板内容")
        public JsonBean delBymbid(HttpServletRequest request,
        		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
        		@Parameter(name = "mbid", description = "模板主键id", required = true)@RequestParam(value = "mbid", required = true) BigDecimal mbid) {
        	JsonBean jsonBean = null;
    		try {
    			jsonBean = tblNbsjMbService.delAttListBymbId(token, mbid);
    		} catch (Exception e) {
    			e.printStackTrace();
    			jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
    		}
    		return jsonBean;
        }
}
