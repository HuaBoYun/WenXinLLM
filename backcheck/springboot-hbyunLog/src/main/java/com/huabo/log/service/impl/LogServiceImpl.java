package com.huabo.log.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.log.db.entity.UserLoginLog;
import com.huabo.log.db.entity.UserRequestLog;
import com.huabo.log.db.mapper.UserLoginLogMapper;
import com.huabo.log.db.service.UserLoginLogService;
import com.huabo.log.db.service.UserRequestLogService;
import com.huabo.log.service.LogService;
import com.huabo.log.vo.LogPageReq;
import com.huabo.log.vo.OperationLog;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final UserRequestLogService userRequestLogService;
    private final UserLoginLogService userLoginLogService;
    
    @Resource
    private UserProvider userProvider;
    @Resource
    private UserLoginLogMapper logmapper;

    @Override
    public void saveBusLog(OperationLog operationLog) {
        userRequestLogService.save(new UserRequestLog().getUserRequestLogByOperationLog(operationLog));
    }

    @Override
    public Page<UserRequestLog> list(LogPageReq req, boolean errorFlag) {
    	
        
        Page<UserRequestLog> page = new Page<>(req.getCurrentPage(), req.getPageSize());
        String userName = req.getUserName();
        String businessModule = req.getBusinessModule();
        Date endTime = new Date();
        if (req.getRequestTimeEnd() != null) {
            endTime = req.getRequestTimeEnd();
        }
       
        LambdaQueryWrapper<UserRequestLog> logW = new LambdaQueryWrapper<>();
        logW.select(UserRequestLog::getId, UserRequestLog::getUserId,
                UserRequestLog::getUsername, UserRequestLog::getUserAccount,
                UserRequestLog::getErrorFlag, UserRequestLog::getError,
                UserRequestLog::getModule, UserRequestLog::getIp, UserRequestLog::getRequestTime,UserRequestLog::getCreatedAt,
                /*UserRequestLog::getError,*/UserRequestLog::getBusinessModule,UserRequestLog::getSubBusinessModule,UserRequestLog::getActionDescription);
        
        logW.like(StrUtil.isNotEmpty(userName), UserRequestLog::getUsername, userName)
        .like(StrUtil.isNotEmpty(businessModule), UserRequestLog::getBusinessModule, businessModule)
        .between(req.getRequestTimeStart() != null, UserRequestLog::getRequestTime, req.getRequestTimeStart(), endTime);
        if (errorFlag) {
        	logW.eq(UserRequestLog::getErrorFlag, 1);
        }
        try {
        	try {
    			TblStaffUtil staff = userProvider.get();
    			if(staff!=null  && staff.getRoleNames().contains("安全管理员")) {
    				String roleid = logmapper.findbyRoleid();
    				if(roleid!=null && roleid.length()>0) {
    					logW.and(q -> q.inSql(UserRequestLog::getUserId, "SELECT STAFFID from TBL_STAFF WHERE ROLEIDSTRS  LIKE '%"+roleid+"%'"));
    				}
    			}
    			if(staff!=null  && staff.getRoleNames().contains("安全审计员")) {
    				logW.and(q -> q.ne(UserRequestLog::getUserId, staff.getStaffid()));
    				logW.and(q -> q.ne(UserRequestLog::getUsername, staff.getUsername()));
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        logW.orderByDesc(UserRequestLog::getCreatedAt);
        return userRequestLogService.page(page, logW);
    }

    @Override
    public Page<UserRequestLog> listError(LogPageReq req) {
        return list(req,true);
    }

    @Override
    public void export(HttpServletResponse response, LogPageReq req) throws Exception {
    	String userName = req.getUserName();
        String businessModule = req.getBusinessModule();
        Date endTime = new Date();
        if (req.getRequestTimeEnd() != null) {
            endTime = req.getRequestTimeEnd();
        }
        try {
        LambdaQueryWrapper<UserRequestLog> logW = new LambdaQueryWrapper<>();
        logW.select(UserRequestLog::getId, UserRequestLog::getUserId,
                UserRequestLog::getUsername, UserRequestLog::getUserAccount,
                UserRequestLog::getModule, UserRequestLog::getIp, UserRequestLog::getRequestTime,
                UserRequestLog::getError,UserRequestLog::getBusinessModule,UserRequestLog::getSubBusinessModule,UserRequestLog::getActionDescription);
        logW.like(StrUtil.isNotEmpty(userName), UserRequestLog::getUsername, userName)
        	.like(StrUtil.isNotEmpty(businessModule), UserRequestLog::getBusinessModule, businessModule)
        	.between(req.getRequestTimeStart() != null, UserRequestLog::getRequestTime, req.getRequestTimeStart(), endTime)
        	.orderByDesc(UserRequestLog::getCreatedAt);
        
        List<UserRequestLog> list = this.userRequestLogService.list(logW);
        
     // 只包含5个字段的表头
        List<List<String>> head = new ArrayList<List<String>>();
        head.add(Collections.singletonList("用户名称"));
        head.add(Collections.singletonList("劳人号"));
        head.add(Collections.singletonList("请求时间"));
        head.add(Collections.singletonList("业务名称"));
        head.add(Collections.singletonList("子业务名称"));
        head.add(Collections.singletonList("IP"));
        
     // 构建数据
        List<List<Object>> data = list.stream()
        	    .map(log -> Arrays.asList(
        	        (Object) log.getUsername(),
        	        (Object) log.getUserAccount(),
        	        (Object) log.getRequestTime(),
        	        (Object) log.getBusinessModule(),
        	        (Object) log.getSubBusinessModule()+"-"+ (Object) log.getActionDescription().toString().replaceAll("成功","").replaceAll("失败",""),
        	        (Object) log.getIp()
        	    ))
        	    .collect(Collectors.toList());
        		
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("日志信息", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 使用EasyExcel导出Excel文件
        EasyExcel.write(response.getOutputStream())
        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())  // 自适应列宽
        .head(head)
        .sheet("请求日志详情")
        .doWrite(data);
        
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }
    
    @Override
	public void exportError(HttpServletResponse response, LogPageReq req) throws Exception {
    	String userName = req.getUserName();
        String businessModule = req.getBusinessModule();
        Date endTime = new Date();
        if (req.getRequestTimeEnd() != null) {
            endTime = req.getRequestTimeEnd();
        }
        LambdaQueryWrapper<UserRequestLog> logW = new LambdaQueryWrapper<>();
        logW.select(UserRequestLog::getId, UserRequestLog::getUserId,
                UserRequestLog::getUsername, UserRequestLog::getUserAccount,
                UserRequestLog::getModule, UserRequestLog::getIp, UserRequestLog::getRequestTime,
                UserRequestLog::getError,UserRequestLog::getBusinessModule,UserRequestLog::getSubBusinessModule,UserRequestLog::getActionDescription);
        logW.like(StrUtil.isNotEmpty(userName), UserRequestLog::getUsername, userName)
        	.eq(StrUtil.isNotEmpty(businessModule), UserRequestLog::getBusinessModule, businessModule)
        	.eq(UserRequestLog::getErrorFlag, 1)
        	.between(req.getRequestTimeStart() != null, UserRequestLog::getRequestTime, req.getRequestTimeStart(), endTime)
        	.orderByDesc(UserRequestLog::getCreatedAt);
        
        List<UserRequestLog> list = this.userRequestLogService.list(logW);
        
     // 只包含5个字段的表头
        List<List<String>> head = new ArrayList<List<String>>();
        head.add(Collections.singletonList("用户名称"));
        head.add(Collections.singletonList("劳人号"));
        head.add(Collections.singletonList("请求时间"));
        head.add(Collections.singletonList("错误内容"));
        head.add(Collections.singletonList("业务名称"));
        head.add(Collections.singletonList("子业务名称"));
        head.add(Collections.singletonList("IP"));
        
     // 构建数据
        List<List<Object>> data = list.stream()
        	    .map(log -> Arrays.asList(
        	        (Object) log.getUsername(),
        	        (Object) log.getUserAccount(),
        	        (Object) log.getRequestTime(),
        	        (Object) log.getError(),
        	        (Object) log.getBusinessModule(),
        	        (Object) log.getSubBusinessModule()+"-"+ (Object) log.getActionDescription().toString().replaceAll("成功","").replaceAll("失败",""),
        	        (Object) log.getIp()
        	    ))
        	    .collect(Collectors.toList());
        		
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("日志信息", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 使用EasyExcel导出Excel文件
        EasyExcel.write(response.getOutputStream())
        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())  // 自适应列宽
        .head(head)
        .sheet("请求日志详情")
        .doWrite(data);
	}
    

    @Override
    public void delBatch(List<String> ids) {
        userRequestLogService.removeByIds(ids);
    }

    @Override
    public Page<UserLoginLog> listLogin(LogPageReq req) {
        Page<UserLoginLog> page = new Page<>(req.getCurrentPage(), req.getPageSize());
        String userName = req.getUserName();
        String realName = req.getRealName();
        Date endTime = new Date();
        if (req.getRequestTimeEnd() != null) {
            endTime = req.getRequestTimeEnd();
        }
        LambdaQueryWrapper<UserLoginLog> logW = new LambdaQueryWrapper<>();
            logW.like(StrUtil.isNotEmpty(userName), UserLoginLog::getUsername, userName)
            		.like(StrUtil.isNotEmpty(realName), UserLoginLog::getRealname, realName)
                    .between(req.getRequestTimeStart() != null, UserLoginLog::getLoginTime, req.getRequestTimeStart(), endTime);
            
            try {
    			TblStaffUtil staff = userProvider.get();
    			if(staff!=null  && staff.getRoleNames().contains("安全管理员")) {
    				String roleid = logmapper.findbyRoleid();
    				if(roleid!=null && roleid.length()>0) {
    					logW.and(q -> q.inSql(UserLoginLog::getUserId, "SELECT STAFFID from TBL_STAFF WHERE ROLEIDSTRS  LIKE '%"+roleid+"%'"));
    				}
    			}
    			if(staff!=null  && staff.getRoleNames().contains("安全审计员")) {
    				logW.and(q -> q.ne(UserLoginLog::getUserId, staff.getStaffid()));
    				logW.and(q -> q.ne(UserLoginLog::getUsername, staff.getUsername()));
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            
            logW.orderByDesc(UserLoginLog::getCreatedAt);
        return userLoginLogService.page(page, logW);
    }

    @Override
    public void delLogBatch(List<String> ids) {
        userLoginLogService.removeByIds(ids);
    }

	@Override
	public void exportlistLogin(LogPageReq req, HttpServletResponse response) throws Exception {
        String userName = req.getUserName();
        String realName = req.getRealName();
        Date endTime = new Date();
        if (req.getRequestTimeEnd() != null) {
            endTime = req.getRequestTimeEnd();
        }
        LambdaQueryWrapper<UserLoginLog> logW = new LambdaQueryWrapper<>();
        logW.select(UserLoginLog::getId,UserLoginLog::getLoginTime,UserLoginLog::getUsername,
        		UserLoginLog::getIp,UserLoginLog::getLoginDevice,UserLoginLog::getLoginMsg,UserLoginLog::getRealname,UserLoginLog::getLogoutTime);
       
        logW.like(StrUtil.isNotEmpty(userName), UserLoginLog::getUsername, userName)
        			.like(StrUtil.isNotEmpty(realName), UserLoginLog::getRealname, realName)
                    .between(req.getRequestTimeStart() != null, UserLoginLog::getLoginTime, req.getRequestTimeStart(), endTime)
                    .orderByDesc(UserLoginLog::getCreatedAt);
        
        List<UserLoginLog> list = this.userLoginLogService.list(logW);
        // 只包含5个字段的表头
        List<List<String>> head = new ArrayList<List<String>>();
        head.add(Collections.singletonList("用户名称"));
        head.add(Collections.singletonList("劳人号"));
        head.add(Collections.singletonList("登录设备"));
        head.add(Collections.singletonList("登录时间"));
        head.add(Collections.singletonList("登出时间"));
        head.add(Collections.singletonList("ip"));
        head.add(Collections.singletonList("接口信息"));
        
     // 构建数据
        List<List<Object>> data = list.stream()
        	    .map(log -> Arrays.asList(
        	        (Object) log.getRealname(),
        	        (Object) log.getUsername(),
        	        (Object) log.getLoginDevice(),
        	        (Object) log.getLoginTime(),
        	        (Object) log.getLogoutTime(),
        	        (Object) log.getIp(),
        	        (Object) log.getLoginMsg()
        	    ))
        	    .collect(Collectors.toList());
        		
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("日志信息", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 使用EasyExcel导出Excel文件
        EasyExcel.write(response.getOutputStream())
        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())  // 自适应列宽
        .head(head)
        .sheet("请求日志详情")
        .doWrite(data);
        
        
	}

	@Override
	public UserRequestLog detail(long id) throws Exception {
		return this.userRequestLogService.getById(id);
	}


}
