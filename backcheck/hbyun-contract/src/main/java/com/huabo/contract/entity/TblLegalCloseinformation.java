package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_CLOSEINFORMATION")
@Schema(name="TblLegalCloseinformation对象")
public class TblLegalCloseinformation implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "CLOSEID",type = IdType.INPUT)
      private BigDecimal closeid;

    @TableField("CLOSEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date closedate;//结项日期

    @TableField("JUDGEMENTAMOUNT")
    private BigDecimal judgementamount;//判决金额

    @TableField("CLOSERESULT")//处理结果
    private String closeresult;

    @TableField("MANAGERECOMMOND")
    private String managerecommond;//管理建议

    @TableField("DISPUTINFO")
    private BigDecimal disputinfo;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @Schema(name = "对应TblStaff中REALNAME，真实名字")
    @TableField(exist=false)
    private String realname;

//    @Schema(name = "对应TblLegalDisputregistration中DISPUTEITEM")
//    @Transient
//    private String diputeItem;
    @TableField(exist=false)
    private BigDecimal minmoney;
    @TableField(exist=false)
    private BigDecimal maxmoney;

    @TableField(exist=false)
    private TblLegalDisputregistration dispute;


    //映射TblStaff与TblLegalDisputregistration
    @TableField(exist=false)
    private BigDecimal staffid;//主键ID,自动增长
    @TableField(exist=false)
    private String fixedphone;//固定电话
    @TableField(exist=false)
    private String address;//地址
    @TableField(exist=false)
    private String email;//邮箱
    @TableField(exist=false)
    private String miblephone;//手机号码
    @TableField(exist=false)
    private String memo;//备注
    @TableField(exist=false)
    private String username;//用户名（登录名）
    @TableField(exist=false)
    private String password;//密码
    @TableField(exist=false)
    private BigDecimal jobid;//岗位ID
    @TableField(exist=false)
    @JSONField(format = "yyyy-MM-dd")
    private Date createDate;
    @TableField(exist=false)
    private Integer status;//状态（1启用，0弃用）
    @TableField(exist=false)
    private BigDecimal orgid;//组织Id
    @TableField(exist=false)
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @TableField(exist=false)
    private String outSideOpenId; //外部同步企业来源Id

    @TableField(exist=false)
    private BigDecimal disputeid;
    @TableField(exist=false)
    private String disputeno;
    @TableField(exist=false)
    private BigDecimal disputestatus;
    @TableField(exist=false)
    private String disputetype;
    @TableField(exist=false)
    private BigDecimal contractinfo;
    @TableField(exist=false)
    private String disputecours;
    @TableField(exist=false)
    private BigDecimal isuegent;
    @TableField(exist=false)
    private BigDecimal whethersued;
    @TableField(exist=false)
    private BigDecimal disputeundertaker;
    @TableField(exist=false)
    private LocalDateTime lastdealdate;
    @TableField(exist=false)
    private String solutionsuggestions;
    @TableField(exist=false)
    private String plaintiff;
    @TableField(exist=false)
    private String defendant;
    @TableField(exist=false)
    private String attorney;
    @TableField(exist=false)
    private String attorneyphont;
    @TableField(exist=false)
    private BigDecimal dispuinfo;
    @TableField(exist=false)
    private String disputeitem;//纠纷主题

    @TableField(exist=false)
    private BigDecimal isattorney;

    @Schema(name = "贵安配合人员")
    @TableField(exist=false)
    private String coordination;

    @Schema(name = "贵安纠纷登记-案由")
    @TableField(exist=false)
    private String casecause;

    @Schema(name = "贵安纠纷登记-涉诉项目")
    @TableField(exist=false)
    private String ssproject;

    @Schema(name = "贵安纠纷登记-管辖法院（仲裁机构）")
    @TableField(exist=false)
    private String courtfirst;

    @Schema(name = "贵安纠纷登记-诉讼金额")
    @TableField(exist=false)
    private BigDecimal litigationamount;

    @Schema(name = "贵安纠纷登记-发生时间")
    @TableField(exist=false)
    private LocalDateTime occurrencetime;

    @Schema(name = "贵安纠纷登记-涉及子公司")
    @TableField(exist=false)
    private String subsidiaries;
}
