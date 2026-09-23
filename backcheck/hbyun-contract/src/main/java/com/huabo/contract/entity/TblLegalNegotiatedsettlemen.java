package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.checkerframework.checker.units.qual.A;


/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
  @TableName("TBL_LEGAL_NEGOTIATEDSETTLEMEN")
@Schema(name="协商过程")
public class TblLegalNegotiatedsettlemen implements Serializable {
//协商过程
  private static final long serialVersionUID = 1L;

    @TableId(value = "NEGOTIAID",type = IdType.INPUT)
    private BigDecimal negotiaid;

  @TableField("NEGOTIASTATUS")
  private BigDecimal negotiastatus;

  @Schema(name="对方谈判人")
  @TableField("COUNTERPART")//对方谈判人
  private String counterpart;

	@Schema(name="对方谈判人联系电话")
  @TableField("COUNTERPARTPHONE")//对方谈判人联系电话
  private String counterpartphone;

	@Schema(name="是否协商一致：1是，2否")
  @TableField("ISAGGREE")//是否协商一致：1是，2否
  private BigDecimal isaggree;

	@Schema(name="是否预设调解方案：1是，2否")
  @TableField("ISPRESETCASE")//是否预设调解方案：1是，2否
  private BigDecimal ispresetcase;

	@Schema(name="谈判结果")
  @TableField("NEGETIARESULT")//谈判结果
  private String negetiaresult;

	@Schema(name="司法解决  1诉讼  2仲裁")
  @TableField("JUDICIALSETTLEMENT")//司法解决  1诉讼  2仲裁
  private BigDecimal judicialsettlement;

  @TableField("LINKORG")
  private BigDecimal linkorg;

  @TableField("DISPUINFO")
  private BigDecimal dispuinfo;

  @TableField("CREATESTAFF")
  private BigDecimal createstaff;

  @TableField("CREATETIME")
  @JSONField(format = "yyyy-MM-dd")
  private Date createtime;

  @Schema(name="解决方法：1私下条件，2司法调节")
  @TableField("SOLUTIONMODE")
  private BigDecimal solutionmode;

	@Schema(name="法院名称")
  @TableField("COURTNAME")//法院名称
  private String courtname;

  @Schema(name="方案状态")
  @TableField("SCHEMESTATUS")
  private BigDecimal schemeStatus;

  @Schema(name="调解方案")
  @TableField("MEDIATIONSCHEME")
  private String mediationScheme;
//
//  @Transient
//  private String disputeItem;

//  @Transient
//  private String disputeType;

//  @Transient
//  private String contractName;

  @TableField(exist=false,select=false,fill = FieldFill.DEFAULT)
  private List<TblAttachment> attList;

  @TableField(exist=false,select=false,fill = FieldFill.DEFAULT)
  private TblLegalDisputregistration dispuIn;

  @TableField(exist=false,select=false,fill = FieldFill.DEFAULT)
  private TblLegalDisputregistration dispu;
  ///////////映射TBL_LEGAL_DISPUTREGISTRATION与TBL_CYHW_UNIT////////////////
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
  private String disputeitem;//隶属纠纷

  @TableField(exist=false)
  private BigDecimal isattorney;

  @TableField(exist=false)
  private String coordination;

  @TableField(exist=false)
  private String casecause;

  @TableField(exist=false)
  private String ssproject;

  @TableField(exist=false)
  private String courtfirst;

  @TableField(exist=false)
  private BigDecimal litigationamount;

  @TableField(exist=false)
  private LocalDateTime occurrencetime;

  @TableField(exist=false)
  private String subsidiaries;

  @TableField(exist=false)
  private BigDecimal contractid;
  //TblCyhwUnit
  @Schema(name = "隶属流程id 关联Tbl_flow flowid")
  @TableField(exist=false)
  private BigDecimal flowid;

  @Schema(name = "单位名称")
  @TableField(exist=false)
  private String unitname;

  @Schema(name = "合同名称")
  @TableField(exist=false)
  private String contractname;//纠纷合同

  @Schema(name = "合同编号")
  @TableField(exist=false)
  private String contractno;

  @Schema(name = "承办部门")
  @TableField(exist=false)
  private BigDecimal contractdept;

  @Schema(name = "合同对方")
  @TableField(exist=false)
  private String contractlink;

