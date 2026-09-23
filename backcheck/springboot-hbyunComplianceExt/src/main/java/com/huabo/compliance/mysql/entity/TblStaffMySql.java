package com.huabo.compliance.mysql.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "TBL_STAFF")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="用户TBL_STAFF对象")
public class TblStaffMySql implements Serializable {
    public final static Integer USER_DISABLE = 0;//禁用
    public final static Integer USER_ENBLE = 1;//启用

    public final static String REGISTERUSERPASSWORD = "REDACTED";

    @Id
    @TableId("STAFFID")
    @Schema(name = "主键ID,自动增长")
    private BigDecimal staffid;//主键ID,自动增长
    @TableField("REALNAME")
    @Column(name = "REALNAME")
    @Schema(name = "真实名字")
    private String realname;//真实名字
    @TableField("FIXEDPHONE")
    @Column(name = "FIXEDPHONE")
    @Schema(name = "固定电话")
    private String fixedphone;//固定电话
    @TableField("ADDRESS")
    @Schema(name = "地址")
    private String address;//地址
    @TableField("EMAIL")
    @Schema(name = "邮箱")
    private String email;//邮箱
    @TableField("MIBLEPHONE")
    @Column(name = "MIBLEPHONE")
    @Schema(name = "手机号码")
    private String miblephone;//手机号码
    @TableField("MEMO")
    @Column(name = "MEMO")
    @Schema(name = "备注")
    private String memo;//备注
    @TableField("USERNAME")
    @Schema(name = "用户名（登录名）")
    private String username;//用户名（登录名）
    @TableField("PASSWORD")
    @Schema(name = "密码")
    private String password;//密码
    @TableField("JOBID")
    @Schema(name = "岗位ID")
    private BigDecimal jobid;//岗位ID
    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "创建时间")
    private Date createDate;
    @TableField("STATUS")
    @Schema(name = "状态（1启用，0弃用）")
    private Integer status;//状态（1启用，0弃用）
    @TableField("ORGID")
    @Schema(name = "组织Id")
    private BigDecimal orgid;//组织Id
    @TableField("ROLEID")//
    @Schema(name = "角色id,已作废")
    private BigDecimal roleid;
    @TableField("OUTSIDEID")
    @Schema(name = "标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源")
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @TableField("OUTSIDEOPENID")
    @Schema(name = "外部同步企业来源Id")
    private String outSideOpenId; //外部同步企业来源Id

    @Column(name = "ROLEIDSTRS")
    @Schema(name = "多个角色主键，用逗号分割 示例：1,2,3,4")
    private String roleIdStrs;

    @Column(name="PKYMSTAFFID")
	@TableField("PKYMSTAFFID")
	@Schema(name="低代码平台主键ID",hidden=true)
	private String pkYmStaffId;
    
    @Transient
    private String jobName;
    @Transient
    private String orgFatherName;
    @Transient
    private String rn;
    @Transient
    private String checked;
    @Transient
    @Schema(name = "角色名，用逗号分隔")
    private String roleNames;

    @Transient
    @Schema(name = "公司名称")
    private String orgname;

    @Transient
    private TblRoleMySql trole;
    @Transient
    private TblOrganizationMySql linkDetp;//用户隶属的部门
    @Transient
    private TblOrganizationMySql currentOrg;//用户当前所在的公司
    @Transient
    private TblOrganizationMySql linkOrg;//用户隶属的公司
    @Transient
    private TblLoginTypeMySql loginType;//登录页面信息
    @Transient
    private TblOrganizationMySql tblOrganization;

    @Transient
    private TblExternalExpertMySql tblExternalExpert;

    //	@JSONField(
//			serialize = false
//	)
//	@Transient
//	private Set<TblManageRight> tblManageRights = new HashSet(0);
//
//	@JSONField(serialize = false)
    public TblOrganizationMySql getTblOrganizationMySql() {
        return this.tblOrganization;
    }

    @JSONField(serialize = false)
    private Set<TblManageRightMySql> tblManageRightMySqls = new HashSet(0);

    @Transient
    private TblRoleMySql tblRole;//角色
}
