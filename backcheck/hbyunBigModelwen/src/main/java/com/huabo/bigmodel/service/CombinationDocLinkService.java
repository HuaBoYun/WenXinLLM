package com.huabo.bigmodel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 组合指标-模型文档自动关联服务
 *
 * 机制：chat 会话中 AI 通过工具向 TBL_INDICATOR_COMBINATION 插入组合配置时，
 * 从工具参数中提取 COMBINATION_ID 暂存（按 sessionId）；
 * 前端在回答完成后保存/更新对话历史（ai_chat_history）时，
 * 将该会话的全部组合自动关联到本次历史记录（DOC_ID=历史记录ID），
 * 文档标题取对话中最后一个 <<<HTML5_START:标题>>>（与前端"取最后一个文档渲染"的规则一致）。
 */
@Slf4j
@Service
public class CombinationDocLinkService {

    private static final Pattern COMBINATION_INSERT_PATTERN =
            Pattern.compile("INSERT\\s+INTO\\s+TBL_INDICATOR_COMBINATION[^;]*?VALUES\\s*\\(\\s*'([^']+)'",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final String DOC_TITLE_MARKER = "<<<HTML5_START:";

    private final JdbcTemplate dmJdbcTemplate;

    // sessionId -> 本次会话中创建的组合ID（内存态，重启即失，仅服务当次生成流程）
    private final Map<String, Set<String>> sessionCombinations = new ConcurrentHashMap<>();

    public CombinationDocLinkService(@Qualifier("dmJdbcTemplate") JdbcTemplate dmJdbcTemplate) {
        this.dmJdbcTemplate = dmJdbcTemplate;
    }

    /**
     * 从工具参数中识别组合配置 INSERT 并记录 COMBINATION_ID（支持 execute_batch_sql 的 sqlList 与单条 sql）
     */
    public void recordFromToolInput(String sessionId, JSONObject toolInput) {
        if (sessionId == null || toolInput == null) {
            return;
        }
        try {
            String sqlList = toolInput.getString("sqlList");
            if (sqlList != null && !sqlList.isEmpty()) {
                JSONArray items = JSON.parseArray(sqlList);
                for (int i = 0; i < items.size(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    if (item != null) {
                        recordFromSql(sessionId, item.getString("sql"));
                    }
                }
            }
            recordFromSql(sessionId, toolInput.getString("sql"));
        } catch (Exception e) {
            log.warn("解析组合配置INSERT失败（不影响对话）: {}", e.getMessage());
        }
    }

    private void recordFromSql(String sessionId, String sql) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        Matcher matcher = COMBINATION_INSERT_PATTERN.matcher(sql);
        while (matcher.find()) {
            String combinationId = matcher.group(1).trim();
            if (!combinationId.isEmpty() && combinationId.length() <= 100) {
                sessionCombinations
                        .computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet())
                        .add(combinationId);
                log.info("记录到会话新建组合: sessionId={}, combinationId={}", sessionId, combinationId);
            }
        }
    }

    /**
     * 对话历史保存/更新后调用：把该会话创建的全部组合关联到本条历史记录。
     * 幂等：同一组合先删旧关联再插入，保证始终指向最新文档记录。
     */
    public void linkPending(String sessionId, String docId, String docName) {
        if (sessionId == null || docId == null || docId.isEmpty()) {
            return;
        }
        Set<String> combinationIds = sessionCombinations.get(sessionId);
        if (combinationIds == null || combinationIds.isEmpty()) {
            return;
        }
        // 复制快照，避免执行期间并发修改
        for (String combinationId : new LinkedHashSet<>(combinationIds)) {
            try {
                // 校验组合确实已入库（INSERT 可能执行失败）
                Integer exists = dmJdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM TBL_INDICATOR_COMBINATION WHERE COMBINATION_ID = ?",
                        Integer.class, combinationId);
                if (exists == null || exists == 0) {
                    log.info("组合未入库，跳过关联: {}", combinationId);
                    continue;
                }
                dmJdbcTemplate.update(
                        "DELETE FROM TBL_COMBINATION_MODEL_DOC WHERE COMBINATION_ID = ?", combinationId);
                dmJdbcTemplate.update(
                        "INSERT INTO TBL_COMBINATION_MODEL_DOC "
                                + "(ID, COMBINATION_ID, DOC_ID, DOC_NAME, CREATE_TIME, UPDATE_TIME, REMARK) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                        UUID.randomUUID().toString().replace("-", ""), combinationId, docId,
                        docName != null && docName.length() > 200 ? docName.substring(0, 200) : docName,
                        new Date(), new Date(), "AI建模自动关联");
                log.info("组合-文档关联完成: combinationId={}, docId={}, docName={}", combinationId, docId, docName);
            } catch (Exception e) {
                log.warn("组合-文档关联失败（不影响对话历史）: combinationId={}, {}",
                        combinationId, e.getMessage());
            }
        }
    }

    /**
     * 从对话 JSON 中提取最后一个文档标题（<<<HTML5_START:标题>>>，与前端取最后一文档的规则一致）
     */
    public String extractLastDocTitle(String dialogue) {
        if (dialogue == null || dialogue.isEmpty()) {
            return null;
        }
        try {
            int idx = dialogue.lastIndexOf(DOC_TITLE_MARKER);
            if (idx < 0) {
                return null;
            }
            Matcher m = Pattern.compile(Pattern.quote(DOC_TITLE_MARKER) + "(.+?)>>>").matcher(dialogue);
            if (m.find(idx)) {
                String title = m.group(1).trim();
                return title.isEmpty() ? null : title;
            }
        } catch (Exception e) {
            log.warn("提取文档标题失败: {}", e.getMessage());
        }
        return null;
    }
}
