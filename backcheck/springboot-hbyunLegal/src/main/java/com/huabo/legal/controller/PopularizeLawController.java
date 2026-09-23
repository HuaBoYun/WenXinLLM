package com.huabo.legal.controller;


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
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.config.DateBaseConfig;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglActivityManagementMySql;
import com.huabo.legal.mysql.entity.TblFwglPopularizeLawPlanMySql;
import com.huabo.legal.mysql.entity.TblFwglSubjectManagementMySql;
import com.huabo.legal.oracle.entity.TblFwglActivityManagementOracle;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.PopularizeLawService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglActivityManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglLeaderXf;
import com.huabo.legal.vo.param.TblFwglLeaderXfQueryParam;
import com.huabo.legal.vo.param.TblFwglPopularizeLawPlanQueryParam;
import com.huabo.legal.vo.param.TblFwglSubjectManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglXfExamQueryParam;
import com.huabo.legal.vo.result.TblFwglActivityManagement;
import com.huabo.legal.vo.result.TblFwglPopularizeLawPlan;
import com.huabo.legal.vo.result.TblFwglSubjectManagement;
import com.huabo.legal.vo.result.TblFwglXfExam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-普法培训所有接口",description="法务管理-普法培训所有接口")
@RequestMapping(value = "/api-auth/law/train")
@Slf4j
public class PopularizeLawController {

