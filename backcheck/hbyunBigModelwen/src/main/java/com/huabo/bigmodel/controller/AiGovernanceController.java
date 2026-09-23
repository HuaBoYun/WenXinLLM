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
 * AI 数据治理控制器 - 数据梳理/资产目录/数据标准/血缘等治理场景
 * <p>
 * 与 {@link AiConsultController}（AI 咨询）、{@link AiChatController}（AI 建模）结构对称，
 * 使用独立的请求入口，底层复用同一个 {@link AiChatService}，
 * 仅通过注入「AI 数据治理专属系统提示词」来区分业务能力，
 * 避免触发建模场景的 SQL 建表/数据操作工具逻辑。
 * 历史记录复用 ChatHistory 接口，前端通过 type='governance' 区分。
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai")
@Tag(name = "AI数据治理接口", description = "星光问心AI大模型数据治理接口，支持联网搜索和深度思考")
public class AiGovernanceController {

    private final AiChatService aiChatService;
    private final LlmProperties llmProperties;

    public AiGovernanceController(AiChatService aiChatService, LlmProperties llmProperties) {
        this.aiChatService = aiChatService;
        this.llmProperties = llmProperties;
    }

    /**
     * 默认的 AI 数据治理系统提示词（当配置项 llm.governance-system-prompt 为空时使用）
     */
    private static final String DEFAULT_GOVERNANCE_SYSTEM_PROMPT =
            "你是星光问心AI大模型，是华博云的数据治理专家顾问。\n" +
            "你的职责是为用户提供专业、体系化、可落地的企业数据治理服务，覆盖以下方向：\n" +
            "1. 数据梳理与资产目录：数据源盘点、元数据采集、表/字段级资产编目、主题域划分\n" +
            "2. 数据标准：指标标准、代码标准、命名规范、数据元与字典管理\n" +
            "3. 数据质量：完整性/一致性/准确性/及时性/唯一性/有效性六维度规则设计与检查评估\n" +
            "4. 数据安全：敏感数据识别与分级分类、脱敏策略、权限管控建议\n" +
            "5. 主数据管理：主数据识别、标准建模、黄金记录与分发共享\n" +
            "6. 数据血缘：表级/字段级血缘关系梳理、影响分析与溯源\n" +
            "7. 标准落标：数据元与业务字段的映射矩阵、落标差距分析与整改建议\n" +
            "8. 治理运营：治理组织、制度流程、考核评估与长效运营机制建议\n\n" +
            "回答要求：\n" +
            "- 结论先行，条理清晰，必要时按治理主题分点或用表格呈现\n" +
            "- 结合用户提供的上下文（数据源、字典、治理工件等）作答，保持口径一致、可追溯\n" +
            "- 产出文档使用规范 Markdown；涉及血缘/流程/架构关系时优先使用 mermaid 语法绘制\n" +
            "- 语言专业、客观、可落地，避免空话套话\n\n" +
            "重要约束：\n" +
            "- 你是数据治理顾问，专注于梳理、评估、建议与文档产出，不要主动执行任何数据库操作\n" +
            "- 不要生成 CREATE TABLE / INSERT / UPDATE 等 SQL 建表或数据变更语句\n" +
            "- 不要主动建议建立数据模型或执行 SQL，除非用户明确进入开发建模场景";

    /**
     * AI 数据治理文档生成通用格式（固化能力，仅本治理接口生效）。
     * 该指令为「条件性触发」：仅当用户要求生成资产目录 / 数据标准 / 血缘分析报告 /
     * 质量评估报告 / 落标映射矩阵 / 治理规划等正式文档时套用，普通问答不受影响。
     * 无论使用默认还是用户自定义系统提示词，均会追加此格式规范。
     */
    private static final String DOC_FORMAT_INSTRUCTION =
            "\n\n【治理文档生成格式规范】\n" +
            "当用户要求生成「资产目录 / 数据标准 / 血缘分析报告 / 数据质量评估报告 / 落标映射矩阵 / 数据治理规划」等正式文档时，" +
            "按治理对象组织内容，遵循“现状梳理 → 标准对标 → 差距分析 → 整改建议 → 运营闭环”的主线，" +
            "并贯穿“源头治理、过程管控、持续运营”的治理思想，按以下结构输出规范的 Markdown 文档：\n" +
            "1. 文档概述：治理背景与目标、治理范围（数据源/系统/主题域）、文档结论与建议摘要。\n" +
            "2. 现状梳理：以“加粗小标题 + 段落说明/表格”的清单形式列出资产、标准、质量、安全等维度的现状与问题。\n" +
            "3. 对标分析：逐项对照数据标准或监管要求，给出差距与风险等级（可用表格呈现）。\n" +
            "4. 治理方案：针对每项差距给出整改措施、责任建议与优先级，资产/血缘/映射类内容可用表格或 mermaid 呈现。\n" +
            "5. 运营闭环：持续监控指标、考核机制与迭代计划。\n" +
            "格式要求：全程使用规范 Markdown（多级标题、表格、有序/无序列表）；涉及血缘链路、治理流程、架构关系时" +
            "优先使用 mermaid 代码块（graph LR/TB、flowchart 等）；问题与措施一一对应，语言专业、客观、可落地。" +
            "若用户未要求生成正式文档，则按正常问答方式作答，无需套用以上结构。";

