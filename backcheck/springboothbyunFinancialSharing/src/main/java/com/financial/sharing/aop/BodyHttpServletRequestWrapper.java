package com.financial.sharing.aop;

import com.vip.vjtools.vjkit.io.IOUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * wuqian
 */
@Slf4j
@Data
public class BodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private String jsonbody = StringUtils.EMPTY;

	// 缓存表单参数
	private Map<String, String[]> formParams = null;

	public BodyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		String contextType = request.getHeader("Content-Type");
		if (StringUtils.containsIgnoreCase(contextType, MediaType.APPLICATION_JSON_VALUE) || StringUtils
				.containsIgnoreCase(contextType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
			try (InputStream inputStream = request.getInputStream()) {
				jsonbody = IOUtil.toString(inputStream);

				// 如果是表单提交，解析参数并缓存
				if (StringUtils.containsIgnoreCase(contextType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
					parseFormParameters(jsonbody);
				}
			} catch (IOException e) {
				// 忽略异常
			}
		}
	}

	/**
	 * 解析表单参数
	 */
	private void parseFormParameters(String body) {
		if (StringUtils.isBlank(body)) {
			return;
		}

		formParams = new HashMap<>();
		String[] pairs = body.split("&");
		for (String pair : pairs) {
			String[] keyValue = pair.split("=", 2);
			if (keyValue.length == 2) {
				try {
					String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
					String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");

					// 处理多值参数
					if (formParams.containsKey(key)) {
						String[] existingValues = formParams.get(key);
						String[] newValues = new String[existingValues.length + 1];
						System.arraycopy(existingValues, 0, newValues, 0, existingValues.length);
						newValues[existingValues.length] = value;
						formParams.put(key, newValues);
					} else {
						formParams.put(key, new String[]{value});
					}
				} catch (UnsupportedEncodingException e) {
					log.error("解析表单参数失败", e);
				}
			}
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonbody.getBytes(StandardCharsets.UTF_8));
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	@Override
	public String getParameter(String name) {
		// 如果有缓存的表单参数，从缓存中获取
		if (formParams != null && formParams.containsKey(name)) {
			String[] values = formParams.get(name);
			return values != null && values.length > 0 ? values[0] : null;
		}
		// 否则调用父类方法
		return super.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// 如果有缓存的表单参数，返回缓存
		if (formParams != null) {
			return Collections.unmodifiableMap(formParams);
		}
		// 否则调用父类方法
		return super.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// 如果有缓存的表单参数，返回缓存的参数名
		if (formParams != null) {
			return Collections.enumeration(formParams.keySet());
		}
		// 否则调用父类方法
		return super.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		// 如果有缓存的表单参数，从缓存中获取
		if (formParams != null && formParams.containsKey(name)) {
			return formParams.get(name);
		}
		// 否则调用父类方法
		return super.getParameterValues(name);
	}


}
