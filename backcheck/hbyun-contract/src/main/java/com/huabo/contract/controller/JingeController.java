package com.huabo.contract.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.controller.jinge.OfficeServer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * JinGe富文本编辑器控制器
 * <p>提供JinGe富文本编辑器的文件上传、内容保存等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="JinGe富文本编辑器",description="JinGe富文本编辑器")
public class JingeController {
	
	@Resource
	private UserProvider userProvider;

	OfficeServer officeServer = new OfficeServer();
	
	@PostMapping("/fwb/fwbEdit/OfficeServer")
	@Operation(summary = "富文本编辑器处理")
	public JsonBean fwbEdit(HttpServletRequest request,  HttpServletResponse response) {

		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
			officeServer.service(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
}
