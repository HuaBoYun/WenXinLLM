package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 预算体系矩阵数据VO
 *
 * @description 展示组织、维度、指标之间的关联矩阵
 * @author AI Assistant
 * @date 2026-04-17
 */
@Data
@ApiModel("预算体系矩阵数据")
public class BudgetSystemMatrixVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组织列表")
    private List<MatrixOrganization> organizations;

    @ApiModelProperty("维度列表")
    private List<MatrixDimension> dimensions;

    @ApiModelProperty("指标列表")
    private List<MatrixIndicator> indicators;

    @ApiModelProperty("模型列表")
    private List<MatrixModel> models;

    @ApiModelProperty("关联关系列表")
    private List<MatrixRelation> relations;

    @Data
    public static class MatrixOrganization implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String code;
        private String name;
        private String type;
        private String status;
        private String parentId;
        private Integer level;
    }

    @Data
    public static class MatrixDimension implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String code;
        private String name;
        private String type;
        private String status;
        private String parentId;
    }

    @Data
    public static class MatrixIndicator implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String code;
        private String name;
        private String type;
        private String dataType;
        private String unit;
        private String status;
    }

    @Data
    public static class MatrixModel implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String code;
        private String name;
        private String type;
        private String status;
        private String budgetCycle;
    }

    @Data
    public static class MatrixRelation implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("关联来源类型: organization/dimension/indicator/model")
        private String sourceType;
        @ApiModelProperty("关联来源ID")
        private String sourceId;
        @ApiModelProperty("关联来源名称")
        private String sourceName;
        @ApiModelProperty("关联目标类型: organization/dimension/indicator/model")
        private String targetType;
        @ApiModelProperty("关联目标ID")
        private String targetId;
        @ApiModelProperty("关联目标名称")
        private String targetName;
        @ApiModelProperty("关联类型描述")
        private String relationType;
    }
}
