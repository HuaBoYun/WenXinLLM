package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname TblYqnsJhglJhprojectName
 * @Description TODO
 * @Date 2024/4/19 0:10
 * @Created by GJ.C
 */
@Data
public class TblYqnsJhglJhprojectName implements Serializable {
    /**
     * 审计单位名称
     */
    @Schema(name = "审计单位名称")
    private String projectName;


    /**
     * 计划主键
     */
    @Schema(name = "计划关联id")
    @TableField(value = "JHID")
    private Long jhid;


    /**
     * 关联id
     */
    @Schema(name = "计划类型关联id")
    @TableField(value = "GLID")
    private String glId;


    /**
     * 审计类型
     * 1：专项审计
     * 11：生产经营管理专项审计
     * 12：基建与投资专项审计
     * *************************************
     * 2：经济责任审计
     * 21：二级单位及所属成员单位离任经济责任审计
     * 22：二级单位任中经济责任审计
     * 23: 三级单位离任经济责任审计
     * *************************************
     * 3: 工程建设项目审计
     * 31：工程建设项目结算审计
     * 32：工程建设项目竣工决算审计
     */
    @Schema(name = "关联类型")
    @TableField(value = "GLTYPE")
    private String gltype;

}