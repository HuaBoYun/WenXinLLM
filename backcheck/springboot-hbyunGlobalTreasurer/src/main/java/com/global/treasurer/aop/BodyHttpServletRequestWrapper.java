package com.global.treasurer.aop;

import com.vip.vjtools.vjkit.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * wuqian
 */
public class BodyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = LoggerFactory.getLogger(BodyHttpServletRequestWrapper.class);

	private String jsonbody = StringUtils.EMPTY;

	public BodyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		String contextType = request.getHeader("Content-Type");
		// 跳过 multipart/form-data 格式，直接使用原始请求
		// 这样可以让 @FlexibleRequestBody 解析器正确处理 FormData
		if (contextType != null && contextType.contains("multipart/form-data")) {
			log.debug("跳过 multipart/form-data 请求，使用原始 HttpServletRequest");
			return; // 不读取请求体，让 @FlexibleRequestBody 处理
		}

		if (StringUtils.containsIgnoreCase(contextType, MediaType.APPLICATION_JSON_VALUE) || StringUtils
				.containsIgnoreCase(contextType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
			try (InputStream inputStream = request.getInputStream()) {
				jsonbody = IOUtil.toString(inputStream);
			} catch (IOException e) {
				log.error("", e);
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

	public String getJsonbody() {
		return jsonbody;
	}

	// ---- form-urlencoded 参数解析 ----
	private Map<String, String[]> formParams = null;

	private Map<String, String[]> parseFormParams() {
		if (formParams != null) {
			return formParams;
		}
		formParams = new LinkedHashMap<>();
		String contentType = getHeader("Content-Type");
		if (StringUtils.containsIgnoreCase(contentType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				&& StringUtils.isNotEmpty(jsonbody)) {
			for (String pair : jsonbody.split("&")) {
				if (pair.isEmpty()) continue;
				String[] kv = pair.split("=", 2);
				try {
					String key = URLDecoder.decode(kv[0], "UTF-8");
					String value = kv.length == 2 ? URLDecoder.decode(kv[1], "UTF-8") : "";
					formParams.merge(key, new String[]{value}, (a, b) -> {
						String[] merged = Arrays.copyOf(a, a.length + 1);
						merged[a.length] = b[0];
						return merged;
					});
				} catch (Exception ignored) {
				}
			}
		}
		return formParams;
	}

	@Override
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		return (values != null && values.length > 0) ? values[0] : super.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parsed = parseFormParams();
		if (!parsed.isEmpty()) {
			return parsed;
		}
		return super.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		Map<String, String[]> parsed = parseFormParams();
		return parsed.containsKey(name) ? parsed.get(name) : super.getParameterValues(name);
	}

}
