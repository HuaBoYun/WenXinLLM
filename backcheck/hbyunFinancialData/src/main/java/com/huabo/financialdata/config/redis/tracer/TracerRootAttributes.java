package com.huabo.financialdata.config.redis.tracer;


import static com.huabo.financialdata.config.redis.tracer.ThreadLocalProcessTracer.MAKE_ATTRIBUTE_FERRIABLE;

/**
 * @author lee
 * @version 1.0.0
 */

public interface TracerRootAttributes {
    //PID
    String PID = "pid";

    //跟踪入口类型
    String TYPE = "type";

    //应用名称
    String APPLICATION = "application";

    //应用ID（端口号）
    String APPLICATION_INDEX = "applicationIndex";

    //租户ID（用于请求跟踪根据租户过滤）
    String TENANT_ID = "tenantId";

    //应用主机地址
    String HOST = "host";

    //调用端IP
    String REMOTE = "remote";

    //调用端设备类型(pc/ios/android/ipad)
    String DEVICE = "device";

    //请求的URI
    String URI = "uri";

    //业务操作
    String OPERATION = "operation";

    //业务操作唯一识别码
    String OPERATION_CODE = "operationCode";

    //HTTP请求的方法
    String METHOD = "method";

    //调用者服务名称
    String CLOUD_SERVICE_FROM = "cloudServiceFrom";

    //调用者服务ID
    String CLOUD_SERVICE_FROM_INDEX = "cloudServiceFromIndex";

    //请求字节数
    String REQUEST_BYTES_READ = "requestBytesRead";

    //错误消息
    String ERROR = "error";

    //是否为业务错误
    String IS_BUSINESS_ERROR = "isBusinessError";

    //当前用户
    String USER = "user";

    //当前会话ID
    String SESSION_ID = "sessionId";

    //HTTP响应码
    String STATUS = "status";

    //API Response code
    String API_RESPONSE_CODE = "responseCode";

    //响应字节数
    String RESPONSE_BYTES_WRITE = "responseBytesWrite";

    //队列类型(QUEUE/TOPIC)
    String MQ_TARGET_TYPE = "mqTargetType";

    //队列名称
    String MQ_TARGET_NAME = "mqTargetName";

    //队列或主题监听器名称
    String MQ_LISTENER = "listener";

    //源头应用
    String FERRY_DATA_HEAD_SOURCE_SERVICE = MAKE_ATTRIBUTE_FERRIABLE("headService");

    //源头应用实例
    String FERRY_DATA_HEAD_SOURCE_SERVICE_INDEX = MAKE_ATTRIBUTE_FERRIABLE("headServiceIndex");

    //请求源IP
    String REQUEST_SOURCE_IP_ADDR = "requestSourceIpAddr";
}
