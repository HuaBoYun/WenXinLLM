package com.huabo.es.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : RuleEntity
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-19 16:14:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleEntity {
    @Schema
    private String keyWord;
    @Schema
    private Long pageSize;
    @Schema
    private Long pageNum;
}
