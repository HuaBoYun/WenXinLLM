package com.huabo.audit.controller;


import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsLetter;
import com.huabo.audit.oracle.entity.TblYqnsResult;
import com.huabo.audit.oracle.entity.TblYqnsSheet;
import com.huabo.audit.oracle.service.TblYqnsLetterService;
import com.huabo.audit.oracle.service.TblYqnsResultService;
import com.huabo.audit.oracle.service.TblYqnsSheetService;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计实施控制器
 * <p>提供审计实施阶段的相关操作接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="审计-审计实施接口",description="审计-审计实施接口")
@Slf4j
public class AuditPutController {

	@Resource
	private TblYqnsSheetService tblYqnsSheetService ;
	 
	
	@Resource
	private TblYqnsLetterService tblYqnsLetterService ;
	
	@Resource
	private TblYqnsResultService tblYqnsResultService ;
	
	@Operation(summary = "我的底稿- 列表查询")
	@GetMapping("/sheet/getList")
	public JsonBean getsheetList(TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsSheetService.findAllList(token, pageNumber, pageSize, tBlNbsjSheetVo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("我的底稿-列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "我的底稿- 保存或修改")
	@PostMapping("/sheet/saveOrupdate")
	public JsonBean saveOrupdate(TblYqnsSheet sheet,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsSheetService.saveOrupdate(token, sheet, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("我的底稿-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "我的底稿- 查询详情")
	@GetMapping("/sheet/getone")
	public JsonBean getone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "sheetid", description = "底稿id", required = false) @RequestParam(value = "sheetid", required = false) BigDecimal sheetid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsSheetService.findByid(token, sheetid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("我的底稿-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "我的底稿- 查询附件列表")
	@GetMapping("/sheet/getattList")
	public JsonBean getattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "sheetid", description = "底稿id", required = false) @RequestParam(value = "sheetid", required = false) BigDecimal sheetid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsSheetService.findattlistByid(token, sheetid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("我的底稿-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "我的底稿- 删除底稿")
	@PostMapping("/sheet/deleteone")
	public JsonBean deleteone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "sheetid", description = "底稿id", required = false) @RequestParam(value = "sheetid", required = false) BigDecimal sheetid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsSheetService.deleteone(token, sheetid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("我的底稿- 删除底稿 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "我的底稿- 删除底稿附件")
	@PostMapping("/sheet/deleteatt")
	public JsonBean deleteatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) BigDecimal attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsSheetService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("我的底稿- 删除底稿附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	
	
	
	@Operation(summary = "审计承诺书- 列表查询")
	@GetMapping("/letter/getList")
	public JsonBean getletterList(TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsLetterService.findAllList(token, pageNumber, pageSize, tBlNbsjSheetVo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计承诺书-列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "审计承诺书- 保存或修改")
	@PostMapping("/letter/saveOrupdate")
	public JsonBean lettersaveOrupdate(TblYqnsLetter letter,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attIds", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attIds", required = false) String attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsLetterService.saveOrupdate(token, letter, attIds);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计承诺书-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "审计承诺书- 查询详情")
	@GetMapping("/letter/getone")
	public JsonBean getletterone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "letterid", description = " 承诺书id", required = false) @RequestParam(value = "letterid", required = false) BigDecimal letterid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsLetterService.findByid(token, letterid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计承诺书-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计承诺书- 查询附件列表")
	@GetMapping("/letter/getattList")
	public JsonBean getletterattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "letterid", description = "承诺书id", required = false) @RequestParam(value = "letterid", required = false) BigDecimal letterid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsLetterService.findattlistByid(token, letterid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计承诺书-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计承诺书- 删除审计承诺书")
	@PostMapping("/letter/deleteone")
	public JsonBean deleteletterone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "letterid", description = "承诺书id", required = false) @RequestParam(value = "letterid", required = false) BigDecimal letterid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsLetterService.deleteone(token, letterid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计承诺书- 删除审计承诺书 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "审计承诺书- 删除底稿附件")
	@PostMapping("/letter/deleteatt")
	public JsonBean deleteletteratt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) BigDecimal attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsLetterService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计承诺书- 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	
	@Operation(summary = "审计结果确认单- 列表查询")
	@GetMapping("/result/getList")
	public JsonBean getresultList(TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.findAllList(token, pageNumber, pageSize, tBlNbsjSheetVo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单-列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	
	@Operation(summary = "审计审减内容- 列表查询")
	@GetMapping("/sjgl/getList")
	public JsonBean sjglgetresultList(TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.findbyhj(token, tBlNbsjSheetVo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单-列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	
	@Operation(summary = "审计结果确认单- 保存或修改")
	@PostMapping("/result/saveOrupdate")
	public JsonBean resultsaveOrupdate(TblYqnsResult result,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids,
			@Parameter(name = "jsonObject", description = "子表项", required = false) @RequestParam(value = "jsonObject", required = false) String jsonObject) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.saveOrupdate(token, result, attids,jsonObject);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "审计结果确认单- 查询详情")
	@GetMapping("/result/getone")
	public JsonBean getresultone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "resultid", description = "结果确认单id", required = false) @RequestParam(value = "resultid", required = false) BigDecimal resultid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.findByid(token, resultid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计结果确认单- 查询附件列表")
	@GetMapping("/result/getattList")
	public JsonBean getresultattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "resultid", description = "结果确认单id", required = false) @RequestParam(value = "resultid", required = false) BigDecimal resultid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.findattlistByid(token, resultid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计结果确认单- 删除审计承诺书")
	@PostMapping("/result/deleteone")
	public JsonBean deleteresultone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "resultid", description = "结果确认单id", required = false) @RequestParam(value = "resultid", required = false) BigDecimal resultid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.deleteone(token, resultid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单- 删除审计承诺书 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "审计结果确认单- 删除底稿附件")
	@PostMapping("/result/deleteatt")
	public JsonBean deleteresultatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) BigDecimal attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计结果确认单 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	@Operation(summary = "审计项目追款- 列表查询")
	@GetMapping("/sjxmzk/getList")
	public JsonBean getSjxmzkList(TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResultService.findAllList(token, pageNumber, pageSize, tBlNbsjSheetVo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计项目追款-列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@GetMapping("/sjxmzk/export")
    @Operation(summary = "审计项目追款-列表导出")
    public JsonBean sjxmzkExport( HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "tBlNbsjSheetVo", description = "审计项目追款entity") TBlNbsjSheetVo tBlNbsjSheetVo,
		  	@Parameter(name = "idList", description = "审计结果id集合") @RequestParam(value = "idList", required = false, defaultValue = "") List<String> idList
	){
        response.setContentType("application/binary;charset=UTF-8");
        JsonBean jsonBean = new JsonBean();
        try{
        	
        	jsonBean =  tblYqnsResultService.sjxmzkExport(response,token,tBlNbsjSheetVo, idList);
        	
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonBean;
    }
	
	
	
	@GetMapping("/sjsj/export")
    @Operation(summary = "审计审减内容-列表导出")
    public JsonBean sjsjExport( HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "tBlNbsjSheetVo", description = "查询实体") TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "idList", description = "审计结果确认id集合", required = false) @RequestParam(value = "idList",required = false) List<String> idList){
        response.setContentType("application/binary;charset=UTF-8");
        JsonBean jsonBean = new JsonBean();
        try{
        	
        	jsonBean =  tblYqnsResultService.exportData(response,token,tBlNbsjSheetVo, idList);
        	
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonBean;
    }
	
	
}
