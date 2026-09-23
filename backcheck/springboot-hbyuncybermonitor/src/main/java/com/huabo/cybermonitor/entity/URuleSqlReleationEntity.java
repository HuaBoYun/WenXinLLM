package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName URULE_SQL_RELEATION
 */
@TableName(value ="URULE_SQL_RELEATION")
@Data
public class URuleSqlReleationEntity implements Serializable {

    public  final static String OFF="OFF";//关闭
    public final static String ON="ON";//开启
    /**
     * 主键
     */
    @TableId(value = "ID", type= IdType.INPUT)
    @Schema(name = "主键")
    private String id;

    /**
     * sql组装数据ID
     */
    @TableField(value = "SQL_DATA_ID")
    private String sqlDataId;

    /**
     * 规则项目名称，用于调用URule使用,与urule_package结合使用 例：urule_projcet/urule_package
     */
    @TableField(value = "URULE_PROJECT_NAME")
    private String uruleProjectName;

    /**
     * 规则知识包ID，用于调用URule使用,与urule_projcet结合使用 例：urule_projcet/urule_package
     */
    @TableField(value = "URULE_PACKAGE_ID")
    private String urulePackageId;

    /**
     * 规则知识包名称
     */
    @TableField(value = "URULE_PACKAGE_NAME")
    private String urulePackageName;

    /**
     * 创建人
     */
    @TableField(value = "CREATEUSER")
    private String createuser;

    /**
     * 修改人
     */
    @TableField(value = "UPDATEUSER")
    private String updateuser;

}