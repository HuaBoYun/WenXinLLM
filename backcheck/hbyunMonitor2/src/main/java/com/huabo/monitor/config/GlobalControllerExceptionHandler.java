package com.huabo.monitor.config;

import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kangjx
 * @createTime 2022/8/19
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public JsonBean handleHttpMessageNotReadableException(MissingServletRequestParameterException ex) {
		logger.error("缺少请求参数,{}", ex.getMessage());
		log.error("缺少请求参数", ex);
		return new JsonBean(400, "缺少必要的参数", null);
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public JsonBean handleTypeMismatchException(NullPointerException ex) {
		logger.error("空指针异常,{}", ex.getMessage());
		log.error("空指针异常", ex);
		return new JsonBean(500, "空指针异常了", null);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public JsonBean handleUnexpectedServer(Exception ex) {
		logger.error("系统异常：", ex);
		log.error("系统异常", ex);
		return new JsonBean(500, "系统异常", null);
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, ServiceException ex) {
		logger.warn("业务异常：", ex);
		log.warn("业务异常 msg={}", ex.getMsg(), ex);
		return new ResponseEntity<>(new JsonBean(ex.getCode(), "业务异常", null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<JsonBean> methodArgumentNotValidException(HttpServletRequest request,
			HttpMessageNotReadableException ex) {
		logger.warn("参数类型不匹配：", ex);
		log.warn("参数类型不匹配 msg={}", ex.getMessage());
		return new ResponseEntity<>(new JsonBean(400, ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

