package com.huabo.legal.aop;

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

/**
 * wuqian
 */
@Slf4j
@Data
public class BodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private String jsonbody = StringUtils.EMPTY;

	public BodyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		String contextType = request.getHeader("Content-Type");
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


}
