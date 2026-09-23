package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Rui
 * @ClassName ProjectSuggestionEntity
 * @Description
 * @DATE 2023/9/6
 */
@Data
@TableName("TBL_YQNS_PROJECT_SUGGESTION")
@Schema(name="立项建议实体")
@Accessors(chain = true)
public class ProjectSuggestionEntity implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="PURPOSENO")
    @Schema(name="审计项目名称")
    private String purposeNo;
    
    @TableField(value="PROJECT_NAME")
    @Schema(name="审计项目名称")
    private String projectName;

    @TableField(value="PROJECT_PURPOSE")
    @Schema(name="立项理由及审计目的")
    private String projectPurpose; 

    @TableField(value="PROJECT_TYPE")
    @Schema(name="项目类型")
    private String projectType;

    @TableField(value="CONCERNS_CONTENT")
    @Schema(name="重点关注内容")
    private String concernsContent;

    @TableField(value="UNIT_RANGE")
    @Schema(name="单位范围")
    private String unitRange;

    @TableField(value="TIME_RANGE")
    @Schema(name="时间范围")
    private String timeRange;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;

    @TableField(value="REMARK")
    @Schema(name="备注")
    private String remark;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;
    
    @TableField(exist = false)
    @Schema(name="用户查看数据权限部门")
    private String queryDeptIds;
    
    
    @Schema(name = "填报单位id")
    @TableField(value = "TBORGID")
    private BigDecimal tborgid;

    @Schema(name = "填报单位名称")
    @TableField(value = "TBORGNAME")
    private String tborgname;


    @TableField(exist = false)
    @Schema(name="导出的选择ids;逗号隔开 1,2,3")
    private String ids;


}
