package com.huabo.legal.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.DailyManagementService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;
import com.huabo.legal.vo.result.TblFwglConferenceManagement;
import com.huabo.legal.vo.result.TblFwglOtherFileMessage;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-日常管理所有接口",description="法务管理-日常管理所有接口")
@RequestMapping(value = "/api-auth/daily/management")
@Slf4j
public class DailyManagementController {

	@Resource
	private DailyManagementService dailyManagementService;

	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Resource
	private UserProvider userProvider;

	@Operation(summary = "会议管理列表 查询")
	@PostMapping("/conference/getList")
	public JsonBean getTblFwglConferenceManagementList(@RequestBody TblFwglConferenceManagementQueryParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
	        System.out.println(staff.getRealname());
			jsonBean = dailyManagementService.getTblFwglConferenceManagementList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 会议管理列表")
	@GetMapping("/conference/download-express")
	public void downloadExpressTblFwglConferenceManagement(@RequestHeader("token") String token, TblFwglConferenceManagementQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean conferenceManagementList = dailyManagementService.getTblFwglConferenceManagementList(param);
		PageResult<TblFwglConferenceManagement> result = (PageResult<TblFwglConferenceManagement>) conferenceManagementList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean conferenceManagementTempList = dailyManagementService.getTblFwglConferenceManagementList(param);
			PageResult<TblFwglConferenceManagement> tempList = (PageResult<TblFwglConferenceManagement>) conferenceManagementTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "会议管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglConferenceManagement.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "会议管理 新增/更新")
	@PostMapping("/conference/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglConferenceManagement(@RequestBody @Validated TblFwglConferenceManagement param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.saveOrUpdateTblFwglConferenceManagement(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "会议管理 刪除")
	@DeleteMapping("/conference/{id}")
	public JsonBean deleteTblFwglConferenceManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.deleteTblFwglConferenceManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "会议管理详情 查询")
	@GetMapping("/conference/{id}")
	public JsonBean getTblFwglConferenceManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.getTblFwglConferenceManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "其他文件报文列表 查询")
	@PostMapping("/other/file/getList")
	public JsonBean getTblFwglOtherFileMessageList(@RequestBody TblFwglOtherFileMessageQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.getTblFwglOtherFileMessageList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("其他文件报文列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 其他文件报文列表")
	@GetMapping("/other/file/download-express")
	public void downloadExpressTblFwglOtherFileMessage(@RequestHeader("token") String token, TblFwglOtherFileMessageQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglOtherFileMessageList = dailyManagementService.getTblFwglOtherFileMessageList(param);
		PageResult<TblFwglOtherFileMessage> result = (PageResult<TblFwglOtherFileMessage>) tblFwglOtherFileMessageList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglOtherFileMessageTempList = dailyManagementService.getTblFwglOtherFileMessageList(param);
			PageResult<TblFwglOtherFileMessage> tempList = (PageResult<TblFwglOtherFileMessage>) tblFwglOtherFileMessageTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "其他文件报文列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglOtherFileMessage.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "其他文件报文 新增/更新")
	@PostMapping("/other/file/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglOtherFileMessage(@RequestBody @Validated TblFwglOtherFileMessage param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.saveOrUpdateTblFwglOtherFileMessage(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("其他文件报文 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "其他文件报文 刪除")
	@DeleteMapping("/other/file/{id}")
	public JsonBean deleteTblFwglOtherFileMessage(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.deleteTblFwglOtherFileMessage(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("其他文件报文 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "其他文件报文详情 查询")
	@GetMapping("/other/file/{id}")
	public JsonBean getTblFwglOtherFileMessage(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = dailyManagementService.getTblFwglOtherFileMessage(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("其他文件报文详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}


	@RequestMapping(value = "/sendMetting/{id}", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@Operation(summary = "会议管理  推送会议消息")
	public String toSendBudgetMessage(@PathVariable Long id) {
		String content = null;
		String url = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300 * 1000).setConnectTimeout(300 * 1000).build();
			//主持人及会议人都需要发送消息
			JsonBean jsonbean = dailyManagementService.getTblFwglConferenceManagement(id);
			HashMap<String, Object> map = (HashMap<String, Object>) jsonbean.getData();
			TblFwglConferenceManagementOracle param = (TblFwglConferenceManagementOracle) map.get("conferenceManagement");
			TblStaffOracle loginStaff = tblStaffOracleService.getUserInfoForId(Long.valueOf(param.getCreator()));
			String user = param.getParticipants() + "," + param.getCompere();
			JSONArray arr = new JSONArray();
			for (String u : user.split(",")) {
				if (StringUtils.isNotBlank(u)) {
					TblStaffOracle staff = tblStaffOracleService.getUserInfoForId(Long.valueOf(u));
					JSONObject obj = new JSONObject();
					obj.put("thirdpartyMessageId", param.getConferenceId());//第三方待办主键（保证唯一） 必填
					obj.put("thirdpartyRegisterCode", HttpClient.oaCode);//为第三方配置的系统注册编码 必填
					obj.put("messageContent", "会议名称：" + param.getConferenceName() + "会议内容：" + param.getContent() + " 会议时间:" + sdf
							.format(param.getConferenceTime()));//待办标题 必填  dataMap.get("title")
					obj.put("creation_date", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
					obj.put("messageType", 0);//state 状态：0:未办理；1:已办理 必填
					obj.put("downloadurl", "");
					obj.put("thirdpartySenderId", loginStaff.getStaffId());////第三方待办发起人主键（保证唯一） 非必填
					obj.put("thirdpartyReceiverId", staff.getStaffId());////第三方待办发起人姓名 必填
					obj.put("creation_date", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
					obj.put("messageType", 0);//0：PC；
					obj.put("messageURL", "");//PC 端穿透链接
					obj.put("messageH5URL", "");
					obj.put("appParam", "");
					obj.put("noneBindingSender", loginStaff.getUserName());//对应的发起人员oa登录名loginStaff.getUserName()
					obj.put("noneBindingReceiver", staff.getUserName());//OA 对应的处理人员staff.getUserName()
					arr.add(obj);
				}
			}
			url = HttpClient.oaUrl + HttpClient.messageList;
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			JSONObject object = new JSONObject();
			object.put("messages", arr);
			post.setHeader("Content-Type", "application/json;charset=utf-8");
			post.setHeader("token", this.getOaToken());
			StringEntity postingString = new StringEntity(object.toString(), "utf-8");
			post.setEntity(postingString);
			HttpResponse response = httpClient.execute(post);
			content = EntityUtils.toString(response.getEntity());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonBean.error();
		}
		return JsonBean.success();
	}

	//获取对方token数据
	public String getOaToken() {
		String token = null;
		try {
			String url = "";
			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300 * 1000).setConnectTimeout(300 * 1000).build();
			url = HttpClient.oaUrl + HttpClient.getToken;
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			JSONObject obj = new JSONObject();
			obj.put("userName", HttpClient.restuname);//第三方待办主键（保证唯一）
			obj.put("password", HttpClient.restpassword);//为第三方配置的系统注册编码
			post.setHeader("Content-Type", "application/json;charset=utf-8");
			StringEntity postingString = new StringEntity(obj.toString(), "utf-8");
			post.setEntity(postingString);
			HttpResponse response = httpClient.execute(post);
			String content = EntityUtils.toString(response.getEntity());
			JSONObject result = JSONObject.parseObject(content);
			token = result.get("id").toString();
			System.out.println(token);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取token失败：" + e.getMessage());
			return token;
		}
		return token;
	}


	public static void main(String[] args) {
		try {
			String url = "http://192.0.2.200:8080/seeyon/rest/token";
			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300 * 1000).setConnectTimeout(300 * 1000).build();
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
			obj.put("userName", "RestForSzfk");//第三方待办主键（保证唯一）
			obj.put("password", "ae54616e-60ae-4637-8664-3609896c2142");//为第三方配置的系统注册编码
			post.setHeader("Content-Type", "application/json;charset=utf-8");
			StringEntity postingString = new StringEntity(obj.toString(), "utf-8");
			post.setEntity(postingString);
			HttpResponse response = httpClient.execute(post);
			String content = EntityUtils.toString(response.getEntity());
			com.alibaba.fastjson.JSONObject result = com.alibaba.fastjson.JSONObject.parseObject(content);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
