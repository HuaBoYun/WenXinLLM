package com.global.treasurer.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.global.treasurer.util.AuditDealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OperatorFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(OperatorFilter.class);

	private static final String TOKEN = "REDACTED";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//法务管理接口token处理
		if (request.getRequestURI().contains("/api-auth")) {
			TblStaffUtil loginStaff = null;
			String token = request.getHeader(TOKEN);
			//当用户信息异常时
			if (StringUtils.isBlank(token)) {
				JsonBean json = new JsonBean(401, "token不能为空", null);
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Type", "application/json;charset=UTF-8");
				response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
				return;
			} else {
				log.debug("请求token={}", token);
				loginStaff = AuditDealUserToken.parseUserToken(token);
				if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
					JsonBean json = new JsonBean(401, "用户已失效", null);
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-Type", "application/json;charset=UTF-8");
					response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
					return;
				}
			}

			try {
				String contextType = request.getHeader("Content-Type");
				/**
				 * 仅当json 参数才需要转
				 */
				if (StringUtils.containsIgnoreCase(contextType, MediaType.APPLICATION_JSON_VALUE) || StringUtils
						.containsIgnoreCase(contextType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
					MyHttpServletRequestWrapper wrapper = new MyHttpServletRequestWrapper(request, "belongGroup", "workUnit", "creator");

					String jsonbody = wrapper.getJsonbody();
					// 新增或更新操作
					if (StringUtils.isNotBlank(jsonbody)) {
						if (StringUtils.startsWith(jsonbody, "[")) {
							/**
							 * TODO
							 */
							JSONArray objects = JSON.parseArray(jsonbody);
							TblStaffUtil finalLoginStaff = loginStaff;
							objects.forEach(x -> {
								JSONObject obj = (JSONObject) JSONObject.toJSON(x);
								obj.put("belongGroup", finalLoginStaff.getCurrentOrg().getOrgid());
								obj.put("workUnit", finalLoginStaff.getLinkDetp().getOrgid());
								obj.put("creator", finalLoginStaff.getStaffid());
							});
							wrapper.setJsonbody(JSON.toJSONString(objects));
						} else if (StringUtils.startsWith(jsonbody, "{")) {
							JSONObject jsonObject = JSON.parseObject(jsonbody);
							jsonObject.put("belongGroup", loginStaff.getCurrentOrg().getOrgid());
							jsonObject.put("workUnit", loginStaff.getLinkDetp().getOrgid());
							jsonObject.put("creator", loginStaff.getStaffid());
							wrapper.setJsonbody(JSON.toJSONString(jsonObject));
						}
					}
					filterChain.doFilter(wrapper, response);
					return;
				}
			} catch (Exception e) {
				log.error("json转换异常：", e);
				JsonBean json = new JsonBean(400, "请求参数异常", null);
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Type", "application/json;charset=UTF-8");
				response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
