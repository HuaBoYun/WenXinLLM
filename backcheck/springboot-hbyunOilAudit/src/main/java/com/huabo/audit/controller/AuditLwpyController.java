package com.huabo.audit.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsHjtz;
import com.huabo.audit.oracle.entity.TblYqnsResearch;
import com.huabo.audit.oracle.entity.TblYqnsResearchtb;
import com.huabo.audit.oracle.entity.TblYqnsTheory;
import com.huabo.audit.oracle.entity.TblYqnsXmHjtz;
import com.huabo.audit.oracle.service.TblYqnsHjtzService;
import com.huabo.audit.oracle.service.TblYqnsResearchtbService;
import com.huabo.audit.oracle.service.TblYqnsTheoryService;
import com.huabo.audit.oracle.service.TblYqnsXmHjtzService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.oracle.vo.XmdqVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 论文评优与理论研究控制器
 * <p>提供论文评优、理论研究的列表查询、新增、修改、删除、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="论文评优-理论研究-所有接口",description="论文评优-理论研究-所有接口")
@Slf4j
public class AuditLwpyController {

	@Resource
	private TblYqnsTheoryService tblYqnsTheoryService ; 
	
	@Resource
	private TblYqnsHjtzService tblYqnsHjtzService ; 
	
	@Resource
	private TblYqnsResearchtbService tblYqnsResearchtbService ; 
	@Resource
	private TblYqnsXmHjtzService  tblYqnsXmHjtzService;
	
	
	
