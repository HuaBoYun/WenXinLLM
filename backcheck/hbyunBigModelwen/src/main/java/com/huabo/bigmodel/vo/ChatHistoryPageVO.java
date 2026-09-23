package com.huabo.bigmodel.vo;

import com.huabo.bigmodel.entity.ChatHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * AI对话历史记录分页结果VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "对话历史分页结果")
public class ChatHistoryPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页数据列表")
    private List<ChatHistory> list;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "当前页码")
    private Integer pageNum;

    @Schema(description = "每页大小")
    private Integer pageSize;

    @Schema(description = "是否还有更多数据")
    private Boolean hasMore;
}
