package com.global.treasurer.exception;

import com.hbfk.util.JsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 全局异常处理
 * wuqian
 */
@RestControllerAdvice
public class GlobalTreasurerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalTreasurerExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, Exception ex) {
		log.error("系统异常 msg={}", ex.getMessage(), ex);
		return new ResponseEntity<>(new JsonBean(500, "服务器内部错误!", null), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, ServiceException ex) {
		log.warn("业务异常 msg={}", ex.getMessage(), ex);
		return new ResponseEntity<>(new JsonBean(ex.getCode(), ex.getMessage(), null), HttpStatus.OK);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<JsonBean> methodArgumentNotValidException(HttpServletRequest request, IOException ex) {
		log.error("流异常 msg={}", ex.getMessage(), ex);
		return new ResponseEntity<>(new JsonBean(500, "服务器错误", null), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<JsonBean> methodArgumentNotValidException(HttpServletRequest request, HttpMessageNotReadableException ex) {
		log.error("参数类型不匹配 msg={}", ex.getMessage(), ex);
		return new ResponseEntity<>(new JsonBean(400, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
