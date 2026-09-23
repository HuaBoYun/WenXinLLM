package com.huabo.contract.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_STAFF")
public class TblStaff {
    public final static Integer USER_DISABLE=0;//禁用
    public final static Integer USER_ENBLE=1;//启用

    public final static String REGISTERUSERPASSWORD = "REDACTED";

    @TableId(value = "STAFFID", type = IdType.INPUT)
    private BigDecimal staffid;//主键ID,自动增长
    @TableField("REALNAME")
    private String realname;//真实名字
    @TableField("FIXEDPHONE")
    private String fixedphone;//固定电话
    @TableField("ADDRESS")
    private String address;//地址
    @TableField("EMAIL")
    private String email;//邮箱
    @TableField("MIBLEPHONE")
    private String miblephone;//手机号码
    @TableField("MEMO")
    private String memo;//备注
    @TableField("USERNAME")
    private String username;//用户名（登录名）
    @TableField("PASSWORD")
    private String password;//密码
    @TableField("JOBID")
    private BigDecimal jobid;//岗位ID
    @TableField("CREATETIME")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date createtime;
    @TableField("STATUS")
    private Integer status;//状态（1启用，0弃用）
   
    //private TblOrganization tblOrganization;//组织ID
    @TableField("ORGID")
    private BigDecimal orgid;//组织Id
    @TableField("OUTSIDEID")
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @TableField("OUTSIDEOPENID")
    private String outSideOpenId; //外部同步企业来源Id
    private String orgName;
    private String jobName;
    private String orgFatherName;
    private String rn;
    private String checked;
	@TableField("HISTORYCODE")
	@Schema(name = "历史id")
	private String historycode;//
	@TableField("DINGID")
	@Schema(name = "钉钉id")
	private String dingid;//
	@TableField("POSTNAME")
	@Schema(name = "职位")
	private String postname;//
	@TableField("ISMAINPOST")
	@Schema(name = "是否主职")
	private String ismainpost;//
	@TableField("STATION")
	@Schema(name = "岗位")
	private String station;//
	@TableField("PK_MDM")
	@Schema(name = "ncc主键")
	private String pk_mdm;//
	@TableField("MDM_CODE")
	@Schema(name = "ncc编码")
	private String mdm_code;
	@TableField("DATASOURCE")
	@Schema(name = "数据来源")
	private String datasource; //
	
	@TableField("MANAGEORGS")
	@Schema(name = "管理部门的id串")
	private String manageorgs;
	@TableField("MANAGEORGNAMES")
	@Schema(name = "管理部门名称")
	private String manageorgnames;
	@TableField("FGORGS")
	@Schema(name = "分管部门id")
	private String fgorgs;
	@TableField("FGORGNAMES")
	@Schema(name = "分管部门名称")
	private String fgorgnames;
	
	@TableField(exist = false, select = false , fill = FieldFill.DEFAULT)
	private TblRole  trole;
	@TableField(exist = false, select = false , fill = FieldFill.DEFAULT)
    private TblOrganization linkDetp;//用户隶属的部门
	@TableField(exist = false, select = false , fill = FieldFill.DEFAULT)
    private TblOrganization currentOrg;//用户当前所在的公司
	@TableField(exist = false, select = false , fill = FieldFill.DEFAULT)
    private TblOrganization linkOrg;//用户隶属的公司
	@TableField(exist = false, select = false , fill = FieldFill.DEFAULT)
    private TblLoginType loginType;//登录页面信息





}

