package com.management.accountant.aop;

import com.alibaba.fastjson.JSONObject;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * wuqian
 * 支持请求体重复读取，并将 form-urlencoded 请求自动转为 application/json
 */
@Slf4j
@Data
public class BodyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	private String jsonbody = StringUtils.EMPTY;
	// 标记是否需要将 Content-Type 改写为 application/json
	private boolean rewriteContentType = false;

	public BodyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		String contextType = request.getHeader("Content-Type");
		if (StringUtils.containsIgnoreCase(contextType, MediaType.APPLICATION_JSON_VALUE)) {
			// JSON 请求：直接读取 body
			try (InputStream inputStream = request.getInputStream()) {
				jsonbody = IOUtil.toString(inputStream);
			} catch (IOException e) {
				log.error("读取 JSON body 失败", e);
			}
		} else if (StringUtils.containsIgnoreCase(contextType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
			// form-urlencoded 请求：读取 body，将 key=value&... 转换为 JSON 对象
			try (InputStream inputStream = request.getInputStream()) {
				String formBody = IOUtil.toString(inputStream);
				if (StringUtils.isNotBlank(formBody)) {
					JSONObject jsonObject = new JSONObject();
					String[] pairs = formBody.split("&");
					for (String pair : pairs) {
						int idx = pair.indexOf('=');
						if (idx > 0) {
							String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
							String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
							// 尝试将数字字符串转为数字类型
							try {
								jsonObject.put(key, Long.parseLong(value));
							} catch (NumberFormatException e1) {
								jsonObject.put(key, value);
							}
						}
					}
					jsonbody = jsonObject.toJSONString();
					rewriteContentType = true;
				}
			} catch (IOException e) {
				log.error("读取 form body 失败", e);
			}
		}
	}

	@Override
	public String getContentType() {
		if (rewriteContentType) {
			return MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
		}
		return super.getContentType();
	}

	@Override
	public String getHeader(String name) {
		if (rewriteContentType && "Content-Type".equalsIgnoreCase(name)) {
			return MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
		}
		return super.getHeader(name);
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

}