	@Resource
	private PopularizeLawService popularizeLawService;

	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "普法计划列表 查询")
	@PostMapping("/popularize/law/plan/getList")
	public JsonBean getTblFwglPopularizeLawPlanList(@RequestBody TblFwglPopularizeLawPlanQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglPopularizeLawPlanList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("普法计划列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "普法计划 新增/更新")
	@PostMapping("/popularize/law/plan/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPopularizeLawPlan(@RequestBody @Validated TblFwglPopularizeLawPlan param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.saveOrUpdateTblFwglPopularizeLawPlan(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("普法计划 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "普法计划 刪除")
	@DeleteMapping("/popularize/law/plan/{id}")
	public JsonBean deleteFwglPopularizeLawPlan(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.deleteFwglPopularizeLawPlan(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("普法计划 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "普法计划详情 查询")
	@GetMapping("/popularize/law/plan/{id}")
	public JsonBean getTblFwglPopularizeLawPlan(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglPopularizeLawPlan(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("普法计划详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 普法计划列表")//download-express
	@GetMapping("/popularize/law/download-express")
	public void downloadExpressTblFwglPopularizeLawPlan(@RequestHeader("token") String token, TblFwglPopularizeLawPlanQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglPopularizeLawPlanList = popularizeLawService.getTblFwglPopularizeLawPlanList(param);
		PageResult<TblFwglPopularizeLawPlanMySql> result = (PageResult<TblFwglPopularizeLawPlanMySql>) tblFwglPopularizeLawPlanList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglPopularizeLawPlanTempList = popularizeLawService.getTblFwglPopularizeLawPlanList(param);
			PageResult<TblFwglPopularizeLawPlanMySql> tempList = (PageResult<TblFwglPopularizeLawPlanMySql>) tblFwglPopularizeLawPlanTempList
					.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "普法计划列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglPopularizeLawPlan.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "活动管理列表 查询")
	@PostMapping("/activity/management/getList")
	public JsonBean getTblFwglActivityManagementList(@RequestBody TblFwglActivityManagementQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglActivityManagementList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("活动管理列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 活动管理列表")//download-express
	@GetMapping("/activity/management/download-express")
	public void downloadExpressTblFwglPracticeExamine(@RequestHeader("token") String token, TblFwglActivityManagementQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglActivityManagementList = popularizeLawService.getTblFwglActivityManagementList(param);
		PageResult<TblFwglActivityManagementMySql> result = (PageResult<TblFwglActivityManagementMySql>) tblFwglActivityManagementList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglActivityManagementTempList = popularizeLawService.getTblFwglActivityManagementList(param);
			PageResult<TblFwglActivityManagementMySql> tempList = (PageResult<TblFwglActivityManagementMySql>) tblFwglActivityManagementTempList
					.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "活动管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglActivityManagement.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "活动管理 新增/更新")
	@PostMapping("/activity/management/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglActivityManagement(@RequestBody @Validated TblFwglActivityManagement param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.saveOrUpdateTblFwglActivityManagement(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("活动管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "活动管理 刪除")
	@DeleteMapping("/activity/management/{id}")
	public JsonBean deleteTblFwglActivityManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.deleteTblFwglActivityManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("活动管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "活动管理详情 查询")
	@GetMapping("/activity/management/{id}")
	public JsonBean getTblFwglActivityManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglActivityManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("活动管理详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "课题管理列表 查询")
	@PostMapping("/subject/management/getList")
	public JsonBean getTblFwglSubjectManagementList(@RequestBody TblFwglSubjectManagementQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglSubjectManagementList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("课题管理列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 课题管理列表")
	@GetMapping("/subject/management/download-express")
	public void downloadExpressTblFwglSubjectManagement(@RequestHeader("token") String token, TblFwglSubjectManagementQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglSubjectManagementList = popularizeLawService.getTblFwglSubjectManagementList(param);
		PageResult<TblFwglSubjectManagementMySql> result = (PageResult<TblFwglSubjectManagementMySql>) tblFwglSubjectManagementList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglSubjectManagementTempList = popularizeLawService.getTblFwglSubjectManagementList(param);
			PageResult<TblFwglSubjectManagementMySql> tempList = (PageResult<TblFwglSubjectManagementMySql>) tblFwglSubjectManagementTempList
					.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "课题管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglSubjectManagement.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "课题管理 新增/更新")
	@PostMapping("/subject/management/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglSubjectManagement(@RequestBody @Validated TblFwglSubjectManagement param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.saveOrUpdateTblFwglSubjectManagement(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("课题管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "课题管理 刪除")
	@DeleteMapping("/subject/management/{id}")
	public JsonBean deleteTblFwglSubjectManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.deleteTblFwglSubjectManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("课题管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "课题管理详情 查询")
	@GetMapping("/subject/management/{id}")
	public JsonBean getTblFwglSubjectManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglSubjectManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("课题管理详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "学法考试列表 查询")
	@PostMapping("/xf/exam/getList")
	public JsonBean getTblFwglXfExamList(@RequestBody TblFwglXfExamQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglXfExamList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学法考试列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "学法考试 新增/更新")
	@PostMapping("/xf/exam/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglXfExam(@RequestBody @Validated TblFwglXfExam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.saveOrUpdateTblFwglXfExam(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学法考试 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "学法考试 刪除")
	@DeleteMapping("/xf/exam/{id}")
	public JsonBean deleteTblFwglXfExam(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.deleteTblFwglXfExam(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学法考试 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "学法考试详情 查询")
	@GetMapping("/xf/exam/{id}")
	public JsonBean getTblFwglXfExam(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglXfExam(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学法考试详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "领导学法列表 查询")
	@PostMapping("/leader/xf/getList")
	public JsonBean getTblFwglLeaderXfList(@RequestBody TblFwglLeaderXfQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglLeaderXfList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("领导学法列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "领导学法 新增/更新")
	@PostMapping("/leader/xf/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLeaderXf(@RequestBody @Validated TblFwglLeaderXf param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.saveOrUpdateTblFwglLeaderXf(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("领导学法 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "领导学法 刪除")
	@DeleteMapping("/leader/xf/{id}")
	public JsonBean deleteTblFwglLeaderXf(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.deleteTblFwglLeaderXf(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("领导学法 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "领导学法详情 查询")
	@GetMapping("/leader/xf/{id}")
	public JsonBean getTblFwglLeaderXf(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = popularizeLawService.getTblFwglLeaderXf(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("领导学法详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}


	@RequestMapping(value = "/sendMetting/{id}", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@Operation(summary = "普法培训-活动管理")
	public String toSendBudgetMessage(@PathVariable Long id) {
		String content = null;
		String url = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300 * 1000).setConnectTimeout(300 * 1000).build();
			// 主持人及会议人都需要发送消息
			JsonBean jsonbean = popularizeLawService.getTblFwglActivityManagement(id);
			HashMap<String, Object> map = (HashMap<String, Object>) jsonbean.getData();
			if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
				TblFwglActivityManagementOracle param = (TblFwglActivityManagementOracle) map.get("activityManagement");
				String user = param.getInformPersonnel();
				TblStaffOracle loginStaff = tblStaffOracleService.getUserInfoForId(Long.valueOf(param.getCreator()));
				JSONArray arr = new JSONArray();
				for (String u : user.split(",")) {
					if (StringUtils.isNotBlank(u)) {
						TblStaffOracle staff = tblStaffOracleService.getUserInfoForId(Long.valueOf(u));
						JSONObject obj = new JSONObject();
						obj.put("thirdpartyMessageId", param.getActivityManagementId());// 第三方待办主键（保证唯一） // 必填
						obj.put("thirdpartyRegisterCode", HttpClient.oaCode);// 为第三方配置的系统注册编码 // 必填
						obj.put("messageContent", "活动主题：" + param.getActivityTopic() + " 活动时间:" + sdf.format(param.getRegisterTime()));// 待办标题
						obj.put("creation_date", sdf.format(new Date()));// 待办创建时间（格式：yyyyMM-dd
						obj.put("messageType", 0);// state 状态：0:未办理；1:已办理 必填
						obj.put("downloadurl", "");
						obj.put("thirdpartySenderId", loginStaff.getStaffId());//// 第三方待办发起人主键（保证唯一） 非必填
						obj.put("thirdpartyReceiverId", staff.getStaffId());//// 第三方待办发起人姓名 必填
						obj.put("creation_date", sdf.format(new Date()));// 待办创建时间（格式：yyyyMM-dd
						obj.put("messageType", 0);// 0：PC；
						obj.put("messageURL", "");// PC 端穿透链接
						obj.put("messageH5URL", "");
						obj.put("appParam", "");
						obj.put("noneBindingSender", loginStaff.getUserName());// 对应的发起人员oa登录名loginStaff.getUserName()
						obj.put("noneBindingReceiver", staff.getUserName());// OA 对应的处理人员 // staff.getUserName()
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
			} else {
				TblFwglActivityManagementMySql param = (TblFwglActivityManagementMySql) map.get("activityManagement");
				String user = param.getInformPersonnel();
				TblStaffOracle loginStaff = tblStaffOracleService.getUserInfoForId(Long.valueOf(param.getCreator()));
				JSONArray arr = new JSONArray();
				for (String u : user.split(",")) {
					if (StringUtils.isNotBlank(u)) {
						TblStaffOracle staff = tblStaffOracleService.getUserInfoForId(Long.valueOf(u));
						JSONObject obj = new JSONObject();
						obj.put("thirdpartyMessageId", param.getActivityManagementId());// 第三方待办主键（保证唯一）
						obj.put("thirdpartyRegisterCode", HttpClient.oaCode);// 为第三方配置的系统注册编码 // 必填
						obj.put("messageContent", "活动主题：" + param.getActivityTopic() + " 活动时间:" + sdf.format(param.getRegisterTime()));// 待办标题
						obj.put("creation_date", sdf.format(new Date()));// 待办创建时间（格式：yyyyMM-dd
						obj.put("messageType", 0);// state 状态：0:未办理；1:已办理 必填
						obj.put("thirdpartySenderId", loginStaff.getUserName());//// 第三方待办发起人主键（保证唯一） 非必填
						obj.put("thirdpartyReceiverId", staff.getUserName());//// 第三方待办发起人姓名 必填
						obj.put("creation_date", sdf.format(new Date()));// 待办创建时间（格式：yyyyMM-dd// HH:mm:ss）
						obj.put("messageType", 0);// 0：PC；
						obj.put("messageURL", "");// PC 端穿透链接
						obj.put("noneBindingSender", loginStaff.getUserName());// 对应的发起人员oa登录名loginStaff.getUserName()
						obj.put("noneBindingReceiver", staff.getUserName());// OA 对应的处理人员
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
			}
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
}