  @Schema(name = "合同金额")
  @TableField(exist=false)
  private BigDecimal contractmoney;

  @Schema(name = "合同状态 1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、7-执行中、		8-已归档、		9-已暂停、		10-已变更、		11-已终止、		12-纠纷中、		13-协商中、		14-诉讼中、		15-仲裁中、		16-已结案")
  @TableField(exist=false)
  private Integer contractstatus;

  @Schema(name = "关联科室")
  @TableField(exist=false)
  private String linkdept;

  @Schema(name = "隶属组织")
  @TableField(exist=false)
  private BigDecimal orgid;

  @Schema(name = "创建人")
  @TableField(exist=false)
  private BigDecimal createuser;



  @Schema(name = "合同简要说明")
  @TableField(exist=false)
  private String momoconcat;

  @Schema(name = "关联的风险控制")
  @TableField(exist=false)
  private String riskcontrol;

  @Schema(name = "合同内容")
  @TableField(exist=false)
  private String describe;

  @Schema(name = "承办人")
  @TableField(exist=false)
  private BigDecimal contractstaff;

  @Schema(name = "保密等级")
  @TableField(exist=false)
  private String scrilevel;

  @Schema(name = "合同开始时间")
  @TableField(exist=false)
  @JSONField(format = "yyyy-MM-dd")
  private Date startdate;//创建日期

  @Schema(name = "合同结束时间")
  @TableField(exist=false)
  @JSONField(format = "yyyy-MM-dd")
  private Date enddate;

  @Schema(name = "合同项目")
  @TableField(exist=false)
  private String contractitem;

  @Schema(name = "合同类型")
  @TableField(exist=false)
  private String contracttype;

  @Schema(name = "合同期限类型")
  @TableField(exist=false)
  private String contractdatetype;

  @Schema(name = "合同性质")
  @TableField(exist=false)
  private String contractxz;

  @Schema(name = "合同相对方主键ID")
  @TableField(exist=false)
  private BigDecimal contractxdfxinfo;

  @Schema(name = "合同类型 HTGL001 - 相对方、HTGL002-合同订立、HTGL003-合同用印、HTGL004-合同履行、HTGL005-变更、HTGL006-合同归档、HTGL007-合同范本")
  @TableField(exist=false)
  private String recordtype;

  @Schema(name = "隶属合同 cyhwunitId")
  @TableField(exist=false)
  private BigDecimal recordparent;

  @Schema(name = "合同金额汉字描述")
  @TableField(exist=false)
  private String hzsumowing;

  @Schema(name = "计价方式")
  @TableField(exist=false)
  private String jijiatype;

  @Schema(name = "币种")
  @TableField(exist=false)
  private String moneytype;

  @Schema(name = "首付方向")
  @TableField(exist=false)
  private String dctype;

  @Schema(name = "合同标的")
  @TableField(exist=false)
  private String contractbd;

  @Schema(name = "是否重大合同")
  @TableField(exist=false)
  private String contractzd;

  @Schema(name = "经办人")
  @TableField(exist=false)
  private BigDecimal jbstaff;

  @Schema(name = "经办部门")
  @TableField(exist=false)
  private BigDecimal jbdept;

  @Schema(name = "经办公司")
  @TableField(exist=false)
  private BigDecimal jbunit;

  @Schema(name = "执行公司")
  @TableField(exist=false)
  private BigDecimal zxunit;

  @Schema(name = "是否关联主合同")
  @TableField(exist=false)
  private String contractchildren;

  @Schema(name = "是否有履行计划")
  @TableField(exist=false)
  private String contractplan;

  @Schema(name = "合同立项信息")
  @TableField(exist=false)
  private String topicid;

  @TableField(exist=false)
  @Schema(name = "合同立项名称")
  private String topicname;

  @Schema(name = "历史状态")
  @TableField(exist=false)
  private BigDecimal hiscontractstatus;

  @Schema(name = "变更类型")
  @TableField(exist=false)
  private String changetype;

  @Schema(name = "变更时间")
  @TableField(exist=false)
  private LocalDateTime changedate;

  @Schema(name = "变更内容")
  @TableField(exist=false)
  private String changedesc;

  @TableField(exist=false)
  private BigDecimal counterpartbank;


}
