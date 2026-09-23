package com.management.accountant.aop;

import com.vip.vjtools.vjkit.io.IOUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


	private static final String BELONG_GROUP_NAME = "belongGroup";
	private static final String WORK_UNIT_NAME = "workUnit";
	private static final String CREATOR_NAME = "creator";

	private String jsonbody = StringUtils.EMPTY;
	private String belongGroup;
	private String workUnit;
	private String creator;

	public MyHttpServletRequestWrapper(HttpServletRequest request, String belongGroup, String workUnit, String creator) {
		super(request);
		this.belongGroup = belongGroup;
		this.workUnit = workUnit;
		this.creator = creator;

		try (InputStream inputStream = request.getInputStream()) {
			jsonbody = IOUtil.toString(inputStream);
		} catch (IOException e) {
			log.error("", e);
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


	//	@Override
	//	public Map<String, String[]> getParameterMap() {
	//		Map<String, String[]> result = super.getParameterMap();
	//		if (result.containsKey(OPERATOR_PARAM_NAME)) {
	//			result.put(OPERATOR_PARAM_NAME, new String[]{operator});
	//		}
	//
	//		return result;
	//	}

	//	@Override
	//	public String[] getParameterValues(String name) {
	//		if (OPERATOR_PARAM_NAME.equals(name)) {
	//			return new String[]{operator};
	//		}
	//		return super.getParameterValues(name);
	//	}
}
