package com.huabo.system.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hbfk.entity.TblAttachment;
import com.hbfk.util.DateUtil;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblSynchronizationRecord;
import com.huabo.system.mapper.TblAttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.service.TblAttachmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 文件预览控制器
 * <p>提供附件文件的在线预览、预览信息获取等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/filePreview")
@Tag(name="FilePreviewController",description="文件预览Controller")
public class FilePreviewController {
	
	@Resource
	private TblAttachmentService tblAttachmentService;

	@Resource
	TblAttachmentMapper tblAttachmentMapper;

	@Autowired(required = false)
	SecretLabel secretLabel;

	/**
	 * 合同匹配 - 合同制度 - 阅览
	 * @param reques
	 * @param token
	 * @param attId
	 * @param attType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPrivewAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="获取预览附件信息")
    public JsonBean getPrivewAttInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="attId",description="附件Id",required=true) @RequestParam(value = "attId", required = true) BigDecimal attId,
    		@Parameter(name="attType",description="附件类型 ，1是签署文件，2是正常上传文件,3.审批流程附件4.合同文本5.审核合同文本",required=true) @RequestParam(value = "attType", required = true) Integer attType) throws Exception {
		JsonBean jsonBean = null;
		try {
			jsonBean =  this.tblAttachmentService.getPrivewAttInfo(token, attId, attType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }


	@RequestMapping(value = "/getPrivewAttInfoNew", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="获取预览附件信息")
	public JsonBean getPrivewAttInfoNew(HttpServletRequest reques,
									 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
									 @Parameter(name="attId",description="附件Id",required=true) @RequestParam(value = "attId", required = true) BigDecimal attId,
									 @Parameter(name="attType",description="附件类型 ，1是签署文件，2是正常上传文件,3.审批流程附件4.合同文本5.审核合同文本",required=true) @RequestParam(value = "attType", required = true) Integer attType) throws Exception {
		JsonBean jsonBean = null;
		try {
			jsonBean =  this.tblAttachmentService.getPrivewAttInfoNew(token, attId, attType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

//	@RequestMapping(value = "/deleteTest")
	//删除临时文件
//	@Scheduled(cron = "0 0 1 * * ?")
//	@RequestMapping(value = "/deleteTest")
	public void scheduleUpdateOrgNewInfo() throws Exception {
		List<TblAttachment> list = tblAttachmentMapper.selectMemoByL();
		for (TblAttachment tblAttachment : list){
			new File(tblAttachment.getAttpath()).delete();
			tblAttachmentMapper.deleteEntity(tblAttachment.getAttid());
		}
	}

}
