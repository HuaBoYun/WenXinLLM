package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.netty.channel.ChannelHandler.Sharable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="用户TBL_STAFF对象", description="")
@TableName(value = "TBL_STAFF")
public class TblStaff implements Serializable {
	public final static Integer USER_DISABLE=0;//禁用
	public final static Integer USER_ENBLE=1;//启用
	
	public final static String REGISTERUSERPASSWORD = "REDACTED";
	
	public final static Integer SPZ=1;//审批中
	public final static Integer XTZ=2;//需调整
	public final static Integer YTG=3;//已通过
	public final static Integer YZZ=4;//已终止
	
	@TableId(value="STAFFID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal staffid;//主键ID,自动增长
	@TableField("REALNAME")
	@Schema(name="真实名字")
	private String realname;//真实名字
	@TableField("FIXEDPHONE")
	@Schema(name="固定电话")
	private String fixedphone;//固定电话
	@TableField("ADDRESS")
	@Schema(name="地址")
	private String address;//地址
	@TableField("EMAIL")
	@Schema(name="邮箱")
	private String email;//邮箱
	@TableField("MIBLEPHONE")
	@Schema(name="手机号码")
	private String miblephone;//手机号码
	@TableField("MEMO")
	@Schema(name="备注")
	private String memo;//备注
	@TableField("USERNAME")
	@Schema(name="用户名（登录名）")
	private String username;//用户名（登录名）
	@TableField("PASSWORD")
	@Schema(name="密码")
	private String password;//密码
	@TableField("JOBID")
	@Schema(name="岗位ID")
	private BigDecimal jobid;//岗位ID
	@TableField("CREATETIME")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(name="创建时间")
	private Date createDate;
	@TableField("STATUS")
	@Schema(name="状态（1启用，0弃用）")
	private Integer status;//状态（1启用，0弃用）
	@TableField("ORGID")
	@Schema(name="组织Id")
	private BigDecimal orgid;//组织Id
	@TableField("ROLEID")//
	@Schema(name="角色id,审批角色")
	private BigDecimal roleid;
	@TableField("OUTSIDEID")
	@Schema(name="标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源")
	private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
	@TableField("OUTSIDEOPENDID")
	@Schema(name="外部同步企业来源Id")
	private String outSideOpenId; //外部同步企业来源Id

	@TableField(value="ROLEIDSTRS")
	@Schema(name="多个角色主键，用逗号分割 示例：1,2,3,4")
	private String roleIdStrs;
	
	@TableField(value="PKYMSTAFFID")
	@Schema(name="低代码平台主键ID",hidden=true)
	private String pkYmStaffId;
	
	@TableField("BIRTHDAY")
	@Schema(name="出生年月")
	private Date birthday;
	
	@TableField("POLITICALOUTLOOK")
	@Schema(name="政治面貌")
	private String politicaloutlook;

	@TableField("EDUCATION")
	@Schema(name="学历")
	private String education;
	
	@TableField("MAJOR")
	@Schema(name="专业")
	private String major;
 
	@TableField("SCHOOL")
	@Schema(name="毕业院校")
	private String school;
	 
	@TableField("OFFICEPHONE")
	@Schema(name="办公电话")
	private String officephone;
	 
	@TableField("WORKTIME")
	@Schema(name="参加工作时间")
	private Date worktime;
	 
	@TableField("TITLE")
	@Schema(name="职称")
	private String title;
 
	@TableField("JOBGRADEID")
	@Schema(name="职级id")
	private String jobGradeId;
	
	@TableField("QUALIFICATION")
	@Schema(name="执业资格")
	private String qualification;
	
	@TableField("SITUATION")
	@Schema(name="参加审计/检查情况")
	private String situation;
	
	@TableField("ENTRYTIME")
	@Schema(name="入职时间")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date entrytime;
	
	
	@TableField("UPDATETIME")
	@Schema(name="更新时间")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updatetime;
	
	@TableField("IDCARD")
	@Schema(name="身份证号")
	private String idCard;
	
	@TableField("RESIGNATIONTIME")
	@Schema(name="离职时间")
	private Date resignationtime;
	
	@TableField("JOBEXPERIENCES")
	@Schema(name="主要工作经历 ")
	private String jobexperiences;
	
	@TableField("AUDITORTYPE")
	@Schema(name="审计人员类型 ")
	private String auditortype;
	
	@TableField("TYPE")
	@Schema(name="全职/兼职 ")
	private String type;
	
	@TableField("ISAUDIT")
	@Schema(name="是否是审计人员 ")
	private String isAudit;
	
	
	@TableField("CHARGELEADERSTAFFID")
	@Schema(name="直属主管 ")
	private BigDecimal chargeLeaderStaffId;
	
	@TableField("GENDER")
	@Schema(name="性别 ")
	private String gender;
	
	
	@TableField("MANAGEORGS")
	@Schema(name="部门负责人部门ID ")
	private String manageorgs;
	
	@TableField("MANAGEORGNAMES")
	@Schema(name="部门负责人名称 ")
	private String manageorgnames;
	
	@TableField("FGORGS")
	@Schema(name="分管部门Id ")
	private String fgorgs;
	
	@TableField("FGORGNAMES")
	@Schema(name="分管部门名称 ")
	private String fgorgnames;
	
	
	
	@TableField("HISTORYCODE")
	@Schema(name="同步用户主键")
	private String historycode;
	
	@TableField("HISTORYDEPARTMENTID")
	@Schema(name="同步用户历史部门ID")
	private String historydepartmentid;
	
	@TableField("DATASOURCE")
	@Schema(name="同步用户来源")
	private String dataSource;
	
	@TableField("SECRECY")
	@Schema(name="密级")
	private String secrecy;

	@TableField("ONDUTYSTATUS")
	@Schema(name="在岗状态：1-在岗闲置 2-在岗项目内 3-请假 4-外派")
	private Integer onDutyStatus;
	
	@Schema(name="密级信息主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Transient
    @Schema(name="密级名称")
    @TableField(exist = false)
    private String secrectLevelName;

    @Transient
    @Schema(name="业务单据密级主键，用于筛选用户")
    @TableField(exist = false)
    private BigDecimal formSecrectId;
    
    @Transient
    @Schema(name="用户密级范围，用于筛选用户")
    @TableField(exist = false)
    private String formSecrectScope;

	@TableField(exist = false)
	@Transient
	private String jobName;//岗位名称
	@Transient
	@TableField(exist = false)
	private String orgFatherName;//所属公司名称
	@Transient
	@TableField(exist = false)
	private String rn;//查询序号
	@Transient
	@TableField(exist = false)
	private String checked;//是否选中
	@Transient
	@TableField(exist = false)
	@Schema(name="角色名，用逗号分隔")
	private String roleNames;
	
	@TableField("PERSONTYPE")
	@Schema(name="审计人员类型：内部 0-外部1 ")
	private String personType;
	
	@Transient
	@TableField(exist = false)
	@Schema(name="公司名称")
	private String orgname;
	
	@Transient
	@TableField(exist = false)
	@Schema(name="公司名称")
	private String companyname;
	
	@TableField(exist = false)
	@Schema(name="直属领导姓名")
	private String leaderName;
	
	@Transient
	@TableField(exist = false)
	private TblRole  trole;
	@Transient
	@TableField(exist = false)
	private TblOrganization linkDetp;//用户隶属的部门
	@Transient
	@TableField(exist = false)
	private TblOrganization currentOrg;//用户当前所在的公司
	@Transient
	@TableField(exist = false)
	private TblOrganization linkOrg;//用户隶属的公司
	@Transient
	@TableField(exist = false)
	private TblLoginType loginType;//登录页面信息
	@Transient
	@TableField(exist = false)
	private TblOrganization tblOrganization;

	@Transient
	@TableField(exist = false)
	private TblExternalExpert tblExternalExpert;
	
	@Transient
	@TableField(exist = false)
	private String orgIdStrs;

//	@JSONField(
//			serialize = false
//	)
//	@Transient
//	private Set<TblManageRight> tblManageRights = new HashSet(0);
//
//	@JSONField(serialize = false)
	public TblOrganization getTblOrganization() {
		return this.tblOrganization;
	}

	@JSONField(serialize = false)
	@TableField(exist = false)
	private Set<TblManageRight> tblManageRights = new HashSet(0);
	
	@Transient
	@TableField(exist = false)
	private TblRole tblRole;//角色

	@Transient
	@TableField(exist = false)
	private BigDecimal orgids;//组织id
	
	
	@TableField("APRSTATUS")
	@Schema(name="审批状态")
	private Integer aprStatus;
	
	
	//==
	@TableField("YEARSOLD")
	@Schema(name="年龄")
	private String yearsold;
	
	@TableField("CONTACTINFO")
	@Schema(name="联系方式")
	private String contactinfo;
	
	@TableField("ZHIWU")
	@Schema(name="职务")
	private String zhiwu;
	
	@TableField("ZHIWULEVEL")
	@Schema(name="职级")
	private String zhiwulevel;
	
	@TableField("TOPMAJOR")
	@Schema(name="最高学历专业")
	private String topmajor;
	
	@TableField("GRADSCHOOL")
	@Schema(name="研究生毕业院校")
	private String gradschool;
	
	
	@Transient
	@TableField(exist = false)
	@Schema(name="用户兼职公司部门集合",hidden=true)
	private List<TblUserOrgRelation> relaList = new ArrayList<TblUserOrgRelation>(0);
	
	@Transient
	@TableField(exist = false)
	@Schema(name="人员部门范围",hidden=true)
	private List<String> orgIdList;


	//中核新增字段
	//PERGENDERCODE  性别编号
	@TableField("PERGENDERCODE")
	@Schema(name="性别编号 ")
	private String PERGENDERCODE;
	//PERNATIVECODE 籍贯编号
	@TableField("PERNATIVECODE")
	@Schema(name="籍贯编号 ")
	private String PERNATIVECODE;
	//RESTYPE 户口性质
	@TableField("RESTYPE")
	@Schema(name="户口性质 ")
	private String RESTYPE;
	//PERNATIONALITY 民族
	@TableField("PERNATIONALITY")
	@Schema(name="民族 ")
	private String PERNATIONALITY;
	//HEALTHSTATUSCODE  健康状况编号
	@TableField("HEALTHSTATUSCODE")
	@Schema(name="健康状况编号 ")
	private String HEALTHSTATUSCODE;
	//健康状况中文 HEALTHSTATUSNAME
	@TableField("HEALTHSTATUSNAME")
	@Schema(name="健康状况中文 ")
	private String HEALTHSTATUSNAME;
	//MARITALSTATUSCODE 婚姻状况编号
	@TableField("MARITALSTATUSCODE")
	@Schema(name="婚姻状况编号 ")
	private String MARITALSTATUSCODE;
	//MARITALSTATUSNAME 婚姻状况中文
	@TableField("MARITALSTATUSNAME")
	@Schema(name="婚姻状况中文 ")
	private String MARITALSTATUSNAME;
	//EDUBACKGROUND 学历
	@TableField("EDUBACKGROUND")
	@Schema(name="学历 ")
	private String EDUBACKGROUND;
	//EDUBACKGROUNDCODE 学位编号
	@TableField("EDUBACKGROUNDCODE")
	@Schema(name="学位编号 ")
	private String EDUBACKGROUNDCODE;
	//TEACHDUTYCODE 专业技术职务编号
	@TableField("TEACHDUTYCODE")
	@Schema(name="专业技术职务编号 ")
	private String TEACHDUTYCODE;
	//TEACHDUTYNAME 专业技术职务中文
	@TableField("TEACHDUTYNAME")
	@Schema(name="专业技术职务中文 ")
	private String TEACHDUTYNAME;
	//职业资格编号 OCCQUALIFICATIONCODE
	@TableField("OCCQUALIFICATIONCODE")
	@Schema(name="职业资格编号 ")
	private String OCCQUALIFICATIONCODE;

	//参加工作时间 WORKBEGINDATE
	@TableField("WORKBEGINDATE")
	@Schema(name="参加工作时间 ")
	private String WORKBEGINDATE;

	//电子邮件 PERMAIL
	@TableField("PERMAIL")
	@Schema(name="电子邮件 ")
	private String PERMAIL;

	//HOMETEL 家庭电话
	@TableField("HOMETEL")
	@Schema(name="家庭电话 ")
	private String HOMETEL;

	//HOMEADD  家庭地址
	@TableField("HOMEADD")
	@Schema(name="家庭地址 ")
	private String HOMEADD;

	//邮政编码 ZIPCODE
	@TableField("ZIPCODE")
	@Schema(name="邮政编码 ")
	private String ZIPCODE;

	//BLOODTYPECODE 血型编码
	@TableField("BLOODTYPECODE")
	@Schema(name="血型编码 ")
	private String BLOODTYPECODE;

	//BLOODTYPENAME 血型中文
	@TableField("BLOODTYPENAME")
	@Schema(name="血型中文 ")
	private String BLOODTYPENAME;

	//BELONGORG 所属组织
	@TableField("BELONGORG")
	@Schema(name="所属组织 ")
	private String BELONGORG;

	//BELONGORGNUMBER 所属组织编码
	@TableField("BELONGORGNUMBER")
	@Schema(name="所属组织编码 ")
	private String BELONGORGNUMBER;

	//USEDNAME 曾用名
	@TableField("USEDNAME")
	@Schema(name="曾用名 ")
	private String USEDNAME;

	//RETIREDDATE 离退休日期
	@TableField("RETIREDDATE")
	@Schema(name="离退休日期 ")
	private String RETIREDDATE;

	//国际地区编号 PERCOUNTRYCODE
	@TableField("PERCOUNTRYCODE")
	@Schema(name="国际地区编号 ")
	private String PERCOUNTRYCODE;

	//国际/地区名中文 PERCOUNTRYNAME
	@TableField("PERCOUNTRYNAME")
	@Schema(name="国际/地区名中文 ")
	private String PERCOUNTRYNAME;

	//MARRIAGEDDATE 结/离婚日期
	@TableField("MARRIAGEDDATE")
	@Schema(name="结/离婚日期")
	private String MARRIAGEDDATE;

	//户口所在地编号 PERMANRESIDECODE
	@TableField("PERMANRESIDECODE")
	@Schema(name="户口所在地编号 ")
	private String PERMANRESIDECODE;

	//PERMANRESIDENAME 户口所在地中文
	@TableField("PERMANRESIDENAME")
	@Schema(name="户口所在地中文 ")
	private String PERMANRESIDENAME;

	//档案所在地 FILEADDRESS
	@TableField("FILEADDRESS")
	@Schema(name="档案所在地 ")
	private String FILEADDRESS;
	//传真 PERFAX
	@TableField("PERFAX")
	@Schema(name="传真 ")
	private String PERFAX;

	//JOINPOLITYDATE 入党(团)日期
	@TableField("JOINPOLITYDATE")
	@Schema(name="入党(团)日期 ")
	private String JOINPOLITYDATE;
	//DIEDATE 身故日期
	@TableField("DIEDATE")
	@Schema(name="身故日期")
	private String DIEDATE;
	//DIEREMARK 身故说明
	@TableField("DIEREMARK")
	@Schema(name="身故说明 ")
	private String DIEREMARK;

	//ISSYSTEMUSER 是否系统用户
	@TableField("ISSYSTEMUSER")
	@Schema(name="是否系统用户 ")
	private String ISSYSTEMUSER;

	//历史成员 ISHISKEYPSN
	@TableField("ISHISKEYPSN")
	@Schema(name="历史成员 ")
	private String ISHISKEYPSN;

	//门店营业员 ISSHOPASSIST
	@TableField("ISSHOPASSIST")
	@Schema(name="门店营业员 ")
	private String ISSHOPASSIST;

	//ISCADRE 是否干部
	@TableField("ISCADRE")
	@Schema(name="是否干部 ")
	private String ISCADRE;

	//ISHISLEADER 是否历史干部
	@TableField("ISHISLEADER")
	@Schema(name="是否历史干部 ")
	private String ISHISLEADER;

	//ISPARENTNAT 父母是否在本地
	@TableField("ISPARENTNAT")
	@Schema(name="父母是否在本地 ")
	private String ISPARENTNAT;

	//ISMATENAT 配偶是否在本地
	@TableField("ISMATENAT")
	@Schema(name="配偶是否在本地 ")
	private String ISMATENAT;

	//职业 PEROCCUPATION
	@TableField("PEROCCUPATION")
	@Schema(name="职业 ")
	private String PEROCCUPATION;

	//WORKTYPE 工种
	@TableField("WORKTYPE")
	@Schema(name="工种 ")
	private String WORKTYPE;
	//WORKADD 工作地点
	@TableField("WORKADD")
	@Schema(name="工作地点 ")
	private String WORKADD;
	//IDTYPECODE 证件类型编码
	@TableField("IDTYPECODE")
	@Schema(name="证件类型编码 ")
	private String IDTYPECODE;

	@Override
	public String toString() {
		return "TblStaff [staffid=" + staffid + ", realname=" + realname + ", username=" + username + ", status="
				+ status + ", pkYmStaffId=" + pkYmStaffId + ", orgname=" + orgname + ", SECRECTLEVELID="+secrectLevelId+",SECRECTLEVELNAME "+secrectLevelName+"]";
	}

}
