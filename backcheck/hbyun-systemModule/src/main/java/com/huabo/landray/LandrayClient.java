package com.huabo.landray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.EncryptUtil;
import com.huabo.system.entity.*;
import com.huabo.system.mapper.TblCirculationMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.service.QyWeiXinService;
import com.huabo.system.utils.QyWeiXinUtil;
import com.landray.kmss.sys.notify.webservice.ISysNotifyTodoWebService;
import com.landray.kmss.sys.notify.webservice.NotifyTodoAppResult;
import com.landray.kmss.sys.notify.webservice.NotifyTodoRemoveContext;
import com.landray.kmss.sys.notify.webservice.NotifyTodoSendContext;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 蓝凌OA系统客户端调用类
 */
@Component
public class LandrayClient {

	@Resource
	TblStaffMapper tblStaffMapper;

	@Resource
	QyWeiXinService qyWeiXinService;

	@Resource
	TblOrganizationMapper tblOrganizationMapper;

	@Resource
	TblCirculationMapper tblCirculationMapper;

	public static final String APP_NAME = "审计系统";

	public static final String MODEL_NAME = "审批代办";

	/**
	 * 回调地址
	 *
	 * @param username
	 * @return
	 */
	public String getCallbackUrl(String username) {
		String localSystemToken = qyWeiXinService.getLocalSystemToken(username);
		return QyWeiXinUtil.call_back_url_prefix.concat(QyWeiXinUtil.call_back_url_wddb).replaceAll("TOKEN", localSystemToken);
	}

	/**
	 * 回调地址-催办消息
	 */
	public String getCallbackUrlReminder(String username) {
		String localSystemToken = qyWeiXinService.getLocalSystemToken(username);
		return QyWeiXinUtil.call_back_url_prefix.concat(QyWeiXinUtil.call_back_url_reminder).replaceAll("TOKEN", localSystemToken);
	}

	/**
	 * 我的代办
	 *
	 * @param token
	 * @param tblMyTask
	 * @param titleName
	 */
	public void sendMyTodoMsg(String token, TblMyTask tblMyTask, String titleName) {
		List<TblMyTask> tasks = new ArrayList<>();
		tasks.add(tblMyTask);
		this.sendMyTodoMsg(token, tasks, titleName);
	}