    /**
     * 数据梳理向导模式指令（wizardGuide=true 时追加，仅 AI 数据治理页会开启）。
     * 要求模型按向导固定步骤顺序引导用户逐步治理，步骤完成状态由前端随请求携带。
     */
    private static final String WIZARD_GUIDE_INSTRUCTION =
            "\n\n【数据梳理向导模式】\n" +
            "用户已开启数据梳理向导模式，你必须按照数据梳理向导的固定步骤顺序，引导用户逐步完成数据梳理：\n" +
            "① 数据资源管理（连接数据源并扫描元数据）→ ② 数据标准管理（元数据分类体系、标准清单与质量监控）→ " +
            "③ 数据血缘管理（数据地图与血缘图谱）→ ④ 主数据治理管理（主数据识别与七步治理路径）→ " +
            "⑤ 维度数据治理（维度定义、编码与层级统一）→ ⑥ 业务数据治理（核心业务表逐字段清洗治理）→ " +
            "⑦ 治理报告（汇总各阶段产出，形成数据治理报告与数据资产目录）。\n" +
            "请求中的 wizardProgress 为各步骤当前完成状态 JSON（含 key/name/complete）。\n" +
            "回答要求：\n" +
            "- 先根据进度判断当前应聚焦的步骤（第一个未完成的步骤），并在回答开头简要汇报整体进度（如 3/7 步）\n" +
            "- 围绕该步骤给出本次梳理目标、具体操作路径、产出要求，收尾时预告下一个步骤名称，引导用户逐步推进\n" +
            "- 已完成的步骤不要重复展开，仅在用户主动询问时简要回顾\n" +
            "- 若用户表达「我要进行数据治理 / 开始数据治理 / 开始梳理」等总体性意图而未指定具体步骤"
            + "（本条优先级最高，压过按进度聚焦的默认规则）：回答开头必须按 1/7 汇报进度并从第一步「数据资源管理」讲起"
            + "（页面右侧已自动打开该步骤面板），不要以「当前处于第 N/7 步」开头，也不要直接聚焦后续未完成步骤；"
            + "先带用户核对该步骤状态：未完成时说明需要连接数据源并扫描元数据，"
            + "已完成时用一两句话确认已有成果（数据源与已扫描表数），"
            + "并询问用户是需要补充扫描还是进入第二步数据标准管理，未经确认不要展开后续步骤的详细方案\n" +
            "- 严格按上述顺序推进，不主动跳步；用户明确要求处理某步骤时，先说明当前进度再按用户要求展开\n" +
            "- 回复保持简洁可执行，配合页面右侧向导的步骤判定规则（完成以「归档产出/确认动作」为准）给出指引\n";

    /**
     * 流式数据治理接口
     */
    @PostMapping(value = "/governance/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式数据治理对话", description = "发送数据治理消息并获取流式响应，支持联网搜索和深度思考")
    public Flux<String> governanceStream(@RequestBody AiChatRequest request) {
        applyGovernanceSystemPrompt(request);
        // 向导顺序梳理模式：仅 AI 数据治理页传 wizardGuide=true 时追加引导指令，
        // 其他调用方（不传该字段）走原有提示词，请求与响应内容均不受影响
        if (Boolean.TRUE.equals(request.getWizardGuide())) {
            applyWizardGuidePrompt(request);
        }
        log.info("收到流式数据治理请求: sessionId={}, enableThinking={}, enableWebSearch={}, wizardGuide={}",
                request.getSessionId(), request.getEnableThinking(), request.getEnableWebSearch(),
                request.getWizardGuide());
        return aiChatService.chatStream(request);
    }

    /**
     * 普通数据治理接口（非流式）
     */
    @PostMapping("/governance")
    @Operation(summary = "数据治理对话", description = "发送数据治理消息并获取完整响应")
    public ResponseEntity<AiChatResponse> governance(@RequestBody AiChatRequest request) {
        applyGovernanceSystemPrompt(request);
        log.info("收到数据治理请求: sessionId={}, enableThinking={}, enableWebSearch={}",
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
            log.error("数据治理对话失败", e);
            return ResponseEntity.ok(AiChatResponse.error(e.getMessage()));
        }
    }

    /**
     * 为请求注入 AI 数据治理专属系统提示词。
     * 基础提示词优先级：调用方传入的 systemPrompt > 配置项 llm.governance-system-prompt > 内置默认。
     * 无论使用哪种基础提示词，都会追加「治理文档生成格式规范」，使该格式能力固化在数据治理接口（仅本控制器生效）。
     */
    private void applyGovernanceSystemPrompt(AiChatRequest request) {
        String basePrompt;
        if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
            // 尊重调用方（用户自定义）提示词作为基础
            basePrompt = request.getSystemPrompt();
        } else {
            String configured = llmProperties.getGovernanceSystemPrompt();
            basePrompt = (configured != null && !configured.isEmpty()) ? configured : DEFAULT_GOVERNANCE_SYSTEM_PROMPT;
        }
        // 文档生成格式为固化能力，始终追加（条件性触发，不影响普通问答）
        request.setSystemPrompt(basePrompt + DOC_FORMAT_INSTRUCTION);
    }

    /**
     * 追加数据梳理向导模式指令（在基础提示词之后拼接，仅流式接口且 wizardGuide=true 时调用）。
     * 步骤进度原样注入，供模型判断当前应聚焦的步骤。
     */
    private void applyWizardGuidePrompt(AiChatRequest request) {
        String progress = request.getWizardProgress();
        String guide = WIZARD_GUIDE_INSTRUCTION +
                ((progress != null && !progress.isEmpty())
                        ? "\n当前向导步骤进度：" + progress
                        : "\n当前向导步骤进度缺失，请从第一步数据资源管理开始引导。");
        request.setSystemPrompt(request.getSystemPrompt() + guide);
    }
}