	@Operation(summary = "理论研讨通知- 列表查询")
	@GetMapping("/yttz/getList")
	public JsonBean getsheetList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "理论研讨通知- 保存或修改")
	@PostMapping("/yttz/saveOrupdate")
	public JsonBean saveOrupdate(TblYqnsTheory ry,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.saveOrupdate(token, ry, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "理论研讨通知- 查询详情")
	@GetMapping("/yttz/getone")
	public JsonBean getone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.findByid(token, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "理论研讨通知- 查询附件列表")
	@GetMapping("/yttz/getattList")
	public JsonBean getattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.findattlistByid(token, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "理论研讨通知- 删除")
	@PostMapping("/yttz/deleteone")
	public JsonBean deleteone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.deleteone(token, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知- 删除 ...接口 异常", e); 
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "理论研讨通知- 删除附件")
	@PostMapping("/yttz/deleteatt")
	public JsonBean deleteatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知- 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "理论研讨通知- 下发人员")
	@PostMapping("/yttz/xfry")
	public JsonBean qdproject(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid,
			@Parameter(name = "xfryids", description = "下发人员id集合", required = false) @RequestParam(value = "xfryids", required = false) String xfryids,
			@Parameter(name = "xfrynames", description = "下发人员名称集合", required = false) @RequestParam(value = "xfrynames", required = false) String xfrynames) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsTheoryService.xfry(token, xfryids, xfrynames, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知- 下发人员 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}  
	


    
    @GetMapping("/yjsb/gettdList")
    @Operation(summary = "理论研究上报-填报列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            SjdwjdVo vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsResearchtbService.findAllList(token, pageNumber, pageSize, vo);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @PostMapping(value ="/yjsb/tbsaveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "理论研究上报-添加或修改填报信息")
    public JsonBean jdsaveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "glids", description = "glids", required = false)@RequestParam(value = "glids", required = false, defaultValue = "") String glids,
                                 TblYqnsResearchtb jd
    ){
        JsonBean jsonBean = null;
        try{
        	jsonBean=tblYqnsResearchtbService.saveOrupdate(token, jd, glids);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("/yjsb/tbdetail")
    @Operation(summary = "理论研究上报-根据ID获取填报详细信息")
    public JsonBean jddetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsResearchtbService.findByid(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @DeleteMapping("/yjsb/tbdelete")
    @Operation(summary = "理论研究上报-删除填报信息")
    public JsonBean jddelete(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean=tblYqnsResearchtbService.deleteone(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    

	@Operation(summary = "理论研究上报- 子表列表查询")
	@GetMapping("/yjsb/getList")
	public JsonBean getzbList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResearchtbService.findbyzb(token, tbid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研究上报- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "理论研究上报- 子表保存或修改")
	@PostMapping("/yjsb/saveOrupdatezb")
	public JsonBean saveOrupdatezb(TblYqnsResearch ch,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResearchtbService.saveOrupdatezb(token, ch);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研究上报-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "理论研究上报- 子表查询详情")
	@GetMapping("/yjsb/getone")
	public JsonBean getzbone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "chid", description = "id", required = false) @RequestParam(value = "chid", required = false) BigDecimal chid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResearchtbService.findByzbid(token, chid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研究上报-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	@Operation(summary = "理论研究台账- 列表查询")
	@GetMapping("/yatz/getList")
	public JsonBean getyatzList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsResearchtbService.findbyztz(token, vo, pageNumber, pageSize);
		} catch (ServiceException ex) {
			throw ex;  
		} catch (Exception e) {
			log.error("理论研究台账- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "理论研究台账- 导出")
	@GetMapping("/yatz/exportt")
	public void export(XmdqVo vo,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		try {
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "理论研究台账" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            vo.setPageSize(9999);
            List<TblYqnsResearch> list= tblYqnsResearchtbService.findexportlist(token, vo);
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"序号","研究方向","上报单位", "组长", "研究人员","撰写人","备注" };
            for (int i = 0; i < list.size(); i++) {
            	TblYqnsResearch r = list.get(i);
                Object[] o = { i+1,r.getDirection(),r.getTborgname(),r.getZzname(),r.getYjname(), r.getZxrname(), r.getRemarks()};
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研究台账- 导出 ...接口 异常", e);
		}
	}
	
	
	
	//==================================================
	@Operation(summary = "获奖通知- 列表查询")
	@GetMapping("/hjtz/getList")
	public JsonBean gethjtzList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("理论研讨通知- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "获奖通知- 保存或修改")
	@PostMapping("/hjtz/saveOrupdate")
	public JsonBean hjtzsaveOrupdate(TblYqnsHjtz ry,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attIds", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attIds", required = false) String attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.saveOrupdate(token, ry, attIds);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获奖通知-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	@Operation(summary = "获奖通知- 查询详情")
	@GetMapping("/hjtz/getone")
	public JsonBean gethjtzone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.findByid(token, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获奖通知-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "获奖通知- 查询附件列表")
	@GetMapping("/hjtz/getattList")
	public JsonBean gethjtzattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.findattlistByid(token, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获奖通知-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "获奖通知- 删除")
	@PostMapping("/hjtz/deleteone")
	public JsonBean deletehjtzone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.deleteone(token, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获奖通知- 删除 ...接口 异常", e); 
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "获奖通知- 删除附件")
	@PostMapping("/hjtz/deleteatt")
	public JsonBean deletehjtzatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获奖通知- 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "获奖通知- 下发人员")
	@PostMapping("/hjtz/hjtzxfry")
	public JsonBean hjtzxfry(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid,
			@Parameter(name = "xfryids", description = "下发人员id集合", required = false) @RequestParam(value = "xfryids", required = false) String xfryids,
			@Parameter(name = "xfrynames", description = "下发人员名称集合", required = false) @RequestParam(value = "xfrynames", required = false) String xfrynames) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsHjtzService.xfry(token, xfryids, xfrynames, ryid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获奖通知- 下发人员 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}  
	
	
	
	
	
	
	//==================================================
		@Operation(summary = "项目评优-获奖通知- 列表查询")
		@GetMapping("/xmpy/hjtz/getList")
		public JsonBean getxmpyhjtzList(XmdqVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
				@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.findAllList(token, pageNumber, pageSize, vo);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知- 列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		@Operation(summary = "项目评优-获奖通知- 保存或修改")
		@PostMapping("/xmpy/hjtz/saveOrupdate")
		public JsonBean xmhjtzsaveOrupdate(TblYqnsXmHjtz ry,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "attIds", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attIds", required = false) String attIds) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.saveOrupdate(token, ry, attIds);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知-保存或修改 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}

		@Operation(summary = "项目评优-获奖通知- 查询详情")
		@GetMapping("/xmpy/hjtz/getone")
		public JsonBean xmgethjtzone(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.findByid(token, ryid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知-查询详情 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		@Operation(summary = "项目评优-获奖通知- 查询附件列表")
		@GetMapping("/xmpy/hjtz/getattList")
		public JsonBean xmgethjtzattList(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.findattlistByid(token, ryid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知-查询附件列表 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		@Operation(summary = "项目评优-获奖通知- 删除")
		@PostMapping("/xmpy/hjtz/deleteone")
		public JsonBean xmdeletehjtzone(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.deleteone(token, ryid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知- 删除 ...接口 异常", e); 
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		
		@Operation(summary = "项目评优-获奖通知- 删除附件")
		@PostMapping("/xmpy/hjtz/deleteatt")
		public JsonBean xmdeletehjtzatt(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.deleteatt(token, attid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知- 删除附件 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		@Operation(summary = "项目评优-获奖通知- 下发人员")
		@PostMapping("/xmpy/hjtz/hjtzxfry")
		public JsonBean xmhjtzxfry(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "ryid", description = "id", required = false) @RequestParam(value = "ryid", required = false) BigDecimal ryid,
				@Parameter(name = "xfryids", description = "下发人员id集合", required = false) @RequestParam(value = "xfryids", required = false) String xfryids,
				@Parameter(name = "xfrynames", description = "下发人员名称集合", required = false) @RequestParam(value = "xfrynames", required = false) String xfrynames) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsXmHjtzService.xfry(token, xfryids, xfrynames, ryid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("项目评优-获奖通知- 下发人员 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}  
	
	
	
	
	
	
	
	
 
}