	/**
	 * 我的代办
	 *
	 * @param token
	 * @param tasks
	 * @param titleName
	 */
	public void sendMyTodoMsg(String token, List<TblMyTask> tasks, String titleName) {
		try {
			String staffIdLogin = EncryptUtil.getInstance().AESdecode(token, EncryptUtil.DESKEY);
			TblStaff tblStaffLogin = this.tblStaffMapper.findByStaffid(new BigDecimal(staffIdLogin));
			TblOrganization organization = this.tblOrganizationMapper.selectCompanyInfoByDeptId(tblStaffLogin.getOrgid());
			TblMyTask tblMyTask = tasks.stream().reduce((a, b) -> b).orElse(null);

			if (tblMyTask == null) {
				return;
			}

			System.out.println("tblMyTask.getHandle========================================" + tblMyTask.getHandle());
			TblCirculation cir = tblCirculationMapper.findById(tblMyTask.getCirid());
			//提交人
			String subStaffId = cir.getCystaffid();
			TblStaff subStaff = tblStaffMapper.getById(subStaffId);

			TblStaff staff = qyWeiXinService.getNextStaff(tblMyTask.getHandle(), organization.getOrgid().toString());
			if (staff == null) {
				System.out.println("next staff =========================== " + staff);
				//审批流程结束发送提交人已办消息
				// this.setTodoDone(cir.getCyid().toString(), subStaff.getUsername());


				//发送待办
				TblNbsjMessagetodo tod = new TblNbsjMessagetodo();
				tod.setModelId(tblMyTask.getCirid());//防止多数据传递
				tod.setKey(tblMyTask.getCirid() + "");//记录表单id
				tod.setAppName(APP_NAME);
				tod.setModelName(MODEL_NAME);

				//流程标题
				tod.setSubject(subStaff.getRealname() + "发起的【" + titleName + "】审批流程  已审核完成");

				tod.setType(2);//设置为通知类待办
				tod.setTargets(subStaff.getUsername());
				tod.setCreateName(subStaff.getUsername());

				String url1 = getCallbackUrl(subStaff.getUsername());
				url1 = url1 + "&cyid=" + tblMyTask.getCirid() + "&taskid=" + tblMyTask.getFromid() + "&cytype=" + cir.getCytype() + "&username="
						+ subStaff.getUsername() + "&istype=2";

				tod.setLink(url1);
				tod.setCreateTime(new Date());
				todoSend(tod);


				return;
			}

			TblNbsjMessagetodo tod = new TblNbsjMessagetodo();
			tod.setModelId(tblMyTask.getCirid());//防止多数据传递
			tod.setKey(tblMyTask.getCirid() + "");//记录表单id
			tod.setAppName(APP_NAME);
			tod.setModelName(MODEL_NAME);

			//流程标题
			tod.setSubject(subStaff.getRealname() + "发起的【" + titleName + "】审批流程");

			tod.setType(1);
			tod.setTargets(staff.getUsername());
			tod.setCreateName(subStaff.getUsername());

			String url1 = getCallbackUrl(staff.getUsername());
			url1 = url1 + "&cyid=" + tblMyTask.getCirid() + "&taskid=" + tblMyTask.getFromid() + "&cytype=" + cir.getCytype() + "&username=" + staff
					.getUsername();

			tod.setLink(url1);
			tod.setCreateTime(new Date());
			todoSend(tod);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 发送代办
	 *
	 * @param tem
	 * @throws Exception
	 */
	public void todoSend(TblNbsjMessagetodo tem) throws Exception {
		WebServiceConfig cfg = WebServiceConfig.getInstance();
		ISysNotifyTodoWebService service = (ISysNotifyTodoWebService) callService(cfg.getAddress(), cfg.getServiceClass());
		// 请在此处添加业务代码
		NotifyTodoSendContext context = new NotifyTodoSendContext();
		context.setKey(tem.getKey());
		context.setAppName(tem.getAppName());
		context.setModelName(tem.getModelName());
		context.setModelId(tem.getModelId());
		context.setSubject(tem.getSubject());
		context.setLink(tem.getLink());
		context.setType(tem.getType());
		context.setDocCreator(getJsonString(tem.getTargets()));  //"+tem.getCreateName()+"
		// 待办对应接收人，数据格式为JSON，格式描述请查看"《2.1 组织架构数据说明》"
		context.setTargets(getJsonString(tem.getTargets())); //"+tem.getTargets()+"
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		context.setCreateTime(sdf2.format(new Date()));

		NotifyTodoAppResult result = service.sendTodo(context);

		System.out.println("发送代办setKey=" + tem.getKey());
		System.out.println("发送代办setAppName=" + APP_NAME);
		System.out.println("发送代办setModelName=" + tem.getModelName());
		System.out.println("发送代办setModelId=" + tem.getModelId());
		System.out.println("发送代办setSubject=" + tem.getSubject());
		System.out.println("发送代办setLink=" + tem.getLink());
		System.out.println("发送代办setDocCreator=" + getJsonString(tem.getTargets()));
		System.out.println("发送代办setTargets=" + getJsonString(tem.getTargets()));
		System.out.println("发送代办setCreateTime=" + sdf2.format(new Date()));

		if (result != null) {
			System.out.println(result.getReturnState() + "***********" + result.getMessage());
			if (result.getReturnState() == 2) {
				System.out.println("我的代办++++++++++++++++++++ " + JSON.toJSONString(result));
			}
		}
	}

	/**
	 * 目标用户
	 *
	 * @param targets
	 * @return
	 */
	private String getJsonString(String targets) {
		JSONObject loginInfo = new JSONObject();
		loginInfo.put("LoginName", targets);
		return loginInfo.toJSONString();
	}

	/**
	 * 设为已办
	 *
	 * @throws Exception
	 */
	public void setTodoDone(String taskId, String userName) throws Exception {
		WebServiceConfig cfg = WebServiceConfig.getInstance();
		ISysNotifyTodoWebService service = (ISysNotifyTodoWebService) callService(cfg.getAddress(), cfg.getServiceClass());
		NotifyTodoRemoveContext context = new NotifyTodoRemoveContext();
		context.setAppName(APP_NAME);
		context.setModelName(MODEL_NAME);
		context.setModelId(taskId);
		context.setOptType(2);
		context.setTargets(getJsonString(userName));
		context.setKey(taskId + "");
		context.setType(1);
		NotifyTodoAppResult result = service.setTodoDone(context);

		System.out.println("设为已办setAppName=" + APP_NAME);
		System.out.println("设为已办setModelName=" + MODEL_NAME);
		System.out.println("设为已办setModelId=" + taskId);
		System.out.println("设为已办setOptType=" + 2);
		System.out.println("设为已办setTargets=" + getJsonString(userName));
		System.out.println("设为已办setKey=" + taskId + "");
		System.out.println("设为已办setType=" + 1);


		if (result != null) {
			System.out.println(result.getReturnState() + "***********" + result.getMessage());
			if (result.getReturnState() == 2) {
				System.out.println("设为已办++++++++++++++++++++ " + JSON.toJSONString(result));
			}
		}
	}


	/**
	 * 通知类设为已办
	 *
	 * @throws Exception
	 */
	public void setTzodoDone(String taskId, String userName) throws Exception {
		WebServiceConfig cfg = WebServiceConfig.getInstance();
		ISysNotifyTodoWebService service = (ISysNotifyTodoWebService) callService(cfg.getAddress(), cfg.getServiceClass());
		NotifyTodoRemoveContext context = new NotifyTodoRemoveContext();
		context.setAppName(APP_NAME);
		context.setModelName(MODEL_NAME);
		context.setModelId(taskId);
		context.setOptType(2);
		context.setTargets(getJsonString(userName));
		context.setKey(taskId + "");
		context.setType(2);
		NotifyTodoAppResult result = service.setTodoDone(context);

		System.out.println("设为通知已办setAppName=" + APP_NAME);
		System.out.println("设为通知已办setModelName=" + MODEL_NAME);
		System.out.println("设为通知已办setModelId=" + taskId);
		System.out.println("设为通知已办setOptType=" + 2);
		System.out.println("设为通知已办setTargets=" + getJsonString(userName));
		System.out.println("设为通知已办setKey=" + taskId + "");
		System.out.println("设为通知已办setType=" + 2);


		if (result != null) {
			System.out.println(result.getReturnState() + "***********" + result.getMessage());
			if (result.getReturnState() == 2) {
				System.out.println("设为已办++++++++++++++++++++ " + JSON.toJSONString(result));
			}
		}
	}


	/**
	 * 审批完成给提交人发送审批完成消息
	 *
	 * @param id       单据标识
	 * @param name     单据名称
	 * @param userName 提交人
	 * @throws Exception 异常信息
	 * @deprecated
	 */
	public void sendAuditCompleteMsg(String id, String name, String userName) throws Exception {
		WebServiceConfig cfg = WebServiceConfig.getInstance();
		ISysNotifyTodoWebService service = (ISysNotifyTodoWebService) callService(cfg.getAddress(), cfg.getServiceClass());
		NotifyTodoRemoveContext cxt = new NotifyTodoRemoveContext();
		cxt.setAppName("审计系统");
		cxt.setModelName("审批模块");
		cxt.setModelId(id);
		cxt.setOptType(2);
		cxt.setTargets(this.getJsonString(userName));
		cxt.setKey(name.concat("【审批完成】"));
		cxt.setType(2);
		NotifyTodoAppResult result = service.setTodoDone(cxt);
		if (result != null) {
			System.out.println(result.getReturnState() + "***********" + result.getMessage());
			if (result.getReturnState() == 2) {
				System.out.println("result = " + result);
			}
		}
	}


	/**
	 * 删除代办
	 *
	 * @throws Exception
	 */
	public void deleteTodo(String taskId, String userName) throws Exception {
		WebServiceConfig cfg = WebServiceConfig.getInstance();
		ISysNotifyTodoWebService service = (ISysNotifyTodoWebService) callService(cfg.getAddress(), cfg.getServiceClass());
		NotifyTodoRemoveContext context = new NotifyTodoRemoveContext();
		context.setAppName(APP_NAME);
		context.setModelName(MODEL_NAME);
		context.setModelId(taskId);
		context.setOptType(2);
		//        context.setType(1);
		context.setTargets(getJsonString(userName));
		context.setKey(taskId + "");
		context.setType(1);
		NotifyTodoAppResult result = service.deleteTodo(context);

		System.out.println("删除代办setAppName=" + APP_NAME);
		System.out.println("删除代办setModelName=" + MODEL_NAME);
		System.out.println("删除代办setModelId=" + taskId);
		System.out.println("删除代办setOptType=" + 2);
		System.out.println("删除代办setTargets=" + getJsonString(userName));
		System.out.println("删除代办setKey=" + taskId + "");
		System.out.println("删除代办setType=" + 1);

		if (result != null) {
			System.out.println(result.getReturnState() + "***********" + result.getMessage());
			if (result.getReturnState() == 2) {
				System.out.println("删除代办++++++++++++++++++++" + JSON.toJSONString(result));
			}
		}
	}


	/**
	 * 办理通过或退回后
	 * 当前审批人数据设置为已办，删除代办
	 *
	 * @param cirid
	 * @param userName
	 */
	public void blProcess(String cirid, String userName) {
		try {
			this.setTodoDone(cirid, userName);
			this.deleteTodo(cirid, userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调用服务，生成客户端的服务代理
	 *
	 * @param address      WebService的URL
	 * @param serviceClass 服务接口全名
	 * @return 服务代理对象
	 * @throws Exception
	 */
	public static Object callService(String address, Class serviceClass) throws Exception {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// 记录入站消息
		factory.getInInterceptors().add(new LoggingInInterceptor());
		// 记录出站消息
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		// 添加消息头验证信息。如果服务端要求验证用户密码，请加入此段代码
		// factory.getOutInterceptors().add(new AddSoapHeader());
		factory.setServiceClass(serviceClass);
		factory.setAddress(address);
		// 使用MTOM编码处理消息。如果需要在消息中传输文档附件等二进制内容，请加入此段代码
		// Map props = new HashMap();
		// props.put("mtom-enabled", Boolean.TRUE);
		// factory.setProperties(props);
		// 创建服务代理并返回
		return factory.create();
	}


	public static void main(String[] args) {


		try {
			WebServiceConfig cfg = WebServiceConfig.getInstance();
			ISysNotifyTodoWebService service = (ISysNotifyTodoWebService) callService(cfg.getAddress(), cfg.getServiceClass());
			NotifyTodoRemoveContext context = new NotifyTodoRemoveContext();
			context.setAppName(APP_NAME);
			context.setModelName(MODEL_NAME);
			context.setModelId("12122");
			context.setOptType(2);
			JSONObject loginInfo = new JSONObject();
			loginInfo.put("LoginName", "zhengqj");

			context.setTargets(loginInfo.toJSONString());
			context.setKey("12122");
			context.setType(1);
			NotifyTodoAppResult result;

			result = service.setTodoDone(context);
			System.out.println("设为已办setAppName=" + APP_NAME);
			System.out.println("设为已办setModelName=" + MODEL_NAME);
			System.out.println("设为已办setModelId=" + 12122);
			System.out.println("设为已办setOptType=" + 2);
			System.out.println("设为已办setTargets=" + 12122);
			System.out.println("设为已办setKey=" + 12122 + "");
			System.out.println("设为已办setType=" + 1);


			if (result != null) {
				System.out.println(result.getReturnState() + "***********" + result.getMessage());
				if (result.getReturnState() == 2) {
					System.out.println("设为已办++++++++++++++++++++ " + JSON.toJSONString(result));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 我的代办（催办消息）
	 */
	public void sendMyTodoMsgReminder(String token, TblSystemRefReminder reminder, String titleName) {
		try {
			if (reminder == null) {
				return;
			}

			//提交人
			String subStaffId = reminder.getCreator().toString();
			TblStaff subStaff = tblStaffMapper.getById(subStaffId);

			//被下发人
			TblStaff nextStaff = tblStaffMapper.getById(reminder.getReminderStaffId().toString());
			if (nextStaff == null) {

				//发送待办
				TblNbsjMessagetodo tod = new TblNbsjMessagetodo();
				tod.setModelId(reminder.getId().toString());//防止多数据传递
				tod.setKey(reminder.getId().toString());//记录表单id
				tod.setAppName(APP_NAME);
				tod.setModelName(MODEL_NAME);

				//流程标题
				tod.setSubject(subStaff.getRealname() + "发起的催办消息【" + titleName + "】");

				tod.setType(2);//设置为通知类待办
				tod.setTargets(subStaff.getUsername());
				tod.setCreateName(subStaff.getUsername());

				String url1 = getCallbackUrlReminder(subStaff.getUsername());
				//                url1 = url1 + "&cyid=" + tblMyTask.getCirid() + "&taskid=" + tblMyTask.getFromid() + "&cytype=" + cir.getCytype() + "&username=" + subStaff.getUsername()+"&istype=2";

				tod.setLink(url1);
				tod.setCreateTime(new Date());
				todoSend(tod);

				return;
			}

			TblNbsjMessagetodo tod = new TblNbsjMessagetodo();
			tod.setModelId(reminder.getId().toString());//防止多数据传递
			tod.setKey(reminder.getId().toString());//记录表单id
			tod.setAppName(APP_NAME);
			tod.setModelName(MODEL_NAME);

			//流程标题
			tod.setSubject(subStaff.getRealname() + "发起的催办消息【" + titleName + "】");

			tod.setType(1);
			tod.setTargets(nextStaff.getUsername());
			tod.setCreateName(subStaff.getUsername());

			String url1 = getCallbackUrlReminder(nextStaff.getUsername());
			//            url1 = url1 + "&cyid=" + tblMyTask.getCirid() + "&taskid=" + tblMyTask.getFromid() + "&cytype=" + cir.getCytype() + "&username=" + staff.getUsername();

			tod.setLink(url1);
			tod.setCreateTime(new Date());
			todoSend(tod);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 我的代办（催办消息）
	 */
	public void sendMyTodoMsgReminderV1(String token, TblSystemRefReminderV reminder, String titleName) {
		try {
			if (reminder == null) {
				return;
			}

			//提交人
			String subStaffId = reminder.getCreator().toString();
			TblStaff subStaff = tblStaffMapper.getById(subStaffId);

			//被下发人
			TblStaff nextStaff = tblStaffMapper.getById(reminder.getReminderStaffId().toString());
			if (nextStaff == null) {

				//发送待办
				TblNbsjMessagetodo tod = new TblNbsjMessagetodo();
				tod.setModelId(reminder.getId().toString());//防止多数据传递
				tod.setKey(reminder.getId().toString());//记录表单id
				tod.setAppName(APP_NAME);
				tod.setModelName(MODEL_NAME);

				//流程标题
				tod.setSubject(subStaff.getRealname() + "发起的催办消息【" + titleName + "】");

				tod.setType(2);//设置为通知类待办
				tod.setTargets(subStaff.getUsername());
				tod.setCreateName(subStaff.getUsername());

				String url1 = getCallbackUrlReminder(subStaff.getUsername());
				//                url1 = url1 + "&cyid=" + tblMyTask.getCirid() + "&taskid=" + tblMyTask.getFromid() + "&cytype=" + cir.getCytype() + "&username=" + subStaff.getUsername()+"&istype=2";

				tod.setLink(url1);
				tod.setCreateTime(new Date());
				todoSend(tod);

				return;
			}

			TblNbsjMessagetodo tod = new TblNbsjMessagetodo();
			tod.setModelId(reminder.getId().toString());//防止多数据传递
			tod.setKey(reminder.getId().toString());//记录表单id
			tod.setAppName(APP_NAME);
			tod.setModelName(MODEL_NAME);

			//流程标题
			tod.setSubject(subStaff.getRealname() + "发起的催办消息【" + titleName + "】");

			tod.setType(1);
			tod.setTargets(nextStaff.getUsername());
			tod.setCreateName(subStaff.getUsername());

			String url1 = getCallbackUrlReminder(nextStaff.getUsername());
			//            url1 = url1 + "&cyid=" + tblMyTask.getCirid() + "&taskid=" + tblMyTask.getFromid() + "&cytype=" + cir.getCytype() + "&username=" + staff.getUsername();

			tod.setLink(url1);
			tod.setCreateTime(new Date());
			todoSend(tod);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
