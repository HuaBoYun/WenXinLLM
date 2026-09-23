package com.management.accountant.exception;

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
@RestControllerAdvice(basePackages = "com.management.accountant")
public class ManagementAccountantExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ManagementAccountantExceptionHandler.class);


	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, Exception ex) {
		log.warn("系统异常 msg={}", ex.getMessage(), ex);
		return new ResponseEntity<>(new JsonBean(500, "服务器内部错误!", null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, ServiceException ex) {
		log.warn("业务异常 msg={}", ex.getMsg(), ex);
		return new ResponseEntity<>(new JsonBean(ex.getCode(), ex.getMsg(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<JsonBean> methodArgumentNotValidException(HttpServletRequest request, IOException ex) {
		log.warn("流异常 msg={}", ex.getMessage());
		return new ResponseEntity<>(new JsonBean(500, "服务器错误", null), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<JsonBean> methodArgumentNotValidException(HttpServletRequest request, HttpMessageNotReadableException ex) {
		String msg = ex.getMessage();
		// 提取 Jackson 解析错误中的有用信息
		if (msg != null && msg.contains("Unrecognized field")) {
			// 提取字段名和类名
			String field = "未知字段";
			String entity = "实体类";
			try {
				int fieldStart = msg.indexOf("Unrecognized field \"") + 20;
				int fieldEnd = msg.indexOf("\"", fieldStart);
				if (fieldStart > 20 && fieldEnd > fieldStart) {
					field = msg.substring(fieldStart, fieldEnd);
				}
				int classStart = msg.indexOf("class ") + 6;
				int classEnd = msg.indexOf(")", classStart);
				if (classStart > 6 && classEnd > classStart) {
					String fullClassName = msg.substring(classStart, classEnd);
					entity = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
				}
			} catch (Exception e) {
				// 解析失败时使用默认值
			}
			log.warn("JSON解析错误: 字段 [{}] 在 [{}] 中不存在", field, entity);
			return new ResponseEntity<>(new JsonBean(400, "请求参数错误: 字段 \"" + field + "\" 不被支持，请检查参数名称", null), HttpStatus.BAD_REQUEST);
		} else if (msg != null && msg.contains("JSON parse error")) {
			log.warn("JSON解析错误: {}", msg);
			return new ResponseEntity<>(new JsonBean(400, "请求参数格式错误，请检查JSON格式和字段类型", null), HttpStatus.BAD_REQUEST);
		}
		log.warn("参数类型不匹配 msg={}", ex.getMessage());
		return new ResponseEntity<>(new JsonBean(400, "请求参数错误，请检查参数格式和类型", null), HttpStatus.BAD_REQUEST);
	}
}
