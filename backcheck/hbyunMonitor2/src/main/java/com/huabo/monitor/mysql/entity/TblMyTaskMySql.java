package com.huabo.monitor.mysql.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_MY_TASK")
@Schema(name="TblMyTask对象")
public class TblMyTaskMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键Id 自增")
    @TableId("ID")
    @Id
    //@KeySql(sql = "select MYTASK_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal id;

    @Schema(name = "任务ID")
    @TableField("TASKID")
    @Column(name= "TASKID")
    private String taskId;

    @Schema(name = "定义名称")
    @TableField("PROCESSDEFINITIONID")
    @Column(name= "PROCESSDEFINITIONID")
    private String processDefinitionId;

    @Schema(name = "流程监控ID")
    @TableField("PROCESSINSTANCEID")
    @Column(name= "PROCESSINSTANCEID")
    private String processInstanceId;

    @Schema(name = "用户ID")
    @TableField("USRID")
    private String usrid;

    @Schema(name = "表单ID")
    @TableField("FROMID")
    private String fromid;

    @Schema(name = "表单名称或者表单路径")
    @TableField("FROMNAME")
    private String fromname;

    @Schema(name = "审批人")
    @TableField("APPROVER")
    private String approver;//办理人

    @Schema(name = "审批意见")
    @TableField("EXAMINATION")//办理意见
    private String examination;

    @Schema(name = "流程名称")
    @TableField("PROCESSNAME")
    @Column(name= "PROCESSNAME")
    private String processName;

    @Schema(name = "审批结果")
    @TableField("RESULT")
    private String result;//办理结果

    @Schema(name = "审批角色")
    @TableField("APPROVALROLE")
    @Column(name= "APPROVALROLE")
    private String approvalrole;//办理角色

    @Schema(name = "审批日期")
    @TableField("APPROVALDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date approvaldate;//办理时间

    @Schema(name = "cirid")
    @TableField("CIRID")
    private String cirid;

    @Schema(name = "handle")
    @TableField("HANDLE")
    private String handle;//"下一步：办理人/办理角色

    @Schema(name = "ANALID")
    @TableField("ANALID")
    private String analid;

    @Schema(name = "FXEXAM")
    @TableField("FXEXAM")
    private String fxexam;

    @Schema(name = "KZJZEXAM")
    @TableField("KZJZEXAM")
    private String kzjzexam;

    @Schema(name = "电子签名图片base64数据")
    @TableField("IMGBASESTR")
    @Column(name= "IMGBASESTR")
    private String imgbasestr;


}
