package com.huabo.compliance.mysql.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_AUDITPLAN")
@Schema(name="TblNbsjAuditplanMySql对象")
public class TblNbsjAuditplanMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "计划编号")
    @TableId("PLANID")
    private BigDecimal planid;

    @Schema(name = "计划编码")
    @TableField("PLANCODE")
    private String plancode;

    @Schema(name = "计划名称")
    @TableField("PLANNAME")
    private String planname;

    @Schema(name = "计划年度")
    @TableField("PALNYEAR")
    private String palnyear;

    @Schema(name = "计划类型")
    @TableField("PLANTYPE")
    private String plantype;

    @Schema(name = "计划对象")
    @TableField("AUDITORGID")
    private BigDecimal auditorgid;

    @Schema(name = "计划估算费用")
    @TableField("PALNCOST")
    private BigDecimal palncost;

    @Schema(name = "开始时间")
    @TableField("STARTTIME")
    private LocalDateTime starttime;

    @Schema(name = "结束时间")
    @TableField("ENDTIME")
    private LocalDateTime endtime;

    @Schema(name = "计划负责人")
    @TableField("PRINCIPALID")
    private BigDecimal principalid;

    @Schema(name = "审计组长")
    @TableField("LEADERID")
    private BigDecimal leaderid;

    @Schema(name = "审计目标和范围")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;

    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    private LocalDateTime createtime;

    @Schema(name = "修改时间")
    @TableField("UPDATETIMR")
    private LocalDateTime updatetimr;

    @Schema(name = "项目状态")
    @TableField("STATUS")
    private BigDecimal status;

    @Schema(name = "审批状态")
    @TableField("OPINIONSTATUS")
    private BigDecimal opinionstatus;

    @Schema(name = "未知")
    @TableField("ISAUDITOR")
    private String isauditor;


    @Schema(name = "主键Id 自增")
    @Transient
    private BigDecimal attid;

    @Schema(name = "附件名称")
    @Transient
    private String attname;

    @Schema(name = "附件路径")
    @Transient
    private String attpath;

    @Schema(name = "附件大小")
    @Transient
    private BigDecimal attsize;

    @Schema(name = "备注")
    @Transient
    private String memo;

    @Schema(name = "上传时间")
    @Transient
    private LocalDateTime uploadtime;

    @Schema(name = "上传人")
    @Transient
    private String uploader;

    @Schema(name = "是否是python爬取文件 0是")
    @Transient
    private String ispythonflag;


    @Transient
    private BigDecimal staffid;//主键ID,自动增长
    @Transient
    private String realname;//真实名字
    @Transient
    private String fixedphone;//固定电话
    @Transient
    private String address;//地址
    @Transient
    private String email;//邮箱
    @Transient
    private String miblephone;//手机号码
    @Transient
    private String username;//用户名（登录名）
    @Transient
    private String password;//密码
    @Transient
    private BigDecimal jobid;//岗位ID
    @Transient
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    @Transient
    private BigDecimal orgid;//组织Id
    @Transient//角色id
    private BigDecimal roleid;
    @Transient
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @Transient
    private String outSideOpenId; //外部同步企业来源Id

}
