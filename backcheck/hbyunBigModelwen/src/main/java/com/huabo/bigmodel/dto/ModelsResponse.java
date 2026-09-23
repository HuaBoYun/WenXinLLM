package com.huabo.bigmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模型列表响应DTO
 * 符合OpenAI /v1/models 接口规范
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelsResponse {

    /**
     * 对象类型
     */
    @Builder.Default
    private String object = "list";

    /**
     * 模型列表
     */
    private List<ModelData> data;

    /**
     * 模型数据
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelData {
        /**
         * 模型ID
         */
        private String id;

        /**
         * 对象类型
         */
        @Builder.Default
        private String object = "model";

        /**
         * 创建时间
         */
        private Long created;

        /**
         * 所有者
         */
        @Builder.Default
        private String ownedBy = "huabo-cloud";
    }
}

