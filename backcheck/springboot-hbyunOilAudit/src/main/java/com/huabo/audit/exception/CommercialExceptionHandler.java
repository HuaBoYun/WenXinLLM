package com.huabo.audit.exception;

import com.hbfk.util.JsonBean;
import com.huabo.audit.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
* @description  异常处理器
* @author   lyz
* @date 2022/4/14 10:58
*/
@Slf4j
@RestControllerAdvice
public class CommercialExceptionHandler {


    @ExceptionHandler(Exception.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail(e.getMessage());
    }
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CommercialException.class)
    public R handleMedicineException(CommercialException e) {
        log.error(e.getMessage(), e);
        return R.fail(e.getErrorCode(), e.getMessage());
    }

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<JsonBean> serviceException(HttpServletRequest request, ServiceException ex) {
		log.warn("业务异常 msg={}", ex.getMsg(), ex);
		return new ResponseEntity<>(new JsonBean(ex.getCode(), ex.getMsg(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<JsonBean> methodArgumentNotValidException(HttpServletRequest request, HttpMessageNotReadableException ex) {
		log.warn("参数类型不匹配 msg={}", ex.getMessage());
		return new ResponseEntity<>(new JsonBean(400, ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
