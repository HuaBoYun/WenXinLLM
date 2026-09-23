package com.huabo.audit.oracle.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CIRCULATION")
@Schema(name="TblCirculation对象")
public class TblCirculation implements Serializable {

    //private static final long serialVersionUID = 1L;
	
    @Id
    @Schema(name = "主键Id 自增")
    @TableId("CYID")
    private BigDecimal cyid;

    @Schema(name = "编号")
    @TableField("CYCODE")
    private String cycode;

    @Schema(name = "名称")
    @TableField("CYNAME")
    private String cyname;

    @Schema(name = "发布时间")
    @TableField("CYDATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date cydate;

    @Schema(name = "标记状态")
    @TableField("CYSTATE")
    private String cystate;

    @Schema(name = "类型")
    @TableField("CYTYPE")
    private String cytype;

    @Schema(name = "详情路径")
    @TableField("CYURL")
    private String cyurl;

    @Schema(name = "用户staffID")
    @TableField("CYSTAFFID")
    private String cyStaffid;

    @Schema(name = "taskId")
    @TableField("TASKID")
    private String taskid;

    @Schema(name = "BUSINESSKEY")
    @TableField("BUSINESSKEY")
    private String businesskey;

    @Schema(name = "DEFINITIONID")
    @TableField("DEFINITIONID")
    private String definitionid;
    @Transient
    private Integer contractstatus;
    @Transient
    private String recordtype;

    public final static String TYPE_JHSP = "计划审批";
    //public final static String URL_JHSP = "/nbsj/jhgl/to_tjsp_info?formid=";//标识计划详情路径
    public final static String URL_JHSP = "/nbsj/jhgl/to_tjspPlan_info?planid=";//标识计划详情路径
    public final static String TYPE_XMSP = "项目审批";
    public final static String URL_XMSP = "/nbsj/xmgl/to_sp_info?spid=";//标识项目审批详情路径
    public final static String TYPE_TZGL = "投资管理";
    public final static String URL_TZGL = "/fxgl/tzfx/to_sptzgl_info?awid=";//标识投资管理审批详情路径
    public final static String TYPE_DGFH = "底稿复核";
    public final static String URL_DGFH = "/nbsj/sjss/to_sp_dggl?spid=";//标识项目审批详情路径
    public final static String TYPE_SSQRS = "事实确认书";
    public final static String URL_SSQRS = "/nbsj/sjss/to_sp_ssqrs?spid=";//标识事实确认书审批详情路径
    public final static String TYPE_SJTZS = "审计通知书";
    public final static String URL_SJTZS = "/nbsj/sjtzs/to_sp_sjtzs?spid=";//标识项目审批详情路径
    public final static String TYPE_SJBG = "审计报告";
    public final static String URL_SJBG = "/nbkz/sjbg/to_sp_sjbg?spid=";//标识项目审批详情路径
    public final static String TYPE_SJBGFH = "审计报告复核";
    public final static String URL_SJBGFH = "/nbkz/sjbg/to_sp_sjbg_fh?spid=";//标识审计报告复核
    public final static String TYPE_SJBGZQYJ = "审计报告征求意见";
    public final static String URL_SJBGZQYJ = "/nbkz/sjbg/to_sp_sjbg_zqyj?spid=";//标识审计报告征求意见
    
    public final static String TYPE_SJYH = "审计人员审核";
    public final static String URL_SJYH = "/nbsjapproval/getAuditUserApprovalInfo?staffid=";//标识项目审批详情路径
    
    public final static String TYPE_PJSH = "评价审核";
    public final static String URL_PJSH = "/nbkz/sjbg/to_sp_pjsh?spid=";//标识项目审批详情路径

    public final static String TYPE_QZD = "取证单审核";
    public final static String URL_QZD = "/nbsjapproval/getAuditUserApprovalInfo?certificateId=";//取证单审批详情路径


    public final static String STATE_FQ = "审批中";
    public final static String STATE_TZ = "需调整";
    public final static String STATE_ZZ = "已终止";
    public final static String STATE_TG = "已通过";
    public final static String STATE_ZD = "中断";
}
