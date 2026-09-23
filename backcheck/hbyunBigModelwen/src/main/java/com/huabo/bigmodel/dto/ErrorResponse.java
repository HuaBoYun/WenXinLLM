package com.huabo.bigmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OpenAI 错误响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Error error;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        /**
         * 错误消息
         */
        private String message;

        /**
         * 错误类型
         */
        private String type;

        /**
         * 参数名
         */
        private String param;

        /**
         * 错误代码
         */
        private String code;
    }

    /**
     * 创建错误响应
     */
    public static ErrorResponse of(String message, String type, String code) {
        return ErrorResponse.builder()
                .error(Error.builder()
                        .message(message)
                        .type(type)
                        .code(code)
                        .build())
                .build();
    }

    /**
     * 创建无效请求错误
     */
    public static ErrorResponse invalidRequest(String message) {
        return of(message, "invalid_request_error", "invalid_request");
    }

    /**
     * 创建认证错误
     */
    public static ErrorResponse authenticationError(String message) {
        return of(message, "authentication_error", "invalid_api_key");
    }

    /**
     * 创建服务器错误
     */
    public static ErrorResponse serverError(String message) {
        return of(message, "server_error", "internal_error");
    }
}

