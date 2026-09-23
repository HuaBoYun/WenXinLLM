package com.huabo.system.service;

import java.io.Closeable;
import java.io.InputStream;

/**
 * WenxinAgent SSE 流式响应上下文。
 *
 * <p>持有底层 HTTP 连接和输入流。Controller 拿到本对象后，
 * 逐行读取 {@link #getInputStream()} 并写出到客户端，
 * 完成或异常时必须调用 {@link #close()} 释放底层连接。</p>
 */
public interface WenxinAgentStreamContext extends Closeable {

    /** 上游响应状态码，例如 200 / 400 / 401。 */
    int getStatusCode();

    /** 上游响应 Content-Type，例如 text/event-stream。 */
    String getContentType();

    /** SSE 字节流，调用方按行读取并转发。 */
    InputStream getInputStream();

    @Override
    void close();
}
