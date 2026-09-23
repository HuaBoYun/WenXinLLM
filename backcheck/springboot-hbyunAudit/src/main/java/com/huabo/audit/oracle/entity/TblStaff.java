package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_STAFF")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TblStaff {
    public final static Integer USER_DISABLE=0;//禁用
    public final static Integer USER_ENBLE=1;//启用

    public final static String REGISTERUSERPASSWORD = "REDACTED";
    
    
    public final static Integer SPZ=1;//审批中
	public final static Integer XTZ=2;//需调整
	public final static Integer YTG=3;//已通过
	public final static Integer YZZ=4;//已终止
	public final static Integer YWC=6;//已终止
    

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableId("STAFFID")
	@TableField("STAFFID")
    private BigDecimal staffid;//主键ID,自动增长
    @TableField("REALNAME")
	@Column(name = "REALNAME")
    private String realname;//真实名字
    @TableField("FIXEDPHONE")
	@Column(name = "FIXEDPHONE")
    private String fixedphone;//固定电话
    @TableField("ADDRESS")
	@Column(name = "ADDRESS")
    private String address;//地址
    @TableField("EMAIL")
	@Column(name = "EMAIL")
    private String email;//邮箱
    @TableField("MIBLEPHONE")
	@Column(name = "MIBLEPHONE")
    private String miblephone;//手机号码
    @TableField("MEMO")
	@Column(name = "MEMO")
    private String memo;//备注
    @TableField("USERNAME")
	@Column(name = "USERNAME")
    private String username;//用户名（登录名）
    @TableField("PASSWORD")
	@Column(name = "PASSWORD")
    private String password;//密码
    @TableField("JOBID")
	@Column(name = "JOBID")
    private BigDecimal jobid;//岗位ID
    @TableField("CREATETIME")
	@Column(name = "CREATETIME")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date createtime;
    @TableField("STATUS")
	@Column(name = "STATUS")
    private Integer status;//状态（1启用，0弃用）
    @TableField("ORGID")
	@Column(name = "ORGID")
    private BigDecimal orgid;//组织Id
    @TableField("OUTSIDEID")
	@Column(name = "OUTSIDEID")
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @TableField("OUTSIDEOPENDID")
	@Column(name = "OUTSIDEOPENDID")
    private String outSideOpenId; //外部同步企业来源Id
    @Transient
	@TableField(exist = false)
    private String orgName;
    @Transient
	@TableField(exist = false)
    private String jobName;
    @Transient
	@TableField(exist = false)
    private String orgFatherName;
    @Transient
	@TableField(exist = false)
    private String rn;
    @Transient
	@TableField(exist = false)
    private String checked;
    
    
    @TableField("BIRTHDAY")
	@Column(name = "BIRTHDAY")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	@Schema(name = "出生年月")
	private Date birthday;
	
	@TableField("POLITICALOUTLOOK")
	@Column(name = "POLITICALOUTLOOK")
	@Schema(name = "政治面貌")
	private String politicaloutlook;

	@TableField("EDUCATION")
	@Column(name = "EDUCATION")
	@Schema(name = "学历")
	private String education;
	
	@TableField("MAJOR")
	@Column(name = "MAJOR")
	@Schema(name = "专业")
	private String major;
 
	@TableField("SCHOOL")
	@Column(name = "SCHOOL")
	@Schema(name = "毕业院校")
	private String school;
	 
	@TableField("OFFICEPHONE")
	@Column(name = "OFFICEPHONE")
	@Schema(name = "办公电话")
	private String officephone;
	 
	@TableField("WORKTIME")
	@Column(name = "WORKTIME")
	@Schema(name = "参加工作时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date worktime;
	 
	@TableField("TITLE")
	@Column(name = "TITLE")
	@Schema(name = "职称")
	private String title;
 
	@TableField("QUALIFICATION")
	@Column(name = "QUALIFICATION")
	@Schema(name = "执业资格")
	private String qualification;
	
	@TableField("SITUATION")
	@Column(name = "SITUATION")
	@Schema(name = "参加审计/检查情况")
	private String situation;
	
	@TableField("ENTRYTIME")
	@Column(name = "ENTRYTIME")
	@Schema(name = "入职时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date entrytime;
	
	@TableField("RESIGNATIONTIME")
	@Column(name = "RESIGNATIONTIME")
	@Schema(name = "离职时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date resignationtime;
	
	@TableField("JOBEXPERIENCES")
	@Column(name = "JOBEXPERIENCES")
	@Schema(name = "主要工作经历 ")
	private String jobexperiences;
	
	@TableField("AUDITORTYPE")
	@Column(name = "AUDITORTYPE")
	@Schema(name = "审计人员类型 ")
	private String auditortype;
 
	 
	@TableField("TYPE")
	@Column(name = "TYPE")
	@Schema(name = "全职/兼职 ")
	private String type;
	
	@TableField("ISAUDIT")
	@Column(name = "ISAUDIT")
	@Schema(name = "是否是审计人员 ")
	private String isAudit;
	
	
	@TableField("GENDER")
	@Column(name = "GENDER")
	@Schema(name = "性别 ")
	private String gender;
	 
	@TableField("DATASOURCE")
	@Column(name = "DATASOURCE")
	private String dataSource;
	
	@TableField("HISTORYCODE")
	@Column(name = "HISTORYCODE")
	private String historycode;
	
	@TableField("HISTORYDEPARTMENTID")
	@Column(name = "HISTORYDEPARTMENTID")
	private String historydepartmentid;
	

	@TableField("PERSONTYPE")
	@Column(name = "PERSONTYPE")
	@Schema(name = "审计人员类型：内部 0-外部1 ")
	private String personType;

	@TableField("CURRENTPOSITION")
	@Column(name = "CURRENTPOSITION")
	@Schema(name = "现任职位 ")
	private String currentPosition;

	@TableField("CURRENTPOSITIONDUR")
	@Column(name = "CURRENTPOSITIONDUR")
	@Schema(name = "现任职位在职时长")
	private String currentPositiondur;

	@TableField("AUDITSTATE")
	@Column(name = "AUDITSTATE")
	@Schema(name = "是否审计对象：1：是  0：否")
	private Integer auditState;

    @Transient
	@TableField(exist = false)
    private TblOrganization linkDetp;//用户隶属的部门
    @Transient
	@TableField(exist = false)
    private TblOrganization currentOrg;//用户当前所在的公司
    @Transient
	@TableField(exist = false)
    private TblOrganization linkOrg;//用户隶属的公司

    
    @TableField("APRSTATUS")
	@Column(name = "APRSTATUS")
	@Schema(name = "审批状态")
	private Integer aprStatus;

}
