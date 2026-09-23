package com.huabo.system.service;

import java.util.Map;

/**
 * WenxinAgent 智能体平台客户端。
 *
 * <p>封装对外部 WenxinAgent 控制台 API（/console/api/**）与服务 API（/v1/**）的调用，
 * 屏蔽底层 HTTP 细节，向上游 Controller 提供面向业务的方法。</p>
 *
 * <p>设计约束：</p>
 * <ul>
 *   <li>所有控制台调用要求 Authorization: Bearer {wenxinAgentAccessToken}，
 *       由前端通过 SSO 拿到后透传；本类只负责 HTTP 转发，不处理凭证缓存。</li>
 *   <li>对话调用使用应用专属 app-xxx API Key，长连接 SSE 流式响应。</li>
 *   <li>SSO 接口 (/console/api/sso/token-exchange) 由 WenxinAgent 端反向校验
 *       华博云 token，需要保证调用方 host 在 WenxinAgent 端配置的白名单内。</li>
 * </ul>
 *
 * <p><b>WenxinAgent 5 种 App Mode 对应的 Console 端点（重要）：</b></p>
 * <table>
 *   <tr><th>Mode</th><th>Console Endpoint</th></tr>
 *   <tr><td>chat / agent-chat</td><td>POST /apps/{id}/chat-messages</td></tr>
 *   <tr><td>advanced-chat (Chatflow)</td><td>POST /apps/{id}/advanced-chat/workflows/draft/run</td></tr>
 *   <tr><td>workflow</td><td>POST /apps/{id}/workflows/draft/run</td></tr>
 *   <tr><td>completion</td><td>POST /apps/{id}/completion-messages</td></tr>
 * </table>
 * <p>每个端点都有 mode 强校验（{@code @get_app_model(mode=[...])}），用错端点会被
 * WenxinAgent 直接 404，因此 Controller 层必须先 {@link #getApp} 拿到 app.mode 再分派。</p>
 */
public interface WenxinAgentClient {

    /** 使用华博云用户 token 完成 SSO 登录，获取 WenxinAgent 自身的 JWT 凭证。 */
    Map<String, Object> ssoTokenExchange(String hbyunToken);

    /** 创建一个 WenxinAgent 应用。 */
    Map<String, Object> createApp(String wenxinAgentAccessToken, String name, String description, String mode);

    /** 为指定应用创建一把新的 API Key。 */
    Map<String, Object> createApiKey(String wenxinAgentAccessToken, String appId);

    /** 列出指定应用已有的 API Key。 */
    Map<String, Object> listApiKeys(String wenxinAgentAccessToken, String appId);

    /** 用应用专属 API Key 调用对话 SSE 流式接口，得到底层流读取上下文。 */
    WenxinAgentStreamContext openChatStream(String appApiKey, Map<String, Object> payload);

    /**
     * 通过 Console API 获取应用详情（含 model_config）。
     *
     * <p>当 nginx 未暴露 service API（/v1/*）时，对话只能走 Console DEBUGGER 端点，
     * 该端点要求请求体里带应用现有的 model_config，所以需要先 GET。</p>
     */
    Map<String, Object> getApp(String wenxinAgentAccessToken, String appId);

    /**
     * 用 WenxinAgent access_token 调用 Console DEBUGGER 模式的聊天 SSE 流式接口。
     *
     * <p>仅适用于 mode=chat 或 agent-chat 的应用。其他 mode 调此接口会被 WenxinAgent 404。</p>
     *
     * @param wenxinAgentAccessToken WenxinAgent 用户 access_token（SSO 换取）
     * @param appId 应用 ID
     * @param payload 必含字段：query / inputs / model_config / response_mode=streaming /
     *                conversation_id（可空）/ retriever_from（默认 "dev"）
     */
    WenxinAgentStreamContext openConsoleChatStream(String wenxinAgentAccessToken, String appId,
                                            Map<String, Object> payload);

    /**
     * 调用 Chatflow（advanced-chat）草稿运行 SSE 流接口。
     *
     * <p>端点：{@code POST /apps/{id}/advanced-chat/workflows/draft/run}。
     * 仅适用于 mode=advanced-chat 的应用。</p>
     *
     * <p>SSE 事件除了 message/message_end 之外，还会包含 workflow_started /
     * node_started / text_chunk / node_finished / workflow_finished，前端需要兼容。</p>
     *
     * @param payload 必含：query / inputs / conversation_id / parent_message_id / files
     *                （不需要 model_config）
     */
    WenxinAgentStreamContext openConsoleAdvancedChatStream(String wenxinAgentAccessToken, String appId,
                                                    Map<String, Object> payload);

    /**
     * 调用 Workflow 草稿运行 SSE 流接口。
     *
     * <p>端点：{@code POST /apps/{id}/workflows/draft/run}。
     * 仅适用于 mode=workflow 的应用。无 query 字段，必须传 inputs(dict)。</p>
     *
     * <p>SSE 事件以 workflow_started / node_started / text_chunk / node_finished /
     * workflow_finished 为主，没有 message/message_end 这种 chat 风格的事件。</p>
     *
     * @param payload 必含：inputs (dict，非空)，可选：files
     */
    WenxinAgentStreamContext openConsoleWorkflowStream(String wenxinAgentAccessToken, String appId,
                                                Map<String, Object> payload);

    /**
     * 调用 Completion 单轮文本生成 SSE 流接口。
     *
     * <p>端点：{@code POST /apps/{id}/completion-messages}。
     * 仅适用于 mode=completion 的应用，不支持多轮对话。</p>
     *
     * @param payload 必含：query / inputs / model_config / response_mode=streaming
     */
    WenxinAgentStreamContext openConsoleCompletionStream(String wenxinAgentAccessToken, String appId,
                                                  Map<String, Object> payload);

    /**
     * 获取 workflow / advanced-chat 应用的草稿 graph。
     *
     * <p>端点：{@code GET /apps/{id}/workflows/draft}。返回结构含 {@code graph}
     * (dict) 和 {@code features} (dict)；开始节点变量定义在
     * {@code graph.nodes[*].data.variables}（其中 node.data.type=='start'）。</p>
     */
    Map<String, Object> getWorkflowDraft(String wenxinAgentAccessToken, String appId);

    /**
     * 上传文件到 WenxinAgent Console（multipart）。
     *
     * <p>端点：{@code POST /console/api/files/upload}。返回的 FileResponse 至少
     * 包含 {@code id, name, size, mime_type, extension}。返回的 {@code id} 可作为
     * workflow run 的 files 参数中的 {@code upload_file_id} 使用。</p>
     *
     * @param fileBytes 文件二进制内容
     * @param filename  原始文件名（含扩展名）
     * @param mimeType  MIME 类型（如 application/pdf, image/png）
     */
    Map<String, Object> uploadFile(String wenxinAgentAccessToken, byte[] fileBytes,
                                   String filename, String mimeType);

    /**
     * 更新应用的 model-config（覆盖式）。
     *
     * <p>调用 {@code POST /console/api/apps/{appId}/model-config}，
     * 将完整的 model_config 字典写回 WenxinAgent。注意该端点只支持
     * chat / agent-chat / completion 三种模式。</p>
     *
     * @param wenxinAgentAccessToken WenxinAgent 控制台 access_token
     * @param appId           应用 ID
     * @param modelConfig     完整的 model_config 字典（不是部分更新）
     * @return WenxinAgent 返回的 {"result": "success"}
     */
    Map<String, Object> updateModelConfig(String wenxinAgentAccessToken, String appId,
                                          Map<String, Object> modelConfig);
}
