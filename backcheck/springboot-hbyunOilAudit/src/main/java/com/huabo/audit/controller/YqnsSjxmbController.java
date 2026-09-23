package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjxmb;
import com.huabo.audit.service.TblYqnsSjxmbService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 项目管理_审计项目表
 */
@Tag(name="项目管理_审计项目表",description="项目管理_审计项目表")
@RestController
@RequestMapping(value = "/xmglsjxmb")
public class YqnsSjxmbController {

    @Resource
    TblYqnsSjxmbService service;

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsSjxmb vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "列表查询接口")
    @GetMapping("/list")
    public JsonBean list(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsSjxmb vo
    ) {
        try {
            return this.service.list(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口", description = "sjxmbid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjxmb vo
    ) {
        try {
            return this.service.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口", description = "ids=?,?")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjxmb vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导出数据接口", description = "ids=?,?")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request,
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               TblYqnsSjxmb vo
    ) {
        try {
            return this.service.exportData(response, token, vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导入数据接口", description = "file=?.xlsx")
    @PostMapping("/importData")
    public JsonBean importData(HttpServletRequest request,
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "file", description = "文件", required = true) @RequestParam("file") MultipartFile file
    ) {
        try {
            return this.service.importData(file, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "下发接口", description = "主键集合ids=1,2   人员集合ryIdsList=1,2")
    @RequestMapping(value = "/xf", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean xf(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsSjxmb vo
    ) {
        try {
            return this.service.xf(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
	@GetMapping("/xmzl/getLiftMenu")
	@Operation(summary = "项目查看-左侧菜单树") 
    public JsonBean getLiftMenu(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectid", description = "项目id", required = false) @RequestParam(value = "projectid", required = false) String projectid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.service.getLiftMenu(token,projectid);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return jsonBean;
    }
	
	@GetMapping("/projectArchiveList")
	@Operation(summary = "项目归档列表信息")
	public JsonBean projectArchiveList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = service.projectArchiveList(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@GetMapping("/da/daList")
    @Operation(summary = "档案列表")
    public JsonBean daList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "prjoectName", description = "项目名称", required = false)@RequestParam(value = "prjoectName", required = false, defaultValue = "") String prjoectName,
                            @Parameter(name = "projectCode", description = "项目编号", required = false)@RequestParam(value = "projectCode", required = false, defaultValue = "") String projectCode
    ){
        JsonBean jsonBean = null;
        try{

            jsonBean = this.service.daList(token,pageNumber,pageSize,prjoectName,projectCode);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
	
	@GetMapping("/da/dajyList")
	@Operation(summary = "档案借阅-列表")
	public JsonBean dajyList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
            @Parameter(name = "qdcode", description = "项目编号", required = false)@RequestParam(value = "qdcode", required = false, defaultValue = "") String qdcode) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.service.dajyList(token, pageNumber, pageSize,projectName,qdcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@Operation(summary = "档案借阅信息-新增修改接口")
    @RequestMapping(value = "/da/dajySaveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean dajySaveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblNbsjBorrowRecordEntity vo
    ) {
        try {
            return this.service.dajySaveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
	
	@RequestMapping(value = "/da/dajyDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "档案借阅-详情")
    public JsonBean dajyDetail(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "borrowId", description = "borrowId", required = false)@RequestParam(value = "borrowId", required = false) Integer borrowId){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.service.dajyDetail(token,borrowId);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	@GetMapping("/da/jyrzList")
	@Operation(summary = "借阅日志-列表")
	public JsonBean jyrzList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
            @Parameter(name = "qdcode", description = "项目编号", required = false)@RequestParam(value = "qdcode", required = false, defaultValue = "") String qdcode) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.service.jyrzList(token, pageNumber, pageSize,projectName,qdcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@GetMapping("/da/jyrzxqList")
	@Operation(summary = "借阅日志详情-列表")
	public JsonBean jyrzxqList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "id", description = "项目id", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.service.jyrzxqList(token,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
}

