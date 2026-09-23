package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.config.LlmProperties;
import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.dto.AiChatResponse;
import com.huabo.bigmodel.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI咨询控制器 - 财经/业务咨询场景
 * <p>
 * 与 {@link AiChatController}（AI 建模）使用独立的请求入口，但底层复用同一个
 * {@link AiChatService}，仅通过注入「AI 咨询专属系统提示词」来区分业务能力，
 * 避免触发建模场景的 SQL 建表/数据操作工具逻辑。
 * 历史记录复用 ChatHistory 接口，前端通过 type='consult' 区分。
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai")
@Tag(name = "AI咨询接口", description = "星光问心AI大模型财经咨询接口，支持联网搜索和深度思考")
public class AiConsultController {

    private final AiChatService aiChatService;
    private final LlmProperties llmProperties;

    public AiConsultController(AiChatService aiChatService, LlmProperties llmProperties) {
        this.aiChatService = aiChatService;
        this.llmProperties = llmProperties;
    }

    /**
     * 默认的 AI 咨询系统提示词（当配置项 llm.consult-system-prompt 为空时使用）
     */
    private static final String DEFAULT_CONSULT_SYSTEM_PROMPT =
            "你是星光问心AI大模型，是华博云的财经领域专家顾问。\n" +
            "你的职责是为用户提供专业、准确、易懂的财经与业务咨询服务，覆盖以下方向：\n" +
            "1. 财务管理：财务分析、预算管理、成本控制、财务报表解读、财务指标分析\n" +
            "2. 国资监管与合规：穿透式监管、内控合规、风险防范、政策法规解读\n" +
            "3. 经营决策：经营分析、投资评估、商业模式、行业趋势研判\n" +
            "4. 通用咨询：对用户提出的财经、管理、业务问题进行专业解答与建议\n\n" +
            "回答要求：\n" +
            "- 结论先行，条理清晰，必要时分点或用表格呈现\n" +
            "- 基于事实与专业逻辑作答，涉及时效性信息时可联网搜索核实\n" +
            "- 语言通俗专业，避免空话套话，给出可落地的建议\n\n" +
            "重要约束：\n" +
            "- 你是咨询顾问，专注于解答与建议，不要主动执行任何数据库操作\n" +
            "- 不要生成 CREATE TABLE / INSERT / UPDATE 等 SQL 建表或数据变更语句\n" +
            "- 不要主动建议建立数据模型或执行 SQL，除非用户明确进入开发建模场景";

    /**
     * AI 咨询文档生成通用格式（固化能力，仅咨询接口生效）。
     * 该指令为「条件性触发」：仅当用户要求生成项目建议书 / 解决方案 / 咨询文档等正式文档时套用，
     * 普通问答不受影响。无论使用默认还是用户自定义系统提示词，均会追加此格式规范。
     */
    private static final String DOC_FORMAT_INSTRUCTION =
            "\n\n【文档生成格式规范】\n" +
            "当用户要求生成「项目建议书 / 解决方案 / 咨询报告 / 分析文档」等正式文档时，" +
            "请遵循“战略目标→现状痛点→必要性→解决方案→效益闭环”的论证主线，" +
            "并贯穿“事前预防、事中控制、事后分析”的闭环管理思想，按以下结构输出规范的 Markdown 文档：\n" +
            "1. 项目概述：用一句话提炼总目标，并列出具体建设目标（项目符号清单）、建设内容、建设周期、主要结论与建议（含分期实施建议）。\n" +
            "2. 现状分析与建设必要性：先写建设背景，再以“加粗小标题 + 段落说明”的清单形式列出 4-6 个具体痛点，然后由痛点推导建设必要性，最后回扣核心需求。\n" +
            "3. 项目建设方案：针对每个痛点给出对应的功能模块，每个模块按“功能价值 → 实现方式”展开；并说明与相关业务系统的集成关系。\n" +
            "4. 效益分析：分“经济效益”“管理效益”两个维度，每条效益单独成点，尽量量化。\n" +
            "格式要求：全程使用规范 Markdown（多级标题、表格、有序/无序列表），痛点与方案一一对应，" +
            "语言专业、客观、可落地。若用户未要求生成正式文档，则按正常咨询方式作答，无需套用以上结构。";

    /**
     * 流式咨询接口
     */
    @PostMapping(value = "/consult/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式咨询", description = "发送咨询消息并获取流式响应，支持联网搜索和深度思考")
    public Flux<String> consultStream(@RequestBody AiChatRequest request) {
        applyConsultSystemPrompt(request);
        log.info("收到流式咨询请求: sessionId={}, enableThinking={}, enableWebSearch={}",
                request.getSessionId(), request.getEnableThinking(), request.getEnableWebSearch());
        return aiChatService.chatStream(request);
    }

    /**
     * 普通咨询接口（非流式）
     */
    @PostMapping("/consult")
    @Operation(summary = "普通咨询", description = "发送咨询消息并获取完整响应")
    public ResponseEntity<AiChatResponse> consult(@RequestBody AiChatRequest request) {
        applyConsultSystemPrompt(request);
        log.info("收到咨询请求: sessionId={}, enableThinking={}, enableWebSearch={}",
                request.getSessionId(), request.getEnableThinking(), request.getEnableWebSearch());
        try {
            String content = aiChatService.chat(request);
            return ResponseEntity.ok(AiChatResponse.builder()
                    .success(true)
                    .content(content)
                    .sessionId(request.getSessionId())
                    .usedThinking(request.getEnableThinking())
                    .usedWebSearch(request.getEnableWebSearch())
                    .build());
        } catch (Exception e) {
            log.error("咨询失败", e);
            return ResponseEntity.ok(AiChatResponse.error(e.getMessage()));
        }
    }

    /**
     * 为请求注入 AI 咨询专属系统提示词。
     * 基础提示词优先级：调用方传入的 systemPrompt > 配置项 llm.consult-system-prompt > 内置默认。
     * 无论使用哪种基础提示词，都会追加「文档生成格式规范」，使该格式能力固化在咨询接口（仅本控制器生效）。
     */
    private void applyConsultSystemPrompt(AiChatRequest request) {
        String basePrompt;
        if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
            // 尊重调用方（用户自定义）提示词作为基础
            basePrompt = request.getSystemPrompt();
        } else {
            String configured = llmProperties.getConsultSystemPrompt();
            basePrompt = (configured != null && !configured.isEmpty()) ? configured : DEFAULT_CONSULT_SYSTEM_PROMPT;
        }
        // 文档生成格式为固化能力，始终追加（条件性触发，不影响普通问答）
        request.setSystemPrompt(basePrompt + DOC_FORMAT_INSTRUCTION);
    }
}
